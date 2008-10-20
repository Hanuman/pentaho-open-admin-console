package org.pentaho.pac.server.config;

import java.io.File;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

public class TestManagerConfig {

  Document document;
  private static final String ROOT_ELEMENT = "test-suite";
  private static final String TEST_MGR_XPATH = ROOT_ELEMENT + "/test-manager";
  private static final String DEFAULT_FILE_NAME = "test-suite/test-settings.xml";
  
  public TestManagerConfig(File pentahoXmlFile) throws IOException, DocumentException{
    this(XmlDom4JHelper.getDocFromFile(pentahoXmlFile, null));    
  }
  
  public TestManagerConfig(String xml) throws DocumentException {
    this(DocumentHelper.parseText(xml));
  }
  
  public TestManagerConfig(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException("Invalid root element.");
    }
    document = doc;
  }
  
  public TestManagerConfig() {
    document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
  }
  
  public String getTestSuiteManager() {
    return getValue(TEST_MGR_XPATH);
  }
  
  public void setTestSuiteManager(String testSuiteManager) {
    setValue(TEST_MGR_XPATH, testSuiteManager);
  }
  
  private void setValue(String xPath, String value) {
    Element element = (Element) document.selectSingleNode( xPath );
    if (element == null) {
      element = DocumentHelper.makeElement(document, xPath);
    }
    element.setText(value);
  }

  private String getValue(String xpath) {
    String value = null;
    Element element = (Element)document.selectSingleNode(xpath);
    return element != null ? element.getText() : null;
  }
  
  public Document getDocument() {
    return document;
  }
}
