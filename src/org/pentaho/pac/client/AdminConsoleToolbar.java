package org.pentaho.pac.client;

import org.pentaho.pac.client.utils.PacImageBundle;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminConsoleToolbar extends HorizontalPanel {
  private Label statusLabel;
  private Timer statusTimer = null;
  private SimplePanel serverIcon = new SimplePanel();
  private Image statusIcon = PacImageBundle.getBundle().statusWorkingIcon().createImage();
  HorizontalPanel buttonsPanel = new HorizontalPanel();
  
  public void addImageButton(Image image) {
    buttonsPanel.setStyleName("buttons"); //$NON-NLS-1$
    buttonsPanel.add(image);
  }
  public AdminConsoleToolbar(){
    super();

    setStyleName("toolbar"); //$NON-NLS-1$
    
    //Left end-cap
    SimplePanel leftCap = new SimplePanel();
    leftCap.setStyleName("toolbar_left"); //$NON-NLS-1$
    add(leftCap);
    this.setCellWidth(leftCap, "5px"); //$NON-NLS-1$
    
    //the body of the toolbar
    HorizontalPanel centerPanel = new HorizontalPanel();
    centerPanel.setStyleName("toolbar_center"); //$NON-NLS-1$
    add(centerPanel);
    
    //Right end-cap
    SimplePanel rightCap = new SimplePanel();
    rightCap.setStyleName("toolbar_right"); //$NON-NLS-1$
    add(rightCap);
    this.setCellWidth(rightCap, "6px"); //$NON-NLS-1$
    
    SimplePanel indicatorsPanel = new SimplePanel();
    indicatorsPanel.setStyleName("toolBarIndicators"); //$NON-NLS-1$
    centerPanel.add(indicatorsPanel);
    
    SimplePanel indicatorsLeft = new SimplePanel();
    indicatorsLeft.setStyleName("indicators_left"); //$NON-NLS-1$
    indicatorsPanel.add(indicatorsLeft);
    
    HorizontalPanel indicatorsRight = new HorizontalPanel();
    indicatorsRight.setStyleName("indicators_right"); //$NON-NLS-1$
    indicatorsLeft.add(indicatorsRight);
   
    statusLabel = new Label(PentahoAdminConsole.MSGS.toolbarStatus());
    statusLabel.setStyleName("indicators_label"); //$NON-NLS-1$

    serverIcon.setStyleName( "biServerDeadIcon" ); //$NON-NLS-1$
    serverIcon.setTitle( PentahoAdminConsole.MSGS.biServerDead() );
    
    HorizontalPanel indicators = new HorizontalPanel();
    indicators.setStyleName("indicators"); //$NON-NLS-1$
    
    indicators.add(statusLabel);
    indicators.add(serverIcon);
    indicators.add(statusIcon);
    
    indicatorsRight.add(indicators);
    Image helpImage = PacImageBundle.getBundle().helpIcon().createImage();
    helpImage.setTitle(PentahoAdminConsole.MSGS.help());
    Image createImage = PacImageBundle.getBundle().refreshIcon().createImage();
    createImage.setTitle(PentahoAdminConsole.MSGS.resetServer());
    helpImage.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
      }
    });

    addImageButton(createImage);
    addImageButton(helpImage);
    
    centerPanel.add(buttonsPanel);
    centerPanel.setCellHorizontalAlignment(buttonsPanel, HorizontalPanel.ALIGN_RIGHT);
    centerPanel.setCellVerticalAlignment(buttonsPanel, HorizontalPanel.ALIGN_MIDDLE);
    
    statusTimer = new Timer() {
      public void run()
      {
        PacServiceFactory.getPacService().isBiServerAlive(
            new AsyncCallback<Object>() {
              public void onSuccess( Object isAlive ) {
                serverIcon.setStyleName( "biServerAliveIcon" ); //$NON-NLS-1$
                serverIcon.setTitle( PentahoAdminConsole.MSGS.biServerAlive() );
              }
              public void onFailure(Throwable caught) {
                serverIcon.setStyleName( "biServerDeadIcon" ); //$NON-NLS-1$
                serverIcon.setTitle( PentahoAdminConsole.MSGS.biServerDead() );
              }
            }
          );
      }
    };
    
    PacServiceFactory.getPacService().getBiServerStatusCheckPeriod(
      new AsyncCallback<Integer>() {
        public void onSuccess( Integer checkPeriod ) {
          if ( checkPeriod > 0 ) {
            statusTimer.scheduleRepeating( checkPeriod );
          }
        }
        public void onFailure(Throwable caught) {
          // otherwise we don't know what the status check period is, so don't schedule anything
        }
      }
    );
  }
  
  public HorizontalPanel getButtonPanel() {
    return buttonsPanel;
  }

}
