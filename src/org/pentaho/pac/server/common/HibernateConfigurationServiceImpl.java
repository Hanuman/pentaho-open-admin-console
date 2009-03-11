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
package org.pentaho.pac.server.common;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.pac.client.HibernateConfigurationService;
import org.pentaho.pac.common.HibernateConfigurationServiceException;
import org.pentaho.pac.common.NameValue;
import org.pentaho.pac.server.i18n.Messages;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class HibernateConfigurationServiceImpl extends RemoteServiceServlet implements HibernateConfigurationService {

  private static final long serialVersionUID = 420L;

  List<NameValue> hibernateConfigurations = new ArrayList<NameValue>();

  private static final String HIBERNATE = "/system/hibernate"; //$NON-NLS-1$

  public HibernateConfigurationServiceImpl() {
  }

  public void initialize() {
  }

  public NameValue[] getAvailableHibernateConfigurations() throws HibernateConfigurationServiceException {
    String solutionPath = AppConfigProperties.getInstance().getSolutionPath();
    if (solutionPath != null && solutionPath.length() > 0) {
      return getAvailableHibernateConfigurations(solutionPath);
    } else {
      throw new HibernateConfigurationServiceException(Messages
          .getErrorString("HibernateConfigurationService.ERROR_0001_SOLUTION_PATH_IS_NULL"));//$NON-NLS-1$
    }
  }

  public NameValue[] getAvailableHibernateConfigurations(String solutionPath)
      throws HibernateConfigurationServiceException {
    return getConfigurations(solutionPath);
  }

  private NameValue[] getConfigurations(String solutionPath) throws HibernateConfigurationServiceException {
    if (hibernateConfigurations.size() == 0) {
      if (solutionPath != null && solutionPath.length() > 0) {
        File parent = new File(solutionPath + HIBERNATE);
        FilenameFilter filter = new FilenameFilter() {

          public boolean accept(File dir, String pathname) {
            return pathname.endsWith(".xml") && pathname.contains("hibernate.cfg"); //$NON-NLS-1$//$NON-NLS-2$
          }
        };

        if (parent.exists()) {
          for (File file : parent.listFiles(filter)) {
            String fileName = file.getName();
            if(fileName != null && fileName.length() > 0) {
              String truncatedFileName = fileName.substring(0, fileName.indexOf(".hibernate"));//$NON-NLS-1$            
              hibernateConfigurations.add(new NameValue(truncatedFileName, fileName));
            }
          }
        } else {
          throw new HibernateConfigurationServiceException(Messages
              .getErrorString("HibernateConfigurationService.ERROR_0002_NO_CONFIGURATION_FILES_FOUND_IN_PATH")); //$NON-NLS-1$
        }

      } else {
        throw new HibernateConfigurationServiceException(Messages
            .getErrorString("HibernateConfigurationService.ERROR_0001_SOLUTION_PATH_IS_NULL")); //$NON-NLS-1$
      }
    }
    return hibernateConfigurations.toArray(new NameValue[hibernateConfigurations.size()]);
  }
}
