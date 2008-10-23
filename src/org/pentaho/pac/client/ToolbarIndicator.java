package org.pentaho.pac.client;

import org.pentaho.pac.client.utils.PacImageBundle;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToolbarIndicator  extends HorizontalPanel{
  protected Label statusLabel;
  protected VerticalPanel imageHolderPanel;
  private Image serverAliveImage;
  private Image serverDeadImage;
  
  public ToolbarIndicator() {
    imageHolderPanel = new VerticalPanel();
    serverAliveImage = PacImageBundle.getBundle().serverAliveIcon().createImage();
    serverAliveImage.setTitle(PentahoAdminConsole.MSGS.biServerAlive());
    serverDeadImage = PacImageBundle.getBundle().serverDeadIcon().createImage();
    serverDeadImage.setTitle(PentahoAdminConsole.MSGS.biServerDead());
    statusLabel = new Label(PentahoAdminConsole.MSGS.toolbarStatus());
    statusLabel.setStyleName("indicators_label"); //$NON-NLS-1$
    imageHolderPanel.add(serverDeadImage);
    setStyleName("indicators"); //$NON-NLS-1$
  }

  protected void buildToolbarIndicator() {
    add(statusLabel);
    add(imageHolderPanel);    
  }
  
  public void setStatusLabel(Label statusLabel) {
    this.statusLabel = statusLabel;
  }
  
  public Label getStatusLabel() {
    return statusLabel;
  }
  
  public void displayServerAlive() {
      serverDeadImage.removeFromParent();
      imageHolderPanel.add(serverAliveImage);
  }

  public void displayServerDead() {
    serverAliveImage.removeFromParent();
    imageHolderPanel.add(serverDeadImage);
  }

}
