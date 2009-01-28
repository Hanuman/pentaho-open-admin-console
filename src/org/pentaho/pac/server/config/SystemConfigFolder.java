package org.pentaho.pac.server.config;

import java.io.File;

public class SystemConfigFolder {
  File systemFolder = null;
  
  public SystemConfigFolder(File folder) {
    systemFolder = folder;
  }
  
  public SystemConfigFolder(String path) {
    systemFolder = new File(path);
  }
  
  public File getFolder() {
    return systemFolder;
  }
  
  public File getSystemListenersConfigFile() { 
    return new File(getFolder().getAbsoluteFile() + File.separator + "systemListeners.xml"); //$NON-NLS-1$
  }
  
  public File getPentahoXmlFile() {
    return new File(getFolder().getAbsoluteFile() + File.separator + "pentaho.xml"); //$NON-NLS-1$
  }
  
  public File getAdminPluginsFile() {
    return new File(getFolder().getAbsoluteFile() + File.separator + "adminPlugins.xml"); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  public File getPentahoObjectsConfigFile() {
    return new File(getFolder().getAbsoluteFile() + File.separator + "pentahoObjects.spring.xml"); //$NON-NLS-1$
  }
  
  public File getSessionStartupActionsFile() {
    return new File(getFolder().getAbsoluteFile() + File.separator + "sessionStartupActions.xml"); //$NON-NLS-1$
  }
  
  public File getAcegiSecurityXmlFile() {
    return new File(getFolder().getAbsoluteFile() + File.separator + "applicationContext-acegi-security.xml"); //$NON-NLS-1$
  }
  
  public File getPentahoSecurityXmlFile() {
    return new File(getFolder().getAbsoluteFile() + File.separator + "applicationContext-pentaho-security-ldap.xml"); //$NON-NLS-1$
  }
  
  public File getAcegiSecurityLdapXmlFile() {
    return new File(getFolder().getAbsoluteFile() + File.separator + "applicationContext-acegi-security-ldap.xml"); //$NON-NLS-1$
  }
  
  public File getCommonAuthorizationXmlFile() {
    return new File(getFolder().getAbsoluteFile() + File.separator + "applicationContext-common-authorization.xml"); //$NON-NLS-1$
  }
}
