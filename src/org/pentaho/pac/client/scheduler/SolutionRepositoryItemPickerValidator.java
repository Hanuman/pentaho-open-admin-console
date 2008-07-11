package org.pentaho.pac.client.scheduler;

import java.util.List;

public class SolutionRepositoryItemPickerValidator implements IUiValidator {

  private SolutionRepositoryItemPicker solRepPicker = null;

  public SolutionRepositoryItemPickerValidator(SolutionRepositoryItemPicker solRepPicker) {
    this.solRepPicker = solRepPicker;
  }

  public boolean isValid() {
    boolean isValid = true;
    
    List<String> actionList = solRepPicker.getActionsAsList();
    if ( solRepPicker.isSingleSelect() && actionList.size() > 1 ) {
      isValid = false;
      solRepPicker.setActionsError( "Only allowed to specify one action sequence." );
    } else if ( actionList.size() <= 0 ) {
      isValid = false;
      solRepPicker.setActionsError( "Action sequence list cannot be empty." );
    }
    
    return isValid;
  }

  public void clear() {
    solRepPicker.setActionsError( null );
  }
}
