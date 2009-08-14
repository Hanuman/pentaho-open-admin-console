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
import org.pentaho.pac.server.common.util.DtdEntityResolver;
import org.pentaho.platform.api.util.XmlParseException;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;
import org.springframework.security.providers.encoding.PasswordEncoder;

/**
 * Object wrapper around contents of <code>applicationContext-spring-security-hibernate.xml</code>.
 * 
 * @author mlowery
 */
public class SpringSecurityHibernateConfig {
  Document document;

  private static final String ROOT_ELEMENT = "beans"; //$NON-NLS-1$
  
  private static final String PASSWORD_ENCODER_CLASS_XPATH = "/beans/bean[@id=\"passwordEncoder\"]/@class"; //$NON-NLS-1$

  public SpringSecurityHibernateConfig(File xmlFile) throws IOException, DocumentException, XmlParseException {
    this(getContents(xmlFile));
  }

  public SpringSecurityHibernateConfig(String xml) throws DocumentException, XmlParseException {
    this(XmlDom4JHelper.getDocFromString(xml, new DtdEntityResolver()));
  }

  public SpringSecurityHibernateConfig(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) && !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException(Messages.getErrorString("PentahoXml.ERROR_0001_INVALID_ROOT_ELEMENT")); //$NON-NLS-1$ 
    }
    document = doc;
  }

  public SpringSecurityHibernateConfig() {
    document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
  }

  /**
   * Returns the password encoder used during login in the platform or null if it cannot be instantiated.
   */
  public PasswordEncoder getPasswordEncoder() {
    try {
      String pentahoEncoderClassName = document.selectSingleNode(PASSWORD_ENCODER_CLASS_XPATH).getText();
      Class passwordEncoderClass = Class.forName(pentahoEncoderClassName);
      return (PasswordEncoder) passwordEncoderClass.newInstance();
    } catch (ClassNotFoundException e) {
      return null;
    } catch (InstantiationException e) {
      return null;
    } catch (IllegalAccessException e) {
      return null;
    }
  }
  
  public Document getDocument() {
    return document;
  }

  private static String getContents(File aFile) throws FileNotFoundException, IOException {
    StringBuilder contents = new StringBuilder();

    BufferedReader input = new BufferedReader(new FileReader(aFile));
    try {
      String line = null;
      String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
      while ((line = input.readLine()) != null) {
        contents.append(line);
        contents.append(lineSeparator);
      }
    } finally {
      input.close();
    }

    return contents.toString();
  }

}
