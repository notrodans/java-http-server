package com.github.notrodans.http.server.bind;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.github.notrodans.http.server.request.RequestContext;
import com.github.notrodans.http.server.response.ResponseContext;

final public class HandlerHolder {
	private final List<Object> handlers = new ArrayList<>();
	private final List<HandlerMethod> handlerMethods = new ArrayList<>();

	public List<HandlerMethod> getHandlerMethods() {
		return handlerMethods;
	}

	public HandlerHolder(final List<Object> handlers) {
		this.handlers.addAll(handlers);
		collectHandlerMethods();
	}

	private void collectHandlerMethods() {
		handlers.forEach(handler -> {
			final Class<?> handlerType = handler.getClass();
			final Method[] methods = handlerType.getDeclaredMethods();
			for (final Method method : methods) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					final Class<?>[] parameterTypes = method.getParameterTypes();
					if (parameterTypes.length == 1 && parameterTypes[0] == RequestContext.class) {
						if (method.getReturnType() == ResponseContext.class) {
							final var annotation = method.getAnnotation(RequestMapping.class);
							handlerMethods
									.add(
										new HandlerMethod(
											handler,
											annotation.path(),
											method,
											annotation.method()
										)
									);
						}
					}
				}
			}
		});
	}
}
