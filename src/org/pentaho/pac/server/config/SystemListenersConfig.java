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
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;


public class SystemListenersConfig {

  private static final String ROOT_ELEMENT = "beans";
  private static final String BEAN_ELEMENT = "bean";
  private static final String CLASS_ATTRIBUTE = "class";
  private static final String ID_ATTRIBUTE = "id";
  private static final String NAME_ATTRIBUTE = "name";
  private static final String VALUE_ATTRIBUTE = "value";
  private static final String PROPERTY_ELEMENT = "property";
  private static final String VALUE_ELEMENT = "value";
  private static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
  private static final String LIST_ELEMENT = "list";
  
  private static final String SYSTEM_LISTENER_LIST_ID="systemListenersList";
    
  public static final String JFREE_REPORT_SYSTEM_LISTENER_ID = "jfreeReportSystemListener";
  public static final String KETTLE_SYSTEM_LISTENER_ID = "kettleSystemListener";
  public static final String MONDRIAN_SYSTEM_LISTENER_ID = "mondrianSystemListener";
  public static final String QUARTZ_SYSTEM_LISTENER_ID = "quartzSystemListener";
  public static final String METADATA_SYSTEM_LISTENER_ID = "metadataSystemListener";
  public static final String VERSION_CHECKER_SYSTEM_LISTENER_ID = "versionCheckerSystemListener";
  public static final String NON_POOLED_DATASOURCE_SYSTEM_LISTENER_ID = "nonPooledDataSourceSystemListener";
  public static final String POOLED_DATASOURCE_SYSTEM_LISTENER_ID = "pooledDataSourceSystemListener";
  
  public static final String VERSION_CHECKER_DISABLED_PROPERTY = "disableVersionCheck";
  public static final String VERSION_CHECKER_INTERVAL_PROPERTY = "repeatIntervalSeconds";
  public static final String VERSION_CHECKER_RELEASES_PROPERTY = "requestedReleases";
  
  
  private static final String LISTENER_LIST_XPATH = ROOT_ELEMENT 
                                                    + "/" + BEAN_ELEMENT 
                                                    + "[@" + ID_ATTRIBUTE + "=\"" + SYSTEM_LISTENER_LIST_ID + "\"]" 
                                                    + "/" + CONSTRUCTOR_ARG_ELEMENT
                                                    + "/" + LIST_ELEMENT; 
  private static final String LISTENER_BEAN_XPATH = LISTENER_LIST_XPATH
                                                    + "/" + BEAN_ELEMENT; 
  private static final String LISTENER_BEAN_SEARCH_TEMPLATE = LISTENER_BEAN_XPATH
                                                              + "[@" + ID_ATTRIBUTE + "=\"{0}\"]"
                                                              + "[@" + CLASS_ATTRIBUTE + "=\"{1}\"]";
  private static final String LISTENER_BEAN_ID_SEARCH_TEMPLATE = LISTENER_BEAN_XPATH
                                                                + "[@" + ID_ATTRIBUTE + "=\"{0}\"]";
  private static final String LISTENER_BEAN_CLASS_SEARCH_TEMPLATE = LISTENER_BEAN_XPATH
                                                                + "[@" + CLASS_ATTRIBUTE + "=\"{0}\"]";
  
  Document document;
  
  public SystemListenersConfig(File systemListenerConfigFile) throws IOException, DocumentException{
    this(XmlDom4JHelper.getDocFromFile(systemListenerConfigFile, null));    
  }
  
  public SystemListenersConfig(String xml) throws DocumentException {
    this(DocumentHelper.parseText(xml));
  }
  
  public SystemListenersConfig(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException("Invalid root element.");
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
