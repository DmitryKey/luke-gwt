package org.apache.luke.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class DirectoryLister extends DialogBox {

	private static final Binder binder = GWT.create(Binder.class);
	@UiField
	Button button;
	@UiField
	FlowPanel flowPanel;
	@UiField
	Button button_1;

    private CustomTreeModel myTreeModel = new CustomTreeModel();
    // left for reference
//	private SingleSelectionModel<FsNode> selectionModelCellTree = null;
//	private Map<FsNode, ListDataProvider<FsNode>> mapDataProviders = null;
//	private ListDataProvider<FsNode> rootDataProvider = null;

	@UiField(provided = true)
	CellTree cellTree = new CellTree(
			myTreeModel,
			"Item 1"
			);

	final AbstractSelectionModel<String> selectionModel = new SingleSelectionModel<String>();

	interface Binder extends UiBinder<Widget, DirectoryLister> {
		
	}

	public DirectoryLister() {
		setWidget(binder.createAndBindUi(this));
		setModal(true);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#onLoad()
	 */
	@Override
	protected void onLoad() {
		super.onLoad();
		fsNavigatorService.getDirEntries(null, false, callback);
	}

	// (1) Create the client proxy. Note that although you are creating the
	// service interface proper, you cast the result to the asynchronous
	// version of the interface. The cast is always safe because the
	// generated proxy implements the asynchronous interface automatically.
	//
	FsNavigatorAsync fsNavigatorService = (FsNavigatorAsync) GWT
			.create(FsNavigator.class);	
	
	// (2) Create an asynchronous callback to handle the result.
	//
	AsyncCallback<Set<String>> callback = new AsyncCallback<Set<String>>() {
		public void onFailure(Throwable caught) {
			Window.alert("Failed to call the remote service ;("
					+ caught.getMessage());
		}

		@Override
		public void onSuccess(Set<String> result) {
			String node = ((SingleSelectionModel<String>) selectionModel).getSelectedObject();
			
			Iterator<String> iter = result.iterator();
			
			while (iter.hasNext()) {
				String path = iter.next();
				if (node != null)
					myTreeModel.add(node, path);
				else myTreeModel.add(null, path);
			}
		}
	};

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		// (3) Make the call. Control flow will continue immediately and later
		// 'callback' will be invoked when the RPC completes.
		//
		fsNavigatorService.getDirEntries(null, false, callback);
	}

	@UiHandler("button_1")
	void onButton_1Click(ClickEvent event) {
		this.hide();
	}

	 /**
	   * The model that defines the nodes in the tree.
	   */
	  private static class CustomTreeModel implements TreeViewModel {
		  
		  ListDataProvider<String> dataProvider = new ListDataProvider<String>();

	    /**
	     * Get the {@link NodeInfo} that provides the children of the specified
	     * value.
	     */
	    public <T> NodeInfo<?> getNodeInfo(T value) {
//	      /*
//	       * Create some data in a data provider. Use the parent value as a prefix
//	       * for the next level.
//	       */
//	      for (int i = 0; i < 2; i++) {
//	        dataProvider.getList().add(value + "." + String.valueOf(i));
//	      }
	      
	      // add level-up marker
	      dataProvider.getList().add("..");

	      // Return a node info that pairs the data with a cell.
	      return new DefaultNodeInfo<String>(dataProvider, new TextCell());
	    }

	    public void add(String parentNode, String childNode) {
			// root-node
			if (parentNode == null) {
				dataProvider.getList().add(childNode);
				// mapDataProviders.put(child, rootDataProvider);
			} else {
				// TODO: locate the parent node and add the child in proper place
				dataProvider.getList().add(childNode);
			}
	    }

		/**
	     * Check if the specified value represents a leaf node. Leaf nodes cannot be
	     * opened.
	     */
	    public boolean isLeaf(Object value) {
	      // The maximum length of a value is ten characters.
	      return value.toString().length() > 10;
	    }
	  }
	  
	/**
	 * File system node class
	 *
	 */
	  public class FsNode {

		private String name;
		private ArrayList<FsNode> childs; // nodes childrens
		private FsNode parent; // track internal parent

		public FsNode(String name) {
			super();
			parent = null;
			this.name = name;
			childs = new ArrayList<FsNode>();
		}

		public boolean hasChildrens() {
			return childs.size() > 0;
		}

		public ArrayList<FsNode> getList() {
			return childs;
		}

		public FsNode getParent() {
			return parent;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}  
	
	@UiHandler("button_2")
	/**
	 * Go one level up in the file system hierarchy
	 * @param event
	 */
	void onButton_2Click(ClickEvent event) {
		myTreeModel.dataProvider.getList().clear();
		fsNavigatorService.getDirEntries("/WEB-INF", true, callback);
	}
}
