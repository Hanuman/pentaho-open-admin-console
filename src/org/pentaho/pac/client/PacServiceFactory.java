package org.pentaho.pac.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class PacServiceFactory {

  private static PacServiceAsync pacService = null;

  private static SchedulerServiceAsync schedulerService = null;
  private static SubscriptionServiceAsync subscriptionService = null;

  public static PacServiceAsync getPacService() {
    if (pacService == null) {
      pacService = (PacServiceAsync) GWT.create(PacService.class);
      ServiceDefTarget endpoint = (ServiceDefTarget) pacService;
      String moduleRelativeURL = GWT.getModuleBaseURL() + "pacsvc"; //$NON-NLS-1$
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return pacService;
  }

  public static SchedulerServiceAsync getSchedulerService() {
    if (schedulerService == null) {
      schedulerService = (SchedulerServiceAsync) GWT.create(SchedulerService.class);
      ServiceDefTarget endpoint = (ServiceDefTarget) schedulerService;
      String moduleRelativeURL = GWT.getModuleBaseURL() + "schedulersvc"; //$NON-NLS-1$
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return schedulerService;
  }

  public static SubscriptionServiceAsync getSubscriptionService() {
    if (subscriptionService == null) {
      subscriptionService = (SubscriptionServiceAsync) GWT.create(SubscriptionService.class);
      ServiceDefTarget endpoint = (ServiceDefTarget) subscriptionService;
      String moduleRelativeURL = GWT.getModuleBaseURL() + "subscriptionsvc"; //$NON-NLS-1$
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return subscriptionService;
  }
}
