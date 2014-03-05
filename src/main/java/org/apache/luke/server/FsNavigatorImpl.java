package org.apache.luke.server;

import java.io.File;
import java.util.Set;

import org.apache.luke.client.FsNavigator;
import org.apache.luke.utils.DataStructureDisplayer;

//import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FsNavigatorImpl extends RemoteServiceServlet implements FsNavigator {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Set<String> getDirEntries(String targetDirPath, boolean goOneLevelUp) {
		Set<String> fileSystemItems = null;
		
		System.out.println("received:" + targetDirPath);
		
		if (targetDirPath == null || targetDirPath.isEmpty()) {
			System.out.println("targetDirPath || currentDirPath are null or emtpy");
			fileSystemItems = super.getServletContext().getResourcePaths("/WEB-INF");
		} else if (goOneLevelUp) {
			//String levelUpPath = ".." + targetDirPath;
			String levelUpPath = "\\";
			System.out.println("Going one level up to: " + levelUpPath);
			
			System.out.println("parent is: " + new File(levelUpPath).getParent());
			
			fileSystemItems = super.getServletContext().getResourcePaths(levelUpPath);
		} else
			fileSystemItems = super.getServletContext().getResourcePaths(targetDirPath);
		
		
		new DataStructureDisplayer<String>().displaySet(fileSystemItems);
		
		return fileSystemItems;
	}

}
