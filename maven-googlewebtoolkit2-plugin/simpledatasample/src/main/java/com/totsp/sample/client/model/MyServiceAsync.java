package com.totsp.sample.client.model;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.totsp.sample.client.model.Entry;


/**
 * Simple GWT CLIENT side ASYNC service interface
 * (same prefix NAME as non async interface, no return type, AsyncCallback param added, no explicit throws).
 *
 * @author ccollins
 *
 */
public interface MyServiceAsync {
    public void myMethod(String s, AsyncCallback<List<Entry>> callback);
}
