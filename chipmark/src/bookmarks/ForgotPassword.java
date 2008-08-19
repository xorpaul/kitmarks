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
import java.util.Properties;
import java.util.Random;
import java.util.ArrayList;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.mail.smtp.SMTPTransport;

/**
 * This Servlet is responsible for resetting a lost of forgotten password. It
 * will only work if the user did include the (optional) email address when he
 * or she first created the account.
 * 
 * @author Jon
 * 
 */
public class ForgotPassword extends HttpServlet {

	/**
	 * This is a unique identifier for this servlet.
	 */
	private static final long serialVersionUID = 18768646509853L;

	/**
	 * This is the Subject of the email sent to the client.
	 */
	private static final String subject = "Chipmark Forgotten Username or Password";

	/**
	 * This string is the first half of the email message sent to the client.
	 * message = messageHead + messageBody + messageTail
	 */
	private static final String messageHead = "This email has been sent to you due to a request "
			+ "for a lost username or password for the Chipmark " + "service.";

	/**
	 * This is the last half of the email message to the client sent on after
	 * resetting the password. message = messageHead + messageBody + messageTail
	 */
	private static final String messageTail = "\n \nThank you for using Chipmark!";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// initialize the request
		Utilities.prepareRequest(request);

		// read in the forum parameters
		String userEmail = Utilities.eliminateNull((String) request
				.getParameter("clientMail"));
		String agent = Utilities.eliminateNull((String) request
				.getParameter("agent"));
		String resetPassword = Utilities.eliminateNull((String) request
				.getParameter("resetPassword"));
		String retrieveUsername = Utilities.eliminateNull((String) request
				.getParameter("retrieveUsername"));

		// read in the properties from the username.properties file
		String from = PropertyManager.email_from;
//		String username = PropertyManager.email_username;
//		String password = PropertyManager.email_password;
		String host = PropertyManager.email_host;
		int port = PropertyManager.email_port;

		// get db
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		boolean successful = false;
		String messageBody = "";
		String clientName = null;

		// Get clientName
		if (getClientName(userEmail, db) != null) {
			clientName = getClientName(userEmail, db);
		}
		ClientEntry user = Utilities.validateUser(request, db);

		/*
		 * NOTE: The plugins don't currently have checkboxes like the web
		 * interface. For now the the default action when a Forgot Password
		 * request is sent from a plugin it will e-mail the user both their
		 * username and the reset password.
		 */
		if (retrieveUsername.equals("on") || agent.equals("ext")) {

			if (clientName != null) {
				messageBody += addClientNameToMessageBody(clientName);
				successful = true;
			} else {
				successful = false;
			}
		}
		if (resetPassword.equals("on") || agent.equals("ext")) {

			if (clientName != null) {
				successful = true;
			} else {
				successful = false;
			}

			if (successful) {
				// Generate the new password
				String newPassword = generateRandomPassword();

				// change password of user
				if (clientName.indexOf("/") == -1)
					successful = changeUsersPassword(userEmail, newPassword, db);
				else
					successful = false;

				if (successful) {
					messageBody += addPasswordToMessageBody(newPassword);
				}
			}
		}

		// If successful, send email for client informing him/her of new
		// password
		if (successful) {

			successful = successful
			&& sendClientInformation(messageBody, host, port, from, userEmail, subject, "forgotPW");
			
			
			
//					&& sendClientInformation(messageBody, host, port, username,
//							password, from, userEmail, subject, "forgotPW");
		}

		// Construct report for user
		String reportToUser = "";
		if (successful) {
			reportToUser = "SUCCESS";
		} else {
			if (clientName != null && clientName.indexOf("/") != -1)
				if (user.getPreferredLang() == "eng")

					reportToUser = "User with an ADS account cant make use of this function,"
							+ " please contact the ADS account management!";
				else
					reportToUser = "Benutzer mit ADS Account können die Passwort vergessen Funktion dieser Anwendungen nicht verwenden, "
							+ "bitte wenden Sie sich an die richtige Stelle um ihr ADS Account Passwort zu ändern!";
			else {
				if (user.getPreferredLang() == "eng")
					reportToUser = "Failed to retrieve user information for account with email "
							+ userEmail;
				else

					reportToUser = "Es konnte kein Konto mit dieser eMailadresse in der Datenbank gefunden werden "
							+ userEmail;
			}
		}
		// prepare XML response
		BookmarkXML xml = new BookmarkXML();
		xml.constructWithResultMsg("", reportToUser);
		if (agent.equals(Utilities.AGENT_EXT)) {
			Utilities.prepareResponseAgent(response);
		} else {
			ClientEntry userInfo = Utilities.validateUser(request, db);

			Utilities.prepareResponseWeb(response);
			Utilities.styleXML(xml, "forgottenusernamepassword", userInfo);
		}

		// send XML response
		Utilities.returnResponse(response, xml);
		return;
	}

	/**
	 * Sends an email to the client informing him or her of his or her new
	 * password.
	 * 
	 * @param newPassword
	 *            , the new password for the client
	 * @param host
	 *            , the host of the SMTP server
	 * @param port
	 *            , the port of the SMTP server
	 * @param username
	 *            for SMTP authentication
	 * @param password
	 *            , password for SMTP authentication
	 * @param from
	 *            , whom the email should appear by
	 * @param userEmail
	 *            , end user's email (TO of the email)
	 * @param subject2
	 *            , subject of the email
	 * @return
	 */
	public static boolean sendClientInformation(String messageBody,
			String host, int port, String from, String userEmail,
			String subject2, String action) {

		// public static boolean sendClientInformation(String messageBody,
		// String host, int port, String username, String password,
		// String from, String userEmail, String subject2, String action) {

		String content = null;

		if (action.equals("forgotPW")) {
			content = constructMessage(messageBody);
		} else {
			content = messageBody;
		}

		try {
			// initialize properties
			Properties prop = new Properties();
			prop.put("mail.transport.protocol", "smtp");
			prop.put("mail.smtp.host", host);
			// prop.setProperty("mail.smtp.socketFactory.class",
			// "javax.net.ssl.SSLSocketFactory");
			// prop.put("mail.smtp.user", username);
			prop.put("mail.smtp.port", port);
			prop.put("mail.smtp.auth", "false");

			// initialize mail objects
			// Authenticator myAuth = new Auth();
			Session mailConnection = Session.getInstance(prop, null);
			// URLName myurl = new URLName("smtp", host, port, null, username,
			// password);
			Message message = new MimeMessage(mailConnection);
			Address sendingTo = new InternetAddress(userEmail);
			Address myaddress = new InternetAddress(from, "Chipmark");
			// SMTPTransport tr = new SMTPTransport(mailConnection, myurl);

			// construct message
			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO, sendingTo);
			message.setFrom(myaddress);
			message.setSubject(subject2);
			message.setContent(content, Utilities.EMAIL_CONTENT_TYPE);

			// send message

			Transport.send(message);

			// tr.connect(host, port, username, password);
			// message.saveChanges();
			// tr.sendMessage(message, message.getAllRecipients());
			// tr.close();
		} catch (Exception e) {
			Utilities.ignoreChipmarkException(e);
			return false;
		}
		return true;
	}

	/**
	 * This constructs the body of the email sent to the client for the
	 * ForgotPassword servlet.
	 * 
	 * @param newPassword
	 *            , the client's new password.
	 * @return returns the content of the email message in the form of a String
	 */
	private static String constructMessage(String messageBody) {
		return messageHead + messageBody + messageTail;
	}

	private String addPasswordToMessageBody(String newPassword) {
		String messageBody = "\n\nAny usernames you have registered with this e-mail account will be "
				+ "affected by this password change.\n";
		messageBody += "Your new password is: " + newPassword + "\n";
		return messageBody;
	}

	private String addClientNameToMessageBody(String clientNames) {
		String messageBody = "\n\nBelow are all the usernames registered to this e-mail account.\n";
		messageBody += "Username(s): " + clientNames + "\n";
		return messageBody;
	}

	/**
	 * This method changes the user's password.
	 * 
	 * @param userEmail
	 *            , used to identify user's account
	 * @param newPassword
	 *            , the user's new password
	 * @param db
	 *            , connection to the database
	 * @return returns true on success, false on failed attempt
	 * @throws ServletException
	 */
	private boolean changeUsersPassword(String userEmail, String newPassword,
			DatabaseWrapper db) throws ServletException {
		try {
			userEmail = userEmail.trim();
			if (userEmail.length() > 0 && userEmail != null) {
				return db.setClientPassByClientMail(newPassword, userEmail);
			}
		} catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}
		return false;
	}

	/**
	 * This method retrieves username based on e-mail account
	 * 
	 * @param userEmail
	 *            , user's e-mail account, used for identification
	 * @param db
	 *            , connection to the database
	 * @return returns true on success, false on fail
	 * @throws ServletException
	 */
	private String getClientName(String userEmail, DatabaseWrapper db)
			throws ServletException {
		try {
			userEmail = userEmail.trim();
			if (userEmail.length() > 0 && userEmail != null) {
				if (db.getUserByMail(userEmail) != null) {
					ArrayList<String> userNames = db.getUserByMail(userEmail);
					// Clean up usernames and return
					return userNames.toString().substring(1,
							userNames.toString().length() - 1);
				}
			}
		} catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}
		return null;
	}

	/**
	 * Used to generate a random 8 character password.
	 * 
	 * @return random password String of 8 characters.
	 */
	private String generateRandomPassword() {
		String newPassword = "";
		Random rand = new Random();
		for (int i = 0; i < 8; i++) {

			int type = rand.nextInt(3);
			int randNum = 0;
			String appendChar;
			if (type == 0) {
				// This will correspond to a number (0-9)
				randNum = rand.nextInt(10);
				newPassword = newPassword + randNum;
			}
			if (type == 1) {
				// This will correspond to a capital letter (A-Z)
				randNum = rand.nextInt(26) + 65;
				appendChar = new Character((char) randNum).toString();
				newPassword = newPassword + appendChar;
			}
			if (type == 2) {
				// This will correspond to a lower case letter (a-z)
				randNum = rand.nextInt(26) + 97;
				appendChar = new Character((char) randNum).toString();
				newPassword = newPassword + appendChar;
			}
		}
		return newPassword;
	}
}

/**
 * This is a helper class, which aides in the authentication method
 * SendClientNewPassword
 * 
 * @author Jon
 * 
 */
class Auth extends javax.mail.Authenticator {

	public Auth() {
		super();
	}

	// public PasswordAuthentication getPasswordAuthentication() {
	// String username = PropertyManager.email_username;
	// String password = PropertyManager.email_password;
	// return new PasswordAuthentication(username, password);
	// }
}
