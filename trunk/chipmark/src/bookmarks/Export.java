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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create a Nescape Bookmark file by placing the users links folders,
 * permissions, labels, and descriptions into an HTML file. Netscape file format
 * is available at:
 * http://msdn.microsoft.com/workshop/browser/external/overview/
 * bookmark_file_format.asp
 * 
 * @author Ross Anderson
 */
public class Export extends HttpServlet {
	/**
	 * Unique identifier for this servlet
	 */
	private static final long serialVersionUID = 134368451368763L;

	/**
	 * This is the message displayed to unauthenticated requests.
	 */
	private static final String unathenticatedMessage = "You must be logged in to export bookmarks.";

	/**
	 * Handles Post request from client. (this simply turns around and does a
	 * Get)
	 * 
	 * @param request
	 *            Request received from client.
	 * @param response
	 *            Response object sent back to client
	 * @throws ServletException
	 *             If a servlet error occurs
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Handles Get request from client.
	 * 
	 * @param request
	 *            Request received from client.
	 * @param response
	 *            Response object sent back to client.
	 * @throws ServletException
	 *             If a servlet error occurs
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// prepare request
		Utilities.prepareRequest(request);

		// get database
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		// validate user
		ClientEntry user = Utilities.validateUser(request, db);

		// read in client parameters
		String agent = Utilities.eliminateNull((String) request
				.getParameter("agent"));
		String submitBtn = Utilities.eliminateNull((String) request
				.getParameter("submitBtn"));

		// init xml response
		BookmarkXML xml = new BookmarkXML();

		if (user == null || user.getClientID() == 0) {
			// insert msg to user
			xml.constructWithResultMsg("", unathenticatedMessage);

			// prep reponse, send response, and return
			Utilities.prepareResponseWeb(response);
			Utilities.styleXML(xml, "error", user);
			Utilities.returnResponse(response, xml);
			return;
		}
		if (submitBtn.equals("") && !agent.equals(Utilities.AGENT_EXT)) {
			// prep response, send response, and return
			xml.constructWithResultMsg(user.getClientName(), "");
			Utilities.prepareResponseWeb(response);
			Utilities.styleXML(xml, "export", user);
			Utilities.returnResponse(response, xml);
			return;
		} else {

			if (agent.equals(Utilities.AGENT_EXT)) {
				Utilities.prepareResponseAgent(response);
			} else {
				Utilities.prepareResponseWeb(response);
			}
			try {
				returnExportOfUserChipmarks(db, response, user);
			} catch (Exception e) {
				Utilities.ignoreChipmarkException(e);
				return;
			}
		}
	}

	private void returnExportOfUserChipmarks(DatabaseWrapper db,
			HttpServletResponse response, ClientEntry user) throws Exception {
		// get the writer
		PrintWriter r = response.getWriter();

		// discover local links, folders, and root folder
		ArrayList<Bookmark> links = db.getAllUserBookmarks(user);
		ArrayList<Folder> folders = db.getUserFolders(user);
		int root = db.getRootFolderID(user);

		// output content disposition
		Utilities.prepareExport(response);

		// Output file following Netscape Bookmark File Format
		r.println("<!DOCTYPE NETSCAPE-Bookmark-file-1>");
		r.println("<!-- This is an automatically generated file.");
		r.println("     It will be read and overwritten.");
		r.println("     Do Not Edit! -->");
		r
				.println("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">");
		// ^^ important and needed
		r.println("<TITLE>Bookmarks</TITLE>");
		r.println("<H1>Bookmarks</H1>\n");
		r.println("<DL><p>");

		// prepare folders by removing root
		folders = removeRootFolder(folders, root);

		// supporting data structures
		Stack<Integer> folderStack = new Stack<Integer>();
		folderStack.push(new Integer(root));
		Integer rootInt = new Integer(root);
		while (!folders.isEmpty() || !links.isEmpty()) {
			Integer parentID = (Integer) folderStack.peek();
			int folderIndex = findNextFolderIndex(parentID.intValue(), folders);

			// if no more folders under parent
			if (folderIndex < 0) {
				if (folderStack.size() < 1) {
					parentID = rootInt;
				} else {
					// close the folder level
					Integer rootCheck = folderStack.peek();
					if (!rootCheck.equals(rootInt)) {
						String tab = tabs(folderStack.size() - 1);
						r.println(tab + "</DL><p>");
					}
					folderStack.pop();
				}
			} else {
				// if there is a folder in this level, add it
				Folder currentFolder = folders.remove(folderIndex);
				folderStack.push(new Integer(currentFolder.getFolderID()));
				parentID = new Integer(currentFolder.getFolderID());
				insertSingleFolder(currentFolder, tabs(folderStack.size() - 1),
						r);
			}

			// add in all chipmarks to this folder
			insertAllLinksInFolder(parentID.intValue(), links, r, folderStack
					.size());

		}
		// close the export
		r.println("</DL><p>");
	}

	/**
	 * This method sub-exports a single folder.
	 * 
	 * @param currentFolder
	 *            , folder to be exported
	 * @param tab
	 *            , the current tab level of folder
	 * @param r
	 *            , the PrintWriter to export into
	 */
	private void insertSingleFolder(Folder currentFolder, String tab,
			PrintWriter r) {
		r.println(tab + "<DT><H3 FOLDED ADD_DATE=\"0000000000\">"
				+ currentFolder.getFolderName() + "</H3>");
		r.println(tab + "<DL><p>");

	}

	/**
	 * This method is a sub-export, inserting all the links contained in a given
	 * folder.
	 * 
	 * @param linkFolerID
	 *            , the parentID of the folder
	 * @param links
	 *            , the ArrayList<Bookmark> of all possible links
	 * @param r
	 *            , a PrintWriter to export into
	 * @param NumOfTabs
	 *            , the number of tabs to use when indexing.
	 */
	private void insertAllLinksInFolder(int linkFolderID,
			ArrayList<Bookmark> links, PrintWriter r, int NumOfTabs) {
		for (Iterator j = links.iterator(); j.hasNext();) {
			Bookmark currentLink = (Bookmark) j.next();
			if (currentLink.getLinkFolderParentID() == linkFolderID) {
				j.remove();
				String url = removeMarkup(currentLink.getLinkURL());
				String title = removeMarkup(currentLink.getLinkName());
				String description = removeMarkup(currentLink
						.getLinkDescription());
				String privacy = removeMarkup(currentLink.getLinkPermission());
				String labels = removeMarkup(currentLink.labelsToString());
				String currentTime = "0000000000";
				String tab = tabs(NumOfTabs);

				r.println(tab + "<DT><A HREF=\"" + url + "\" ADD_DATE=\""
						+ currentTime + "\" LAST_VISIT=\"" + currentTime
						+ "\" LAST_MODIFIED=\"" + currentTime + "\" PRIVACY=\""
						+ privacy + "\" LABELS=\"" + labels + "\">" + title
						+ "</A>");
				if (description.length() > 0) {
					tab = tabs(NumOfTabs + 1);
					r.println(tab + "<DD>" + description);
				}
			}
		}
	}

	/**
	 * This is a small helper function that removes the root folder from a set
	 * of folders.
	 * 
	 * @param folders
	 *            , ArrayList<Folders> containing a root folder
	 * @param root
	 *            , the ID of the root folder
	 * @return folders, the new ArrayList<Folder> without the root folder in it
	 */
	private ArrayList<Folder> removeRootFolder(ArrayList<Folder> folders,
			int root) {
		for (Iterator<Folder> i = folders.iterator(); i.hasNext();) {
			Folder current = i.next();

			if (current.getFolderID() == root) {
				i.remove();
				break;
			}
		}
		return folders;
	}

	/**
	 * Find id of next folder to use
	 * 
	 * @param parent
	 *            parent to search for
	 * @param folderList
	 *            list of folders to search through
	 * @return Return int of next folder to use, -1 if there the folders don't
	 *         go any deeper
	 */
	private int findNextFolderIndex(int parent, ArrayList<Folder> folderList) {
		for (int i = 0; i < folderList.size(); i++) {
			Folder current = folderList.get(i);
			if (current.getFolderParentID() == parent) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Create tab spacing for formatting
	 * 
	 * @param number
	 *            Number of tabs to insert
	 * @return Return string of tabs
	 */
	private String tabs(int number) {
		String output = "";
		for (int i = 0; i < number; i++) {
			output += "    ";
		}
		return output;
	}

	/**
	 * Remove markup to keep HTML output clean
	 * 
	 * @param in
	 *            string to remove markup from
	 * @return Return an encoded string
	 */
	private String removeMarkup(String in) {
		in = in.replaceAll("&", "&amp;");
		in = in.replaceAll("<", "&lt;");
		in = in.replaceAll(">", "&gt;");
		in = in.replaceAll("\"", "&quot;");
		in = in.replaceAll("'", "&#039;");
		return in;
	}
}
