package gash.messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class Node<M extends Message> extends Thread {
	protected int nodeId;
	protected List<M> inbox;
	private Random rand;
	private int delayFactor = 0;
	private boolean forever = true;

	public Node(int id) {
		inbox = Collections.synchronizedList(new ArrayList<M>());
		nodeId = id;

		rand = new Random(System.currentTimeMillis() + id * 10);
	}

	public abstract void process(M msg);

	/**
	 * Simulate work latency variation. A n = 0 is no variation and n = 10 is
	 * maximum variation.
	 * 
	 * @param n,
	 *            variation 0 - 10;
	 */
	public void setDelayFactor(int n) {
		if (n > 10)
			n = 10;
		if (n < 0)
			n = 0;

		delayFactor = n;
	}

	public void message(M msg) {
		if (msg != null) {
			msg.incrementHops(nodeId);
			getInbox().add(msg);
		}
	}

	public void shutdown() {
		forever = false;
	}

	private boolean simulateDelay() {
		int n = rand.nextInt(15); // must be bigger than the latency factor.

		// change this break point to simulate latency pressure in
		// processing vs incoming messages.

		// a delay factor of 4 means we have weighted processing in favor of
		// delaying by ~25%
		try {
			sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public void run() {

		while (forever) {
			try {
				if (getInbox().size() == 0) {
					sleep(100);
				} else if (simulateDelay()) {
					// System.out.println("<node.run() processing a message>");
					M msg = getInbox().remove(0);
					process(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("** node " + nodeId + " shutting down **");
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int id) {
		this.nodeId = id;
	}

	public List<M> getInbox() {
		return inbox;
	}

	public void setInbox(List<M> inbox) {
		this.inbox = inbox;
	}
}