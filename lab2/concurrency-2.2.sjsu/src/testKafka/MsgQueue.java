package testKafka;

import java.util.concurrent.LinkedBlockingQueue;

import gash.messaging.Message;
import gash.messaging.Message.Delivery;
import gash.messaging.transports.Hub;
import gash.messaging.transports.Hub.SpokeNode;

public class MsgQueue extends SpokeNode {

	static int brokerId = 2;
	private Hub network;
	LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	public MsgQueue(int id, Hub network) {
		super(id, network);
		this.network = network;
	}

	@Override
	public void process(Message msg) {
		queue.add(msg);
		while (!queue.isEmpty()) {
			Message reply = queue.poll();
			reply.setOriginator(this.getNodeId());
			reply.setDestination(brokerId);
			reply.setDeliverAs(Delivery.Direct);
			network.sendMessage(reply);
		}
	}
}
