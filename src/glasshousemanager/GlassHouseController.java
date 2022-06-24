package glasshousemanager;

import glasshousemanager.commands.*;
import glasshousemanager.messaging.*;
import glasshousemanager.rules.*;
import glasshousemanager.specifications.business.*;
import glasshousemanager.utils.Logger;

import org.json.JSONObject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Classe recevant les informations de la station météo (composant situé sur la même machine)
 * les retraitant et émettant des messages à destination de la serre
 * Elle reçoit également des callbacks de la serre pour être informée de son état
 * Ces états, les seuils ainsi que les actions possibles sont exposés par un bean
 * pour management distant via client RMI, console ou navigateur web
 */
public class GlassHouseController extends Subscriber implements GlassHouseControllerMBean, NotificationListener, MessageListener {
    // données relatives au capteur
    private String lastReceivedJSON;
    private WeatherResult lastWeatherResult;

    // données relatives à la serre
    // Etats
    private String openingState = "Unknown";
    private String wateringState = "Unknown";
    // Seuils
    private int openingTemperature = 25;
    private int wateringTemperature = 30;
    // l'URL du broker de messagerie
    private static final String BROKER_URL = "failover://tcp://localhost:61616";

    private static final String CALLBACK_TOPIC_NAME = "callback";
    private static final String SUBSCRIBER_NAME = "GlassHouseController";
    private static final String DEFAULT_WEATHER_CHANNEL_ADDRESS = "localhost";
    private static final String DEFAULT_CITY = "Lorient";

    // Erreurs
    private LocalDateTime lastActionErrorTime;
    private String lastActionError = "";

    private Publisher actionPublisher;

    /**
     * Constructeur par défaut
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    public GlassHouseController() throws MalformedObjectNameException, IOException {
        this(DEFAULT_WEATHER_CHANNEL_ADDRESS, DEFAULT_CITY);
    }

    /**
     * @param weatherChannelAddress             : adresse de la station météo
     * @param city                              : ville dont on veut observer la météo
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    public GlassHouseController(String weatherChannelAddress, String city) throws MalformedObjectNameException, IOException {
        super(BROKER_URL, CALLBACK_TOPIC_NAME, SUBSCRIBER_NAME);

        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + weatherChannelAddress + ":9999/weatherchannel");
        JMXConnector connector = JMXConnectorFactory.connect(url);
        ObjectName name = new ObjectName("WeatherChannelAgent:name=" + city);

        WeatherChannelMBean mbean = MBeanServerInvocationHandler.newProxyInstance(
                connector.getMBeanServerConnection(),    // MBeanServer
                name,   // ObjectName
                WeatherChannelMBean.class, // interface
                false); // détaillé par la suite

        mbean.addNotificationListener(this, null, null);

        try {
            getSubscriber().setMessageListener(this);
        } catch (JMSException e) {
//            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public double getTemperature() {
        try {
            return this.lastWeatherResult.getTemp();
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public String getDescription() {
        return this.lastWeatherResult.getDescription();
    }

    @Override
    public boolean getRainingState() {
        return this.lastWeatherResult.getRainingState();
    }

    @Override
    public String getLastUpdateTime() {
        return this.lastWeatherResult.getLastUpdateTime();
    }

    @Override
    public int getOpeningTemperature() {
        return openingTemperature;
    }

    @Override
    public void setOpeningTemperature(int temp) {
        this.openingTemperature = temp;
        checkForActions();
    }

    @Override
    public String getOpeningState() {
        return this.openingState;
    }

    @Override
    public int getWateringTemperature() {
        return this.wateringTemperature;
    }

    @Override
    public void setWateringTemperature(int temp) {
        this.wateringTemperature = temp;
        checkForActions();
    }

    @Override
    public String getWateringState() {
        return this.wateringState;
    }

    @Override
    public void close() {
        Logger.log("closeTheRoof invoked !");
        publish("closeTheRoof");
    }

    @Override
    public void open() {
        Logger.log("openTheRoof invoked !");
        publish("openTheRoof");
    }

    @Override
    public void waterOn() {
        Logger.log("waterOn invoked !");
        publish("waterOn");
    }

    @Override
    public void waterOff() {
        Logger.log("waterOf invoked !");
        publish("waterOff");
    }

    @Override
    public String getLastActionError() {
        return this.lastActionError;
    }

    @Override
    public String getLastActionErrorTime() {
        if (this.lastActionErrorTime == null) return "";
        return String.valueOf(this.lastActionErrorTime);
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        Logger.log("Notification received ! (" + notification.getMessage().substring(0, 15) + "...)");
        if (notification.getType().equals("json")) {
            if (!notification.getMessage().equals(lastReceivedJSON)) {
                Logger.log("=> Notification different from the previous one.");
                if (parseJSON(notification.getMessage())) {
                    this.lastReceivedJSON = notification.getMessage();
                    checkForActions();
                }
            }
            else{
                Logger.log("=> Same content as the previous one. Unprocessed.");
            }
        }
    }

    /**
     * Méthode qui s'enquiert des actions à effectuer contextuellement aux infos météo reçues
     * Implémentation du patron Rule
     */
    private void checkForActions() {
        Rule<GlassHouseControllerMBean, Boolean> sprinklerOn = new Rule<>(new LaunchWatering(), new WaterOn());
        Rule<GlassHouseControllerMBean, Boolean> sprinklerOff = new Rule<>(new StopWatering(), new WaterOff());
        Rule<GlassHouseControllerMBean, Boolean> open = new Rule<>(new OpenTheGlassHouse(), new Open());
        Rule<GlassHouseControllerMBean, Boolean> close = new Rule<>(new CloseTheGlassHouse(), new Close());
        Rules<GlassHouseControllerMBean, Boolean> rules = new Rules<>();
        rules.add(sprinklerOn).add(sprinklerOff).add(open).add(close);

        try {
            rules.execute(this, null);
        } catch (Exception e) {
        }

    }

    private boolean parseJSON(String JSON) {
        try {
            WeatherResult wr = new WeatherResult();
            wr.populateFromSensorResponse(new JSONObject(JSON));
            this.lastWeatherResult = wr;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void publish(String message) {
        try {
            if(this.actionPublisher == null)
                this.actionPublisher = new Publisher(BROKER_URL, "action");

            this.actionPublisher.publish(message);
            Logger.log("Action sent ! (" + message + ")");
        } catch (Exception e) {
            lastActionError = message + " => " + e.getMessage();
            lastActionErrorTime = LocalDateTime.now();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String callback = ((TextMessage) message).getText();
                Logger.log("Callback received : " + callback);

                switch (callback) {
                    case "Roof_is_opened":
                        this.openingState = "Opened";
                        break;
                    case "Roof_is_closed":
                        this.openingState = "Closed";
                        break;
                    case "Water_is_on":
                        this.wateringState = "On";
                        break;
                    case "Water_is_off":
                        this.wateringState = "Off";
                        break;
                    default:
                        if (callback.startsWith("error")) {
                            this.lastActionError = callback;
                            this.lastActionErrorTime = LocalDateTime.now();
                        }
                }
            }
        } catch (Exception e) {
        }
    }
}
