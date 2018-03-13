package gash.messaging.transports;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import gash.messaging.Message;
import gash.messaging.transports.Scatter;

public class ScatterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testFloodLoad() throws Exception {
		Scatter myKafka = new Scatter(10);
		for (int n = 0; n < 1000; n++) {
			Message m = new Message(n);
			m.setOriginator(-999);
			m.setMessage("This is a message - " + n);

			myKafka.sendMessage(m);
		}

		Thread.sleep(20000);

		myKafka.printFinalStats();
	}
}
