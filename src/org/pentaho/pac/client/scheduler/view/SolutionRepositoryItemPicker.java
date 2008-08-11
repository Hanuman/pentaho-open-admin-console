package org.pentaho.pac.client.scheduler.view;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.gwt.widgets.client.controls.ErrorLabel;
import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SolutionRepositoryItemPicker extends VerticalPanel {
  
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();

  private TextArea actionsTA = new TextArea();

  private ErrorLabel actionsLabel = null;
  private boolean bIsSingleSelect = true;

  public SolutionRepositoryItemPicker() {
    super();

    setStylePrimaryName( "solRepItemPicker" ); //$NON-NLS-1$
    actionsTA.setWidth( "100%" ); //$NON-NLS-1$
    actionsTA.setHeight( "20ex" ); //$NON-NLS-1$
    actionsLabel = new ErrorLabel( new Label( MSGS.commaSeparatedList() ) );
    add( actionsLabel );
    add( actionsTA );
  }
  
  public void reset() {    
    actionsTA.setText( "" ); //$NON-NLS-1$
  }
  
  public String getActionsAsString() {
    return actionsTA.getText();
  }
  
  public List<String> getActionsAsList() {
    String[] actions = actionsTA.getText().split( "," ); //$NON-NLS-1$
    List<String> l = new ArrayList<String>();
    if ( actions.length > 1 || ( 1 == actions.length && !StringUtils.isEmpty( actions[0]) ) ) {
      for ( String action : actions ) {
        l.add( action.trim() );
      }
    }
    return l;
  }
  
  public void setActionsAsString( String actions ) {
    actionsTA.setText( actions );
  }
  
  public void setActionsAsList( List<String> actions ) {
    StringBuilder strBldr = new StringBuilder();
    int numActions = actions.size();
    for ( int ii=0; ii<numActions-1; ++ii ) {
      String action = actions.get( ii );
      strBldr.append( action ).append( "," ); //$NON-NLS-1$
    }
    if ( numActions > 0 ) {
      String action = actions.get( numActions-1 );
      strBldr.append( action );
    }
    actionsTA.setText( strBldr.toString() );
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
    actionsTA.setFocus( true );
    actionsTA.setSelectionRange( 0, actionsTA.getText().length() );
  }
}
