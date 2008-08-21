package org.pentaho.pac.client.scheduler.view;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.gwt.filechooser.client.FileChooser;
import org.pentaho.gwt.widgets.client.controls.ErrorLabel;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;

public class SolutionRepositoryActionSequenceListEditor extends VerticalPanel {
  
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  
  private TableEditor actionsEditor = new TableEditor( MSGS.fileLocationLabel() );

  private boolean bIsSingleSelect = true;

  public SolutionRepositoryActionSequenceListEditor() {
    super();

    setStylePrimaryName( "solRepItemPicker" ); //$NON-NLS-1$
    actionsEditor.setStyleName( "actionsEditor" ); //$NON-NLS-1$
    //actionsEditor.setWidth( "100%" ); // TODO sbarkdull, get in css? //$NON-NLS-1$
    //actionsEditor.setHeight( "100%" ); // TODO sbarkdull, get in css? //$NON-NLS-1$
    add( actionsEditor );
    setCellHeight( actionsEditor, "100%" );
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
    assert friendlyNames.size() == names.size() : "size of lists must be identical"; //$NON-NLS-1$
    
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

  public boolean isSingleSelect() {
    return bIsSingleSelect;
  }

  public void setSingleSelect(boolean isSingleSelect) {
    bIsSingleSelect = isSingleSelect;
  }
  
  public void setFocus() {
    // TODO sbarkdull
    System.out.println( "implement setFocus on SolutionRepositoryActionSequenceListEditor()" );
//    actionsTA.setFocus( true );
//    actionsTA.setSelectionRange( 0, actionsTA.getText().length() );
  }
  
  public void setOnAddClickedHandler( ICallback<TableEditor> handler ) {
    actionsEditor.setOnAddClickedHandler( handler );
  }
  
  public void setOnDeleteClickedHandler( ICallback<TableEditor> handler ) {
    actionsEditor.setOnDeleteClickedHandler( handler );
  }
}
