package org.apache.luke.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * client side interface for reading remote directory
 * @author dmitrykan
 *
 */
@RemoteServiceRelativePath("fsnavigator")
public interface FsNavigator extends RemoteService {
	public Set<String> getDirEntries(String targetDirPath, boolean goOneLevelUp);
}
