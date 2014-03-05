package com.totsp.mavenplugin.gwt.scripting;

public class TestResult {

   public enum TestCode {
      SUCCESS, FAILURE, ERROR
   }

   public TestCode code;
   public String message;
   public String lastLine;
}