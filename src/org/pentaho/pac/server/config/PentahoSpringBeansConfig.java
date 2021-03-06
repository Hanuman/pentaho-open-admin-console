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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.pac.server.common.util.DtdEntityResolver;
import org.pentaho.platform.api.util.XmlParseException;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

public class PentahoSpringBeansConfig {

  public enum AuthenticationType {
    MEMORY_BASED_AUTHENTICATION, LDAP_BASED_AUTHENTICATION, DB_BASED_AUTHENTICATION
  };
  
  private static final String ROOT_ELEMENT = "beans"; //$NON-NLS-1$
  private static final String IMPORT_ELEMENT = "import";//$NON-NLS-1$
  private static final String RESOURCE_XPATH = ROOT_ELEMENT + "/" + IMPORT_ELEMENT;//$NON-NLS-1$
  private static final String RESOURCE_ATTR_NAME = "resource";//$NON-NLS-1$
  
  private static final String SPRING_SECURITY_LDAP_CONFIG_FILE = "applicationContext-spring-security-ldap.xml"; //$NON-NLS-1$
  private static final String SPRING_SECURITY_DB_CONFIG_FILE = "applicationContext-spring-security-hibernate.xml"; //$NON-NLS-1$
  private static final String SPRING_SECURITY_MEMORY_CONFIG_FILE = "applicationContext-spring-security-memory.xml"; //$NON-NLS-1$
  private static final String PENTAHO_SECURITY_LDAP_CONFIG_FILE = "applicationContext-pentaho-security-ldap.xml"; //$NON-NLS-1$
  private static final String PENTAHO_SECURITY_DB_CONFIG_FILE = "applicationContext-pentaho-security-hibernate.xml"; //$NON-NLS-1$
  private static final String PENTAHO_SECURITY_MEMORY_CONFIG_FILE = "applicationContext-pentaho-security-memory.xml"; //$NON-NLS-1$
  
  Document document;
  
  public PentahoSpringBeansConfig(File pentahoXmlFile) throws IOException, DocumentException{
    this(XmlDom4JHelper.getDocFromFile(pentahoXmlFile, new DtdEntityResolver()));    
  }
  
  public PentahoSpringBeansConfig(String xml) throws DocumentException, XmlParseException {
    this(XmlDom4JHelper.getDocFromString(xml, new DtdEntityResolver()));
  }
  
  public PentahoSpringBeansConfig(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException(Messages.getInstance().getErrorString("PentahoSpringBeansConfig.ERROR_0001_INVALID_ROOT_ELEMENT")); //$NON-NLS-1$      
    }
    document = doc;
  }
  
  public PentahoSpringBeansConfig() {
    document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
  }
  
  @SuppressWarnings("unchecked")
  public String[] getSystemConfigFileNames() {
    ArrayList<String> fileNames = new ArrayList<String>();
    List nodes = document.selectNodes(RESOURCE_XPATH);
    for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
      Element element = (Element)iter.next();
      fileNames.add(element.attributeValue(RESOURCE_ATTR_NAME));
    }
    return fileNames.toArray(new String[0]);
  }
  
  @SuppressWarnings("unchecked")
  public void setSystemConfigFileNames(String[] fileNames) {
    List nodes = document.selectNodes(RESOURCE_XPATH);
    for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
      ((Element)iter.next()).detach();
    }
    for (int i = 0; i < fileNames.length; i++) {
      document.getRootElement().addElement(IMPORT_ELEMENT).addAttribute(RESOURCE_ATTR_NAME, fileNames[i]);
    }
  }
  
  public Document getDocument() {
    return document;
  }
  
  public AuthenticationType getAuthenticationType() {
    List<String> configFiles = Arrays.asList(getSystemConfigFileNames());
    AuthenticationType authenticationType = null;
    if(configFiles.contains(SPRING_SECURITY_MEMORY_CONFIG_FILE) && configFiles.contains(PENTAHO_SECURITY_MEMORY_CONFIG_FILE)) { 
      authenticationType = AuthenticationType.MEMORY_BASED_AUTHENTICATION;
    } else if(configFiles.contains(SPRING_SECURITY_DB_CONFIG_FILE) && configFiles.contains(PENTAHO_SECURITY_DB_CONFIG_FILE)) { 
      authenticationType = AuthenticationType.DB_BASED_AUTHENTICATION;
    } else if(configFiles.contains(SPRING_SECURITY_LDAP_CONFIG_FILE) && configFiles.contains(PENTAHO_SECURITY_LDAP_CONFIG_FILE)) { 
      authenticationType = AuthenticationType.LDAP_BASED_AUTHENTICATION;
    }
      
    return authenticationType;
  }
  
  public void setAuthenticationType(AuthenticationType authenticationType) {
    if ((authenticationType != getAuthenticationType()) && (authenticationType != null)) {
      ArrayList<String> configFiles = new ArrayList<String>();
      configFiles.addAll(Arrays.asList(getSystemConfigFileNames()));
      configFiles.remove(SPRING_SECURITY_MEMORY_CONFIG_FILE);
      configFiles.remove(SPRING_SECURITY_DB_CONFIG_FILE);
      configFiles.remove(SPRING_SECURITY_LDAP_CONFIG_FILE);
      configFiles.remove(PENTAHO_SECURITY_MEMORY_CONFIG_FILE);
      configFiles.remove(PENTAHO_SECURITY_DB_CONFIG_FILE);
      configFiles.remove(PENTAHO_SECURITY_LDAP_CONFIG_FILE);
      switch (authenticationType) {
        case MEMORY_BASED_AUTHENTICATION:
          configFiles.add(SPRING_SECURITY_MEMORY_CONFIG_FILE);
          configFiles.add(PENTAHO_SECURITY_MEMORY_CONFIG_FILE);
          break;
        case DB_BASED_AUTHENTICATION:
          configFiles.add(SPRING_SECURITY_DB_CONFIG_FILE);
          configFiles.add(PENTAHO_SECURITY_DB_CONFIG_FILE);
          break;
        case LDAP_BASED_AUTHENTICATION:
          configFiles.add(SPRING_SECURITY_LDAP_CONFIG_FILE);
          configFiles.add(PENTAHO_SECURITY_LDAP_CONFIG_FILE);
          break;
      }
      setSystemConfigFileNames(configFiles.toArray(new String[0]));
    }
  }
  
}
