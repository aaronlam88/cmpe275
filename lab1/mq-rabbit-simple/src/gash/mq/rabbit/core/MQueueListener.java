package gash.mq.rabbit.core;

public class MQueueListener {

	/**
	 * Override this method to provide application processing of a message
	 * 
	 * @param msg
	 */
	public void onMessage(String msg, String topic) {
		// TODO should be abstract but for demonstration we print the message
		System.out.println("MSG (" + topic + "): " + msg);
	}
}
