package org.pentaho.pac.client.common.ui.widget;

import org.pentaho.pac.client.common.util.StringUtils;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ValidationLabel extends VerticalPanel {

  private Label label = null;
  private Label errorLabel = null;
  
  public ValidationLabel() {
    this( "" ); //$NON-NLS-1$
  }
  
  public ValidationLabel( String strLabel ) {
    errorLabel = new Label();
    errorLabel.setStyleName( "errorLabel" ); //$NON-NLS-1$
    errorLabel.setVisible( false );
    add( errorLabel );
    
    label = new Label( strLabel );
    add( label );
  }
  
  /**
   * Set an error message to be associated with the label
   * 
   * @param msg String if null, clear the error message, else set
   * the error message to <param>mgs</param>.
   */
  public void setErrorMsg( String msg ) {
    if ( !StringUtils.isEmpty( msg ) ) {
      errorLabel.setText( msg );
      errorLabel.setVisible( true );
    } else {
      errorLabel.setText( "" ); //$NON-NLS-1$
      errorLabel.setVisible( false );
    }
  }
  
  public void setText( String strLabel ) {
    label.setText( strLabel );
  }
  
  public String getText() {
    return label.getText();
  }
}
