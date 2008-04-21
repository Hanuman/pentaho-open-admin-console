package org.pentaho.pac.client.common.ui;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class GroupBox extends DockPanel{
  private Label title;
  private Grid grid;
  
  public GroupBox(String title){
    this.setStylePrimaryName("GroupBox");
    
    this.title = new Label(title);
    this.title.setStylePrimaryName("groupBoxHeader");
    
    grid = new Grid(5, 5);
    grid.setWidth("100%");
    grid.setHeight("100%");
    grid.setCellPadding(0);
    grid.setCellSpacing(0);
    
    grid.setWidget(0,1,this.title);
    
    grid.getCellFormatter().setStyleName(0, 0, "groupbox-nw");
    grid.getCellFormatter().setStyleName(0, 1, "groupbox-n");
    grid.getCellFormatter().setStyleName(0, 2, "groupbox-ne");
    grid.getCellFormatter().setStyleName(1, 0, "groupbox-w");
    grid.getCellFormatter().setStyleName(1, 1, "groupbox-c");
    grid.getCellFormatter().setStyleName(1, 2, "groupbox-e");
    grid.getCellFormatter().setStyleName(2, 0, "groupbox-sw");
    grid.getCellFormatter().setStyleName(2, 1, "groupbox-s");
    grid.getCellFormatter().setStyleName(2, 2, "groupbox-se");
    
    this.add(grid, DockPanel.CENTER);

  }
  public GroupBox(String title, Widget widget){
    this(title);
    this.setContent(widget);
  }
  
  public void setContent(Widget widget){
    grid.setWidget(1,1, widget);
    
    
  }
}

  