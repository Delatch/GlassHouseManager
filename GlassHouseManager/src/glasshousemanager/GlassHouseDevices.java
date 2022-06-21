package glasshousemanager;

import glasshousemanager.messaging.*;
import javax.jms.*;



public class GlassHouseDevices extends Subscriber implements MessageListener {

    public GlassHouseDevices() {
        super("action", "GlassHouseDevices");

        try {
            getSubscriber().setMessageListener(this);
        } catch (Exception e) {
//            Logger.log(e.getMessage());
        }
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("onMessage du GHSubscriber");
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
//                Logger.log("Received message=>"
//                        + textMessage.getText() + "' (" + LocalDateTime.now() + ")");
                handleMessage(((TextMessage) message).getText());
            }
        } catch (Exception e) {
            System.out.println("Caught:" + e);
            e.printStackTrace();
        }
    }

    private void handleMessage(String action) throws Exception {
        switch(action){
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

        }
    }

    private void publish(String message) {
        try {
            Publisher pub = new Publisher("callback");
            pub.publish(message);
        }
        catch(Exception e){

        }
    }

    public static void main(String args[]) throws InterruptedException {
        GlassHouseDevices ghd = new GlassHouseDevices();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
