package com.example.inventory_management.core.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import com.example.inventory_management.core.annotation.AuthorityUpdateEvent;
import com.example.inventory_management.core.annotation.NoAuthorityRequiredEvent;
import com.example.inventory_management.core.constant.CoreConst;
import com.example.inventory_management.core.controller.CoreController;
import com.example.inventory_management.core.controller.CoreForm;
import com.example.inventory_management.core.manager.RequestScopeInfoMgr;
import com.example.inventory_management.core.utils.CoreUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Configuration
@RequiredArgsConstructor
public class ActionControllerAdvice {

  private final HttpServletRequest request;

  // @Around("target(com.example.inventory_management.core.controller.CoreController)")
  @Around("target(com.example.inventory_management.core.controller.CoreController) && " +
    "(@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
    "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
    "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
    "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
    "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
    "@annotation(org.springframework.web.bind.annotation.PatchMapping))"
  )
  private Object preHandleAction(ProceedingJoinPoint pjp) throws Throwable {
    Object result = null;

    try {
      Object target = pjp.getTarget();

      CoreController<?> actionController = (CoreController<?>)target;
      String methodName = pjp.getSignature().getName();

      String functionId = CoreUtils.getFunctionId(actionController);
      String screenId = CoreUtils.getScreenId(actionController);

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      Method method = CoreUtils.getMethod(actionController.getClass(), methodName);
      String eventAuthority = getEventAuthority(method);

      if (eventAuthority != CoreConst.NO_AUTHORITY) {
        String authorityToCheck = String.format("%s:%s", functionId, eventAuthority);
    
        if (authentication != null && authentication.isAuthenticated()) {
          boolean hasAuthority = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authorityToCheck::equals);
    
          if (!hasAuthority) {
            throw new SecurityException("User does not have the required authority: " + authorityToCheck);
          }
        } else {
          throw new SecurityException("User is not authenticated");
    
        }
      }

      // set RequestScopeInfoMgr
      RequestScopeInfoMgr.setEventId(CoreUtils.getEventId(request.getRequestURL()));
      
      // Validate if method has a BindingResult parameter
      CoreForm form = (CoreForm) pjp.getArgs()[0];

      if (!executeValidate(actionController, form)) {
        return CoreConst.SUCCESS;
      }

      result = pjp.proceed();

    } catch (Exception e) {
      result = exceptionHandle(pjp, e);
    }

    return result;
  }

  private String getEventAuthority(Method method) {

		String eventAuthority = "";
	
		if (method.getAnnotation(AuthorityUpdateEvent.class) != null) {
			eventAuthority = CoreConst.AUTHORITY_UPDATE;

		} else if (method.getAnnotation(NoAuthorityRequiredEvent.class) != null) {
      eventAuthority = CoreConst.NO_AUTHORITY;
      
    } else {
			eventAuthority = CoreConst.AUTHORITY_READ;
		}

		return eventAuthority;

	}

  private boolean executeValidate(CoreController<?> actionController, CoreForm form) {

		CoreUtils.invokeMethod(actionController, "validate", form);

    CoreUtils.invokeMethod(actionController, "verifyStrictly", form);

		return !actionController.hasErrors();
	}

  private Object exceptionHandle(ProceedingJoinPoint pjp, Exception e) {
    Object result = null;

    // result = null;

    return result;
  }
}
