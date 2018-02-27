package testKafka;

import java.util.HashSet;
import java.util.Random;

import gash.messaging.Message;
import gash.messaging.Message.Delivery;
import gash.messaging.transports.Hub;
import gash.messaging.transports.Hub.SpokeNode;

public class MsgBroker extends SpokeNode {
	Hub network;
	HashSet<Integer> consumerId = new HashSet<>();
	
	public MsgBroker(int id, Hub network) {
		super(id, network);
		this.network = network;
	}
	
	@Override
	public void process(Message msg) {
		Random generator = new Random();
		Object[] values = consumerId.toArray();
		int id = (int) values[generator.nextInt(values.length)];

		msg.setOriginator(this.getNodeId());
		msg.setDestination(id);
		msg.setDeliverAs(Delivery.Direct);
		network.sendMessage(msg);
	}

}
