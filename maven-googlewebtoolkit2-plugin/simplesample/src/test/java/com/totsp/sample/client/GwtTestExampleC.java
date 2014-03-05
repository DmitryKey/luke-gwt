package com.totsp.sample.client;

import junit.framework.Assert;

import com.google.gwt.junit.client.GWTTestCase;

// this class is named "GwtTest"Exammple to demonstrate that it WILL be picked up individually (see the GwtTestSuite for more info).
public class GwtTestExampleC extends GWTTestCase {

   public String getModuleName() {
      return "com.totsp.sample.Application";
   }

   public void testSomething() {
      Assert.assertTrue(true);

      // uncomment here to have a FAILURE
      ///Assert.assertTrue(false);
   }
}