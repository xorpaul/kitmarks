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

public class DeleteLink extends HttpServlet {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -8239935653034159883L;
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request,response);
	}
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException, IOException {
        Utilities.prepareRequest(request);

		String delete = Utilities.eliminateNull(request.getParameter("delete"));
		// if delete = "set", then delete a set of id's
		// if delete = "all", then delete everything, and linkID's is never used.
		// if delete = "", then allow for grandfather compatibility, and just set it to "set"
		String[] linkIDs = request.getParameterValues("linkID");
		int[] intLinkIDs = null;
		String agent = Utilities.eliminateNull(request.getParameter("agent"));
		//System.err.println("agent = "+agent+"\n");

		if(delete.equals("set") && linkIDs != null){ // deleting a set of chipmarks, denoted by the ID's in linkID
			intLinkIDs = new int[linkIDs.length];
			for(int i=0; i<intLinkIDs.length; i++){
				try{
					//System.err.println("linkIDs["+i+"] = "+linkIDs[i]+"");
					intLinkIDs[i] = Integer.parseInt(linkIDs[i]);
					//System.err.println("intLinkIDs["+i+"] = "+intLinkIDs[i]+"\n");
				} catch(Exception ex){
					//throw new IOException("You gave me something thats not an int");
					intLinkIDs[i] = -1; // best effort algorithm,
									    // if there's a way to get it done,
										// then I try to find that way.
				}
			}
		}
		
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		
		BookmarkXML xml = new BookmarkXML();
		
		ClientEntry user = Utilities.validateUser(request, db);

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
		
		try {
			if((delete.equals("set") || delete.equals("")) && intLinkIDs != null){ // deleting a set of chipmarks, denoted by the ID's in linkID
				for(int i=0; i<intLinkIDs.length; i++){
					if(intLinkIDs[i] >= 0){
						//System.err.println("Deleting the link "+intLinkIDs[i]+"...\n");
						db.deleteBookmark(user,intLinkIDs[i]);
						//System.err.println("Deleted the link "+intLinkIDs[i]+"...\n");
					} else {
						//then linkIDs[i] was an invalid integer
						//WARNING, this WILL allow the users to delete the inbox!
					}
				}
			} else if(delete.equals("all")) {
				// then delete everything
				//System.err.println("Deleting all the links...\n");
				db.deleteAllBookmarks(user);
				db.deleteAllFolderSubscriptions(user);
				//System.err.println("Deleted all the links...\n");
			} else {
				if(agent.equals(Utilities.AGENT_EXT)){
					xml.constructNodeActionFailure("Improper usage, DeleteLink's delete parameter must be \"set\" \"all\" or \"\"");
					Utilities.prepareResponseAgent(response);
				} else {
					Utilities.prepareResponseWeb(response);
					xml.constructGenericFailure("Improper usage, DeleteLink's delete parameter must be \"set\" \"all\" or \"\"");
				}
				Utilities.returnResponse(response, xml);
				return;
			}
		}
		catch(Exception e) {
			if(agent.equals(Utilities.AGENT_EXT)){
				xml.constructNodeActionFailure("");
				Utilities.prepareResponseAgent(response);
			} else {
				Utilities.prepareResponseWeb(response);
				xml.constructGenericFailure("");
			}
			Utilities.wrapChipmarkException(e);
		}

		if(agent.equals(Utilities.AGENT_EXT)){
			Utilities.prepareResponseAgent(response);
			xml.constructNodeActionSuccess();
			Utilities.returnResponse(response, xml);
		} else {
			response.sendRedirect("Manage");
		}
	}
}
