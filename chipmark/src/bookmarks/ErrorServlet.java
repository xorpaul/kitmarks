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
 * This class is responsible for catching all unexpected exceptions and displaying
 * an error message to the user or plugin.
 * @author Joshua Fleck
 *
 */
public class ErrorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4503575794348230188L;
	
	/**
	 * The message displayed to the user if an exception is thrown.
	 */
	private static final String ERROR_MESSAGE = "Chipmark encountered an unexpected error";

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		
		Utilities.prepareRequest(arg0);
		BookmarkXML xml = new BookmarkXML();
		String agent = Utilities.eliminateNull((String) arg0.getParameter("agent"));
		
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		
		ClientEntry user = Utilities.validateUser(arg0, db);
		
		if(agent.equals(Utilities.AGENT_EXT)) {
			xml.constructGenericFailure(ERROR_MESSAGE);
			Utilities.prepareResponseAgent(arg1);
		} else {
			if(user != null) {
				xml.constructWithResultMsg(user.getClientName(), ERROR_MESSAGE);
			}
			else {
				xml.constructWithResultMsg("", ERROR_MESSAGE);
			}
			Utilities.styleXML(xml,"error", user);
			Utilities.prepareResponseWeb(arg1);
		}
		
		Utilities.returnResponse(arg1, xml);
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		this.doGet(arg0, arg1);
	}
	
	

}
