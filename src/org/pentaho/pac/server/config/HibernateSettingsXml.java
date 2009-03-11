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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.pac.common.config.IHibernateSettings;
import org.pentaho.pac.server.common.util.DtdEntityResolver;
import org.pentaho.platform.api.util.XmlParseException;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

public class HibernateSettingsXml implements IHibernateSettings{
  
  Document document;
  
  private static final String ROOT_ELEMENT = "settings";//$NON-NLS-1$
  private static final String XPATH_TO_HIBERNATE_CFG_FILE = "settings/config-file";//$NON-NLS-1$
  private static final String XPATH_TO_HIBERNATE_MANAGED = "settings/managed"; //$NON-NLS-1$
  
  public HibernateSettingsXml(File hibernateSettingsXmlFile) throws IOException, DocumentException, XmlParseException{
    this(getContents(hibernateSettingsXmlFile));    
  }
  
  public HibernateSettingsXml(String xml) throws DocumentException, XmlParseException {
    this(XmlDom4JHelper.getDocFromString(xml, new DtdEntityResolver()));    
  }
  
  public HibernateSettingsXml(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException(Messages.getErrorString("HibernateSettingsXml.ERROR_0001_INVALID_ROOT_ELEMENT")); //$NON-NLS-1$
    }
    document = doc;
  }
  
  public HibernateSettingsXml() {
    document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
  }
  
  public String getHibernateConfigFile() {
    return getValue(XPATH_TO_HIBERNATE_CFG_FILE);
  }
  
  public void setHibernateConfigFile(String hibernateConfigFile) {
    setValue(XPATH_TO_HIBERNATE_CFG_FILE, hibernateConfigFile);
  }
  
  public boolean getHibernateManaged() {
    return Boolean.parseBoolean(getValue(XPATH_TO_HIBERNATE_MANAGED));
  }
  
  public void setHibernateManaged(boolean hibernateManaged) {
    setValue(XPATH_TO_HIBERNATE_MANAGED, Boolean.toString(hibernateManaged));
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
  
  private static String getContents(File aFile) throws FileNotFoundException, IOException{
    StringBuilder contents = new StringBuilder();
    
    BufferedReader input =  new BufferedReader(new FileReader(aFile));
    try {
      String line = null;
      String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
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
}
