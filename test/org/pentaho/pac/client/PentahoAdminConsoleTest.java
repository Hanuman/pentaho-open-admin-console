package org.pentaho.pac.client;

import org.pentaho.pac.client.UserAndRoleMgmtService;

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

}
