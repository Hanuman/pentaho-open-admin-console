package org.pentaho.pac.client.scheduler.view;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.gwt.filechooser.client.FileChooser;
import org.pentaho.gwt.widgets.client.controls.ErrorLabel;
import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SolutionRepositoryItemPicker extends VerticalPanel {
  
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  
  private ListBoxEx actionsLB = new ListBoxEx();

  private ErrorLabel actionsLabel = null;
  private boolean bIsSingleSelect = true;

  public SolutionRepositoryItemPicker() {
    super();

    setStylePrimaryName( "solRepItemPicker" ); //$NON-NLS-1$
//    actionsTA.setWidth( "100%" ); //$NON-NLS-1$
//    actionsTA.setHeight( "20ex" ); //$NON-NLS-1$
    actionsLabel = new ErrorLabel( new Label( MSGS.commaSeparatedList() ) );
    add( actionsLabel );
    
    FileChooser f = new FileChooser();
    
    add( actionsLB );
  }
  
  public void reset() {
    actionsLB.removeAll();
  }
  
  /**
   * Get a comma separated list of the solution paths
   * @return
   */
  public String getActionsAsString() {
    String actionsList = "";
    for ( int ii=0; ii<actionsLB.getItemCount(); ++ii ) {
      actionsList += actionsLB.getValue( ii );
      if ( ii < actionsLB.getItemCount()-1 ) {
        actionsList += ",";
      }
    }
    return actionsList;
  }
  
  public List<String> getActionsAsList() {
    List<String> l = new ArrayList<String>();
    for ( int ii=0; ii<actionsLB.getItemCount(); ++ii ) {
      l.add( actionsLB.getValue( ii ) );
    }
    return l;
  }
  
  /*
  public void setActionsAsString( String actions ) {
    String[] actionAr = actions.split( "," );
    for ( String action : actionAr ) {
      actionsLB.addItem(item, value)( action );
    }
  }
  */
  
  public void setActionsAsList( List<String> actions ) {
    for ( String action : actions ) {
      actionsLB.addItem( action );
    }
  }
  
  public void setActionsError( String errorMsg ) {
    actionsLabel.setErrorMsg( errorMsg );
  }

  public boolean isSingleSelect() {
    return bIsSingleSelect;
  }

  public void setSingleSelect(boolean isSingleSelect) {
    bIsSingleSelect = isSingleSelect;
  }
  
  public void setFocus() {
    // TODO sbarkdull
    System.out.println( "implement setFocus on SolutionRepositoryITemPicker()" );
//    actionsTA.setFocus( true );
//    actionsTA.setSelectionRange( 0, actionsTA.getText().length() );
  }
}
