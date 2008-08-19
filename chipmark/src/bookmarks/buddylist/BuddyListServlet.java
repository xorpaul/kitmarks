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

package bookmarks.buddylist;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import bookmarks.BookmarkXML;
import bookmarks.ClientEntry;
import bookmarks.DatabaseWrapper;
import bookmarks.Utilities;
import bookmarks.constraints.ConstraintException;
import bookmarks.constraints.StringConstraint;

/**
 *This servlet handles requests to edit a user's buddy list
 *@author Carmen Wick 
 */

public class BuddyListServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1178254185526300466L;

	/**
	 * 
	 */
	public static final StringConstraint NICKNAME_CONSTRAINT = 
		new StringConstraint("nickname", true, 0, 64);

	/**
	 * 
	 */
	public static final StringConstraint USERNAME_CONSTRAINT = 
		bookmarks.AddClient.USERNAME_CONSTRAINT;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
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

		HttpSession session = request.getSession();
		ClientEntry userInfo = (ClientEntry)session.getAttribute("AuthStamp");
		String action = (String) request.getParameter("action");
		String agent = Utilities.eliminateNull( (String) request.getParameter("agent"));
		BookmarkXML xml = new BookmarkXML();
		String nameEntry = Utilities.trimString((String) request.getParameter("name"));
		String nickEntry = Utilities.trimString((String) request.getParameter("nick"));

		final String AUTH_FAILURE = "You must register or log in before managing"
			+ " your buddies.";

		Utilities.prepareResponseAgent(response);


		if (userInfo == null){
			if (agent.equals(Utilities.AGENT_EXT)){
				xml.constructNodeAction("FAILURE", AUTH_FAILURE);
			}
			else {
				Utilities.prepareResponseWeb(response);
				xml.constructWithResultMsg("", AUTH_FAILURE);
				Utilities.styleXML(xml, "error", userInfo);				
			}

			response.getWriter().println(xml.toString());
			return;
		}

		//If no action specified, construct buddies management page
		if (action == null){
			Utilities.prepareResponseWeb(response);

			if (agent.equals(Utilities.AGENT_EXT)){
				return;
			}

			xml.constructMain(userInfo.getClientName(), "manageBuddies");			
			Utilities.styleXML(xml, userInfo);

			response.getWriter().println(xml.toString());

			return;
		}


		//Validate nickname and username
		if (action.equals("add") || action.equals("edit") || action.equals("remove"))
		{
			try {
				NICKNAME_CONSTRAINT.validate(nickEntry);
				USERNAME_CONSTRAINT.validate(nameEntry);
			}
			catch(ConstraintException e){
				if (e.getConstraint() instanceof StringConstraint){
					StringConstraint c = (StringConstraint) e.getConstraint();
					String message;

					if (e.getType() == ConstraintException.VALUE_ABOVE_MAXIMUM){
						message = c.getName() + " cannot exceed " +
						c.getMaxLength() +
						" characters.";
					}
					else
						if (e.getType() == ConstraintException.VALUE_BELOW_MINIMUM){
							message = c.getName() + " must be at least " +
							c.getMinLength() +
							" characters.";
						}
						else 
							if (e.getType() == ConstraintException.NULL_NOT_ALLOWED){
								message = "You must enter a value for " + c.getName();
							}
							else{
								message = c.getName() + " failed constraint check.";
							}

					constructFailure(message).write(response.getWriter());
					return;
				}
			}
		}

		MySqlBuddyList buddyList = null;
		try{
			buddyList = new MySqlBuddyList(userInfo.getClientID());

			if (action.equals("display")){
				Document result = constructSuccess();
				Document buddyListDoc = buddyList.toXml();

				result.getRootElement().add(buddyListDoc.getRootElement());
				result.write(response.getWriter());

				return;
			}

			if (action.equals("add")){
				boolean addResult;

				if(buddyList.contains(nameEntry)){
					constructFailure("Unable to add buddy: user '" + nameEntry + 
					"' is already in your buddy list.").write(response.getWriter());
					return;
				}
				if(buddyList.containsPending(nameEntry)){
					constructFailure("Unable to add buddy: user '" + nameEntry + 
					"' already has a pending buddy request.  He or she has not yet accepted or rejected your friendship.").write(response.getWriter());
					return;
				}
				if(nameEntry.toLowerCase().equals(userInfo.getClientName().toLowerCase())){
					constructFailure("You may not become buddies with youself.  Go get some friends.").write(response.getWriter());
					return;
				}
				if (nickEntry != null)
					addResult = buddyList.add(nameEntry, nickEntry);
				else
					addResult = buddyList.add(nameEntry);

				if(!addResult){
					constructFailure("Unable to add buddy: user '" + nameEntry + 
					"' does not exist.").write(response.getWriter());
					return;
				}


				constructSuccess().write(response.getWriter());
				return;
			}

			if (action.equals("remove")){
				if (!buddyList.remove(nameEntry)){
					constructFailure("Unable to remove user: user '" +nameEntry + 
					"' does not exist in your buddy list")
					.write(response.getWriter());
					return;
				}

				constructSuccess().write(response.getWriter());
				return;
			}

			if(action.equals("edit")){
				if(!buddyList.editBuddyNickname(nameEntry, nickEntry)){
					constructFailure("Unable to edit user: user '" + nameEntry + 
					"' does not exist in your buddy list.").write(response.getWriter());
					return;
				}

				constructSuccess().write(response.getWriter());
				return;
			}
			if(action.equals("getBuddyRequests") || action.equals("login")){
				Utilities.prepareResponseAgent(response);
				ArrayList<BuddyList.Buddy> buddyRequests = buddyList.getBuddyRequests();
				xml.constructBuddyRequests(buddyRequests);
				Utilities.returnResponse(response, xml);
				return;
			}
			if(action.equals("setBuddyRequests")){
				String[] userNames = request.getParameterValues("UserName");
				String[] accepteds = request.getParameterValues("Accepted");
				if(userNames.length != accepteds.length){
					constructFailure("Error, UserNames and Accepted input fields do not match in length.").write(response.getWriter());
					return;
				}
				for(int i = 0 ; i < userNames.length; i++){
					buddyList.setAccepted(userNames[i], accepteds[i]);
				}
				Utilities.prepareResponseAgent(response);
				xml.constructGenericSuccess();
				Utilities.returnResponse(response, xml);
			}
			if(action.equals("viewPendingBuddyRequests")){
				Utilities.prepareResponseAgent(response);
				ArrayList<BuddyList.Buddy> pendingBuddyRequests = buddyList.getPendingBuddyRequests();
				xml.constructBuddyRequests(pendingBuddyRequests);
				Utilities.returnResponse(response, xml);
				return;
			}
			if(action.equals("viewTransitiveBuddies")){
				if(nameEntry == null || nameEntry.equals("")){
					constructFailure("Error, no one spefied by name").write(response.getWriter());
					return;
				}
				DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
				MySqlBuddyList myBuddiesBuddies = null;
				if(!db.checkUserExists(nameEntry)){
					constructFailure("Error, user does not exist").write(response.getWriter());
					return;
				}
				if(!buddyList.contains(nameEntry)){
					constructFailure("Error, user is not your buddy").write(response.getWriter());
					return;
				}
				myBuddiesBuddies = new MySqlBuddyList(db.getUserByName(nameEntry).getClientID());
				Document result = constructSuccess();
				Document buddyListDoc = myBuddiesBuddies.toXml();
				result.getRootElement().add(buddyListDoc.getRootElement());
				result.write(response.getWriter());

				return;
			}
		}
		catch (SQLException e){
			constructFailure("Error accessing database:\n"+e.getMessage()).write(response.getWriter());
			Utilities.ignoreChipmarkException(e);
		} catch (IllegalAccessException e){
			constructFailure("Error IllegalAccess:\n"+e.getMessage()).write(response.getWriter());
			Utilities.ignoreChipmarkException(e);
		}  catch (Exception e){
			constructFailure("Error :\n"+e.getMessage()).write(response.getWriter());
			Utilities.ignoreChipmarkException(e);
		}



	}

	private Document constructSuccess(){
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");
		Element status = root.addElement("result");

		status.addElement("status").addText("SUCCESS");
		status.addElement("message");
		return document;
	}

	private Document constructFailure(String errMessage){
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");
		Element status = root.addElement("result");

		status.addElement("status").addText("FAILURE");
		status.addElement("message").addText(errMessage);
		return document;
	}
}
