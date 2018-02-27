package testKafka;

import gash.messaging.transports.Hub;

public class Test {

	public static void main(String[] args) throws Exception {
		Hub hub = new Hub();
		
		MsgQueue queue = new MsgQueue(1, hub);
		MsgBroker broker = new MsgBroker(2, hub);
		
		Consumer con3 = new Consumer(3, hub);
		Consumer con4 = new Consumer(4, hub);
		Consumer con5 = new Consumer(5, hub);
		
		broker.consumerId.add(3);
		broker.consumerId.add(4);
		broker.consumerId.add(5);
		
		Producer prod = new Producer(6, hub);
		
		hub.addNode(queue);
		hub.addNode(broker);
		
		hub.addNode(con3);
		hub.addNode(con4);
		hub.addNode(con5);
		
		hub.addNode(prod);
		
		hub.privateMessage(prod.getNodeId(), Producer.queueId, "from prod: test1");
		Thread.sleep((long)(Math.random() * 1000));
		hub.privateMessage(prod.getNodeId(), Producer.queueId, "from prod: test2");
		Thread.sleep((long)(Math.random() * 1000));
		hub.privateMessage(prod.getNodeId(), Producer.queueId, "from prod: test3");
		Thread.sleep((long)(Math.random() * 1000));
		hub.privateMessage(prod.getNodeId(), Producer.queueId, "from prod: test4");
	}

}
