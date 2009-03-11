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

import java.io.Serializable;


public class PentahoDataSource implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String name;
  private int maxActConn;
  private String driverClass;
  private int idleConn;
  private String userName;
  private String password;
  private String url;
  private String query;
  private long wait;

  public PentahoDataSource() {

  }

  public PentahoDataSource(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getQuery() {
    return this.query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getMaxActConn() {
    return this.maxActConn;
  }

  public void setMaxActConn(int maxActConn) {
    this.maxActConn = maxActConn;
  }


  public long getWait() {
    return this.wait;
  }

  public void setWait(long wait) {
    this.wait = wait;
  }

  public boolean equals(Object o) {
    return ((o instanceof PentahoDataSource) ? this.name.equals(((PentahoDataSource) o).getName()) : false);
  }

  public int hashCode() {
    return name.hashCode();
  }

  public int getIdleConn() {
    return this.idleConn;
  }

  public void setIdleConn(int idleConn) {
    this.idleConn = idleConn;
  }

  public String getDriverClass() {
    return this.driverClass;
  }

  public void setDriverClass(String driverClass) {
    this.driverClass = driverClass;
  }

}
