package org.apache.luke.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FsNavigatorAsync {
	public void getDirEntries(String dirPath, boolean goOneLevelUp, AsyncCallback<Set<String>> callback);
}
