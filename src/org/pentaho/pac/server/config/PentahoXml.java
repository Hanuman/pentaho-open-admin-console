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

public class PentahoXml {
  
  Document document;
  
  private static final String ROOT_ELEMENT = "pentaho-system";
  private static final String AUDIT_DATE_FORMAT_XPATH = ROOT_ELEMENT + "/audit/auditDateFormat";
  private static final String AUDIT_LOG_FILE_XPATH = ROOT_ELEMENT + "/audit/auditLogFile";
  private static final String AUDIT_LOG_SEPARATOR_XPATH = ROOT_ELEMENT + "/audit/id_separator";
  private static final String DEFAULT_PARAMETER_FORM = ROOT_ELEMENT + "/default-parameter-xsl";
  private static final String LOG_FILE_XPATH = ROOT_ELEMENT + "/log-file";
  private static final String LOG_LEVEL_XPATH = ROOT_ELEMENT + "/log-level";
  private static final String SOLUTION_REPOSITORY_CACHE_SIZE_XPATH = ROOT_ELEMENT + "/solution-repository/cache-size";
  private static final String ACL_FILES_XPATH = ROOT_ELEMENT + "/acl-files";
  private static final String ADMIN_ROLE_XPATH = ROOT_ELEMENT + "/acl-voter/admin-role";
  private static final String ANONYMOUS_USER_XPATH = ROOT_ELEMENT + "/anonymous-authentication/anonymous-user";
  private static final String ANONYMOUS_ROLE_XPATH = ROOT_ELEMENT + "/anonymous-authentication/anonymous-role";
  
  public PentahoXml(File pentahoXmlFile) throws IOException, DocumentException{
    this(getContents(pentahoXmlFile));    
  }
  
  public PentahoXml(String xml) throws DocumentException {
    this(DocumentHelper.parseText(xml));
  }
  
  public PentahoXml(Document doc) throws DocumentException {
    Element rootElement = doc.getRootElement();
    if ((rootElement != null) &&  !doc.getRootElement().getName().equals(ROOT_ELEMENT)) {
      throw new DocumentException("Invalid root element.");
    }
    document = doc;
  }
  
  public PentahoXml() {
    document = DocumentHelper.createDocument();
    document.addElement(ROOT_ELEMENT);
  }
  
  public String getAuditDateFormat() {
    return getValue(AUDIT_DATE_FORMAT_XPATH);
  }
  
  public void setAuditDateFormat(String auditDateFormat) {
    setValue(AUDIT_DATE_FORMAT_XPATH, auditDateFormat);
  }
  
  public String getAuditLogFile() {
    return getValue(AUDIT_LOG_FILE_XPATH);
  }
  
  public void setAuditLogFile(String auditLogFile) {
    setValue(AUDIT_LOG_FILE_XPATH, auditLogFile);
  }
  
  public String getAuditLogSeparator() {
    return getValue(AUDIT_LOG_SEPARATOR_XPATH);
  }
  
  public void setAuditLogSeparator(String auditLogSeparator) {
    setValue(AUDIT_LOG_SEPARATOR_XPATH, auditLogSeparator, true);
  }
  
  public String getDefaultParameterForm() {
    return getValue(DEFAULT_PARAMETER_FORM );
  }
  
  public void setDefaultParameterForm(String defaultParameterForm) {
    setValue(DEFAULT_PARAMETER_FORM , defaultParameterForm);
  }
  
  
  public String getLogFile() {
    return getValue(LOG_FILE_XPATH);
  }
  
  public void setLogFile(String logFile) {
    setValue(LOG_FILE_XPATH, logFile);
  }
  
  public String getLogLevel() {
    return getValue(LOG_LEVEL_XPATH);
  }
  
  public void setLogLevel(String logLevel) {
    setValue(LOG_LEVEL_XPATH, logLevel);
  }
  
  public Integer getSolutionRepositoryCacheSize() {
    Integer cacheSize = null;
    String tempValue = getValue(SOLUTION_REPOSITORY_CACHE_SIZE_XPATH);
    if (tempValue != null) {
      try {
        cacheSize = Integer.parseInt(tempValue);
      } catch(Exception ex) {
        // do nothing we'll return null
      }
    }
    return cacheSize;
  }
  
  public void setSolutionRepositoryCacheSize(Integer solutionReporitoryCacheSize) {
    setValue(SOLUTION_REPOSITORY_CACHE_SIZE_XPATH, solutionReporitoryCacheSize == null ? "" : solutionReporitoryCacheSize.toString());
  }
  
  
  public String getAclFiles() {
    return getValue(ACL_FILES_XPATH);
  }
  
  public void setAclFiles(String fileExtensions) {
    setValue(ACL_FILES_XPATH, fileExtensions != null ? fileExtensions : "");
  }
  
  public String getAdminRole() {
    return getValue(ADMIN_ROLE_XPATH);
  }
  
  public void setAdminRole(String role) {
    setValue(ADMIN_ROLE_XPATH, role != null ? role : "");
  }
  
  public String getAnonymousUser() {
    return getValue(ANONYMOUS_USER_XPATH);
  }
  
  public void setAnonymousUser(String user) {
    setValue(ANONYMOUS_USER_XPATH, user != null ? user : "");
  }
  
  public String getAnonymousRole() {
    return getValue(ANONYMOUS_ROLE_XPATH);
  }
  
  public void setAnonymousRole(String role) {
    setValue(ANONYMOUS_ROLE_XPATH, role != null ? role : "");
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
}
