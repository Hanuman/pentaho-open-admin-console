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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.pentaho.pac.common.config.TestResult;
import org.pentaho.platform.engine.security.userroledao.messages.Messages;

public abstract class AbstractDiagnosticsJmxXml {

  protected static final String ROOT_ELEMENT = "jmxxml";  //$NON-NLS-1$
  protected static final String ID_ATTRIBUTE = "id"; //$NON-NLS-1$
  protected static final String ATTRIBUTE_ELEMENT = "attribute"; //$NON-NLS-1$
  protected static final String VALUE_ELEMENT = "value"; //$NON-NLS-1$
  protected static final String RESULT_CODE_ELEMENT = "resultCode"; //$NON-NLS-1$
  protected static final String DESCRIPTION_ELEMENT = "description"; //$NON-NLS-1$
  protected static final String ATTRIBUTE_VALUE_XPATH = ROOT_ELEMENT + "/" + ATTRIBUTE_ELEMENT + "[@" + ID_ATTRIBUTE + "=\"{0}\"]/" + VALUE_ELEMENT; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  protected static final String ATTRIBUTE_RESULT_CODE_XPATH = ROOT_ELEMENT + "/" + ATTRIBUTE_ELEMENT + "[@" + ID_ATTRIBUTE + "=\"{0}\"]/" + RESULT_CODE_ELEMENT; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  protected static final String ATTRIBUTE_DESCRIPTION_XPATH = ROOT_ELEMENT + "/" + ATTRIBUTE_ELEMENT + "[@" + ID_ATTRIBUTE + "=\"{0}\"]/" + DESCRIPTION_ELEMENT; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  protected static final String ATTRIBUTE_XPATH = ROOT_ELEMENT + "/" + ATTRIBUTE_ELEMENT + "[@" + ID_ATTRIBUTE + "=\"{0}\"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  
  protected Document document;
  
  public AbstractDiagnosticsJmxXml(String jmxXml) throws DocumentException {
    this(DocumentHelper.parseText(jmxXml));
  }
  
  public AbstractDiagnosticsJmxXml(Document jmxDocument) throws DocumentException {
    if(jmxDocument != null) {
      Element rootElement = jmxDocument.getRootElement();
      if ((rootElement != null) &&  !jmxDocument.getRootElement().getName().equals(ROOT_ELEMENT)) {
        throw new DocumentException(Messages.getErrorString("GoogleMapsConfig.ERROR_0001_INVALID_ROOT_ELEMENT")); //$NON-NLS-1$
      }
      document = jmxDocument;
    }
  }
  
  public AbstractDiagnosticsJmxXml() {
    document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
  }
  
  
  protected String getAttributeResultCode(String attributeName) {
    String xPath = MessageFormat.format(ATTRIBUTE_RESULT_CODE_XPATH, attributeName);
    Node node = document.selectSingleNode(xPath);
    return node != null ? node.getText() : null;
  }
  
  protected void setAttributeResultCode(String attributeName, String resultCode) {
    String attrXPath = MessageFormat.format(ATTRIBUTE_RESULT_CODE_XPATH, attributeName);
    Element attrElement = (Element)document.selectSingleNode(attrXPath);
    if (resultCode == null) {
      if (attrElement != null) {
        attrElement.detach();
      }
    } else {
      if (attrElement == null) {
        attrElement = document.getRootElement().addElement(ATTRIBUTE_ELEMENT);
        attrElement.addAttribute(ID_ATTRIBUTE, attributeName);
      }
      Element attrValueElement = DocumentHelper.makeElement(attrElement, RESULT_CODE_ELEMENT);
      attrValueElement.setText(resultCode);
    }
  }
  
  protected String getAttributeValue(String attributeName) {
    String xPath = MessageFormat.format(ATTRIBUTE_VALUE_XPATH, attributeName);
    Node node = document.selectSingleNode(xPath);
    return node != null ? node.getText() : null;
  }
  
  protected void setAttributeValue(String attributeName, String attributeValue) {
    String attrXPath = MessageFormat.format(ATTRIBUTE_XPATH, attributeName);
    Element attrElement = (Element)document.selectSingleNode(attrXPath);
    if (attributeValue == null) {
      if (attrElement != null) {
        attrElement.detach();
      }
    } else {
      if (attrElement == null) {
        attrElement = document.getRootElement().addElement(ATTRIBUTE_ELEMENT);
        attrElement.addAttribute(ID_ATTRIBUTE, attributeName);
      }
      Element attrValueElement = DocumentHelper.makeElement(attrElement, VALUE_ELEMENT);
      attrValueElement.setText(attributeValue);
    }
  }
  
  public List<TestResult> getWarnings() {
    // All warnings are currently being returned as errors.
    return new ArrayList<TestResult>();
//    List<Element> resultCodeElements = document.selectNodes("//" + ATTRIBUTE_ELEMENT + "/" + RESULT_CODE_ELEMENT);
//    for (Element resultCodeElement : resultCodeElements) {
//      if (resultCodeElement.getText().equals("WARN")) {
//        TestResult testResult = new TestResult();
//        Element attributeElement = resultCodeElement.getParent();
//        testResult.setTestName(attributeElement.attributeValue(ID_ATTRIBUTE));
//        testResult.setResultMessage(attributeElement.element(VALUE_ELEMENT).getText());
//        warnings.add(testResult);
//      }
//    }
//    return warnings;
  }
  
  public List<TestResult> getErrors() {
    ArrayList<TestResult> errors = new ArrayList<TestResult>();
    List<Element> resultCodeElements = document.selectNodes("//" + ATTRIBUTE_ELEMENT + "/" + RESULT_CODE_ELEMENT);
    for (Element resultCodeElement : resultCodeElements) {
      if (!resultCodeElement.getText().equals("PASS")) {
        TestResult testResult = new TestResult();
        Element attributeElement = resultCodeElement.getParent();
        testResult.setTestName(attributeElement.attributeValue(ID_ATTRIBUTE));
        testResult.setResultMessage(attributeElement.element(VALUE_ELEMENT).getText());
        errors.add(testResult);
      }
    }
    return errors;
  }
}
