package org.pentaho.pac.client;

import org.pentaho.pac.client.common.ui.dialog.MessageDialog;
import org.pentaho.pac.client.utils.PacImageBundle;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminConsoleToolbar extends HorizontalPanel {
  ToolbarIndicator toolbarIndicator;
  private Timer statusTimer = null;
  HorizontalPanel buttonsPanel = new HorizontalPanel();
  HorizontalPanel indicatorsRight = new HorizontalPanel();
  public void addImageButton(Image image) {
    buttonsPanel.setStyleName("buttons"); //$NON-NLS-1$
    buttonsPanel.add(image);
  }
  public AdminConsoleToolbar(){
    super();

    setStyleName("adminconsole-toolbar"); //$NON-NLS-1$
    
    //Left end-cap
    SimplePanel leftCap = new SimplePanel();
    leftCap.setStyleName("adminconsole-toolbar_left"); //$NON-NLS-1$
    add(leftCap);
    this.setCellWidth(leftCap, "5px"); //$NON-NLS-1$
    
    //the body of the toolbar
    HorizontalPanel centerPanel = new HorizontalPanel();
    centerPanel.setStyleName("adminconsole-toolbar_center"); //$NON-NLS-1$
    add(centerPanel);
    
    //Right end-cap
    SimplePanel rightCap = new SimplePanel();
    rightCap.setStyleName("adminconsole-toolbar_right"); //$NON-NLS-1$
    add(rightCap);
    this.setCellWidth(rightCap, "6px"); //$NON-NLS-1$
    
    SimplePanel indicatorsPanel = new SimplePanel();
    indicatorsPanel.setStyleName("toolBarIndicators"); //$NON-NLS-1$
    centerPanel.add(indicatorsPanel);
    
    SimplePanel indicatorsLeft = new SimplePanel();
    indicatorsLeft.setStyleName("indicators_left"); //$NON-NLS-1$
    indicatorsPanel.add(indicatorsLeft);
    
    indicatorsRight.setStyleName("indicators_right"); //$NON-NLS-1$
    indicatorsLeft.add(indicatorsRight);
    contructToolbarIndicator();
    setIndicators(toolbarIndicator);

    Image resetRepositoryImage = PacImageBundle.getBundle().refreshIcon().createImage();
    resetRepositoryImage.setTitle(PentahoAdminConsole.MSGS.resetServer());
    resetRepositoryImage.addClickListener(new ClickListener(){
      public void onClick(Widget sender) {
        final MessageDialog resetDialog = new MessageDialog(PentahoAdminConsole.MSGS.resetServer());
        resetDialog.setMessage("Resetting repository...");
        resetDialog.center();
        
        //TODO refresh all of the components in the EE console
        PacServiceFactory.getPacService().resetRepository(new AsyncCallback<Object>() {
          public void onSuccess(Object result) {
            MessageDialog successDialog = new MessageDialog("Success");
            successDialog.setMessage("Repository successfully reset.");
            
            resetDialog.hide();
            successDialog.center();
          }
          public void onFailure(Throwable caught) {
            MessageDialog failureDialog = new MessageDialog("Success");
            failureDialog.setMessage("Repository reset failed: " + caught.getMessage());
            
            resetDialog.hide();
            failureDialog.center();
          }
        });
      }
    });

    addImageButton(resetRepositoryImage);
    
    Image helpImage = PacImageBundle.getBundle().helpIcon().createImage();
    helpImage.setTitle(PentahoAdminConsole.MSGS.help());
    helpImage.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
        PacServiceFactory.getPacService().getHelpUrl(new AsyncCallback<String>(){

          public void onFailure(Throwable arg0) {
            // TODO Add message indicating failure to find help doc
          }

          public void onSuccess(String helpUrl) {
            Window.open(helpUrl, "UserGuide", ""); //$NON-NLS-1$ //$NON-NLS-2$
          }          
        });
      }
    });

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
                toolbarIndicator.displayServerAlive();
              }
              public void onFailure(Throwable caught) {
                toolbarIndicator.displayServerDead();
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
  public void setToolbarIndicator(ToolbarIndicator toolbarIndicator) {
    this.toolbarIndicator = toolbarIndicator;
  }
  public ToolbarIndicator getToolbarIndicator() {
    return this.toolbarIndicator;
  }
  public void contructToolbarIndicator() {
   ToolbarIndicator toolbarIndicator = new ToolbarIndicator();
   toolbarIndicator.buildToolbarIndicator();
   setToolbarIndicator(toolbarIndicator);
  }
  public void setIndicators(ToolbarIndicator toolbarIndicator) {
    indicatorsRight.add(toolbarIndicator);    
  }

}
