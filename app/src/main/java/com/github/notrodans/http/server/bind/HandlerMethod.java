package com.github.notrodans.http.server.bind;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.github.notrodans.http.server.common.HttpMethod;
import com.github.notrodans.http.server.exception.HandlerException;

final public class HandlerMethod {
	private final Object handlerObject;
	private final HttpMethod method;
	private final String path;
	private final Method handler;

	public HandlerMethod(final Object handlerObject, final String path, final Method handler,
			final HttpMethod method) {
		this.handlerObject = handlerObject;
		this.method = method;
		this.path = path;
		this.handler = handler;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	@SuppressWarnings("unchecked")
	public <T> T invoke(final Object... args) {
		try {
			return (T) handler.invoke(handlerObject, args);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new HandlerException(e);
		}
	}
}
