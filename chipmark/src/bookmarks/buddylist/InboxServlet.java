/*
 -------------------------------------------------------------------------------------------------
 Copyright, 2005, Chipmark.

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
package bookmarks.buddylist;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookmarks.BookmarkXML;
import bookmarks.ClientEntry;
import bookmarks.DatabaseWrapper;
import bookmarks.Utilities;
/**
 *This servlet handles requests send links to other users' inboxes.
 *@author Zach Snow
 */

public class InboxServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 436379932332909976L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException, IOException{		
		doGet(request,response);		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException, IOException {

		Utilities.prepareRequest(request);

		String agent = Utilities.eliminateNull((String) request.getParameter("agent"));

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		ClientEntry user = Utilities.validateUser(request, db);

		BookmarkXML xml = new BookmarkXML();

		if(user == null) {//Ensure the user is valid and logged in

			if (agent.equals(Utilities.AGENT_EXT)) {
				Utilities.prepareResponseAgent(response);
				Utilities.reportInvalidUserAgent(xml);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.reportInvalidUserWeb(xml);
			}


		} else {

			Utilities.prepareResponseAgent(response);
			
			String action = request.getParameter("action");

			if (action != null){
				
				//Get users to send to:
				String strSendToList = Utilities.eliminateNull(request.getParameter("name"));

				//Get link information
				String strLinkName = Utilities.eliminateNull(request.getParameter("linkname"));
				String strLinkURL = Utilities.eliminateNull(request.getParameter("linkurl"));
				String strLinkDesc = Utilities.eliminateNull(request.getParameter("linkdesc"));

				//Construct a list of users to send to:
				String[] sendTo = strSendToList.split(":");
				
				String strError = null;
				
//				Make sure a username was specified:
				if(sendTo.length == 0){
					strError = "Unable to send link to user: no users specified.";
				}

				try {
					
					for(int i = 0; i < sendTo.length; i++){
						
						String strSendTo = sendTo[i];

						//Get the specified user's client entry:
						ClientEntry client = db.getUserByName(strSendTo);
						boolean isValidBuddy = db.validateBuddy(user, strSendTo);
						if(client == null){
							strError = "Unable to send link to user: user '" + strSendTo + "' does not exist.\n";
						} 
						else if(!isValidBuddy){
							strError = "Unable to send link to user: "+strSendTo+" is not your buddy.\n";
						}
						else if(db.countUserBookmarks(client) >= DatabaseWrapper.MAXCHIPMARKS){
							//Make sure there is room in the client's database:
							strError = "Unable to send link to user '" + strSendTo + "': user's inbox is full.\n";
						}
						else if(!db.sendLinkToUser(user, client, strLinkName, strLinkURL, strLinkDesc)){
							//Attempt to send the link to the user:
							strError = "Unable to send link to user '" + strSendTo + "'.\n";
						}
					}
					
				} catch (Exception e) {
					Utilities.wrapChipmarkException(e);
				}

				//Check for errors:
				if(strError == null){
					xml.constructGenericSuccess();
				}
				else{
					xml.constructGenericFailure(strError);
				}

			} else {
				xml.constructGenericFailure("error- no action specified");
			}

		}
		
		Utilities.returnResponse(response, xml);

	}
}
