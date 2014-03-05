package com.totsp.sample.client.model;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.totsp.sample.client.model.DataException;
import com.totsp.sample.client.model.Entry;


/**
 * Simple GWT CLIENT side RPC service interface.
 *
 * @author ccollins
 *
 */
public interface MyService extends RemoteService {
    public List<Entry> myMethod(String s) throws DataException;
    
    /**
     * Utility class for simplifying access to the instance of async service 
     * (props to Ryan Dewsbury for this tip).
     */
    public static class Util {
       private static MyServiceAsync instance;
       public static MyServiceAsync getInstance(){
          if (instance == null) {
             instance = (MyServiceAsync) GWT.create(MyService.class);
             ServiceDefTarget target = (ServiceDefTarget) instance;
             target.setServiceEntryPoint(GWT.getModuleBaseURL() + "MyService");
          }
          return instance;
       }
    }

}
