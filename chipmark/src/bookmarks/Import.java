/*
 -------------------------------------------------------------------------------------------------
 Copyright 2004-2007 Chipmark.

 This file is part of Chipmark.

 Chipmark is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 Chipmark is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Chipmark; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------------------------------------
 */

package bookmarks;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bookmarks.upload.MultipartRequest;

/** Servlet to handle importing an HTML file into a users account.
 *
 *
 * Warning: Because this servlet relies upon incoming streams that
 * represet files, it is very important to shutdown, and restart
 * ports when rebuilding if you plan to use this.  Not restaring
 * the ports can cause data to be trapped and repeated out the streams.
 *
 * Important Note: The method used to parse the input file is based
 * on the Netscape Bookmark File Format which can be found at the
 * following address:
 * http://msdn.microsoft.com/workshop/browser/external/overview/bookmark_file_format.asp
 * @author Ross D. Anderson
 * @author Ross Olson
 */
public class Import extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 184735394635625252L;

	/**
	 *  Maximum file size allowed to upload. 
	 */    
	final int MAX_FILE_SIZE = 1024 * 1024 * 10;  // 10MB maximum file upload size

	/**
	 * This is the redirect for unathenticated requests.
	 */
	private static final String unauthenticatedRedirect = "Main";
	
	private static final String successfullyImportWebRedirect = "Manage";

	/** Handles Post request from client.
	 * @param request Request received from client.
	 * @param response Response object sent back to client.
	 * @throws ServletException If a servlet error occurs
	 * @throws IOException If an I/O error occurs
	 */    
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}

	/** Handles Get request from client.
	 * @param request Request received from client.
	 * @param response Response object to client.
	 * @throws ServletException If a servlet error occurs
	 * @throws IOException If an I/O error occurs
	 */    
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		//Required to correctly handle the file upload.
		Utilities.prepareSystemFileEncoding();
		
		// prepare the request
		Utilities.prepareRequest(request);
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		
		//initialize for later use
		BookmarkXML xml = new BookmarkXML();
		ServletInputStream servletinputstream = request.getInputStream();
		String contentType = request.getContentType();
		int contentLength = request.getContentLength();
		int maxImportNum = 0;  //Maximum number of chipmarks that can be imported before user's quota is reached
		
		//web or plugin, without multipart-formdata submission encoding
		String agent = Utilities.eliminateNull((String) request.getParameter("agent"));
		
		//validate client
		ClientEntry user = Utilities.validateUser(request, db); 
		if(user == null){
			if (agent.equals(Utilities.AGENT_EXT)){
				returnExtError(xml, "Error! Please log in before importing.", response);
			} else {
				returnWebRedirect(response);
			}
			return;
		} 

		if (contentLength > MAX_FILE_SIZE){
			returnWebError(xml, user.getClientName(), "Error! Imported files cannot exceed "+MAX_FILE_SIZE+"bytes", response, user);
			return;
		}
		try {
			maxImportNum = PropertyManager.database_max_chipmarks - db.countUserBookmarks(user);
		}
		catch (Exception e){
			Utilities.ignoreChipmarkException(e);
			return;
		}

		if (maxImportNum < 1){
			returnWebError(xml, user.getClientName(), "Chipmark quota of " + PropertyManager.database_max_chipmarks + " exceeded.  You cannot import any more chipmarks.", response, user);
			return;
		}

		/* The reason behind using a MultipartRequest instead of an HTTPServletRequest
		 * is because we are uploading a file, and must use a "multipart/form-data" form
		 * to transmit the file.  If we were transfering just the file, this wouldn't be
		 * necessary, but because we are transmiting other values as well, we must use
		 * the MultipartRequest to access the other values passed in.
		 */
		if(contentType != null && !contentType.equals("application/x-www-form-urlencoded")){
			MultipartRequest myUpload = new MultipartRequest(
					contentType,
					contentLength,
					servletinputstream,
					MAX_FILE_SIZE,
					Utilities.REQUEST_ENCODING);
				
			String permission = "";

			// any parameters now, as a multipart-form encoded submission?
			if (agent.equals(Utilities.AGENT_EXT)){
				permission = Utilities.eliminateNull((String) request.getParameter("permission"));
			} else {
				permission = Utilities.eliminateNull(myUpload.getURLParameter("permission"));
			}
			
			//set default permissions, just in case
			permission = setDefultPermissions(permission);

			// the file uploaded is stream to us via memory... (NOT a temporary file)
			InputStream myStream = myUpload.getFileContents("fname");
			
			//make sure there really is a file...
			validateFileExistance(myStream, user.getClientName(), xml, response, agent, user);

			// Use an HTML parser to breakdown the bookmark file and extract the new links
			ParseHTML breakup = new ParseHTML(maxImportNum);
			String result = breakup.ParseFile(myStream, db, user, permission);
			myStream.close();

			// If extension, return Links in XML, if web-based, redirect to
			if (agent.equals(Utilities.AGENT_EXT)){	
				if(result == null) {
					returnNewLinksToExt(xml, response, db, user);
				} else {
					this.returnExtError(xml, result, response);
				}
				
			} else {
				if(result == null) {
					response.sendRedirect(successfullyImportWebRedirect);
				} else {
					this.returnWebError(xml, user.getClientName(), result, response, user);
				}
			}

			return;
		} else {
			if (agent.equals(Utilities.AGENT_EXT)){
				returnExtError(xml, "Error! Unable to read file for importing.", response);
			} else {
				this.returnWebError(xml, user.getClientName(), "Error! Unable to read file for importing.", response, user);
			}
			return;
		}
	}

	/**
	 * 
	 * @param myStream
	 * @param xml
	 * @param response
	 * @param agent
	 * @throws IOException
	 */
	private void validateFileExistance(InputStream myStream, String userName, BookmarkXML xml, HttpServletResponse response, String agent, ClientEntry user) throws IOException {
		try {
			myStream.available();
		} catch (Exception e) {
			if(agent.equals(Utilities.AGENT_EXT))
				returnExtError(xml, "Error! Please specify a file to import.", response);
			else
				returnWebError(xml, userName, "Error! Please specify a file to import.", response, user);
			return;
		}
	}

	/**
	 * 
	 * @param xml
	 * @param response
	 * @param db
	 * @param user
	 * @throws IOException
	 */
	private void returnNewLinksToExt(BookmarkXML xml, HttpServletResponse response, DatabaseWrapper db, ClientEntry user) throws IOException {
		ArrayList<Bookmark> links = null;
		ArrayList<Folder> folders = null;
		ArrayList<String> labels = null;

		try {
			links = db.getAllUserBookmarks(user);
			folders = db.getUserFolders(user);
			labels = db.getAllUserLabels(user);
		} catch (Exception ex) {
			Utilities.ignoreChipmarkException(ex);
			return;
		}

		try {
			xml.constructFoldersAndLinks(links, folders, labels);
		} catch (Exception ex) {
			// output error
			return;
		}

		Utilities.prepareResponseAgent(response);
		Utilities.returnResponse(response, xml);
	}

	/**
	 * 
	 * @param xml
	 * @param response
	 * @throws IOException
	 */
	private void returnExtError(BookmarkXML xml, String message, HttpServletResponse response) throws IOException {
		xml.constructNodeActionFailure(message);
		Utilities.prepareResponseAgent(response);
		Utilities.returnResponse(response, xml);
	}

	/**
	 * 
	 * @param response
	 * @throws IOException
	 */
	private void returnWebRedirect(HttpServletResponse response) throws IOException {
		Utilities.prepareResponseWeb(response);
		response.sendRedirect(unauthenticatedRedirect);
	}

	/**
	 * 
	 * @param xml
	 * @param string
	 * @param response
	 * @throws IOException
	 */
	private void returnWebError(BookmarkXML xml, String userName, String string, HttpServletResponse response, ClientEntry user) throws IOException {
		xml.constructWithResultMsg(userName, string);
		//prep reponse, send response, and return
		Utilities.prepareResponseWeb(response);
		
		Utilities.styleXML(xml, "importerror", user);
		Utilities.returnResponse(response, xml);
	}

	/**
	 * 
	 * @param permission
	 * @return
	 */
	private String setDefultPermissions(String permission) {
		if (!(permission.equals("private"))){
			permission = new String("public");
		}
		return permission;
	}

}
