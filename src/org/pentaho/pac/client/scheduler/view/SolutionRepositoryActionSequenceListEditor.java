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
package org.pentaho.pac.client.scheduler.view;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.gwt.widgets.client.controls.TableEditor;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.i18n.Messages;

import com.google.gwt.user.client.ui.SimplePanel;

public class SolutionRepositoryActionSequenceListEditor extends SimplePanel {

  private ICallback<SolutionRepositoryActionSequenceListEditor> onLoadingCompleteHandler = null;
  
  private TableEditor actionsEditor = new TableEditor( Messages.getString("fileLocationLabel") ); //$NON-NLS-1$

  public SolutionRepositoryActionSequenceListEditor() {
    super();

    setStylePrimaryName( "solutionRepositoryActionSequenceListEditor" ); //$NON-NLS-1$
    actionsEditor.setStyleName( "actionsEditor" ); //$NON-NLS-1$
    actionsEditor.setHeight("100%"); //$NON-NLS-1$
    add( actionsEditor );
  }
  
  public void reset() {
    actionsEditor.removeAll();
  }
  
  /**
   * Get a comma separated list of the solution paths
   * @return
   */
  public String getActionsAsString() {
    String actionsList = ""; //$NON-NLS-1$
    for ( int ii=0; ii<actionsEditor.getItemCount(); ++ii ) {
      actionsList += actionsEditor.getItemValue( ii );
      if ( ii < actionsEditor.getItemCount()-1 ) {
        actionsList += ","; //$NON-NLS-1$
      }
    }
    return actionsList;
  }
  
  public List<String> getActionsAsList() {
    List<String> l = new ArrayList<String>();
    for ( int ii=0; ii<actionsEditor.getItemCount(); ++ii ) {
      l.add( actionsEditor.getItemValue( ii ) );
    }
    return l;
  }
  
  public void setActionsAsList( List<String> friendlyNames, List<String> names ) {
    assert friendlyNames == null || friendlyNames.size() == names.size() : Messages.getString("sizeListMustBeIdentical"); //$NON-NLS-1$
    
    actionsEditor.removeAll();
    if ( null != friendlyNames ) {
      for ( int ii=0; ii<friendlyNames.size(); ++ii ) {
        actionsEditor.addItem( friendlyNames.get( ii ), names.get( ii ) );
      }
    }
  }
  
  public void addAction( String friendlyName, String name ) {
    actionsEditor.addItem( friendlyName, name );
  }
  
//  public void removeAction( String action ) {
//    actionsEditor.getIte
//    actionsEditor.remove( idx );
//  }
//  
//  private static final int NOT_FOUND = -1;
//  
//  private int findActionIdxByName( String action ) {
//    for ( int ii=0; ii<actionsEditor.getItemCount(); ++ii ) {
//      String currentAction = actionsEditor.get
//      if () {
//        
//      }
//    }
//    return NOT_FOUND;
//  }
  
  public void setActionsError( String errorMsg ) {
    actionsEditor.setErrorMsg( errorMsg );
  }
  
  public void setFocus() {
    actionsEditor.setFocus();
  }
  
  public void setOnAddClickedHandler( ICallback<TableEditor> handler ) {
    actionsEditor.setOnAddClickedHandler( handler );
  }
  
  public void setOnDeleteClickedHandler( ICallback<TableEditor> handler ) {
    actionsEditor.setOnDeleteClickedHandler( handler );
  }
  
  public void setOnSelectHandler( ICallback<TableEditor> handler ) {
    actionsEditor.setOnSelectHandler( handler );
  }
  
  public void setAddBtnEnabled( boolean enabled ) {
    actionsEditor.setAddBtnEnabled( enabled );
  }
  
  public void setDeleteBtnEnabled( boolean enabled ) {
    actionsEditor.setDeleteBtnEnabled( enabled );
  }
  
  public void setMessage( String message ) {
    actionsEditor.setMessage( message );
  }
  
  public void clearMessage() {
    actionsEditor.clearMessage();
  }
  
  public void setOnLoadingCompleteHandler( ICallback<SolutionRepositoryActionSequenceListEditor> handler ) {
    onLoadingCompleteHandler = handler;
  }
  
  public void fireLoadingComplete() {
    if ( null != onLoadingCompleteHandler ) {
      onLoadingCompleteHandler.onHandle( this );
    }
  }
}
