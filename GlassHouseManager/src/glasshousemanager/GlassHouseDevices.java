package glasshousemanager;

import glasshousemanager.messaging.Publisher;
import glasshousemanager.messaging.Subscriber;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class GlassHouseDevices extends Subscriber implements MessageListener {
    private final String brokerURL = "failover://tcp://192.168.1.53:61616";

    public GlassHouseDevices() {
        super("action", "GlassHouseDevices");

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
            case "openTheRoof" -> publish("Roof_is_opened");
            case "closeTheRoof" -> publish("Roof_is_closed");
            case "waterOn" -> publish("Water_is_on");
            case "waterOff" -> publish("Water_is_off");
            default -> {
                publish("error : Unknown action !");
                throw new RuntimeException("Unknown action !");
            }
        }
    }

    private void publish(String message) {
        try {
            Publisher pub = new Publisher(this.brokerURL, "callback");
            pub.publish(message);
        }
        catch(Exception e){

        }
    }
}
