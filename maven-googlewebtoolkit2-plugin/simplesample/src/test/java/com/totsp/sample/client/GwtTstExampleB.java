package com.totsp.sample.client;

import junit.framework.Assert;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTstExampleB extends GWTTestCase {

   public String getModuleName() {
      return "com.totsp.sample.Application";
   }

   public void testSomething() {

      // uncomment these to get ERROR (as opposed to failure)
      ///String test = "123";
      ///String test2 = test.substring(6, 9); 

      // Not much to actually test in this sample app
      // Ideally you would test your Controller here (NOT YOUR UI)
      // (Make calls to RPC services, test client side model objects, test client side logic, etc)
      Assert.assertTrue(true);
   }
}