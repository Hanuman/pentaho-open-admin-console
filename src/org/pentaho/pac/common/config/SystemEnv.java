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

public class SystemEnv implements ISystemEnv, Serializable {
  String osName;
  String osVersion;
  String sunOsPathLevel;
  String userCountry;
  String userLanguage;
  String userName;
  String userTimeZone;
  String userDir;
  String javaVersion;
  String javaVendor;
  String javaVmInfo;
  String javaVmName;
  String fileEncoding;
  String javaClassPath;
  String javaHome;
  String javaLibraryPath;
  String appServerBindAddr;
  String appServerHomeDir;
  String appServerLibUrl;
  String pentahoOlapXmlDataSources;
  
  public SystemEnv() {
    
  }
  
  public SystemEnv(ISystemEnv systemEnv) {
    setOsName(systemEnv.getOsName());
    setOsVersion(systemEnv.getOsVersion());
    setSunOsPatchLevel(systemEnv.getSunOsPatchLevel());
    setUserCountry(systemEnv.getUserCountry());
    setUserLanguage(systemEnv.getUserLanguage());
    setUserName(systemEnv.getUserName());
    setUserTimeZone(systemEnv.getUserTimeZone());
    setUserDir(systemEnv.getUserDir());
    setJavaVersion(systemEnv.getJavaVersion());
    setJavaVendor(systemEnv.getJavaVendor());
    setJavaVmInfo(systemEnv.getJavaVmInfo());
    setJavaVmName(systemEnv.getJavaVmName());
    setFileEncoding(systemEnv.getFileEncoding());
    setJavaClassPath(systemEnv.getJavaClassPath());
    setJavaHome(systemEnv.getJavaHome());
    setJavaLibraryPath(systemEnv.getJavaLibraryPath());
    setAppServerBindAddr(systemEnv.getAppServerBindAddr());
    setAppServerHomeDir(systemEnv.getAppServerHomeDir());
    setAppServerLibUrl(systemEnv.getAppServerLibUrl());
    setPentahoOlapXmlDataSources(systemEnv.getPentahoOlapXmlDataSources());
  }
  public String getOsName() {
    return osName;
  }
  public void setOsName(String osName) {
    this.osName = osName;
  }
  public String getOsVersion() {
    return osVersion;
  }
  public void setOsVersion(String osVersion) {
    this.osVersion = osVersion;
  }
  public String getSunOsPatchLevel() {
    return sunOsPathLevel;
  }
  public void setSunOsPatchLevel(String sunOsPathLevel) {
    this.sunOsPathLevel = sunOsPathLevel;
  }
  public String getUserCountry() {
    return userCountry;
  }
  public void setUserCountry(String userCountry) {
    this.userCountry = userCountry;
  }
  public String getUserLanguage() {
    return userLanguage;
  }
  public void setUserLanguage(String userLanguage) {
    this.userLanguage = userLanguage;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getUserTimeZone() {
    return userTimeZone;
  }
  public void setUserTimeZone(String userTimeZone) {
    this.userTimeZone = userTimeZone;
  }
  public String getUserDir() {
    return userDir;
  }
  public void setUserDir(String userDir) {
    this.userDir = userDir;
  }
  public String getJavaVersion() {
    return javaVersion;
  }
  public void setJavaVersion(String javaVersion) {
    this.javaVersion = javaVersion;
  }
  public String getJavaVendor() {
    return javaVendor;
  }
  public void setJavaVendor(String javaVendor) {
    this.javaVendor = javaVendor;
  }
  public String getJavaVmInfo() {
    return javaVmInfo;
  }
  public void setJavaVmInfo(String javaVmInfo) {
    this.javaVmInfo = javaVmInfo;
  }
  public String getJavaVmName() {
    return javaVmName;
  }
  public void setJavaVmName(String javaVmName) {
    this.javaVmName = javaVmName;
  }
  public String getFileEncoding() {
    return fileEncoding;
  }
  public void setFileEncoding(String fileEncoding) {
    this.fileEncoding = fileEncoding;
  }
  public String getJavaClassPath() {
    return javaClassPath;
  }
  public void setJavaClassPath(String javaClassPath) {
    this.javaClassPath = javaClassPath;
  }
  public String getJavaHome() {
    return javaHome;
  }
  public void setJavaHome(String javaHome) {
    this.javaHome = javaHome;
  }
  public String getJavaLibraryPath() {
    return javaLibraryPath;
  }
  public void setJavaLibraryPath(String javaLibraryPath) {
    this.javaLibraryPath = javaLibraryPath;
  }
  public String getAppServerBindAddr() {
    return appServerBindAddr;
  }
  public void setAppServerBindAddr(String appServerBindAddr) {
    this.appServerBindAddr = appServerBindAddr;
  }
  public String getAppServerHomeDir() {
    return appServerHomeDir;
  }
  public void setAppServerHomeDir(String appServerHomeDir) {
    this.appServerHomeDir = appServerHomeDir;
  }
  public String getAppServerLibUrl() {
    return appServerLibUrl;
  }
  public void setAppServerLibUrl(String appServerLibUrl) {
    this.appServerLibUrl = appServerLibUrl;
  }
  public String getPentahoOlapXmlDataSources() {
    return pentahoOlapXmlDataSources;
  }
  public void setPentahoOlapXmlDataSources(String pentahoOlapXmlDataSources) {
    this.pentahoOlapXmlDataSources = pentahoOlapXmlDataSources;
  }
  
  
}
