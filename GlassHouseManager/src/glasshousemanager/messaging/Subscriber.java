package glasshousemanager.messaging;

import org.apache.activemq.ActiveMQConnection;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class Subscriber {
    private String topicName;
    private String subscriberName;
    private Context contexte;
    private TopicConnectionFactory connectionFactory;
    private TopicSession session;
    private TopicConnection connection;
    private MessageConsumer subscriber;

    public Subscriber(String topicName, String subscriberName) {
        try {
            this.topicName = topicName;
            this.subscriberName = subscriberName;

            initContext();
            initConnection();
            initSession();
            initSubscription();

        } catch (NamingException e) {
//            Logger.log(e.getMessage());
            e.printStackTrace();
        } catch (JMSException e) {
//            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    private void initContext() throws NamingException {
        Hashtable<String, String> props;
        props = new Hashtable<String, String>();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.put(Context.PROVIDER_URL, ActiveMQConnection.DEFAULT_BROKER_URL);
        props.put("topic." + topicName, topicName);
        contexte = new InitialContext(props);
    }

    private void initConnection() throws NamingException, JMSException {
        connectionFactory = (TopicConnectionFactory) contexte.lookup("TopicConnectionFactory");
        connection = connectionFactory.createTopicConnection();
        System.out.println("Intialisation de la connection pour " + this.subscriberName + "." + this.topicName);
//        Logger.log(LocalDateTime.now() + "Inscription du ClientID " + this.subscriberName + "." + this.topicName);
        if (connection.getClientID() == null || !connection.getClientID().equals(this.subscriberName + "." + this.topicName)) {
            connection.setClientID(this.subscriberName + "." + this.topicName);
        }
        connection.start();
    }

    private void initSession() throws JMSException {
        session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private void initSubscription() throws NamingException, JMSException {
        Topic topic = (Topic) contexte.lookup(topicName);
        subscriber = session.createDurableSubscriber(topic, this.subscriberName);
    }

    public void close() throws JMSException {
        session.close();
        connection.close();
    }

    public MessageConsumer getSubscriber() {
        return subscriber;
    }
        public void onMessage(Message message) {
            if(message != null) {
                System.out.println((TextMessage)message);
            }
        }
//    }
}

