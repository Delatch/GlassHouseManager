package glasshousemanager.messaging;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

/**
 * Classe mutualisant des méthodes utiles pour le messaging JMS via ActiveMQ
 * (Initialisation de contexte, de connexion, de session...)
 */
public class Helper {
    public static String DEFAULT_BROKER_URL;

    static {
        try {
            DEFAULT_BROKER_URL = "failover://tcp://"+ InetAddress.getLocalHost().getHostAddress() +":61616";
        } catch (UnknownHostException e) {
            DEFAULT_BROKER_URL = "failover://tcp://localhost:61616";
        }
    }

    public static Context getContext(String URL, String topicName) throws NamingException {
        Hashtable<String, String> props = new Hashtable<>();

        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
//        props.put(Context.PROVIDER_URL, ActiveMQConnection.DEFAULT_BROKER_URL);
        props.put(Context.PROVIDER_URL, URL);
        props.put("topic." + topicName, topicName);
        return new InitialContext(props);
    }

    public static TopicConnection initTopicConnection(Context context) throws NamingException, JMSException {
        return initTopicConnection(context, "");
    }

    public static TopicConnection initTopicConnection(Context context, String clientID) throws NamingException, JMSException {
        TopicConnectionFactory connectionFactory = (TopicConnectionFactory) context.lookup("TopicConnectionFactory");
        TopicConnection connection = connectionFactory.createTopicConnection();
        if(!clientID.isEmpty()){
            connection.setClientID(clientID);
        }
        connection.start();
        return connection;
    }

    public static TopicSession initTopicSession(TopicConnection connection) throws JMSException {
        return connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }
}
