package org.pentaho.pac.client.scheduler;

import org.pentaho.pac.client.common.util.StringUtils;

public class SolutionRepositoryItemPickerValidator implements IUiValidator {

  private SolutionRepositoryItemPicker solRepPicker = null;

  public SolutionRepositoryItemPickerValidator(SolutionRepositoryItemPicker solRepPicker) {
    this.solRepPicker = solRepPicker;
  }

  public boolean isValid() {
    String errorMsg = null;
    boolean isValid = true;
    
    String solution = solRepPicker.getSolution();
    errorMsg = StringUtils.isEmpty( solution ) ? "Solution name cannot be empty." : null;
    isValid &= errorMsg == null;
    solRepPicker.setSolutionError( errorMsg );
    
    String path = solRepPicker.getPath();
    errorMsg = StringUtils.isEmpty( path ) ? "Path cannot be empty." : null;
    isValid &= errorMsg == null;
    solRepPicker.setPathError( errorMsg );
    
    String action = solRepPicker.getAction();
    errorMsg = StringUtils.isEmpty( action ) ? "Action name cannot be empty." : null;
    isValid &= errorMsg == null;
    solRepPicker.setActionError( errorMsg );
    
    return isValid;
  }

  public void clear() {
    // TODO Auto-generated method stub
    
  }
}
