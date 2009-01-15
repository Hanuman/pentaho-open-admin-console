package org.pentaho.pac.server.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.pentaho.pac.server.common.util.DtdEntityResolver;
import org.pentaho.platform.api.util.XmlParseException;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;
import org.pentaho.platform.util.xml.dom4j.XmlDom4JHelper;

/**
 * Object wrapper around contents of <code>applicationContext-acegi-security-hibernate.xml</code>.
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
