package gash.mq.rabbit.core;

import com.rabbitmq.client.Channel;

public class MQueuePublisher extends MQueueBase {

	MQueuePublisher(Channel channel, String queue) throws Exception {
		super(channel);
		setQueueBasename(queue);

		init();
	}

	private void init() throws Exception {

		// declare message messages are to be sent to a general topic
		channel.exchangeDeclare(getExchange(), "topic");
	}

	/**
	 * publish with no topic (binding)
	 * 
	 * @param msg
	 * @throws Exception
	 */
	public void publish(String msg) throws Exception {
		synchronized (channel) {
			channel.basicPublish(getExchange(), "", null, msg.getBytes());
		}
	}

	/**
	 * publish with no topic (binding)
	 * 
	 * @param msg
	 * @throws Exception
	 */
	public void publish(String msg, String topic) throws Exception {
		synchronized (channel) {
			channel.basicPublish(getExchange(), topic, null, msg.getBytes());
		}
	}
}
