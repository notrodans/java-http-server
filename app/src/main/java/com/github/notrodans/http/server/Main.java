package com.github.notrodans.http.server;

import java.util.List;
import com.github.notrodans.http.server.bind.HandlerHolder;
import com.github.notrodans.http.server.common.ApplicationParameters;
import com.github.notrodans.http.server.controller.ApplicationController;
import com.github.notrodans.http.server.intercept.InterceptorHolder;
import com.github.notrodans.http.server.service.HandlerMethodResolver;

final public class Main {
	public static void main(final String[] args) {
		final String directory =
			args.length > 1 && "--directory".equals(args[0])
				? args[1]
				: "/home/notrodans/java-http-server/tmp";

		final var applicationParameters = new ApplicationParameters(directory);
		final var applicationController = new ApplicationController(applicationParameters);
		final var handlerHolder = new HandlerHolder(List.of(applicationController));
		final var handlerMethodResolver = new HandlerMethodResolver(handlerHolder);
		final var interceptorHolder = new InterceptorHolder();

		final var server = new Server(8080, handlerMethodResolver, interceptorHolder);
		server.start();
	}
}
