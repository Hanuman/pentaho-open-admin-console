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

import org.pentaho.pac.client.common.ui.ICallback;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class SchedulerToolbar extends HorizontalPanel {

  public final static String ALL_FILTER = "All";
  private PushButton selectAllBtn = null;
  private PushButton unselectAllBtn = null;
  private PushButton createBtn = null;
  private PushButton updateBtn = null;
  private PushButton deleteBtn = null;
  private PushButton resumeBtn = null;
  private PushButton suspendBtn = null;
  private PushButton refreshBtn = null;
  private PushButton toggleResumeSuspendAllBtn = null;
  private ListBox filterList = null;

  private ICallback onSelectAllListener = null;
  private ICallback onUnselectAllListener = null;
  private ICallback onCreateListener = null;
  private ICallback onUpdateListener = null;
  private ICallback onDeleteListener = null;
  private ICallback onResumeListener = null;
  private ICallback onSuspendListener = null;
  private ICallback onRefreshListener = null;
  private ICallback onToggleResumeSuspendAllListener = null;
  private ICallback onFilterListChangeListener = null;
  
  public SchedulerToolbar() {
    super();
    createToolbar();
  }
  
  private void createToolbar() {
    
    final SchedulerToolbar localThis = this;
    
    selectAllBtn = new PushButton( "SelAl", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onSelectAllListener ) {
          onSelectAllListener.onHandle( null );
        }
      }
    });  
    add( selectAllBtn );
    
    unselectAllBtn = new PushButton( "UnSelAl", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onUnselectAllListener ) {
          onUnselectAllListener.onHandle( null );
        }
      }
    });  
    add( unselectAllBtn );
    
    createBtn = new PushButton( "Cr", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onCreateListener ) {
          onCreateListener.onHandle( null );
        }
      }
    });  
    add( createBtn );
    
    updateBtn = new PushButton( "UpD", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onUpdateListener ) {
          onUpdateListener.onHandle( null );
        }
      }
    });  
    add( updateBtn );
    
    deleteBtn = new PushButton( "Del", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onDeleteListener ) {
          onDeleteListener.onHandle( null );
        }
      }
    });  
    add( deleteBtn );
    
    resumeBtn = new PushButton( "Res", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onResumeListener ) {
          onResumeListener.onHandle( null );
        }
      }
    });  
    add( resumeBtn );
    
    suspendBtn = new PushButton( "Susp", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onSuspendListener ) {
          onSuspendListener.onHandle( null );
        }
      }
    });  
    add( suspendBtn );
    
    refreshBtn = new PushButton( "Refresh", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onRefreshListener ) {
          onRefreshListener.onHandle( null );
        }
      }
    });  
    add( refreshBtn );
    
    toggleResumeSuspendAllBtn = new PushButton( "RSAll", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onToggleResumeSuspendAllListener ) {
          onToggleResumeSuspendAllListener.onHandle( sender );
        }
      }
    });  
    add( toggleResumeSuspendAllBtn );
    
    HorizontalPanel filterPanel = new HorizontalPanel();
    filterPanel.setStyleName( "filterPanel" );
    Label l = new Label( "Filter:" );
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
    add( filterPanel );
  }
  
  public void addFilterItem( String name ) {
    filterList.addItem( name );
  }
  
  public String getFilterValue() {
    return filterList.getValue( filterList.getSelectedIndex() );
  }
  
  public void setOnSelectAllListener( ICallback cb ) {
    this.onSelectAllListener = cb;
  }
  
  public void setOnUnselectAllListener( ICallback cb ) {
    this.onUnselectAllListener = cb;
  }
  
  public void setOnCreateListener( ICallback cb ) {
    this.onCreateListener = cb;
  }
  
  public void setOnUpdateListener( ICallback cb ) {
    this.onUpdateListener = cb;
  }
  
  public void setOnDeleteListener( ICallback cb ) {
    this.onDeleteListener = cb;
  }
  
  public void setOnResumeListener( ICallback cb ) {
    this.onResumeListener = cb;
  }
  
  public void setOnSuspendListener( ICallback cb ) {
    this.onSuspendListener = cb;
  }
  
  public void setOnRefreshListener( ICallback cb ) {
    this.onRefreshListener = cb;
  }
  
  public void setOnFilterListChangeListener( ICallback cb ) {
    this.onFilterListChangeListener = cb;
  }

  public void setOnToggleResumeSuspendAllListener(ICallback cb) {
    this.onToggleResumeSuspendAllListener = cb;
  }
}
