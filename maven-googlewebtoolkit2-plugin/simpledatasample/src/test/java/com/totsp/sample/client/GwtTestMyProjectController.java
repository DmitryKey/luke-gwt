package com.totsp.sample.client;

import junit.framework.Assert;

import com.google.gwt.junit.client.GWTTestCase;
import com.totsp.sample.client.controller.MyProjectController;
import com.totsp.sample.client.model.DataChangeListener;
import com.totsp.sample.client.model.MyProjectData;

public class GwtTestMyProjectController extends GWTTestCase {

    public String getModuleName() {
        return "com.totsp.sample.MyProject";
    }
    
    // an example of a GWT RPC test, delay in straight line OUTSIDE the callback
    // finish and assert INSIDE the callback
    public void testAddEntry() {
        MyProjectData data = new MyProjectData();
        MyProjectController controller = new MyProjectController(data);
        controller.addEntry("testing123");

        data.addChangeListener(new DataChangeListener() {
            public void onChange(MyProjectData data) {
                Assert.assertEquals(1, data.getEntries().size());
                finishTest();
            }
        });

        delayTestFinish(5000);
        Assert.assertTrue(true);
    }
}