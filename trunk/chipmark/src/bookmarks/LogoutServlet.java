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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Removes cookie to end client's session.
 * @author August Schwerdfeger
 */
public class LogoutServlet extends HttpServlet {
		
	/**
	 * Unique identifier for this version of the class.
	 */
	private static final long serialVersionUID = 6759782000547194337L;

	/**
	 * After a logout the user will be directed to this page.
	 */
	private static final String REDIRECT_MAIN = "Main";

	/** Handles Post request from client.
	 * @param request Request received from client.
	 * @param response Response object sent back to client.
	 * @throws ServletException If a servlet error occurs
	 * @throws IOException If an I/O error occurs
	 */    
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException,IOException {
		doGet(request,response);
	}
	
	
	/** Handles Get request from client.
	 * @param request Request received from client.
	 * @param response Response object sent back to client.
	 * @throws ServletException If a servlet error occurs
	 * @throws IOException If an I/O error occurs
	 */    
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException, IOException {
	
        Utilities.prepareRequest(request);
        
		HttpSession session = request.getSession();
		session.invalidate();
		response.addCookie(new Cookie(Utilities.COOKIE_ATTR,null));
		
		response.sendRedirect(REDIRECT_MAIN);
	}	
}
