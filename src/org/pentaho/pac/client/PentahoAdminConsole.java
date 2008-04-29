package org.pentaho.pac.client;

import org.pentaho.pac.client.common.ui.MessageDialog;
import org.pentaho.pac.client.datasources.DataSourcesPanel;
import org.pentaho.pac.client.home.HomePanel;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.scheduler.SchedulerPanel;
import org.pentaho.pac.client.services.AdminServicesPanel;
import org.pentaho.pac.client.utils.PacImageBundle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PentahoAdminConsole implements EntryPoint, ClickListener, TabListener {
  
  //TODO can this be a "real" Java 5 enum?
  public static final int ADMIN_USERS_ROLES_TAB_INDEX = 0;
  public static final int ADMIN_DATA_SOURCES_TAB_INDEX = 1;
  public static final int ADMIN_SERVICES_TAB_INDEX = 2;
  public static final int ADMIN_SCHEDULER_TAB_INDEX = 3;
  public static PacLocalizedMessages pacLocalizedMessages = (PacLocalizedMessages)GWT.create(PacLocalizedMessages.class);

  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  ToggleButton adminToggleBtn = new ToggleButton(MSGS.administration());
  ToggleButton homeToggleBtn = new ToggleButton(MSGS.home());
  
  VerticalPanel leftVerticalPanel = new VerticalPanel();
  DockPanel centerPanel = new DockPanel();
  HorizontalPanel toolbar = new ConsoleToolbar();
  HorizontalPanel topPanel = new HorizontalPanel();
  
  TabPanel rightTabPanel = new TabPanel();
  DockPanel mainPanel = new DockPanel();
  DockPanel dockPanel = new DockPanel();
  DeckPanel deckPanel = new DeckPanel();
  DockPanel principalsPanel = new DockPanel();
  AdminServicesPanel servicesPanel = new AdminServicesPanel();
  UsersAndRolesPanel usersAndRolesPanel = new UsersAndRolesPanel();
  DataSourcesPanel dataSourcesPanel = new DataSourcesPanel();
	SchedulerPanel schedulerPanel = new SchedulerPanel();
  TabPanel adminTabPanel = new TabPanel();
  
  boolean securityInfoInitialized = false;  
  
  HomePanel homePanel;
  CommonTasks commonTasks;
  
  
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

    homePanel = new HomePanel("http://www.pentaho.com/console_home"); //$NON-NLS-1$
    commonTasks = new CommonTasks();
    
    homeToggleBtn.setStylePrimaryName("leftToggleButtons"); //$NON-NLS-1$
    adminToggleBtn.setStylePrimaryName("leftToggleButtons"); //$NON-NLS-1$
    
    homeToggleBtn.addClickListener(this);
    adminToggleBtn.addClickListener(this);
    
    Label spacer = new Label();
    leftVerticalPanel.add(spacer);
    leftVerticalPanel.setCellHeight(spacer, "20px"); //$NON-NLS-1$
    leftVerticalPanel.add(homeToggleBtn);
    leftVerticalPanel.add(adminToggleBtn);
    
    spacer = new Label();
    leftVerticalPanel.add(spacer);
    leftVerticalPanel.setCellHeight(spacer, "50"); //$NON-NLS-1$
    leftVerticalPanel.add(commonTasks);
    spacer = new Label();
    leftVerticalPanel.add(spacer);
    leftVerticalPanel.setCellHeight(spacer, "100%"); //$NON-NLS-1$

    leftVerticalPanel.setStyleName("leftTabPanel"); //$NON-NLS-1$
    
    // Order that things are placed in the tab panel is important. There are
    // static constants defined within this class that assume a given tab position
    // for each of the panels on the tab panel.
    adminTabPanel.add(usersAndRolesPanel, getLocalizedMessages().usersAndRoles());
    adminTabPanel.add(dataSourcesPanel, getLocalizedMessages().dataSources());
    adminTabPanel.add(servicesPanel, getLocalizedMessages().services());
    adminTabPanel.add(schedulerPanel, getLocalizedMessages().scheduler());

    deckPanel.setStyleName("deckPanel"); //$NON-NLS-1$
    deckPanel.add(homePanel);
    deckPanel.add(adminTabPanel);

    SimplePanel logo = new SimplePanel();
    logo.setStyleName("logo"); //$NON-NLS-1$
    topPanel.add(logo);
    topPanel.add(toolbar);
    
    adminTabPanel.setWidth("97%"); //$NON-NLS-1$
    adminTabPanel.setHeight("97%"); //$NON-NLS-1$
    adminTabPanel.getDeckPanel().setHeight("100%"); //$NON-NLS-1$
    
    usersAndRolesPanel.setWidth("100%"); //$NON-NLS-1$
    usersAndRolesPanel.setHeight("100%"); //$NON-NLS-1$
    dataSourcesPanel.setWidth("100%"); //$NON-NLS-1$
    dataSourcesPanel.setHeight("100%"); //$NON-NLS-1$
    servicesPanel.setWidth("100%"); //$NON-NLS-1$
    servicesPanel.setHeight("100%"); //$NON-NLS-1$
    deckPanel.setWidth("100%"); //$NON-NLS-1$
    deckPanel.setHeight("100%"); //$NON-NLS-1$
    adminTabPanel.selectTab(ADMIN_USERS_ROLES_TAB_INDEX);

    centerPanel.add(leftVerticalPanel, DockPanel.WEST);
    centerPanel.add(deckPanel, DockPanel.CENTER);
    centerPanel.setCellHeight(deckPanel, "100%"); //$NON-NLS-1$
    centerPanel.setCellWidth(deckPanel, "100%"); //$NON-NLS-1$
    
    //Main DockPanel
    mainPanel.setStyleName("main-panel"); //$NON-NLS-1$
    mainPanel.add(topPanel, DockPanel.NORTH);
    mainPanel.add(centerPanel, DockPanel.CENTER);
    mainPanel.setCellHeight(centerPanel, "100%"); //$NON-NLS-1$
    
    //attach all to the page
    RootPanel.get("canvas").add(mainPanel);  //$NON-NLS-1$
    deckPanel.showWidget(0);
    homeToggleBtn.setDown(true);
    
    adminTabPanel.addTabListener(this);
  }


public void onClick(Widget sender) {
    if (sender == homeToggleBtn) {
      if (homeToggleBtn.isDown()) {
        adminToggleBtn.setDown(false);
        deckPanel.showWidget(0);
      } else {
        homeToggleBtn.setDown(true);
      }
    } else if (sender == adminToggleBtn) {
      if (adminToggleBtn.isDown()) {
        homeToggleBtn.setDown(false);
        deckPanel.showWidget(1);
        int selectedTab = adminTabPanel.getDeckPanel().getVisibleWidget();
        onTabSelected( null, selectedTab );
      } else {
        adminToggleBtn.setDown(true);
      }
    } else {
      throw new RuntimeException( MSGS.invalidToggleButton() );
    }
  }
  
  private void initializeSecurityInfo() {
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
        usersAndRolesPanel.refresh();
        securityInfoInitialized = true;
      }
    
      public void onFailure(Throwable caught) {
        MessageDialog messageDialog = new MessageDialog( MSGS.error() );
        messageDialog.setMessage(getLocalizedMessages().securityRefreshError(caught.getMessage()));
        messageDialog.center();
      }
    };
    UserAndRoleMgmtService.instance().refreshSecurityInfo(callback);

  }

  public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
    return true;
  }
  
  public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
    switch (tabIndex) {
      case ADMIN_USERS_ROLES_TAB_INDEX:
        if (!securityInfoInitialized) {
          initializeSecurityInfo();
        }
        break;
      case ADMIN_DATA_SOURCES_TAB_INDEX: 
        if (!dataSourcesPanel.isInitialized()) {
          dataSourcesPanel.refresh();
        }
        break;
      case ADMIN_SERVICES_TAB_INDEX:
        // do nothing;
        break;
      case ADMIN_SCHEDULER_TAB_INDEX: 
        if (!schedulerPanel.isInitialized()) {
          schedulerPanel.refresh();
        }
        break;
      default:
        throw new RuntimeException(getLocalizedMessages().invalidTabIndex(Integer.toString(tabIndex)));
    }   
  }
  
  static public PacLocalizedMessages getLocalizedMessages() {
    return pacLocalizedMessages;
  }
  
  //TOP Toolbar
  private class ConsoleToolbar extends HorizontalPanel{
    
    private Label statusLabel;
    private Timer statusTimer = null;
    private SimplePanel serverIcon = new SimplePanel();
    private Image statusIcon = PacImageBundle.getBundle().statusWorkingIcon().createImage();
    
    public ConsoleToolbar(){
      super();

      setStyleName("toolbar"); //$NON-NLS-1$
     
      SimplePanel indicatorsPanel = new SimplePanel();
      indicatorsPanel.setStyleName("ToolBarIndicators"); //$NON-NLS-1$
      add(indicatorsPanel);
      
      SimplePanel indicatorsLeft = new SimplePanel();
      indicatorsLeft.setStyleName("indicators_left"); //$NON-NLS-1$
      indicatorsPanel.add(indicatorsLeft);
      
      HorizontalPanel indicatorsRight = new HorizontalPanel();
      indicatorsRight.setStyleName("indicators_right"); //$NON-NLS-1$
      indicatorsLeft.add(indicatorsRight);
     
      statusLabel = new Label(getLocalizedMessages().toolbarStatus());
      statusLabel.setStyleName("indicators_label"); //$NON-NLS-1$

      serverIcon.setStyleName( "biServerDeadIcon" ); //$NON-NLS-1$
      serverIcon.setTitle( MSGS.biServerDead() );
      
      HorizontalPanel indicators = new HorizontalPanel();
      indicators.setStyleName("indicators"); //$NON-NLS-1$
      
      indicators.add(statusLabel);
      indicators.add(serverIcon);
      indicators.add(statusIcon);
      
      indicatorsRight.add(indicators);
      
      HorizontalPanel buttonsPanel = new HorizontalPanel();
      buttonsPanel.setStyleName("buttons"); //$NON-NLS-1$
      buttonsPanel.add(PacImageBundle.getBundle().refreshIcon().createImage());
      Image helpImage = PacImageBundle.getBundle().helpIcon().createImage();
      helpImage.addClickListener( new ClickListener() {

        public void onClick(Widget sender) {
          Window.open( "http://www.youtube.com/watch?v=9ibX3TejlZE", "Help", null ); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
      });
      buttonsPanel.add( helpImage );
      add(buttonsPanel);
      this.setCellHorizontalAlignment(buttonsPanel, HorizontalPanel.ALIGN_RIGHT);
      this.setCellVerticalAlignment(buttonsPanel, HorizontalPanel.ALIGN_MIDDLE);
      
      statusTimer = new Timer() {
        public void run()
        {
          PacServiceFactory.getPacService().isBiServerAlive(
              new AsyncCallback() {
                public void onSuccess( Object isAlive ) {
                  serverIcon.setStyleName( "biServerAliveIcon" ); //$NON-NLS-1$
                  serverIcon.setTitle( MSGS.biServerAlive() );
                }
                public void onFailure(Throwable caught) {
                  serverIcon.setStyleName( "biServerDeadIcon" ); //$NON-NLS-1$
                  serverIcon.setTitle( MSGS.biServerDead() );
                }
              }
            );
        }
      };
      
      PacServiceFactory.getPacService().getBiServerStatusCheckPeriod(
        new AsyncCallback() {
          public void onSuccess( Object oCheckPeriod ) {
            int checkPeriod = ((Integer)oCheckPeriod ).intValue();
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
  }
  
  private class CommonTasks extends SimplePanel{
    public CommonTasks(){
      super();
      VerticalPanel vertPanel = new VerticalPanel();
      
      SimplePanel headerPanel = new SimplePanel();
      headerPanel.setStyleName("CommonTasksHeader"); //$NON-NLS-1$
      
      Label header = new Label("Common Tasks");
      header.setStyleName("commonTasksHeaderText"); //$NON-NLS-1$
      headerPanel.add(header);
      vertPanel.add(headerPanel);
      
      VerticalPanel list = new VerticalPanel();
      list.add(new Hyperlink("Link 1 text","Link1")); //$NON-NLS-1$ //$NON-NLS-2$
      list.add(new Hyperlink("Link 2 text","Link2")); //$NON-NLS-1$ //$NON-NLS-2$
      list.add(new Hyperlink("Link 3 text","Link3")); //$NON-NLS-1$ //$NON-NLS-2$
      list.setStyleName("CommonTasksLinks"); //$NON-NLS-1$
      vertPanel.add(list);
      
      setStyleName("CommonTasks"); //$NON-NLS-1$
      this.add(vertPanel);
    }
  }
  
}
 