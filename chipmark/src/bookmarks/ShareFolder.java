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
 * 
 * @author Brian Terlson
 *
 */

public class ShareFolder extends HttpServlet {
	
	private static final long serialVersionUID = 318879161529289501L;
	
	/**
	 * The error message shown if a user is not logged in.
	 */
	private static final String INVALID_USER_MSG = "You must register or login before managing your account.";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		Utilities.prepareRequest(request);

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		ClientEntry user = Utilities.validateUser(request, db);
		BookmarkXML xml = new BookmarkXML();

		String agent = Utilities.eliminateNull(request.getParameter("agent"));
		boolean externalAgent = agent.equals(Utilities.AGENT_EXT);
		if (user == null) {
			// user not logged in
			if (externalAgent) {
				Utilities.prepareResponseAgent(response);
				xml.constructNodeActionFailure(INVALID_USER_MSG);
			} else {
				Utilities.prepareResponseWeb(response);
				xml.constructWithResultMsg("", INVALID_USER_MSG);
				Utilities.styleXML(xml, user);
			}
		}
		else {
			
			String action = Utilities.eliminateNull(request.getParameter("action"));
			try {
				if(action.equals("share")) {
					// update specific folder's public attribute to true.
					int folderID = Integer.parseInt(Utilities.eliminateNull(request.getParameter("folderid")));
					db.editFolderPublicity(user, folderID, true);
					xml.constructNodeAction("SUCCESS", "Folder shared successfully.");
				}
				else if(action.equals("unshare")) {
					// update specific folder's public attribute to false.
					int folderID = Integer.parseInt(Utilities.eliminateNull(request.getParameter("folderid")));
					db.editFolderPublicity(user, folderID, false);
					xml.constructNodeAction("SUCCESS", "Folder unshared successfully.");
				}
				else if(action.equals("invite")) {
					// invite users to subscribe to specified folder.
					int folderID = Integer.parseInt(Utilities.eliminateNull(request.getParameter("folderid")));
					if(folderID == db.getRootFolderID(user)) {
						xml.constructNodeAction("FAILURE", "Cannot share root folder.");
					}
					else {
						String[] usernames = request.getParameterValues("username");
						ClientEntry invitedUser = null;
						for(int i = 0; i<usernames.length; i++) {
							// Could check to make sure each operation is completed successfully, otherwise
							// return message containing which usernames failed, but this will likely only
							// happen if someone is messing with the servelets.  It'd be advisable to change
							// this behavior in the future, however the end of the semester looms.
							invitedUser = db.getUserByName(usernames[i]);
							// can't invite self.
							if(invitedUser.getClientID() != user.getClientID() && db.validateBuddy(user, usernames[i]))
								db.createFolderSubscriptionInvite(invitedUser, user, folderID);
						}
						xml.constructNodeAction("SUCCESS", "Invitees Invited.");
					}
				}
				else if(action.equals("accept")) {
					// accepts a pending folder_subcription
					int folderID = Integer.parseInt(Utilities.eliminateNull(request.getParameter("folderid")));
					db.acceptFolderSubscription(user, folderID);
					xml.constructNodeAction("SUCCESS", "Folder subscription invite accepted.");
				}
				else if(action.equals("reject")) {
					// rejects a pending folder subscription
					int folderID = Integer.parseInt(Utilities.eliminateNull(request.getParameter("folderid")));
					db.deleteFolderSubscription(user, folderID);
					xml.constructNodeAction("SUCCESS", "Folder subscription invite rejected.");
				}
				else if(action.equals("revoke")) {
					// revokes an invite to a folder.  Will work whether the invite is accepted or not.
					int folderID = Integer.parseInt(Utilities.eliminateNull(request.getParameter("folderid")));
					String[] usernames = request.getParameterValues("username");
					if(db.folderBelongsToClient(folderID, user.getClientID())){
						for(int i = 0; i<usernames.length; i++) {
							ClientEntry invitee = db.getUserByName(usernames[i]);
							db.deleteFolderSubscription(invitee, folderID);
						}
					} else {
						throw new IllegalAccessException();
					}
					xml.constructNodeAction("SUCCESS", "Folder subscription invite revoked.");
				}
				else if(action.equals("listinvites") || action.equals("login")) {
					// lists what invites you currently have to others' shared folders.
					ArrayList<SharedFolder> folders = db.getFolderInvites(user);
					xml.constructSharedFolderInvites(folders);
				}
				else {
					xml.constructWithResultMsg("", "Invalid action.");
				}
			}
			catch(Exception e){
				xml.constructWithResultMsg("FAILURE", "An error occurred.");
				Utilities.ignoreChipmarkException(e);
			}
			Utilities.prepareResponseAgent(response);
		}
		
		Utilities.returnResponse(response, xml);
	}
}