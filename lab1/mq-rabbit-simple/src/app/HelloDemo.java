package app;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class HelloDemo {
	public final static String QUEUE_NAME = "hello";

	public void demo() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);
		while (true) {
			// blocking
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();

			String message = new String(delivery.getBody());
			System.out.println(" [x] Received '" + message + "'");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HelloDemo demo = new HelloDemo();
			demo.demo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
