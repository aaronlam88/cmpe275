package gash.mq.rabbit.core;

import java.io.IOException;
import java.util.HashMap;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * note virtualHost must be unique across hosts
 * 
 * @author gash1
 * 
 */
public class MQueueFactory {
	private String host;
	private int port = AMQP.PROTOCOL.PORT; // default is 5672
	private transient String user;
	private transient String password;

	private HashMap<String, MQueueBase> queues = new HashMap<String, MQueueBase>();
	private Connection connection;
	private Channel channel;

	public MQueueFactory(String host, Integer port) {
		this.host = host;

		if (port != null)
			this.port = port;

		this.user = null;
		this.password = null;
	}

	public MQueueFactory(String host, Integer port, String user, String password) {
		this.host = host;

		if (port != null)
			this.port = port;

		this.user = user;
		this.password = password;
		init();
	}

	public void finalize() {
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		System.out.println("connecting to " + host + ", port " + port);

		ConnectionFactory factory = new ConnectionFactory();

		// restrict queue access
		// factory.setUsername(user);
		// factory.setPassword(password);

		// factory.setVirtualHost("/testing");

		factory.setHost(host);
		factory.setPort(port);

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
		} catch (IOException e) {
			throw new RuntimeException("unable to create connection", e);
		}
	}

	/**
	 * queue: writes messages
	 * 
	 * @param name
	 * @return
	 */
	public MQueueProducer createProducer(String name) {
		MQueueBase r = queues.get(name);
		if (r == null) {
			try {
				r = new MQueueProducer(channel, name);
				queues.put(name, r);
			} catch (Exception e) {
				r = null;
				e.printStackTrace();
			}
		} else if (r instanceof MQueueConsumer) {
			// may have already registered a consumer so we want to create a
			// producer using the same channel
			try {
				r = new MQueueProducer(r.channel, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return (MQueueProducer) r;
	}

	/**
	 * queue: reads messages
	 * 
	 * @param name
	 * @return
	 */
	public MQueueConsumer createConsumer(String name) {
		MQueueBase r = queues.get(name);
		if (r == null) {
			try {
				r = new MQueueConsumer(channel, name);
				queues.put(name, r);
			} catch (Exception e) {
				r = null;
				e.printStackTrace();
			}
		} else if (r instanceof MQueueProducer) {
			// may have already registered a producer so we want to create a
			// consumer using the same channel
			try {
				r = new MQueueConsumer(r.channel, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return (MQueueConsumer) r;
	}

	/**
	 * pub-sub: the publisher on a topic
	 * 
	 * @param name
	 * @return
	 */
	public MQueuePublisher createPublisher(String name) {
		MQueueBase r = queues.get(name);
		if (r == null) {
			try {
				r = new MQueuePublisher(channel, name);
				queues.put(name, r);
			} catch (Exception e) {
				r = null;
				e.printStackTrace();
			}
		} else if (r instanceof MQueueConsumer) {
			// may have already registered a consumer so we want to create a
			// producer using the same channel
			try {
				r = new MQueueProducer(r.channel, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return (MQueuePublisher) r;
	}

	/**
	 * pub-sub: the publisher on a topic
	 * 
	 * @param name
	 * @return
	 */
	public MQueueSubscriber createSubscriber(String name) {
		MQueueBase r = queues.get(name);
		if (r == null) {
			try {
				r = new MQueueSubscriber(channel, name);
				queues.put(name, r);
			} catch (Exception e) {
				r = null;
				e.printStackTrace();
			}
		} else if (r instanceof MQueueConsumer) {
			// may have already registered a consumer so we want to create a
			// producer using the same channel
			try {
				r = new MQueueProducer(r.channel, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return (MQueueSubscriber) r;
	}

}
