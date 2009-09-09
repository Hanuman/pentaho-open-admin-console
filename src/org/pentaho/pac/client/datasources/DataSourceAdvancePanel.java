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

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.client.common.keyfilters.KeyListenerFactory;
import org.pentaho.pac.client.common.keyfilters.KeyListenerFactory.FILTER_TYPE;
import org.pentaho.pac.client.i18n.Messages;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataSourceAdvancePanel extends VerticalPanel{
  private KeyboardListener defaultKeyListener = KeyListenerFactory.getKeyboardListener(FILTER_TYPE.NUMERIC);
  TextBox maxActiveConnTextBox = new TextBox();
  TextBox idleConnTextBox = new TextBox();
  TextBox validationQueryTextBox = new TextBox();
  TextBox waitTextBox = new TextBox();
  Label label = new Label(Messages.getString("datasourceAdvanceInfo")); //$NON-NLS-1$
  
  public DataSourceAdvancePanel() {
    add(new Label(Messages.getString("maxActiveDbConnections"))); //$NON-NLS-1$
    add(maxActiveConnTextBox);
    add(new Label(Messages.getString("numIdleDbConnnections"))); //$NON-NLS-1$
    add(idleConnTextBox);
    add(new Label(Messages.getString("dbValidationQuery"))); //$NON-NLS-1$
    add(validationQueryTextBox);
    add(new Label(Messages.getString("dbWaitTime"))); //$NON-NLS-1$
    add(waitTextBox);
    add(label);
    maxActiveConnTextBox.addKeyboardListener(defaultKeyListener);
    idleConnTextBox.addKeyboardListener(defaultKeyListener);
    waitTextBox.addKeyboardListener(defaultKeyListener);
    
    maxActiveConnTextBox.setWidth("100%"); //$NON-NLS-1$
    idleConnTextBox.setWidth("100%"); //$NON-NLS-1$
    validationQueryTextBox.setWidth("100%"); //$NON-NLS-1$
    waitTextBox.setWidth("100%"); //$NON-NLS-1$
    label.setWidth("100%"); //$NON-NLS-1$
  }

  public int getMaxActiveConnections() {
    int count = -1;
    try {
      count = Integer.parseInt(maxActiveConnTextBox.getText());
    } catch (Exception ex) {
      // Do nothing.
    }
    return count;
  }

  public void setMaxActiveConnections(int count) {
    maxActiveConnTextBox.setText(count > 0 ? Integer.toString(count) : ""); //$NON-NLS-1$
  }
  
  public int getIdleConnections() {
    int count = -1;
    try {
      count = Integer.parseInt(idleConnTextBox.getText());
    } catch (Exception ex) {
      // Do nothing.
    }
    return count;
  }
  
  public void setIdleConnections(int count) {
    idleConnTextBox.setText(count > 0 ? Integer.toString(count) : ""); //$NON-NLS-1$
  }
  
  public String getValidationQuery() {
    return validationQueryTextBox.getText();
  }

  public void setValidationQuery(String query) {
    validationQueryTextBox.setText(query);
  }

  public long getWait() {
    int count = -1;
    try {
      count = Integer.parseInt(waitTextBox.getText());
    } catch (Exception ex) {
      // Do nothing.
    }
    return count;
  }
  
  public void setWait(long count) {
    waitTextBox.setText(count > 0 ? Long.toString(count) : ""); //$NON-NLS-1$
 }
  
  public TextBox getMaxActiveConnectionsTextBox() {
    return maxActiveConnTextBox;
  }

  public TextBox getIdleConnectionsTextBox() {
    return maxActiveConnTextBox;
  }
  
  public TextBox getValidationQueryTextBox() {
    return validationQueryTextBox;
  }

  public TextBox getWaitTextBox() {
    return waitTextBox;
  }
  
  public void setDataSource(PentahoDataSource dataSource) {
    if (dataSource == null) {
      setMaxActiveConnections(-1);
      setIdleConnections(-1);
      setValidationQuery(""); //$NON-NLS-1$
      setWait(-1);
    } else {
      setMaxActiveConnections(dataSource.getMaxActConn());
      setIdleConnections(dataSource.getIdleConn());
      setValidationQuery(dataSource.getQuery());
      setWait(dataSource.getWait());
    }
  }
  
  public PentahoDataSource getDataSource() {
    PentahoDataSource dataSource = new PentahoDataSource();
    dataSource.setMaxActConn(getMaxActiveConnections());
    dataSource.setIdleConn(getIdleConnections());
    dataSource.setQuery(getValidationQuery());
    dataSource.setWait(getWait());
    return dataSource;
  }
  
  public void setEnabled(boolean enabled) {
    maxActiveConnTextBox.setEnabled(enabled);
    idleConnTextBox.setEnabled(enabled);
    validationQueryTextBox.setEnabled(enabled);
    waitTextBox.setEnabled(enabled);
  }

  public void refresh() {
  }
 
}
