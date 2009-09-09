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
package org.pentaho.pac.client.datasources;

import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.common.NameValue;
import org.pentaho.pac.common.datasources.PentahoDataSource;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataSourceGeneralPanel extends VerticalPanel {
  public static final int PASSWORD_MAX_LENGTH = 50;
  HorizontalPanel jdbcClassNamePanel = new HorizontalPanel(); 
  TextBox userNameTextBox = new TextBox();
  PasswordTextBox passwordTextBox = new PasswordTextBox();
  TextBox jndiNameTextBox = new TextBox();
  TextBox urlTextBox = new TextBox();
  private final ListBox driverList = new ListBox();
  TextBox driverClassNameTextBox = new TextBox();
  boolean driverClassListBoxHasValue = false;
  public DataSourceGeneralPanel() {
    jdbcClassNamePanel.add(driverClassNameTextBox);
    driverClassNameTextBox.setWidth("100%"); //$NON-NLS-1$
    constructDatasourcePanel();
  }

  private void constructDatasourcePanel() {
    add(new Label(Messages.getString("jndiName"))); //$NON-NLS-1$
    add(jndiNameTextBox);
    add(new Label(Messages.getString("jdbcDriverClass"))); //$NON-NLS-1$
    add(jdbcClassNamePanel);
    jdbcClassNamePanel.setWidth("100%");//$NON-NLS-1$
    add(new Label(Messages.getString("dbUserName"))); //$NON-NLS-1$
    add(userNameTextBox);
    add(new Label(Messages.getString("dbPassword"))); //$NON-NLS-1$
    add(passwordTextBox);
    passwordTextBox.setMaxLength(PASSWORD_MAX_LENGTH);
    add(new Label(Messages.getString("dbUrl"))); //$NON-NLS-1$
    add(urlTextBox);
    jndiNameTextBox.setWidth("100%"); //$NON-NLS-1$
    driverList.setWidth("100%"); //$NON-NLS-1$
    userNameTextBox.setWidth("100%"); //$NON-NLS-1$
    passwordTextBox.setWidth("100%"); //$NON-NLS-1$
    urlTextBox.setWidth("100%"); //$NON-NLS-1$    
  }

  public String getUserName() {
    return userNameTextBox.getText();
  }

  public void setUserName(String userName) {
    this.userNameTextBox.setText(userName);
  }

  public String getPassword() {
    return passwordTextBox.getText();
  }

  public void setPassword(String password) {
    this.passwordTextBox.setText(password);
  }

  public String getJndiName() {
    return jndiNameTextBox.getText();
  }

  public void setJndiName(String jndiName) {
    jndiNameTextBox.setText(jndiName);
  }

  public String getDriverClass() {
    String returnValue = null;
    if(driverClassListBoxHasValue) {
      if((driverList.getSelectedIndex() >= 0) && (driverList.getSelectedIndex() < driverList.getItemCount())){
        returnValue = driverList.getValue(driverList.getSelectedIndex());
      }
    } else {
      returnValue = driverClassNameTextBox.getText();
    }
      
    return returnValue;
  }

  public void setDriverClass(String className) {
    if(driverClassListBoxHasValue) {
      for (int i = 0; i < driverList.getItemCount(); i++) {
        if (driverList.getValue(i).equals(className)) {
          driverList.setSelectedIndex(i);
        }
      }
    } else {
      driverClassNameTextBox.setText(className);
    }
  }
  
  public String getUrl() {
    return urlTextBox.getText();
  }

  public void setUrl(String url) {
    urlTextBox.setText(url);
  }

  public TextBox getUserNameTextBox() {
    return userNameTextBox;
  }

  public PasswordTextBox getPasswordTextBox() {
    return passwordTextBox;
  }

  public TextBox getJndiNameTextBox() {
    return jndiNameTextBox;
  }

  public ListBox getDriverClassListBox() {
    return driverList;
  }

  public TextBox getDriverClassTextBox() {
    return driverClassNameTextBox;
  }
  
  public TextBox getUrlTextBox() {
    return urlTextBox;
  }

  public void setDataSource(PentahoDataSource dataSource) {
    if (dataSource == null) {
      setUserName(""); //$NON-NLS-1$
      setPassword(""); //$NON-NLS-1$
      setJndiName(""); //$NON-NLS-1$
      setDriverClass(""); //$NON-NLS-1$
      setUrl(""); //$NON-NLS-1$
    } else {
      setUserName(dataSource.getUserName());
      setPassword(dataSource.getPassword());
      setJndiName(dataSource.getName());
      setDriverClass(dataSource.getDriverClass());
      setUrl(dataSource.getUrl());
    }
  }
  
  public PentahoDataSource getDataSource() {
    PentahoDataSource dataSource = new PentahoDataSource();
    dataSource.setUserName(getUserName());
    dataSource.setPassword(getPassword());
    dataSource.setName(getJndiName());
    dataSource.setDriverClass(getDriverClass());
    dataSource.setUrl(getUrl());
    return dataSource;
  }
  
  public void setEnabled(boolean enabled) {
    userNameTextBox.setEnabled(enabled);
    passwordTextBox.setEnabled(enabled);
    jndiNameTextBox.setEnabled(enabled);
    driverList.setEnabled(enabled);
    urlTextBox.setEnabled(enabled);
    driverClassNameTextBox.setEnabled(enabled);
  }

  public void refresh(NameValue[] drivers) {
    // First remove all the item in the list and then add the latest ones
    for(int i=0; i< driverList.getItemCount();i++) {
      driverList.removeItem(i);
    }
    if(drivers != null && drivers.length >  0) {
      for (NameValue res : drivers)
        driverList.addItem(res.getName(), res.getValue());
      driverClassListBoxHasValue = drivers != null && drivers.length > 0;
      if(driverClassListBoxHasValue) {
        driverClassNameTextBox.removeFromParent();
        jdbcClassNamePanel.add(driverList);
        driverList.setWidth("100%"); //$NON-NLS-1$
      } else {
        driverList.removeFromParent();
        jdbcClassNamePanel.add(driverClassNameTextBox);
        driverClassNameTextBox.setWidth("100%"); //$NON-NLS-1$
      }
    } else {
      driverList.removeFromParent();
      jdbcClassNamePanel.add(driverClassNameTextBox);
      driverClassNameTextBox.setWidth("100%"); //$NON-NLS-1$
    }
  }
}
