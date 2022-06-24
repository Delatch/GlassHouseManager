package glasshousemanager.messaging;

import glasshousemanager.utils.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;

public class Subscriber {
    private String topicName;
    private String subscriberName;
    private Context context;
    private TopicSession session;
    private TopicConnection connection;
    private MessageConsumer subscriber;

    /**
     * Un souscripteur
     *
     * @param brokerURL         : adresse du serveur de messagerie (ActiveMQ)
     * @param topicName         : nom du topic(sujet) auquel souscrire
     * @param subscriberName    : nom à affecter au souscripteur
     */
    public Subscriber(String brokerURL, String topicName, String subscriberName) {
        try {
            this.topicName = topicName;
            this.subscriberName = subscriberName;

            brokerURL = Helper.formatBrokerURL(brokerURL);

            context = Helper.getContext(brokerURL, topicName);
            connection = Helper.initTopicConnection(context, subscriberName);
            session = Helper.initTopicSession(connection);
            initSubscription();

        } catch (NamingException e) {
//            Logger.log(e.getMessage());
            e.printStackTrace();
        } catch (JMSException e) {
//            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    private void initSubscription() throws NamingException, JMSException {
        Topic topic = (Topic) context.lookup(topicName);
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
        if (message != null) {
            Logger.log(message.toString());
        }
    }
}

