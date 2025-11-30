package com.github.notrodans.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.github.notrodans.http.server.handler.RequestHandler;

public class Server {
	private final int port;

	public Server(int port) {
		this.port = port;
		this.executorService = Executors.newFixedThreadPool(10);
	}

	public void start() {
		try (var serverSocket = new ServerSocket(port)) {
			serverSocket.setReuseAddress(true);
			while (true) {
				var clientSocket = serverSocket.accept();
				System.out.println("accepted new connection");
				executorService.execute(new RequestHandler(clientSocket));
			}
		} catch (final IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}

	private final ExecutorService executorService;
}
