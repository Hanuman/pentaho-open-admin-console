package org.pentaho.pac.client;

import org.pentaho.pac.common.datasources.PentahoDataSource;

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
    return "org.pentaho.pac.PentahoAdminConsole";
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
        String driverClass = "";
        int idleConn = 20;
        String jndiName = "myJndi";
        int maxActConn = 25;
        String userName = "hibuser";
        String password = "password";
        String query = "select * from users;";
        String url = "jdbc:hsqldb:hsql://localhost:9001/hibernate";
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


  public void testUpdateDataSource() {
    // we're going to need to wait a few seconds to let the async. call complete before we check
    // the users list to see if it has been populated with users. We'll create a timer that we'll 
    // schedule for future execution. When the timer is executed it will make sure the users list
    // has been populated.
    Timer timer = new Timer() {
      public void run() {
        PentahoDataSource dataSource = new PentahoDataSource();
        String driverClass = "";
        int idleConn = 20;
        String jndiName = "myJndi";
        int maxActConn = 25;
        String userName = "pentaho_user";
        String password = "password";
        String query = "select * from orders;";
        String url = "jdbc:hsqldb:hsql://localhost:9001/sampledata";
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
        String driverClass = "";
        int idleConn = 20;
        String jndiName = "myJndi";
        int maxActConn = 25;
        String userName = "pentaho_user";
        String password = "password";
        String query = "select * from orders;";
        String url = "jdbc:hsqldb:hsql://localhost:9001/sampledata";
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
  
}
