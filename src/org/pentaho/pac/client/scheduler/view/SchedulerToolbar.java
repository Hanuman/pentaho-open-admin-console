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
package org.pentaho.pac.client.scheduler.view;

import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class SchedulerToolbar extends HorizontalPanel {

  protected static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  public final static String ALL_FILTER = "All";
  private PushButton createBtn = null;
  private PushButton updateBtn = null;
  private PushButton deleteBtn = null;
  private PushButton suspendBtn = null;
  private PushButton resumeBtn = null;
  private PushButton runNowBtn = null;
  private PushButton suspendSchedulerBtn = null;
  private PushButton resumeSchedulerBtn = null;
  private PushButton refreshBtn = null;
  private ListBox filterList = null;

  private ICallback<Widget> onCreateListener = null;
  private ICallback<Widget> onUpdateListener = null;
  private ICallback<Widget> onDeleteListener = null;
  private ICallback<Widget> onSuspendListener = null;
  private ICallback<Widget> onResumeListener = null;
  private ICallback<Widget> onRunNowListener = null;
  private ICallback<Widget> onSuspendSchedulerListener = null;
  private ICallback<Widget> onResumeSchedulerListener = null;
  private ICallback<Widget> onRefreshListener = null;
  private ICallback<String> onFilterListChangeListener = null;
  
  public SchedulerToolbar() {
    super();
    setStyleName( "schedToolbar" );
    createToolbar();
  }
  
  private void createToolbar() {

    HorizontalPanel leftPanel = new HorizontalPanel();
    leftPanel.setStyleName( "schedToolbar.leftPanel" );
    add( leftPanel );
    
    HorizontalPanel rightPanel = new HorizontalPanel();
    rightPanel.setStyleName( "schedToolbar.rightPanel" );
    add( rightPanel );
    
    createBtn = createPushButton( "Create schedule", "toolbarCreateBtn", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onCreateListener ) {
          onCreateListener.onHandle( null );
        }
      }
    });
    leftPanel.add( createBtn );
    
    updateBtn = createPushButton( "Edit schedule", "toolbarUpdateBtn", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onUpdateListener ) {
          onUpdateListener.onHandle( null );
        }
      }
    });
    leftPanel.add( updateBtn );

    deleteBtn = createPushButton( "Delete schedule(s)", "toolbarDeleteBtn", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onDeleteListener ) {
          onDeleteListener.onHandle( null );
        }
      }
    });
    leftPanel.add( deleteBtn );
    
    //toolbarDivider
    Image img = new Image( "style/images/toolbarDivider.png", 0, 0, 2, 16 );
    leftPanel.add( img );

    suspendBtn = createPushButton( "Suspend schedule(s)", "toolbarSuspendBtn", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onSuspendListener ) {
          onSuspendListener.onHandle( null );
        }
      }
    });
    leftPanel.add( suspendBtn );

    resumeBtn = createPushButton( "Resume schedule(s)", "toolbarResumeBtn", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onResumeListener ) {
          onResumeListener.onHandle( null );
        }
      }
    });
    leftPanel.add( resumeBtn );

    runNowBtn = createPushButton( "Run schedule(s) now", "toolbarRunNowBtn", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onRunNowListener ) {
          onRunNowListener.onHandle( null );
        }
      }
    });
    leftPanel.add( runNowBtn );
    
    //toolbarDivider
    img = new Image( "style/images/toolbarDivider.png", 0, 0, 2, 16 );
    leftPanel.add( img );
    
    suspendSchedulerBtn = createPushButton( "Suspend scheduler", "toolbarSuspendSchedulerBtn", new ClickListener() {
      public void onClick(Widget sender) {
        assert false : "note implemented";
        if ( null != onSuspendSchedulerListener ) {
          onSuspendSchedulerListener.onHandle( null );
        }
      }
    });
    leftPanel.add( suspendSchedulerBtn );

    resumeSchedulerBtn = createPushButton( "Resume scheduler", "toolbarResumeSchedulerBtn", new ClickListener() {
      public void onClick(Widget sender) {
        assert false : "note implemented";
        if ( null != onResumeSchedulerListener ) {
          onResumeSchedulerListener.onHandle( null );
        }
      }
    });
    leftPanel.add( resumeSchedulerBtn );
    
    //toolbarDivider
    img = new Image( "style/images/toolbarDivider.png", 0, 0, 2, 16 );
    leftPanel.add( img );
    
    refreshBtn = createPushButton( "Refresh schedule list", "toolbarRefreshBtn", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onRefreshListener ) {
          onRefreshListener.onHandle( null );
        }
      }
    });
    leftPanel.add( refreshBtn );
    
    HorizontalPanel filterPanel = new HorizontalPanel();
    filterPanel.setStyleName( "filterPanel" );
    Label l = new Label( MSGS.filterBy() );
    filterPanel.add( l );
    
    filterList = new ListBox();
    filterList.setVisibleItemCount( 1 );
    filterList.addItem( ALL_FILTER );

    filterList.addChangeListener( new ChangeListener() {
      public void onChange(Widget sender) {
        if ( null != onFilterListChangeListener ) {
          String val = filterList.getValue( filterList.getSelectedIndex() );
          onFilterListChangeListener.onHandle( val );
        }
      }
    });
    filterPanel.add( filterList );
    rightPanel.add( filterPanel );

    this.setCellHorizontalAlignment( leftPanel, HasHorizontalAlignment.ALIGN_LEFT );
    this.setCellHorizontalAlignment( rightPanel, HasHorizontalAlignment.ALIGN_RIGHT );
  }
  
  private static PushButton createPushButton( String hoverText, String styleName, ClickListener clickListener ) {
    PushButton btn = new PushButton();
    btn.addClickListener( clickListener ); 
    
    btn.setStylePrimaryName( styleName );
    btn.addStyleName( "toolbarBtn" );
    btn.setTitle( hoverText );
    
    return btn;
  }
  
  public void clearFilters() {
    filterList.clear();
  }
  
  public void addFilterItem( String name ) {
    filterList.addItem( name );
  }
  
  public String getFilterValue() {
    return filterList.getValue( filterList.getSelectedIndex() );
  }
  
  public void setOnCreateListener( ICallback<Widget> cb ) {
    this.onCreateListener = cb;
  }
  
  public void setOnUpdateListener( ICallback<Widget> cb ) {
    this.onUpdateListener = cb;
  }
  
  public void setOnDeleteListener( ICallback<Widget> cb ) {
    this.onDeleteListener = cb;
  }
  
  public void setOnResumeListener( ICallback<Widget> cb ) {
    this.onResumeListener = cb;
  }
  
  public void setOnSuspendListener( ICallback<Widget> cb ) {
    this.onSuspendListener = cb;
  }
  
  public void setOnRunNowListener( ICallback<Widget> cb ) {
    this.onRunNowListener = cb;
  }
  
  public void setOnResumeSchedulerListener( ICallback<Widget> cb ) {
    this.onResumeSchedulerListener = cb;
  }
  
  public void setOnSuspendSchedulerListener( ICallback<Widget> cb ) {
    this.onSuspendSchedulerListener = cb;
  }
  
  public void setOnRefreshListener( ICallback<Widget> cb ) {
    this.onRefreshListener = cb;
  }
  
  public void setOnFilterListChangeListener( ICallback<String> cb ) {
    this.onFilterListChangeListener = cb;
  }

  public PushButton getRunNowBtn() {
    return runNowBtn;
  }

  public PushButton getCreateBtn() {
    return createBtn;
  }

  public PushButton getUpdateBtn() {
    return updateBtn;
  }

  public PushButton getDeleteBtn() {
    return deleteBtn;
  }

  public PushButton getResumeBtn() {
    return resumeBtn;
  }

  public PushButton getSuspendBtn() {
    return suspendBtn;
  }

  public PushButton getResumeSchedulerBtn() {
    return resumeSchedulerBtn;
  }

  public PushButton getSuspendSchedulerBtn() {
    return suspendSchedulerBtn;
  }

  public PushButton getRefreshBtn() {
    return refreshBtn;
  }
}
