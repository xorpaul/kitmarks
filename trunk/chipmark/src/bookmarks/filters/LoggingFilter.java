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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import sun.security.action.GetBooleanAction;

import bookmarks.Utilities;

/**
 *This filter tracks the flow of information into and out of key servlets
 *@author Carmen Wick
 */

public class LoggingFilter implements Filter{
	
	/**
	 * 
	 */
	private static final String LOGIN_SERVLET = "/Login";
	
	/**
	 * The FilterConfig from the Web.xml file
	 */
	private FilterConfig config;
	
	/**
	 * The MySqlEventLogger used to log to the database
	 */
	private EventLogger logger;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {

		config = filterConfig;
		logger = new MySqlEventLogger();
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        request.setCharacterEncoding(Utilities.REQUEST_ENCODING);
		
		//might want to check if req is actually an HttpServletRequest
		SmartHttpRequest smartRequest = new SmartHttpRequest((HttpServletRequest)request);

        smartRequest.setCharacterEncoding(Utilities.REQUEST_ENCODING);
		
		String servletPath = smartRequest.getServletPath();
		String username = smartRequest.getUserName();
		String ipAddress = smartRequest.getIpAddress();
		String browserType = smartRequest.getBrowserType();
		//boolean usingExtension = request.usingExtension();
		
		String action = smartRequest.getParameter("action");
		long startTime = System.currentTimeMillis();
		boolean threwException = false;
		Exception exception = null;
		
		int eventId=-1;	
		long endTime;
		int userId=-1;
		
		//Must not get user id before login servlet is called
		if (!servletPath.equals(LOGIN_SERVLET))
			userId = smartRequest.getUserId();
		
		//call servlet
		try {
			chain.doFilter(smartRequest, response);
		}
		catch (Exception ex){
			threwException = true;
			exception = ex;
		}
		
		endTime = System.currentTimeMillis();
		
		//Must get user id after login servlet is called
		if (servletPath.equals(LOGIN_SERVLET))
			userId = smartRequest.getUserId();
		
		try {
			eventId = this.logger.log(startTime, servletPath, (int)(endTime - startTime), userId, action, 
					threwException, ipAddress, browserType);
		}
		catch (IOException e){
			this.config.getServletContext().log("Warning: can't log event. " +
					"Reason: " + e.getMessage());
		}
		
		if (threwException){
			this.config.getServletContext().log("Caught exception while in " + servletPath + 
					". Username: " + 
					username + ". Event ID: " + eventId, exception);
			Utilities.wrapChipmarkException(exception);
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		this.config = null;
		this.logger = null;
	}
	
}
