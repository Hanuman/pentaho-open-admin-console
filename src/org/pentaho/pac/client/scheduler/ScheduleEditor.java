package org.pentaho.pac.client.scheduler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ScheduleEditor extends FlexTable {

  private TextBox nameTb = new TextBox();
  private TextBox groupNameTb = new TextBox();
  private TextBox descriptionTb = new TextBox();
  private TextBox crontStringTb = new TextBox();
  
  private RecurranceDialog recurranceDialog = new RecurranceDialog();
  public ScheduleEditor() {
    super();
    
    
    

    setCellPadding( 0 );
    setCellSpacing( 0 );
    
    Label l = new Label( "Name:" );
    setWidget( 0 , 0, l );
    setWidget( 0 , 1, nameTb );
    
    l = new Label( "Group:" );
    setWidget( 1 , 0, l );
    setWidget( 1 , 1, groupNameTb );
    
    l = new Label( "Description:" );
    setWidget( 2 , 0, l );
    setWidget( 2 , 1, descriptionTb );
      

    
    l = new Label( "Cron String:" );
    setWidget( 3 , 0, l );
    //crontStringTb.setReadOnly( true );
    setWidget( 3 , 1, crontStringTb );
    Button b = new Button( "Create...", new ClickListener() {
      public void onClick(Widget sender) {
        recurranceDialog.center();
      }
    });
    setWidget( 3 , 2, b );
  }
  
  public String getName() {
    return nameTb.getText();
  }
  
  public String getGroupName() {
    return groupNameTb.getText();
  }
  
  public String getDescription() {
    return descriptionTb.getText();
  }
  
  public String getCronString() {
    return crontStringTb.getText();
  }
}
