/**
 * 
 */
package org.apache.luke.client;

import java.util.HashMap;

import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.store.FSDirectory;
import org.getopt.luke.decoders.Decoder;

/**
 * @author dmitrykan
 *
 */
public class LuceneIndexLoader {
	
	private static HashMap<String, Decoder> decoders = new HashMap<String, Decoder>();
	private static IndexCommit currentCommit = null;
	private static int tiiDiv = 1;
	
	public static void loadIndex(String pName, LukeInspector mainWnd) {
	    Object dirImpl = null; // TODO: a dialogue option: getSelectedItem(find(dialog, "dirImpl"));
	    String dirClass = null;
	    if (dirImpl == null) {
	      dirClass = FSDirectory.class.getName();
	    } 
	    if (pName == null || pName.trim().equals("")) {
	      //errorMsg("Invalid path.");
	      return;
	    }
	    boolean force = false;
	    boolean readOnly = true;
	    boolean ram = false;
	    boolean keepCommits = true;
	    boolean slowAccess = true;
	    decoders.clear();
	    currentCommit = null;
	    mainWnd.openIndex(pName, force, dirClass, readOnly, ram, keepCommits, null, tiiDiv);
	}
}
