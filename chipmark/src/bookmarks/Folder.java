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
 * A <code>Folder</code> object represents a folder that contains bookmarks. A
 * <code>Folder</code> consists of a folder id number, client or user id number,
 * parent id number, and a name for the folder.
 */
public class Folder {
	/**
	 * A number to uniquely identify the folder
	 */
	private int folderID;

	/**
	 * The unique identifying number of the client or user owning this folder
	 */
	private int folderClientID;

	/**
	 * The unique identifying number of the parent <code>Folder</code> of this
	 * folder
	 */
	private int folderParentID;
	public String[] subscribedUsers = null;
	private String folderType;
	/**
	 * The name given to this folder
	 */
	String folderName;

	/**
	 * Value for holding the Folder's optional toolbar position. A value of '0'
	 * means that it is not on the toolbar.
	 */
	private int toolbarPosition;

	/**
	 * Creates a new <code>Folder</code> with given ID number, client ID, parent
	 * folder, and name, that is not on the toolbar.
	 * 
	 * @param n_folderID
	 *            the identifying number to be given the new <code>Folder
     * </code>
	 * @param n_folderClientID
	 *            the number for the client owning this <code>
     * Folder</code>
	 * @param n_folderParentID
	 *            the number for the parent <code>Folder</code> for this
	 *            <code>Folder</code>
	 * @param n_folderName
	 *            the name given to the new <code>Folder</code>
	 */
	public Folder(int n_folderID, int n_folderClientID, int n_folderParentID,
			String n_folderName, String n_folderType) {
		folderID = n_folderID;
		folderClientID = n_folderClientID;
		folderParentID = n_folderParentID;
		if (!n_folderName.equals(""))
			folderName = n_folderName;
		else
			folderName = "unnamed";
		folderType = n_folderType;
		toolbarPosition = 0;
	}

	/**
	 * Creates a new <code>Folder</code> with given ID number, client ID, parent
	 * folder, name and position on the toolbar.
	 * 
	 * @param n_folderID
	 *            the identifying number to be given the new <code>Folder
     * </code>
	 * @param n_folderClientID
	 *            the number for the client owning this <code>
     * Folder</code>
	 * @param n_folderParentID
	 *            the number for the parent <code>Folder</code> for this
	 *            <code>Folder</code>
	 * @param n_folderName
	 *            the name given to the new <code>Folder</code>
	 * @param n_toolbarPosition
	 *            the folder's position on the toolbar
	 */
	public Folder(int n_folderID, int n_folderClientID, int n_folderParentID,
			String n_folderName, String n_folderType, int n_toolbarPosition) {
		folderID = n_folderID;
		folderClientID = n_folderClientID;
		folderParentID = n_folderParentID;
		if (!n_folderName.equals(""))
			folderName = n_folderName;
		else
			folderName = "unnamed";
		folderType = n_folderType;
		toolbarPosition = n_toolbarPosition;
	}

	public String getFolderType() {
		return folderType;
	}

	/**
	 * Gets the unique identifying number for this <code>Folder</code>.
	 * 
	 * @return the number for this <code>Folder</code>
	 */
	public int getFolderID() {
		return folderID;
	}

	/**
	 * Gets the unique identifying number for the client or user owning this
	 * <code>Folder</code>.
	 * 
	 * @return the id number of the client owning this <code>Folder</code>
	 */
	public int getFolderClientID() {
		return folderClientID;
	}

	/**
	 * Gets the unique identifying number for the parent <code>Folder</code> for
	 * this <code>Folder</code>.
	 * 
	 * @return the identifying number of the parent <code>Folder</code>
	 */
	public int getFolderParentID() {
		return folderParentID;
	}

	/**
	 * Gets the name of this <code>Folder</code>.
	 * 
	 * @return the name of this <code>Folder</code>
	 */
	public String getFolderName() {
		return folderName;
	}

	public void setFolderParentID(int parentID) {
		folderParentID = parentID;
	}

	public int getToolbarPosition() {
		return toolbarPosition;
	}
}
