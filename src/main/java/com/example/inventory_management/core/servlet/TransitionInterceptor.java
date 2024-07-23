package com.example.inventory_management.core.servlet;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.inventory_management.core.bean.MessageHolderBean;
import com.example.inventory_management.core.bean.web.ModelAttributeHolder;
import com.example.inventory_management.core.constant.CoreConst;
import com.example.inventory_management.core.controller.CoreController;
import com.example.inventory_management.core.controller.CoreForm;
import com.example.inventory_management.core.manager.RequestScopeInfoMgr;
import com.example.inventory_management.core.utils.CoreUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TransitionInterceptor implements AsyncHandlerInterceptor {

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

    if (!isActionControllerClass(handler)) {
			return;
		}

    CoreController<?> actionController = (CoreController<?>)((HandlerMethod)handler).getBean();

    if (CoreConst.SUCCESS.equals(modelAndView.getViewName())) {
			modelAndView.setViewName(createViewName(actionController));
		}
    else if (CoreConst.BACK.equals(modelAndView.getViewName())) {
      modelAndView.setViewName("redirect:" + createViewName(actionController));
    }

    ModelAttributeHolder attributeHolder = ModelAttributeHolder.builder()
      .eventId(RequestScopeInfoMgr.getEventId())
      .messageHolderBean(RequestScopeInfoMgr.getMessageHolderBean())
      .build();

    modelAndView.addObject(CoreConst.MODEL_ATTRIBUTE_KEY, attributeHolder);

    // // add form to model
    // CoreForm form = (CoreForm)modelAndView.getModel().get(CoreUtils.getModelAttributeName(actionController));
    // modelAndView.addObject(CoreUtils.getModelAttributeName(actionController), form);

    removeThreadLocal();
  }

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

  private String createViewName(CoreController<?> actionController) {

		StringBuilder sb = new StringBuilder();
		sb.append("/");
		sb.append(CoreUtils.getFunctionId(actionController));
		sb.append("/");
		sb.append(CoreUtils.getScreenId(actionController));
		return sb.toString();

	}

  private void initializeThreadLocal() {
		RequestScopeInfoMgr.init();

	}

	private void removeThreadLocal() {
		RequestScopeInfoMgr.remove();
    
	}
}
