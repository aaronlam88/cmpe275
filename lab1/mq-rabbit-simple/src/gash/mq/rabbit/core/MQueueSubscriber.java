package gash.mq.rabbit.core;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

/**
 * A pub-sub example: this represents the consumer of messages
 * 
 * @author gash
 * 
 */
public class MQueueSubscriber extends MQueueBase {
	private String tempQ;
	private List<MQueueListener> listeners = new ArrayList<MQueueListener>();

	MQueueSubscriber(Channel channel, String queue) throws Exception {
		super(channel);
		setQueueBasename(queue);

		init();
	}

	private void init() throws Exception {

		// declare message messages are to be sent to a general topic
		channel.exchangeDeclare(getExchange(), "topic");
		tempQ = channel.queueDeclare().getQueue();
	}

	public void addListener(MQueueListener listens) {
		if (listens != null)
			listeners.add(listens);
	}

	/**
	 * Topic is the binding key for filtering messages. Uses:
	 * <ul>
	 * <li>"#" - all messages
	 * <li>"*" - wildcard (e.g., *.error or db.*)
	 * </ul>
	 * 
	 * @param keyPattern
	 */
	public void addTopic(String keyPattern) {
		if (keyPattern == null)
			return;

		try {
			System.out.println("--> MQueueSubscriber adding key pattern: " + keyPattern);
			channel.queueBind(tempQ, getExchange(), keyPattern);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeTopic(String keyPattern) {
		if (keyPattern == null)
			return;

		try {
			channel.queueUnbind(tempQ, getExchange(), keyPattern);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * this is blocking: start receiving messages
	 * 
	 * @throws Exception
	 */
	public void subscribe() throws Exception {

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(tempQ, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			String topic = delivery.getEnvelope().getRoutingKey();

			// TODO should this be sequential processing
			// delivery.getProperties().getTimestamp();
			// delivery.getEnvelope().isRedeliver();
			for (MQueueListener ql : listeners)
				ql.onMessage(message, topic);
		}
	}
}
