package glasshousemanager;

import glasshousemanager.commands.Close;
import glasshousemanager.commands.Open;
import glasshousemanager.commands.WaterOff;
import glasshousemanager.commands.WaterOn;
import glasshousemanager.messaging.Publisher;
import glasshousemanager.messaging.Subscriber;
import glasshousemanager.rules.Rule;
import glasshousemanager.rules.Rules;
import glasshousemanager.specifications.business.CloseTheGlassHouse;
import glasshousemanager.specifications.business.LaunchWatering;
import glasshousemanager.specifications.business.OpenTheGlassHouse;
import glasshousemanager.specifications.business.StopWatering;
import org.json.JSONObject;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.management.*;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;

/**
 * Classe recevant les informations de la station météo (composant situé sur la même machine)
 * les retraitant et émettant des messages à destination de la serre
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
    private final String brokerURL = "failover://tcp://localhost:61616";
    private LocalDateTime lastActionErrorTime;
    // Erreurs
    private String lastActionError = "";

    public GlassHouseController() throws MalformedObjectNameException {
        super("callback", "GlassHouseController");

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("WeatherChannelAgent:name=Lorient");

        WeatherChannelMBean mbean = MBeanServerInvocationHandler.newProxyInstance(
                mbs,    // MBeanServer
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
        publish("closeTheRoof");
    }

    @Override
    public void open() {
        publish("openTheRoof");
    }

    @Override
    public void waterOn() {
        publish("waterOn");
    }

    @Override
    public void waterOff() {
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
        if (notification.getType().equals("json")) {
            if (!notification.getMessage().equals(lastReceivedJSON)) {
                if (parseJSON(notification.getMessage())) {
                    this.lastReceivedJSON = notification.getMessage();
                    checkForActions();
                }
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
        }
        catch(Exception e){}

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
        Publisher pub=null;
        try {
            pub = new Publisher(this.brokerURL, "action");
            pub.publish(message);
        }
        catch(Exception e){
            lastActionError = message + " => " + e.getMessage();
            lastActionErrorTime = LocalDateTime.now();
        }
        finally {
            try {
                if (pub != null) {
                    pub.close();
                }
            } catch (JMSException e) {}
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String callback = ((TextMessage) message).getText();

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
                        if (callback.startsWith("error")){
                            this.lastActionError = callback;
                            this.lastActionErrorTime = LocalDateTime.now();
                        }
                }
            }
        }
        catch(Exception e){}
    }
}
