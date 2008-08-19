/*
 -------------------------------------------------------------------------------------------------
 Copyright, 2006, Chipmark.
 
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

package bookmarks.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import bookmarks.ClientEntry;
import bookmarks.Utilities;

public class SmartHttpRequest extends HttpServletRequestWrapper{

	/**
	 * 
	 */
	private ClientEntry userInfo;
	
	/**
	 * 
	 * @param r
	 */
	public SmartHttpRequest(HttpServletRequest r){
		super(r);
		setUserInfo();
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getBrowserType(){
		String browser = Utilities.eliminateNull(getHeader("user-agent"));
		
		if (browser.equals(""))
			browser = "unknown";
		
		return browser;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getIpAddress(){
		return getRemoteAddr();
	}
	
	/**
	 * 
	 * @return boolean
	 */
	public boolean usingExtension(){
		if (Utilities.eliminateNull(getParameter("agent")).equals(Utilities.AGENT_EXT))
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @return String
	 */
	public String getUserName(){
		if (this.userInfo == null)
			return null;
		
		return this.userInfo.getClientName();
	}
	
	/**
	 * 
	 * @return int
	 */
	public int getUserId(){
		setUserInfo();
		
		if (this.userInfo == null)
			return -1;
		
		return this.userInfo.getClientID();
	}
	
	/** grabs the ClientEntry object from the session.
	 * Must be called if session object was modified.
	 */
	private void setUserInfo(){
		HttpSession session = getSession(false);
		if (session == null)
			this.userInfo = null;
		else
			this.userInfo = (ClientEntry)session.getAttribute(Utilities.SESSION_ATTR);
	}
	
}
