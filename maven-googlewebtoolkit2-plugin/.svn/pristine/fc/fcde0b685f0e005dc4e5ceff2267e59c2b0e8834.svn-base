package com.totsp.sample.client;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

/*
From GWT docs:
 
The GWTTestSuite mechanism has the overhead of having to start a hosted mode shell and servlet or compile your code. You can potentially increase the speed of your unit test runs by organizing your tests into suites of test cases.

Combining test cases together into a single GWTTestSuite  reduces overhead if multiple GWTTestCase classes share the same module (that is, they return the same value from getModuleName().) The GWTTestSuite class re-orders the test cases so that all cases that share a module are run back to back.

Creating a suite is simple if you have already defined individual JUnit TestCases or GWTTestCases. Here is an example:

public class MapsTestSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Test for a Maps Application");
    suite.addTestSuite(MapTest.class); 
    suite.addTestSuite(EventTest.class);
    suite.addTestSuite(CopyTest.class);
    return suite;
  }
}
*/

/**
 * GWTTestSuite example.
 */
public class GwtTestSuite extends GWTTestSuite {
    public static Test suite() {
      TestSuite suite = new TestSuite("sample app test suite");
      // Note, for GWT-Maven, if you NAME these tests 
      // so they DON"T match the test filter (start with GwtTest)
      // then they won't run individually - name them TstExampleA.java, or such.
      //
      // (Also, if you name them "TestSomething" or "SomethingTest" Surefire will pick them up, and that won't work I used "Tst" and not "Test".)
      //
      suite.addTestSuite(GwtTstExampleA.class); 
      suite.addTestSuite(GwtTstExampleB.class);
      
      // If you DO name them GwtTestSomething (start with GwtTest) they will still
      // work but they will be run individually, the suite like this is better).
      suite.addTestSuite(GwtTestExampleC.class);
      return suite;
    }
  }