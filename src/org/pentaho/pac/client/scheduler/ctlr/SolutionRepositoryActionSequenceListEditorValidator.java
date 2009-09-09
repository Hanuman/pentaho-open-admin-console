/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
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
 * Created  
 * @author Steven Barkdull
 */
package org.pentaho.pac.client.scheduler.ctlr;

import java.util.List;

import org.pentaho.pac.client.i18n.Messages;
import org.pentaho.pac.client.scheduler.view.SolutionRepositoryActionSequenceListEditor;

public class SolutionRepositoryActionSequenceListEditorValidator implements IUiValidator {
  
  private SolutionRepositoryActionSequenceListEditor solRepActionSequenceListEditor = null;
  private boolean isSubscriptionSched;
  
  public SolutionRepositoryActionSequenceListEditorValidator(SolutionRepositoryActionSequenceListEditor solRepActionSequenceListEditor,
      boolean isSubscriptionSched ) {
    this.solRepActionSequenceListEditor = solRepActionSequenceListEditor;
    this.isSubscriptionSched = isSubscriptionSched;
  }

  public boolean isValid() {
    boolean isValid = true;
    if(!isSubscriptionSched) {
      List<String> actionList = solRepActionSequenceListEditor.getActionsAsList();
      if (actionList.size() != 1 ) {
        isValid = false;
        solRepActionSequenceListEditor.setActionsError( Messages.getString("onlyOneActionSequence") ); //$NON-NLS-1$
      } else if ( actionList.size() <= 0 ) {
        isValid = false;
        solRepActionSequenceListEditor.setActionsError( Messages.getString("actionSequenceCannotBeEmpty") ); //$NON-NLS-1$
      }
    }
    return isValid;
  }

  public void clear() {
    solRepActionSequenceListEditor.setActionsError( null );
  }
}
