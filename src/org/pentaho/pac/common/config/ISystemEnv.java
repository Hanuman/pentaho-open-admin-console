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

public interface ISystemEnv {
  public String getOsName();
  public void setOsName(String osName);
  public String getOsVersion();
  public void setOsVersion(String osVersion);
  public String getSunOsPatchLevel();
  public void setSunOsPatchLevel(String sunOsPathLevel);
  public String getUserCountry();
  public void setUserCountry(String userCountry);
  public String getUserLanguage();
  public void setUserLanguage(String userLanguage);
  public String getUserName();
  public void setUserName(String userName);
  public String getUserTimeZone();
  public void setUserTimeZone(String userTimeZone);
  public String getUserDir();
  public void setUserDir(String userDir);
  public String getJavaVersion();
  public void setJavaVersion(String javaVersion);
  public String getJavaVendor();
  public void setJavaVendor(String javaVendor);
  public String getJavaVmInfo();
  public void setJavaVmInfo(String javaVmInfo);
  public String getJavaVmName();
  public void setJavaVmName(String javaVmName);
  public String getFileEncoding();
  public void setFileEncoding(String fileEncoding);
  public String getJavaClassPath();
  public void setJavaClassPath(String javaClassPath);
  public String getJavaHome();
  public void setJavaHome(String javaHome);
  public String getJavaLibraryPath();
  public void setJavaLibraryPath(String javaLibraryPath);
  public String getAppServerBindAddr();
  public void setAppServerBindAddr(String appServerBindAddr);
  public String getAppServerHomeDir();
  public void setAppServerHomeDir(String appServerHomeDir);
  public String getAppServerLibUrl();
  public void setAppServerLibUrl(String appServerLibUrl);
  public String getPentahoOlapXmlDataSources();
  public void setPentahoOlapXmlDataSources(String pentahoOlapXmlDataSources);
}
