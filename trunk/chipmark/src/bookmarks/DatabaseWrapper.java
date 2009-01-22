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

/* NOTES:
 * If the indentation looks funny, you may need to set your tab size to 4
 * (in vi do ":set ts=4")
 *
 * When adding new code to this class, please be sure to follow the model
 * that the existing methods follow when using database connections.
 *
 * CODE GUIDELINES:
 * - try/finally blocks guarantee connections get closed
 * - Only have one ResultSet/PreparedStatement open per connection at any one time
 * - ResultSet and PreparedStatement names should end with the connection number,
 *   followed by a letter [a-z] if more than one is used.
 *    Example: If only one statement is used for conn1, its name would be named stmt1 and its
 *             ResultSet would be rs1
 *             If there were multiple statments for conn1 (each closed before the next is created!!)
 *             then they would be named stmt1a, stmt1b, stmt1c, etc. and the ResultSets
 *             would be named rs1a, rs1b, rs1c, etc.  See the code for more examples!
 *
 * - Use muliple connection if you need more than one Statement/ResultSet open at a time
 * - Use getConn() to get connection rather than acquireing them directly
 *    (See notes about JUnit below to understand why)
 */

package bookmarks;

import java.io.UnsupportedEncodingException;
import java.rmi.AlreadyBoundException;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.security.auth.login.LoginException;

@SuppressWarnings("unchecked")
class KeyBookmarkPair implements Comparable {
	public int key;
	public Bookmark value;

	public KeyBookmarkPair(int newKey, Bookmark newValue) {
		key = newKey;
		value = newValue;
	}

	public int compareTo(Object o) {
		if (o.getClass().getName().equals(this.getClass().getName())) {
			return ((KeyBookmarkPair) o).key - key;
		} else {
			return -1;
		}
	}
}

/**
 * Wraps an SQL database connection in convenient methods.
 * 
 * @author schwerdf, andyk
 */
public class DatabaseWrapper {

	/**
	 * This is a regex used in the parsing of search criteria for the function
	 * getBookmarkSet(). Change this and all implementing functions will change
	 * accordingly.
	 */
	public final String CRITERIASEPARATOR = "=>";

	/**
	 * The max number of chipmarks a user can have.
	 */
	public static final int MAXCHIPMARKS = 5000;

	/**
	 * The maximum size of the inbox
	 */
	private static final int MAXINBOXSIZE = 100;

	/**
	 * This will be the one instance of the database wrapper shared by all
	 * classes.
	 */
	private static DatabaseWrapper databaseWrapper;

	/**
	 * Here we instantiate the single instance of DatabaseWrapper. We
	 * instantiate here rather than in the retrieval method to avoid dealing
	 * with synchronization issues.
	 */
	static {

		DatabaseWrapper.databaseWrapper = new DatabaseWrapper();

	}

	/**
	 * This is how database wrapper instances should be retrieved from now on.
	 * 
	 * @return A database wrapper instance.
	 */
	public static DatabaseWrapper getDatabaseWrapper() {

		return DatabaseWrapper.databaseWrapper;
	}

	/**
	 * Get all bookmarks belonging to a certain user
	 * 
	 * @param user
	 *            The ClientEntry object for the logged in user
	 * @return an ArrayList of all the user's bookmarks
	 * @author andyk
	 */
	public ArrayList<Bookmark> getAllUserBookmarks(ClientEntry user)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		Connection conn2 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		try { // see finally that closes DB conection

			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("(SELECT link.* FROM link LEFT JOIN folder_subscription ON folderID = linkFolderParentID WHERE folder_subscription.clientID= ? AND folder_subscription.accepted = 'true') UNION (SELECT * FROM link WHERE link.linkClientID = ?) ORDER BY linkSortOrder DESC");
			int clientID = user.getClientID();
			stmt1.setInt(1, clientID);
			stmt1.setInt(2, clientID);
			// The return ArrayList
			ArrayList<Bookmark> rv = new ArrayList<Bookmark>();

			// The result set
			rs1 = stmt1.executeQuery();

			conn2 = ConnectionPool.getConn();
			stmt2 = conn2
					.prepareStatement("SELECT * FROM label WHERE labelLinkID = ?");

			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {
				// Get all its data
				int linkID = rs1.getInt("linkID");
				int linkClientID = rs1.getInt("linkClientID");
				String linkName = rs1.getString("linkName");
				String linkURL = rs1.getString("linkURL");
				String linkPermission = rs1.getString("linkPermission");
				String linkDescription = rs1.getString("linkDescription");
				int linkFolderParentID = rs1.getInt("linkFolderParentID");
				int toolbarPosition = rs1.getInt("toolbarPosition");

				// Query the label table to find out what labels the bookmark
				// has
				stmt2.setInt(1, linkID);
				rs2 = stmt2.executeQuery();

				// Create the bookmark object to add to the list
				Bookmark toAdd = new Bookmark(linkID, linkClientID, linkName,
						linkURL, linkPermission, linkDescription,
						linkFolderParentID, toolbarPosition);

				// Get all the bookmark's labels and stick them in the bookmark
				// object
				String labelName = null;
				for (boolean truth = rs2.first(); truth; truth = rs2.next()) {
					labelName = rs2.getString("labelName");
					toAdd.addLabel(labelName);
				}

				// Add the bookmark
				rv.add(toAdd);
			}

			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
			if (conn2 != null) {
				conn2.close();
			}
		}
	}

	/**
	 * Sorts a bookmark list by the number of hits it has against the
	 * labelNames. All bookmarks that do not have any labelName hits are
	 * removed.
	 * <p>
	 * This function effectively filters an ArrayList of bookmarks, returning
	 * only those that have a label from the labelNames
	 * 
	 * @param bookmarks
	 *            An ArrayList of Bookmarks that are being filtered on
	 * @param labelNames
	 *            An ArrayList of labelNames
	 * @author schwerdf
	 */
	@SuppressWarnings("unchecked")
	private void sortResultsByLabels(ArrayList<Bookmark> bookmarks,
			ArrayList labelNames) throws SQLException, NoSuchFieldException,
			IllegalAccessException {
		ArrayList<KeyBookmarkPair> toSort = new ArrayList<KeyBookmarkPair>();
		for (Iterator it = bookmarks.iterator(); it.hasNext();) {
			Bookmark current = (Bookmark) it.next();
			toSort.add(new KeyBookmarkPair(current.labelMatches(labelNames),
					current));
		}
		java.util.Collections.sort(toSort);
		bookmarks.clear();
		for (Iterator it = toSort.iterator(); it.hasNext();) {
			KeyBookmarkPair current = (KeyBookmarkPair) it.next();
			if (current.key > 0) {
				bookmarks.add(current.value);
			}
		}
	}

	/**
	 * Checks whether a username is already used in the DB
	 * 
	 * @param username
	 *            String of the user you are checking for
	 * @return true if user exists, false if not
	 * @author mcassano
	 */
	public boolean checkUserExists(String username) throws SQLException,
			NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT count(clientID) as usercount FROM client where clientName = ?");
			stmt1.setString(1, username);

			rs1 = stmt1.executeQuery();
			int numResults;
			if (rs1.first()) {
				numResults = rs1.getInt("usercount");
			} else {
				return false;
			}

			if (numResults == 1) {
				return true;
			} else {
				return false;
			}
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Removes a user from the database
	 * 
	 * @param user
	 *            ClientEntry of the user you would like to delete
	 * @author mcassano
	 */
	public void deleteClient(ClientEntry user) throws SQLException,
			NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			// cascading delete should remove all of the user's data from the
			// other tables
			stmt1 = conn1
					.prepareStatement("DELETE FROM client WHERE clientID = ? LIMIT 1");
			stmt1.setInt(1, user.getClientID());
			stmt1.executeUpdate();

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Searches all bookmarks belonging to a certain user. The search is an
	 * exact match of the given strings or wildcards; no tokenizing is done.
	 * 
	 * @param user
	 *            ClientEntry object for the user whos bookmarks we are
	 *            searching
	 * @param linkName
	 *            The linkName to match, empty string if not being used
	 * @param linkURL
	 *            The linkURL to match, empty string is not being used
	 * @param linkPermission
	 *            'public' or 'private' for searching permissions, empty string
	 *            if not being used
	 * @param linkDescription
	 *            The linkDescription to match, empty string if not being used
	 * @param labelNames
	 *            An arraylist of labelnames. The match is done in an "OR"
	 *            fashion
	 * @return An ArrayList of bookmarks, sorted in descending order of the
	 *         number of labels it matches in labelNames.
	 * @author schwerdf
	 */
	@SuppressWarnings( { "unchecked" })
	public ArrayList<Bookmark> searchUserBookmarks(ClientEntry user,
			String linkName, String linkURL, String linkPermission,
			String linkDescription, ArrayList labelNames) throws SQLException,
			NoSuchFieldException, IllegalAccessException {

		if (linkName.equals("")) {
			linkName = "%";
		}
		if (linkURL.equals("")) {
			linkURL = "%";
		}
		if (linkPermission.equals("")) {
			linkPermission = "%";
		}
		if (linkDescription.equals("")) {
			linkDescription = "%";
		}
		ArrayList<Bookmark> rv = getBookmarkSet(
				"linkClientID = ? AND linkName LIKE ? AND linkURL LIKE ? AND linkPermission LIKE ? AND linkDescription LIKE ?",
				String.valueOf(user.getClientID()) + CRITERIASEPARATOR
						+ linkName + CRITERIASEPARATOR + linkURL
						+ CRITERIASEPARATOR + linkPermission
						+ CRITERIASEPARATOR + linkDescription);
		if (labelNames != null) {
			sortResultsByLabels(rv, labelNames);
		}
		return rv;
	}

	/**
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public String getClientIdByClientName(String username) throws SQLException,
			NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			String clientId = "-1";
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientId FROM client WHERE clientName = ?");
			stmt1.setString(1, username);
			rs1 = stmt1.executeQuery();
			if (rs1.first()) {
				// If a matching ID was found
				clientId = rs1.getString("clientId");
			}
			return clientId;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	public ArrayList<Bookmark> searchLinks(String searchTerm, String action)
			throws SQLException {
		return searchLinks(searchTerm, action, -1);
	}

	public ArrayList<Bookmark> searchLinks(String searchTerm, String action,
			int clientID) throws SQLException {

		String userClause;
		ArrayList<Bookmark> foundBookmarks = new ArrayList<Bookmark>();

		// if (searchTerm.length() < 3)
		// return foundBookmarks;

		if (!action.equals("searchAllUserLinks"))
			userClause = " AND linkPermission = 'public' ";
		else
			userClause = " AND link.linkClientID = '" + clientID + "' ";

		// limit query to 200 results
		// String query2 = "SELECT link.linkID, link.linkName, link.linkURL, "
		// +
		// "link.linkDescription, link.linkClientID, linkPermission, linkLastMod,"
		// +
		// "linkFolderParentID, link.toolbarPosition, GROUP_CONCAT(labelName) AS labels FROM linktext "
		// + "LEFT JOIN link USING(linkID) "
		// + "LEFT JOIN label ON labelLinkID = link.linkID "
		// +
		// "WHERE MATCH (linktext.linkName,linktext.linkURL,linktext.linkDescription) "
		// + "AGAINST ('" + searchTerm + "' IN BOOLEAN MODE)  "
		// + userClause + "GROUP BY link.linkID LIMIT 200";

		String query = "SELECT COUNT(link.linkID) as \"SearchCount\", link.linkID, link.linkName, link.linkURL, "
				+ "link.linkDescription, link.linkClientID, linkPermission, linkLastMod,"
				+ "linkFolderParentID, link.toolbarPosition, GROUP_CONCAT(labelName) AS labels FROM linktext "
				+ "LEFT JOIN link USING(linkID) "
				+ "LEFT JOIN label ON labelLinkID = link.linkID "
				+ "WHERE (linktext.linkName LIKE '"
				+ searchTerm
				+ "'"
				+ " OR linktext.linkName LIKE '%"
				+ searchTerm
				+ "'"
				+ " OR linktext.linkName LIKE '"
				+ searchTerm
				+ "%'"
				+ " OR linktext.linkName LIKE '"
				+ searchTerm
				+ "'"
				+ " OR linktext.linkName LIKE '%"
				+ searchTerm
				+ "'"
				+ " OR linktext.linkName LIKE '"
				+ searchTerm
				+ "%'"
				+ " OR linktext.linkURL LIKE '"
				+ searchTerm
				+ "'"
				+ " OR linktext.linkURL LIKE '%"
				+ searchTerm
				+ "'"
				+ " OR linktext.linkURL LIKE '"
				+ searchTerm
				+ "%'"
				+ " OR linktext.linkDescription LIKE '"
				+ searchTerm
				+ "'"
				+ " OR linktext.linkDescription LIKE '%"
				+ searchTerm
				+ "'"
				+ " OR linktext.linkDescription LIKE '"
				+ searchTerm
				+ "%')" + userClause + " GROUP BY link.linkID LIMIT 0, 200;";
		// + "ORDER BY my_column LIKE '" + searchTerm + "' DESC,"
		// + " my_column LIKE '" + searchTerm + "%' DESC,"
		// + " my_column LIKE '%" + searchTerm + "' DESC";

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;

		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1.prepareStatement(query);
			// stmt1.setString(1, searchTerm);

			rs1 = stmt1.executeQuery();

			for (boolean more = rs1.first(); more; more = rs1.next()) {
				int linkID = rs1.getInt("linkID");
				int linkClientID = rs1.getInt("linkClientID");
				String linkName = rs1.getString("linkName");
				String linkURL = rs1.getString("linkURL");
				String linkPermission = rs1.getString("linkPermission");
				String linkDescription = rs1.getString("linkDescription");
				String labels = Utilities
						.eliminateNull(rs1.getString("labels"));
				int linkFolderParentID = rs1.getInt("linkFolderParentID");
				int toolbarPosition = rs1.getInt("toolbarPosition");
				int searchCount = rs1.getInt("SearchCount");
				Bookmark bm = new Bookmark(linkID, linkClientID, linkName,
						linkURL, linkPermission, linkDescription,
						linkFolderParentID, toolbarPosition);

				StringTokenizer st = new StringTokenizer(labels, ",");

				while (st.hasMoreTokens()) {
					String s = st.nextToken();
					bm.addLabel(s);
				}

				foundBookmarks.add(bm);
			}
			return foundBookmarks;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}

	}

	/**
	 * Gets all bookmarks in a given folder. Presently used only in the JUnit
	 * testing framework.
	 * 
	 * @param user
	 * @param folderID
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList getBookmarksInFolder(ClientEntry user, int folderID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		return getBookmarkSet("linkClientID = ? AND linkFolderParentID = ?",
				user.getClientID() + CRITERIASEPARATOR + folderID);
	}

	/**
	 * A back-end for all bookmark-retrieval functions. Takes in a set of
	 * criteria for bookmarks and returns a set of bookmarks matching the
	 * criteria.
	 * 
	 * @param preparedStatement
	 *            SQL code, which will be prepended with
	 *            "SELECT * FROM link WHERE " and fed into a PreparedStatement
	 *            object.
	 * @param criteria
	 *            The strings to be inserted into the PreparedStatement's
	 *            "question mark" fields, separated by the token
	 *            DatabaseWrapper.CRITERIASEPARATOR
	 * @return Bookmarks matching the given criteria
	 * @author schwerdf
	 */
	private ArrayList<Bookmark> getBookmarkSet(String preparedStatement,
			String criteria) throws SQLException, NoSuchFieldException,
			IllegalAccessException {
		Connection conn1 = null;
		Connection conn2 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1.prepareStatement("SELECT * FROM link WHERE "
					+ preparedStatement);

			// Split the criteria along the token
			// DatabaseWrapper.CRITERIASEPARATOR
			String[] criteriaA = criteria.split(CRITERIASEPARATOR);

			// Count the number of fields to be filled in for the
			// PreparedStatement
			int numberMarks = 0;
			for (int i = 0; i < preparedStatement.length(); i++) {
				if (preparedStatement.charAt(i) == '?') {
					numberMarks++;
				}
			}
			// If the number does not match the number of criteria given, bomb
			// out
			if (numberMarks != criteriaA.length) {
				throw new NoSuchFieldException(
						"Number of criteria specified and number given do not match");
			}

			// Feed all the criteria into the PreparedStatement:
			for (int i = 0; i < criteriaA.length; i++) {
				// If a criterion is a number, put it in as a number
				if (criteriaA[i].matches("[0-9]+")) {
					stmt1.setInt(i + 1, Integer.parseInt(criteriaA[i]));
				}
				// Else put it in as a string
				else {
					stmt1.setString(i + 1, criteriaA[i]);
				}
			}

			// The return ArrayList
			ArrayList<Bookmark> rv = new ArrayList<Bookmark>();

			// The result set
			rs1 = stmt1.executeQuery();

			// For all bookmarks in the result set (i.e. all bookmarks owned
			// by the given user)
			conn2 = ConnectionPool.getConn();
			stmt2 = conn2
					.prepareStatement("SELECT * FROM label WHERE labelLinkID = ?");
			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {
				// Get all its data
				int linkID = rs1.getInt("linkID");
				int linkClientID = rs1.getInt("linkClientID");
				String linkName = rs1.getString("linkName");
				String linkURL = rs1.getString("linkURL");
				String linkPermission = rs1.getString("linkPermission");
				String linkDescription = rs1.getString("linkDescription");
				int linkFolderParentID = rs1.getInt("linkFolderParentID");
				int toolbarPosition = rs1.getInt("toolbarPosition");

				// Query the label table to find out what labels the bookmark
				// has - note that we repeatedly reuse stmt2 here
				stmt2.setInt(1, linkID);
				rs2 = stmt2.executeQuery();

				// Create the bookmark object to add to the list
				Bookmark toAdd = new Bookmark(linkID, linkClientID, linkName,
						linkURL, linkPermission, linkDescription,
						linkFolderParentID, toolbarPosition);

				// Get all the bookmark's labels and stick them in the bookmark
				// object
				String labelName = null;
				for (boolean truth = rs2.first(); truth; truth = rs2.next()) {
					labelName = rs2.getString("labelName");
					toAdd.addLabel(labelName);
				}

				// Add the bookmark
				rv.add(toAdd);
			}

			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
			if (conn2 != null) {
				conn2.close();
			}
		}
	}

	/**
	 * Gets a single bookmark by its database ID
	 * 
	 * @param user
	 *            The ClientEntry object of the user that is accessing the
	 *            bookmark
	 * @param bookmarkID
	 *            int of the id of the bookmark from the database
	 * @return The bookmark
	 * @author schwerdf
	 */
	public Bookmark getBookmark(ClientEntry user, int bookmarkID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1a = null;
		ResultSet rs1a = null;
		PreparedStatement stmt1b = null;
		ResultSet rs1b = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1a = conn1
					.prepareStatement("SELECT * FROM link WHERE linkID = ? AND (linkClientID = ? OR linkPermission = 'public')");
			stmt1a.setInt(1, bookmarkID);
			stmt1a.setInt(2, user.getClientID());

			// Execute the queries
			rs1a = stmt1a.executeQuery();

			// If no bookmark was found with the given ID, bomb out
			if (!rs1a.first()) {
				throw new NoSuchFieldException(
						"No accessible bookmark with ID " + bookmarkID
								+ " found in table");
			}

			// Get the bookmark's data from the database entry
			int linkClientID = rs1a.getInt("linkClientID");
			String linkName = rs1a.getString("linkName");
			String linkURL = rs1a.getString("linkURL");
			String linkPermission = rs1a.getString("linkPermission");
			String linkDescription = rs1a.getString("linkDescription");
			int linkFolderParentID = rs1a.getInt("linkFolderParentID");
			int toolbarPosition = rs1a.getInt("toolbarPosition");

			// Encapsulate it in a bookmark object
			Bookmark rv = new Bookmark(bookmarkID, linkClientID, linkName,
					linkURL, linkPermission, linkDescription,
					linkFolderParentID, toolbarPosition);

			if (stmt1a != null) {
				stmt1a.close();
			}

			// The statement to get all of a bookmark's labels from the label
			// table
			stmt1b = conn1
					.prepareStatement("SELECT * FROM label WHERE labelLinkID = ?");
			stmt1b.setInt(1, bookmarkID);
			rs1b = stmt1b.executeQuery();
			// Get all the bookmark's labels and put them in the bookmark object
			String labelName = null;
			for (boolean truth = rs1b.first(); truth; truth = rs1b.next()) {
				labelName = rs1b.getString("labelName");
				rv.addLabel(labelName);
			}

			return rv;

		} finally {
			if (stmt1a != null) {
				stmt1a.close();
			}
			if (stmt1b != null) {
				stmt1b.close();
			}
			if (rs1a != null) {
				rs1a.close();
			}
			if (rs1b != null) {
				rs1b.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Gets a user-owned single bookmark by its database ID
	 * 
	 * @param user
	 *            The ClientEntry object of the user that owns the bookmark
	 * @param bookmarkID
	 *            int of the id of the bookmark from the database
	 * @return The bookmark
	 * @author schwerdf
	 */
	public Bookmark getUserBookmark(ClientEntry user, int bookmarkID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1a = null;
		ResultSet rs1a = null;
		PreparedStatement stmt1b = null;
		ResultSet rs1b = null;
		try {
			// The statement to get the bookmark data from the link table
			conn1 = ConnectionPool.getConn();
			stmt1a = conn1
					.prepareStatement("SELECT * FROM link WHERE linkID = ? AND linkClientID = ?");
			stmt1a.setInt(1, bookmarkID);
			stmt1a.setInt(2, user.getClientID());

			// Execute the queries
			rs1a = stmt1a.executeQuery();

			// If no bookmark was found with the given ID, bomb out
			if (!rs1a.first()) {
				throw new NoSuchFieldException("Error: No bookmark with ID "
						+ bookmarkID + " owned by user #" + user.getClientID()
						+ " was found!");
			}

			// Get the bookmark's data from the database entry
			int linkClientID = rs1a.getInt("linkClientID");
			String linkName = rs1a.getString("linkName");
			String linkURL = rs1a.getString("linkURL");
			String linkPermission = rs1a.getString("linkPermission");
			String linkDescription = rs1a.getString("linkDescription");
			int linkFolderParentID = rs1a.getInt("linkFolderParentID");
			int toolbarPosition = rs1a.getInt("toolbarPosition");

			// Encapsulate it in a bookmark object
			Bookmark rv = new Bookmark(bookmarkID, linkClientID, linkName,
					linkURL, linkPermission, linkDescription,
					linkFolderParentID, toolbarPosition);

			if (stmt1a != null) {
				stmt1a.close();
			}
			// The statement to get all of a bookmark's labels from the label
			// table
			stmt1b = conn1
					.prepareStatement("SELECT * FROM label WHERE labelLinkID = ?");
			stmt1b.setInt(1, bookmarkID);
			rs1b = stmt1b.executeQuery();
			// Get all the bookmark's labels and put them in the
			// bookmark object
			String labelName = null;
			for (boolean truth = rs1b.first(); truth; truth = rs1b.next()) {
				labelName = rs1b.getString("labelName");
				rv.addLabel(labelName);
			}

			return rv;

		} finally {
			if (stmt1a != null) {
				stmt1a.close();
			}
			if (rs1a != null) {
				rs1a.close();
			}
			if (stmt1b != null) {
				stmt1b.close();
			}
			if (rs1b != null) {
				rs1b.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 *Updates the sort order for the specified user.
	 * 
	 *@param user
	 *            The Client Entry object for the user
	 *@param sortArray
	 *            Ordered list of linkIDs to insert into sort table
	 *@author andyk
	 */
	public void setSortOrder(ClientEntry user, int sortArray[])
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			int sortArraySize = sortArray.length;
			if (sortArraySize < 1) {
				throw new NoSuchFieldException("User '" + user.getClientName()
						+ "' sent an empty sort string!");
			}

			conn1 = ConnectionPool.getConn();

			conn1.setAutoCommit(false);

			stmt1 = conn1
					.prepareStatement("UPDATE link SET linkSortOrder = ? WHERE linkID = ? AND linkClientID = ?");

			for (int position = 0; position < sortArraySize; position++) {
				// ignore ID of zero, means folder.
				if (sortArray[position] == 0) {
					continue;
				}
				stmt1.setInt(1, sortArraySize - position);
				stmt1.setInt(2, sortArray[position]);
				stmt1.setInt(3, user.getClientID());

				try {
					stmt1.executeUpdate();
				} catch (SQLException sqlEx) {
					// Ran into problems... undo all previous changes
					conn1.rollback();
					// Let the servlet know what happened
					throw new SQLException(
							"Sort table INSERT failed for clientID:"
									+ user.getClientID() + " linkID:"
									+ sortArray[position]
									+ " - Transaction Aborted");
				}
			}

			// DB will execute all statements as one transaction block at this
			// commit
			conn1.commit();
			conn1.setAutoCommit(true);

			return;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.setAutoCommit(true);
				conn1.close();
			}
		}
	}

	/**
	 * Deletes a bookmark by its database ID
	 * 
	 * @param user
	 *            The ClientEntry object of the user that owns the bookmark
	 * @param bookmarkID
	 *            int of the id of the bookmark from the database
	 * @author schwerdf
	 */
	public void deleteBookmark(ClientEntry user, int linkID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;

		try {

			conn1 = ConnectionPool.getConn();

			Bookmark bm = getBookmark(user, linkID);

			String linkURL = bm.getLinkURL();

			updateTableURLEntry(linkURL, "delete");

			stmt1 = conn1
					.prepareStatement("DELETE FROM link WHERE linkID = ? AND linkClientID = ?");
			stmt1.setInt(1, linkID);
			stmt1.setInt(2, user.getClientID());

			stmt1.executeUpdate();

			stmt1 = conn1
					.prepareStatement("DELETE FROM linktext WHERE linkID = ?");
			stmt1.setInt(1, linkID);

			stmt1.executeUpdate();

			stmt1 = conn1
					.prepareStatement("DELETE FROM label WHERE labelLinkID = ?");
			stmt1.setInt(1, linkID);

			stmt1.executeUpdate();

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * @param linkURL
	 *            The URL of a link (e.g.: http://www.fzk.de)
	 * 
	 * @author Andreas Paul
	 */

	public void updateTableURLEntry(String linkURL, String action)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;

		int urlCount = 1;

		try {

			conn = ConnectionPool.getConn();

			// Test if the new url is already in the url table

			stmt = conn.prepareStatement("SELECT urlCount FROM url "
					+ "WHERE url = ?");

			stmt.setString(1, linkURL);
			rs = stmt.executeQuery();

			if (action.equals("delete")) {
				if (rs.first()) {
					urlCount = rs.getInt("urlCount");

					if (urlCount == 1) {
						stmt = conn
								.prepareStatement("DELETE FROM url WHERE url = ? ");
						stmt.setString(1, linkURL);
					} else {
						stmt = conn
								.prepareStatement("UPDATE url SET urlCount = ? WHERE url = ? ");

						stmt.setInt(1, (urlCount - 1));
						stmt.setString(2, linkURL);
					}

					stmt.executeUpdate();

				} else {
					String njee = null;
				}
			}
			if (action.equals("add")) {
				if (rs.first()) {
					stmt = conn
							.prepareStatement("UPDATE url SET urlCount = ? WHERE url = ? ");

					stmt.setInt(1, (urlCount + 1));
					stmt.setString(2, linkURL);
				} else {
					stmt = conn
							.prepareStatement("INSERT INTO url (urlCount, url) VALUES (?,?)");

					stmt.setInt(1, (urlCount));
					stmt.setString(2, linkURL);
				}
				stmt.executeUpdate();
			}

		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

	}

	/**
	 * Deletes All of the user's bookmarks, except the inbox :)
	 * 
	 * @param user
	 *            The ClientEntry object of the user that wishes to clear ALL of
	 *            his or her bookmarks
	 * @author Jon McLachlan (kryptpenguin)
	 */
	public void deleteAllBookmarks(ClientEntry user) throws SQLException,
			NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmtInbox = null;
		int inboxID = -1;
		PreparedStatement stmt = null;
		PreparedStatement stmntQuery = null;
		ResultSet r = null;
		try {
			conn1 = ConnectionPool.getConn();
			// first, figure out where this guy's inbox is...
			stmtInbox = conn1
					.prepareStatement("SELECT clientinboxid FROM client WHERE clientname = ?");
			stmtInbox.setString(1, user.getClientName());
			r = stmtInbox.executeQuery();
			if (r.first()) {
				// found the inbox folderID here
				inboxID = r.getInt("clientinboxid");
				r.close();
				stmtInbox.close();
			}
			if (inboxID < 0) {
				// a boo boo happened...
				throw new SQLException("The user " + user.getClientName()
						+ " does not have an inbox, crap!");
			}
			if (inboxID >= 0) {

				stmntQuery = conn1
						.prepareStatement("SELECT linkID FROM link WHERE linkClientID = ?");
				stmntQuery.setInt(1, user.getClientID());
				r = stmntQuery.executeQuery();
				if (r.first()) {
					ArrayList<Integer> allClientLinkIDs = new ArrayList<Integer>();
					allClientLinkIDs.add(r.getInt("linkID"));
					while (r.next()) {
						allClientLinkIDs.add(r.getInt("linkID"));
					}

					Iterator<Integer> linkID = allClientLinkIDs.listIterator();
					while (linkID.hasNext()) {
						deleteBookmark(user, linkID.next());
					}
				}
				// delete all links
				stmt = conn1
						.prepareStatement("DELETE FROM link WHERE linkClientID = ?");
				stmt.setInt(1, user.getClientID());
				stmt.executeUpdate();
				if (stmt != null) {
					stmt.close();
				}
				// delete all folders, but the inbox
				stmt = conn1
						.prepareStatement("DELETE FROM folder WHERE folderclientid = ? AND folderparentid IS NOT NULL AND folderid != ?");
				stmt.setInt(1, user.getClientID());
				stmt.setInt(2, inboxID);
				stmt.executeUpdate();
				if (stmt != null) {
					stmt.close();
				}
			}

		} finally {
			if (stmtInbox != null) {
				stmtInbox.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (r != null) {
				r.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Edits a bookmark specified by the given linkID.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param linkID
	 *            The ID of the link to edit.
	 * @param linkName
	 *            The new name of the link, or null not to change.
	 * @param linkURL
	 *            The new URL of the link, or null not to change.
	 * @param linkPermission
	 *            The new permission of the link, or null not to change.
	 * @param linkDescription
	 *            The new description of the link, or null not to change.
	 * @param labelNames
	 *            The new list of label names for the link, or null not to
	 *            change.
	 * @return The edited bookmark.
	 * @author schwerdf
	 */
	public Bookmark editBookmark(ClientEntry user, int linkID, String linkName,
			String linkURL, String linkPermission, String linkDescription,
			ArrayList<String> labelNames, int toolbarPosition)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement stmt1a = null;
		ResultSet rs1a = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt1b = null;
		PreparedStatement stmt1c = null;
		PreparedStatement stmt1d = null;
		ResultSet rs2b = null;
		PreparedStatement stmt2b = null;

		String OriginalLinkPermission = null;

		try {
			// check for dups in the labels
			if (labelNames != null) {
				HashSet<String> h = new HashSet<String>(labelNames);
				labelNames.clear();
				labelNames.addAll(h);
			}

			// Bomb out if all fields are marked "do not change"
			if (linkName == null && linkURL == null && linkPermission == null
					&& linkDescription == null && labelNames == null) {
				throw new NoSuchFieldException("Nothing to change");
			}

			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT linkID, linkPermission FROM link WHERE linkID = ? AND linkClientID = ?");
			stmt1.setInt(1, linkID);
			stmt1.setInt(2, user.getClientID());
			rs1 = stmt1.executeQuery();

			if (!rs1.first()) {
				throw new IllegalAccessException(
						"Attempting to edit a non-existent bookmark, or a bookmark not owned by this user");
			} else
				OriginalLinkPermission = rs1.getString("linkPermission");

			if (stmt1 != null) {
				stmt1.close();
			}

			// Check if URL is changing and find correct url table entry
			int urlID = 0;
			if (linkURL != null) {

				// there is a max size on URLs
				if (linkURL.length() > 255) {
					linkURL = linkURL.substring(0, 254);
				}

				// Update the URL table

				if (OriginalLinkPermission.equals("public")
						&& linkPermission.equals("private"))
					this.updateTableURLEntry(linkURL, "delete");

				if (OriginalLinkPermission.equals("private")
						&& linkPermission.equals("public"))
					this.updateTableURLEntry(linkURL, "add");
			}
			// When something need to be done
			if (linkName != null || linkURL != null || linkPermission != null
					|| linkDescription != null) {

				String prepStart = "UPDATE link SET ";
				if (linkName != null) {
					prepStart += "linkName = ?, ";
				}
				if (linkURL != null) {
					prepStart += "linkURL = ?, ";
					prepStart += "linkUrlID = ?, ";
				}
				if (linkPermission != null) {
					prepStart += "linkPermission = ?, ";
				}
				if (linkDescription != null) {
					prepStart += "linkDescription = ?, ";
				}
				prepStart += "toolbarPosition = ?, ";
				prepStart = prepStart.substring(0, prepStart.length() - 2)
						+ " WHERE linkID = ?";

				stmt1b = conn1.prepareStatement(prepStart);

				int i = 1;
				if (linkName != null) {
					stmt1b.setString(i++, linkName);
				}
				if (linkURL != null) {
					stmt1b.setString(i++, linkURL);
					stmt1b.setInt(i++, urlID);
				}
				if (linkPermission != null) {
					stmt1b.setString(i++, linkPermission);
				}
				if (linkDescription != null) {
					stmt1b.setString(i++, linkDescription);
				}
				stmt1b.setInt(i++, toolbarPosition);
				stmt1b.setInt(i++, linkID);

				try {
					stmt1b.executeUpdate();
				} catch (SQLException sqlEx) {
					throw new SQLException("Unable to add link '" + linkURL
							+ "' to table:" + sqlEx);
				} finally {
					if (stmt1b != null) {
						stmt1b.close();
					}
				}
			}
			// if labels are null then we should delete all the labels
			if (labelNames == null) {
				stmt1c = conn1
						.prepareStatement("DELETE FROM label WHERE labelLinkID = ?");
				stmt1c.setInt(1, linkID);
				stmt1c.executeUpdate();
				if (stmt1c != null) {
					stmt1c.close();
				}
			} else {
				stmt1c = conn1
						.prepareStatement("DELETE FROM label WHERE labelLinkID = ?");
				stmt1c.setInt(1, linkID);
				stmt1c.executeUpdate();
				if (stmt1c != null) {
					stmt1c.close();
				}

				stmt1d = conn1
						.prepareStatement("INSERT INTO label(labelLinkID,labelName) VALUES(?,?)");
				for (java.util.Iterator it = labelNames.iterator(); it
						.hasNext();) {
					String label = (String) it.next();
					// adding a blank label fix by Ross Olson
					// Do we need to handle other types of whitespace here?
					// -Andyk
					String nullLabel = label.replaceAll(" ", "");
					if (!nullLabel.equals("")) {
						stmt1d.setInt(1, linkID);
						stmt1d.setString(2, label);
						stmt1d.executeUpdate();
					}
				}
				if (stmt1d != null) {
					stmt1d.close();
				}
			}
			return getBookmark(user, linkID);

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (stmt1a != null) {
				stmt1a.close();
			}
			if (rs1a != null) {
				rs1a.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
			if (stmt1b != null) {
				stmt1b.close();
			}
			if (stmt1c != null) {
				stmt1c.close();
			}
			if (stmt1d != null) {
				stmt1d.close();
			}
			if (stmt2b != null) {
				stmt2b.close();
			}
			if (rs2b != null) {
				rs2b.close();
			}
			if (conn1 != null) {
				conn1.close();
			}

		}
	}

	public Bookmark addBookmark(ClientEntry user, String linkName,
			String linkURL, String linkPermission, String linkDescription,
			ArrayList<String> labelNames) throws SQLException,
			NoSuchFieldException, IllegalAccessException {
		int rootID = getRootFolderID(user);
		return addBookmark(user, linkName, linkURL, linkPermission,
				linkDescription, rootID, labelNames, 0);
	}

	/**
	 * Adds a bookmark in the root folder given all the information about it
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param linkName
	 *            The new name of the link
	 * @param linkURL
	 *            The new URL of the link
	 * @param linkPermission
	 *            The new permission of the link
	 * @param linkDescription
	 *            The new description of the link
	 * @param labelNames
	 *            The new list of label names for the link
	 * @return The new bookmark.
	 * @author Ross Olson
	 */
	public Bookmark addBookmark(ClientEntry user, String linkName,
			String linkURL, String linkPermission, String linkDescription,
			ArrayList<String> labelNames, int toolbarPosition)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		// This is a hard coded fix. It puts all added bookmarks under the ROOT
		// folder.
		// get the clients root folder id

		int rootID = getRootFolderID(user);

		return addBookmark(user, linkName, linkURL, linkPermission,
				linkDescription, rootID, labelNames, toolbarPosition);
	}

	/**
	 * Adds a bookmark to a given folder given all the information about it
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param linkName
	 *            The new name of the link
	 * @param linkURL
	 *            The new URL of the link
	 * @param linkPermission
	 *            The new permission of the link
	 * @param linkDescription
	 *            The new description of the link
	 * @param linkFolderParentID
	 *            The folder in which to place the bookmark.
	 * @param labelNames
	 *            The new list of label names for the link
	 * @return The new bookmark.
	 * @author schwerdf, Ross Olson
	 */
	public Bookmark addBookmark(ClientEntry user, String linkName,
			String linkURL, String linkPermission, String linkDescription,
			int linkFolderParentID, ArrayList<String> labelNames,
			int toolbarPosition) throws SQLException, NoSuchFieldException,
			IllegalAccessException {

		Connection conn1 = null;
		Connection conn2 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt1a = null;
		PreparedStatement stmt1b = null;
		ResultSet rs1b = null;
		PreparedStatement stmt1d = null;
		PreparedStatement stmt1c = null;
		PreparedStatement stmt2b = null;
		PreparedStatement stmt2c = null;
		ResultSet rs1c = null;

		try {
			// Check to see if the user can actually add a chipmark.
			if (countUserBookmarks(user) >= MAXCHIPMARKS) {
				throw new NoSuchFieldException(
						"Chipmark quota is full for user "
								+ user.getClientName());
			}

			// check for dups in the labels
			if (labelNames != null) {
				HashSet<String> h = new HashSet<String>(labelNames);
				labelNames.clear();
				labelNames.addAll(h);
			}

			int urlID = 0;
			int clientID = user.getClientID();

			conn1 = ConnectionPool.getConn();
			conn2 = ConnectionPool.getConn();

			// First lets make sure the linkURL is not longer than it can be,
			// since it'll get truncated anyway.
			if (linkURL.length() > 255) {
				linkURL = linkURL.substring(0, 254);
			}

			// Check if the user already saved this URL
			stmt2c = conn1
					.prepareStatement("SELECT linkID FROM link WHERE linkClientID = ? AND linkURL = ?");
			stmt2c.setInt(1, clientID);
			stmt2c.setString(2, linkURL);

			rs1c = stmt2c.executeQuery();

			if (linkPermission.equals("public") && !rs1c.first()) {
				updateTableURLEntry(linkURL, "add");

				stmt1 = conn1
						.prepareStatement("SELECT urlID FROM url WHERE url = ?");
				stmt1.setString(1, linkURL);
				rs1 = stmt1.executeQuery();
				if (rs1.first()) {
					urlID = rs1.getInt("urlID");
				} else {
					throw new SQLException("I can't find the URL ID!");
				}
			}

			// The statement to add the bookmark to the link table
			stmt1a = conn1
					.prepareStatement("INSERT INTO link(linkClientID,linkFolderParentID,linkName,linkURL,linkPermission,linkDescription,linkUrlID,linkSortOrder,linkLastMod,toolbarPosition) VALUES(?,?,?,?,?,?,?,?,NOW(),?)");
			stmt1a.setInt(1, clientID);
			stmt1a.setInt(2, linkFolderParentID);
			stmt1a.setString(3, linkName);
			stmt1a.setString(4, linkURL);
			stmt1a.setString(5, linkPermission);
			stmt1a.setString(6, linkDescription);
			stmt1a.setInt(7, urlID);
			stmt1a.setInt(8, 0);
			stmt1a.setInt(9, toolbarPosition);

			// The statement to add the bookmark to the linktext table
			stmt1b = conn1
					.prepareStatement("INSERT INTO linktext(linkName,linkURL,linkDescription) VALUES(?,?,?)");
			// stmt1b.setInt(1, clientID);
			stmt1b.setString(1, linkName);
			stmt1b.setString(2, linkURL);
			stmt1b.setString(3, linkDescription);
			stmt1a.executeUpdate();
			// Add the bookmark
			try {

				stmt1b.executeUpdate();
			} catch (Exception ex) {
				throw new NoSuchFieldException(
						"Database failed to add bookmark");
			} finally {
				if (stmt1a != null) {
					stmt1a.close();
				}
			}

			// Get the ID used for the insert into the link table
			stmt1b = conn1
					.prepareStatement("SELECT last_insert_id() as lastid");
			rs1b = stmt1b.executeQuery();
			rs1b.first();
			int linkID = rs1b.getInt("lastid");
			if (rs1b != null) {
				rs1b.close();
			}

			Bookmark rv = new Bookmark(linkID, clientID, linkName, linkURL,
					linkPermission, linkDescription, linkFolderParentID,
					toolbarPosition);

			// If there are no label names to add, we are done
			if (labelNames != null && !labelNames.isEmpty()) {
				// We reuse this for efficiency
				stmt1c = conn1
						.prepareStatement("INSERT INTO label(labelLinkID,labelName) VALUES(?,?)");
				for (Iterator it = labelNames.iterator(); it.hasNext();) {

					// fix for blank labels by Ross Olson
					String tempLabel = (String) it.next();
					String nullLabel = tempLabel.replaceAll(" ", "");
					if (!nullLabel.equals("")) {
						stmt1c.setInt(1, linkID);
						stmt1c.setString(2, tempLabel);
						stmt1c.executeUpdate();
						rv.addLabel(tempLabel);
					}
				}
			}

			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (stmt1a != null) {
				stmt1a.close();
			}
			if (stmt1b != null) {
				stmt1b.close();
			}
			if (stmt2b != null) {
				stmt2b.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
			if (rs1b != null) {
				rs1b.close();
			}
			if (stmt1c != null) {
				stmt1c.close();
			}
			if (stmt1d != null) {
				stmt1d.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
			if (conn2 != null) {
				conn2.close();
			}
		}
	}

	/**
	 * Adds a user to the client table.
	 * 
	 * @param clientName
	 *            The user name of the new user
	 * @param clientPass
	 *            The password of the new user (plaintext)
	 * @param clientMail
	 *            The e-mail address of the new user
	 * @author McLachlan
	 * @throws SQLException
	 *             ,IllegalAccessException,AlreadyBoundException,
	 *             NoSuchAlgorithmException,DigestException,
	 *             UnsupportedEncodingException
	 */
	public void addClient(String clientName, String clientMail,
			String clientPass, boolean filter, boolean isADSAccount)
			throws SQLException, IllegalAccessException, AlreadyBoundException,
			NoSuchAlgorithmException, DigestException,
			UnsupportedEncodingException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt1a = null;
		PreparedStatement stmt1b = null;
		PreparedStatement stmt1c = null;
		PreparedStatement stmt1d = null;
		PreparedStatement stmt1e = null;
		PreparedStatement stmt1f = null;
		PreparedStatement stmt1g = null;
		ResultSet rs = null;
		ResultSet rs1b = null;
		ResultSet rs1d = null;
		ResultSet rs1f = null;
		try {
			conn1 = ConnectionPool.getConn();

			// Prepare a statement to get any existing client with the same user
			// name out of the database
			stmt1 = conn1
					.prepareStatement("SELECT * FROM client WHERE clientName = ?");
			stmt1.setString(1, clientName);
			rs = stmt1.executeQuery();

			// If there was such a client, bomb out
			if (rs.first()) {
				throw new AlreadyBoundException(
						"That username is already in use, please enter a different username");
			}

			// Close the statement
			if (stmt1 != null) {
				stmt1.close();
			}

			// Prepare a statement to add a new user with the given data
			stmt1a = conn1
					.prepareStatement("INSERT INTO client(clientName,clientMail,clientPass, clientRegDate,clientLoginDate, clientInboxID, clientAdultFilter) VALUES (?,?,?,NOW(),NOW(),?,?)");
			stmt1a.setString(1, clientName);
			stmt1a.setString(2, clientMail);
			stmt1a.setString(3, Utilities.encrypt(clientPass));

			stmt1a.setInt(4, 0); // The Inbox folder is none for now.
			if (filter)
				stmt1a.setString(5, "true");
			else
				stmt1a.setString(5, "false");

			// Add the user
			stmt1a.executeUpdate();
			if (stmt1a != null) {
				stmt1a.close();
			}

			// now create their root folder!
			// get the client ID
			stmt1b = conn1
					.prepareStatement("SELECT last_insert_id() as lastid");
			rs1b = stmt1b.executeQuery();
			rs1b.first();
			int clientID = rs1b.getInt("lastid");
			if (rs1b != null) {
				rs1b.close();
			}

			// add the root folder
			// change 'Your Chipmarks' to 'my chipmarks' for 2.0 release -Andyk
			stmt1c = conn1
					.prepareStatement("INSERT INTO folder(folderClientID, folderName) VALUES (?, 'my chipmarks')");
			stmt1c.setInt(1, clientID);
			stmt1c.executeUpdate();
			if (stmt1c != null) {
				stmt1c.close();
			}

			// Get the folder's ID
			stmt1d = conn1
					.prepareStatement("SELECT last_insert_id() as lastid");
			rs1d = stmt1d.executeQuery();
			rs1d.first();
			int rootID = rs1d.getInt("lastid");
			if (rs1d != null) {
				rs1d.close();
			}

			// add the inbox folder:
			stmt1e = conn1
					.prepareStatement("INSERT INTO folder(folderClientID, folderParentID, folderName) VALUES (?, ?, 'inbox')");
			stmt1e.setInt(1, clientID);
			stmt1e.setInt(2, rootID);
			stmt1e.executeUpdate();
			if (stmt1e != null) {
				stmt1e.close();
			}

			// Get the folder's ID
			stmt1f = conn1
					.prepareStatement("SELECT last_insert_id() as lastid");
			rs1f = stmt1f.executeQuery();
			rs1f.first();
			int inboxID = rs1f.getInt("lastid");
			if (rs1f != null) {
				rs1f.close();
			}

			// Update the inbox ID.
			stmt1g = conn1
					.prepareStatement("UPDATE client SET clientInboxID = ? WHERE clientID = ?");
			stmt1g.setInt(1, inboxID);
			stmt1g.setInt(2, clientID);
			stmt1g.executeUpdate();
			if (stmt1g != null) {
				stmt1g.close();
			}

		} catch (SQLException sqlEx) {
			throw new SQLException("Unable to create user in database: "
					+ sqlEx);
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (stmt1a != null) {
				stmt1a.close();
			}
			if (stmt1b != null) {
				stmt1b.close();
			}
			if (rs1b != null) {
				rs1b.close();
			}
			if (stmt1c != null) {
				stmt1c.close();
			}
			if (rs1d != null) {
				rs1d.close();
			}
			if (stmt1d != null) {
				stmt1d.close();
			}
			if (stmt1e != null) {
				stmt1e.close();
			}
			if (rs1f != null) {
				rs1f.close();
			}
			if (stmt1f != null) {
				stmt1f.close();
			}
			if (stmt1g != null) {
				stmt1g.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * returns the boolean of a URL being safe or not
	 * 
	 * @param url
	 * @return boolean
	 * @throws SQLException
	 * @author pete2786
	 */
	public boolean isUrlSafe(String url) throws SQLException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;

		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT domain FROM blacklist WHERE domain = ? LIMIT 1");
			stmt1.setString(1, url);
			rs1 = stmt1.executeQuery();
			return !rs1.first();
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Returns a ClientEntry object for servlet use (logs a user in). Does
	 * modify the database! This updates the date on when they loged in.
	 * 
	 * @param clientName
	 *            The name of the user.
	 * @param clientPass
	 *            The password of the user.
	 * @param isEncrypted
	 *            Whether or not clientPass is plaintext or an encrypted hash.
	 * @return A ClientEntry object representing the given user.
	 * @author schwerdf
	 * @throws UnsupportedEncodingException
	 */
	public ClientEntry authLogin(String clientName, String clientPass,
			boolean isEncrypted) throws SQLException, IllegalAccessException,
			LoginException, NoSuchAlgorithmException, DigestException,
			UnsupportedEncodingException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt1a = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientID, clientMail, clientPass, clientAdultFilter, preferredLang, userRole FROM client WHERE clientName = ? AND clientPass = ?");
			stmt1.setString(1, clientName);

			if (!isEncrypted) {
				stmt1.setString(2, Utilities.encrypt(clientPass));
			} else {
				stmt1.setString(2, clientPass);
			}
			String test = Utilities.encrypt(clientPass);
			rs1 = stmt1.executeQuery();

			boolean valid = rs1.first();
			if (!valid) {
				throw new LoginException("Invalid login information");
			}

			int clientID = rs1.getInt("clientID");
			String clientMail = rs1.getString("clientMail");
			String clientPass_encrypted = rs1.getString("clientPass");
			boolean clientAdultFilterDB;
			if (rs1.getString("clientAdultFilter").equals("false")) {
				clientAdultFilterDB = false;
			} else {
				clientAdultFilterDB = true;
			}

			String prefLang = rs1.getString("preferredLang");
			String role = rs1.getString("userRole");

			if (stmt1 != null) {
				stmt1.close();
			}

			// Ross Olson added code here for updating the date when a client
			// logs in...
			stmt1a = conn1
					.prepareStatement("UPDATE client SET clientLoginDate = NOW() WHERE clientID = ?");
			stmt1a.setInt(1, clientID);
			stmt1a.executeUpdate();
			if (stmt1a != null) {
				stmt1a.close();
			}

			return new ClientEntry(clientID, clientName, clientMail,
					clientPass_encrypted, clientAdultFilterDB, prefLang, role);

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (stmt1a != null) {
				stmt1a.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 */
	public boolean authCheck(ClientEntry user) throws SQLException,
			IllegalAccessException {

		if (user == null)
			return false;

		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		Connection conn1 = null;

		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT * FROM client WHERE clientID = ? AND clientPass = ?");
			stmt1.setInt(1, user.getClientID());
			stmt1.setString(2, user.getClientPass());

			rs1 = stmt1.executeQuery();

			return rs1.first();

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}

	}

	/**
	 * Returns all labels for a particular user
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 */
	public ArrayList<String> getAllUserLabels(ClientEntry user)
			throws SQLException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;

		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT DISTINCT labelName FROM link li, label l WHERE li.linkID = l.labelLinkID and li.linkClientID = ? ORDER BY labelName ASC");
			stmt1.setInt(1, user.getClientID());

			rs1 = stmt1.executeQuery();
			ArrayList<String> rv = new ArrayList<String>();

			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {
				rv.add(rs1.getString("labelName"));
			}
			return rv;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Sets all X by given Y
	 * 
	 * @param x
	 * @param y
	 * @param xVal
	 * @param yVal
	 * @return True, on successful update, false otherwise
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private boolean setXByY(String x, String y, String xVal, String yVal)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			// Prepare a statement to change set x by y
			stmt1 = conn1.prepareStatement("UPDATE client SET " + x
					+ " = ?  WHERE " + y + " = ? ");

			stmt1.setString(1, xVal);
			stmt1.setString(2, yVal);
			int rows = stmt1.executeUpdate();
			if (rows == 0) {
				// throw new NoSuchFieldException("Incorrect " + y);
				return false;
			}
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}

		return true;
	}

	/**
	 * 
	 * @param clientMail
	 * @param clientName
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public boolean setClientMailByClientName(String clientMail,
			String clientName) throws SQLException, NoSuchFieldException,
			IllegalAccessException {
		return setXByY("clientMail", "clientName", clientMail, clientName);
	}

	/**
	 * 
	 * @param adult
	 *            filter
	 * @param clientName
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public boolean setClientFilterByClientName(boolean filter, String clientName)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		if (filter)
			return setXByY("clientAdultFilter", "clientName", "true",
					clientName);
		else
			return setXByY("clientAdultFilter", "clientName", "false",
					clientName);
	}

	/**
	 * 
	 * @param preferred
	 *            Lang
	 * @param clientName
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public boolean setPrefLangByClientName(String prefLang, String clientName)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		return setXByY("preferredLang", "clientName", prefLang, clientName);

	}

	/**
	 * 
	 * @param clientPass
	 * @param clientName
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NoSuchAlgorithmException
	 * @throws DigestException
	 * @throws UnsupportedEncodingException
	 */
	public boolean setClientPassByClientName(String clientPass,
			String clientName) throws SQLException, NoSuchFieldException,
			IllegalAccessException, NoSuchAlgorithmException, DigestException,
			UnsupportedEncodingException {
		return setXByY("clientPass", "clientName", Utilities
				.encrypt(clientPass), clientName);
	}

	/**
	 * 
	 * @param clientPass
	 * @param clientMail
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NoSuchAlgorithmException
	 * @throws DigestException
	 * @throws UnsupportedEncodingException
	 */
	public boolean setClientPassByClientMail(String clientPass,
			String clientMail) throws SQLException, NoSuchFieldException,
			IllegalAccessException, NoSuchAlgorithmException, DigestException,
			UnsupportedEncodingException {
		return setXByY("clientPass", "clientMail", Utilities
				.encrypt(clientPass), clientMail);
	}

	/**
	 * Gets the URL's of the top 10 bookmarks in the database
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 */
	public ArrayList<TopBookmarkInfo> getTop10Bookmarked() throws SQLException,
			IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;

		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT url, urlCount FROM url ORDER BY urlCount DESC LIMIT 10");
			rs1 = stmt1.executeQuery();
			ArrayList<TopBookmarkInfo> rv = new ArrayList<TopBookmarkInfo>();
			int i = 1;

			for (boolean valid = rs1.first(); valid; valid = rs1.next(), i++) {
				rv.add(new TopBookmarkInfo(rs1.getString("url"), rs1
						.getInt("urlCount"), i));
			}

			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Gets a random chipmarks.
	 * 
	 * @params none.
	 * @author William Bullard
	 */
	public LinkNameUrlObject getRandom() throws SQLException,
			NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;

		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT * FROM link AS r1 JOIN (SELECT ROUND(RAND() * (SELECT MAX(linkID) FROM link)) AS linkID) AS r2 WHERE r1.linkID >= r2.linkID AND r1.`linkPermission` = 'public' ORDER BY r1.linkID ASC LIMIT 1");
			// stmt1.setInt(1,limit);
			rs1 = stmt1.executeQuery();
			String linkName;
			String linkURL;

			if (rs1.first()) {
				linkName = rs1.getString("linkName");
				linkURL = rs1.getString("linkURL");
			} else {
				linkName = "Chipmark";
				linkURL = "http://www.chipmark.com";
			}

			LinkNameUrlObject random = new LinkNameUrlObject(linkName, linkURL);

			return random;

		} finally {
			if (rs1 != null) {
				rs1.close();
			}
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Gets the most recently added chipmarks.
	 * 
	 * @param limit
	 *            - Number of chipmarks to get.
	 * @author Ross Olson
	 */
	public ArrayList<LinkNameUrlObject> getMostRecentlyAdded(int limit)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;

		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT linkName, linkURL, DATE_FORMAT(linkLastMod,'%h:%i:%S %p on %M %D, %Y') AS date FROM link WHERE linkPermission='public' ORDER BY linkID DESC LIMIT ?");
			stmt1.setInt(1, limit);
			ResultSet rs1 = stmt1.executeQuery();
			// If no bookmarks were found
			ArrayList<LinkNameUrlObject> rv = new ArrayList<LinkNameUrlObject>();
			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {
				String linkName = rs1.getString("linkName");
				String linkURL = rs1.getString("linkURL");
				String linkDate = rs1.getString("date");
				rv.add(new LinkNameUrlObject(linkName, linkURL, linkDate));
			}

			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Count the number of users in db
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 */
	public int countUsers() throws SQLException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT count(clientID) AS usercount FROM client");
			rs1 = stmt1.executeQuery();
			rs1.first();

			return rs1.getInt("usercount");

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	// Where is there the extra String label???
	/**
	 * Adds a label to the database for a given bookmark.
	 * 
	 * @param bookmark
	 *            - a Bookmark object to be labeled.
	 * @param label
	 *            - a String containing the label name to be associated with the
	 *            bookmark
	 * @author Chris Chase
	 */
	public void addLabel(Bookmark bookmark, String label) throws SQLException,
			IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;

		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("INSERT INTO label(labelLinkID,labelName) VALUES(?,?)");
			stmt1.setInt(1, bookmark.getLinkID());
			stmt1.setString(2, label);
			// mysql logs this for us:
			// logger.info(stmt1.toString());
			stmt1.executeUpdate();
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Returns an integer containing the number of unique URLs in the database.
	 * This was written intended for use in the BrokenLink servlet which will
	 * have no public user access. Do not use this call in a servlet which the
	 * public has access to, as it could be hijacked for a massive breach of the
	 * privacy policy.
	 * 
	 * @return an int containing the number of unique urls in the database
	 * @author Chris Chase
	 */
	// WARNING: Do not use this call in a servlet. It could result in a massive
	// breach of the privacy policy.
	public int getCountOfBookmarksFor404() throws SQLException,
			IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			// PreparedStatement stmt1 =
			// conn.prepareStatement("delete from label where labelName=?;");
			// stmt1.setString(1, "Broken Links");
			// stmt1.execute();
			// This was removed because if the time that this program runs
			// becomes
			// too long, the last links retrieved from the database may have
			// their
			// Broken Links label removed for the duration of the running of the
			// script, which could be days if the database is quite large.
			stmt1 = conn1
					.prepareStatement("SELECT count(DISTINCT linkURL) from link;");

			rs1 = stmt1.executeQuery();
			// logger.info(stmt1.toString());
			int result = 0;
			if (rs1.first()) {
				result = rs1.getInt("count(DISTINCT linkURL)");
			} else {
				// Do we really want to log like this here?
				System.out
						.println("Query did not execute correctly in getCountOfBookmarksFor404");
			}

			return result;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Moves a link from one folder to another.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param linkID
	 *            The database ID of the link to move.
	 * @param destFolderID
	 *            The database ID of the destination folder.
	 * @author schwerdf
	 */
	public void moveLink(ClientEntry user, int linkID, int destFolderID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1a = null;
		ResultSet rs1a = null;
		PreparedStatement stmt1b = null;
		try {
			int clientID = user.getClientID();
			conn1 = ConnectionPool.getConn();
			// Also verify folderclientid (added by andyk 3/28/06)
			stmt1a = conn1
					.prepareStatement("SELECT * FROM folder WHERE folderID = ? AND folderClientID = ?");
			stmt1a.setInt(1, destFolderID);
			stmt1a.setInt(2, clientID);
			rs1a = stmt1a.executeQuery();
			if (!rs1a.first()) {
				throw new NoSuchFieldException("No such folder " + destFolderID
						+ " for client #" + clientID);
			}
			if (stmt1a != null) {
				stmt1a.close();
			}

			stmt1b = conn1
					.prepareStatement("UPDATE link SET linkFolderParentID = ? WHERE linkID = ? AND linkClientID = ?");
			stmt1b.setInt(1, destFolderID);
			stmt1b.setInt(2, linkID);
			stmt1b.setInt(3, clientID);

			stmt1b.executeUpdate();
		} finally {
			if (stmt1a != null) {
				stmt1a.close();
			}
			if (stmt1b != null) {
				stmt1b.close();
			}
			if (rs1a != null) {
				rs1a.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Moves a folder from one folder to another.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param folderID
	 *            The database ID of the folder to move.
	 * @param destFolderID
	 *            The database ID of the destination folder.
	 * @author schwerdf
	 */
	public void moveFolder(ClientEntry user, int folderID, int destFolderID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1a = null;
		ResultSet rs1a = null;
		PreparedStatement stmt1b = null;
		ResultSet rs1b = null;
		PreparedStatement stmt1c = null;
		try {
			int clientID = user.getClientID();
			conn1 = ConnectionPool.getConn();
			// Also verify folderclientid (added by andyk 3/28/06)
			stmt1a = conn1
					.prepareStatement("SELECT * FROM folder WHERE folderID = ? AND folderClientID = ?");
			stmt1a.setInt(1, destFolderID);
			stmt1a.setInt(2, clientID);
			rs1a = stmt1a.executeQuery();
			if (!rs1a.first()) {
				throw new NoSuchFieldException("No such folder " + destFolderID
						+ " for client #" + clientID);
			}
			if (stmt1a != null) {
				stmt1a.close();
			}

			// Circularity check
			stmt1b = conn1
					.prepareStatement("SELECT folderParentID FROM folder WHERE folderID = ?");
			for (int id = destFolderID; id != 0;) {
				if (id == folderID) {
					throw new NoSuchFieldException(
							"You may not move a folder into one of its own folders.");
				}
				stmt1b.setInt(1, id);
				rs1b = stmt1b.executeQuery();
				rs1b.first();
				id = rs1b.getInt("folderParentID");
				if (rs1b != null) {
					rs1b.close();
				}
			}

			stmt1c = conn1
					.prepareStatement("UPDATE folder SET folderParentID = ? WHERE folderID = ? AND folderClientID = ?");
			stmt1c.setInt(1, destFolderID);
			stmt1c.setInt(2, folderID);
			stmt1c.setInt(3, clientID);
			stmt1c.executeUpdate();

		} finally {
			if (stmt1a != null) {
				stmt1a.close();
			}
			if (stmt1b != null) {
				stmt1b.close();
			}
			if (stmt1c != null) {
				stmt1c.close();
			}
			if (rs1a != null) {
				rs1a.close();
			}
			if (rs1b != null) {
				rs1b.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Copies a link from one folder to another.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param linkID
	 *            The database ID of the link to copy.
	 * @param destFolderID
	 *            The database ID of the destination folder.
	 * @return The new bookmark.
	 * @author schwerdf
	 */
	public Bookmark copyLink(ClientEntry user, int linkID, int destFolderID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			int clientID = user.getClientID();
			conn1 = ConnectionPool.getConn();
			// Also verify folderclientid (added by andyk 3/28/06)
			stmt1 = conn1
					.prepareStatement("SELECT * FROM folder WHERE folderID = ? AND folderClientID = ?");
			stmt1.setInt(1, destFolderID);
			stmt1.setInt(2, clientID);
			rs1 = stmt1.executeQuery();
			if (!rs1.first()) {
				throw new NoSuchFieldException("No such folder " + destFolderID
						+ " for client #" + clientID);
			}
			if (conn1 != null) {
				conn1.close();
			}
			conn1 = null;
			Bookmark link = getUserBookmark(user, linkID);
			ArrayList<String> labelNames = new ArrayList<String>();
			for (Iterator it = link.getLabels(); it.hasNext();) {
				labelNames.add((String) it.next());
			}
			return addBookmark(user, link.getLinkName(), link.getLinkURL(),
					link.getLinkPermission(), link.getLinkDescription(),
					destFolderID, labelNames, link.getToolbarPosition());
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Changes a folder's name.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param folderID
	 *            The database ID of the folder to edit.
	 * @param folderName
	 *            The new name of the folder.
	 * @param toolbarPosition
	 *            The position of the folder on the toolbar.
	 * @author Ross Olson
	 */
	public void editFolder(ClientEntry user, int folderID, String folderName,
			int toolbarPosition) throws SQLException, NoSuchFieldException,
			IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("UPDATE folder SET folderName = ?, toolbarPosition = ? WHERE folderID = ? AND folderClientID = ?");
			stmt1.setString(1, folderName);
			stmt1.setInt(2, toolbarPosition);
			stmt1.setInt(3, folderID);
			stmt1.setInt(4, user.getClientID());
			stmt1.executeUpdate();
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Makes a folder public or private
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param folderID
	 *            The database ID of the folder to edit.
	 * @param pub
	 *            Whether we're setting the folder public or private (true ==
	 *            public).
	 * @author Brian Terlson
	 */
	public void editFolderPublicity(ClientEntry user, int folderID, boolean pub)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;

		try {
			conn1 = ConnectionPool.getConn();
			if (folderBelongsToClient(folderID, user.getClientID())) {
				// folder belongs to current client

				// first, update the folder's publicity.
				stmt1 = conn1
						.prepareStatement("UPDATE folder SET public = ? WHERE folderID = ?");

				stmt1.setString(1, Boolean.toString(pub));
				stmt1.setInt(2, folderID);
				stmt1.executeUpdate();
				if (!pub) {
					// if we're making the folder private, we will want to
					// remove current subscriptions
					// Possible future change is to remove only current
					// subscriptions that are not buddes
					// of the folder owner.
					stmt2 = conn1
							.prepareStatement("DELETE FROM folder_subscription WHERE folderID=?");
					stmt2.setInt(1, folderID);
					stmt2.executeUpdate();
				}
			} else
				throw new IllegalAccessException();

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Creates a new folder subscription.
	 * 
	 * @param invitee
	 *            person invited to share the specified folder
	 * @param folderID
	 *            folder to invite to.
	 * @author Brian Terlson & Jon McLachlan
	 */
	public void createFolderSubscriptionInvite(ClientEntry invitee,
			ClientEntry inviter, int folderID) throws SQLException,
			NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt = null;
		try {
			conn1 = ConnectionPool.getConn();
			if (folderBelongsToClient(folderID, inviter.getClientID())) {
				// folder belongs to client. Proceed.
				stmt = conn1
						.prepareStatement("INSERT INTO folder_subscription(clientId, folderID) VALUES(?, ?)");
				stmt.setInt(1, invitee.getClientID());
				stmt.setInt(2, folderID);
				stmt.executeUpdate();
			} else
				throw new IllegalAccessException();
		} finally {
			if (conn1 != null) {
				conn1.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * Accepts a folder subscription.
	 * 
	 * @param invitee
	 *            person invited to share the specified folder
	 * @param folderID
	 *            folder to accept subscription to
	 * @author Brian Terlson & Jon McLachlan
	 */
	public void acceptFolderSubscription(ClientEntry invitee, int folderID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt = conn1
					.prepareStatement("UPDATE folder_subscription SET accepted='true' WHERE clientId=? AND folderID=?");
			stmt.setInt(1, invitee.getClientID());
			stmt.setInt(2, folderID);
			stmt.executeUpdate();
		} finally {
			if (conn1 != null) {
				conn1.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * Removes a subscription to a shared folder.
	 * 
	 * @param invitee
	 *            invited user
	 * @param folderID
	 *            ID of folder in subscription to delete
	 * @author Brian Terlson & Jon McLachlan
	 */
	public void deleteFolderSubscription(ClientEntry invitee, int folderID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt = conn1
					.prepareStatement("DELETE FROM folder_subscription WHERE clientId=? AND folderID=?");
			stmt.setInt(1, invitee.getClientID());
			stmt.setInt(2, folderID);
			stmt.executeUpdate();
		} finally {
			if (conn1 != null) {
				conn1.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * Removes all of a user's folder subscriptions.
	 * 
	 * @param user
	 *            user to nuke (BOOM!)
	 * @author Brian Terlson
	 */
	public void deleteAllFolderSubscriptions(ClientEntry user)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt = conn1
					.prepareStatement("DELETE FROM folder_subscription WHERE clientId=?");
			stmt.setInt(1, user.getClientID());
			stmt.executeUpdate();
		} finally {
			if (conn1 != null) {
				conn1.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * Get all a user's shared folder invites.
	 * 
	 * @param user
	 * @author Brian Terlson
	 */
	public ArrayList<SharedFolder> getFolderInvites(ClientEntry user)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		ArrayList<SharedFolder> folders = new ArrayList<SharedFolder>();
		Connection conn1 = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt = conn1
					.prepareStatement("SELECT folder_subscription.*, folder.*, client.clientName FROM folder_subscription LEFT JOIN folder ON folder_subscription.folderID = folder.folderID LEFT JOIN client ON folder.folderClientID = client.clientID WHERE folder_subscription.clientID = ? AND folder_subscription.accepted = 'false'");
			stmt.setInt(1, user.getClientID());
			res = stmt.executeQuery();
			while (res.next()) {
				SharedFolder newFolder = new SharedFolder(res
						.getInt("folder.folderID"), res
						.getInt("folder.folderClientID"), res
						.getInt("folder.folderParentID"), res
						.getString("folder.folderName"));
				newFolder.setOwnerName(res.getString("client.clientName"));
				folders.add(newFolder);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
			if (res != null) {
				res.close();
			}
		}
		return folders;
	}

	/**
	 * Checks whether the specified folderID belongs to the specified clientID
	 * 
	 * @param clientID
	 *            client to check
	 * @param folderID
	 *            folder to check
	 * @return whether the folder specified by folderID belongs to the client
	 *         specified by ClientID
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @author Brian Terlson
	 */
	public boolean folderBelongsToClient(int folderID, int clientID)
			throws SQLException, NoSuchFieldException {
		Connection conn1 = null;
		PreparedStatement checkStmt = null;
		ResultSet res = null;
		try {
			conn1 = ConnectionPool.getConn();
			checkStmt = conn1
					.prepareStatement("SELECT folderID FROM folder WHERE folderID=? AND folderClientID=?");
			checkStmt.setInt(1, folderID);
			checkStmt.setInt(2, clientID);
			res = checkStmt.executeQuery();
			if (res.first()) {
				return true;
			} else {
				return false;
			}
		} finally {
			if (checkStmt != null) {
				checkStmt.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
			if (res != null) {
				res.close();
			}
		}
	}

	/**
	 * Deletes a folder and all links and folders therein.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param folderID
	 *            The database ID of the folder to delete.
	 * @author Ross Olson
	 */
	public void deleteFolder(ClientEntry user, int folderID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		try {
			// Maybe we should allow this so that users can easily delete
			// all of their bookmarks... then we would have the create
			// a new root & mailbox for them too.
			if (getRootFolderID(user) == folderID) {
				throw new NoSuchFieldException(
						"You are not allowed to delete your root folder.");
			}
			if (getInboxFolderID(user) == folderID) {
				throw new NoSuchFieldException(
						"You are not allowed to delete your inbox folder.");
			}

			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("DELETE FROM folder WHERE folderID = ? AND folderClientID = ?");
			stmt1.setInt(1, folderID);
			stmt1.setInt(2, user.getClientID());
			stmt1.executeUpdate();
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Adds a new folder that is not on the toolbar.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param parentFolderID
	 *            The database ID of the folder in which to place the new
	 *            folder.
	 * @param folderName
	 *            The name of the new folder.
	 * @return The created folder.
	 * @author schwerdf
	 */
	public Folder addFolder(ClientEntry user, int parentFolderID,
			String folderName) throws SQLException, NoSuchFieldException,
			IllegalAccessException {
		return addFolder(user, parentFolderID, folderName, 0);
	}

	/**
	 * Adds a new folder.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @param parentFolderID
	 *            The database ID of the folder in which to place the new
	 *            folder.
	 * @param folderName
	 *            The name of the new folder.
	 * @param toolbarPosition
	 *            The position of the folder on the toolbar.
	 * @return The created folder.
	 * @author schwerdf
	 */
	public Folder addFolder(ClientEntry user, int parentFolderID,
			String folderName, int toolbarPosition) throws SQLException,
			NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1a = null;
		ResultSet rs1a = null;
		PreparedStatement stmt1b = null;
		ResultSet rs1c = null;
		PreparedStatement stmt1c = null;
		try {
			int clientID = user.getClientID();
			conn1 = ConnectionPool.getConn();
			// Also verify folderclientid (added by andyk 3/28/06)
			stmt1a = conn1
					.prepareStatement("SELECT * FROM folder WHERE folderID = ? AND folderClientID = ?");
			stmt1a.setInt(1, parentFolderID);
			stmt1a.setInt(2, clientID);
			rs1a = stmt1a.executeQuery();
			if (!rs1a.first()) {
				throw new NoSuchFieldException("No such folder "
						+ parentFolderID + " for client #" + clientID);
			}
			if (stmt1a != null) {
				stmt1a.close();
			}

			stmt1b = conn1
					.prepareStatement("INSERT INTO folder(folderClientID, folderParentID, folderName, toolbarPosition) VALUES(?, ?, ?, ?)");
			stmt1b.setInt(1, clientID);
			stmt1b.setInt(2, parentFolderID);
			stmt1b.setString(3, folderName);
			stmt1b.setInt(4, toolbarPosition);
			stmt1b.executeUpdate();
			if (stmt1b != null) {
				stmt1b.close();
			}

			// new, faster approach:
			stmt1c = conn1
					.prepareStatement("SELECT last_insert_id() as lastid");
			rs1c = stmt1c.executeQuery();
			rs1c.first();
			int folderID = rs1c.getInt("lastid");

			return new Folder(folderID, clientID, parentFolderID, folderName,
					"local", toolbarPosition);

		} finally {
			if (stmt1a != null) {
				stmt1a.close();
			}
			if (stmt1b != null) {
				stmt1b.close();
			}
			if (rs1a != null) {
				rs1a.close();
			}
			if (rs1c != null) {
				rs1c.close();
			}
			if (stmt1c != null) {
				stmt1c.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Gets the client folders.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @author schwerdf
	 */
	public ArrayList<Folder> getUserFolders(ClientEntry user)
			throws SQLException, IllegalAccessException {
		int clientID = user.getClientID();
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			int inboxID = getInboxFolderID(user);
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("(SELECT folder.folderID, folder.folderClientID, folder.folderParentID, folder.folderName, folder.toolbarPosition, GROUP_CONCAT(client.clientName) AS clientList FROM folder LEFT JOIN folder_subscription ON folder.folderID = folder_subscription.folderID LEFT JOIN client ON folder_subscription.clientID = client.clientID WHERE folder.folderClientID= ? GROUP BY folder.folderID) UNION (SELECT folder.folderID, folderClientID, folderParentID, folderName, toolbarPosition, NULL as clientList FROM folder_subscription LEFT JOIN folder USING(folderID) WHERE clientID = ? AND accepted = 'true') ORDER BY folderName ASC;");
			stmt1.setInt(1, clientID);
			stmt1.setInt(2, clientID);

			rs1 = stmt1.executeQuery();

			ArrayList<Folder> rv = new ArrayList<Folder>();

			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {
				int folderID = rs1.getInt("folderID");
				int folderParentID = rs1.getInt("folderParentID");
				int folderClientID = rs1.getInt("folderClientID");
				String folderName = rs1.getString("folderName");
				int toolbarPosition = rs1.getInt("toolbarPosition");
				String folderType = "local";

				if (folderID == inboxID) {
					folderType = "inbox";
				} else if (folderParentID == 0) {
					folderType = "root";
				} else if (folderClientID != user.getClientID()) {
					folderType = "shared";
				}
				Folder newFolder = new Folder(folderID, clientID,
						folderParentID, folderName, folderType, toolbarPosition);

				// check for users subscribed to this folder. Will be in the
				// comma deliminated list in the field
				// clientList
				if (rs1.getString("clientList") != null)
					newFolder.subscribedUsers = rs1.getString("clientList")
							.split(",");

				rv.add(newFolder);
			}

			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Gets the client folders that contain one or more chipmark.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @author Jon McLachlan
	 */
	public ArrayList<Folder> getUserNonEmptyFoldersAndRoot(ClientEntry user)
			throws SQLException, IllegalAccessException {
		int clientID = user.getClientID();
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		Connection conn2 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		try {
			// one query can not handle a union when the selects have diff
			// number of columns in them
			int inboxID = getInboxFolderID(user);
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT folderID, folderClientID, folderParentID, folderName, toolbarPosition, COUNT(*) AS num FROM folder RIGHT JOIN link ON linkFolderParentID = folderID WHERE (folderClientID = ?) GROUP BY folderID HAVING (num > 0)");
			stmt1.setInt(1, clientID);
			rs1 = stmt1.executeQuery();

			ArrayList<Folder> rv = new ArrayList<Folder>();

			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {
				int folderID = rs1.getInt("folderID");
				int folderParentID = rs1.getInt("folderParentID");
				int folderClientID = rs1.getInt("folderClientID");
				String folderName = rs1.getString("folderName");
				int toolbarPosition = rs1.getInt("toolbarPosition");
				String folderType = "local";

				if (folderID == inboxID) {
					folderType = "inbox";
				} else if (folderParentID == 0) {
					folderType = "root";
				} else if (folderClientID != user.getClientID()) {
					folderType = "shared";
				}

				rv.add(new Folder(folderID, clientID, folderParentID,
						folderName, folderType, toolbarPosition));
			}
			conn2 = ConnectionPool.getConn();
			stmt2 = conn2
					.prepareStatement("SELECT folder.folderID, folderClientID, folderParentID, folderName, toolbarPosition FROM folder_subscription LEFT JOIN folder USING(folderID) WHERE folderClientID = ? AND accepted = 'true' ORDER BY folderName ASC;");
			stmt2.setInt(1, clientID);
			rs2 = stmt2.executeQuery();

			for (boolean valid = rs2.first(); valid; valid = rs2.next()) {
				int folderID = rs2.getInt("folderID");
				int folderParentID = rs2.getInt("folderParentID");
				int folderClientID = rs2.getInt("folderClientID");
				String folderName = rs2.getString("folderName");
				int toolbarPosition = rs2.getInt("toolbarPosition");
				String folderType = "local";

				if (folderID == inboxID) {
					folderType = "inbox";
				} else if (folderParentID == 0) {
					folderType = "root";
				} else if (folderClientID != user.getClientID()) {
					folderType = "shared";
				}

				rv.add(new Folder(folderID, clientID, folderParentID,
						folderName, folderType, toolbarPosition));
			}

			if (stmt2 != null) {
				stmt2.close();
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (conn2 != null) {
				conn2.close();
			}

			conn2 = ConnectionPool.getConn();
			stmt2 = conn2
					.prepareStatement("SELECT folderID, folderClientID, folderParentID, folderName, toolbarPosition FROM folder WHERE (folderClientID = ? AND folderParentID IS NULL);");
			stmt2.setInt(1, clientID);
			rs2 = stmt2.executeQuery();
			rs2.first(); // everyon has a root folder
			int folderID = rs2.getInt("folderID");
			int folderParentID = rs2.getInt("folderParentID");
			String folderName = rs2.getString("folderName");
			int toolbarPosition = rs2.getInt("toolbarPosition");
			String folderType = "root";
			rv.add(new Folder(folderID, clientID, folderParentID, folderName,
					folderType, toolbarPosition));
			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (conn2 != null) {
				conn2.close();
			}
		}
	}

	/**
	 * Gets the clients root folders.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @author Ross Olson
	 */
	public int getRootFolderID(ClientEntry user) throws SQLException,
			IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT folderID FROM folder WHERE folderClientID = ? AND folderParentID IS NULL");
			stmt1.setInt(1, user.getClientID());
			rs1 = stmt1.executeQuery();
			rs1.first();
			return rs1.getInt("folderID");

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Gets the the number of chipmark a client has.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @author Ross Olson
	 */
	public int countUserBookmarks(ClientEntry user) throws SQLException,
			IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT COUNT(linkid) AS count FROM link WHERE linkClientID = ?");
			stmt1.setInt(1, user.getClientID());
			rs1 = stmt1.executeQuery();
			rs1.first();
			return rs1.getInt("count");
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Gets the clients folder id by folder name ** SHOULD ONLY BE USED FOR
	 * JUNIT TESTING **
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @author mcassano
	 */
	public int getFolderIDByName(ClientEntry user, String folderName)
			throws SQLException, IllegalAccessException, NoSuchFieldException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT folderID FROM folder WHERE folderClientID = ? AND folderName = ?");
			stmt1.setInt(1, user.getClientID());
			stmt1.setString(2, folderName);
			rs1 = stmt1.executeQuery();
			int folderID = -1;
			if (rs1.first()) {
				folderID = rs1.getInt("folderID");
			}

			return folderID;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Gets the clients inbx folder ID.
	 * 
	 * @param user
	 *            The user presently logged in.
	 * @author Zach Snow
	 */
	public int getInboxFolderID(ClientEntry user) throws SQLException,
			IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientInboxID FROM client WHERE clientID = ?");
			stmt1.setInt(1, user.getClientID());
			rs1 = stmt1.executeQuery();
			if (!rs1.first()) {
				return 0;
			}
			int inboxID = rs1.getInt("clientInboxID");
			if (inboxID > 0) {
				return inboxID;
			} else {
				return 0;
			}
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * 
	 * @param sender
	 * @param user
	 * @param strLinkName
	 * @param strLinkURL
	 * @param strLinkDesc
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 */
	public boolean sendLinkToUser(ClientEntry sender, ClientEntry user,
			String strLinkName, String strLinkURL, String strLinkDesc)
			throws SQLException, IllegalAccessException {

		ArrayList<String> newFlag = new ArrayList<String>();
		newFlag.add("new");
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			// Find the specified user's inbox
			int iInboxID = getInboxFolderID(user);
			if (iInboxID == 0) {
				return false;
			}
			stmt1 = conn1
					.prepareStatement("SELECT COUNT(linkID) AS count FROM link WHERE linkFolderParentID = ?");
			stmt1.setInt(1, iInboxID);
			rs1 = stmt1.executeQuery();
			if (!rs1.first()) {
				return false;
			}
			int inboxSize = rs1.getInt("count");
			if (inboxSize >= MAXINBOXSIZE) {
				return false;
			}
			if (stmt1 != null) {
				stmt1.close();
			}

			Date now = new Date();

			// Set up description
			strLinkDesc = "Sent by: " + sender.getClientName() + ", "
					+ DateFormat.getInstance().format(now) + ". " + strLinkDesc;

			// Add a link
			try {
				// links are always sent as private
				addBookmark(user, strLinkName, strLinkURL, "private",
						strLinkDesc, iInboxID, newFlag, 0);
			} catch (NoSuchFieldException e) {
				// Nothing can be done about this. It must mean that some
				// massive database failure occurred, the best we can do
				// here is return failure.
				return false;
			}
			return true;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * 
	 * @param strUser
	 * @return
	 * @throws SQLException
	 */
	public ClientEntry getUserByName(String strUser) throws SQLException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientID, clientName, clientMail, clientPass, clientAdultFilter, preferredLang, userRole FROM client WHERE clientName = ? LIMIT 1");
			stmt1.setString(1, strUser);
			rs1 = stmt1.executeQuery();

			// Make sure that there is a result:
			if (!rs1.first()) {
				return null;
			}
			// Construct a new ClientEntry
			boolean clientAdultFilterDB;
			if (rs1.getString("clientAdultFilter").equals("false")) {
				clientAdultFilterDB = false;
			} else {
				clientAdultFilterDB = true;
			}
			return new ClientEntry(rs1.getInt("clientID"), rs1
					.getString("clientName"), rs1.getString("clientMail"), rs1
					.getString("clientPass"), clientAdultFilterDB, rs1
					.getString("preferredLang"),rs1.getString("userRole"));
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * 
	 * @param strUser
	 * @return
	 * @throws SQLException
	 */
	public ClientEntry getUserByID(int strUser) throws SQLException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientID, clientName, clientMail, clientPass, clientAdultFilter, preferredLang, userRole FROM client WHERE clientID = ? LIMIT 1");
			stmt1.setInt(1, strUser);
			rs1 = stmt1.executeQuery();

			// Make sure that there is a result:
			if (!rs1.first()) {
				return null;
			}
			// Construct a new ClientEntry
			boolean clientAdultFilterDB;
			if (rs1.getString("clientAdultFilter").equals("false")) {
				clientAdultFilterDB = false;
			} else {
				clientAdultFilterDB = true;
			}
			return new ClientEntry(rs1.getInt("clientID"), rs1
					.getString("clientName"), rs1.getString("clientMail"), rs1
					.getString("clientPass"), clientAdultFilterDB, rs1
					.getString("preferredLang"), rs1.getString("userRole"));
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Get client username from client email address
	 * 
	 * @param userMail
	 *            client email address
	 * @return client username
	 * @throws SQLException
	 */
	public ArrayList<String> getUserByMail(String userMail) throws SQLException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientName FROM client WHERE clientMail = ?");
			stmt1.setString(1, userMail);
			rs1 = stmt1.executeQuery();

			// Make sure that there is a result
			if (!rs1.first()) {
				return null;
			}
			ArrayList<String> userNames = new ArrayList<String>();
			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {
				userNames.add(rs1.getString("clientName"));
			}
			return userNames;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * This method returns true if user really has the buddy fromMyBuddy
	 * 
	 * @param user
	 *            - owner of the buddy
	 * @param fromMyBuddy
	 *            - buddy with this person?
	 * @return true, they are buddies, false they are not
	 * @throws SQLException
	 */
	public boolean validateBuddy(ClientEntry user, String fromMyBuddy)
			throws SQLException {
		ClientEntry buddy = this.getUserByName(fromMyBuddy);
		if (buddy == null)
			return false;
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		if (user == null)
			return false;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT buddyID FROM buddy WHERE ( buddyID = ? AND ownerID = ? AND buddyAccepted = 'true' ) LIMIT 1");
			stmt1.setLong(1, buddy.getClientID());
			stmt1.setLong(2, user.getClientID());
			rs1 = stmt1.executeQuery();

			// Make sure that there is a result:
			if (!rs1.first()) {
				return false;
			}
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
		// all
		return true;
	}

	/**
	 * This method returns all of the user's PUBLIC chipmarks
	 * 
	 * @param user
	 *            - the owner of the chipmarks that are to be returned
	 * @return ArrayList of Bookmarks of the owner.
	 * @throws SQLException
	 */
	public ArrayList<Bookmark> getAllUserPublicBookmarks(ClientEntry user)
			throws SQLException {
		Connection conn1 = null;
		Connection conn2 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		try { // see finally that closes DB conection

			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT * FROM link WHERE ( link.linkClientID = ? and link.linkPermission = 'public' ) ORDER BY linkSortOrder DESC");

			int clientID = user.getClientID();
			stmt1.setInt(1, clientID);

			// The return ArrayList
			ArrayList<Bookmark> rv = new ArrayList<Bookmark>();

			// The result set
			rs1 = stmt1.executeQuery();

			conn2 = ConnectionPool.getConn();
			stmt2 = conn2
					.prepareStatement("SELECT * FROM label WHERE labelLinkID = ?");

			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {
				// Get all its data
				int linkID = rs1.getInt("linkID");
				int linkClientID = rs1.getInt("linkClientID");
				String linkName = rs1.getString("linkName");
				String linkURL = rs1.getString("linkURL");
				String linkPermission = rs1.getString("linkPermission");
				String linkDescription = rs1.getString("linkDescription");
				int linkFolderParentID = rs1.getInt("linkFolderParentID");
				int toolbarPosition = rs1.getInt("toolbarPosition");

				// Query the label table to find out what labels the bookmark
				// has
				stmt2.setInt(1, linkID);
				rs2 = stmt2.executeQuery();

				// Create the bookmark object to add to the list
				Bookmark toAdd = new Bookmark(linkID, linkClientID, linkName,
						linkURL, linkPermission, linkDescription,
						linkFolderParentID, toolbarPosition);

				// Get all the bookmark's labels and stick them in the bookmark
				// object
				String labelName = null;
				for (boolean truth = rs2.first(); truth; truth = rs2.next()) {
					labelName = rs2.getString("labelName");
					toAdd.addLabel(labelName);
				}

				// Add the bookmark
				rv.add(toAdd);
			}

			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
			if (conn2 != null) {
				conn2.close();
			}
		}
	}

	/**
	 * This method returns a TOP-X recent list of the user's PUBLIC chipmarks
	 * 
	 * @param user
	 *            - the owner of the chipmarks that are to be returned
	 * @param numberOfBookmarks
	 *            - number of bookmarks to include in result
	 * @return ArrayList of Bookmarks of the owner.
	 * @throws SQLException
	 */
	public ArrayList<Bookmark> getTopXRecentUserPublicBookmarks(
			ClientEntry user, int numberOfBookmarks) throws SQLException {
		Connection conn1 = null;
		Connection conn2 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		try { // see finally that closes DB conection

			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT * FROM link WHERE (link.linkClientID = ? AND link.linkPermission='public') ORDER BY linkLastMod DESC LIMIT "
							+ String.valueOf(numberOfBookmarks) + ";");

			int clientID = user.getClientID();
			stmt1.setInt(1, clientID);

			// The return ArrayList
			ArrayList<Bookmark> rv = new ArrayList<Bookmark>();

			// The result set
			rs1 = stmt1.executeQuery();

			conn2 = ConnectionPool.getConn();
			stmt2 = conn2
					.prepareStatement("SELECT * FROM label WHERE labelLinkID = ?");

			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {
				// Get all its data
				int linkID = rs1.getInt("linkID");
				int linkClientID = rs1.getInt("linkClientID");
				String linkName = rs1.getString("linkName");
				String linkURL = rs1.getString("linkURL");
				String linkPermission = rs1.getString("linkPermission");
				String linkDescription = rs1.getString("linkDescription");
				int linkFolderParentID = rs1.getInt("linkFolderParentID");
				int toolbarPosition = rs1.getInt("toolbarPosition");

				// Query the label table to find out what labels the bookmark
				// has
				stmt2.setInt(1, linkID);
				rs2 = stmt2.executeQuery();

				// Create the bookmark object to add to the list
				Bookmark toAdd = new Bookmark(linkID, linkClientID, linkName,
						linkURL, linkPermission, linkDescription,
						linkFolderParentID, toolbarPosition);

				// Get all the bookmark's labels and stick them in the bookmark
				// object
				String labelName = null;
				for (boolean truth = rs2.first(); truth; truth = rs2.next()) {
					labelName = rs2.getString("labelName");
					toAdd.addLabel(labelName);
				}

				// Add the bookmark
				rv.add(toAdd);
			}

			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
			if (conn2 != null) {
				conn2.close();
			}
		}
	}

	/**
	 * Adds a recommendation for a client into the db.
	 * 
	 * @param clientID
	 *            The client id
	 * @param linkID
	 *            The link id
	 * @throws SQLException
	 */
	public void insertRecommendation(int clientID, int linkID)
			throws SQLException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("insert into recommendation (recommendationClientID, recommendationUrlID) values (?,?)");
			stmt1.setInt(1, clientID);
			stmt1.setInt(2, linkID);
			stmt1.executeUpdate();

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Removes all recommendations for a particular user
	 * 
	 * @param clientID
	 *            The client id
	 * @throws SQLException
	 */
	public void deleteRecommendationsForClient(int clientID)
			throws SQLException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("delete from recommendation where recommendationClientID = ?");
			stmt1.setInt(1, clientID);
			stmt1.executeUpdate();

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * Retrieves all recommendations for a user
	 * 
	 * @param user
	 *            A ClientEntry object (not null)
	 * @return An ArrayList (Bookmark) of recommendations, never null
	 * @throws SQLException
	 */
	public ArrayList<LinkNameUrlObject> retrieveRecommendationsForClient(
			ClientEntry user) throws SQLException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try { // see finally that closes DB conection

			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("select url from url, recommendation where urlID = recommendationUrlID and recommendationClientID = ?");
			int clientID = user.getClientID();
			stmt1.setInt(1, clientID);
			// The return ArrayList
			ArrayList<LinkNameUrlObject> rv = new ArrayList<LinkNameUrlObject>();

			// The result set
			rs1 = stmt1.executeQuery();

			for (boolean valid = rs1.first(); valid; valid = rs1.next()) {

				String linkURL = rs1.getString("url");

				// Create the bookmark object to add to the list
				LinkNameUrlObject toAdd = new LinkNameUrlObject(null, linkURL);

				// Add the bookmark
				rv.add(toAdd);
			}

			return rv;

		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	public String getClientFilterByClientName(String clientName)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			String clientF = null;
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientAdultFilter FROM client WHERE clientName = ?");
			stmt1.setString(1, clientName);
			rs1 = stmt1.executeQuery();
			if (rs1.first()) {
				// If a matching ID was found
				clientF = rs1.getString("clientAdultFilter");
			}
			return clientF;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}

	}

	public ArrayList<String> getDistinctUserEmails() throws SQLException,
			NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			ArrayList<String> distinctClientEmails = new ArrayList<String>();
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT DISTINCT clientMail FROM client");
			rs1 = stmt1.executeQuery();
			if (rs1.first()) {
				// If 1 email adress was found
				while (rs1.next()) {
					distinctClientEmails.add(rs1.getString("clientMail"));
				}
			}
			return distinctClientEmails;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}

	}

	public String getPrefLangByClientName(String clientName)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			String clientPL = null;
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT preferredLang FROM client WHERE clientName = ?");
			stmt1.setString(1, clientName);
			rs1 = stmt1.executeQuery();
			if (rs1.first()) {
				// If a matching ID was found
				clientPL = rs1.getString("preferredLang");
			}

			return clientPL;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}

	}

	// Yes... this class does eventually end

	// No it doesn't! =)

	/**
	 * Method that retrieves a facebookID based on a given clientName
	 * 
	 * @param clientName
	 *            name of client
	 * @return facebookID for the given client - null if there is no facebookID
	 *         associated with the given account
	 */
	public Integer getFacebookIdByClientName(String clientName)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			Integer clientF = null;
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientFacebookID FROM client WHERE clientName = ?");
			stmt1.setString(1, clientName);
			rs1 = stmt1.executeQuery();
			if (rs1.first()) {
				// If a matching ID was found
				clientF = rs1.getInt("clientFacebookID");
			}
			return clientF;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}

	}

	/**
	 * Method that retrieves a facebookID based on a given clientName
	 * 
	 * @param clientID
	 *            name of client
	 * @return facebookID for the given client - null if there is no facebookID
	 *         associated with the given account
	 */
	public Integer getFacebookIdByClientID(Integer clientID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			Integer clientF = null;
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientFacebookID FROM client WHERE clientID = ?");
			stmt1.setInt(1, clientID);
			rs1 = stmt1.executeQuery();
			if (rs1.first()) {
				// If a matching ID was found
				clientF = rs1.getInt("clientFacebookID");
			}
			return clientF;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}

	}

	/**
	 * Method that retrieves a clientID based on a given facebookID
	 * 
	 * @param facebookID
	 * @return clientID for the given client - null if there is no clientID
	 *         associated with the given facebook account
	 */
	public Integer getClientIDByFacebookID(Integer clientID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {

		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			Integer clientF = null;
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientID FROM client WHERE clientFacebookID = ?");
			stmt1.setInt(1, clientID);
			rs1 = stmt1.executeQuery();
			if (rs1.first()) {
				// If a matching ID was found
				clientF = rs1.getInt("clientID");
			}
			return clientF;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}

	}

	/**
	 * Method that retrieves a clientPass based on a given clientName. This
	 * allows the password to be retrieved without having the user logged in.
	 * 
	 * @param clientName
	 * @return encrypted password for the given clientName - "" if there is no
	 *         password associated with the given clientName
	 */
	public String getClientPassByClientName(String clientName)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		String clientPassword = "";
		Connection conn1 = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		try {
			conn1 = ConnectionPool.getConn();
			stmt1 = conn1
					.prepareStatement("SELECT clientPass FROM client WHERE clientName = ?");
			stmt1.setString(1, clientName);
			rs1 = stmt1.executeQuery();
			if (rs1.first()) {
				// If a matching Password was found
				clientPassword = rs1.getString("clientPass");
			}
			return clientPassword;
		} finally {
			if (stmt1 != null) {
				stmt1.close();
			}
			if (rs1 != null) {
				rs1.close();
			}
			if (conn1 != null) {
				conn1.close();
			}
		}
	}

	/**
	 * 
	 * @param clientFacebookID
	 * @param clientName
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public boolean setClientFacebookIDByClientName(String clientFacebookID,
			String clientName) throws SQLException, NoSuchFieldException,
			IllegalAccessException {
		return setXByY("clientFacebookID", "clientName", clientFacebookID,
				clientName);
	}

	/**
	 * 
	 * @param clientFacebookID
	 * @return
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public boolean removeClientFacebookIDByFacebookID(String clientFacebookID)
			throws SQLException, NoSuchFieldException, IllegalAccessException {
		return setXByY("clientFacebookID", "clientFacebookID", null,
				clientFacebookID);
	}

}
