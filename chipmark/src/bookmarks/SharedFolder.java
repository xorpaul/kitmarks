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
 * Shared Folders are the same as folders, except the type is always shared and
 * it has an ownerName attribute for ease of use.
 * @author Brian Terlson
 *
 */
public class SharedFolder extends Folder {
	private String ownerName;
	
	/*
	 * Constructor notes:
	 * 	the folder's parentID isn't necessarily needed.  Its only kept here to keep folders and shared
	 *  folders as much the same as possible at this point.
	 */
	public SharedFolder(int n_folderID, int n_folderClientID, int n_folderParentID, String n_folderName) {
		super(n_folderID, n_folderClientID, n_folderParentID, n_folderName, "shared");
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public void setOwnerName(String name) {
		ownerName = name;
	}
}
