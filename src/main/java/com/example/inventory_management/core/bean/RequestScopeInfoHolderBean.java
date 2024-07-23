package com.example.inventory_management.core.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestScopeInfoHolderBean {

  private String eventId;

  private MessageHolderBean messageHolderBean = new MessageHolderBean();
}
