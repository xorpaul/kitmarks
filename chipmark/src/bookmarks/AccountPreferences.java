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
import java.util.Iterator;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookmarks.constraints.StringConstraint;
import bookmarks.constraints.ConstraintException;

/**
 * This servlet is responsible for changing a users password and/or email
 * address. Current password is required to change either or both.
 * 
 * Refactored by Dominique DuCharme on 2/4/2007
 * 
 * @author Dominique DuCharme
 * 
 */
public class AccountPreferences extends HttpServlet {
	/**
	 * The error to show if an invalid email address is entered.
	 */
	private static final String INVALID_EMAIL = "Please enter a valid email address";

	/**
	 * The text sent by the submit button.
	 */
	private static final String SUBMIT_BTN_LABEL = "update preferences";

	/**
	 * The error message shown if a user is not logged in.
	 */
	private static final String INVALID_USER_MSG = "You must register or login before managing your account.";

	/**
	 * A unique identifier for this version of the class.
	 */
	private static final long serialVersionUID = 4279138580788774005L;

	/**
	 * A status indicating if a field has an updated value or not.
	 */
	public enum updateCode {
		NO_UPDATE, UPDATE, ERROR
	}

	/**
	 * Defines what a valid password is or isn't
	 */
	public static final StringConstraint PASSWORD_CONSTRAINT = new StringConstraint(
			"password", false, 6, 32);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Utilities.prepareRequest(request);

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		ClientEntry user = Utilities.validateUser(request, db);
		BookmarkXML xml = new BookmarkXML();

		String agent = Utilities.eliminateNull(request.getParameter("agent"));
		boolean externalAgent = agent.equals(Utilities.AGENT_EXT);

		if (user == null && !user.getClientName().equals("admin")) {
			// user not logged in
			if (externalAgent) {
				Utilities.prepareResponseAgent(response);
				xml.constructNodeActionFailure(INVALID_USER_MSG);
			} else {
				Utilities.prepareResponseWeb(response);
				xml.constructWithResultMsg("", INVALID_USER_MSG);
				Utilities.styleXML(xml, "error", user);
			}

		} else if ((user != null && !user.getClientName().equals("admin"))) {

			String submitBtn = request.getParameter("submitBtn");
			String newClientMail = Utilities.eliminateNull(request
					.getParameter("newClientMail"));
			String newPassword = Utilities.eliminateNull(request
					.getParameter("newPassword"));
			String newPassword2 = Utilities.eliminateNull(request
					.getParameter("newPassword2"));
			String oldPassword = Utilities.eliminateNull(request
					.getParameter("oldPassword"));

			String encryptedOldPassword = "";
			try {
				encryptedOldPassword = Utilities.encrypt(oldPassword);
			} catch (Exception ex) {
				Utilities.wrapChipmarkException(ex);
			}

			ArrayList<ResultMessage> resultMsgs = new ArrayList<ResultMessage>();

			if ((submitBtn != null) || externalAgent) {
				updateCode updatePassword = checkUpdatePasswordFields(
						newPassword, newPassword2, resultMsgs);
				updateCode updateEmail = checkEmailField(newClientMail,
						resultMsgs);
				boolean validPassword = encryptedOldPassword.equals(user
						.getClientPass());

				if (validPassword) {

					switch (updateEmail) {
					case ERROR:
						break;
					case UPDATE:
						changeEmail(db, user, newClientMail, resultMsgs);
						break;
					case NO_UPDATE:
						break;
					default:
						break;
					}

					switch (updatePassword) {
					case ERROR:
						break;
					case UPDATE:
						changePassword(db, user, newPassword, newPassword2,
								oldPassword, resultMsgs);
						break;
					case NO_UPDATE:
						break;
					default:
						break;
					}

				} else {
					resultMsgs
							.add(new ResultMessage(
									"current password incorrect - your preferences cannot be updated unless you provide your correct current password",
									"error"));
				}
			}

			// don't pass the password fields back.
			xml.constructWithMultResultMsg(user.getClientName(), resultMsgs,
					newClientMail, "", "", "");

			try {
				if (user.getClientID() != 0)
					Utilities.styleXML(xml, "accountpreferences", user);

			} catch (Exception e) {
				Utilities.wrapChipmarkException(e);
			}

			Utilities.prepareResponseWeb(response);
		}
		// admin Benutzer erkannt NewsletterForm ausgefült und versendet

		String submitBtn = request.getParameter("submitBtn");
		String newsLetterSubject = Utilities.eliminateNull(request
				.getParameter("newsLetterSubject"));
		String newsLetterText = Utilities.eliminateNull(request
				.getParameter("newsLetterText"));

		if ((submitBtn != null && submitBtn.equals("send newsletter"))) {
			@SuppressWarnings("unused")
			ArrayList<String> distinctClientEmails = new ArrayList<String>();

			try {
				distinctClientEmails = db.getDistinctUserEmails();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// read in the properties from the username.properties file
			String from = PropertyManager.email_from;
			// String username = PropertyManager.email_username;
			// String password = PropertyManager.email_password;
			String host = PropertyManager.email_host;
			int port = PropertyManager.email_port;

			Iterator<String> mailIt = distinctClientEmails.listIterator();

			while (mailIt.hasNext()) {

				String userEmail = mailIt.next();

				// ForgotPassword.sendClientInformation(newsLetterText, host,
				// port, username, password, from, userEmail,
				// newsLetterSubject, "newsletter");

				ForgotPassword.sendClientInformation(newsLetterText, host,
						port, from, userEmail, newsLetterSubject, "newsletter");

			}

			try {
				if (user.getClientID() != 0)
					Utilities.styleXML(xml, "main", user);

			} catch (Exception e) {
				Utilities.wrapChipmarkException(e);
			}

			Utilities.prepareResponseWeb(response);

		}

		Utilities.returnResponse(response, xml);
	}

	/**
	 * @param db
	 * @param user
	 * @param newClientMail
	 * @param resultMsgs
	 * @throws ServletException
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private boolean changeEmail(DatabaseWrapper db, ClientEntry user,
			String newClientMail, ArrayList<ResultMessage> resultMsgs)
			throws ServletException {
		try {
			if (db.setClientMailByClientName(newClientMail, user
					.getClientName())) {
				resultMsgs
						.add(new ResultMessage(
								"Your email address was successfully updated",
								"status"));
				user.setClientMail(newClientMail);

				return true;
			} else {
				resultMsgs.add(new ResultMessage(
						"There was an error updating your email address.",
						"error"));
				return false;
			}
		} catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}

		return false;
	}

	/**
	 * @param db
	 * @param user
	 * @param newPassword
	 * @param newPassword2
	 * @param oldPassword
	 * @param resultMsgs
	 * @throws ServletException
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NoSuchAlgorithmException
	 * @throws DigestException
	 * @throws UnsupportedEncodingException
	 */
	private boolean changePassword(DatabaseWrapper db, ClientEntry user,
			String newPassword, String newPassword2, String oldPassword,
			ArrayList<ResultMessage> resultMsgs) throws ServletException {

		try {
			if (newPassword.compareTo(oldPassword) == 0) {
				resultMsgs.add(new ResultMessage(
						"Your password remains unchanged.", "status"));
				return false;
			} else if (newPassword.compareTo(oldPassword) != 0
					&& newPassword2.compareTo(newPassword) == 0) {
				if (db.setClientPassByClientName(newPassword, user
						.getClientName())) {
					resultMsgs
							.add(new ResultMessage(
									"Your password was successfully updated",
									"status"));
					String newEncryptedPassword = Utilities
							.encrypt(newPassword);
					user.setClientPass(newEncryptedPassword);

					return true;
				} else {
					resultMsgs.add(new ResultMessage(
							"There was an error updating your password.",
							"error"));
					return false;
				}
			}
		} catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}

		return false;
	}

	/**
	 * @param successful
	 * @param newClientMail
	 * @param resultMsgs
	 * @return
	 */
	private updateCode checkEmailField(String newClientMail,
			ArrayList<ResultMessage> resultMsgs) {
		newClientMail = newClientMail.trim();

		if (newClientMail != "") {
			if (newClientMail.indexOf("@") == -1) {
				resultMsgs.add(new ResultMessage(INVALID_EMAIL, "error"));
				return updateCode.ERROR;
			} else if (newClientMail.trim().length() < 6) {
				resultMsgs.add(new ResultMessage(INVALID_EMAIL, "error"));
				return updateCode.ERROR;
			} else {
				String email = newClientMail.trim();
				String clientEmail[] = email.split("@");

				// incase: emailaddress@ or
				// email@email@email or
				// @@@@@@
				if (clientEmail.length != 2) {
					resultMsgs.add(new ResultMessage(INVALID_EMAIL, "error"));
					return updateCode.ERROR;
				} else {
					if (clientEmail[0].equals("") || clientEmail[1].equals("")) {
						resultMsgs
								.add(new ResultMessage(INVALID_EMAIL, "error"));
						return updateCode.ERROR;
					} else {
						char last = email.charAt(email.length() - 1);
						if (last == '@') {
							resultMsgs.add(new ResultMessage(INVALID_EMAIL,
									"error"));
							return updateCode.ERROR;
						}
					}

				}
			}
			return updateCode.UPDATE;
		}

		return updateCode.NO_UPDATE;
	}

	/**
	 * 
	 * @param newPassword
	 * @param newPassword2
	 * @param resultMsgs
	 * @return
	 */
	private updateCode checkUpdatePasswordFields(String newPassword,
			String newPassword2, ArrayList<ResultMessage> resultMsgs) {
		// don't check if both fields are empty
		if (newPassword.equals("") && newPassword2.equals("")) {
			return updateCode.NO_UPDATE;
		}
		try {
			PASSWORD_CONSTRAINT.validate(newPassword);
		} catch (ConstraintException ex) {
			switch (ex.getType()) {
			case ConstraintException.ILLEGAL_CHARACTER:
				resultMsgs
						.add(new ResultMessage(
								"There are illegal characters in the new password field.",
								"error"));
				return updateCode.ERROR;
			case ConstraintException.VALUE_ABOVE_MAXIMUM:
				resultMsgs.add(new ResultMessage(
						"A new password must be between "
								+ PASSWORD_CONSTRAINT.getMinLength() + " and "
								+ PASSWORD_CONSTRAINT.getMaxLength()
								+ " characters in length.", "error"));
				return updateCode.ERROR;
			case ConstraintException.VALUE_BELOW_MINIMUM:
				resultMsgs.add(new ResultMessage(
						"A new password must be between "
								+ PASSWORD_CONSTRAINT.getMinLength() + " and "
								+ PASSWORD_CONSTRAINT.getMaxLength()
								+ " characters in length.", "error"));
				return updateCode.ERROR;
			default:
				resultMsgs.add(new ResultMessage(
						"There was an error with your new password.", "error"));
				return updateCode.ERROR;
			}
		}

		if (newPassword.equals(newPassword2) == false) {
			resultMsgs.add(new ResultMessage("Your new passwords do no agree.",
					"error"));
			return updateCode.ERROR;
		}

		return updateCode.UPDATE;
	}
}
