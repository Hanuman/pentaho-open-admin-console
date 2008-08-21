package org.pentaho.pac.client;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminConsoleMasterPanel extends DockPanel {
  VerticalPanel buttonPanel = new VerticalPanel();
  CommonTasksPanel commonTasksPanel = new CommonTasksPanel();
  VerticalPanel verticalPanel = new VerticalPanel();
  
  public AdminConsoleMasterPanel(){
    Label spacer = new Label();
    verticalPanel.add(spacer);
    verticalPanel.setCellHeight(spacer, "20px"); //$NON-NLS-1$   
    
    verticalPanel.add(buttonPanel);
    buttonPanel.setStyleName("leftTabPanel"); //$NON-NLS-1$
    
    spacer = new Label();
    verticalPanel.add(spacer);
    verticalPanel.setCellHeight(spacer, "50"); //$NON-NLS-1$
    
    Grid grid = new Grid(3,1);
    grid.setWidth("100%"); //$NON-NLS-1$
    grid.setHeight("100%"); //$NON-NLS-1$
    grid.setCellPadding(0);
    grid.setCellSpacing(0);
    grid.getCellFormatter().setVerticalAlignment(1,0,HasVerticalAlignment.ALIGN_TOP);
    
    grid.getCellFormatter().setStyleName(0, 0, "deckPanel-nw"); //$NON-NLS-1$
    grid.getCellFormatter().setStyleName(1, 0, "deckPanel-cw"); //$NON-NLS-1$
    grid.getCellFormatter().setStyleName(2, 0, "deckPanel-sw"); //$NON-NLS-1$
    
    grid.setWidget(1, 0, verticalPanel);
        
    add(grid, DockPanel.CENTER);
  }
  
  public void addButton(ToggleButton toggleButton) {
    buttonPanel.add(toggleButton);
  }
  
  public void addQuickLink(Hyperlink hyperlink) {
    commonTasksPanel.addQuickLink(hyperlink);
  }
  
  public void showQuickLinks(boolean show) {
    Widget parent = commonTasksPanel.getParent();
    if (show && (parent == null)) {
      verticalPanel.add(commonTasksPanel);
    } else if (!show && (parent != null)) {
      commonTasksPanel.removeFromParent();
    }
  }

}
