package com.example.inventory_management.controller.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.inventory_management.core.controller.CoreController;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/product/")
@RequiredArgsConstructor
public class IndexController extends CoreController<IndexForm> {
  
  private static final String EVENT_ID_INDEX = "index";

  private static final String EVENT_ID_MINUS = "product_02";

  private static final String EVENT_ID_PLUS = "product_03";

  @Override
  @ModelAttribute
  protected IndexForm getModelAttribute() {
    return new IndexForm();
  }

  @RequestMapping(value = EVENT_ID_INDEX)
  public String doEventIndex(IndexForm form) {
    super.clearForm(form);

    return SUCCESS;
  }

  @RequestMapping(value = EVENT_ID_MINUS)
  public String doEventMinus(@ModelAttribute("indexForm") IndexForm form) {
    form.setA(100);

    return SUCCESS;
  }

  @RequestMapping(value = EVENT_ID_PLUS)
  public String doEventPlus(IndexForm form) {
    form.setA(form.getA() + 1);

    return SUCCESS;
  }
}
