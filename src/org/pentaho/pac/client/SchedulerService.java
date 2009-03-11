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

import org.pentaho.pac.common.SchedulerServiceException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SchedulerService extends RemoteService, ISchedulerService {
  
  public void deleteJob( String jobName, String jobGroup ) throws SchedulerServiceException;
  public void executeJob( String jobName, String jobGroup ) throws SchedulerServiceException;
  public boolean isSchedulerPaused() throws SchedulerServiceException;
  public void pauseAll() throws SchedulerServiceException;
  public void pauseJob( String jobName, String jobGroup ) throws SchedulerServiceException;
  public void resumeAll() throws SchedulerServiceException;
  public void resumeJob( String jobName, String jobGroup ) throws SchedulerServiceException;
}
