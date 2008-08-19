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

public class SearchLinks extends HttpServlet {

	private static final long serialVersionUID = -8803904778922287951L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Utilities.prepareRequest(request);
		Utilities.prepareResponseWeb(response);

		String action = null;
		String searchString = Utilities.eliminateNull(request
				.getParameter("search"));
		// String searchAllUserLinks =
		// Utilities.eliminateNull(request.getParameter("searchAllUserLinks"));
		String searchAllUserLinks = (String) request
				.getParameter("searchAllUserLinks");

		searchString = searchString.trim();

		if (searchString.length() == 0) {
			response.sendRedirect("Main");
			return;
		}

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		try {

			ArrayList<Bookmark> results = null;
			ClientEntry user = Utilities.validateUser(request, db);
			BookmarkXML xml = new BookmarkXML();
			String userName = Utilities.getUsername(request, db);

			if (searchAllUserLinks != null) {
				action = "searchAllUserLinks";

				results = db.searchLinks(searchString, action, user
						.getClientID());

				xml.constructSearchUser(results, user.getClientName(),
						searchString);

				Utilities.styleXML(xml, "searchuser", user);

			} else {
				action = "searchAllPublic";
				results = db.searchLinks(searchString, action);

				xml.constructSearchLinks(userName, results, searchString);

				Utilities.styleXML(xml, "searchlinks", user);
			}

			Utilities.returnResponse(response, xml);

		} catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}
	}
}
