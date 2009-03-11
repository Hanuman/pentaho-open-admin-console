/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class PacServiceFactory {

  private static PacServiceAsync pacService = null;

  private static SchedulerServiceAsync schedulerService = null;
  private static SubscriptionServiceAsync subscriptionService = null;
  private static SolutionRepositoryServiceAsync solutionRepositoryService = null;
  private static JdbcDriverDiscoveryServiceAsync jdbcDriverDiscoveryService = null;
  private static HibernateConfigurationServiceAsync hibernateConfigurationService = null;
  

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

  public static SolutionRepositoryServiceAsync getSolutionRepositoryService() {
    if (solutionRepositoryService == null) {
      solutionRepositoryService = (SolutionRepositoryServiceAsync) GWT.create(SolutionRepositoryService.class);
      ServiceDefTarget endpoint = (ServiceDefTarget) solutionRepositoryService;
      String moduleRelativeURL = GWT.getModuleBaseURL() + "solutionrepositorysvc"; //$NON-NLS-1$
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return solutionRepositoryService;
  }
  
  public static JdbcDriverDiscoveryServiceAsync getJdbcDriverDiscoveryService() {
    if (jdbcDriverDiscoveryService == null) {
      jdbcDriverDiscoveryService = (JdbcDriverDiscoveryServiceAsync) GWT.create(JdbcDriverDiscoveryService.class);
      ServiceDefTarget endpoint = (ServiceDefTarget) jdbcDriverDiscoveryService;
      String moduleRelativeURL = GWT.getModuleBaseURL() + "jdbcdriverdiscoverysvc"; //$NON-NLS-1$
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return jdbcDriverDiscoveryService;
  }  

  public static HibernateConfigurationServiceAsync getHibernateConfigurationService() {
    if (hibernateConfigurationService == null) {
      hibernateConfigurationService = (HibernateConfigurationServiceAsync) GWT.create(HibernateConfigurationService.class);
      ServiceDefTarget endpoint = (ServiceDefTarget) hibernateConfigurationService;
      String moduleRelativeURL = GWT.getModuleBaseURL() + "hibernateconfigurationsvc"; //$NON-NLS-1$
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return hibernateConfigurationService;
  }  
}
