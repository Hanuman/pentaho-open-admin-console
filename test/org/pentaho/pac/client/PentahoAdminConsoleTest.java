package org.pentaho.pac.client;

import org.pentaho.pac.common.datasources.PentahoDataSource;
import org.pentaho.pac.common.roles.ProxyPentahoRole;
import org.pentaho.pac.common.users.ProxyPentahoUser;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class PentahoAdminConsoleTest extends GWTTestCase {
  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "org.pentaho.pac.PentahoAdminConsole"; //$NON-NLS-1$
  }

  /**
   * Add as many tests as you like.
   */
  public void testSimple() {

    // Refresh the security into. This will make an async. call 
    // to the server, which will take some time to complete.
    AsyncCallback callback = new AsyncCallback() {
      public void onSuccess(Object result) {
      }

      public void onFailure(Throwable caught) {
      }
    };
    UserAndRoleMgmtService.instance().refreshSecurityInfo(callback);

    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        assertTrue(UserAndRoleMgmtService.instance().getUsers().length > 0);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(10000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(5000);
  }

  public void testGetDataSource() {

    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            PentahoDataSource[] dataSources = (PentahoDataSource[]) result;
            assertTrue(dataSources.length > 0);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        PacServiceFactory.getPacService().getDataSources(callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testCreateDataSource() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        PentahoDataSource dataSource = new PentahoDataSource();
        String driverClass = ""; //$NON-NLS-1$
        int idleConn = 20;
        String jndiName = "myJndi"; //$NON-NLS-1$
        int maxActConn = 25;
        String userName = "hibuser"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        String query = "select * from users;"; //$NON-NLS-1$
        String url = "jdbc:hsqldb:hsql://localhost:9001/hibernate"; //$NON-NLS-1$
        long wait = 1000;
        dataSource.setDriverClass(driverClass);
        dataSource.setIdleConn(idleConn);
        dataSource.setJndiName(jndiName);
        dataSource.setMaxActConn(maxActConn);
        dataSource.setPassword(password);
        dataSource.setQuery(query);
        dataSource.setUrl(url);
        dataSource.setUserName(userName);
        dataSource.setWait(wait);
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };

        PacServiceFactory.getPacService().createDataSource(dataSource, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testValidationQuery() {

    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        PentahoDataSource dataSource = new PentahoDataSource();
        String driverClass = ""; //$NON-NLS-1$
        int idleConn = 20;
        String jndiName = "myJndi"; //$NON-NLS-1$
        int maxActConn = 25;
        String userName = "hibuser"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        String query = "select * from users;"; //$NON-NLS-1$
        String url = "jdbc:hsqldb:hsql://localhost:9001/hibernate"; //$NON-NLS-1$
        long wait = 1000;
        dataSource.setDriverClass(driverClass);
        dataSource.setIdleConn(idleConn);
        dataSource.setJndiName(jndiName);
        dataSource.setMaxActConn(maxActConn);
        dataSource.setPassword(password);
        dataSource.setQuery(query);
        dataSource.setUrl(url);
        dataSource.setUserName(userName);
        dataSource.setWait(wait);
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };

        PacServiceFactory.getPacService().testDataSourceValidationQuery(dataSource, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }
  
  public void testUpdateDataSource() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        PentahoDataSource dataSource = new PentahoDataSource();
        String driverClass = ""; //$NON-NLS-1$
        int idleConn = 20;
        String jndiName = "myJndi"; //$NON-NLS-1$
        int maxActConn = 25;
        String userName = "pentaho_user"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        String query = "select * from orders;"; //$NON-NLS-1$
        String url = "jdbc:hsqldb:hsql://localhost:9001/sampledata"; //$NON-NLS-1$
        long wait = 1000;
        dataSource.setDriverClass(driverClass);
        dataSource.setIdleConn(idleConn);
        dataSource.setJndiName(jndiName);
        dataSource.setMaxActConn(maxActConn);
        dataSource.setPassword(password);
        dataSource.setQuery(query);
        dataSource.setUrl(url);
        dataSource.setUserName(userName);
        dataSource.setWait(wait);
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        PacServiceFactory.getPacService().updateDataSource(dataSource, callback);
        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testDeleteDataSources() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        PentahoDataSource dataSource = new PentahoDataSource();
        PentahoDataSource[] dataSources = new PentahoDataSource[1];
        String driverClass = ""; //$NON-NLS-1$
        int idleConn = 20;
        String jndiName = "myJndi"; //$NON-NLS-1$
        int maxActConn = 25;
        String userName = "pentaho_user"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        String query = "select * from orders;"; //$NON-NLS-1$
        String url = "jdbc:hsqldb:hsql://localhost:9001/sampledata"; //$NON-NLS-1$
        long wait = 1000;
        dataSource.setDriverClass(driverClass);
        dataSource.setIdleConn(idleConn);
        dataSource.setJndiName(jndiName);
        dataSource.setMaxActConn(maxActConn);
        dataSource.setPassword(password);
        dataSource.setQuery(query);
        dataSource.setUrl(url);
        dataSource.setUserName(userName);
        dataSource.setWait(wait);
        dataSources[0] = dataSource;
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };

        PacServiceFactory.getPacService().deleteDataSources(dataSources, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testGetUsers() {

    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        assertTrue(UserAndRoleMgmtService.instance().getUsers().length > 0);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testCreateUser() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        ProxyPentahoUser user = new ProxyPentahoUser();
        String description = "myuser"; //$NON-NLS-1$
        boolean enabled = true;
        String name = "hibuser"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        user.setDescription(description);
        user.setEnabled(enabled);
        user.setName(name);
        user.setPassword(password);

        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        UserAndRoleMgmtService.instance().createUser(user, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testUpdateUser() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        ProxyPentahoUser user = new ProxyPentahoUser();
        String description = "myuser"; //$NON-NLS-1$
        boolean enabled = true;
        String name = "hibuser"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        user.setDescription(description);
        user.setEnabled(enabled);
        user.setName(name);
        user.setPassword(password);

        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        UserAndRoleMgmtService.instance().updateUser(user, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();

      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testDeleteUser() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        ProxyPentahoUser[] users = new ProxyPentahoUser[1];
        ProxyPentahoUser user  = new ProxyPentahoUser();
        String description = "myuser"; //$NON-NLS-1$
        boolean enabled = true;
        String name = "hibuser"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        user.setDescription(description);
        user.setEnabled(enabled);
        user.setName(name);
        user.setPassword(password);
        users[0] = user;
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        UserAndRoleMgmtService.instance().deleteUsers(users, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();

      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }
  
  public void testGetRoles() {

    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        assertTrue(UserAndRoleMgmtService.instance().getRoles().length > 0);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testCreateRole() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        ProxyPentahoRole role = new ProxyPentahoRole();
        String description = "poweruser"; //$NON-NLS-1$
        String name = "poweruser"; //$NON-NLS-1$
        role.setDescription(description);
        role.setName(name);

        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        UserAndRoleMgmtService.instance().createRole(role, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testUpdateRole() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        ProxyPentahoRole role = new ProxyPentahoRole();
        String description = "poweruser"; //$NON-NLS-1$
        String name = "poweruser"; //$NON-NLS-1$
        role.setDescription(description);
        role.setName(name);

        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        UserAndRoleMgmtService.instance().updateRole(role, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();

      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testDeleteRoles() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        ProxyPentahoRole[] roles = new ProxyPentahoRole[1];
        ProxyPentahoRole role = new ProxyPentahoRole();
        String description = "poweruser"; //$NON-NLS-1$
        String name = "poweruser"; //$NON-NLS-1$
        role.setDescription(description);
        role.setName(name);
        roles[0] = role;
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        UserAndRoleMgmtService.instance().deleteRoles(roles, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  public void testHomePage() {

    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        String url = "http://www.pentaho.com/console_home"; //$NON-NLS-1$
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        PacServiceFactory.getPacService().getHomePage(url, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  
  public void testDataSourceConnection() {

    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        PentahoDataSource dataSource = new PentahoDataSource();
        String driverClass = ""; //$NON-NLS-1$
        int idleConn = 20;
        String jndiName = "myJndi"; //$NON-NLS-1$
        int maxActConn = 25;
        String userName = "hibuser"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        String query = "select * from users;"; //$NON-NLS-1$
        String url = "jdbc:hsqldb:hsql://localhost:9001/hibernate"; //$NON-NLS-1$
        long wait = 1000;
        dataSource.setDriverClass(driverClass);
        dataSource.setIdleConn(idleConn);
        dataSource.setJndiName(jndiName);
        dataSource.setMaxActConn(maxActConn);
        dataSource.setPassword(password);
        dataSource.setQuery(query);
        dataSource.setUrl(url);
        dataSource.setUserName(userName);
        dataSource.setWait(wait);
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };

        PacServiceFactory.getPacService().testDataSourceConnection(dataSource, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }
  
  public void testAddUsersToRole() {
    testCreateUser();
    testCreateRole();
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        
        ProxyPentahoRole[] roles = new ProxyPentahoRole[1];
        ProxyPentahoRole role = new ProxyPentahoRole();
        String description = "poweruser"; //$NON-NLS-1$
        String name = "poweruser"; //$NON-NLS-1$
        role.setDescription(description);
        role.setName(name);
        roles[0] = role;
        ProxyPentahoUser user  = new ProxyPentahoUser();
        String uDescription = "myuser"; //$NON-NLS-1$
        boolean enabled = true;
        String uName = "hibuser"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        user.setDescription(uDescription);
        user.setEnabled(enabled);
        user.setName(uName);
        user.setPassword(password);

        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        UserAndRoleMgmtService.instance().setRoles(user, roles, callback);

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        testDeleteUser();
        testDeleteRoles();
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }
  
  public void testAddRolesToUser() {
    testCreateUser();
    testCreateRole();

    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        ProxyPentahoUser[] users = new ProxyPentahoUser[1];
        ProxyPentahoRole role = new ProxyPentahoRole();
        String description = "poweruser"; //$NON-NLS-1$
        String name = "poweruser"; //$NON-NLS-1$
        role.setDescription(description);
        role.setName(name);
        ProxyPentahoUser user  = new ProxyPentahoUser();
        String uDescription = "myuser"; //$NON-NLS-1$
        boolean enabled = true;
        String uName = "hibuser"; //$NON-NLS-1$
        String password = "password"; //$NON-NLS-1$
        user.setDescription(uDescription);
        user.setEnabled(enabled);
        user.setName(uName);
        user.setPassword(password);
        users[0] = user;
        AsyncCallback callback = new AsyncCallback() {
          public void onSuccess(Object result) {
            assertTrue(true);
          }

          public void onFailure(Throwable caught) {
            fail();
          }
        };
        UserAndRoleMgmtService.instance().setUsers(role, users, callback);
        testDeleteUser();
        testDeleteRoles();

        // Finish this test. This will allow the test method to no longer be delayed in finishing.
        finishTest();
      }
    };

    // Delay the completion of this test method for some time, so that the timer can run and the 
    // results can be checked. If the finishTest() method is not called before this timer expires
    // and exception will be thrown and this test case will fail.
    delayTestFinish(30000);

    // Schedule the timer to fire in a few seconds. Again, we need to wait a bit to allow the async call 
    // to return results.
    timer.schedule(1000);
  }

  
}
