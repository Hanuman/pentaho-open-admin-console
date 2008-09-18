package org.pentaho.pac.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.pentaho.pac.client.PentahoAdminConsole.AdminConsolePageId;
import org.pentaho.pac.client.common.SelectListener;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class AdminConsoleMasterDetailsPanel extends DockPanel  implements ClickListener{
  
  protected class PageInfo {
    boolean hide = false;
    ToggleButton activationButton;
    Widget  page;
    
    protected PageInfo(ToggleButton activationButton, Widget page) {
      this.activationButton = activationButton;
      this.page = page;
    }
  }

  protected class RightPanel extends DockPanel{
    DeckPanel deckPanel = new DeckPanel();
    
    public RightPanel(){
      deckPanel.setStyleName("deckPanel"); //$NON-NLS-1$
      deckPanel.setWidth("100%"); //$NON-NLS-1$
      deckPanel.setHeight("100%"); //$NON-NLS-1$
      
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
      
      grid.setWidget(1, 0, deckPanel);
      add(grid, DockPanel.CENTER);
    }
    
    public void selectPage(int pageId) {
      PageInfo pageInfo = pageMap.get(pageId);
      if ((pageInfo != null) && !pageInfo.hide) {
        for (Integer tmpPageId : pageMap.keySet()) {
          pageMap.get(tmpPageId).activationButton.setDown(tmpPageId.intValue() == pageId);
        }
        int pageIndex = deckPanel.getWidgetIndex(pageInfo.page);
        if (pageIndex != deckPanel.getVisibleWidget()) {
          deckPanel.showWidget(deckPanel.getWidgetIndex(pageInfo.page));
          if (pageInfo.page instanceof SelectListener) {
            ((SelectListener)pageInfo.page).onSelect(pageInfo.activationButton);
          }
        }
      }
    }
    
    public void addPage(Widget page) {
      deckPanel.add(page);
    }
  }
  
  protected AdminConsoleMasterPanel leftPanel = new AdminConsoleMasterPanel();
  protected RightPanel rightPanel = new RightPanel();
  protected LinkedHashMap<Integer, PageInfo> pageMap = new LinkedHashMap<Integer, PageInfo>();
  
  public AdminConsoleMasterDetailsPanel() {
    add(leftPanel, DockPanel.WEST);
    leftPanel.setHeight("100%"); //$NON-NLS-1$
    
    add(rightPanel, DockPanel.CENTER);
    rightPanel.setWidth("100%"); //$NON-NLS-1$
    rightPanel.setHeight("100%"); //$NON-NLS-1$
    setCellHeight(rightPanel, "100%"); //$NON-NLS-1$
    setCellWidth(rightPanel, "100%"); //$NON-NLS-1$
  }
  
  public ToggleButton getPageActivationButton(int pageId) {
    PageInfo pageInfo = pageMap.get(pageId);
    return pageInfo != null ? pageInfo.activationButton : null;
  }
  
  public Widget getPage(int pageId) {
    PageInfo pageInfo = pageMap.get(pageId);
    return pageInfo != null ? pageInfo.page : null;
  }
  
  public Collection<Widget> getPages() {
    ArrayList<Widget> pages = new ArrayList<Widget>();
    for (PageInfo pageInfo : pageMap.values()) {
      pages.add(pageInfo.page);
    }
    return pages;
  }
  
  public Collection<Integer> getPageIds() {
    return pageMap.keySet();
  }
  
  public void addPage(int pageId, String toggleButtonLabel, final Widget page) {

    ToggleButton toggleButton = new ToggleButton(toggleButtonLabel);
    toggleButton.setStylePrimaryName("leftToggleButtons"); //$NON-NLS-1$
    toggleButton.addClickListener(this);
    
    pageMap.put(pageId, new PageInfo(toggleButton, page));
    
    leftPanel.addButton(toggleButton);
    
    page.setWidth("100%"); //$NON-NLS-1$
    page.setHeight("610px"); //$NON-NLS-1$
    rightPanel.addPage(page);
  }
  
  public Widget removePage(int pageId) {
    Widget removedPage = null;
    PageInfo pageInfo = pageMap.get(pageId);
    if (pageInfo != null) {
      pageInfo.activationButton.removeClickListener(this);
      pageInfo.activationButton.removeFromParent();
      pageInfo.page.removeFromParent();
      pageMap.remove(pageId);
      removedPage = pageInfo.page;
    }
    return removedPage;
  }
  
  public void onClick(Widget sender) {
    for (Map.Entry<Integer, PageInfo> entry : pageMap.entrySet()) {
      if (entry.getValue().activationButton == sender) {
        if (!entry.getValue().activationButton.isDown()) {
          entry.getValue().activationButton.setDown(true);
        } else {
          selectPage(entry.getKey().intValue());
        }
      }
    }
  }
    
  public void selectPage(int pageId) {
    PageInfo pageInfo = pageMap.get(pageId);
    if ((pageInfo != null) && !pageInfo.hide) {
      pageInfo.activationButton.setDown(true);
      rightPanel.selectPage(pageId);
    }
  }

  public int indexOf(int pageId) {
    int index = -1;
    int tmpIndex = 0;
    for (Integer key : pageMap.keySet()) {
      if (key.intValue() == pageId) {
        index = tmpIndex;
        break;
      }
      tmpIndex++;
    }
    return index;
  }
  
  public boolean isPageHidden(int pageId) {
    PageInfo pageInfo = pageMap.get(pageId);
    return pageInfo == null || pageInfo.hide;
  }
    
  public void hidePage(int pageId, boolean hide) {
    PageInfo pageInfo = pageMap.get(pageId);
    if ((pageInfo != null) && (pageInfo.hide != hide)) {
      int pageIndex = indexOf(pageId);
      ArrayList<Map.Entry<Integer, PageInfo>> list = new ArrayList<Map.Entry<Integer, PageInfo>>(pageMap.entrySet());
      if (hide) {
        if (pageInfo.activationButton.isDown()) {
          pageInfo.activationButton.setDown(false);
          boolean newSelectionMade = false;
          for (int index2 = pageIndex + 1; index2 < list.size(); index2++) {
            if (!list.get(index2).getValue().hide) {
              selectPage(list.get(index2).getKey().intValue());
              newSelectionMade = true;
              break;
            }
          }
          
          if (!newSelectionMade) {
            for (int index2 = pageIndex - 1; index2 >= 0; index2--) {
              if (!list.get(index2).getValue().hide) {
                selectPage(list.get(index2).getKey().intValue());
              }
            }
          }
        }
        
        pageInfo.activationButton.removeFromParent();
        pageInfo.page.removeFromParent();
        pageInfo.hide = true;
      } else {
        for (int index2 = pageIndex + 1; index2 < list.size(); index2++) {
          if (!list.get(index2).getValue().hide) {
            pageInfo.activationButton.removeFromParent();
          }
        }
        
        leftPanel.addButton(pageInfo.activationButton);
        rightPanel.addPage(pageInfo.page);
        pageInfo.hide = false;
        
        for (int index2 = pageIndex + 1; index2 < list.size(); index2++) {
          if (!list.get(index2).getValue().hide) {
            leftPanel.addButton(list.get(index2).getValue().activationButton);
          }
        }
      }
    }
  }
  
  public AdminConsoleMasterPanel getMasterPanel() {
    return leftPanel;
  }
  
  public RightPanel getDetailPanel() {
    return rightPanel;
  }

  public void addQuickLink(Hyperlink hyperlink) {
    leftPanel.addQuickLink(hyperlink);
  }

  public void showQuickLinks(boolean show) {
    leftPanel.showQuickLinks(show);
  }
}
