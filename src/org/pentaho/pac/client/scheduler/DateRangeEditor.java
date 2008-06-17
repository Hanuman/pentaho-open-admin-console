package org.pentaho.pac.client.scheduler;

import java.util.Date;

import org.pentaho.pac.client.common.ui.DatePickerEx;
import org.pentaho.pac.client.common.ui.SimpleGroupBox;
import org.pentaho.pac.client.common.ui.widget.ErrorLabel;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DateRangeEditor extends SimpleGroupBox {

  private static final String END_DATE_RB_GROUP = "end-date-group"; //$NON-NLS-1$

  private DatePickerEx startDatePicker = null;
  private EndDatePanel endDatePanel = null;
  
  private ErrorLabel startLabel = null;

  public DateRangeEditor( Date date ) {

    super( "Range of recurrence" );

    HorizontalPanel hp = new HorizontalPanel();
    add(hp);

    startLabel = new ErrorLabel( new Label( "Start:" ) );
    startLabel.setStyleName("startLabel");
    hp.add(startLabel);
    startDatePicker = new DatePickerEx();
    hp.add(startDatePicker);

    VerticalPanel vp = new VerticalPanel();
    hp.add(vp);

    // add end time radio buttons to vp
    HorizontalPanel endByHP = new HorizontalPanel();
    vp.add(endByHP);

    endDatePanel = new EndDatePanel( date );
    vp.add(endDatePanel);
    reset( date );
  }
  
  public Date getStartDate() {
    return startDatePicker.getSelectedDate();
  }
  
  public void setStartDate( Date d ) {
    startDatePicker.setSelectedDate( d );
  }
  
  public Date getEndDate() {
    return endDatePanel.getDate();
  }
  
  public void setEndDate( Date d ) {
    endDatePanel.setDate( d );
  }
  
  public void reset( Date d ) {
    startDatePicker.setSelectedDate( d );
    startDatePicker.setYoungestDate( d );
    endDatePanel.reset( d );
  }
  
  public void setNoEndDate() {
    endDatePanel.setNoEndDate();
  }
  
  public boolean isEndBy() {
    return endDatePanel.isEndBy();
  }
  
  public void setEndBy() {
    endDatePanel.setEndBy();
  }
  
  public boolean isNoEndDate() {
    return endDatePanel.isNoEndDate();
  }
  
  public void setStartByError( String errorMsg ) {
    startLabel.setErrorMsg( errorMsg );
  }
  
  public void setEndByError( String errorMsg ) {
    endDatePanel.setEndByError( errorMsg );
  }

  private class EndDatePanel extends VerticalPanel {

    private DatePickerEx endDatePicker = null;
    private RadioButton noEndDateRb = null;
    private RadioButton endByRb = null;
    private ErrorLabel endByLabel = null;
    
    public EndDatePanel( Date date ) {
      final EndDatePanel localThis = this;
  
      noEndDateRb = new RadioButton(END_DATE_RB_GROUP, "No end date");
      noEndDateRb.setStyleName("recurrenceRadioButton");
      noEndDateRb.setChecked(true);
      add(noEndDateRb);
      HorizontalPanel hp = new HorizontalPanel();
      add(hp);
  
      endByRb = new RadioButton(END_DATE_RB_GROUP, "End by:");
      endByRb.setStyleName("recurrenceRadioButton");
      endByLabel = new ErrorLabel( endByRb );
      hp.add(endByLabel);
      endDatePicker = new DatePickerEx();
      endDatePicker.setEnabled(false);
      hp.add(endDatePicker);
  
      noEndDateRb.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          localThis.endDatePicker.setEnabled(false);
        }
      });
  
      endByRb.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          localThis.endDatePicker.setEnabled(true);
        }
      });
      reset( date );
    }
    
    public void reset( Date d ) {
      setNoEndDate();
      endDatePicker.setSelectedDate( d );
      endDatePicker.setYoungestDate( d );
    }
    
    public DatePickerEx getEndDatePicker() {
      return endDatePicker;
    }
    
    public void setNoEndDate() {
      noEndDateRb.setChecked( true );
      endByRb.setChecked( false );
      endDatePicker.setEnabled( false );
    }
    
    public boolean isEndBy() {
      return endByRb.isChecked();
    }
    
    public void setEndBy() {
      endByRb.setChecked( true );
      noEndDateRb.setChecked( false );
      endDatePicker.setEnabled( true );
    }
    
    public boolean isNoEndDate() {
      return noEndDateRb.isChecked();
    }
    
    public Date getDate() {
      return isEndBy()
        ? endDatePicker.getSelectedDate()
        : null;
    }
    
    public void setDate( Date d ) {
      endDatePicker.setSelectedDate( d );
    }
    
    public void setEndByError( String errorMsg ) {
      endByLabel.setErrorMsg( errorMsg );
    }
  }
}
