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
  
  public String getContextConfigFileName() {
    return getContextParamValue(CONTEXT_CONFIG_CONTEXT_PARAM_NAME);
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

  public void setContextConfigFileName(String fileName) {
    setContextParamValue(CONTEXT_CONFIG_CONTEXT_PARAM_NAME, fileName);
  }

  public void setBaseUrl(String baseUrl) {
    setContextParamValue(BASE_URL_CONTEXT_PARAM_NAME, baseUrl); //$NON-NLS-1$
  }

  public void setSolutionPath(String solutionPath) {
    setContextParamValue(SOLUTION_PATH_CONTEXT_PARAM_NAME, solutionPath); //$NON-NLS-1$
  }
    
  public void setLocaleLanguage(String language) {
    setContextParamValue(LOCALE_LANGUAGE_CONTEXT_PARAM_NAME, language); //$NON-NLS-1$
  }
  
  public void setLocaleCountry(String country) {
    setContextParamValue(LOCALE_COUNTRY_CONTEXT_PARAM_NAME, country); //$NON-NLS-1$
  }
  
  public void setEncoding(String encoding) {
    setContextParamValue(ENCODING_CONTEXT_PARAM_NAME, encoding); //$NON-NLS-1$
  }
  
  public void setHomePage(String homePage) {
    setServletMapping(HOME_SERVLET_NAME, homePage); //$NON-NLS-1$
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
    Element contextParamNameElement = (Element)document.selectSingleNode(xPath);
    if (value == null) {
      if (contextParamNameElement != null) {
        contextParamNameElement.getParent().detach();
      }
    } else {
      if (contextParamNameElement == null) {
        contextParamNameElement = document.getRootElement().addElement(CONTEXT_PARAM_ELEMENT);
        Element paramNameElement = contextParamNameElement.addElement(PARAM_NAME_ELEMENT);
        paramNameElement.setText(name);
      }
      Element paramValueElement = DocumentHelper.makeElement(contextParamNameElement.getParent(), PARAM_VALUE_ELEMENT);
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
