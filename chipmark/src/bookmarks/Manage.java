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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Displays the manage interface.
 *
 * Refactored by Dominique DuCharme on 2/2/2007
 * @author Dominique DuCharme
 *
 */
public class Manage extends HttpServlet {

	/**
	 * A unique identifier for this version of the class.
	 */
	private static final long serialVersionUID = 5787665950498368307L;

	/**
	 * Handles Post requst from client.
	 *
	 * @param request
	 *            Request received from client.
	 * @param response
	 *            Response object received from client.
	 * @throws ServletException
	 *             If a servlet error occurs
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Utilities.prepareRequest(request);

		String agent = Utilities.eliminateNull(request.getParameter("agent"));

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		ClientEntry user = Utilities.validateUser(request, db);
		BookmarkXML xml = new BookmarkXML();

		int bookmarkCount = 0;

		if (user == null) {
			// user not logged in
			if (agent.equals(Utilities.AGENT_EXT)) {
				Utilities.prepareResponseAgent(response);
				Utilities.reportInvalidUserAgent(xml);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.reportInvalidUserWeb(xml);
			}
		} else {
			// User is logged in
			try {
				bookmarkCount = db.countUserBookmarks(user);
			} catch (Exception e) {
				Utilities.wrapChipmarkException(e);
			}

			xml.constructMainCount(user.getClientName(), bookmarkCount, null);

			if (agent.equals("count")) {
				// request for the count
				Utilities.prepareResponseAgent(response);
			} else {
				// not a request for the count
				try {
					Utilities.styleXML(xml, "manage", user);
				} catch (Exception e) {
					Utilities.wrapChipmarkException(e);
				}

				Utilities.prepareResponseWeb(response);
			}

		}

		Utilities.returnResponse(response, xml);
		return;
	}
}
