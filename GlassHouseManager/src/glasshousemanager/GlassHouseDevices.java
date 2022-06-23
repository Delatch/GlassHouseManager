package glasshousemanager;

import glasshousemanager.messaging.Publisher;
import glasshousemanager.messaging.Subscriber;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class GlassHouseDevices extends Subscriber implements MessageListener {
    private String brokerURL;

    public GlassHouseDevices(){
        this("failover://tcp://192.168.1.53:61616");
    }
    public GlassHouseDevices(String brokerURL) {
        super("action", "GlassHouseDevices");

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
                handleMessage(((TextMessage) message).getText());
            }
        } catch (Exception e) {
            System.out.println("Caught:" + e);
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
            Publisher pub = new Publisher(this.brokerURL, "callback");
            pub.publish(message);
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        GlassHouseDevices devices = new GlassHouseDevices();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
