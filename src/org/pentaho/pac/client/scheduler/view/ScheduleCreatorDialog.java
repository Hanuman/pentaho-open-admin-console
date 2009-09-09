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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.i18n.Messages;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;

public class ScheduleCreatorDialog extends ConfirmDialog {
  private static final String SELECTED = "selected"; //$NON-NLS-1$
  
  public enum TabIndex {
    SCHEDULE( 0, Messages.getString("schedule") ), //$NON-NLS-1$
    SCHEDULE_ACTION( 1, Messages.getString("selectedFilesTabLabel") ); //$NON-NLS-1$
    
    private TabIndex( int value, String name ) {
      this.value = value;
      this.name = name;
    }
    private int value;
    private String name;
    
    private static TabIndex[] tabIndexAr = {
      SCHEDULE, 
      SCHEDULE_ACTION 
    };

    public static TabIndex get(int idx) {
      return tabIndexAr[idx];
    }
    
    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }
  }; // end enum
  
  private DualModeScheduleEditor scheduleEditor = new DualModeScheduleEditor();
  private SolutionRepositoryActionSequenceListEditor actionSequenceEditor = new SolutionRepositoryActionSequenceListEditor();
  private Label scheduleTabLabel = new Label( TabIndex.SCHEDULE.toString() );
  private Label scheduleActionTabLabel = new Label( TabIndex.SCHEDULE_ACTION.toString() );
  private Map<TabIndex, Label> tabLabelMap = new HashMap<TabIndex, Label>();
  private TabPanel tabPanel = new TabPanel();
  
  public ScheduleCreatorDialog() {
    super();
    this.setNoBorderOnClientPanel();
    setTitle( Messages.getString("scheduleCreator") ); //$NON-NLS-1$
    
    tabPanel.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
    
    actionSequenceEditor.setWidth( "100%" ); //$NON-NLS-1$
    actionSequenceEditor.setHeight( "100%" ); //$NON-NLS-1$
    
    // tabPanel.setStylePrimaryName( "schedulerTabPanel" ); //$NON-NLS-1$
    tabPanel.add( scheduleEditor, scheduleTabLabel );
    tabPanel.add( actionSequenceEditor, scheduleActionTabLabel );
    DeckPanel dp = tabPanel.getDeckPanel();
    dp.setStyleName( "scheduleCreatorDeckPanel" ); //$NON-NLS-1$
    dp.addStyleName("gwt-TabPanelBottom"); //$NON-NLS-1$
    
    scheduleTabLabel.setStylePrimaryName( "tabLabel" ); //$NON-NLS-1$
    scheduleActionTabLabel.setStylePrimaryName( "tabLabel" ); //$NON-NLS-1$
    tabLabelMap.put( TabIndex.SCHEDULE, scheduleTabLabel );
    tabLabelMap.put( TabIndex.SCHEDULE_ACTION, scheduleActionTabLabel );
    
    tabPanel.selectTab( TabIndex.SCHEDULE.value() );
    
    tabPanel.addTabListener( new TabListener() {
      public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
        return true;
      }
      public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
        for ( Map.Entry<TabIndex,Label> me : tabLabelMap.entrySet() ) {
          Label l = me.getValue();
          l.removeStyleDependentName( SELECTED );
        }
        Label l = tabLabelMap.get( TabIndex.get(  tabIndex ) );
        l.addStyleDependentName( SELECTED );
        switch (TabIndex.get( tabIndex) ) {
          case SCHEDULE:
            scheduleEditor.setFocus();
            break;
          case SCHEDULE_ACTION:
            actionSequenceEditor.setFocus();
            break;
        }
      }
    });
    
    addWidgetToClientArea( tabPanel );
  }

  public void setPanelSize(String width, String height){
    setSize(width, height);
    
    DeckPanel dp = tabPanel.getDeckPanel();
    dp.setSize(width, height);    
  }

  public DualModeScheduleEditor getScheduleEditor() {
    return scheduleEditor;
  }

  public SolutionRepositoryActionSequenceListEditor getSolutionRepositoryActionSequenceEditor() {
    return actionSequenceEditor;
  }
  
  public void reset( Date d ) {
    scheduleEditor.reset( d );
    actionSequenceEditor.reset();
    
    tabPanel.selectTab( TabIndex.SCHEDULE.value() );
  }
  
  public void setSelectedTab( TabIndex tabKey ) {
    tabPanel.selectTab( tabKey.value() );
  }
  
  public TabIndex getSelectedTab() {
    return TabIndex.get( tabPanel.getTabBar().getSelectedTab() );
  }
  
  public void setTabError( TabIndex tabKey ) {
    tabLabelMap.get(tabKey).setStylePrimaryName( "tabLabelError" ); //$NON-NLS-1$
  }
  
  public void clearTabError() {
    for ( Map.Entry<TabIndex, Label> me : tabLabelMap.entrySet() ) {
      me.getValue().setStylePrimaryName( "tabLabel" ); //$NON-NLS-1$
    }
  }
}
