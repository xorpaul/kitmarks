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
 * Servlet to search through a users personal bookmarks.
 */
public class SearchUser extends HttpServlet {

	private static final long serialVersionUID = -4532325034785011332L;

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
		Utilities.prepareResponseWeb(response);

		String searchString = Utilities.eliminateNull(request.getParameter("search"));
 
		searchString = searchString.trim();

		if (searchString.length() == 0) {
			response.sendRedirect("Main");
			return;
		}

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		ClientEntry user = Utilities.validateUser(request, db);

		BookmarkXML xml = new BookmarkXML();

		boolean valid = false;

		try {
			valid = db.authCheck(user);
		} catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}

		if (!valid) {
			Utilities.reportInvalidUserWeb(xml);
		} else {

			ArrayList<Bookmark> results = null;
			try {

				results = db.searchLinks(searchString, "searchAllUserLinks",user.getClientID());

				xml.constructSearchUser(results, user.getClientName(), searchString);
				
				Utilities.styleXML(xml, "searchuser", user);

				Utilities.returnResponse(response, xml);

			} catch (Exception e) {
				Utilities.wrapChipmarkException(e);
			}
		}

	}
}
