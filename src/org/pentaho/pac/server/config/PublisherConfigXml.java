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

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.pac.common.config.IPublisherConfig;
import org.pentaho.pac.server.common.util.DtdEntityResolver;
import org.pentaho.platform.api.util.XmlParseException;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

public class PublisherConfigXml implements IPublisherConfig {
  
  private static final String ROOT_ELEMENT = "publisher-config";  //$NON-NLS-1$
  private static final String PASSWORD_XPATH = ROOT_ELEMENT +"/publisher-password";  //$NON-NLS-1$
  
  String password;
  
  public PublisherConfigXml(File pentahoXmlFile) throws IOException, DocumentException{
    this(XmlDom4JHelper.getDocFromFile(pentahoXmlFile, new DtdEntityResolver()));    
  }
  
  public PublisherConfigXml(String xml) throws DocumentException, XmlParseException {
    this(XmlDom4JHelper.getDocFromString(xml, new DtdEntityResolver()));
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
