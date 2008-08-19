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

package bookmarks.buddylist;

import java.sql.SQLException;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class BuddyList{

	/**
	 * 
	 */
	protected ArrayList<Buddy> buddies;

	/**
	 * 
	 */
	protected int owner;

	/**
	 * 
	 * @param ownerID
	 */
	public BuddyList(int ownerID){ 
		this.buddies = new ArrayList<Buddy>(); 
		this.owner = ownerID;
	}

	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public Document toXml() throws SQLException {
		Buddy curBuddy = null;
		Document buddyList = DocumentHelper.createDocument();
		Element root = buddyList.addElement("buddylist");

		for (int i=0 ; i<this.buddies.size() ; i++){
			Element buddy = root.addElement("buddy");
			Element username = buddy.addElement("username");
			Element nickname = buddy.addElement("nickname");

			curBuddy = (Buddy)this.buddies.get(i);
			username.addText(curBuddy.username);
			if (curBuddy.nickname != null)
				nickname.addText(curBuddy.nickname);
		}

		return buddyList;

	}

	/**
	 * 
	 * @return
	 */
	public String toHtml(){ 
		String retval = "<H1>Buddy List</H1>";
		Buddy curBuddy;
		for (int i=0 ; i<this.buddies.size() ; i++){
			curBuddy = (Buddy)this.buddies.get(i);
			if (curBuddy.nickname == null){
				retval += curBuddy.username;
			}
			else{
				retval += curBuddy.nickname;
				retval += " (";
				retval += curBuddy.username;
				retval += ")";
			}
			retval += "<br>";
		}
		return retval;
	}

	/**Adds the specified buddy to this buddy list.  If the buddy is already in
	 *the buddy list, nothing is changed and this method returns false.
	 *@return true if the buddy was added to this list.
	 * @throws SQLException 
	 *@throws IllegalArgumentException if the name to be added is null or the empty string.
	 */
	public boolean add(String username) throws SQLException {
		return add(username, null);
	}

	/**
	 * 
	 * @param username
	 * @param nick
	 * @return
	 * @throws SQLException
	 */
	public boolean editBuddyNickname(String username, String nick) throws SQLException{
		return false;
	}

	/**Adds the specified buddy to this buddy list, and associates a nickname with it.
	 *If the buddy is already in
	 *the buddy list, nothing is changed and this method returns false.  If the nickname
	 *is null or the empty string, it is ignored.
	 *@return true if the buddy was added to this list.
	 * @throws SQLException 
	 *@throws IllegalArgumentException if the username is null
	 */
	public boolean add(String username, String nick) throws SQLException {
		Buddy newBuddy=null;

		if (username == null)
			throw new IllegalArgumentException();

		if (nick != null && nick.equals(""))
			nick=null;

		newBuddy = new Buddy();
		newBuddy.username = username;
		newBuddy.nickname = nick;
		this.buddies.add(newBuddy);
		return true;
	}

	/**Removes the specified buddy from this buddy list.  If the buddy does not exist in this
	 *buddy list, nothing is changed and this method returns false
	 *@return true if the buddy was removed from this list
	 */
	public boolean remove(String name) throws SQLException{
		int index;
		Buddy buddy = new Buddy(name);

		if (name == null || name.equals(""))
			throw new IllegalArgumentException();

		index = this.buddies.indexOf(buddy);
		if (index == -1)
			return false;
		else {
			this.buddies.remove(index);
			return true;
		}
	}

	/** returns true if the buddy list contains the specified username 
	 * @throws SQLException */
	public boolean contains(String username) throws SQLException {
		Buddy curBuddy;
		for (int i=0 ; i<this.buddies.size() ; i++){
			curBuddy = this.buddies.get(i);
			if (curBuddy.username.equals(username))
				return true;
		}
		return false;
	}


	public class Buddy {

		public String username;
		public String nickname;

		/**
		 * 
		 *
		 */
		public Buddy(){ username=nickname = null; }

		/**
		 * 
		 * @param name
		 */
		public Buddy(String name){ username=name; nickname=null;}

		/**
		 * 
		 * @param name
		 * @param nick
		 */
		public Buddy(String name, String nick){
			username = name;
			nickname = nick;
		}

		@Override
		public boolean equals(Object bud){
			Buddy b = (Buddy)bud;
			return b.username.equals(this.username);
		}

	}

}

