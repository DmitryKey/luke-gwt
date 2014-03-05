package org.apache.luke.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileSystemNavigator extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Set<String> fileSystemItems = null;
		
		if (req.getParameter("path") == null) {
			fileSystemItems = this.getServletContext().getResourcePaths(".");
		}
		
		PrintWriter out = resp.getWriter();
		for (String item: fileSystemItems) {
			out.write(item + ",");			
		}
		
		out.close();
	}
	
	

}
