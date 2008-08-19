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
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Returns data as XML for any asyncronous request.
 * @author Joshua Fleck
 *
 */
public class AutosuggestServlet  extends HttpServlet {

	/**
	 * A unique identifier for this version of the class.
	 */
	private static final long serialVersionUID = -4345018100675841165L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Utilities.prepareRequest(request);

		int suggestType = Integer.parseInt(Utilities.eliminateNull((String) request.getParameter("suggest")));

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		BookmarkXML xml = new BookmarkXML();
		ClientEntry user = Utilities.validateUser(request, db);
		
		//Determine the type of information required.
		if(user != null) {
			try {
				switch(suggestType) {
				case 1:
					this.getLabels(user, db, xml);
					break;
				case 2:
					this.getURLs(user, db, xml);
					break;
				default:	
					xml.constructNodeActionFailure("");
					break;
				}
			} catch (Exception e) {
				xml.constructNodeActionFailure("");
			}
		} else {
			xml.constructNodeActionFailure("");
		}

		Utilities.prepareResponseAgent(response);
		Utilities.returnResponse(response, xml);

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);	
	}
	
	/**
	 * Adds the users labels to an xml form.
	 * @param user A CientEntry (not null)
	 * @param db A DBWrapper (not null)
	 * @param xml A BookmarkXMl object (not null)
	 * @throws SQLException
	 * @throws IllegalAccessException
	 */
	private void getLabels(ClientEntry user, DatabaseWrapper db, BookmarkXML xml) throws SQLException, IllegalAccessException {
				
		ArrayList<String> labels = db.getAllUserLabels(user);
		xml.constructNodeActionList(labels);
		
	}
	
	/**
	 * Adds the users urls to an xml form.
	 * @param user A CientEntry (not null)
	 * @param db A DBWrapper (not null)
	 * @param xml A BookmarkXMl object (not null)
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException 
	 */
	private void getURLs(ClientEntry user, DatabaseWrapper db, BookmarkXML xml) throws SQLException, IllegalAccessException, NoSuchFieldException {
				
		ArrayList<Bookmark> bookmarks = db.getAllUserBookmarks(user);
		ArrayList<String> urls = new ArrayList<String>(bookmarks.size());
		
		for(Bookmark b:bookmarks) {
			urls.add(b.getLinkURL());
		}
		
		xml.constructNodeActionList(urls);
		
	}

}
