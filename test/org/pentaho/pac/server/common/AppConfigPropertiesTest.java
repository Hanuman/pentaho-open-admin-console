package org.pentaho.pac.server.common;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.junit.Test;
import org.pentaho.platform.api.engine.ISystemSettings;

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
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getPasswordServiceClass()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getPlatformUsername()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getSolutionPath()));
    assertTrue(StringUtils.isNotBlank(AppConfigProperties.getInstance().getWarPath()));
  }

}
