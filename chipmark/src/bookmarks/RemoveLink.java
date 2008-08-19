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
 * This servlet will remove links. It is saved in case
 * people are using old versions of the plugins.
 * @see DeleteLink
 * @author Joshua Fleck
 *
 */

public class RemoveLink extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1951932315365614287L;
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request,response);
	}
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException, IOException {
	
		Utilities.prepareRequest(request);
		
		String linkID_entry_S = Utilities.eliminateNull(request.getParameter("linkID"));
		String agent = Utilities.eliminateNull(request.getParameter("agent"));
		
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		
		ClientEntry user = Utilities.validateUser(request, db);
		
		BookmarkXML xml = new BookmarkXML();

		if(user == null) {
			
			if (agent.equals(Utilities.AGENT_EXT)) {
				Utilities.prepareResponseAgent(response);
				Utilities.reportInvalidUserAgent(xml);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.reportInvalidUserWeb(xml);
			}
			
			Utilities.returnResponse(response, xml);
			return;
		}		
		
		int linkID_entry = Integer.parseInt(linkID_entry_S);
	
		try {
			db.deleteBookmark(user,linkID_entry);
		}
		catch(Exception ex) {
			Utilities.wrapChipmarkException(ex);
		}		
		
		if(agent.equals(Utilities.AGENT_EXT)){
			
			Utilities.prepareResponseAgent(response);
			
			try {
				ArrayList<Bookmark> matches = db.getAllUserBookmarks(user);
				ArrayList<String> labels = db.getAllUserLabels(user);
				xml.constructViewLinks(matches, user.getClientName(), labels);
			} catch (Exception e){
				Utilities.wrapChipmarkException(e);
			}
			
			Utilities.returnResponse(response, xml);
			
		} else {
			
			response.sendRedirect("Main");
			
		}

	}
}
