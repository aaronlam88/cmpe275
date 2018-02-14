package gash.mq.rabbit.demo;

import gash.mq.rabbit.core.MQueueFactory;
import gash.mq.rabbit.core.MQueueListener;
import gash.mq.rabbit.core.MQueueSubscriber;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.rabbitmq.client.AMQP;

/**
 * Application (consumer) example of an AMQP subscriber that can selectively
 * receive publications on a routing_key (binding)
 * 
 * @author gash
 * 
 */
public class SubscribeApp extends MQueueListener {
	private SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
	private MQueueSubscriber sub;

	SubscribeApp() {
		// in a robust application/server this information would be passed to
		// the factory from a configuration (e.g., from a file).
		String host = "localhost";
		String user = "test";
		String passwd = "test";

		MQueueFactory factory = new MQueueFactory(host, AMQP.PROTOCOL.PORT, user, passwd);
		sub = factory.createSubscriber("pubsub-demo");
		sub.addListener(this);
	}

	@Override
	public void onMessage(String msg, String topic) {
		// TODO should be abstract but for demonstration we print the message
		System.out.println("MSG(" + topic + ") at " + fmt.format(new Date()) + " - " + msg);
	}

	public void addBindingFilter(String v) {
		sub.addTopic(v);
	}

	public void demo() {

		// blocking
		try {
			sub.subscribe();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// banner
		System.out.print("** RabbitMQ pub-sub - Subscriber of ");
		for (String arg : args)
			System.out.print(arg + " ");
		System.out.println("**\n");

		SubscribeApp sa = new SubscribeApp();

		// a binding filter is in the form of '#.level' where '#' is a wildcard
		// and we are matching on the last part. Our example has binding of the
		// form: "hello.info", "hello.error", "hello.sub.info",
		// "hello.sub.error"
		for (String arg : args)
			sa.addBindingFilter(arg);

		sa.demo();
	}

}
