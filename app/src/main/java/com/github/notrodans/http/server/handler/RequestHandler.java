package com.github.notrodans.http.server.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import com.github.notrodans.http.server.common.HttpStatus;
import com.github.notrodans.http.server.intercept.InterceptorHolder;
import com.github.notrodans.http.server.request.RequestContextFromReader;
import com.github.notrodans.http.server.response.ResponseContext;
import com.github.notrodans.http.server.response.ResponseFromString;
import com.github.notrodans.http.server.service.HandlerMethodResolver;

final public class RequestHandler implements Runnable {
	private final Socket clientSocket;
	private final HandlerMethodResolver handleMethodResolver;
	private final InterceptorHolder interceptorHolder;

	public RequestHandler(final Socket clientSocket, final HandlerMethodResolver handleMethodResolver,
			final InterceptorHolder interceptorHolder) {
		this.clientSocket = clientSocket;
		this.handleMethodResolver = handleMethodResolver;
		this.interceptorHolder = interceptorHolder;
	}

	@Override
	public void run() {
		try {
			final var inputStream = clientSocket.getInputStream();
			final var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			final var context = new RequestContextFromReader(bufferedReader).get();
			if (context == null) {
				System.out.println("Context is null");
				return;
			}
			final var os = clientSocket.getOutputStream();
			final var handlerMethod = handleMethodResolver.resolve(context);
			if (handlerMethod == null) {
				os.write(
					new ResponseFromString(HttpStatus.NOT_FOUND, null, null)
							.get()
							.getResponseAsBytes()
				);
				os.flush();
			} else {
				final ResponseContext responseContext = handlerMethod.invoke(context);
				if (responseContext.getStatus().isError()) {
					os.write(
						new ResponseFromString(responseContext.getStatus(), null, null)
								.get()
								.getResponseAsBytes()
					);
					os.flush();
				} else {
					interceptorHolder.beforeSendResponse(context, responseContext);
					os.write(responseContext.getResponseAsBytes());
					os.flush();
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException("Handler exception", e);
		} finally {
			try {
				clientSocket.close();
				System.out.println("Socket closed");
			} catch (final IOException e) {
				System.out.println("Exception trying to close socket");
			}
		}
	}
}
