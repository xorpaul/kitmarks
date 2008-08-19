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
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookmarks.constraints.StringConstraint;

/**
 * This servlet is responsible for adding new bookmarks to the database.
 * 
 * @author Joshua Fleck
 *
 */
public class AddLink extends HttpServlet {

	/**
	 * A unique identifier for this version of the class.
	 */
	private static final long serialVersionUID = -5186342270839884042L;

	/**
	 * The page to redirect to on a successfull add.
	 */
	public static final String VIEW_LINKS_REDIRECT = "Manage";

	/**
	 * Constraint used for titles
	 */
	public static final StringConstraint TITLE_CONSTRAINT =
		new StringConstraint("Name", false, 1, 256);

	/**
	 * Constraint used for urls
	 */
	public static final StringConstraint URL_CONSTRAINT =
		new StringConstraint("URL", false, 3, 1024);

	/**
	 * Constraint used for desrciptions
	 */
	public static final StringConstraint DESCRIPTION_CONSTRAINT = 
		new StringConstraint("Link description", true, 0, 512);

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Utilities.prepareRequest(request);
		
		String submitBtn = Utilities.eliminateNull((String) request.getParameter("submitBtn"));
		String linkName_entry = Utilities.eliminateNull((String) request.getParameter("linkName"));
		String linkURL_entry = Utilities.eliminateNull((String) request.getParameter("linkURL"));
		String linkPermission_entry = Utilities.eliminateNull((String) request.getParameter("linkPermission"));
		String linkDescription_entry = Utilities.eliminateNull((String) request.getParameter("linkDescription"));
		String labelNames_entry = Utilities.eliminateNull((String) request.getParameter("labelNames"));
		String agent = Utilities.eliminateNull((String) request.getParameter("agent"));
		String toolbarPosition_entry = Utilities.eliminateNull((String) request.getParameter("toolbarPosition"));

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		ClientEntry user = Utilities.validateUser(request, db);

		// A string cannot be converted to an int if it is empty.
		if (toolbarPosition_entry == null || toolbarPosition_entry.equals("")) { toolbarPosition_entry = "0"; }
		
		BookmarkXML xml = new BookmarkXML();
		
		String errorMessage = null;

		if(user == null) {
			
			if (agent.equals(Utilities.AGENT_EXT)) {
				Utilities.prepareResponseAgent(response);
				Utilities.reportInvalidUserAgent(xml);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.reportInvalidUserWeb(xml);
			}

		// Display the Add Bookmark page
		} else if(submitBtn.equals("") && !agent.equals(Utilities.AGENT_EXT)) {

			xml.constructAddLink(user.getClientName(),null);
			Utilities.prepareResponseWeb(response);
			Utilities.styleXML(xml, "addlink", user);

		// Display the Add Bookmark page with fields inserted
		} else if(submitBtn.equals("prefilled") && !agent.equals(Utilities.AGENT_EXT)) {
			xml.constructAddLinkResult(user.getClientName(), linkName_entry, linkURL_entry, linkPermission_entry, linkDescription_entry, labelNames_entry, toolbarPosition_entry, "");
			Utilities.prepareResponseWeb(response);
			Utilities.styleXML(xml, "addlink", user);

		} else if ((errorMessage = checkLinkValid(linkURL_entry, linkName_entry, linkDescription_entry)) != null){
			
			if(!agent.equals(Utilities.AGENT_EXT)) {			
				xml.constructAddLinkResult(user.getClientName(), linkName_entry, linkURL_entry, linkPermission_entry, linkDescription_entry, labelNames_entry, toolbarPosition_entry, errorMessage);
				Utilities.prepareResponseWeb(response);
				Utilities.styleXML(xml, "addlink", user);
			} else {
				xml.constructGenericFailure(errorMessage);
				Utilities.prepareResponseAgent(response);
			}

		} else {

			// Set the link permission
			linkPermission_entry = getLinkPermissions(user, linkPermission_entry);

			// Check URL for a protocol.  If there is no protocol, assume its http and tack it on the front.
			linkURL_entry = Utilities.addURLProtocol(linkURL_entry);

			ArrayList<String> labelNames = Utilities.getLabelNames(labelNames_entry);

			try {

				db.addBookmark(user,linkName_entry,linkURL_entry,linkPermission_entry,linkDescription_entry,labelNames, Integer.valueOf(toolbarPosition_entry));

			} catch (Exception e) {
				Utilities.wrapChipmarkException(e);
			} 

			if(agent.equals(Utilities.AGENT_EXT)) {
				Utilities.prepareResponseAgent(response);
				try {
					returnAgentBookmark(xml, db, user);
				} catch (Exception e) {
					Utilities.wrapChipmarkException(e);
				}

			} else {
				// Redirect to label view on success
				response.sendRedirect(VIEW_LINKS_REDIRECT);
				
				return;
			}

		}
		
		Utilities.returnResponse(response, xml);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		this.doGet(arg0, arg1);
	}

	/**
	 * Returns the proper text to the plug-in for a successful add/edit.
	 * @param xml A BookmarkXML object (Not null)
	 * @param db A DatabaseWrapper object (Not null)
	 * @param user A ClientEntry object (Not null)
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws SQLException 
	 */
	public static void returnAgentBookmark(BookmarkXML xml, DatabaseWrapper db, ClientEntry user) throws SQLException, NoSuchFieldException, IllegalAccessException {

		ArrayList<Bookmark> matches = null;
		ArrayList<String> labels = null;
		
		matches = db.getAllUserBookmarks(user);
		labels = db.getAllUserLabels(user);

		xml.constructViewLinks(matches, user.getClientName(), labels);

	}

	/**
	 * Ensures the bookmark url has a valid URL, Name, and Description.
	 * @param linkURL_entry The link name (Not null)
	 * @param linkName_entry The link URL (Not null)
	 * @param linkDescription_entry The link description
	 * @return An error message, if the link was invalid. Null, otherwise.
	 */
	public static String checkLinkValid(String linkURL_entry, String linkName_entry, String linkDescription_entry) {

		String errorResult = null;

		if((linkURL_entry.equals("") || linkURL_entry.trim().equalsIgnoreCase("http://")
				|| linkURL_entry.trim().equalsIgnoreCase("https://")) && linkName_entry.equals("")) {

			errorResult = "You must fill in the Name and URL field.";

		} else if(linkURL_entry.equals("") || linkURL_entry.trim().equalsIgnoreCase("http://")
				|| linkURL_entry.trim().equalsIgnoreCase("https://")) {

			errorResult = "You must fill in the URL field.";

		} else {

			errorResult = AddLink.checkLinkConstraints(linkDescription_entry, linkURL_entry, linkName_entry);

		}
		
		return errorResult;
	}
	
	/**
	 * Locates the default permissions for the link.
	 * @param user A ClientEntry object (Not null)
	 * @param linkPermission_entry The link permissions (Not null)
	 * @return The proper link permissions.
	 */
	public static String getLinkPermissions(ClientEntry user, String linkPermission_entry) {
		
		// Set the link permission
		if(!linkPermission_entry.equals("public") && !linkPermission_entry.equals("private")) {

			linkPermission_entry = user.prefs.getDefaultPermission();

		}
		
		return linkPermission_entry;
		
	}

	/**
	 * This method will check the string constraints and return a message if any constraints fail.
	 * Null will be return if no constraints fail.
	 * 
	 * @param linkDescription
	 * @param linkURL
	 * @param linkName
	 * @return An error message string or null
	 */
	public static String checkLinkConstraints(String linkDescription, String linkURL, String linkName) {
		
		String errorMessage = null;
		
		if((errorMessage = Utilities.checkConstraint(DESCRIPTION_CONSTRAINT, linkDescription)) != null) {
			return errorMessage;
		}
		
		if((errorMessage = Utilities.checkConstraint(URL_CONSTRAINT, linkURL)) != null) {
			return errorMessage;
		}
		
		if((errorMessage = Utilities.checkConstraint(TITLE_CONSTRAINT, linkName)) != null) {
			return errorMessage;
		}
				
		return null;
	}
	

}
