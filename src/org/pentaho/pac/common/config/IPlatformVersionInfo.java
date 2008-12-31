package org.pentaho.pac.common.config;

public interface IPlatformVersionInfo {

  public String getBiSuiteVersion();
  public void setBiSuiteVersion(String biSuiteVersion);
  public String getSubscriptionVersion();
  public void setSubscriptionVersion(String subscriptionVersion);
  public String getAnalysisVersion();
  public void setAnalysisVersion(String analysisVersion);
  public String getDataIntegrationVersion();
  public void setDataIntegrationVersion(String dataIntegrationVersion);
  public String getReportingVersion();
  public void setReportingVersion(String reportingVersion);
  public String getMetaDataVersion();
  public void setMetaDataVersion(String metaDataVersion);
}
