package glasshousemanager.messaging;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;


public class Publisher {
    private String topicName;
    private Context context;
    private TopicSession session;
    private TopicConnection connection;

    /**
     * Un éditeur de messages
     * @param brokerURL     : adresse du serveur de messagerie (ActiveMQ)
     * @param topicName     : nom du topic(sujet) sur lequel publier
     */
    public Publisher(String brokerURL, String topicName) {
        try {
            this.topicName = topicName;

            brokerURL = Helper.formatBrokerURL(brokerURL);

            context = Helper.getContext(brokerURL, topicName);
            connection = Helper.initTopicConnection(context);
            session = Helper.initTopicSession(connection);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void publish(String messageContent) throws JMSException, NamingException {
        Topic topic = (Topic) context.lookup(topicName);
        TopicPublisher publisher = session.createPublisher(topic);
        TextMessage message = session.createTextMessage(messageContent);
        message.setJMSDeliveryMode(1); // Persistant

        publisher.publish(message);
    }

    public void close() throws JMSException {
        session.close();
        connection.close();
    }
}