package com.example.inventory_management.core.bean.web;

import com.example.inventory_management.core.bean.MessageHolderBean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ModelAttributeHolder {
  private String eventId;

  @Builder.Default
  private MessageHolderBean messageHolderBean = new MessageHolderBean();
}
