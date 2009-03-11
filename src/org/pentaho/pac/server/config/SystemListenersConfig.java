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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.pac.server.common.util.DtdEntityResolver;
import org.pentaho.platform.api.util.XmlParseException;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;


public class SystemListenersConfig {

  private static final String ROOT_ELEMENT = "beans"; //$NON-NLS-1$
  private static final String BEAN_ELEMENT = "bean"; //$NON-NLS-1$
  private static final String CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$
  private static final String ID_ATTRIBUTE = "id"; //$NON-NLS-1$
  private static final String NAME_ATTRIBUTE = "name"; //$NON-NLS-1$
  private static final String VALUE_ATTRIBUTE = "value"; //$NON-NLS-1$
  private static final String PROPERTY_ELEMENT = "property"; //$NON-NLS-1$
  private static final String VALUE_ELEMENT = "value"; //$NON-NLS-1$
  private static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg"; //$NON-NLS-1$
  private static final String LIST_ELEMENT = "list"; //$NON-NLS-1$
  
  private static final String SYSTEM_LISTENER_LIST_ID="systemListenersList";//$NON-NLS-1$ 
    
  public static final String JFREE_REPORT_SYSTEM_LISTENER_ID = "jfreeReportSystemListener"; //$NON-NLS-1$
  public static final String KETTLE_SYSTEM_LISTENER_ID = "kettleSystemListener"; //$NON-NLS-1$ 
  public static final String MONDRIAN_SYSTEM_LISTENER_ID = "mondrianSystemListener"; //$NON-NLS-1$  
  public static final String QUARTZ_SYSTEM_LISTENER_ID = "quartzSystemListener"; //$NON-NLS-1$
  public static final String METADATA_SYSTEM_LISTENER_ID = "metadataSystemListener"; //$NON-NLS-1$
  public static final String VERSION_CHECKER_SYSTEM_LISTENER_ID = "versionCheckerSystemListener"; //$NON-NLS-1$
  public static final String NON_POOLED_DATASOURCE_SYSTEM_LISTENER_ID = "nonPooledDataSourceSystemListener"; //$NON-NLS-1$
  public static final String POOLED_DATASOURCE_SYSTEM_LISTENER_ID = "pooledDataSourceSystemListener"; //$NON-NLS-1$
  
  public static final String VERSION_CHECKER_DISABLED_PROPERTY = "disableVersionCheck"; //$NON-NLS-1$
  public static final String VERSION_CHECKER_INTERVAL_PROPERTY = "repeatIntervalSeconds"; //$NON-NLS-1$
  public static final String VERSION_CHECKER_RELEASES_PROPERTY = "requestedReleases"; //$NON-NLS-1$
  
  
  private static final String LISTENER_LIST_XPATH = ROOT_ELEMENT 
                                                    + "/" + BEAN_ELEMENT //$NON-NLS-1$ 
                                                    + "[@" + ID_ATTRIBUTE + "=\"" + SYSTEM_LISTENER_LIST_ID + "\"]" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
                                                    + "/" + CONSTRUCTOR_ARG_ELEMENT //$NON-NLS-1$
                                                    + "/" + LIST_ELEMENT;  //$NON-NLS-1$
  private static final String LISTENER_BEAN_XPATH = LISTENER_LIST_XPATH
                                                    + "/" + BEAN_ELEMENT; //$NON-NLS-1$ 
  private static final String LISTENER_BEAN_SEARCH_TEMPLATE = LISTENER_BEAN_XPATH
                                                              + "[@" + ID_ATTRIBUTE + "=\"{0}\"]"//$NON-NLS-1$ //$NON-NLS-2$
                                                              + "[@" + CLASS_ATTRIBUTE + "=\"{1}\"]"; //$NON-NLS-1$ //$NON-NLS-2$
  private static final String LISTENER_BEAN_ID_SEARCH_TEMPLATE = LISTENER_BEAN_XPATH
                                                                + "[@" + ID_ATTRIBUTE + "=\"{0}\"]"; //$NON-NLS-1$ //$NON-NLS-2$
  private static final String LISTENER_BEAN_CLASS_SEARCH_TEMPLATE = LISTENER_BEAN_XPATH
                                                                + "[@" + CLASS_ATTRIBUTE + "=\"{0}\"]"; //$NON-NLS-1$ //$NON-NLS-2$ 
  
  Document document;
  
  public SystemListenersConfig(File systemListenerConfigFile) throws IOException, DocumentException{
    this(XmlDom4JHelper.getDocFromFile(systemListenerConfigFile, new DtdEntityResolver()));    
  }
  
  public SystemListenersConfig(String xml) throws DocumentException, XmlParseException {
    this(XmlDom4JHelper.getDocFromString(xml, new DtdEntityResolver()));
  }
  
  public SystemListenersConfig(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException(Messages.getErrorString("SystemListenersConfig.ERROR_0001_INVALID_ROOT_ELEMENT")); //$NON-NLS-1$
    }
    document = doc;
  }
  
  public SystemListenersConfig() {
    document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
  }
  
  public String getJFreeReportSystemListener() {
    return getSystemListenerClassName(JFREE_REPORT_SYSTEM_LISTENER_ID);
  }

  public void setJFreeReportSystemListener(String listenerClassName) {
    addSystemListener(JFREE_REPORT_SYSTEM_LISTENER_ID, listenerClassName);
  }

  public String getKettleSystemListener() {
    return getSystemListenerClassName(KETTLE_SYSTEM_LISTENER_ID);
  }

  public void setKettleSystemListener(String listenerClassName) {
    addSystemListener(KETTLE_SYSTEM_LISTENER_ID, listenerClassName);
  }

  public String getMondrianSystemListener() {
    return getSystemListenerClassName(MONDRIAN_SYSTEM_LISTENER_ID);
  }

  public void setMondrianSystemListener(String listenerClassName) {
    addSystemListener(MONDRIAN_SYSTEM_LISTENER_ID, listenerClassName);
  }

  public String getQuartzSystemListener() {
    return getSystemListenerClassName(QUARTZ_SYSTEM_LISTENER_ID);
  }

  public void setQuartzSystemListener(String listenerClassName) {
    addSystemListener(QUARTZ_SYSTEM_LISTENER_ID, listenerClassName);
  }

  public String getMetadataSystemListener() {
    return getSystemListenerClassName(METADATA_SYSTEM_LISTENER_ID);
  }

  public void setMetadataSystemListener(String listenerClassName) {
    addSystemListener(METADATA_SYSTEM_LISTENER_ID, listenerClassName);
  }

  public String getVersionCheckerSystemListener() {
    return getSystemListenerClassName(VERSION_CHECKER_SYSTEM_LISTENER_ID);
  }

  public void setVersionCheckerSystemListener(String listenerClassName) {
    addSystemListener(VERSION_CHECKER_SYSTEM_LISTENER_ID, listenerClassName);
  }

  private String getSystemListenerClassName(String listenerId) {
    SystemListener systemListener = getSystemListener(listenerId, null);
    return systemListener != null ? systemListener.getClassName() : null;
  }
  
  public void addSystemListener(String listenerId, String listenerClassName) {
    if (listenerId != null) {
      Element element = getSystemListenerElement(listenerId, null);
      if (element != null) {
        element.addAttribute(CLASS_ATTRIBUTE, listenerClassName);
      } else {
        addSystemListenerElement(listenerId, listenerClassName);
      }
    } else {
      Element element = getSystemListenerElement(null, listenerClassName);
      if (element == null) {
        addSystemListenerElement(listenerId, listenerClassName);
      }
    }
  }
  
  public void removeSystemListener(String listenerId, String listenerClassName) {
    Element systemListenerElement = getSystemListenerElement(listenerId, listenerClassName);
    if (systemListenerElement != null) {
      systemListenerElement.detach();
    }
  }
  
  @SuppressWarnings("unchecked")
  public List<SystemListener> getSystemListeners() {
    ArrayList<SystemListener> systemListeners = new ArrayList<SystemListener>();
    List elements = document.selectNodes( LISTENER_BEAN_XPATH );
    for (Object object : elements) {
      Element element = (Element)object;
      SystemListener systemListener = new SystemListener();
      systemListener.setClassName(element.attributeValue(CLASS_ATTRIBUTE));
      systemListener.setId(element.attributeValue(ID_ATTRIBUTE));
      systemListeners.add(systemListener);
    }
    return systemListeners;
  }
  
  @SuppressWarnings("unchecked")
  public void setSystemListeners(List<SystemListener> systemListeners) {
    List<Element> originalListenerElements = document.selectNodes( LISTENER_BEAN_XPATH );
    for (Element origElement : originalListenerElements) {
      origElement.detach();
    }
    
    for (SystemListener systemListener : systemListeners) {
      Element elementToReinsert = null;
      for (Element origElement : originalListenerElements) {
        elementToReinsert = null;;
        String newId = systemListener.getId();
        String origId = origElement.attributeValue(ID_ATTRIBUTE);
        String newClass = systemListener.getClassName();
        String origClass = origElement.attributeValue(CLASS_ATTRIBUTE);
        if (newId == null) {
          if (newClass.equals(origClass)) {
            origElement.addAttribute(ID_ATTRIBUTE, null);
            elementToReinsert = origElement;
            break;
          }
        } else if (newId.equals(origId)) {
          origElement.addAttribute(CLASS_ATTRIBUTE, newClass);
          elementToReinsert = origElement;
          break;
        }
      }
      if (elementToReinsert != null) {
        Element listenerListElement = (Element)document.selectSingleNode(LISTENER_LIST_XPATH);
        listenerListElement.add(elementToReinsert);
      }
      addSystemListener(systemListener.getId(), systemListener.getClassName());
    }
  }
  
  private Element addSystemListenerElement(String listenerId, String listenerClassName) {
    DocumentHelper.makeElement(document, LISTENER_LIST_XPATH);
    Element listenerBeanElement = getSystemListenerElement(listenerId, listenerClassName);
    if (listenerBeanElement == null) {
      Element listenerListElement = (Element)document.selectSingleNode(LISTENER_LIST_XPATH);
      listenerBeanElement = listenerListElement.addElement(BEAN_ELEMENT);
      listenerBeanElement.addAttribute(CLASS_ATTRIBUTE, listenerClassName);
      if (listenerId != null) {
        listenerBeanElement.addAttribute(ID_ATTRIBUTE, listenerClassName);
      }
    }
    return listenerBeanElement;
  }
  
  private Element getSystemListenerElement(String listenerId, String className) {
    String xPath = null;
    Element element = null;
    if ((listenerId != null) && (className == null)) {
      xPath = MessageFormat.format(LISTENER_BEAN_ID_SEARCH_TEMPLATE, listenerId);
    } else if ((listenerId == null) && (className != null)) {
      xPath = MessageFormat.format(LISTENER_BEAN_CLASS_SEARCH_TEMPLATE, className);
    } else if ((listenerId != null) && (className != null)) {
      xPath = MessageFormat.format(LISTENER_BEAN_SEARCH_TEMPLATE, listenerId, className);
    }
    
    if (xPath != null) {
      element = (Element) document.selectSingleNode( xPath );
    }
    return element;
  }
  
  private SystemListener getSystemListener(String listenerId, String className) {
    SystemListener systemListener = null;
    String xPath = null;
    if ((listenerId != null) && (className == null)) {
      xPath = MessageFormat.format(LISTENER_BEAN_ID_SEARCH_TEMPLATE, listenerId);
    } else if ((listenerId == null) && (className != null)) {
      xPath = MessageFormat.format(LISTENER_BEAN_CLASS_SEARCH_TEMPLATE, className);
    } else if ((listenerId != null) && (className != null)) {
      xPath = MessageFormat.format(LISTENER_BEAN_SEARCH_TEMPLATE, listenerId, className);
    }
    
    if (xPath != null) {
      Element element = (Element) document.selectSingleNode( xPath );
      if (element != null) {
        systemListener = new SystemListener();
        systemListener.setClassName(element.attributeValue(CLASS_ATTRIBUTE));
        systemListener.setId(element.attributeValue(ID_ATTRIBUTE));
      }
    }
    
    return systemListener;
  }
    
  public Document getDocument() {
    return document;
  }
  @SuppressWarnings("unchecked")
  public Properties getSystemListenerProperties(String listenerId, String className) {
    Properties properties = new Properties();
    Element systemListenerElement = getSystemListenerElement(listenerId, className);
    if (systemListenerElement != null) {
      List<Element> propertyElements = systemListenerElement.elements(PROPERTY_ELEMENT);
      for (Element propertyElement : propertyElements) {
        String key = propertyElement.attributeValue(NAME_ATTRIBUTE);
        String value = propertyElement.attributeValue(VALUE_ATTRIBUTE);
        if (value == null) {
          Element valueElement = propertyElement.element(VALUE_ELEMENT);
          if (valueElement != null) {
            value = valueElement.getText();
          }
        }
        properties.setProperty(key, value);
      }
    }
    return properties;
  }
  @SuppressWarnings("unchecked")
  public void setSystemListenerProperties(String listenerId, String className, Properties properties) {
    Element systemListenerElement = getSystemListenerElement(listenerId, className);
    if (systemListenerElement != null) {
      List<Element> propertyElements = systemListenerElement.elements(PROPERTY_ELEMENT);
      for (Element propertyElement : propertyElements) {
        propertyElement.detach();
      }
      Enumeration enumeration = properties.propertyNames();
      while (enumeration.hasMoreElements()) {
        String key = enumeration.nextElement().toString();
        String value = properties.getProperty(key);
        if (value != null) {
          Element propertyElement = systemListenerElement.addElement(PROPERTY_ELEMENT);
          propertyElement.addAttribute(NAME_ATTRIBUTE, key);
          propertyElement.addElement(VALUE_ELEMENT).setText(value);
        }
      }
    }
  }
}
