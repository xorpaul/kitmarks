/*
 -------------------------------------------------------------------------------------------------
 Copyright, 2005, 2006 Chipmark.

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

package bookmarks.buddylist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.dom4j.Document;

import bookmarks.ClientEntry;
import bookmarks.ConnectionPool;
import bookmarks.DatabaseWrapper;

public class MySqlBuddyList extends BuddyList{

	/**
	 * 
	 * @param ownerID
	 */
	public MySqlBuddyList(int ownerID){
		super(ownerID);		
	}

	/**
	 * @return Document
	 * @throws SQLException 
	 */
	public Document toXml() throws SQLException{
		loadFromDatabase();
		return super.toXml();
	}

	public boolean contains(String username) throws SQLException {
		return dbContains(username);
	}
	
	public boolean containsPending(String username) throws SQLException {
		return dbContainsPending(username);
	}

	/**Adds the specified buddy to this buddy list.  If the buddy is already in
	 *the buddy list, nothing is changed and this method returns false.
	 *@return true if the buddy was added to this list.
	 *@throws SQLException if a database error occured while adding the buddy
	 *@throws IllegalArgumentException if the name to be added is null or the empty string.
	 */

	public boolean add(String username) throws SQLException {
		return add(username, null);
	}

	/**Adds the specified buddy to this buddy list, and associates a nickname with it.
	 *If the buddy is already in
	 *the buddy list, nothing is changed and this method returns false.  If the 
	 *nickname is null or the empty string it is ignored
	 *@return true if the buddy was added to this list.
	 *@throws SQLException if a database error occured while adding the buddy
	 *@throws IllegalArgumentException if the username is null or the empty string
	 */

	public boolean add(String username, String nick) throws SQLException{
		return add(username, nick, false);
	}

	/**
	 * 
	 * @param username
	 * @param nick
	 * @param accepted
	 * @return
	 * @throws SQLException 
	 */
	public boolean add(String username, String nick, boolean accepted) throws SQLException {

		Connection connection = null;
		PreparedStatement st = null;
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		if (username == null || username.equals(""))
			throw new IllegalArgumentException();

		if (dbContains(username))
			throw new IllegalArgumentException();

		try {
			//Get the user ID:
			ClientEntry userEntry = db.getUserByName(username);
			if(userEntry == null)
				return false;
			if (nick == null)
				nick = username;
			connection = ConnectionPool.getConn();
			st = connection.prepareStatement("INSERT INTO buddy (buddyID, nickname, ownerID, buddyAccepted) VALUES (?,?,?,?)");
			st.setInt(1, userEntry.getClientID());
			st.setString(2, nick);
			st.setInt(3, owner);
			if(accepted){
				st.setString(4, "true");
			} else {
				st.setString(4, "false");
			}
			st.executeUpdate();
		} finally {
			if(st != null) {
				st.close();
			}
			if(connection != null) {
				connection.close();
			}
		}

		return true;
	}

	/**Edits a the nickname of a particular buddy.  If the nickname is null or the empty string, 
	 *the nickname is cleared.  If the username is not in this buddy list, this method returns
	 *false.
	 *@return true if the buddy's nickname was changed
	 *@throws SQLException if a database error occured while editing the buddy
	 *@throws IllegalArgumentException if the username is null or the empty string
	 */	
	public boolean editBuddyNickname(String username, String nick) throws SQLException{                         
		if (username == null || username.equals(""))
			throw new IllegalArgumentException();

		Connection connection = null;
		PreparedStatement st = null;
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		try {
			ClientEntry userEntry = db.getUserByName(username);

			if (!dbContains(username))
				return false;

			if (nick == null)
				nick = "";

			connection = ConnectionPool.getConn();
			st = connection.prepareStatement("UPDATE buddy SET nickname=? WHERE ownerID=? AND buddyID=?;");
			st.setString(1, nick);
			st.setInt(2, owner);
			st.setInt(3, userEntry.getClientID());

			st.executeUpdate();

		} finally {
			if(st != null) {

				st.close();

			}
			if(connection != null) {

				connection.close();

			}
		}

		return true;
	}

	/** Removes the specified buddy from this buddy list.  If the buddy does not exist in this
	 * buddy list, nothing is changed and this method returns false
	 * @return true if the buddy was removed from this list
	 * @throws SQLException if a database error occured while removing the buddy
	 */
	public boolean remove(String username) throws SQLException{
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		PreparedStatement st3 = null;
		Connection connection = null;

		if (username == null || username.equals(""))
			throw new IllegalArgumentException();


		if (!dbContains(username))
			return false;

		//Get user ID:
		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();


		try {
			ClientEntry userEntry = db.getUserByName(username);

			connection = ConnectionPool.getConn();
			st = connection.prepareStatement("DELETE FROM buddy WHERE ownerID=? AND buddyID=?");
			st.setInt(1,owner);
			st.setInt(2,userEntry.getClientID());
			st.executeUpdate();
			st.setInt(1,userEntry.getClientID());
			st.setInt(2,owner);
			st.executeUpdate();
			
			st2 = connection.prepareStatement("delete from folder_subscription where clientID = ?");
			st2.setInt(1,owner);
			st2.executeUpdate();
			st3 = connection.prepareStatement("delete folder_subscription from folder_subscription left join folder on folder_subscription.folderID = folder.folderID WHERE folder.folderClientID=?");
			st3.setInt(1, owner);
			st3.executeUpdate();
			
		} finally {
			if(st != null) {
				st.close();
			}
			if(st2 != null) {
				st2.close();
			}
			if(st3 != null) {
				st3.close();
			}
			if(connection != null) {
				connection.close();
			}
		}

		return true;
	}

	/**
	 * 
	 * @param username
	 * @return
	 * @throws SQLException 
	 */
	private boolean dbContains(String username) throws SQLException{

		Connection connection = null;
		PreparedStatement st = null;

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		try {
			ClientEntry userEntry = db.getUserByName(username);
			if(userEntry == null)
				return false;

			connection = ConnectionPool.getConn();
			st = connection.prepareStatement("SELECT * FROM buddy WHERE ownerID=? AND buddyID=? AND buddyAccepted='true' LIMIT 1");
			st.setInt(1,owner);
			st.setInt(2,userEntry.getClientID());
			ResultSet result =  st.executeQuery();

			return result.first();
		} finally {
			if(st != null) {

				st.close();

			}
			if(connection != null) {

				connection.close();

			}
		}
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 * @throws SQLException 
	 */
	private boolean dbContainsPending(String username) throws SQLException{

		Connection connection = null;
		PreparedStatement st = null;

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		try {
			ClientEntry userEntry = db.getUserByName(username);
			if(userEntry == null)
				return false;

			connection = ConnectionPool.getConn();
			st = connection.prepareStatement("SELECT * FROM buddy WHERE ownerID=? AND buddyID=? AND buddyAccepted='false' LIMIT 1");
			st.setInt(1,owner);
			st.setInt(2,userEntry.getClientID());
			ResultSet result =  st.executeQuery();

			return result.first();
		} finally {
			if(st != null) {

				st.close();

			}
			if(connection != null) {

				connection.close();

			}
		}
	}
		

	/**
	 * @throws SQLException 
	 * 
	 *
	 */
	private void loadFromDatabase() throws SQLException { 
		String user, nickname;
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet result = null;

		try {
			connection = ConnectionPool.getConn();
			st = connection.prepareStatement("SELECT * FROM buddy, client WHERE buddy.ownerID=? AND buddy.buddyID=client.clientID AND buddy.buddyAccepted='true' ORDER BY buddy.nickname ASC");
			st.setInt(1,owner);

			result = st.executeQuery();

			while (result.next()){
				user = result.getString("clientName");
				nickname = result.getString("nickname");

				if (nickname == null){
					super.add(user, user);
				}
				else{
					super.add(user,nickname);
				}
			}
		} finally {
			if(result != null) {

				result.close();

			}
			if(st != null) {

				st.close();

			}
			if(connection != null) {

				connection.close();

			}
		}
	}


	/**
	 * This method returns all pending buddy requests from other people.
	 * @return returns an ArrayList<Buddy> of pending buddy requests
	 * @throws SQLException 
	 */	
	public ArrayList<Buddy> getBuddyRequests() throws SQLException{
		String user;
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet result = null;
		ArrayList<Buddy> returnMe = new ArrayList<Buddy>();

		try {
			connection = ConnectionPool.getConn();
			st = connection.prepareStatement("SELECT * FROM buddy, client WHERE buddy.buddyID=? AND buddy.buddyAccepted='false' AND buddy.ownerID=client.clientID ORDER BY client.clientName ASC");
			st.setInt(1,owner);

			result = st.executeQuery();

			while (result.next()){
				user = result.getString("clientName");
				//nickname = result.getString("nickname");
				// NOTE: it doesn't make sense to return a nickname
				returnMe.add(new BuddyList.Buddy(user, null));
			}
		} finally {
			if(result != null) {

				result.close();

			}
			if(st != null) {

				st.close();

			}
			if(connection != null) {

				connection.close();

			}
		}

		return returnMe;
	}

	/**
	 * This method returns all pending buddy requests that you have for other people.
	 * @return returns an ArrayList<Buddy> of pending buddy requests
	 * @throws SQLException 
	 */	
	public ArrayList<Buddy> getPendingBuddyRequests() throws SQLException{
		String user;
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet result = null;
		ArrayList<Buddy> returnMe = new ArrayList<Buddy>();

		try {
			connection = ConnectionPool.getConn();
			st = connection.prepareStatement("SELECT * FROM buddy, client WHERE buddy.buddyID=client.clientID AND buddy.buddyAccepted='false' AND buddy.ownerID=? ORDER BY client.clientName ASC");
			st.setInt(1,owner);

			result = st.executeQuery();

			while (result.next()){
				user = result.getString("clientName");
				//nickname = result.getString("nickname");
				// NOTE: it doesn't make sense to return a nickname
				returnMe.add(new BuddyList.Buddy(user, null));
			}
		} finally {
			if(result != null) {

				result.close();

			}
			if(st != null) {

				st.close();

			}
			if(connection != null) {

				connection.close();

			}
		}

		return returnMe;
	}

	/**
	 * This method persists acceptence or rejection of a
	 * buddy.  It also adds the requtor as a buddy to the 
	 * requestee, no acceptance is nessesary, since the user
	 * has originally requested the requestee as his or her buddy.
	 * @param username, owner of pending buddy request
	 * @param accepted, "1" if accepted, "0" if rejected
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public void setAccepted(String username, String accepted) throws SQLException{
		if (username == null || username.equals("") || accepted == null || (!accepted.equals("1") && !accepted.equals("0")))
			throw new IllegalArgumentException();

		Connection connection = null;
		PreparedStatement st = null;

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();
		try {
			ClientEntry userEntry = db.getUserByName(username);

			connection = ConnectionPool.getConn();
			if (accepted.equals("0")){//delete
				st = connection.prepareStatement("DELETE FROM buddy WHERE ownerID=? AND buddyID=?;");
				st.setInt(1,userEntry.getClientID());
				st.setInt(2,owner);
				st.executeUpdate();
			} else { // accept
				add(userEntry.getClientName(), userEntry.getClientName(), true);
				st = connection.prepareStatement("UPDATE buddy SET buddyAccepted='true' WHERE ownerID=? AND buddyID=?;");
				st.setInt(1, userEntry.getClientID());
				st.setInt(2, owner);
				st.executeUpdate();
			}
		} finally {

			if(st != null) {

				st.close();

			}
			if(connection != null) {

				connection.close();

			}
		}
	}
}

