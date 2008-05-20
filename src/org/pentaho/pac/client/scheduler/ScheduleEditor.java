/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created May 19, 2008
 * 
 */
package org.pentaho.pac.client.scheduler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ScheduleEditor extends FlexTable {

  private TextBox nameTb = new TextBox();
  private TextBox groupNameTb = new TextBox();
  private TextBox descriptionTb = new TextBox();
  private TextBox cronStringTb = new TextBox();
  
  private RecurranceDialog recurranceDialog = new RecurranceDialog();
  public ScheduleEditor() {
    super();

    setCellPadding( 0 );
    setCellSpacing( 0 );
    
    Label l = new Label( "Name:" );
    setWidget( 0 , 0, l );
    setWidget( 0 , 1, nameTb );
    
    l = new Label( "Group:" );
    setWidget( 1 , 0, l );
    setWidget( 1 , 1, groupNameTb );
    
    l = new Label( "Description:" );
    setWidget( 2 , 0, l );
    setWidget( 2 , 1, descriptionTb );
      
    l = new Label( "Cron String:" );
    setWidget( 3 , 0, l );
    //crontStringTb.setReadOnly( true );
    setWidget( 3 , 1, cronStringTb );
    Button b = new Button( "Edit...", new ClickListener() {
      public void onClick(Widget sender) {
        // TODO sbarkdull
        recurranceDialog.inititalizeWithCronString( getCronString() );
        recurranceDialog.center();
      }
    });
    setWidget( 3 , 2, b );
  }
  
  public String getName() {
    return nameTb.getText();
  }
  
  public void setName( String name ) {
    nameTb.setText( name );
  }
  
  public String getGroupName() {
    return groupNameTb.getText();
  }
  
  public void setGroupName( String groupName ) {
    groupNameTb.setText( groupName );
  }
  
  public String getDescription() {
    return descriptionTb.getText();
  }
  
  public void setDescription( String description ) {
    descriptionTb.setText( description );
  }
  
  public String getCronString() {
    // TODO sbarkdull
    return cronStringTb.getText();
  }
  
  public void setCronString( String cronStr ) {
    cronStringTb.setText( cronStr );
  }
}
