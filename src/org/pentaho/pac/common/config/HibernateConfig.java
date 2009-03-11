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
package org.pentaho.pac.common.config;

import java.io.Serializable;

public class HibernateConfig implements IHibernateConfig, Serializable {

  String dbDriver;
  String dbUrl;
  String dialect;
  String password;
  String userId;
  Integer connectionPoolSize;
  public HibernateConfig() { 
  }
  public HibernateConfig(IHibernateConfig hibernateConfig) {
    setDbDriver(hibernateConfig.getDbDriver());
    setDbUrl(hibernateConfig.getDbUrl());
    setDialect(hibernateConfig.getDialect());
    setPassword(hibernateConfig.getPassword());
    setUserId(hibernateConfig.getUserId());
    setConnectionPoolSize(hibernateConfig.getConnectionPoolSize());
  }
  public String getDbDriver() {
    return dbDriver;
  }
  public void setDbDriver(String dbDriver) {
    this.dbDriver = dbDriver;
  }
  public String getDbUrl() {
    return dbUrl;
  }
  public void setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
  }
  public String getDialect() {
    return dialect;
  }
  public void setDialect(String dialect) {
    this.dialect = dialect;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public Integer getConnectionPoolSize() {
    return connectionPoolSize;
  }
  public void setConnectionPoolSize(Integer connectionPoolSize) {
    this.connectionPoolSize = connectionPoolSize;
  }
  
}
