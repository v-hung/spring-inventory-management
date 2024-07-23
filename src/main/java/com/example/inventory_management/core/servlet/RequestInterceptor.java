package com.example.inventory_management.core.servlet;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.inventory_management.core.manager.RequestScopeInfoMgr;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor extends CoreInterceptor {
  @Override
  public boolean preHandle(
    @SuppressWarnings("null") HttpServletRequest request, 
    @SuppressWarnings("null") HttpServletResponse response, 
    @SuppressWarnings("null") Object handler
  ) throws Exception {

    if (!isActionControllerClass(handler)) {
			return true;
		}

    initializeThreadLocal();

    return true;
  }
  
  
  @Override
  public void postHandle(
    @SuppressWarnings("null") HttpServletRequest request, 
    @SuppressWarnings("null") HttpServletResponse response, 
    @SuppressWarnings("null") Object handler,
    @SuppressWarnings("null") ModelAndView modelAndView
  ) throws Exception {


    removeThreadLocal();
  }

  private void initializeThreadLocal() {
		RequestScopeInfoMgr.init();

	}

	private void removeThreadLocal() {
		RequestScopeInfoMgr.remove();
    
	}
}
