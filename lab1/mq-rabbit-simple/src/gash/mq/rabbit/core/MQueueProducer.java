package gash.mq.rabbit.core;

import com.rabbitmq.client.Channel;

/**
 * encapsulation of a producer to a durable queue. This class helps to ensure
 * that we can control concurrent access to a queue. Only one thread should
 * access a queue at at time!
 * 
 * @author gash1
 * 
 */
public class MQueueProducer extends MQueueBase {

	MQueueProducer(Channel channel, String queue) throws Exception {
		super(channel);
		setQueueBasename(queue);

		init();
	}

	private void init() throws Exception {

		// whoa! shouldn't we use log4j here?!
		System.out.println("--> declaring: " + getExchange());

		// declare messages are to be sent directly (not fanout or topic)
		//channel.exchangeDeclare(getExchange(), "direct", true);

		channel.queueDeclare(getQueue(), false, false, false, null);

		channel.queueBind(getQueue(), getExchange(), getRouting());
	}

	public void post(String msg) throws Exception {
		synchronized (channel) {
			channel.basicPublish("", getQueue(), null, msg.getBytes());
		}
	}

	public void publish(String msg) throws Exception {
		synchronized (channel) {
			channel.basicPublish(getExchange(), getRouting(), null, msg.getBytes());
		}
	}
}