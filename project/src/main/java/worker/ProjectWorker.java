package worker;

import com.google.protobuf.Message;
import io.grpc.project.Header;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class ProjectWorker {

    private LinkedBlockingQueue<Message> incomingQueue;

    private LinkedBlockingQueue<Message> outgoingQueue;

    public ProjectWorker(LinkedBlockingQueue<Message> outgoingQueue, LinkedBlockingQueue<Message> incomingQueue) {
        this.outgoingQueue = outgoingQueue;
        this.incomingQueue = incomingQueue;
    }

    public void pickMessage() throws InterruptedException{
        Header message;
        while (!incomingQueue.isEmpty()) {
            message =(Header)incomingQueue.poll();
            dealWithMessage(message);
            finishMessage(message);
        }
    }

    public void dealWithMessage(Header message) {
         
    }

    public void finishMessage(Header message)throws InterruptedException{
         outgoingQueue.put(message);
    }
}
