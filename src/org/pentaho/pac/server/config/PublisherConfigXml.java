package org.pentaho.pac.server.config;

import java.io.File;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.pac.common.config.IPublisherConfig;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

public class PublisherConfigXml implements IPublisherConfig {
  
  private static final String ROOT_ELEMENT = "publisher-config";  //$NON-NLS-1$
  private static final String PASSWORD_XPATH = ROOT_ELEMENT +"/publisher-password";  //$NON-NLS-1$
  
  String password;
  
  public PublisherConfigXml(File pentahoXmlFile) throws IOException, DocumentException{
    this(XmlDom4JHelper.getDocFromFile(pentahoXmlFile, null));    
  }
  
  public PublisherConfigXml(String xml) throws DocumentException {
    this(DocumentHelper.parseText(xml));
  }
  
  public PublisherConfigXml(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException(Messages.getErrorString("GoogleMapsConfig.ERROR_0001_INVALID_ROOT_ELEMENT")); //$NON-NLS-1$
    }
    setPassword(getValue(doc, PASSWORD_XPATH));
  }
  
  public PublisherConfigXml() {
  }
  
  
  private void setValue(Document document, String xPath, String value) {
    Element element = (Element) document.selectSingleNode( xPath );
    if (element == null) {
      element = DocumentHelper.makeElement(document, xPath);
    }
    element.setText(value);
  }

  private String getValue(Document document, String xpath) {
    Element element = (Element)document.selectSingleNode(xpath);
    return element != null ? element.getText() : null;
  }
  
  public Document getDocument() {
    Document document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
    setValue(document, PASSWORD_XPATH, getPassword());
    return document;
  }
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
}
