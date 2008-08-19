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
 * This servlet is used by the manage page to handle the rearranging of the
 * chipmarks by the user.
 *
 * Refactored by Dominique DuCharme on 2/3/2007
 * @author Dominique DuCharme
 *
 */
public class UpdateBookmarkOrder extends HttpServlet {

	/**
	 * A unique identifier for this version of the class.
	 */
	private static final long serialVersionUID = 1203392016940996530L;


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Utilities.prepareRequest(request);

		String agent = Utilities.eliminateNull(request.getParameter("agent"));
		String formatString = Utilities.eliminateNull(request.getParameter("sortString"));
				
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		ClientEntry user = Utilities.validateUser(request, db);
		BookmarkXML xml = new BookmarkXML();

		boolean externalAgent = agent.equals(Utilities.AGENT_EXT);

		if (user == null) {
			// user not logged in
			if (externalAgent) {
				Utilities.prepareResponseAgent(response);
				Utilities.reportInvalidUserAgent(xml);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.reportInvalidUserWeb(xml);
			}
			Utilities.returnResponse(response, xml);
		} else {
			if(formatString == "") {
				xml.constructNodeActionFailure("Sort string not present!"); 
				Utilities.returnResponse(response, xml); 
				return;
			}
			int[] bookmarkID = processSortOrderString(formatString);

			try {
				if(bookmarkID != null && bookmarkID.length > 0) {
					db.setSortOrder(user, bookmarkID);
				}
				xml.constructNodeActionSuccess();
				Utilities.returnResponse(response, xml);
			} catch (Exception ex) {
				Utilities.wrapChipmarkException(ex);
			}
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 * @throws IOException
	 */
	private int[] processSortOrderString(String formatString) {
	
			
		String[] sortedIDList = formatString.split("\\|");
		
		int bookmarkID[] = new int[sortedIDList.length];
		
		for(int i = 0; i < sortedIDList.length; i++) {
			bookmarkID[i] = Integer.parseInt(sortedIDList[i]);
		}
		
		return bookmarkID;
	}
}
