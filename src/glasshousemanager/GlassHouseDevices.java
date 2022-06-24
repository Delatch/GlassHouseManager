package glasshousemanager;

import glasshousemanager.messaging.*;
import glasshousemanager.utils.Logger;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Classe représentant la serre
 * Reçoit les actions et renvoie des callback d'état
 */
public class GlassHouseDevices extends Subscriber implements MessageListener {
    private final String brokerURL;
    private Publisher callbackPublisher;
    private static final String TOPIC_NAME = "action";
    private static final String SUBSCRIBER_NAME = "GlassHouseDevices";

    /**
     * Constructeur par défaut, utilise l'adresse par défaut du serveur de messagerie
     */
    public GlassHouseDevices(){
        this(Helper.DEFAULT_BROKER_URL);
    }

    /**
     * @param brokerURL : adresse du serveur de messagerie
     */
    public GlassHouseDevices(String brokerURL) {
        super(brokerURL, TOPIC_NAME, SUBSCRIBER_NAME);

        this.brokerURL = brokerURL;
        try {
            getSubscriber().setMessageListener(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                Logger.log("Action received : " + ((TextMessage) message).getText());
                handleMessage(((TextMessage) message).getText());
            }
        } catch (Exception e) {
            Logger.log("Caught:" + e);
            e.printStackTrace();
        }
    }

    private void handleMessage(String action) throws Exception {
        switch (action) {
            case "openTheRoof":
                publish("Roof_is_opened");
                break;
            case "closeTheRoof":
                publish("Roof_is_closed");
                break;
            case "waterOn":
                publish("Water_is_on");
                break;
            case "waterOff":
                publish("Water_is_off");
                break;
            default:
                publish("error : Unknown action !");
                throw new RuntimeException("Unknown action !");
        }
    }

    private void publish(String message) {
        try {
            if(this.callbackPublisher == null)
                this.callbackPublisher = new Publisher(this.brokerURL, "callback");

            this.callbackPublisher.publish(message);
            Logger.log("Callback sent ! (" + message + ")");
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        GlassHouseDevices devices = new GlassHouseDevices();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
