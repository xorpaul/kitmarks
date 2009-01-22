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
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Handles Add, Edit, Move and Delete operations from Manage interface. Returns
 * XML to be parsed by javascript for AJAX functionality.
 * 
 * @author Joshua Fleck
 * 
 */
public class NodeAction extends HttpServlet {

	/**
	 * The unique identifier for this version of the class
	 */
	private static final long serialVersionUID = 3375981759249808974L;

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
		Utilities.prepareRequest(request);
		Utilities.prepareResponseAgent(response);

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		ClientEntry user = Utilities.validateUser(request, db);

		BookmarkXML xml = new BookmarkXML();

		if (user == null) {
			xml.constructNodeActionFailure("");
		} else {

			String actionType = Utilities.eliminateNull((String) request
					.getParameter("action"));

			if (actionType.equals("")) {
				xml.constructNodeActionFailure("Action not specified");
			} else if (actionType.equals("editLink")) {
				editLink(request, user, xml, db);
			} else if (actionType.equals("addLink")) {
				addLink(request, user, xml, db);
			} else if (actionType.equals("moveLink")) {
				this.moveLink(request, user, xml, db);
			} else if (actionType.equals("moveFolder")) {
				this.moveFolder(request, user, xml, db);
			} else if (actionType.equals("copyLink")) {
				this.copyLink(request, user, xml, db);
			} else if (actionType.equals("editFolder")) {
				this.editFolder(request, user, xml, db);
			} else if (actionType.equals("deleteLink")) {
				this.deleteLink(request, user, xml, db);
			} else if (actionType.equals("deleteFolder")) {
				this.deleteFolder(request, user, xml, db);
			} else if (actionType.equals("addFolder")) {
				this.addFolder(request, user, xml, db);
			} else if (actionType.equals("setFilter")) {
				this.setFilter(request, user, xml, db);
			} else if (actionType.equals("getFilter")) {
				this.getFilter(request, user, xml, db);
			} else if (actionType.equals("setPrefLang")) {
				this.setPrefLang(request, user, xml, db);
			} else if (actionType.equals("getPrefLang")) {
				this.getPrefLang(request, user, xml, db);
			} else if (actionType.equals("setprefLangSession")) {
				this.setPrefLangSession(request, user, xml, db);
			} else if (actionType.equals("getprefLangSession")) {
				this.getPrefLangSession(request, user, xml, db);
			}
		}

		Utilities.returnResponse(response, xml);
	}

	/**
	 * Sets content filtering preferences.
	 * 
	 * @param request
	 * @param user
	 * @param xml
	 * @param db
	 */
	private void setFilter(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {

		String f = Utilities.eliminateNull((String) request
				.getParameter("filter"));

		boolean setme = false;
		if (f.equals("true")) {
			setme = true;
		} else if (f.equals("false")) {
			setme = false;
		} else {
			xml
					.constructNodeActionFailure("Chipmark encountered an unexpected exception");
			return;
		}
		try {
			db.setClientFilterByClientName(setme, user.getClientName());
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		user.setOnlySafe(setme);

		xml.constructNodeActionSuccess();
	}

	/**
	 * Returns the filtering preference.
	 * 
	 * @param request
	 * @param user
	 * @param xml
	 * @param db
	 */
	private void getFilter(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {
		String f = "";
		try {
			f = db.getClientFilterByClientName(user.getClientName());
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}
		xml.constructNodeAction("SUCCESS", f);
	}

	/**
	 * Sets content filtering preferences.
	 * 
	 * @param request
	 * @param user
	 * @param xml
	 * @param db
	 */
	private void setPrefLang(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {

		String prefLang = Utilities.eliminateNull((String) request
				.getParameter("prefLang"));

		if (!prefLang.equals("ger") && !prefLang.equals("eng")) {
			xml
					.constructNodeActionFailure("Chipmark encountered an unexpected exception");
			return;
		}
		try {
			// prefLang = prefLang == "eng" ? "eng" : "ger";
			db.setPrefLangByClientName(prefLang, user.getClientName());
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		user.setPreferredLang(prefLang);

		xml.constructNodeActionSuccess();
	}

	/**
	 * Returns the preferred language from the database.
	 * 
	 * @param request
	 * @param user
	 * @param xml
	 * @param db
	 */
	private void getPrefLang(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {
		String prefLang = "";
		try {
			prefLang = db.getPrefLangByClientName(user.getClientName());
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}
		xml.constructNodeAction("SUCCESS", prefLang);
	}

	/**
	 * Sets content filtering preferences.
	 * 
	 * @param request
	 * @param user
	 * @param xml
	 * @param db
	 */
	private void setPrefLangSession(HttpServletRequest request,
			ClientEntry user, BookmarkXML xml, DatabaseWrapper db) {

		String prefLangSession = Utilities.eliminateNull((String) request
				.getParameter("prefLangSession"));

		HttpSession session = request.getSession(false);

		if (!prefLangSession.equals("ger") && !prefLangSession.equals("eng")) {
			xml
					.constructNodeActionFailure("Chipmark encountered an unexpected exception");
			return;
		}
		try {
			if (user.getClientID() != 0)
				db.setPrefLangByClientName(prefLangSession, user
						.getClientName());
			else {
				ClientEntry userJoined = new ClientEntry(0,
						"SessionDummyChanged", "dummy@session.de", "xxx", true,
						prefLangSession, "student");

				session = request.getSession(true);
				session.setAttribute(Utilities.SESSION_ATTR, userJoined);
			}

		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		user.setPreferredLang(prefLangSession);

		xml.constructNodeActionSuccess();
	}

	/**
	 * Returns the preferred langugage.
	 * 
	 * @param request
	 * @param user
	 * @param xml
	 * @param db
	 */
	private void getPrefLangSession(HttpServletRequest request,
			ClientEntry user, BookmarkXML xml, DatabaseWrapper db) {
		String prefLangSession = "";
		try {
			// prefLang = db.getPrefLangByClientName(user.getClientName());

			HttpSession session = request.getSession(false);

			if (session != null) {

				user = (ClientEntry) session
						.getAttribute(Utilities.SESSION_ATTR);

				prefLangSession = user.getPreferredLang();

			}

		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}
		xml.constructNodeAction("SUCCESS", prefLangSession);
	}

	/**
	 * Method to move Link from one folder to another.
	 * 
	 * @param request
	 *            Request received from client.
	 */
	private void moveLink(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {

		String linkID_entry_S = Utilities.eliminateNull((String) request
				.getParameter("linkID"));
		String destFolderID_entry_S = Utilities.eliminateNull((String) request
				.getParameter("destFolderID"));
		int linkID_entry = 0;
		int destFolderID_entry = 0;

		linkID_entry = Integer.parseInt(linkID_entry_S);
		destFolderID_entry = Integer.parseInt(destFolderID_entry_S);

		try {
			db.moveLink(user, linkID_entry, destFolderID_entry);
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		xml.constructNodeActionSuccess();

	}

	/**
	 * Method to move subfolder to a different parent.
	 * 
	 * @param request
	 *            Request received from client.
	 */
	private void moveFolder(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {

		String folderID_entry_S = Utilities.eliminateNull((String) request
				.getParameter("folderID"));
		String destFolderID_entry_S = Utilities.eliminateNull((String) request
				.getParameter("destFolderID"));
		int folderID_entry = 0;
		int destFolderID_entry = 0;

		folderID_entry = Integer.parseInt(folderID_entry_S);
		destFolderID_entry = Integer.parseInt(destFolderID_entry_S);

		try {
			db.moveFolder(user, folderID_entry, destFolderID_entry);
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		xml.constructNodeActionSuccess();
	}

	/**
	 * Method to duplicate a link.
	 * 
	 * @param request
	 *            Request received from client.
	 */
	private void copyLink(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {

		String linkID_entry_S = Utilities.eliminateNull((String) request
				.getParameter("linkID"));
		String destFolderID_entry_S = Utilities.eliminateNull((String) request
				.getParameter("destFolderID"));
		int linkID_entry = 0;
		int destFolderID_entry = 0;

		linkID_entry = Integer.parseInt(linkID_entry_S);
		destFolderID_entry = Integer.parseInt(destFolderID_entry_S);

		Bookmark newBookmark = null;

		try {
			newBookmark = db.copyLink(user, linkID_entry, destFolderID_entry);
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		xml.constructNodeAction(String.valueOf(newBookmark.getLinkID()), "");
	}

	/**
	 * Edit data of a link.
	 * 
	 * @param request
	 *            Request received from client.
	 */
	private void editLink(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {

		String linkID_entry_S = Utilities.eliminateNull((String) request
				.getParameter("linkID"));
		int linkID_entry = Integer.parseInt(linkID_entry_S);
		String linkName_entry = (String) request.getParameter("linkName");
		String linkURL_entry = (String) request.getParameter("linkURL");
		String linkPermission_entry = (String) request
				.getParameter("linkPermission");
		String linkDescription_entry = (String) request
				.getParameter("linkDescription");
		String toolbarPosition_entry = (String) request
				.getParameter("toolbarPosition");
		String labelNames_entry = Utilities.eliminateNull((String) request
				.getParameter("labelNames"));

		// A string cannot be converted to an int if it is empty.
		if (toolbarPosition_entry == null || toolbarPosition_entry.equals("")) {
			toolbarPosition_entry = "0";
		}

		ArrayList<String> labelNames = Utilities
				.getLabelNames(labelNames_entry);

		String errorMessage = null;
		if ((errorMessage = AddLink.checkLinkValid(linkURL_entry,
				linkName_entry, linkDescription_entry)) != null) {
			xml.constructNodeActionFailure(errorMessage);
			return;
		}

		// Set the link permission
		linkPermission_entry = AddLink.getLinkPermissions(user,
				linkPermission_entry);

		// Check URL for a protocol. If there is no protocol, assume its http
		// and tack it on the front.
		linkURL_entry = Utilities.addURLProtocol(linkURL_entry);

		try {
			db.editBookmark(user, linkID_entry, linkName_entry, linkURL_entry,
					linkPermission_entry, linkDescription_entry, labelNames,
					Integer.valueOf(toolbarPosition_entry));
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		xml.constructNodeActionSuccess();
	}

	/**
	 * Edit data of a folder.
	 * 
	 * @param request
	 *            Request received from client.
	 */
	private void editFolder(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {

		String folderID_entry_S = Utilities.eliminateNull((String) request
				.getParameter("folderID"));
		String folderName_entry = Utilities.eliminateNull((String) request
				.getParameter("folderName"));
		String toolbarPosition_entry = Utilities.eliminateNull((String) request
				.getParameter("toolbarPosition"));
		int folderID_entry = Integer.parseInt(folderID_entry_S);

		// A string cannot be converted to an int if it is empty.
		if (toolbarPosition_entry == null || toolbarPosition_entry.equals("")) {
			toolbarPosition_entry = "0";
		}

		String errorMessage = null;
		if ((errorMessage = Utilities.checkConstraint(AddLink.TITLE_CONSTRAINT,
				folderName_entry)) != null) {
			xml.constructNodeActionFailure(errorMessage);
			return;
		}

		try {
			db.editFolder(user, folderID_entry, folderName_entry, Integer
					.valueOf(toolbarPosition_entry));
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		xml.constructNodeActionSuccess();
	}

	/**
	 * Remove link.
	 * 
	 * @param request
	 *            Request received from client.
	 */
	private void deleteLink(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {
		String linkID_entry_S = Utilities.eliminateNull(request
				.getParameter("linkID"));
		int linkID_entry = Integer.parseInt(linkID_entry_S);

		try {
			db.deleteBookmark(user, linkID_entry);
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		xml.constructNodeActionSuccess();
	}

	/**
	 * Remove folder and all contents.
	 * 
	 * @param request
	 *            Request received from client.
	 */
	private void deleteFolder(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {

		String folderID_entry_S = Utilities.eliminateNull((String) request
				.getParameter("folderID"));
		int folderID_entry = Integer.parseInt(folderID_entry_S);

		try {

			if (folderID_entry == db.getInboxFolderID(user)) {
				xml.constructNodeActionFailure("Unable to delete Inbox.");
				return;
			} else if (!db.folderBelongsToClient(folderID_entry, user
					.getClientID())) {
				// is a shared folder.
				db.deleteFolderSubscription(user, folderID_entry);
			} else {
				db.deleteFolder(user, folderID_entry);
			}

		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		xml.constructNodeActionSuccess();
	}

	/**
	 * Add Link to hierarchy.
	 * 
	 * @param request
	 *            Request received from client.
	 */
	private void addLink(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {
		String linkName_entry = Utilities.eliminateNull((String) request
				.getParameter("linkName"));

		String linkURL_entry = Utilities.eliminateNull((String) request
				.getParameter("linkURL"));

		String linkPermission_entry = Utilities.eliminateNull((String) request
				.getParameter("linkPermission"));

		String linkDescription_entry = Utilities.eliminateNull((String) request
				.getParameter("linkDescription"));

		String toolbarPosition_entry = Utilities.eliminateNull((String) request
				.getParameter("toolbarPosition"));

		String labelNames_entry = Utilities.eliminateNull((String) request
				.getParameter("labelNames"));

		// This is a hack to ensure adding a link on the manage page will work.
		String firstAdd_entry = Utilities.eliminateNull((String) request
				.getParameter("firstAdd"));

		// this is added by Ross for support to adding to link to a file.
		String parentFolderID_entry_S = Utilities
				.eliminateNull((String) request.getParameter("parentFolderID"));

		// A string cannot be converted to an int if it is empty.
		if (toolbarPosition_entry == null || toolbarPosition_entry.equals("")) {
			toolbarPosition_entry = "0";
		}

		int parentFolderID_entry = Integer.parseInt(parentFolderID_entry_S);

		ArrayList<String> labelNames = Utilities
				.getLabelNames(labelNames_entry);

		String errorMessage = null;
		if (firstAdd_entry.equals("")
				&& (errorMessage = AddLink.checkLinkValid(linkURL_entry,
						linkName_entry, linkDescription_entry)) != null) {
			xml.constructNodeActionFailure(errorMessage);
			return;
		}

		// Set the link permission
		linkPermission_entry = AddLink.getLinkPermissions(user,
				linkPermission_entry);

		// Check URL for a protocol. If there is no protocol, assume its http
		// and tack it on the front.
		linkURL_entry = Utilities.addURLProtocol(linkURL_entry);

		Bookmark newBookmark = null;

		try {
			newBookmark = db.addBookmark(user, linkName_entry, linkURL_entry,
					linkPermission_entry, linkDescription_entry,
					parentFolderID_entry, labelNames, Integer
							.valueOf(toolbarPosition_entry));
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		xml.constructNodeAction(String.valueOf(newBookmark.getLinkID()), "");
	}

	/**
	 * Add Folder to hierarchy.
	 * 
	 * @param request
	 *            Request received from client.
	 */
	private void addFolder(HttpServletRequest request, ClientEntry user,
			BookmarkXML xml, DatabaseWrapper db) {

		String parentFolderID_entry_S = Utilities
				.eliminateNull((String) request.getParameter("parentFolderID"));
		String folderName_entry = Utilities.eliminateNull((String) request
				.getParameter("folderName"));
		String toolbarPosition_entry = Utilities.eliminateNull((String) request
				.getParameter("toolbarPosition"));
		int parentFolderID_entry = Integer.parseInt(parentFolderID_entry_S);

		// A string cannot be converted to an int if it is empty.
		if (toolbarPosition_entry == null || toolbarPosition_entry.equals("")) {
			toolbarPosition_entry = "0";
		}

		Folder newFolder = null;

		String errorMessage = null;
		if ((errorMessage = Utilities.checkConstraint(AddLink.TITLE_CONSTRAINT,
				folderName_entry)) != null) {
			xml.constructNodeActionFailure(errorMessage);
			return;
		}

		try {
			newFolder = db.addFolder(user, parentFolderID_entry,
					folderName_entry, Integer.valueOf(toolbarPosition_entry));
		} catch (Exception e) {
			NodeAction.xmlChipmarkException(e, xml);
			return;
		}

		xml.constructNodeAction(String.valueOf(newFolder.getFolderID()), "");
	}

	/**
	 * This will output a user-friendly error message if an unexpeetced
	 * exception is thrown
	 * 
	 * @param e
	 *            The exception
	 * @param xml
	 *            The bookmark xml
	 */
	public static void xmlChipmarkException(Exception e, BookmarkXML xml) {
		Utilities.ignoreChipmarkException(e);
		xml
				.constructNodeActionFailure("Chipmark encountered an unexpected exception");
	}

	/**
	 * Handles Post request from client.
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
