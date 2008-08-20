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

import de.fzk.iwr.ads.ADSConnection;
import de.fzk.iwr.ads.Domain;

/**
 * Authenticates and sets cookie for client's session.
 * 
 * @author August Schwerdfeger
 */
public class LoginServlet extends HttpServlet {

	/**
	 * A unique identifier for this version of the class.
	 */
	private static final long serialVersionUID = -5940099144012570075L;
	/**
	 * The message to return in the xml for an un-successful login.
	 */
	private static final String FAIL_MESSAGE = "The username/password you entered is incorrect or invalid";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		Utilities.prepareRequest(arg0);

		String agent = Utilities.eliminateNull((String) arg0
				.getParameter("agent"));
		String clientName_entry = Utilities.eliminateNull((String) arg0
				.getParameter("clientName"));
		String clientPass_entry = Utilities.eliminateNull((String) arg0
				.getParameter("clientPass"));
		String sendCookie = Utilities.eliminateNull((String) arg0
				.getParameter("keepCookie"));
		String encrypted = Utilities.eliminateNull((String) arg0
				.getParameter("encrypted"));

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		ClientEntry userInfo = null;
		String domain = null;

		if (clientName_entry.indexOf("/") != -1)
		// Wurde ein Benutzername mit /
		// angegeben?
		{

			String[] clientName_array = clientName_entry.split("/");

			domain = clientName_array[0];
			String clientName_ohneDomain = clientName_array[1];

			userInfo = this.attemptLogin(encrypted, clientName_ohneDomain,
					clientPass_entry, db, true, domain);
		} else {
			// Nein, es wurde kein ADS Benutzer mit / im Benutzernamen
			// eingegeben

			userInfo = this.attemptLogin(encrypted, clientName_entry,
					clientPass_entry, db, false, domain);
		}

		boolean loggedIn = userInfo == null ? false : true;

		if (loggedIn && userInfo.getClientID() != 0) {
			this.createSession(arg0, userInfo);
			this.createCookie(arg1, userInfo, sendCookie);
		}

		BookmarkXML xml = new BookmarkXML();

		if (agent.equals(Utilities.AGENT_EXT)) {
			Utilities.prepareResponseAgent(arg1);
			this.prepareAgentXML(xml, userInfo);
		} else {
			Utilities.prepareResponseWeb(arg1);
			this.prepareWebXML(xml, userInfo, loggedIn);
		}

		Utilities.returnResponse(arg1, xml);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		this.doGet(arg0, arg1);
	}

	/**
	 * Uses the database to authorize the user-provided login.
	 * 
	 * @param encrypted
	 *            If 'yes' the password is encrypted.
	 * @param clientName
	 *            The username.
	 * @param clientPass
	 *            The password.
	 * @param db
	 *            A Database Wrapper object.
	 * @return A ClientEntry object on success. Null on failure.
	 */
	public ClientEntry attemptLogin(String encrypted, String clientName,
			String clientPass, DatabaseWrapper db, Boolean useADSConnector,
			String domainname) {

		ClientEntry userInfo = null;

		try {
			if (useADSConnector) {
				ADSConnection ADSC = new ADSConnection();
				Domain d = new Domain();

				/**
				 * Login Parameter für ADS Connector
				 */

				if (domainname.equalsIgnoreCase("FZKA")) {
					d.setBaseDn("dc=ka,dc=fzk,dc=de");
					d.setSite("KA");
					d.setHost("ka.fzk.de");
					d.setName("FZKA");
				}

				if (domainname.equalsIgnoreCase("IAI")) {
					d.setBaseDn("dc=iai,dc=fzk,dc=de");
					d.setSite("KA-IAI");
					d.setHost("iai.fzk.de");
					d.setName("IAI");
				}

				d.setPort(389);

				/*
				 * 
				 * Überprüfe angegeben Logindaten mit ADS DomainController
				 */

				String mail = ADSC.login(d, clientName, clientPass);

				clientName = domainname + "/" + clientName;

				if (mail != null)// gültiger ADS Account ?
				{
					// Ja, es ist ein gültiger ADS Account!

					userInfo = db.getUserByName(clientName);

					if (userInfo != null) // Gibt es den User schon in der
					// lokalen
					// Datenbank?
					{
						// Ja, User gibt es schon in der lokalen Datenbank!

						// Fahre mit dem normalen Login fort!
						if (encrypted.equals("yes")) {
							userInfo = db.authLogin(clientName,
									PropertyManager.ADS_PASSWORD, true);
						} else {
							userInfo = db.authLogin(clientName,
									PropertyManager.ADS_PASSWORD, false);
						}

					} else {
						// Nein, User gibt es NICHT in der lokalen Datenbank!

						db.addClient(clientName, mail,
								PropertyManager.ADS_PASSWORD, true, true); // Erstelle
						// Account in
						// der lokalen
						// DB

						// Fahre mit dem normalen Login fort!
						if (encrypted.equals("yes")) {
							userInfo = db.authLogin(clientName,
									PropertyManager.ADS_PASSWORD, true);
						} else {
							userInfo = db.authLogin(clientName,
									PropertyManager.ADS_PASSWORD, false);
						}
					}
				}
			}

			else {// Nein, es ist KEIN gültiger ADS Account!

				// Versuche, ob die Login Daten mit einem lokalen Benutzer
				// übereinstimmen
				if (encrypted.equals("yes")) {
					userInfo = db.authLogin(clientName, clientPass, true);
				} else {
					userInfo = db.authLogin(clientName, clientPass, false);
				}

			}

		} catch (Exception e) {
			userInfo = null;
		}

		return userInfo;
	}

	/**
	 * Creates a new HttpSession for this user.
	 * 
	 * @param request
	 *            The HttpServletRequest.
	 * @param userInfo
	 *            The user's ClientEntry (not null) object.
	 */
	public void createSession(HttpServletRequest request, ClientEntry userInfo) {
		HttpSession session = request.getSession();
		session.setAttribute(Utilities.SESSION_ATTR, userInfo);
		session.setMaxInactiveInterval(Utilities.SESSION_TIMEOUT);
	}

	/**
	 * Creates a new cookie for this user.
	 * 
	 * @param response
	 *            The HttpServletResponse
	 * @param userInfo
	 *            The user's clientEntry (not null) object.
	 * @param sendCookie
	 *            A String, if empty the cookie will not be created.
	 * @throws ServletException
	 */
	public void createCookie(HttpServletResponse response,
			ClientEntry userInfo, String sendCookie) throws ServletException {
		if (!sendCookie.equals("")) {
			Cookie authCookie = new Cookie(Utilities.COOKIE_ATTR, Utilities
					.encodeAuthCookie(userInfo));
			authCookie.setMaxAge(Utilities.COOKIE_TIMEOUT);
			response.addCookie(authCookie);
		}
	}

	/**
	 * Prepares the xml if the request came from a plugin.
	 * 
	 * @param xml
	 *            A BookmarkXML object.
	 * @param userInfo
	 *            The user's clientEntry object.
	 */
	public void prepareAgentXML(BookmarkXML xml, ClientEntry userInfo) {
		String userName = null;
		if (userInfo == null) {
			xml.constructLogin("", "");
		} else {
			userName = userInfo.getClientName();
			xml.constructLogin(userName, "");
		}
	}

	/**
	 * Prepares the xml if the request came from the web.
	 * 
	 * @param xml
	 *            A BookmarkXML object.
	 * @param userInfo
	 *            The user's clientEntry object.
	 * @param loggedIn
	 *            True, if the user is logged in.
	 */
	public void prepareWebXML(BookmarkXML xml, ClientEntry userInfo,
			boolean loggedIn) {

		String resultMsg = null;

		if (loggedIn && userInfo != null && userInfo.getClientID() != 0) {

			resultMsg = BookmarkXML.SUCCESS;
			String userName = null;

			userName = userInfo.getClientName();

			if (!userName.equals("admin")) {
				xml.constructWithResultMsg(userName, resultMsg);
				Utilities.styleXML(xml, "userHome", userInfo);
			}
			if (userName.equals("admin")) {
				xml.constructWithResultMsg(userName, resultMsg);
				Utilities.styleXML(xml, "admin", userInfo);
			}
		} else {
			resultMsg = FAIL_MESSAGE;
			xml.constructWithResultMsg("", resultMsg);

			ClientEntry userJoined = new ClientEntry(0, "SessionDummy",
					"dummy@session.de", "xxx", true, "ger");

			Utilities.styleXML(xml, "error", userJoined);

		}

	}
}
