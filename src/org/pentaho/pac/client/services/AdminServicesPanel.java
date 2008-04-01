package org.pentaho.pac.client.services;

import org.pentaho.pac.client.PacService;
import org.pentaho.pac.client.PacServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminServicesPanel extends VerticalPanel implements ClickListener {

  Button refreshSolutionRepositoryBtn = new Button("Refresh Solution Repository");
  Button cleanRepositoryBtn = new Button("Remove Stale Conent");
  Button clearMondrianDataCacheBtn = new Button("Purge Mondrian Data Cache");
  Button clearMondrianSchemaCacheBtn = new Button("Purge Mondrian Schema Cahce");
  Button scheduleRepositoryCleaningBtn = new Button("Schedule Daily Repository Cleaning");
  Button resetRepositoryBtn = new Button("Restore Default File Permissions");
  Button refreshSystemSettingsBtn = new Button("Refresh System Settings");
  Button executeGlobalActionsBtn = new Button("Execute Global Actions");
  Button refreshReportingMetadataBtn = new Button("Refresh Reporting Metadata");
  PacServiceAsync pacService;
  
  public AdminServicesPanel() {
    Grid grid = new Grid(5, 2);
    grid.setWidth("100%");
    
    grid.setWidget(0, 0, refreshSolutionRepositoryBtn);
    grid.setWidget(0, 1, cleanRepositoryBtn);
    grid.setWidget(1, 0, clearMondrianDataCacheBtn);
    grid.setWidget(1, 1, clearMondrianSchemaCacheBtn);
    grid.setWidget(2, 0, scheduleRepositoryCleaningBtn);
    grid.setWidget(2, 1, resetRepositoryBtn);
    grid.setWidget(3, 0, refreshSystemSettingsBtn);
    grid.setWidget(3, 1, executeGlobalActionsBtn);
    grid.setWidget(4, 0, refreshReportingMetadataBtn);
    
    refreshSolutionRepositoryBtn.setWidth("100%");
    cleanRepositoryBtn.setWidth("100%");
    clearMondrianDataCacheBtn.setWidth("100%");
    clearMondrianSchemaCacheBtn.setWidth("100%");
    scheduleRepositoryCleaningBtn.setWidth("100%");
    resetRepositoryBtn.setWidth("100%");
    refreshSystemSettingsBtn.setWidth("100%");
    executeGlobalActionsBtn.setWidth("100%");
    refreshReportingMetadataBtn.setWidth("100%");
    
    refreshSolutionRepositoryBtn.addClickListener(this);
    cleanRepositoryBtn.addClickListener(this);
    clearMondrianDataCacheBtn.addClickListener(this);
    clearMondrianSchemaCacheBtn.addClickListener(this);
    scheduleRepositoryCleaningBtn.addClickListener(this);
    resetRepositoryBtn.addClickListener(this);
    refreshSystemSettingsBtn.addClickListener(this);
    executeGlobalActionsBtn.addClickListener(this);
    refreshReportingMetadataBtn.addClickListener(this);
    
    add(grid);
  }

  public void onClick(Widget sender) {
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Services");
        Button okButton = new Button("OK");
        okButton.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            dialogBox.hide();
          }
          
        });
        HorizontalPanel footerPanel = new HorizontalPanel();
        footerPanel.add(okButton);
        
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(new Label(result.toString()));
        verticalPanel.add(footerPanel);
        
        dialogBox.setWidget(verticalPanel);
        dialogBox.show();
      }

      public void onFailure(Throwable caught) {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Services");
        Button okButton = new Button("OK");
        okButton.addClickListener(new ClickListener() {
          public void onClick(Widget sender) {
            dialogBox.hide();
            
          }
          
        });
        HorizontalPanel footerPanel = new HorizontalPanel();
        footerPanel.add(okButton);
        
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(new Label(caught.getMessage()));
        verticalPanel.add(footerPanel);
        
        dialogBox.setWidget(verticalPanel);
        dialogBox.show();
      }
    };

    if (sender == refreshSolutionRepositoryBtn) {
      getPacService().refreshSolutionRepository(callback);
    } else if (sender == cleanRepositoryBtn) {
      getPacService().cleanRepository(callback);
    } else if (sender == clearMondrianDataCacheBtn) {
      getPacService().clearMondrianDataCache(callback);
    } else if (sender == clearMondrianSchemaCacheBtn) {
      getPacService().clearMondrianSchemaCache(callback);
    } else if (sender == scheduleRepositoryCleaningBtn) {
      getPacService().scheduleRepositoryCleaning(callback);
    } else if (sender == resetRepositoryBtn) {
      getPacService().resetRepository(callback);
    } else if (sender == refreshSystemSettingsBtn) {
      getPacService().refreshSystemSettings(callback);
    } else if (sender == executeGlobalActionsBtn) {
      getPacService().executeGlobalActions(callback);
    } else if (sender == refreshReportingMetadataBtn) {
      getPacService().refreshReportingMetadata(callback);
    }
  }
  
  private PacServiceAsync getPacService() {
    if (pacService == null) {
      pacService = (PacServiceAsync) GWT.create(PacService.class);
      ServiceDefTarget endpoint = (ServiceDefTarget) pacService;
      String moduleRelativeURL = GWT.getModuleBaseURL() + "pacsvc";
      endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    return pacService;
  }
  
}
