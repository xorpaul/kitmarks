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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bookmarks.AccountPreferences.updateCode;
import bookmarks.constraints.ConstraintException;
import bookmarks.constraints.StringConstraint;

/**
 * This class is a space for methods that are used by multiple servlets. Using
 * methods from the Utilities class will prevent code bloat, and make it easier
 * to refactor Chipmark. Any method used in more than one servlet should be
 * extracted to the Utilities class.
 * 
 * @author Joshua Fleck
 * 
 */
public class Utilities {

	/**
	 * This represents a plug-in
	 */
	public static final String AGENT_EXT = "ext";

	/**
	 * We are assuming all requests are in Unicode.
	 */
	public static final String REQUEST_ENCODING = "UTF-8";

	/**
	 * Email content type
	 */
	public static final String EMAIL_CONTENT_TYPE = "text/plain";

	/**
	 * System's file encoding property name
	 */
	public static final String SYS_FILE_ENCODE_NAME = "file.encoding";

	/**
	 * System's file encoding property value (For UTF-8)
	 */
	public static final String SYS_FILE_ENCODE_VALUE = "UTF-8";

	/**
	 * We send all responses in Unicode. This is for an html response.
	 */
	public static final String RESPONSE_ENCODING_WEB = "text/html; charset=utf-8";

	/**
	 * This is the content disposition html header for an export.
	 */
	public static final String EXPORT_CONTENT_DISPOSITION = "Content-Disposition";

	/**
	 * This is the content disposition value for an export.
	 */
	public static final String EXPORT_CONTENT_DIS_VALUE = "attachment; filename=Chipmarks.html";

	/**
	 * We send all responses in Unicode. This is for an xml response.
	 */
	public static final String RESPONSE_ENCODING_AGENT = "text/xml; charset=utf-8";

	/**
	 * The attribute stored in the session holding the user info.
	 */
	public static final String SESSION_ATTR = "AuthStamp";

	/**
	 * The attribute stored in the cookie holding the user info.
	 */
	public static final String COOKIE_ATTR = "Chipmark_AuthStamp";

	/**
	 * The length of time in seconds until the session will time out.
	 */
	public static final int SESSION_TIMEOUT = 40 * 60;

	/**
	 * The length of time in seconds until the cookie will expire.
	 */
	public static final int COOKIE_TIMEOUT = 86400 * 30;

	/**
	 * The name of the main xsl stylesheet used by most of the classes.
	 */
	private static final String MAIN_STYLESHEET = "main.xsl";

	/**
	 * The message to display when the user is not logged in properly
	 */
	private static final String INVALID_USER_MSG = "You must register or login before viewing bookmarks.";

	/**
	 * The DOCTYPE of all XSL transformations (DTD file)
	 */
	public static final String REQUEST_DOCTYPE_DTD = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";

	/**
	 * The DOCTYPE of all XSL transformations (Transitions//EN)
	 */
	public static final String REQUEST_DOCTYPE_TRAN = "-//W3C//DTD XHTML 1.0 Transitional//EN";

	/**
	 * Verifies whether the connection is authorized to proceed. It first tries
	 * to find a valid session attached to this request. If it can't find one,
	 * it seraches for a chipmark auth stamp cookie. If this fails, then the
	 * request is not authorized and this method returns false.
	 * 
	 *(note, this is only a temporary method to fix bug #1351728. Eventually,
	 * authentication will be handled by a filter.)
	 * 
	 * @throws Exception
	 *             if a database error occured
	 * 
	 *@return true if the request passes authentication
	 *@return false if the request fails authentication
	 */

	public static boolean authenticateRequest(HttpServletRequest request)
			throws Exception {
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		return Utilities.validateUser(request, db) != null ? true : false;
	}

	public static void prepareSystemFileEncoding() {
		System.setProperty(SYS_FILE_ENCODE_NAME, SYS_FILE_ENCODE_VALUE);
	}

	/**
	 * Determines if the user is logged in to chipmark, and locates that user's
	 * information. If the user is not logged in, null is returned.
	 * 
	 * @param request
	 *            The HttpServletRequest object (not null)
	 * @param db
	 *            A DatabaseWrapper object (not null)
	 * @return A ClientEntry object on success. Null on failure.
	 */
	public static ClientEntry validateUser(HttpServletRequest request,
			DatabaseWrapper db) {

		try {
			HttpSession session = request.getSession(true);

			ClientEntry user = null;

			if (session != null) {

				user = (ClientEntry) session.getAttribute(SESSION_ATTR);
				if (user != null
						&& !user.getClientName().equals("SessionDummy")) {
					return user;
				}

				else {
					session = null;
				}

			}

			if (session == null) {

				// look for cookie
				Cookie[] cookies = request.getCookies();

				for (int i = 0; cookies != null && i < cookies.length; i++) {

					if (cookies[i].getName().equals(COOKIE_ATTR)) {

						String userData = cookies[i].getValue();

						if (userData != null && userData.indexOf("*") != -1) {

							String clientPass = userData.substring(0, userData
									.indexOf("*"));
							String userName = URLDecoder.decode(userData
									.substring(userData.indexOf("*") + 1),
									REQUEST_ENCODING);

							user = db.authLogin(userName, clientPass, true);

							// Add authentication to user's session
							session = request.getSession(true);
							session.setAttribute(SESSION_ATTR, user);
							session.setMaxInactiveInterval(SESSION_TIMEOUT);

							return user;
						}

					}

				}

				// Couldn't find any matching cookie

				ClientEntry userJoined = new ClientEntry(0, "SessionDummy",
						"dummy@session.de", "xxx", true, "ger");

				HttpSession sessionDummy = request.getSession();
				sessionDummy.setAttribute(Utilities.SESSION_ATTR, userJoined);
				sessionDummy
						.setMaxInactiveInterval((Utilities.SESSION_TIMEOUT * 5));

				return userJoined;
			}
		} catch (Exception e) {
			Utilities.ignoreChipmarkException(e);
		}

		// should never get here
		return null;

	}

	/**
	 * Displays a message on the main page if the user is not logged in.
	 * 
	 * @param xml
	 *            A BookmarkXML object (Not null)
	 */
	public static void reportInvalidUserWeb(BookmarkXML xml) {
		xml.constructWithResultMsg("", INVALID_USER_MSG);

		ClientEntry userJoined = new ClientEntry(0, "SessionDummy",
				"dummy@session.de", "xxx", true, "ger");

		Utilities.styleXML(xml, "error", userJoined);
	}

	/**
	 * Displays a message on the plug in if the user is not logged in.
	 * 
	 * @param xml
	 *            A BookmarkXML object (Not null)
	 */
	public static void reportInvalidUserAgent(BookmarkXML xml) {
		xml.constructNodeActionFailure(Utilities.INVALID_USER_MSG);
	}

	/**
	 * Sets the request to have the correct encoding.
	 * 
	 * @param request
	 *            The HttpServletRequest object (not null)
	 */
	public static void prepareRequest(HttpServletRequest request) {
		try {
			request.setCharacterEncoding(REQUEST_ENCODING);
		} catch (UnsupportedEncodingException e) {
			Utilities.ignoreChipmarkException(e);
		}
	}

	/**
	 * Prepares the response with the correct encoding for the web UI.
	 * 
	 * @param response
	 *            The HttpServletResponse object (not null)
	 */
	public static void prepareResponseWeb(HttpServletResponse response) {
		response.setContentType(RESPONSE_ENCODING_WEB);
	}

	/**
	 * Prepares the response with the correct encoding for the web UI. This also
	 * prepars the request for Exporting the bookmarks
	 * 
	 * @param response
	 *            The HttpServletResponse object (not null)
	 */
	public static void prepareExport(HttpServletResponse response) {
		response
				.setHeader(EXPORT_CONTENT_DISPOSITION, EXPORT_CONTENT_DIS_VALUE);
	}

	/**
	 * Prepares the response with the correct encoding for the extentions.
	 * 
	 * @param response
	 *            The HttpServletResponse object (not null)
	 */
	public static void prepareResponseAgent(HttpServletResponse response) {
		response.setContentType(RESPONSE_ENCODING_AGENT);
	}

	/**
	 * Outputs the xml to the user. Note: no more text may be added to the
	 * response after this is called.
	 * 
	 * @param response
	 *            An HttpResponse object (not null)
	 * @param xml
	 *            A BookmarkXML object (not null)
	 * @throws IOException
	 */
	public static void returnResponse(HttpServletResponse response,
			BookmarkXML xml) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.print(xml.toString());
	}

	/**
	 * Performs the xsl style transformation.
	 * 
	 * @param xml
	 *            A BookmarkXML object
	 * @param target
	 *            The target to be passed to the xsl template
	 */
	public static void styleXML(BookmarkXML xml, String target, ClientEntry user) {
		xml.addXSLTarget(target);
		if (user.getPreferredLang().equals("eng"))
			xml.styleDocument(MAIN_STYLESHEET, "eng");
		else
			xml.styleDocument(MAIN_STYLESHEET, "ger");
	}

	/**
	 * Performs the xsl style transformation.
	 * 
	 * @param xml
	 *            A BookmarkXML object
	 */
	public static void styleXML(BookmarkXML xml, ClientEntry user) {
		if (user.getPreferredLang().equals("eng"))
			xml.styleDocument(MAIN_STYLESHEET, "eng");
		else
			xml.styleDocument(MAIN_STYLESHEET, "ger");
	}

	/**
	 * Performs the xsl style transformation.
	 * 
	 * @param xml
	 *            A BookmarkXML object
	 * @param stylesheet
	 *            The name of a xsl stylesheet
	 * @param target
	 *            The target to be passed to the xsl template
	 */
	public static void styleXMLCustom(BookmarkXML xml, String stylesheet,
			String target, ClientEntry user) {
		xml.addXSLTarget(target);
		if (user.getPreferredLang().equals("eng"))
			xml.styleDocument(stylesheet, "eng");
		else
			xml.styleDocument(stylesheet, "ger");
	}

	/**
	 * Encodes information for an authentication cookie.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @return A hash containing the password and username of the user separated
	 *         by an asterisk.
	 * @author schwerdf
	 * @throws ServletException
	 */
	public static String encodeAuthCookie(ClientEntry user)
			throws ServletException {
		try {
			return new String(user.getClientPass()
					+ "*"
					+ URLEncoder.encode(user.getClientName(),
							Utilities.REQUEST_ENCODING));
		} catch (UnsupportedEncodingException e) {
			Utilities.wrapChipmarkException(e);
		}
		return null;
	}

	/**
	 * Trims a string by calling its trim() method.
	 * 
	 * @returns the trimmed string, or null if the string was null
	 */
	public static String trimString(String s) {
		if (s == null)
			return null;
		else
			return s.trim();
	}

	/**
	 * Converts a null string to an empty string.
	 * 
	 * @param s
	 *            The string to convert.
	 * @return A new blank string if s == null, s otherwise.
	 * @author schwerdf
	 */
	public static String eliminateNull(String s) {
		if (s == null) {
			return new String("");
		} else {
			return s;
		}
	}

	/**
	 * This method takes a constraint and a string and tests the constraint
	 * anaginst the string.
	 * 
	 * @param test
	 *            A StringConstraint
	 * @param string
	 *            A String to test
	 * @return An error message, or null if the test passed
	 */
	public static String checkConstraint(StringConstraint test, String string) {

		StringBuffer out = new StringBuffer();

		try {
			test.validate(string);
		} catch (ConstraintException e) {

			if (e.getConstraint() instanceof StringConstraint) {

				StringConstraint c = (StringConstraint) e.getConstraint();

				if (e.getType() == ConstraintException.VALUE_ABOVE_MAXIMUM) {
					out.append(c.getName());
					out.append(" cannot exceed ");
					out.append(c.getMaxLength());
					out.append(" characters.");
				} else {

					if (e.getType() == ConstraintException.NULL_NOT_ALLOWED) {
						out.append("You must enter a value for ");
						out.append(c.getName());
					} else {
						if (e.getType() == ConstraintException.VALUE_BELOW_MINIMUM) {
							out.append("You must enter at least ");
							out.append(c.getMinLength());
							out.append(" character(s) for ");
							out.append(c.getName());
						} else {
							out.append("Constraint check failed for ");
							out.append(c.getName());
						}
					}
				}

				return out.toString();
			}

			return "Constraint check failed";
		}

		return null;
	}

	/**
	 * Ensures the link URL is in the proper protocol.
	 * 
	 * @param linkURL_entry
	 *            The link name (Not null)
	 * @return The correct link URL.
	 */
	public static String addURLProtocol(String linkURL_entry) {

		if (linkURL_entry == null) {
			return null;
		}

		if (!linkURL_entry.matches("^.*://.*$")) {
			linkURL_entry = "http://" + linkURL_entry;
		}

		return linkURL_entry;
	}

	/**
	 * Hashes a password using the MD5 function.
	 * 
	 * @param inputString
	 *            The password to hash.
	 * @return The hash.
	 * @author syilek
	 * @throws UnsupportedEncodingException
	 */
	private static String md5(String inputString) throws DigestException,
			NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] inputBytes = inputString.getBytes(REQUEST_ENCODING);
		byte[] digestBytes;
		String digestString = "";
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(inputBytes);
		digestBytes = md.digest();
		for (int i = 0; i < digestBytes.length; i++) {
			String tempDigestString = Integer
					.toHexString(0xFF & digestBytes[i]);
			if (tempDigestString.length() == 1) {
				tempDigestString = "0" + tempDigestString;
			}
			digestString += tempDigestString;
		}
		return digestString;
	}

	/**
	 * Hashes a password.
	 * 
	 * @param inputString
	 *            The password to hash.
	 * @return The hash.
	 * @author syilek
	 * @throws UnsupportedEncodingException
	 */

	public static String encrypt(String inputString) throws DigestException,
			NoSuchAlgorithmException, UnsupportedEncodingException {
		// we are simply using md5 for encryption at this time
		return md5(inputString);
	}

	/**
	 * Call this method for an unexpected exception that should be reported to
	 * the user in a respectable manner. The exception should be caught and
	 * masked by a generic Chipmark error servlet.
	 * 
	 * @param e
	 *            The caught exception
	 * @throws ServletException
	 */
	public static void wrapChipmarkException(Exception e)
			throws ServletException {
		e.printStackTrace();
		throw new ServletException(e);
	}

	/**
	 * Call this method for an expected exception that should be recorded, but
	 * not reported to the user
	 * 
	 * @param e
	 *            The caught exception
	 */
	public static void ignoreChipmarkException(Exception e) {
		e.printStackTrace();
	}

	/**
	 * Takes a string of labels and splits them into a list of labels. This
	 * method is usefull for splitting any type of comma-seperated list.
	 * 
	 * @param labelNames_entry
	 *            The user-selected labels (Not null)
	 * @return An ArrayList of labels or null if an empty string was provided.
	 */
	public static ArrayList<String> getLabelNames(String labelNames_entry) {

		ArrayList<String> labelNames = null;

		if (!labelNames_entry.equals("")) {

			labelNames = new ArrayList<String>();

			String[] labelNames_split = labelNames_entry
					.split("[\\s]*[,]+[\\s]*");

			if (labelNames_split.length != 1 || !labelNames_split[0].equals("")) {

				for (int i = 0; i < labelNames_split.length; i++) {

					labelNames.add(labelNames_split[i]);

				}
			}

			// check for duplicate names
			for (int i = 0; i < labelNames.size(); i++) {
				if (labelNames.lastIndexOf(labelNames.get(i)) != i) {
					labelNames.remove(i);
					i--;
				}
			}

		}

		return labelNames;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static String encodeURIComponent(String data) {
		String encodedString = "";
		try {
			encodedString = URLEncoder.encode(data, Utilities.REQUEST_ENCODING);
		} catch (Exception e) {
		}

		return encodedString;
	}

	/**
	 * 
	 * @param request
	 * @param db
	 * @return
	 */
	public static String getUsername(HttpServletRequest request,
			DatabaseWrapper db) {
		ClientEntry user = validateUser(request, db);
		if (user == null || user.getClientID() == 0) {
			return "";
		} else {
			return user.getClientName();
		}
	}

	/**
	 * This method determines if a url is safe, and if the user cares
	 * 
	 * @param url
	 * @param user
	 * @return boolean
	 */
	public static boolean isSafeUrl(String url, ClientEntry user) {
		if (user == null || user.isOnlySafe())
			return isSafeUrl(url);
		else
			return true;
	}

	/**
	 * This method determines if a url is safe
	 * 
	 * @param url
	 * @return boolean
	 */
	public static boolean isSafeUrl(String url) {
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		java.net.URL fullPath;
		try {
			fullPath = new java.net.URL(url);
			String domain = fullPath.getHost().replaceAll("www.", "");
			return db.isUrlSafe(domain);
		} catch (MalformedURLException e) {
			return true;
		} catch (SQLException e) {
			Utilities.ignoreChipmarkException(e);
			return false;
		}
	}

	/**
	 * This method will block out any unsafe urls in the list based on the
	 * user's preferences.
	 * 
	 * @param links
	 * @param user
	 */
	public static void cleanseUrlList(ArrayList<LinkNameUrlObject> links,
			ClientEntry user, boolean display) {

		LinkNameUrlObject l = null;
		int size = links.size();

		for (int i = 0; i < size; i++) {

			l = links.get(i);

			if (!Utilities.isSafeUrl(l.getLinkURL(), user)) {

				if (display) {// This is the web api (disabled for now.
					// remove the '&& false' to enabled)

					if (user != null) {
						l
								.setLinkName("Content deemed explicit. Click on link to remove content filtering.");
						l
								.setLinkURL("javascript: reverseContentFiltering( false );");
					} else {
						l
								.setLinkName("Unable to display URL. Content deemed explicit.");
						l.setLinkURL("");
					}

				} else {// This is for the plugins

					links.remove(l);
					size--;
					i--;

				}

			}

		}

	}
}
