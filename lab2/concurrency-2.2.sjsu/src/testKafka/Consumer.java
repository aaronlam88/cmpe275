package testKafka;

import gash.messaging.Message;
import gash.messaging.transports.Hub;
import gash.messaging.transports.Hub.SpokeNode;

public class Consumer extends SpokeNode {

	public Consumer(int id, Hub network) {
		super(id, network);
	}
	
	@Override
	public void process(Message msg) {
		System.out.println("Consumer [" + getNodeId() + "] get message: " + msg.getMessage());
	}

}
