package com.totsp.sample.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.totsp.sample.client.controller.MyProjectController;
import com.totsp.sample.client.model.DataChangeListener;
import com.totsp.sample.client.model.Entry;
import com.totsp.sample.client.model.MyProjectData;


/**
 * EntryPoint example of a sample app that contains a GWT RPC call,
 * in order to demonstrate using the TotSP Maven GWT plugin:
 * http://code.google.com/p/gwt-maven (with context.xml and web.xml in the shell AND in deploy artifact).
 *
 * In the real world do not put all your crap in an EntryPoint like this
 * (this is an example which is meant to be quick and dirty - in reality you should make re-usable widgets in their own classes, 
 * and then add a main widget to the entry point, if the entire app is GWT based, else just add the specific widget(s)).
 * 
 * Also note that we are using a rudimentary MVC approach here, see the model and controller packages for more detail.
 *
 * @author ccollins
 *
 */
public class MyProjectEntryPoint implements EntryPoint {
    
    // basic layout and input elements
    final VerticalPanel panel = new VerticalPanel();
    final Label label = new Label("Results:");
    final Label name = new Label("Enter Name: ");
    final Button button = new Button("Go");
    final TextBox input = new TextBox();
    FlexTable table = new FlexTable();
    
    /**
     * EntryPoint onModuleLoad.
     */
    public void onModuleLoad() {
        
        panel.setStyleName("main-panel");
        
        // we are going to use some simple home grown MVC here to separate the layers
        // so that we don't bind view components directly to RPC calls and so on
        // (this makes later maintenance a lot easier, and facilitates testing sans the UI)
        
        // instantiate the model
        final MyProjectData data = new MyProjectData();

        // instantiate the controller
        final MyProjectController controller = new MyProjectController(data);
        
        // add a view change listener to the "data" model object 
        // and if the model changes, update the view
        // (this is used INSTEAD of passing view components into the controller, or worse, just doing service calls here)
        data.addChangeListener(new DataChangeListener() {
                public void onChange(MyProjectData data) {
                    // update view with data
                    MyProjectEntryPoint.this.updateTable(data.getEntries());
                }
            });

        // handle the button being clicked with a ClickListener
        button.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    // make the RPC call to the server
                    controller.addEntry(input.getText());
                }
            });
        
        // for the FIRST time the module is loaded, prime the data pump (outside the listener)
        // (would pull this from RPC call in real life, to get data already in DB)
        List<Entry> primeEntries = new ArrayList<Entry>();
        Entry entry = new Entry();
        entry.name="No entries yet";
        entry.time="0";
        primeEntries.add(entry);        
        this.updateTable(primeEntries);

        // add the widgets to the panel, and the panel to the RootPanel
        panel.add(name);
        panel.add(input);
        panel.add(new HTML("<br />"));
        panel.add(button);
        panel.add(new HTML("<br />"));
        panel.add(label);
        panel.add(table);
        RootPanel.get().add(panel);
    }
    
    // populate table widget with current entries
    public void updateTable(List<Entry> entries) {
        int count = 0;
        for (Entry e : entries) {
            table.setText(count, 0, e.time);
            table.setText(count, 1, e.name);
            table.getFlexCellFormatter().setStyleName(count, 0, "table-style0");
            table.getFlexCellFormatter().setStyleName(count, 1, "table-style1");
            count++;
        }        
    }
}
