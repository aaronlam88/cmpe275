package app;

import gash.mq.rabbit.core.MQueueFactory;
import gash.mq.rabbit.core.MQueueProducer;

public class LogClient {
	MQueueFactory factory;

	public LogClient(String host, int port, String user, String password) {
		factory = new MQueueFactory(host, port, user, password);
	}

	public void sendMessage(String message) {
		try {
			MQueueProducer queue = factory.createProducer("testing");
			queue.post("hello I am a message");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
