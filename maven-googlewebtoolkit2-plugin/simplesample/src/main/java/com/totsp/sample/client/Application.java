package com.totsp.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.totsp.sample.client.SampleRemoteService;
import com.totsp.sample.client.SampleRemoteServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Application implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    Image img = new Image("http://code.google.com/webtoolkit/logo-185x175.png");
    Button button = new Button("Click me");

    VerticalPanel vPanel = new VerticalPanel();
    // We can add style names.
    vPanel.addStyleName("widePanel");
    vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    vPanel.add(img);
    vPanel.add(button);

    // Add image and button to the RootPanel
    RootPanel.get().add(vPanel);

    // Create the dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Welcome to GWT!");
    dialogBox.setAnimationEnabled(true);
    Button closeButton = new Button("close");
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.setWidth("100%");
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    dialogVPanel.add(closeButton);

    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialogBox.hide();
      }
    });

    // Set the contents of the Widget
    dialogBox.setWidget(dialogVPanel);
    
    button.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialogBox.center();
        dialogBox.show();
      }
    });
  
  
  final Label label = new Label();
  final Button rpcButton = new Button("Run GWT Async RPC!");  
  rpcButton.setTitle("This will execute the RPC call to the Java server");  
  rpcButton.addClickListener(new ClickListener() {
     public void onClick(Widget sender) {

        // (1) Create the client proxy. Note that although you are
        // creating the
        // service interface proper, you cast the result to the
        // asynchronous
        // version of
        // the interface. The cast is always safe because the generated
        // proxy
        // implements the asynchronous interface automatically.
        SampleRemoteServiceAsync sampleRemoteService = (SampleRemoteServiceAsync) GWT
              .create(SampleRemoteService.class);

        // (2) Specify the URL at which our service implementation is
        // running.
        // Note that the target URL must reside on the same domain and
        // port from
        // which the host page was served.
        ServiceDefTarget endpoint = (ServiceDefTarget) sampleRemoteService;

        String moduleRelativeURL = GWT.getModuleBaseURL()
              + "sampleRemoteService";
        endpoint.setServiceEntryPoint(moduleRelativeURL);

        // (3) Create an asynchronous callback to handle the result.
        AsyncCallback callback = new AsyncCallback() {
           public void onSuccess(Object result) {
              // do some UI stuff to show success
              label.setText((String) result);
           }

           public void onFailure(Throwable caught) {
              // do some UI stuff to show failure
              label.setText("DAMMIT! This didnt work.");
           }
        };

        // (4) Make the call. Control flow will continue immediately and
        // later
        // 'callback' will be invoked when the RPC completes.
        sampleRemoteService.doComplimentMe(callback);

     }
   });
  
  RootPanel.get().add(new HTML("<br /><br />"));
  RootPanel.get().add(label);
  RootPanel.get().add(rpcButton);  
  
  RootPanel.get().add(new HTML("<br /><br />"));
  RootPanel.get().add(new Label("change URL to append \"?locale=fr\" to see the fancy french text"));  
  
  RootPanel.get().add(new HTML("<br /><br />"));
  AppConstants appConstants = (AppConstants) GWT.create(AppConstants.class);
  String constantTest = appConstants.constant1();
  RootPanel.get().add(new Label ("i18n constantTest - " + constantTest));
  
  RootPanel.get().add(new HTML("<br /><br />"));
  AppMessages appMessages = (AppMessages) GWT.create(AppMessages.class);
  String messageTest = appMessages.message1("zipededoodaaa");
  RootPanel.get().add(new Label ("i18n messageTest - " + messageTest));
  
  
  
 }
}
