package org.pentaho.pac.client.ui;

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
    
    grid.getCellFormatter().setStylePrimaryName(0, 0, "groupbox-nw");
    grid.getCellFormatter().setStylePrimaryName(0, 1, "groupbox-n");
    grid.getCellFormatter().setStylePrimaryName(0, 2, "groupbox-ne");
    grid.getCellFormatter().setStylePrimaryName(1, 0, "groupbox-w");
    grid.getCellFormatter().setStylePrimaryName(1, 1, "groupbox-c");
    grid.getCellFormatter().setStylePrimaryName(1, 2, "groupbox-e");
    grid.getCellFormatter().setStylePrimaryName(2, 0, "groupbox-sw");
    grid.getCellFormatter().setStylePrimaryName(2, 1, "groupbox-s");
    grid.getCellFormatter().setStylePrimaryName(2, 2, "groupbox-se");
    
    this.add(grid, DockPanel.CENTER);
//    
//    <table height="100%" width="100%" cellpadding="0" cellspacing="0" border="0">
//    <tr>
//      <td width="10" height="28">
//        <img src="style/images/dialog_nw.png" width="10" height="28"/>
//      </td>
//      <td background="style/images/dialog_n.png" width="100%"></xsl:text><br/></td>
//      <td width="10" height="28">
//        <img src="style/images/dialog_ne.png" width="10" height="28"/>
//      </td>
//    </tr>
//    <tr>
//      <td background="style/images/dialog_w.png" width="10">
//      </td>
//      <td height="100%" width="100%" bgcolor="#ffffff" style="padding:6px;">
//
//      </td>
//      <td background="style/images/dialog_e.png" width="10">
//      </td>
//    </tr>
//    <tr>
//      <td width="10" height="11" style="height:11px;width:10px; overflow:hidden">
//        <img src="style/images/dialog_sw.png" width="10" height="11"/><br/>
//      </td>
//      <td background="style/images/dialog_s.png" align="right">
//      </td>
//      <td width="10" height="11" style="height:11px;width:10px; overflow:hidden">
//        <img src="style/images/dialog_se.png" width="10" height="11"/><br/>
//      </td>
//    </tr>
//  </table>
//    
  }
  public GroupBox(String title, Widget widget){
    this(title);
    this.setContent(widget);
  }
  
  public void setContent(Widget widget){
    grid.setWidget(1,1, widget);
    
    
  }
}

  