package com.example.inventory_management.core.servlet;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.example.inventory_management.core.controller.CoreController;

public class CoreInterceptor implements AsyncHandlerInterceptor {
  protected boolean isActionControllerClass(Object handler) {
		if (handler == null) {
			return false;
		}

		if (!(handler instanceof HandlerMethod)) {
			return false;
		}
    
		if (!(((HandlerMethod)handler).getBean() instanceof CoreController)) {
		  return false;
		}

		return true;
	}
}
