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

public class EmailConfig implements IEmailConfig, Serializable {
  
  String password;
  boolean authenticate;
  boolean debug;
  String defaultFrom;
  String smtpHost;
  Integer smtpPort;
  String smtpProtocol;
  boolean useSsl;
  boolean useStartTls;
  String userId;
  String pop3Server;
  boolean quitWait;
  
  public EmailConfig() {
  }
  
  public EmailConfig(IEmailConfig config) {
    setAuthenticate(config.getAuthenticate());
    setDebug(config.getDebug());
    setDefaultFrom(config.getDefaultFrom());
    setPassword(config.getPassword());
    setPop3Server(config.getPop3Server());
    setSmtpHost(config.getSmtpHost());
    setSmtpPort(config.getSmtpPort());
    setSmtpProtocol(config.getSmtpProtocol());
    setUserId(config.getUserId());
    setUseSsl(config.getUseSsl());
    setUseStartTls(config.getUseStartTls());
    setQuitWait(config.getQuitWait());
  }
  
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public boolean getAuthenticate() {
    return authenticate;
  }
  public void setAuthenticate(boolean authenticate) {
    this.authenticate = authenticate;
  }
  public boolean getDebug() {
    return debug;
  }
  public void setDebug(boolean debug) {
    this.debug = debug;
  }
  public String getDefaultFrom() {
    return defaultFrom;
  }
  public void setDefaultFrom(String defaultFrom) {
    this.defaultFrom = defaultFrom;
  }
  public String getSmtpHost() {
    return smtpHost;
  }
  public void setSmtpHost(String smtpHost) {
    this.smtpHost = smtpHost;
  }
  public Integer getSmtpPort() {
    return smtpPort;
  }
  public void setSmtpPort(Integer smtpPort) {
    this.smtpPort = smtpPort;
  }
  public String getSmtpProtocol() {
    return smtpProtocol;
  }
  public void setSmtpProtocol(String smtpProtocol) {
    this.smtpProtocol = smtpProtocol;
  }
  public boolean getUseSsl() {
    return useSsl;
  }
  public void setUseSsl(boolean useSsl) {
    this.useSsl = useSsl;
  }
  public boolean getUseStartTls() {
    return useStartTls;
  }
  public void setUseStartTls(boolean useStartTls) {
    this.useStartTls = useStartTls;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getPop3Server() {
    return pop3Server;
  }
  public void setPop3Server(String pop3Server) {
    this.pop3Server = pop3Server;
  }

  public boolean getQuitWait() {
    return quitWait;
  }

  public void setQuitWait(boolean quitWait) {
    this.quitWait = quitWait;
  }
  
}
