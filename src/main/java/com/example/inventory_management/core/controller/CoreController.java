package com.example.inventory_management.core.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.inventory_management.core.bean.MessageBean;
import com.example.inventory_management.core.constant.CoreConst;
import com.example.inventory_management.core.manager.RequestScopeInfoMgr;
import com.example.inventory_management.core.utils.CoreUtils;

@Controller
public abstract class CoreController<T extends CoreForm> implements CoreConst {

  protected String redirect(String functionId, String eventId) {

		return "redirect:/" + functionId + "/" + eventId;

	}

  protected void verifyStrictly(T form) {

	}

  protected void validate(T form) {
    
	}

  @ModelAttribute
  protected abstract T getModelAttribute();

  protected void clearForm(T form) {
		BeanUtils.copyProperties(CoreUtils.createForm(this), form);

	}

  public String getFunctionId() {
    return CoreUtils.getFunctionId(this);
  }

  public String getScreenId() {
    return CoreUtils.getScreenId(this);
  }

  public String getEventId() {
    return RequestScopeInfoMgr.getEventId();
  }

  public void addSuccessMessage(String message, String ...params) {
    RequestScopeInfoMgr.getMessageHolderBean().getSuccessMessages().add(
      MessageBean.builder()
        .message(message)
        .params(params)
        .build()
    );
  }

  public void addErrorMessage(String fieldName, String message, String ...params) {
    RequestScopeInfoMgr.getMessageHolderBean().getErrorMessages().add(
      MessageBean.builder()
        .fieldName(fieldName)
        .message(message)
        .params(params)
        .build()
    );
  }

  public boolean hasErrors() {
    return !RequestScopeInfoMgr.getMessageHolderBean().getErrorMessages().isEmpty();
  }
}
