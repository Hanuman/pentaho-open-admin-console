package org.pentaho.pac.common.config;

import java.io.Serializable;


public class PlatformVersionInfo implements IPlatformVersionInfo, Serializable {

  private static final long serialVersionUID = 1L;
  private String biSuiteVersion;
  private String subscriptionVersion;
  private String analysisVersion;
  private String dataIntegrationVersion;
  private String reportingVersion;
  private String metaDataVersion;
  

  public PlatformVersionInfo() {
  }

  public PlatformVersionInfo(IPlatformVersionInfo platformVersionInfo) {
    setBiSuiteVersion(platformVersionInfo.getBiSuiteVersion());
    setSubscriptionVersion(platformVersionInfo.getSubscriptionVersion());
    setAnalysisVersion(platformVersionInfo.getAnalysisVersion());
    setDataIntegrationVersion(platformVersionInfo.getDataIntegrationVersion());
    setReportingVersion(platformVersionInfo.getReportingVersion());
    setMetaDataVersion(platformVersionInfo.getMetaDataVersion());
  }
  
  public String getBiSuiteVersion() {
    return this.biSuiteVersion;
  }

  public void setBiSuiteVersion(String biSuiteVersion) {
    this.biSuiteVersion = biSuiteVersion;
  }

  public String getSubscriptionVersion() {
    return this.subscriptionVersion;
  }

  public void setSubscriptionVersion(String subscriptionVersion) {
    this.subscriptionVersion = subscriptionVersion;
  }

  public String getAnalysisVersion() {
    return this.analysisVersion;
  }

  public void setAnalysisVersion(String analysisVersion) {
    this.analysisVersion = analysisVersion;
  }

  public String getDataIntegrationVersion() {
    return this.dataIntegrationVersion;
  }

  public void setDataIntegrationVersion(String dataIntegrationVersion) {
    this.dataIntegrationVersion = dataIntegrationVersion;
  }
  public String getReportingVersion() {
    return this.reportingVersion;
  }

  public void setReportingVersion(String reportingVersion) {
    this.reportingVersion = reportingVersion;
  } 
  
  public String getMetaDataVersion() {
    return this.metaDataVersion;
  }

  public void setMetaDataVersion(String metaDataVersion) {
    this.metaDataVersion = metaDataVersion;
  } 
 }
