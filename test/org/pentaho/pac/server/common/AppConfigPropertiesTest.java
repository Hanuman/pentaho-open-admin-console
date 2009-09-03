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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Tests {@link AppConfigProperties}.
 * 
 * @author mlowery
 */
public class AppConfigPropertiesTest {

  @Test
  public void testGetInstance() {
    assertNotNull(AppConfigProperties.getInstance());
  }

  @Test
  public void testGetX() {
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getBiServerBaseUrl()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getBiServerContextPath()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getBiServerStatusCheckPeriod()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getHibernateConfigPath()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getHomepageTimeout()));
    //assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getPasswordServiceClass()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getPlatformUsername()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getSolutionPath()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getWarPath()));
  }

}
