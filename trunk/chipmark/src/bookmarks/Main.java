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

public class Main extends HttpServlet {
	/**
	 * Unique Serial Number of Servlet
	 */
	private static final long serialVersionUID = 139485729485L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Utilities.prepareRequest(request);

		String target = Utilities.eliminateNull((String) request
				.getParameter("target"));
		String agent = Utilities.eliminateNull((String) request
				.getParameter("agent"));

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper(); // get db
		ClientEntry user = Utilities.validateUser(request, db); // is there a
		// user?
		BookmarkXML xml = new BookmarkXML(); // prep xml response

		if (user != null && user.getClientID() != 0
				&& !user.getClientName().equals("SessionDummy")
				&& !user.getClientName().equals("SessionDummyChanged")) {
			xml.constructMain(user.getClientName(), target); // greet particular
			// client
		} else if (user == null && target.equals("pluginManage")) {
			xml.constructFacebookLogin("");
		} else {
			xml.constructMain("", target); // greet general

		}

		if (agent.equals(Utilities.AGENT_EXT)) {
			Utilities.prepareResponseAgent(response); // prep respose to a
			// plugin
		} else {
			Utilities.prepareResponseWeb(response); // prep response to web
			Utilities.styleXML(xml, user); // style the output
		}
		Utilities.returnResponse(response, xml); // send response
	}
}
