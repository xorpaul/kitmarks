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

public class Random extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7476949160916652948L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Utilities.prepareRequest(request);

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		BookmarkXML xml = new BookmarkXML();

		ClientEntry user = Utilities.validateUser(request, db);

		try {
			LinkNameUrlObject random = db.getRandom();
			
			int count = 10;
			while(count > 0 && !Utilities.isSafeUrl(random.getLinkURL(), user)) { random = db.getRandom(); count--; }
			
			if(!Utilities.isSafeUrl(random.getLinkURL(), user)) {
				random = new LinkNameUrlObject("chipmark","https://www.chipmark.com");
			}

			xml.constructRandom(user == null ? "" : user.getClientName(), random);

			Utilities.prepareResponseAgent(response);
    
			Utilities.returnResponse(response, xml);
		}

		catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}
	}
}
