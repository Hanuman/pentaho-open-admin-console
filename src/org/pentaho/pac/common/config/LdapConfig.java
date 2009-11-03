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

public class LdapConfig implements ILdapConfig, Serializable {

  protected String allRolesAttribute;
  protected String allRolesSearchBase;
  protected String allRolesSearchFilter;
  protected boolean convertUserRolesToUpperCase;
  protected String providerPassword;
  protected String providerUrl;
  protected boolean searchSubtreeForUserRoles;
  protected String userDn;
  protected String userRolesAttribute;
  protected String userRolesPrefix;
  protected String userRolesSearchBase;
  protected String userRolesSearchFilter;
  protected String userSearchBase;
  protected String userSearchFilter;
  
  public LdapConfig() {
    
  }
  
  public LdapConfig(ILdapConfig ldapConfig) {
    setAllRolesAttribute(ldapConfig.getAllRolesAttribute());
    setAllRolesSearchBase(ldapConfig.getAllRolesSearchBase());
    setAllRolesSearchFilter(ldapConfig.getAllRolesSearchFilter());
    setConvertUserRolesToUpperCase(ldapConfig.getConvertUserRolesToUpperCase());
    setProviderPassword(ldapConfig.getProviderPassword());
    setProviderUrl(ldapConfig.getProviderUrl());
    setSearchSubtreeForUserRoles(ldapConfig.getSearchSubtreeForUserRoles());
    setUserDn(ldapConfig.getUserDn());
    setUserRolesAttribute(ldapConfig.getUserRolesAttribute());
    setUserRolesPrefix(ldapConfig.getUserRolesPrefix());
    setUserRolesSearchBase(ldapConfig.getUserRolesSearchBase());
    setUserRolesSearchFilter(ldapConfig.getUserRolesSearchFilter());
    setUserSearchBase(ldapConfig.getUserSearchBase());
    setUserSearchFilter(ldapConfig.getUserSearchFilter());
  }

  public String getAllRolesAttribute() {
    return allRolesAttribute;
  }

  public void setAllRolesAttribute(String allRolesAttribute) {
    this.allRolesAttribute = allRolesAttribute;
  }

  public String getAllRolesSearchBase() {
    return allRolesSearchBase;
  }

  public void setAllRolesSearchBase(String allRolesSearchBase) {
    this.allRolesSearchBase = allRolesSearchBase;
  }

  public String getAllRolesSearchFilter() {
    return allRolesSearchFilter;
  }

  public void setAllRolesSearchFilter(String allRolesSearchFilter) {
    this.allRolesSearchFilter = allRolesSearchFilter;
  }

  public boolean getConvertUserRolesToUpperCase() {
    return convertUserRolesToUpperCase;
  }

  public void setConvertUserRolesToUpperCase(boolean convertUserRolesToUpperCase) {
    this.convertUserRolesToUpperCase = convertUserRolesToUpperCase;
  }

  public String getProviderPassword() {
    return providerPassword;
  }

  public void setProviderPassword(String providerPassword) {
    this.providerPassword = providerPassword;
  }

  public String getProviderUrl() {
    return providerUrl;
  }

  public void setProviderUrl(String providerUrl) {
    this.providerUrl = providerUrl;
  }

  public boolean getSearchSubtreeForUserRoles() {
    return searchSubtreeForUserRoles;
  }

  public void setSearchSubtreeForUserRoles(boolean searchSubtreeForUserRoles) {
    this.searchSubtreeForUserRoles = searchSubtreeForUserRoles;
  }

  public String getUserDn() {
    return userDn;
  }

  public void setUserDn(String userDn) {
    this.userDn = userDn;
  }

  public String getUserRolesAttribute() {
    return userRolesAttribute;
  }

  public void setUserRolesAttribute(String userRolesAttribute) {
    this.userRolesAttribute = userRolesAttribute;
  }

  public String getUserRolesPrefix() {
    return userRolesPrefix;
  }

  public void setUserRolesPrefix(String userRolesPrefix) {
    this.userRolesPrefix = userRolesPrefix;
  }

  public String getUserRolesSearchBase() {
    return userRolesSearchBase;
  }

  public void setUserRolesSearchBase(String userRolesSearchBase) {
    this.userRolesSearchBase = userRolesSearchBase;
  }

  public String getUserRolesSearchFilter() {
    return userRolesSearchFilter;
  }

  public void setUserRolesSearchFilter(String userRolesSearchFilter) {
    this.userRolesSearchFilter = userRolesSearchFilter;
  }

  public String getUserSearchBase() {
    return userSearchBase;
  }

  public void setUserSearchBase(String userSearchBase) {
    this.userSearchBase = userSearchBase;
  }

  public String getUserSearchFilter() {
    return userSearchFilter;
  }

  public void setUserSearchFilter(String userSearchFilter) {
    this.userSearchFilter = userSearchFilter;
  }
}
