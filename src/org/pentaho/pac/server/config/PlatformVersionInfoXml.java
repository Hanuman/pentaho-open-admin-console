/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 - 2009 Pentaho Corporation.  All rights reserved.
*/
package org.pentaho.pac.server.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.pentaho.pac.common.config.IPlatformVersionInfo;

public class PlatformVersionInfoXml extends AbstractDiagnosticsJmxXml implements IPlatformVersionInfo {

  public static final String PENTAHO_BI_SUITE_VERSION = "PentahoBISuiteVersion"; //$NON-NLS-1$
  public static final String PENTAHO_PRO_SUBSCRIPTION_VERSION = "PentahoProSubscriptionVersion"; //$NON-NLS-1$
  public static final String PENTAHO_ANALYSIS_VERSION = "PentahoAnalysisVersion"; //$NON-NLS-1$
  public static final String PENTAHO_DATA_INTEGRATION_VERSION = "PentahoDataIntegrationVersion"; //$NON-NLS-1$
  public static final String PENTAHO_REPORTING_ENGINE_VERSION = "PentahoReportingEngineVersion"; //$NON-NLS-1$
  public static final String PENTAHO_METADATA_VERSION = "PentahoMetadataVersion"; //$NON-NLS-1$
  
  public PlatformVersionInfoXml(String jmxXml) throws DocumentException {
    super(jmxXml);
  }
  
  public PlatformVersionInfoXml(Document jmxDocument) throws DocumentException {
    super(jmxDocument);
  }
  
  public PlatformVersionInfoXml() {
    super();
  }
  
  public String getAnalysisVersion() {
    return getAttributeValue(PENTAHO_ANALYSIS_VERSION);
  }

  public String getBiSuiteVersion() {
    return getAttributeValue(PENTAHO_BI_SUITE_VERSION);
  }

  public String getDataIntegrationVersion() {
    return getAttributeValue(PENTAHO_DATA_INTEGRATION_VERSION);
  }

  public String getMetaDataVersion() {
    return getAttributeValue(PENTAHO_METADATA_VERSION);
  }

  public String getReportingVersion() {
    return getAttributeValue(PENTAHO_REPORTING_ENGINE_VERSION);
  }

  public String getSubscriptionVersion() {
    return getAttributeValue(PENTAHO_PRO_SUBSCRIPTION_VERSION);
  }

  public void setAnalysisVersion(String analysisVersion) {
    setAttributeValue(PENTAHO_ANALYSIS_VERSION, analysisVersion);
  }

  public void setBiSuiteVersion(String biSuiteVersion) {
    setAttributeValue(PENTAHO_BI_SUITE_VERSION, biSuiteVersion);
  }

  public void setDataIntegrationVersion(String dataIntegrationVersion) {
    setAttributeValue(PENTAHO_DATA_INTEGRATION_VERSION, dataIntegrationVersion);
  }

  public void setMetaDataVersion(String metaDataVersion) {
    setAttributeValue(PENTAHO_METADATA_VERSION, metaDataVersion);
  }

  public void setReportingVersion(String reportingVersion) {
    setAttributeValue(PENTAHO_REPORTING_ENGINE_VERSION, reportingVersion);
  }

  public void setSubscriptionVersion(String subscriptionVersion) {
    setAttributeValue(PENTAHO_PRO_SUBSCRIPTION_VERSION, subscriptionVersion);
  }

}
