package node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.protobuf.Message;

import client.ProjectClient;
import server.ProjectServer;
import worker.ProjectWorker;

/*
 * Node is wrapper class
 * 	it should start 1 Server and multiple Client depend on how many out connection it has
 * 	                 					     /--> worker --\                         /--> otherNode/CLIENT
 * CLIENT ====> Node --> server/incomingQueue ---> worker ----> client/outgoingQueue	---> otherNode/CLIENT
 *                                            \--> worker --/                         \--> otherNode/CLIENT
 */
public class ProjectNode {
	// server: a ProjectServer to receive messages
	ProjectServer server;

	// workerPoll: using a workerPoll avoid create and destroy Worker when work is needed or done
	LinkedList<ProjectWorker> workerPoll;

	// incomingQueue: when server get a message, it will put the message in the incomingQueue
	LinkedBlockingQueue<Message> incomingQueue;

	// outgoingQueue: when worker is done processing a message and need to send out a respond
	// 		it will put the message in the outgoingQueue
	LinkedBlockingQueue<Message> outgoingQueue;

	// rountingTable: Node will keep track of all the outgoing edges using routingTable
	//		when a message need to be sent to a specific client, use this routingTable to get the client
	//		if you want to respond to a incoming connection after you done:
	//			create an id, and create a client, and put them in the routingTable
	//			make sure that worker will send respond to the correct id, after the work is done
	HashMap <Integer, ProjectClient> routingTable;

	public static void main(String[] args) {

	}

}
