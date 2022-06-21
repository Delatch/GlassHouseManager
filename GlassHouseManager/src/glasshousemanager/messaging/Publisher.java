package glasshousemanager.messaging;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnection;

import java.util.Hashtable;


public class Publisher {
    private String topicName;
    private Context contexte;
    private TopicConnectionFactory connectionFactory;
    private TopicSession session;
    private TopicConnection connection;

//    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    public Publisher(String topicName) {
        try {
            this.topicName = topicName;
            initContext();
            initConnection();
            initSession();
        } catch (NamingException e) {
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
        props.put("topic." + this.topicName, this.topicName);
        contexte = new InitialContext(props);
    }

    private void initConnection() throws NamingException, JMSException {
        connectionFactory = (TopicConnectionFactory) contexte.lookup("TopicConnectionFactory");
        connection = connectionFactory.createTopicConnection();
        connection.start();
    }

    private void initSession() throws JMSException {
        session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void publish(String messageContent) throws JMSException, NamingException {
//        session.createTopic(topicName);
        Topic topic = (Topic) contexte.lookup(topicName);
        TopicPublisher publisher = session.createPublisher(topic);
        TextMessage message = session.createTextMessage(messageContent);
        message.setJMSDeliveryMode(1); // Persistant

        publisher.publish(message);
//        Logger.log("Sent message '" + message.getText() + "' (" + LocalDateTime.now() + ")");
    }

    public void close() throws JMSException {
        session.close();
        connection.close();
    }

    public static void main(String args[]) throws JMSException, NamingException {
        Publisher p = new Publisher("Température");
        p.publish("Encore encore plus chaud");
    }
}