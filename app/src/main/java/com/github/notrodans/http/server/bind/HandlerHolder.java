package com.github.notrodans.http.server.bind;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.github.notrodans.http.server.controller.ApplicationController;
import com.github.notrodans.http.server.request.RequestContext;
import com.github.notrodans.http.server.response.ResponseContext;

final public class HandlerHolder {
	private static volatile HandlerHolder INSTANCE;

	private final List<Class<?>> handlerTypeList = new ArrayList<>();
	private final List<HandlerMethod> handlerMethods = new ArrayList<>();

	public List<HandlerMethod> getHandlerMethods() {
		return handlerMethods;
	}

	private HandlerHolder() {
		handlerTypeList.add(ApplicationController.class);
		collectHandlerMethods();
	}

	private void collectHandlerMethods() {
		handlerTypeList.forEach(handlerType -> {
			final Method[] methods = handlerType.getDeclaredMethods();
			for (final Method method : methods) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					final Class<?>[] parameterTypes = method.getParameterTypes();
					if (parameterTypes.length == 1 && parameterTypes[0] == RequestContext.class) {
						if (method.getReturnType() == ResponseContext.class) {
							final var annotation = method.getAnnotation(RequestMapping.class);
							try {
								handlerMethods
									.add(
										new HandlerMethod(
											handlerType.getConstructor().newInstance(),
											annotation.path(), method, annotation.method()));
							} catch (InvocationTargetException | IllegalAccessException
								| InstantiationException | NoSuchMethodException e) {
								System.out
									.println(
										"Exception trying to create object for class: "
											+ handlerType.getName());
							}
						}
					}
				}
			}
		});
	}

	public static HandlerHolder getInstance() {
		if (INSTANCE == null) {
			synchronized (HandlerHolder.class) {
				if (INSTANCE == null) {
					INSTANCE = new HandlerHolder();
					return INSTANCE;
				}
			}
		}

		return INSTANCE;
	}
}
