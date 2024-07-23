package com.example.inventory_management.core.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHolderBean {
  @Builder.Default
  private List<MessageBean> successMessages = new ArrayList<MessageBean>();

  @Builder.Default
	private MessageBean warningMessage = new MessageBean();

  @Builder.Default
	private List<MessageBean> errorMessages = new ArrayList<MessageBean>();
}
