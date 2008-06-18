package org.pentaho.pac.client.scheduler;

import org.pentaho.pac.client.common.ui.widget.ErrorLabel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SolutionRepositoryItemPicker extends VerticalPanel {

  private TextBox solutionTb = new TextBox();
  private TextBox pathTb = new TextBox();
  private TextBox actionTb = new TextBox();

  private ErrorLabel solutionLabel = null;
  private ErrorLabel pathLabel = null;
  private ErrorLabel actionLabel = null;

  public SolutionRepositoryItemPicker() {
    super();
    
    solutionLabel = new ErrorLabel( new Label( "Solution:" ) );
    add( solutionLabel );
    add( solutionTb );

    pathLabel = new ErrorLabel( new Label( "Path:" ) );
    add( pathLabel );
    add( pathTb );

    actionLabel = new ErrorLabel( new Label( "Action:" ) );
    add( actionLabel );
    add( actionTb );
  }
  
  public void reset() {    
    solutionTb.setText( "" ); //$NON-NLS-1$
    pathTb.setText( "" ); //$NON-NLS-1$
    actionTb.setText( "" ); //$NON-NLS-1$
  }
  
  public String getSolution() {
    return solutionTb.getText();
  }
  
  public void setSolution( String solution ) {
    solutionTb.setText( solution );
  }
  
  public String getPath() {
    return pathTb.getText();
  }
  
  public void setPath( String path ) {
    pathTb.setText( path );
  }
  
  public String getAction() {
    return actionTb.getText();
  }
  
  public void setAction( String action ) {
    actionTb.setText( action );
  }
  
  public void setSolutionError( String errorMsg ) {
    solutionLabel.setErrorMsg( errorMsg );
  }
  
  public void setPathError( String errorMsg ) {
    pathLabel.setErrorMsg( errorMsg );
  }
  
  public void setActionError( String errorMsg ) {
    actionLabel.setErrorMsg( errorMsg );
  }
  
}
