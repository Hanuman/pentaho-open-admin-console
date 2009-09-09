/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
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
 *
 * @created May 19, 2008
 * 
 */
package org.pentaho.pac.client.scheduler.view;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.i18n.Messages;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class SchedulerToolbar extends HorizontalPanel {

  public final static String ALL_GROUPS_FILTER = Messages.getString("allGroups"); //$NON-NLS-1$
  private PushButton createBtn = null;
  private PushButton updateBtn = null;
  private PushButton deleteBtn = null;
  private PushButton suspendBtn = null;
  private PushButton resumeBtn = null;
  private PushButton runNowBtn = null;
  private PushButton refreshBtn = null;
  private ListBox filterList = null;

  private ICallback<Widget> onCreateListener = null;
  private ICallback<Widget> onUpdateListener = null;
  private ICallback<Widget> onDeleteListener = null;
  private ICallback<Widget> onSuspendListener = null;
  private ICallback<Widget> onResumeListener = null;
  private ICallback<Widget> onRunNowListener = null;
  private ICallback<Widget> onRefreshListener = null;
  private ICallback<String> onFilterListChangeListener = null;
  
  public SchedulerToolbar() {
    super();
    setStyleName( "schedToolbar" ); //$NON-NLS-1$
    createToolbar();
  }
  
  private void createToolbar() {

    HorizontalPanel leftPanel = new HorizontalPanel();
    leftPanel.setStyleName( "schedToolbar.leftPanel" ); //$NON-NLS-1$
    add( leftPanel );
    
    HorizontalPanel rightPanel = new HorizontalPanel();
    rightPanel.setStyleName( "schedToolbar.rightPanel" ); //$NON-NLS-1$
    add( rightPanel );
    
    createBtn = createPushButton( Messages.getString("createSchedule"), "toolbarCreateBtn", new ClickListener() { //$NON-NLS-1$ //$NON-NLS-2$
      public void onClick(Widget sender) {
        if ( null != onCreateListener ) {
          onCreateListener.onHandle( null );
        }
      }
    });
    leftPanel.add( createBtn );
    
    updateBtn = createPushButton( Messages.getString("editSchedule"), "toolbarUpdateBtn", new ClickListener() { //$NON-NLS-1$ //$NON-NLS-2$
      public void onClick(Widget sender) {
        if ( null != onUpdateListener ) {
          onUpdateListener.onHandle( null );
        }
      }
    });
    leftPanel.add( updateBtn );

    deleteBtn = createPushButton( Messages.getString("deleteSchedules"), "toolbarDeleteBtn", new ClickListener() { //$NON-NLS-1$ //$NON-NLS-2$
      public void onClick(Widget sender) {
        if ( null != onDeleteListener ) {
          onDeleteListener.onHandle( null );
        }
      }
    });
    leftPanel.add( deleteBtn );
    
    //toolbarDivider
    Image img = new Image( "style/images/toolbarDivider.png", 0, 0, 2, 16 ); //$NON-NLS-1$
    leftPanel.add( img );

    suspendBtn = createPushButton( Messages.getString("suspendSchedules"), "toolbarSuspendBtn", new ClickListener() { //$NON-NLS-1$ //$NON-NLS-2$
      public void onClick(Widget sender) {
        if ( null != onSuspendListener ) {
          onSuspendListener.onHandle( null );
        }
      }
    });
    leftPanel.add( suspendBtn );

    resumeBtn = createPushButton( Messages.getString("resumeSchedules"), "toolbarResumeBtn", new ClickListener() { //$NON-NLS-1$ //$NON-NLS-2$
      public void onClick(Widget sender) {
        if ( null != onResumeListener ) {
          onResumeListener.onHandle( null );
        }
      }
    });
    leftPanel.add( resumeBtn );

    runNowBtn = createPushButton( Messages.getString("runSchedules"), "toolbarRunNowBtn", new ClickListener() { //$NON-NLS-1$ //$NON-NLS-2$
      public void onClick(Widget sender) {
        if ( null != onRunNowListener ) {
          onRunNowListener.onHandle( null );
        }
      }
    });
    leftPanel.add( runNowBtn );
    
    //toolbarDivider
    img = new Image( "style/images/toolbarDivider.png", 0, 0, 2, 16 ); //$NON-NLS-1$
    leftPanel.add( img );
    
    refreshBtn = createPushButton( Messages.getString("refreshScheduleList"), "toolbarRefreshBtn", new ClickListener() { //$NON-NLS-1$ //$NON-NLS-2$
      public void onClick(Widget sender) {
        if ( null != onRefreshListener ) {
          onRefreshListener.onHandle( null );
        }
      }
    });
    leftPanel.add( refreshBtn );
    
    HorizontalPanel filterPanel = new HorizontalPanel();
    filterPanel.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
    Label l = new Label( Messages.getString("filterBy") ); //$NON-NLS-1$
    filterPanel.add( l );
    
    filterList = new ListBox();
    filterList.setVisibleItemCount( 1 );

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
    btn.addStyleName( "toolbarBtn" ); //$NON-NLS-1$
    btn.setTitle( hoverText );
    
    return btn;
  }
  
  public void clearFilters() {
    filterList.clear();
  }
  
  public void addFilterItem( String name ) {
    filterList.addItem( name );
  }
  
  /**
   * 
   * @return null if list is emtpy, otherwise the value of the selected item
   */
  public String getFilterValue() {
    return ( 0 == filterList.getItemCount() )
      ? null
      : filterList.getValue( filterList.getSelectedIndex() );
  }
  
  public void setFilterValue( String value ) {
    int selectedIdx = 0;
    for ( int ii=0; ii<filterList.getItemCount(); ++ii ) {
      String filterValue = filterList.getValue( ii );
      if ( filterValue.equals( value ) ) {
        selectedIdx = ii;
        break;
      }
    }
    filterList.setSelectedIndex( selectedIdx );
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

  public PushButton getRefreshBtn() {
    return refreshBtn;
  }
}
