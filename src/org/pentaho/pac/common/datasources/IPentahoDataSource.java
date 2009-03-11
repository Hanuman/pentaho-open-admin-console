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
package org.pentaho.pac.common.datasources;


public interface IPentahoDataSource extends java.io.Serializable{

  public String getName();
  public int getMaxActConn();
  public String getDriverClass();
  public int getIdleConn();
  public String getUserName();
  public String getPassword();
  public String getUrl();
  public String getQuery();
  public long getWait();
  public void setName(String jndiName);
  public void setMaxActConn(int maxActConn);
  public void setDriverClass(String driverClass);
  public void setIdleConn(int idleConn);
  public void setUserName(String userName);
  public void setPassword(String password);
  public void setUrl(String url);
  public void setQuery(String query);
  public void setWait(long wait);
}
