package server;

//import io.grpc.comm.Request;
//import io.grpc.comm.Response;
import com.cmpe275.grpcComm.*;


import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;

class GZipProcessor {
    public GZipProcessor() { }

    public BufferedReader getBufferedReader(String file_path) throws IOException {
        InputStream fileStream = new FileInputStream(file_path);
        InputStream gzipStream = new GZIPInputStream(fileStream);
        Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
        return new BufferedReader(decoder);
    }
}

public class TaskManager {
    GZipProcessor gZipProcessor;
    LinkedBlockingQueue<Request> incomming_queue;
    LinkedBlockingQueue<Response> outgoing_queue;


    public TaskManager(LinkedBlockingQueue<Request> incomming_queue, LinkedBlockingQueue<Response> outgoing_queue) {
        this.gZipProcessor = new GZipProcessor();
        this.incomming_queue = incomming_queue;
        this.outgoing_queue = outgoing_queue;
    }

}
