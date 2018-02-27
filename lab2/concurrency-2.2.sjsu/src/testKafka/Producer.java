package testKafka;

import gash.messaging.transports.Hub;
import gash.messaging.transports.Hub.SpokeNode;

public class Producer extends SpokeNode {
	
	static int queueId = 1;
	
	public Producer(int id, Hub network) {
		super(id, network);
	}

}
