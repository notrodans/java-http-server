package com.github.notrodans.http.server.service;

import com.github.notrodans.http.server.bind.HandlerHolder;
import com.github.notrodans.http.server.bind.HandlerMethod;
import com.github.notrodans.http.server.request.RequestContext;

final public class HandlerMethodResolver {
	private final HandlerHolder handlerHolder;

	public HandlerMethodResolver(final HandlerHolder handlerHolder) {
		this.handlerHolder = handlerHolder;
	}

	public HandlerMethod resolve(final RequestContext context) {
		return handlerHolder
			.getHandlerMethods()
			.stream()
			.filter(
				it -> context.getMethod() == it.getMethod()
					&& new PathPatternFromPath(it.getPath()).get().match(context.getPath()))
			.findFirst()
			.orElseGet(() -> {
				System.out.println("Handler by context not found: " + context);
				return null;
			});
	}
}
