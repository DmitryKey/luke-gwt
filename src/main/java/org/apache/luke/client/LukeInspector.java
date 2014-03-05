package org.apache.luke.client;
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.SegmentInfos;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.luke.client.data.FieldsDummyData;
import org.getopt.luke.KeepAllIndexDeletionPolicy;
import org.getopt.luke.KeepLastIndexDeletionPolicy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LukeInspector implements EntryPoint {
	
	public static Version LV = Version.LUCENE_46;
	
	private final static String solrIndexDir = "D:\\Projects\\information_retrieval\\solr\\solr-4.6.0\\solr-4.6.0\\example\\solr\\collection1\\data\\index";
	private String pName                     = null;
	private Directory dir                    = null;
	private IndexReader ir                   = null;
	private IndexSearcher is                 = null;
	
	public void onModuleLoad() {
		final RootPanel rootPanel = RootPanel.get();
		
		CaptionPanel cptnpnlNewPanel = new CaptionPanel("New panel");
		cptnpnlNewPanel.setCaptionHTML("Luke version 5.0");
		rootPanel.add(cptnpnlNewPanel, 10, 10);
		cptnpnlNewPanel.setSize("959px", "652px");
		
		TabPanel tabPanel = new TabPanel();
		cptnpnlNewPanel.setContentWidget(tabPanel);
		tabPanel.setSize("5cm", "636px");
		
		//LuceneIndexLoader.loadIndex(pName, this);
		
		SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel();
		tabPanel.add(splitLayoutPanel, "Index overview", false);
		tabPanel.setVisible(true);
		splitLayoutPanel.setSize("652px", "590px");
		
		SplitLayoutPanel splitLayoutPanel_1 = new SplitLayoutPanel();
		splitLayoutPanel.addNorth(splitLayoutPanel_1, 288.0);
		
		Label lblIndexStatistics = new Label("Index statistics");
		lblIndexStatistics.setDirectionEstimator(true);
		lblIndexStatistics.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		splitLayoutPanel_1.addNorth(lblIndexStatistics, 26.0);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		splitLayoutPanel_1.addWest(verticalPanel, 125.0);
		
		Label lblTest = new Label("Index name:");
		verticalPanel.add(lblTest);
		lblTest.setWidth("109px");
		
		Label lblTest_1 = new Label("# fields:");
		verticalPanel.add(lblTest_1);
		
		Label lblNumber = new Label("# documents:");
		verticalPanel.add(lblNumber);
		lblNumber.setWidth("101px");
		
		Label lblTerms = new Label("# terms:");
		verticalPanel.add(lblTerms);
		
		Label lblHasDeletions = new Label("Has deletions?");
		verticalPanel.add(lblHasDeletions);
		
		Label lblNewLabel = new Label("Optimised?");
		verticalPanel.add(lblNewLabel);
		
		Label lblIndexVersion = new Label("Index version:");
		verticalPanel.add(lblIndexVersion);
		
		SplitLayoutPanel splitLayoutPanel_2 = new SplitLayoutPanel();
		splitLayoutPanel.addWest(splitLayoutPanel_2, 240.0);
		
		
	    // Create name column.
	    TextColumn<Field> nameColumn = new TextColumn<Field>() {
	      @Override
	      public String getValue(Field field) {
	        return field.getName();
	      }
	    };

	    // Make the name column sortable.
	    nameColumn.setSortable(true);

	    // Create termCount column.
	    TextColumn<Field> termCountColumn = new TextColumn<Field>() {
	      @Override
	      public String getValue(Field contact) {
	        return contact.getTermCount();
	      }
	    };
		
	    // Create decoder column.
	    TextColumn<Field> decoderColumn = new TextColumn<Field>() {
	      @Override
	      public String getValue(Field contact) {
	        return contact.getDecoder();
	      }
	    };
		  
		final CellTable<Field> cellTable = new CellTable<Field>();

		cellTable.addColumn(nameColumn, "Name");
		cellTable.addColumn(termCountColumn, "Term count");
		cellTable.addColumn(decoderColumn, "Decoder");
		
		cellTable.setRowCount(FieldsDummyData.Fields.size(), true);
		 // Set the range to display. In this case, our visible range is smaller than
	    // the data set.
		cellTable.setVisibleRange(0, 3);
		
	    // Create a data provider.
	    AsyncDataProvider<Field> dataProvider = new AsyncDataProvider<Field>() {
	      @Override
	      protected void onRangeChanged(HasData<Field> display) {
	        final Range range = display.getVisibleRange();

	        // Get the ColumnSortInfo from the table.
	        final ColumnSortList sortList = cellTable.getColumnSortList();

	        // This timer is here to illustrate the asynchronous nature of this data
	        // provider. In practice, you would use an asynchronous RPC call to
	        // request data in the specified range.
	        new Timer() {
	          @Override
	          public void run() {
	            int start = range.getStart();
	            int end = start + range.getLength();
	            // This sorting code is here so the example works. In practice, you
	            // would sort on the server.
	            Collections.sort(FieldsDummyData.Fields, new Comparator<Field>() {
	              public int compare(Field o1, Field o2) {
	                if (o1 == o2) {
	                  return 0;
	                }

	                // Compare the name columns.
	                int diff = -1;
	                if (o1 != null) {
	                  diff = (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
	                }
	                return sortList.get(0).isAscending() ? diff : -diff;
	              }
	            });
	            List<Field> dataInRange = FieldsDummyData.Fields.subList(start, end);

	            // Push the data back into the list.
	            cellTable.setRowData(start, dataInRange);
	          }
	        }.schedule(2000);
	      }
	    };

	    // Connect the list to the data provider.
	    dataProvider.addDataDisplay(cellTable);

	    // Add a ColumnSortEvent.AsyncHandler to connect sorting to the
	    // AsyncDataPRrovider.
	    AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
	    cellTable.addColumnSortHandler(columnSortHandler);

	    // We know that the data is sorted alphabetically by default.
	    cellTable.getColumnSortList().push(nameColumn);
		
		splitLayoutPanel_2.add(cellTable);
		
		SplitLayoutPanel splitLayoutPanel_3 = new SplitLayoutPanel();
		splitLayoutPanel.addEast(splitLayoutPanel_3, 215.0);
		
		StackPanel stackPanel = new StackPanel();
		rootPanel.add(stackPanel, 714, 184);
		stackPanel.setSize("259px", "239px");
		
		FlowPanel flowPanel = new FlowPanel();
		stackPanel.add(flowPanel, "Open index", false);
		flowPanel.setSize("100%", "100%");
		
		TextBox textBox = new TextBox();
		flowPanel.add(textBox);
		
		Button btnNewButton = new Button("...");
		btnNewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DirectoryLister directoryLister = new DirectoryLister();
				directoryLister.setPopupPosition(rootPanel.getAbsoluteLeft() + rootPanel.getOffsetWidth() / 2,
						                         rootPanel.getAbsoluteTop() + rootPanel.getOffsetHeight() / 2);
				directoryLister.show();
				
			}
		});
		flowPanel.add(btnNewButton);
		
		
		// exception handling
		// credits: http://code.google.com/p/mgwt/source/browse/src/main/java/com/googlecode/mgwt/examples/showcase/client/ShowCaseEntryPoint.java?repo=showcase
	    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
	        @Override
	        public void onUncaughtException(Throwable e) {
	          Window.alert("uncaught: " + e.getMessage());
	          String s = buildStackTrace(e, "RuntimeExceotion:\n");
	          Window.alert(s);
	          e.printStackTrace();

	        }
		});
	    
	}
	
	
	// showing the stacktrace
	// credits: http://code.google.com/p/mgwt/source/browse/src/main/java/com/googlecode/mgwt/examples/showcase/client/ShowCaseEntryPoint.java?repo=showcase
    final String buildStackTrace(Throwable t, String log) {
        // return "disabled";
         if (t != null) {
         log += t.getClass().toString();
         log += t.getMessage();
         //
         StackTraceElement[] stackTrace = t.getStackTrace();
         if (stackTrace != null) {
         StringBuffer trace = new StringBuffer();
        
         for (int i = 0; i < stackTrace.length; i++) {
         trace.append(stackTrace[i].getClassName() + "." + stackTrace[i].getMethodName() + "("
         + stackTrace[i].getFileName() + ":" + stackTrace[i].getLineNumber());
         }
        
         log += trace.toString();
         }
         //
         Throwable cause = t.getCause();
         if (cause != null && cause != t) {
        
         log += buildStackTrace(cause, "CausedBy:\n");
        
         }
         }
         return log;
      }

    public Directory openDirectory(String dirImpl, String file, boolean create) throws Exception {
        File f = new File(file);
        if (!f.exists()) {
          throw new Exception("Index directory doesn't exist.");
        }
        Directory res = null;
        if (dirImpl == null || dirImpl.equals(Directory.class.getName()) || dirImpl.equals(FSDirectory.class.getName())) {
          return FSDirectory.open(f);
        }
        try {
          Class implClass = Class.forName(dirImpl);
          Constructor<Directory> constr = implClass.getConstructor(File.class);
          if (constr != null) {
            res = constr.newInstance(f);
          } else {
            constr = implClass.getConstructor(File.class, LockFactory.class);
            res = constr.newInstance(f, (LockFactory)null);
          }
        } catch (Throwable e) {
          //errorMsg("Invalid directory implementation class: " + dirImpl + " " + e);
          return null;
        }
        if (res != null) return res;
        // fall-back to FSDirectory.
        if (res == null) return FSDirectory.open(f);
        return null;
      }
	
    /**
	 * open Lucene index and re-init all the sub-widgets
	 * @param name
	 * @param force
	 * @param dirImpl
	 * @param ro
	 * @param ramdir
	 * @param keepCommits
	 * @param point
	 * @param tiiDivisor
	 */
    public void openIndex(String name, boolean force, String dirImpl, boolean ro,
		      boolean ramdir, boolean keepCommits, IndexCommit point, int tiiDivisor) {
		pName = name;
		File baseFileDir = new File(name);
		
		ArrayList<Directory> dirs = new ArrayList<Directory>();
		Throwable lastException = null;
		
        try {
            Directory d = openDirectory(dirImpl, pName, false);
            if (IndexWriter.isLocked(d)) {
              if (!ro) {
                if (force) {
                  IndexWriter.unlock(d);
                } else {
                  //errorMsg("Index is locked. Try 'Force unlock' when opening.");
                  d.close();
                  d = null;
                  return;
                }
              }
            }
            boolean existsSingle = false;
            // IR.indexExists doesn't report the cause of error
            try {
              new SegmentInfos().read(d);
              existsSingle = true;
            } catch (Throwable e) {
              e.printStackTrace();
              lastException = e;
              //
            }
            if (!existsSingle) { // try multi
              File[] files = baseFileDir.listFiles();
              for (File f : files) {
                if (f.isFile()) {
                  continue;
                }
                Directory d1 = openDirectory(dirImpl, f.toString(), false);
                if (IndexWriter.isLocked(d1)) {
                  if (!ro) {
                    if (force) {
                      IndexWriter.unlock(d1);
                    } else {
                      //errorMsg("Index is locked. Try 'Force unlock' when opening.");
                      d1.close();
                      d1 = null;
                      return;
                    }
                  }
                }
                existsSingle = false;
                try {
                  new SegmentInfos().read(d1);
                  existsSingle = true;
                } catch (Throwable e) {
                  lastException = e;
                  e.printStackTrace();
                }
                if (!existsSingle) {
                  d1.close();
                  continue;
                }
                dirs.add(d1);
              }
            } else {
              dirs.add(d);
            }
            
            if (dirs.size() == 0) {
              if (lastException != null) {
                //errorMsg("Invalid directory at the location, check console for more information. Last exception:\n" + lastException.toString());
              } else {
                //errorMsg("No valid directory at the location, try another location.\nCheck console for other possible causes.");
              }
              return;
            }

            if (ramdir) {
              //showStatus("Loading index into RAMDirectory ...");
              Directory dir1 = new RAMDirectory();
              IndexWriterConfig cfg = new IndexWriterConfig(LV, new WhitespaceAnalyzer(LV));
              IndexWriter iw1 = new IndexWriter(dir1, cfg);
              iw1.addIndexes((Directory[])dirs.toArray(new Directory[dirs.size()]));
              iw1.close();
              //showStatus("RAMDirectory loading done!");
              if (dir != null) dir.close();
              dirs.clear();
              dirs.add(dir1);
            }
            IndexDeletionPolicy policy;
            if (keepCommits) {
              policy = new KeepAllIndexDeletionPolicy();
            } else {
              policy = new KeepLastIndexDeletionPolicy();
            }
            ArrayList<DirectoryReader> readers = new ArrayList<DirectoryReader>();
            for (Directory dd : dirs) {
              DirectoryReader reader;
              if (tiiDivisor > 1) {
                reader = DirectoryReader.open(dd, tiiDivisor);
              } else {
                reader = DirectoryReader.open(dd);
              }
              readers.add(reader);
            }
            if (readers.size() == 1) {
              ir = readers.get(0);
              dir = ((DirectoryReader)ir).directory();
            } else {
              ir = new MultiReader((IndexReader[])readers.toArray(new IndexReader[readers.size()]));
            }
            is = new IndexSearcher(ir);
            // XXX 
            //slowAccess = false;
            //initOverview();
            //initPlugins();
            //showStatus("Index successfully open.");
          } catch (Exception e) {
            e.printStackTrace();
            //errorMsg(e.getMessage());
            return;
          }
	}

}
