package org.pentaho.pac.server.config;

import java.io.File;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.pac.common.config.IGoogleMapsConfig;
import org.pentaho.pac.server.common.util.DtdEntityResolver;
import org.pentaho.platform.api.util.XmlParseException;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

public class GoogleMapsConfigXml implements IGoogleMapsConfig {

  private static final String ROOT_ELEMENT = "google";  //$NON-NLS-1$
  private static final String GOOGLE_MAPS_KEY_XPATH = ROOT_ELEMENT +"/google_maps_api_key";  //$NON-NLS-1$
  
  Document document;
  
  public GoogleMapsConfigXml(File pentahoXmlFile) throws IOException, DocumentException{
    this(XmlDom4JHelper.getDocFromFile(pentahoXmlFile, new DtdEntityResolver()));    
  }
  
  public GoogleMapsConfigXml(String xml) throws DocumentException, XmlParseException {
    this(XmlDom4JHelper.getDocFromString(xml, new DtdEntityResolver()));    
  }
  
  public GoogleMapsConfigXml(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException(Messages.getErrorString("GoogleMapsConfig.ERROR_0001_INVALID_ROOT_ELEMENT")); //$NON-NLS-1$
    }
    document = doc;
  }
  
  public GoogleMapsConfigXml() {
    document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
  }
  
  public String getGoogleMapsKey() {
    return getValue(GOOGLE_MAPS_KEY_XPATH);
  }
  
  public void setGoogleMapsKey(String googleMapsKey) {
    setValue(GOOGLE_MAPS_KEY_XPATH, googleMapsKey);
  }
  
  private void setValue(String xPath, String value) {
    Element element = (Element) document.selectSingleNode( xPath );
    if (element == null) {
      element = DocumentHelper.makeElement(document, xPath);
    }
    element.setText(value);
  }

  private String getValue(String xpath) {
    Element element = (Element)document.selectSingleNode(xpath);
    return element != null ? element.getText() : null;
  }
  
  public Document getDocument() {
    return document;
  }
}
