package org.pentaho.pac.client.scheduler.view;

import java.util.Date;

import org.pentaho.pac.client.common.ui.widget.ErrorLabel;
import org.pentaho.pac.client.common.util.TimeUtil;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CronEditor extends VerticalPanel {
  
  private TextBox cronTb = new TextBox();
  private DateRangeEditor dateRangeEditor = null;
  private ErrorLabel cronLabel = null;
  
  public CronEditor() {
    Label l = new Label( "Cron String:" );
    cronLabel = new ErrorLabel( l );
    add( cronLabel );
    add( cronTb );
    
    dateRangeEditor = new DateRangeEditor( new Date() );
    add( dateRangeEditor );
  }
  
  public void reset( Date d ) {
    cronTb.setText( "" );
    dateRangeEditor.reset( d );
  }

  public String getCronString() {
    return cronTb.getText();
  }

  public void setCronString( String cronStr) {
    this.cronTb.setText( cronStr );
  }
  
  public Date getStartDate() {
    return dateRangeEditor.getStartDate();
  }
  
  public void setStartDate( Date d ) {
    dateRangeEditor.setStartDate( d );
  }
  
  public Date getEndDate() {
    return dateRangeEditor.getEndDate();
  }
  
  public void setEndDate( Date d ) {
    dateRangeEditor.setEndDate( d );
  }
  
  public void setNoEndDate() {
    dateRangeEditor.setNoEndDate();
  }
  
  public boolean isEndBy() {
    return dateRangeEditor.isEndBy();
  }
  
  public void setEndBy() {
    dateRangeEditor.setEndBy();
  }
  
  public boolean isNoEndDate() {
    return dateRangeEditor.isNoEndDate();
  }
  
  public String getStartTime() {
    return TimeUtil.get0thTime();
  }
  
  /**
   * NOTE: should only ever be used by validators. This is a backdoor
   * into this class that shouldn't be here, do not use this method
   * unless you are validating.
   * 
   * @return DateRangeEditor
   */
  public DateRangeEditor getDateRangeEditor() {
    return dateRangeEditor;
  }
  
  public void setCronError( String errorMsg ) {
    cronLabel.setErrorMsg( errorMsg );
  }
}
