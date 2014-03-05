package com.totsp.sample.client.controller;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.totsp.sample.client.model.MyService;
import com.totsp.sample.client.model.MyServiceAsync;
import com.totsp.sample.client.model.Entry;
import com.totsp.sample.client.model.MyProjectData;

/**
 * This is an example of a rudimentary top level "controller" and the usage of MVC with a GWT app. 
 *  
 * MVC is an architectural pattern, and it happens on many levels, you may
 * have a controller per widget, and a controller for the entire app, and
 * you may have other not-so-explicit controllers. 
 * 
 * The idea though, however you slice it, is that the controller has the wiring to do
 * stuff through the action part of the model. In a canonical GWT app the action
 * part of the model is made up of the service implementations (and other client side operations 
 * related stuff).   
 * 
 * Then the view uses the controller to do stuff, the controller uses the service layer,
 * and everything is nicely de-coupled. Also note that the controller has NO direct
 * link to the view, the view uses the controller, but does NOT hand off elements that 
 * the controller then updates directly. Instead, the controller updates the model - and
 * the view is "watching" the model (the Observer pattern, here when we call data.setEntries
 * that fires a notification to all the listeners). 
 *
 * 
 * @author ccollins
 *
 */
public class MyProjectController {
    
    private MyProjectData data;
    
    public MyProjectController(MyProjectData data) {
        this.data = data;
    }    
   
    public void addEntry(final String s) {
        // invoke the service, getting reference to it from the handy inner Util class
        MyServiceAsync service = MyService.Util.getInstance();
        service.myMethod(s,
            new AsyncCallback<List<Entry>>() {
            
                // what to do if it works                
                public void onSuccess(List<Entry> results) {
                  data.setEntries(results);
                }

                // what to do if it fails
                public void onFailure(final Throwable caught) {
                    Window.alert("failure - \n" + caught.getMessage());
                }
            });
    }    
}