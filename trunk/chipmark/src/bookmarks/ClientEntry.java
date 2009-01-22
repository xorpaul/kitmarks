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

/**
 * A <code>ClientEntry</code> object represents a user that is logged in.  
 * When a user logs in, an object is created holding their username, id number,
 * email address, password, and preferences.  
 */
public class ClientEntry {
    /**
     * The unique identifying number for this client.
     */
    private int clientID;
    
    /**
     * The user name for this client.
     */
    private String clientName;

    /**
     * The email address for this client.
     */
    private String clientMail;
    
    /**
     * The password for this client.  If the password should always be hashed;
     * if pulled from the database, it will be hashed using md5.
     */
    private String clientPass;

    /**
     * The preferences set for this client.
     */
    public UserPreferences prefs;
    
    /**
     *  True if the user has adult filter on.
     */
	private boolean filterAdultContent;
	
    /**
     *  Contains the user preferred language.
     */
	private String prefLang;
	
    /**
     *  Contains the user role (student or tutor).
     */
	private String role;
	
    /**
     * Constructor to create a new <code>ClientEntry</code> for when a user
     * logs in.  
     *
     * @param n_clientID the unique identifying number for the user this object 
     * represents
     * @param n_clientName the user name for the user this object represents
     * @param n_clientMail the email address for the user this object represents
     * @param n_clientPass the password for the user this object represents.  
     * The password always be hashed.  If the password is pulled from the 
     * database, it will be hashed uing MD5.
     */
    public ClientEntry(int n_clientID,
		       String n_clientName,
		       String n_clientMail,
		       String n_clientPass) {
    	clientID   = n_clientID;
    	clientName = n_clientName;
    	clientMail = n_clientMail;
    	clientPass = n_clientPass;
    	prefs = new UserPreferences();
    	filterAdultContent = true;
    }
    
    public ClientEntry(int n_clientID,
		       String n_clientName,
		       String n_clientMail,
		       String n_clientPass,
		       boolean n_adultFilter,
		       String n_prefLang, String n_role) {
 	clientID   = n_clientID;
 	clientName = n_clientName;
 	clientMail = n_clientMail;
 	clientPass = n_clientPass;
 	prefs = new UserPreferences();
 	filterAdultContent = n_adultFilter;
 	prefLang = n_prefLang;
 	role = n_role;
 }

    /**
     * Returns the unique identifying number for this user.
     *
     * @return the unique identifying number for this user.
     */
    public int    getClientID()   { return clientID;   }

    /**
     * Returns the user name for this user.
     *
     * @return the username for this user
     */
    public String getClientName() { return clientName; }

    /**
     * Returns the email address associated with this user.
     *
     * @return the email address for this user
     */
    public String getClientMail() { return clientMail; }

    /**
     * Returns the password for this user.  The password should always be hashed,
     * if the password was taken from the database it will be hashed using MD5.
     * Note that the class does not hash passwords, they should come that way.
     *
     * @return the hashed password for this user.
     */
    public String getClientPass() { return clientPass; }

    /**
     * Sets the unquie identifying number for this user to another number.
     * Care should be taken to ensure the new number is also unique.
     *
     * @param n_clientID the new unquie identifying number for this user
     */
    public void setClientID(int n_clientID) { clientID = n_clientID; }

    /**
     * Sets the username for this user to a new name.  Care should be taken to
     * ensure the new name is not already in use.
     *
     * @param n_clientName the new username for this user
     */
    public void setClientName(String n_clientName) { clientName = n_clientName; }

    /**
     * Sets the email address for this user to a new email address.
     *
     * @param n_clientMail the new email address for this user
     */
    public void setClientMail(String n_clientMail) { clientMail = n_clientMail; }

    /**
     * Sets the password for this user.  Note that this method does not hash
     * the password the password must be hashed before sending it to this
     * method.  
     *
     * @param n_clientPass the new hashed password for this user
     */
    public void setClientPass(String n_clientPass) { clientPass = n_clientPass; }

    /**
     * check to see if adult content filter is on for this user
     * @return true, one OR false, off
     */
	public boolean isOnlySafe() {
		return filterAdultContent;
	} 
	
    /**
     * sets the adult content filter
     * @return true, one OR false, off
     */
	public void setOnlySafe(boolean in) {
		filterAdultContent = in;
	} 
	
	 /**
     * get the preferred Lang of the user
     * @return String with value "ger" OR "eng"
     */
	public String getPreferredLang() {
		return prefLang;
	} 
	
    /**
     * sets the adult content filter
     * @return true, one OR false, off
     */
	public void setPreferredLang(String lang) {
		prefLang = lang;
	} 
	
	 /**
     * get the role of the user
     * @return String with value "student" OR "tutor"
     */
	public String getUserRole() {
		return role;
	} 
	
    /**
     * sets the adult content filter
     * @return true, one OR false, off
     */
	public void setUserRole(String setRole) {
		role = setRole;
	} 

}
