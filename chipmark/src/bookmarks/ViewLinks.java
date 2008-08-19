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

/**
 * This servlet is responsible for displaying the View Links page.
 * @author Joshua Fleck
 *
 */
public class ViewLinks extends HttpServlet {

	/**
	 * A unique number identifying this version of the class
	 */
	private static final long serialVersionUID = -1451319008078883257L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		Utilities.prepareRequest(request);

		String agent = Utilities.eliminateNull((String) request.getParameter("agent"));	
		String linkName_entry = Utilities.eliminateNull((String) request.getParameter("linkName"));
		String linkURL_entry = Utilities.eliminateNull((String) request.getParameter("linkURL"));
		String linkPermission_entry = Utilities.eliminateNull((String) request.getParameter("linkPermission"));
		String linkDescription_entry = Utilities.eliminateNull((String) request.getParameter("linkDescription"));
		String labelNames_entry = Utilities.eliminateNull((String) request.getParameter("labelNames"));

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		ClientEntry user = Utilities.validateUser(request, db);

		BookmarkXML xml = new BookmarkXML();

		if(user == null) {// The user authentication failed
			
			if (agent.equals(Utilities.AGENT_EXT)) {
				Utilities.prepareResponseAgent(response);
				Utilities.reportInvalidUserAgent(xml);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.reportInvalidUserWeb(xml);
			}
			
		} else {// Return the bookmarks

			ArrayList<String> labelNames = Utilities.getLabelNames(labelNames_entry);

			ArrayList<Bookmark> matches = null;
			ArrayList<String> labels = null;
			try {
				matches = db.searchUserBookmarks(user, linkName_entry, linkURL_entry, linkPermission_entry, linkDescription_entry, labelNames);
				labels = db.getAllUserLabels(user);
			} catch (Exception e) {
				Utilities.wrapChipmarkException(e);
			} 

			xml.constructViewLinks(matches, user.getClientName(), labels);

			if(agent.equals(Utilities.AGENT_EXT)){
				Utilities.prepareResponseAgent(response);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.styleXML(xml, "viewlinks", user);			
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

}
