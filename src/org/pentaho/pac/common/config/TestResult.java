package org.pentaho.pac.common.config;

import java.io.Serializable;

public class TestResult implements Serializable{
  String testName;
  String resultMessage;
  
  public String getTestName() {
    return testName;
  }
  public void setTestName(String testName) {
    this.testName = testName;
  }
  public String getResultMessage() {
    return resultMessage;
  }
  public void setResultMessage(String resultMessage) {
    this.resultMessage = resultMessage;
  }
  
  
}
