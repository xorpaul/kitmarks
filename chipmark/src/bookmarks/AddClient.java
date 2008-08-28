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
import java.rmi.AlreadyBoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bookmarks.constraints.ConstraintException;
import bookmarks.constraints.StringConstraint;

/**
 * Servlet which handles new user requests. Refactored by Carmen Wick 4/8/05
 * Refactored by Dominique DuCharme on 2/4/2007
 * 
 * @author Dominique DuCharme
 */
public class AddClient extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = -2769055945462503273L;

	/**
	 * Defines what a valid username is or isn't.
	 * 
	 * Cant' use : due to buddies, white space not allowed in plugins
	 */
	public static final StringConstraint USERNAME_CONSTRAINT = new StringConstraint(
			"username", false, 1, 32, ": \t");

	/**
	 * Defines what a valid password is or isn't
	 */
	public static final StringConstraint PASSWORD_CONSTRAINT = new StringConstraint(
			"password", false, 6, 32);

	/**
	 * Defines what a valid email account is or isn't.
	 */
	public static final StringConstraint EMAIL_CONSTRAINT = new StringConstraint(
			"email address", true, 6, 255);

	/**
	 * Handles Post requst from client.
	 * 
	 * @param request
	 *            Request received from client.
	 * @param response
	 *            Response object received from client.
	 * @throws ServletException
	 *             If a servlet error occurs
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Handles Get request from client.
	 * 
	 * @param request
	 *            Request received from client.
	 * @param response
	 *            Response object sent back to client.
	 * @throws ServletException
	 *             If a servlet error occurs
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Utilities.prepareRequest(request);

		// String submitBtn =
		// Utilities.eliminateNull(request.getParameter("submitBtn"));
		String agent = Utilities.eliminateNull(request.getParameter("agent"));

		String clientName_entry = Utilities.eliminateNull(request
				.getParameter("clientName"));
		String clientMail_entry = Utilities.eliminateNull(request
				.getParameter("clientMail"));
		String clientPass_entry = Utilities.eliminateNull(request
				.getParameter("clientPass"));
		String clientPass2_entry = Utilities.eliminateNull(request
				.getParameter("clientPass2"));

		clientName_entry = clientName_entry.trim();
		if ("".equals(clientMail_entry)) {
			clientMail_entry = null;
		}

		boolean externalAgent;

		externalAgent = agent.equals(Utilities.AGENT_EXT);

		if (!isValidClientInfo(response, clientName_entry, clientMail_entry,
				clientPass_entry, clientPass2_entry, externalAgent, request)) {
			return;
		}

		clientMail_entry = Utilities.eliminateNull(clientMail_entry);
		clientMail_entry = clientMail_entry.trim();

		// The information is valid, so add the client
		addClientInfo(response, clientName_entry, clientMail_entry,
				clientPass_entry, clientPass2_entry, externalAgent, false);
	}

	/**
	 * @param response
	 *            The HttpServletResponce
	 * @param clientName_entry
	 *            The text entered in the name field
	 * @param clientMail_entry
	 *            The text entered in the email address field
	 * @param clientPass_entry
	 *            The text entered in the 1st password field
	 * @param clientPass2_entry
	 *            The text entered in the 2nd password field
	 * @param externalAgent
	 *            true if accessed from a plugin
	 * @return true if all information entered is valid
	 * @throws IOException
	 */
	private boolean isValidClientInfo(HttpServletResponse response,
			String clientName_entry, String clientMail_entry,
			String clientPass_entry, String clientPass2_entry,
			boolean externalAgent, HttpServletRequest request)
			throws IOException {
		String resultMsg;

		// get db
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		ClientEntry user = Utilities.validateUser(request, db);

		// Validate username, password, and email
		try {
			USERNAME_CONSTRAINT.validate(clientName_entry);
			PASSWORD_CONSTRAINT.validate(clientPass_entry);
			EMAIL_CONSTRAINT.validate(clientMail_entry);

			if (clientName_entry.indexOf("/") != -1) {
				if (user.getPreferredLang() == "eng")
					writeMessage("Accountname contains invalid character '/'!",
							response, externalAgent, clientName_entry,
							clientPass_entry, clientPass2_entry,
							clientMail_entry);
				else
					writeMessage("Benutzernamen mit '/' ist nicht möglich!",
							response, externalAgent, clientName_entry,
							clientPass_entry, clientPass2_entry,
							clientMail_entry);
				return false;
			}

			if (!clientPass_entry.equals(clientPass2_entry)) {
				if (user.getPreferredLang() == "eng")
					writeMessage("Both passwords must match.", response,
							externalAgent, clientName_entry, clientPass_entry,
							clientPass2_entry, clientMail_entry);
				else
					writeMessage("Beide Passwörter müssen übereinstimmen.",
							response, externalAgent, clientName_entry,
							clientPass_entry, clientPass2_entry,
							clientMail_entry);
				return false;
			}

			if (clientMail_entry == null || clientMail_entry.indexOf('@') == -1) {
				if (user.getPreferredLang() == "eng")
					writeMessage("You must enter a valid email address.",
							response, externalAgent, clientName_entry,
							clientPass_entry, clientPass2_entry,
							clientMail_entry);
				else
					writeMessage("Bitte eine gültige Email Adresse angeben.",
							response, externalAgent, clientName_entry,
							clientPass_entry, clientPass2_entry,
							clientMail_entry);
				return false;
			}
		} catch (ConstraintException e) {
			if (e.getConstraint() instanceof StringConstraint) {
				StringConstraint c = (StringConstraint) e.getConstraint();

				switch (e.getType()) {
				case ConstraintException.NULL_NOT_ALLOWED:
					resultMsg = "You must enter a value for " + c.getName();
					break;

				case ConstraintException.VALUE_ABOVE_MAXIMUM:
					resultMsg = c.getName() + " cannot exceed "
							+ c.getMaxLength() + " characters.";
					break;

				case ConstraintException.VALUE_BELOW_MINIMUM:
					resultMsg = c.getName() + " must be at least "
							+ c.getMinLength() + " characters.";
					break;

				case ConstraintException.ILLEGAL_CHARACTER:
					resultMsg = "Invalid characters in " + c.getName();
					break;

				default:
					resultMsg = c.getName() + " failed constraint check";
				}

				writeMessage(resultMsg, response, externalAgent,
						clientName_entry, clientPass_entry, clientPass2_entry,
						clientMail_entry);
				return false;
			}
		}
		return true;
	}

	/**
	 * @param response
	 *            The HttpServletResponce
	 * @param clientName_entry
	 *            The text entered in the name field
	 * @param clientMail_entry
	 *            The text entered in the email address field
	 * @param clientPass_entry
	 *            The text entered in the 1st password field
	 * @param clientPass2_entry
	 *            The text entered in the 2nd password field
	 * @param externalAgent
	 *            true if accessed from a plugin
	 * @return true if the client was added
	 * @throws IOException
	 * @throws ServletException
	 */
	private boolean addClientInfo(HttpServletResponse response,
			String clientName_entry, String clientMail_entry,
			String clientPass_entry, String clientPass2_entry,
			boolean externalAgent, boolean isADSAccount) throws IOException,
			ServletException {
		// User info OK, add user
		try {
			DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

			db.addClient(clientName_entry, clientMail_entry, clientPass_entry,
					true, isADSAccount);

			writeMessage("SUCCESS", response, externalAgent, "", "", "", "");
			return true;
		} catch (AlreadyBoundException e) {
			writeMessage(clientName_entry
					+ " is already in use, please enter a different username",
					response, externalAgent, "", "", "", "");
		} catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}

		return false;
	}

	/**
	 * 
	 * @param message
	 * @param response
	 * @param externalAgent
	 * @param clientName_entry
	 * @param clientPass_entry
	 * @param clientPass2_entry
	 * @param clientMail_entry
	 * @throws IOException
	 */
	private void writeMessage(String message, HttpServletResponse response,
			boolean externalAgent, String clientName_entry,
			String clientPass_entry, String clientPass2_entry,
			String clientMail_entry) throws IOException {

		BookmarkXML xml = new BookmarkXML();
		xml.constructAddClientResult("", message, clientName_entry,
				clientMail_entry);

		if (externalAgent) {
			Utilities.prepareResponseAgent(response);
		} else {
			Utilities.prepareResponseWeb(response);

			ClientEntry userJoined = new ClientEntry(0, "SessionDummy",
					"dummy@session.de", "xxx", true, "ger");

			Utilities.styleXML(xml, "addclientresult", userJoined);
		}

		Utilities.returnResponse(response, xml);
	}

}
