/*
 * Copyright 2015, gRPC Authors All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.google.protobuf.Message;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.project.PingReply;
import io.grpc.project.PingRequest;
import io.grpc.project.SendPingGrpc;
import io.grpc.stub.StreamObserver;


/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class ProjectServer {
	private static final Logger logger = Logger.getLogger(ProjectServer.class.getName());

	private Server server;
	private LinkedBlockingQueue<Message> incomingQueue;

    public ProjectServer(LinkedBlockingQueue<Message> incomingQueue) {
        this.incomingQueue = incomingQueue;
    }

	private void start() throws IOException {
		/* The port on which the server should run */
		int port = 50051;
		server = ServerBuilder.forPort(port).addService(new SendPingImpl()).build().start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown
				// hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				ProjectServer.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	/**
	 * Main launches the server from the command line.
	 */
//	public static void main(String[] args) throws IOException, InterruptedException {
//		final ProjectServer server = new ProjectServer();
//		server.start();
//		server.blockUntilShutdown();
//	}

    private void moveToQueue(Message message) {
        try {
               incomingQueue.put(message);
           } catch (Exception e) {
               e.printStackTrace();
           }
    }


	static class SendPingImpl extends SendPingGrpc.SendPingImplBase {

		@Override
		public void ping (PingRequest req, StreamObserver<PingReply> responseObserver) {
			PingReply reply = PingReply.newBuilder().setMilliseconds(System.currentTimeMillis()).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		}
	}
}