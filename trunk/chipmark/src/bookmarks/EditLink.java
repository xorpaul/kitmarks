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
 * This servlet is responsible for editing existing bookmarks in the database.
 * @author Joshua Fleck
 *
 */
public class EditLink extends HttpServlet {

	/**
	 * A unique identifier for this version of the class
	 */
	private static final long serialVersionUID = 6941893728506744132L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		Utilities.prepareRequest(request);

		String submitBtn = Utilities.eliminateNull((String) request.getParameter("submitBtn"));
		String linkID_entry_S = Utilities.eliminateNull((String) request.getParameter("linkID"));
		String agent = Utilities.eliminateNull((String) request.getParameter("agent"));
		String linkName_entry = Utilities.eliminateNull((String) request.getParameter("linkName"));
		String linkURL_entry = Utilities.eliminateNull((String) request.getParameter("linkURL"));
		String linkPermission_entry = Utilities.eliminateNull((String) request.getParameter("linkPermission"));
		String linkDescription_entry = Utilities.eliminateNull((String) request.getParameter("linkDescription"));
		String labelNames_entry = Utilities.eliminateNull((String) request.getParameter("labelNames"));
		String toolbarPositon_entry = Utilities.eliminateNull((String) request.getParameter("toolbarPosition"));
		
		int linkID_entry = !"".equals(linkID_entry_S) ? Integer.parseInt(linkID_entry_S) : -1;

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

		} else if(submitBtn.equals("")){//Display the Edit Link page with fields populated

			Bookmark bookmark = null;
			
			try {
				bookmark = db.getUserBookmark(user, linkID_entry);
			} catch (Exception e) {
				Utilities.wrapChipmarkException(e);
			} 

			xml.constructEditLink(bookmark, user.getClientName());
			Utilities.styleXML(xml, "editlink", user);
			Utilities.prepareResponseWeb(response);

		} else if(submitBtn.equals("save chipmark") || submitBtn.equals("save_chipmark")){

			ArrayList<String> labelNames = Utilities.getLabelNames(labelNames_entry);
			
			String errorMessage = null;
			if ((errorMessage = AddLink.checkLinkValid(linkURL_entry, linkName_entry, linkDescription_entry)) != null)
			{
				// Link was invalid; output error
				if(!agent.equals(Utilities.AGENT_EXT)) {
					xml.constructEditLinkResult(user.getClientName(), linkID_entry_S, linkName_entry, linkURL_entry, linkPermission_entry, linkDescription_entry, labelNames, toolbarPositon_entry, errorMessage);
			Utilities.styleXML(xml, "editlink", user);
					Utilities.prepareResponseWeb(response);	
				} else {
					xml.constructGenericFailure(errorMessage);
					Utilities.prepareResponseAgent(response);
				}


			} else {//Link edit will be persisted

				// Set the link permission
				linkPermission_entry = AddLink.getLinkPermissions(user, linkPermission_entry);

				// Check URL for a protocol.  If there is no protocol, assume its http and tack it on the front.
				linkURL_entry = Utilities.addURLProtocol(linkURL_entry);

				try {
					db.editBookmark(user,linkID_entry,linkName_entry,linkURL_entry,linkPermission_entry,linkDescription_entry,labelNames, Integer.parseInt(toolbarPositon_entry));
				} catch (Exception e) {
					Utilities.wrapChipmarkException(e);
				} 

				if(agent.equals(Utilities.AGENT_EXT)){
					Utilities.prepareResponseAgent(response);					
					try {						
						AddLink.returnAgentBookmark(xml, db, user);						
					} catch (Exception e) {
						Utilities.wrapChipmarkException(e);
					} 

				} else {
					response.sendRedirect(AddLink.VIEW_LINKS_REDIRECT);
					return;
				}
			}
		}

		Utilities.returnResponse(response, xml);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}


}
