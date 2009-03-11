/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
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
 *
 * Created  
 * @author Steven Barkdull
 */
package org.pentaho.pac.client.scheduler.model;

import java.io.Serializable;
import java.util.List;

/**
 * NOTE: I wanted this class to be a simple POJO, no getters/setters, just
 * public member data. In GWT 1.4, the compiler seems to choke when there are 
 * no setters/getters. When the getters/setters are added, GWT is happy. Based
 * on MD's experience, this requirement does NOT exist in GWT 1.5.
 * 
 * @author Steven Barkdull
 *
 */
public class Schedule implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 69L;
  
  private String triggerName = null;
  private String triggerGroup = null;
  private String triggerState = null;
  private String nextFireTime = null;
  private String prevFireTime = null;
  private String jobName = null;
  private String jobGroup = null;
  private String description = null;
  private String cronString = null;
  private String repeatCount = null;
  private String repeatInterval = null;
  private String startDate = null;
  private String endDate = null;
  private List<String> actionsList = null;
  
  // these properties are in subscription schedule only
  private String subscriberCount = null;
  private String schedId = null;
  private String title = null;
  
  public String getStartDate() {
    return startDate;
  }
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
  public String getEndDate() {
    return endDate;
  }
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
  public String getCronString() {
    return cronString;
  }
  public void setCronString(String cronString) {
    this.cronString = cronString;
  }
  public String getTriggerName() {
    return triggerName;
  }
  public void setTriggerName(String triggerName) {
    this.triggerName = triggerName;
  }
  public String getTriggerGroup() {
    return triggerGroup;
  }
  public void setTriggerGroup(String triggerGroup) {
    this.triggerGroup = triggerGroup;
  }
  public String getTriggerState() {
    return triggerState;
  }
  public void setTriggerState(String triggerState) {
    this.triggerState = triggerState;
  }
  public String getNextFireTime() {
    return nextFireTime;
  }
  public void setNextFireTime(String nextFireTime) {
    this.nextFireTime = nextFireTime;
  }
  public String getPrevFireTime() {
    return prevFireTime;
  }
  public void setPrevFireTime(String prevFireTime) {
    this.prevFireTime = prevFireTime;
  }
  public String getJobName() {
    return jobName;
  }
  public void setJobName(String jobName) {
    this.jobName = jobName;
  }
  public String getJobGroup() {
    return jobGroup;
  }
  public void setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getRepeatInterval() {
    return repeatInterval;
  }
  
  public void setRepeatInterval(String repeatInterval) {
    this.repeatInterval = repeatInterval;
  }
  
  public List<String> getActionsList() {
    return actionsList;
  }
  
  public void setActionsList(List<String> actionsList) {
    this.actionsList = actionsList;
  }

  public String getSubscriberCount() {
    return subscriberCount;
  }

  public void setSubscriberCount(String subscriberCount) {
    this.subscriberCount = subscriberCount;
  }

  public String getSchedId() {
    return schedId;
  }

  public void setSchedId(String schedId) {
    this.schedId = schedId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  public boolean isSubscriptionSchedule() {
    return schedId != null;
  }
  public String getRepeatCount() {
    return repeatCount;
  }
  public void setRepeatCount(String repeatCount) {
    this.repeatCount = repeatCount;
  }
  
  public boolean isRepeatSchedule() {
    return null != repeatInterval;
  }
  
  public boolean isCronSchedule() {
    return null != cronString;
  }
}