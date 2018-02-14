package gash.mq.rabbit;

import gash.mq.rabbit.core.MQueueConsumer;
import gash.mq.rabbit.core.MQueueFactory;
import gash.mq.rabbit.core.MQueueProducer;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabbitmq.client.AMQP;

public class MQPostFactoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testPost() throws Exception {
		MQueueFactory factory = new MQueueFactory("localhost", AMQP.PROTOCOL.PORT, "test", "test");
		MQueueProducer queue = factory.createProducer("testing");
		queue.post("hello I am a message");
		System.out.println("message sent");
		// Thread.sleep(200000);
	}

	@Test
	public void testRetrieve() throws Exception {
		MQueueFactory factory = new MQueueFactory("localhost", AMQP.PROTOCOL.PORT, "test", "test");
		MQueueConsumer queue = factory.createConsumer("testing");

		List<String> list = queue.retrieve();
		for (String msg : list)
			System.out.println("msg: " + msg);
	}

}
