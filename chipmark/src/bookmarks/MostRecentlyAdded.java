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

public class MostRecentlyAdded extends HttpServlet {

	private static final long serialVersionUID = 3912304285251785439L;

	private static final int DISPLAY_COUNT = 50;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Utilities.prepareRequest(request);

		boolean agent = Utilities.eliminateNull((String) request.getParameter("agent")).equals(Utilities.AGENT_EXT);

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		BookmarkXML xml = new BookmarkXML();

		ClientEntry user = Utilities.validateUser(request, db);

		try {
			ArrayList<LinkNameUrlObject> bookmarkArray = db.getMostRecentlyAdded(DISPLAY_COUNT);
			
			Utilities.cleanseUrlList(bookmarkArray, user, !agent);

			xml.constructMostRecentlyAdded(user == null ? "" : user.getClientName(), bookmarkArray);

			if (agent) {
				Utilities.prepareResponseAgent(response);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.styleXML(xml, "mostrecentlyadded", user);
			}
			Utilities.returnResponse(response, xml);
		}

		catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}
	}
}
