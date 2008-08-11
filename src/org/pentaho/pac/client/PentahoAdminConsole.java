package org.pentaho.pac.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pentaho.pac.client.common.SelectListener;
import org.pentaho.pac.client.common.ui.GroupBox;
import org.pentaho.pac.client.common.ui.ICallback;
import org.pentaho.pac.client.common.ui.dialog.ConfirmDialog;
import org.pentaho.pac.client.home.HomePanel;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.client.utils.PacImageBundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PentahoAdminConsole extends DockPanel implements ClickListener {
  
  protected class PageInfo {
    ToggleButton activationButton;
    Widget  page;
    
    protected PageInfo(ToggleButton activationButton, Widget page) {
      this.activationButton = activationButton;
      this.page = page;
    }
  }
  public int shutdownWindowIndex=0;   
  public static final PacLocalizedMessages MSGS = (PacLocalizedMessages)GWT.create(PacLocalizedMessages.class);
  ConfirmDialog confirmStopServerDialog = new ConfirmDialog(MSGS.stoppingServerConfirmation(), MSGS.confirmStopServerMsg());
  protected Map<Integer, PageInfo> pageMap = new HashMap<Integer, PageInfo>();
  
  protected VerticalPanel leftVerticalPanel = new VerticalPanel();
  protected ConsoleToolbar toolbar = new ConsoleToolbar();
  protected HorizontalPanel topPanel = new HorizontalPanel();
  
  protected DeckPanel deckPanel = new DeckPanel();
  protected AdministrationTabPanel adminTabPanel = new AdministrationTabPanel();
  protected HorizontalPanel horizontalPanel;
  protected Widget body; 
  
  protected HomePanel homePanel;
  
  public enum AdminConsolePageId {
    HOME_PAGE, ADMIN_PAGE,PDI_PAGE
  };
  
  public PentahoAdminConsole() {
    horizontalPanel = buildTopPanel() ;
    horizontalPanel.setWidth("100%");    //$NON-NLS-1$
    body = buildBody();
    body.setWidth("100%");//$NON-NLS-1$
    body.setHeight("100%");   //$NON-NLS-1$
    add(horizontalPanel, DockPanel.NORTH);
    setCellWidth(horizontalPanel, "100%"); //$NON-NLS-1$
    add(body, DockPanel.CENTER);
    setStyleName("main-panel"); //$NON-NLS-1$
    setCellWidth(body, "100%"); //$NON-NLS-1$
    setCellHeight(body, "100%"); //$NON-NLS-1$    
    
    showPage(AdminConsolePageId.HOME_PAGE);
  }
  
  public Widget buildBody() {
    DockPanel centerPanel = new DockPanel();
    SimplePanel commonTasks = createCommonTasks();
    VerticalPanel leftPanel = new VerticalPanel();
    SimplePanel tempPanel = new SimplePanel();
    tempPanel.setStyleName("leftTabPanel_top"); //$NON-NLS-1$
    leftPanel.add(tempPanel);
    Label spacer = new Label();
    leftVerticalPanel.add(spacer);
    leftVerticalPanel.setCellHeight(spacer, "20px"); //$NON-NLS-1$
    
    initializeAdminDeck();
    
    adminTabPanel.setWidth("100%"); //$NON-NLS-1$
    adminTabPanel.setHeight("100%"); //$NON-NLS-1$
    adminTabPanel.getDeckPanel().setHeight("100%"); //$NON-NLS-1$
    
    if (commonTasks != null) {
      spacer = new Label();
      leftVerticalPanel.add(spacer);
      leftVerticalPanel.setCellHeight(spacer, "50"); //$NON-NLS-1$
      leftVerticalPanel.add(commonTasks);
    }
    
    spacer = new Label();
    leftVerticalPanel.add(spacer);
    leftVerticalPanel.setCellHeight(spacer, "100%"); //$NON-NLS-1$
    leftVerticalPanel.setStyleName("leftTabPanel"); //$NON-NLS-1$
    
    deckPanel.setStyleName("deckPanel"); //$NON-NLS-1$
    deckPanel.setWidth("100%"); //$NON-NLS-1$
    deckPanel.setHeight("100%"); //$NON-NLS-1$

    DockPanel deckPanelWrapper = new DeckPanelWrapper(deckPanel);
    deckPanelWrapper.setWidth("100%"); //$NON-NLS-1$
    deckPanelWrapper.setHeight("100%"); //$NON-NLS-1$
    
    DockPanel leftPanelWrapper = new LeftPanelWrapper(leftVerticalPanel);
    leftPanelWrapper.setHeight("100%"); //$NON-NLS-1$
    
    centerPanel.add(leftPanelWrapper, DockPanel.WEST);
    centerPanel.add(deckPanelWrapper, DockPanel.CENTER);
    centerPanel.setHeight("100%"); //$NON-NLS-1$
    
    centerPanel.setCellHeight(deckPanelWrapper, "100%"); //$NON-NLS-1$
    centerPanel.setCellWidth(deckPanelWrapper, "100%"); //$NON-NLS-1$
    
    confirmStopServerDialog.setOnOkHandler(new ICallback() {
      public void onHandle(Object o) {
        confirmStopServerDialog.hide();
        removeFromParent();
        VerticalPanel shutdownPanel = getShutDownPanel();
        shutdownPanel.setWidth("100%");
        shutdownPanel.setHeight("100%");
        add(shutdownPanel);
        deckPanel.showWidget(shutdownWindowIndex);
        DOM.getElementById("downloadIframe").setAttribute("src", "halt");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
    });
    return centerPanel;
  }
  
  public ConsoleToolbar getConsoleToolbar() {
    return this.toolbar;
  }

  protected void addPageToDeck(AdminConsolePageId pageId, String activationButtonLabel, Widget page) {
    addPageToDeck(pageId.ordinal(), activationButtonLabel, page);
  }
  
  protected ToggleButton getPageActivationButton(AdminConsolePageId pageId) {
    PageInfo pageInfo = pageMap.get(pageId.ordinal());
    return pageInfo != null ? pageInfo.activationButton : null;
  }
  
  protected Widget getPage(AdminConsolePageId pageId) {
    PageInfo pageInfo = pageMap.get(pageId.ordinal());
    return pageInfo != null ? pageInfo.page : null;
  }
  
  protected Collection<Widget> getPages() {
    ArrayList<Widget> pages = new ArrayList<Widget>();
    for (PageInfo pageInfo : pageMap.values()) {
      pages.add(pageInfo.page);
    }
    return pages;
  }
  
  protected void addPageToDeck(int pageId, String toggleButtonLabel, final Widget widget) {

    ToggleButton toggleButton = new ToggleButton(toggleButtonLabel);
    toggleButton.setStylePrimaryName("leftToggleButtons"); //$NON-NLS-1$
    toggleButton.addClickListener(this);
    
    pageMap.put(pageId, new PageInfo(toggleButton, widget));
    leftVerticalPanel.add(toggleButton);
    widget.setWidth("100%"); //$NON-NLS-1$
    widget.setHeight("100%"); //$NON-NLS-1$
    deckPanel.add(widget);
    
    if (widget instanceof SelectListener)
    	toggleButton.addClickListener(new ClickListener()
    	{
			public void onClick(Widget sender) {
				((SelectListener)widget).onSelect(sender);
			}
    		
    	});
  }
  
  public HorizontalPanel buildTopPanel() {
    HorizontalPanel topPanel = new HorizontalPanel();
    SimplePanel logo = new SimplePanel();
    logo.setStyleName("logo"); //$NON-NLS-1$
    topPanel.setWidth("100%");  //$NON-NLS-1$
    topPanel.add(logo);
    topPanel.add(toolbar);
    topPanel.setCellWidth(toolbar, "100%"); //$NON-NLS-1$
    return topPanel;
  }
  
  protected void initTopPanel() {
    SimplePanel logo = new SimplePanel();
    logo.setStyleName("logo"); //$NON-NLS-1$
    topPanel.setWidth("100%");  //$NON-NLS-1$
    topPanel.add(logo);
    topPanel.add(toolbar);
    topPanel.setCellWidth(toolbar, "100%"); //$NON-NLS-1$
  }
  
  public void onClick(Widget sender) {
    for (Map.Entry<Integer, PageInfo> entry : pageMap.entrySet()) {
      if (entry.getValue().activationButton == sender) {
        if (!entry.getValue().activationButton.isDown()) {
          entry.getValue().activationButton.setDown(true);
        } else {
          showPage(entry.getKey().intValue());
        }
      }
    }
  }
    
  protected void initializeAdminDeck() {
    homePanel = new HomePanel("http://www.pentaho.com/console_home"); //$NON-NLS-1$
    addPageToDeck(AdminConsolePageId.HOME_PAGE, PentahoAdminConsole.MSGS.home(), homePanel);
    addPageToDeck(AdminConsolePageId.ADMIN_PAGE, PentahoAdminConsole.MSGS.administration(), adminTabPanel);
    showPage(AdminConsolePageId.HOME_PAGE);
  }
  
  protected void showPage(int pageId) {
    PageInfo pageInfo = pageMap.get(pageId);
    if (pageInfo != null) {
      for (Integer tmpPageId : pageMap.keySet()) {
        pageMap.get(tmpPageId).activationButton.setDown(tmpPageId.intValue() == pageId);
      }
      deckPanel.showWidget(deckPanel.getWidgetIndex(pageInfo.page));
    }
  }
  
  public void showPage(AdminConsolePageId pageId)
  {
    showPage(pageId.ordinal());
  }
  
  //TOP Toolbar
  public class ConsoleToolbar extends HorizontalPanel{
    
    private Label statusLabel;
    private Timer statusTimer = null;
    private SimplePanel serverIcon = new SimplePanel();
    private Image statusIcon = PacImageBundle.getBundle().statusWorkingIcon().createImage();
    HorizontalPanel buttonsPanel = new HorizontalPanel();
    
    public void addImageButton(Image image) {
      buttonsPanel.setStyleName("buttons"); //$NON-NLS-1$
      buttonsPanel.add(image);
    }
    public ConsoleToolbar(){
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
      Image createImage = PacImageBundle.getBundle().refreshIcon().createImage();
      Image stopConsoleImage = PacImageBundle.getBundle().stopIcon().createImage();
      stopConsoleImage.addClickListener( new ClickListener() {

        public void onClick(Widget sender) {
          confirmStopServerDialog.center();
        }});
      
      helpImage.addClickListener( new ClickListener() {

        public void onClick(Widget sender) {
          Window.open( "http://www.youtube.com/watch?v=9ibX3TejlZE", "Help", null ); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
      });

      addImageButton(createImage);
      addImageButton(helpImage);
      addImageButton(stopConsoleImage);
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
  
  
  protected SimplePanel createCommonTasks() {
    return null;
  }
  
  private class DeckPanelWrapper extends DockPanel{
    public DeckPanelWrapper(DeckPanel deck){
      Grid grid = new Grid(3,2);
      grid.setWidth("100%"); //$NON-NLS-1$
      grid.setHeight("100%"); //$NON-NLS-1$
      grid.setCellPadding(0);
      grid.setCellSpacing(0);
      
      grid.getRowFormatter().setStyleName(0,"deckPanel-top-tr"); //$NON-NLS-1$
      grid.getCellFormatter().setStyleName(0, 0, "deckPanel-n"); //$NON-NLS-1$
      grid.getCellFormatter().setStyleName(0, 1, "deckPanel-ne"); //$NON-NLS-1$
      grid.getCellFormatter().setStyleName(1, 0, "deckPanel-c"); //$NON-NLS-1$
      grid.getCellFormatter().setStyleName(1, 1, "deckPanel-ce"); //$NON-NLS-1$
      grid.getCellFormatter().setStyleName(2, 0, "deckPanel-s"); //$NON-NLS-1$
      grid.getCellFormatter().setStyleName(2, 1, "deckPanel-se"); //$NON-NLS-1$
      
      grid.setWidget(1, 0, deck);
      add(grid, DockPanel.CENTER);
    }
  }
  
  private class LeftPanelWrapper extends DockPanel{
    public LeftPanelWrapper(VerticalPanel vertPanel){
      Grid grid = new Grid(3,1);
      grid.setWidth("100%"); //$NON-NLS-1$
      grid.setHeight("100%"); //$NON-NLS-1$
      grid.setCellPadding(0);
      grid.setCellSpacing(0);
      
      grid.getCellFormatter().setStyleName(0, 0, "deckPanel-nw"); //$NON-NLS-1$
      grid.getCellFormatter().setStyleName(1, 0, "deckPanel-cw"); //$NON-NLS-1$
      grid.getCellFormatter().setStyleName(2, 0, "deckPanel-sw"); //$NON-NLS-1$
      
      grid.setWidget(1, 0, vertPanel);
      add(grid, DockPanel.CENTER);
    }
  }
  
  private VerticalPanel getShutDownPanel() {
   
    
    GroupBox box = new GroupBox(MSGS.consoleShutdown());
    
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.add(new Label(MSGS.consoleShutdownComplete()));
    verticalPanel.setWidth("100%");
    verticalPanel.setHeight("100%");
    
    box.setContent(verticalPanel);
    box.setHeight("100px");
    box.setWidth("400px");
    
    VerticalPanel vp = new VerticalPanel();
    vp.add(box);
    vp.setCellHorizontalAlignment(box, VerticalPanel.ALIGN_CENTER);
    vp.setCellVerticalAlignment(box, VerticalPanel.ALIGN_MIDDLE);
    
    return vp;
  }
  
  static public PacLocalizedMessages getLocalizedMessages() {
    return MSGS;
  }
}
 