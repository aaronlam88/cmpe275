package gash.mq.rabbit;

import gash.mq.rabbit.core.MQueueFactory;
import gash.mq.rabbit.core.MQueuePublisher;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabbitmq.client.AMQP;

/**
 * These tests are for producing messages for the SubscribeApp. The demo
 * application uses binding
 * 
 * @author gash
 * 
 */
public class MQueuePublisherTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Simple one off message - sanity check.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPublish() throws Exception {
		MQueueFactory factory = new MQueueFactory("localhost", AMQP.PROTOCOL.PORT, "test", "test");
		MQueuePublisher pub = factory.createPublisher("pubsub-demo");
		pub.publish("hello I am a message");
		System.out.println("message sent");
	}

	/**
	 * test to verify the use of a routing key (binding key on receiver) to
	 * selectively manage messages
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPublishUsingBinding() throws Exception {

		// routing (binding)
		String[] bindings = { "hello.info", "hello.error", "hello.sub.info", "hello.sub.error" };

		// in a robust application/client this information would be passed to
		// the the factory from a configuration (e.g., from a file).
		String host = "localhost";
		String user = "test";
		String passwd = "test";

		MQueueFactory factory = new MQueueFactory(host, AMQP.PROTOCOL.PORT, user, passwd);
		MQueuePublisher pub = factory.createPublisher("pubsub-demo");

		Random rand = new Random();
		for (int i = 0, I = 10; i < I; i++) {
			// randomly select a binding to send the message to - note for
			// repeatable testing, we would now want to use randomly generated
			// bindings as each test would be different and the resulting
			// verification more complex
			int n = rand.nextInt(bindings.length);

			// incorporate the binding into the message for visual validation
			pub.publish("hello I am " + (i + 1) + " of " + I + ": this is a " + bindings[n] + " message", bindings[n]);
			Thread.sleep(1000);
		}

		System.out.println("\n * messages sent *");
	}
}
