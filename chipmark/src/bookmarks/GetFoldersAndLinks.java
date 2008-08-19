/*
 -------------------------------------------------------------------------------------------------
 Copyright 2004-2007 Chipmark.

 This File Is Part Of Chipmark.

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

public class GetFoldersAndLinks extends HttpServlet {

	private static final long serialVersionUID = 7049131929012911576L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Utilities.prepareRequest(request);
		Utilities.prepareResponseAgent(response);
		
		String fromMyBuddy = Utilities.eliminateNull((String) request.getParameter("fromMyBuddy"));
		
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		ClientEntry user = Utilities.validateUser(request, db);

		BookmarkXML xml = new BookmarkXML();
		
		ArrayList<Bookmark> links = null;
		ArrayList<Folder> folders = null;
		ArrayList<String> labels = null;
		
		ClientEntry userToGet = null;
		boolean valid = false;
		try {
			valid = db.authCheck(user);

			if (!valid) {
				xml.constructNodeActionFailure("Not logged in or bad password.");
			} else if(fromMyBuddy.equals("")) {
				userToGet = user;
				folders = db.getUserFolders(userToGet); // all folders
				links = db.getAllUserBookmarks(userToGet); // all links

			} else if(db.validateBuddy(user, fromMyBuddy)){
				userToGet = db.getUserByName(fromMyBuddy);
				folders = db.getUserNonEmptyFoldersAndRoot(userToGet); // only non empty folders
				links = db.getAllUserPublicBookmarks(userToGet); // only public
			} else if(!db.validateBuddy(user, fromMyBuddy)){
				xml.constructNodeActionFailure("The user "+ fromMyBuddy+" is not your buddy.");
			}
			if(userToGet != null) {
				// set shared folder's parent folder ID to the root folder.
				int rootFolderID = db.getRootFolderID(userToGet);
				for(int i = 0; i < folders.size(); i++) {
					if(folders.get(i).getFolderType().equals("shared")) {
						folders.get(i).setFolderParentID(rootFolderID);
					}
				}
				
				labels = db.getAllUserLabels(userToGet);
				xml.constructFoldersAndLinks(links, folders, labels);
			}
			Utilities.returnResponse(response, xml);
		} catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}

	}

}
