package com.github.notrodans.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.github.notrodans.http.server.handler.RequestHandler;
import com.github.notrodans.http.server.intercept.InterceptorHolder;
import com.github.notrodans.http.server.service.HandlerMethodResolver;

final public class Server {
	private final int port;
	private final ExecutorService executorService;
	private final HandlerMethodResolver handlerMethodResolver;
	private final InterceptorHolder interceptorHolder;

	public Server(final int port, final HandlerMethodResolver handlerMethodResolver,
		final InterceptorHolder interceptorHolder) {
		this.port = port;
		this.handlerMethodResolver = handlerMethodResolver;
		this.interceptorHolder = interceptorHolder;
		this.executorService = Executors.newFixedThreadPool(10);
	}

	public void start() {
		try (var serverSocket = new ServerSocket(port)) {
			serverSocket.setReuseAddress(true);
			while (true) {
				final var clientSocket = serverSocket.accept();
				System.out.println("accepted new connection");
				executorService
					.execute(new RequestHandler(clientSocket, handlerMethodResolver, interceptorHolder));
			}
		} catch (final IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
}
