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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.pac.common.config.IHibernateConfig;
import org.pentaho.pac.server.common.util.DtdEntityResolver;
import org.pentaho.platform.api.util.XmlParseException;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

public class HibernateConfigXml implements Serializable, IHibernateConfig {
  
  private static final String ROOT_ELEMENT = "hibernate-configuration";  //$NON-NLS-1$
  private static final String PASSWORD_XPATH = ROOT_ELEMENT +"/session-factory/property[@name='connection.password']";  //$NON-NLS-1$
  private static final String DB_DRIVER_XPATH = ROOT_ELEMENT +"/session-factory/property[@name='connection.driver_class']";  //$NON-NLS-1$
  private static final String DB_URL_XPATH = ROOT_ELEMENT +"/session-factory/property[@name='connection.url']";  //$NON-NLS-1$
  private static final String DIALECT_XPATH = ROOT_ELEMENT +"/session-factory/property[@name='dialect']";  //$NON-NLS-1$
  private static final String CONNECTION_POOL_SIZE_XPATH = ROOT_ELEMENT +"/session-factory/property[@name='connection.pool_size']";  //$NON-NLS-1$
  private static final String USER_ID_XPATH = ROOT_ELEMENT +"/session-factory/property[@name='connection.username']";  //$NON-NLS-1$
  
  Document document;
  
  public HibernateConfigXml(File pentahoXmlFile) throws IOException, DocumentException{
    this(XmlDom4JHelper.getDocFromFile(pentahoXmlFile, new DtdEntityResolver()));    
  }
  
  public HibernateConfigXml(String xml) throws DocumentException, XmlParseException {
    this(XmlDom4JHelper.getDocFromString(xml, new DtdEntityResolver()));    
  }
  
  public HibernateConfigXml(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException(Messages.getErrorString("PentahoXml.ERROR_0001_INVALID_ROOT_ELEMENT")); //$NON-NLS-1$ 
    }
    document = doc;
  }
  
  public HibernateConfigXml() {
  }
  
  public HibernateConfigXml(IHibernateConfig hibernateConfig) {
    this();
    setDbDriver(hibernateConfig.getDbDriver());
    setDbUrl(hibernateConfig.getDbUrl());
    setDialect(hibernateConfig.getDialect());
    setPassword(hibernateConfig.getPassword());
    setUserId(hibernateConfig.getUserId());
    setConnectionPoolSize(hibernateConfig.getConnectionPoolSize());
  }
  
  private void setValue(String xPath, String value) {
    setValue(xPath, value, false);
  }
  
  private void setValue(String xPath, String value, boolean useCData) {
    Element element = (Element) document.selectSingleNode( xPath );
    if (element == null) {
      element = DocumentHelper.makeElement(document, xPath);
    }
    if (useCData) {
      element.clearContent(); 
      element.addCDATA( value );
    } else {
      element.setText( value );
    }
  }

  private String getValue(String xpath) {
    Element element = (Element)document.selectSingleNode(xpath);
    return element != null ? element.getText() : null;
  }
  
  public Document getDocument() {
    return document;
  }
  
  public String getDbDriver() {
    return getValue(DB_DRIVER_XPATH);
  }
  
  public void setDbDriver(String driver) {
    setValue(DB_DRIVER_XPATH, driver);
  }
  
  public String getDbUrl() {
    return getValue(DB_URL_XPATH);
  }
  
  public void setDbUrl( String url) {
    setValue(DB_URL_XPATH, url);
  }
  
  public String getDialect() {
    return getValue(DIALECT_XPATH);
  }
  
  public void setDialect(String dialect) {
    setValue(DIALECT_XPATH, dialect);
  }
  
  public String getUserId() {
    return getValue(USER_ID_XPATH);
  }
  
  public void setUserId(String userId) {
    setValue(USER_ID_XPATH, userId);
  }

  public String getPassword() {
    return getValue(PASSWORD_XPATH);
  }

  public void setPassword(String password) {
    setValue(PASSWORD_XPATH, password);
  }
  
  public Integer getConnectionPoolSize() {
    Integer port = null;
    try {
      port = new Integer(getValue(CONNECTION_POOL_SIZE_XPATH));
    } catch (Exception ex) {
      // Do nothing..
    }
    return port;
  }
  
  public void setConnectionPoolSize(Integer poolSize) {
    setValue(CONNECTION_POOL_SIZE_XPATH, poolSize != null ? poolSize.toString() : "");
  }
}
