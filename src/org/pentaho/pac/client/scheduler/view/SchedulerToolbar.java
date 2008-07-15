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
  private PushButton createBtn = null;
  private PushButton updateBtn = null;
  private PushButton deleteBtn = null;
  private PushButton resumeBtn = null;
  private PushButton suspendBtn = null;
  private PushButton refreshBtn = null;
  private PushButton toggleResumePauseAllBtn = null;
  private ListBox filterList = null;

  private ICallback<Widget> onCreateListener = null;
  private ICallback<Widget> onUpdateListener = null;
  private ICallback<Widget> onDeleteListener = null;
  private ICallback<Widget> onResumeListener = null;
  private ICallback<Widget> onPauseListener = null;
  private ICallback<Widget> onRefreshListener = null;
  private ICallback<Widget> onToggleResumePauseAllListener = null;
  private ICallback<String> onFilterListChangeListener = null;
  
  public SchedulerToolbar() {
    super();
    createToolbar();
  }
  
  private void createToolbar() {
    
    final SchedulerToolbar localThis = this;
    
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
        if ( null != onPauseListener ) {
          onPauseListener.onHandle( null );
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
    
    toggleResumePauseAllBtn = new PushButton( "RSAll", new ClickListener() {
      public void onClick(Widget sender) {
        if ( null != onToggleResumePauseAllListener ) {
          onToggleResumePauseAllListener.onHandle( sender );
        }
      }
    });  
    add( toggleResumePauseAllBtn );
    
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
  
  public void clearFilters() {
    filterList.clear();
  }
  
  public void addFilterItem( String name ) {
    filterList.addItem( name );
  }
  
  public String getFilterValue() {
    return filterList.getValue( filterList.getSelectedIndex() );
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
  
  public void setOnPauseListener( ICallback cb ) {
    this.onPauseListener = cb;
  }
  
  public void setOnRefreshListener( ICallback cb ) {
    this.onRefreshListener = cb;
  }
  
  public void setOnFilterListChangeListener( ICallback cb ) {
    this.onFilterListChangeListener = cb;
  }

  public void setOnToggleResumePauseAllListener(ICallback cb) {
    this.onToggleResumePauseAllListener = cb;
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

  public PushButton getRefreshBtn() {
    return refreshBtn;
  }

  public PushButton getToggleResumePauseAllBtn() {
    return toggleResumePauseAllBtn;
  }
}
