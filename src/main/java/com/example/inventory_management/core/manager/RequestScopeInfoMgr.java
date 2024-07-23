package com.example.inventory_management.core.manager;

import com.example.inventory_management.core.bean.MessageHolderBean;
import com.example.inventory_management.core.bean.RequestScopeInfoHolderBean;

public class RequestScopeInfoMgr {
  protected static ThreadLocal<RequestScopeInfoHolderBean> thread = new ThreadLocal<RequestScopeInfoHolderBean>() {

		@Override
		protected RequestScopeInfoHolderBean initialValue() {

			return new RequestScopeInfoHolderBean();

		}
	};

  public static void init() {
    thread.set(new RequestScopeInfoHolderBean());
  }

  public static void remove() {
    thread.remove();
  }

  public static void setEventId(String eventId) {
    thread.get().setEventId(eventId);
  }

  public static String getEventId() {
    return thread.get().getEventId();
  }

  public static void setMessageHolderBean(MessageHolderBean messageHolderBean) {
    thread.get().setMessageHolderBean(messageHolderBean);
  }

  public static MessageHolderBean getMessageHolderBean() {
    return thread.get().getMessageHolderBean();
  }
}
