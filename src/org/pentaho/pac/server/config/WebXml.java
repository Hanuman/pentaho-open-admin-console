package org.pentaho.pac.server.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class WebXml {

  Document document;
  
  private static final String PARAM_NAME_ELEMENT = "param-name";
  private static final String PARAM_VALUE_ELEMENT = "param-value";
  private static final String ROOT_ELEMENT = "web-app";
  private static final String CONTEXT_CONFIG_CONTEXT_PARAM_NAME = "contextConfigLocation";
  private static final String BASE_URL_CONTEXT_PARAM_NAME = "base-url";
  private static final String SOLUTION_PATH_CONTEXT_PARAM_NAME = "solution-path";
  private static final String LOCALE_LANGUAGE_CONTEXT_PARAM_NAME = "locale-language";
  private static final String LOCALE_COUNTRY_CONTEXT_PARAM_NAME = "locale-country";
  private static final String ENCODING_CONTEXT_PARAM_NAME = "encoding";
  private static final String HOME_SERVLET_NAME = "Home";
  private static final String CONTEXT_PARAM_ELEMENT = "context-param";
  private static final String CONTEXT_PARAM_XPATH = ROOT_ELEMENT + "/" + CONTEXT_PARAM_ELEMENT;
  private static final String CONTEXT_PARAM_NAME_TEMPLATE_XPATH = CONTEXT_PARAM_XPATH + "/param-name[text()=\"{0}\"]";
  private static final String SERVLET_NAME_TEMPLATE_XPATH = ROOT_ELEMENT + "/servlet/servlet-name[text() = \"{0}\"]";
  
  private static final String ACEGI_SECURITY_LDAP_CONFIG_FILE = "applicationContext-acegi-security-ldap.xml";
  private static final String ACEGI_SECURITY_DB_CONFIG_FILE = "applicationContext-acegi-security-jdbc.xml";
  private static final String ACEGI_SECURITY_MEMORY_CONFIG_FILE = "applicationContext-acegi-security-memory.xml";
  private static final String PENTAHO_SECURITY_LDAP_CONFIG_FILE = "applicationContext-pentaho-security-ldap.xml";
  private static final String PENTAHO_SECURITY_DB_CONFIG_FILE = "applicationContext-pentaho-security-jdbc.xml";
  private static final String PENTAHO_SECURITY_MEMORY_CONFIG_FILE = "applicationContext-pentaho-security-memory.xml";
  
  public enum AuthenticationType {
    MEMORY_BASED_AUTHENTICATION, LDAP_BASED_AUTHENTICATION, DB_BASED_AUTHENTICATION
  };
  
  public WebXml(File pentahoXmlFile) throws IOException, DocumentException{
    this(getContents(pentahoXmlFile));    
  }
  
  public WebXml(String xml) throws DocumentException {
    this(DocumentHelper.parseText(xml));
  }
  
  public WebXml(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException("Invalid root element.");
    }
    document = doc;
  }
  
  public WebXml() {
    document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
  }
  
  public String getContextConfig() {
    return getContextParamValue(CONTEXT_CONFIG_CONTEXT_PARAM_NAME);
  }

  public AuthenticationType getAuthentication() {
    
    String contextConfig = getContextConfig();
    AuthenticationType authenticationType = null;
      
    if((contextConfig.indexOf(ACEGI_SECURITY_MEMORY_CONFIG_FILE) >= 0) && (contextConfig.indexOf(PENTAHO_SECURITY_MEMORY_CONFIG_FILE) >= 0)) { 
      authenticationType = AuthenticationType.MEMORY_BASED_AUTHENTICATION;
    } else if((contextConfig.indexOf(ACEGI_SECURITY_DB_CONFIG_FILE) >= 0) && (contextConfig.indexOf(PENTAHO_SECURITY_DB_CONFIG_FILE) >= 0)) { 
      authenticationType = AuthenticationType.DB_BASED_AUTHENTICATION;
    } else if((contextConfig.indexOf(ACEGI_SECURITY_LDAP_CONFIG_FILE) >= 0) && (contextConfig.indexOf(PENTAHO_SECURITY_LDAP_CONFIG_FILE) >= 0)) { 
      authenticationType = AuthenticationType.LDAP_BASED_AUTHENTICATION;
    }
      
    return authenticationType;
  }
  
  public String getBaseUrl() {
    return getContextParamValue(BASE_URL_CONTEXT_PARAM_NAME); //$NON-NLS-1$
  }

  public String getSolutionPath() {
    return getContextParamValue(SOLUTION_PATH_CONTEXT_PARAM_NAME); //$NON-NLS-1$
  }
    
  public String getLocaleLanguage() {
    return getContextParamValue(LOCALE_LANGUAGE_CONTEXT_PARAM_NAME); //$NON-NLS-1$
  }
  
  public String getLocaleCountry() {
    return getContextParamValue(LOCALE_COUNTRY_CONTEXT_PARAM_NAME); //$NON-NLS-1$
  }
  
  public String getEncoding() {
    return getContextParamValue(ENCODING_CONTEXT_PARAM_NAME); //$NON-NLS-1$
  }
  
  public String getHomePage() {
    return getServletMapping( HOME_SERVLET_NAME ); //$NON-NLS-1$
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
    String value = null;
    Element element = (Element)document.selectSingleNode(xpath);
    return element != null ? element.getText() : null;
  }
  
  public Document getDocument() {
    return document;
  }
  
  private static String getContents(File aFile) throws FileNotFoundException, IOException{
    StringBuilder contents = new StringBuilder();
    
    BufferedReader input =  new BufferedReader(new FileReader(aFile));
    try {
      String line = null;
      String lineSeparator = System.getProperty("line.separator");
      while (( line = input.readLine()) != null){
        contents.append(line);
        contents.append(lineSeparator);
      }
    }
    finally {
      input.close();
    }
    
    return contents.toString();
  }
  
  public String getContextParamValue( String name ) {
    String xPath = MessageFormat.format(CONTEXT_PARAM_NAME_TEMPLATE_XPATH, name);
    Node node = document.selectSingleNode(xPath);
    String value = null;
    if( node != null ) {
      node = node.selectSingleNode( "../param-value" ); //$NON-NLS-1$
    }
    if( node != null ) {
      value = node.getText();
    }
    return value;
  }
  
  public void setContextParamValue( String name, String value) {
    String xPath = MessageFormat.format(CONTEXT_PARAM_NAME_TEMPLATE_XPATH, name);
    Element contextParamElement = (Element)document.selectSingleNode(xPath);
    if (value == null) {
      if (contextParamElement != null) {
        contextParamElement.getParent().detach();
      }
    } else {
      if (contextParamElement == null) {
        contextParamElement = document.getRootElement().addElement(CONTEXT_PARAM_ELEMENT);
        Element paramNameElement = contextParamElement.addElement(PARAM_NAME_ELEMENT);
        paramNameElement.setText(name);
      }
      Element paramValueElement = DocumentHelper.makeElement(contextParamElement, PARAM_VALUE_ELEMENT);
      paramValueElement.setText(value);
    }
  }
  
  public boolean setServletMapping( String name, String value) {
    String xPath = MessageFormat.format(SERVLET_NAME_TEMPLATE_XPATH, name);
    Node node = document.selectSingleNode(xPath); //$NON-NLS-1$ //$NON-NLS-2$
    if( node != null ) {
      node = node.selectSingleNode( "../jsp-file" ); //$NON-NLS-1$
    }
    if( node != null ) {
      node.setText( value );
      return true;
    }
    return false;
  }

  public String getServletMapping( String name ) {
    String xPath = MessageFormat.format(SERVLET_NAME_TEMPLATE_XPATH, name);
    Node node = document.selectSingleNode(xPath); //$NON-NLS-1$ //$NON-NLS-2$
    String value = null;
    if( node != null ) {
      node = node.selectSingleNode( "../jsp-file" ); //$NON-NLS-1$
    }
    if( node != null ) {
      value = node.getText();
    }
    return value;
  }
}
