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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The <code>Bookmark</code> class holds all the information of a bookmark in 
 * an object. This class provides methods to get all the bookmark information.
 */
public class Bookmark extends LinkNameUrlObject {
	/**
	 * An unique identifying number for the bookmark
	 */
	private int linkID;

	/**
	 * The identifying number of the user who owns this bookmark.
	 */
	private int linkClientID;

	/**
	 * The identifying number of the folder that contains this bookmark.
	 */
	private int linkFolderParentID;

	/**
	 * Contains the description for this bookmark.
	 */
	private String linkDescription;

	/**
	 * Contains the permission set to this bookmark, either public or private.
	 */
	private String linkPermission;

	/**
	 * List of labels assigned to this bookmark.
	 */
	private ArrayList<String> labelNames;

	/**
	 * Value for holding the Bookmarks optional toolbar position.
	 * A value of '0' means that it is not on the toolbar.
	 */
	private int toolbarPosition;
	
	/**
	 * Constructs a new <code>Bookmark</code> from the given parameters.
	 *
	 * @param n_linkID the unique identifying number for the new <code>
	 * Bookmark</code>
	 * @param n_linkClientID the unique identifying number for the client owning
	 * the new <code>Bookmark</code>
	 * @param n_linkName the name given to the new <code>Bookmark</code>
	 * @param n_linkURL the URL for the new <code>Bookmark</code>
	 * @param n_linkPermission the permission to set the new <code>Bookmark</code>
	 * to, either public or private
	 * @param n_linkDescription the description to be given to the new 
	 * <code>Bookmark</code>
	 * @param n_linkFolderParentID the unique identifying number for the parent
	 * <code>Folder</code> for the new <code>Bookmark</code>
	 */
	public Bookmark(int n_linkID,
			int n_linkClientID,
			String n_linkName,
			String n_linkURL,
			String n_linkPermission,
			String n_linkDescription,
			int n_linkFolderParentID) {
		super(n_linkName, n_linkURL);
		linkID = n_linkID;
		linkClientID = n_linkClientID;
		linkPermission = n_linkPermission;
		linkDescription = n_linkDescription;
		linkFolderParentID = n_linkFolderParentID;
		labelNames = new ArrayList<String>();
		toolbarPosition = 0;
	}

	/**
	 * Constructs a new <code>Bookmark</code> from the given parameters.
	 *
	 * @param n_linkID the unique identifying number for the new <code>
	 * Bookmark</code>
	 * @param n_linkClientID the unique identifying number for the client owning
	 * the new <code>Bookmark</code>
	 * @param n_linkName the name given to the new <code>Bookmark</code>
	 * @param n_linkURL the URL for the new <code>Bookmark</code>
	 * @param n_linkPermission the permission to set the new <code>Bookmark</code>
	 * to, either public or private
	 * @param n_linkDescription the description to be given to the new 
	 * <code>Bookmark</code>
	 * @param n_linkFolderParentID the unique identifying number for the parent
	 * <code>Folder</code> for the new <code>Bookmark</code>
	 * @param n_toolbarPosition the position of the <code>Bookmark</code> on the
	 * toolbar. A value of <code>0</code> means that it's not on the toolbar.
	 */
	public Bookmark(int n_linkID,
			int n_linkClientID,
			String n_linkName,
			String n_linkURL,
			String n_linkPermission,
			String n_linkDescription,
			int n_linkFolderParentID,
			int n_toolbarPosition) {
		super(n_linkName, n_linkURL);
		linkID = n_linkID;
		linkClientID = n_linkClientID;
		linkPermission = n_linkPermission;
		linkDescription = n_linkDescription;
		linkFolderParentID = n_linkFolderParentID;
		labelNames = new ArrayList<String>();
		toolbarPosition = n_toolbarPosition;
	}
	
	/**
	 * Returns the <code>linkID</code> for this bookmark.
	 * 
	 * @return the <code>linkID</code> of this bookmark.
	 */
	public int     getLinkID()              { return linkID;             }

	/**
	 * Returns the ID number of the user owning this bookmark.
	 *
	 * @return the linkClientID for this bookmark.
	 */
	public int     getLinkClientID()        { return linkClientID;       }

	/**
	 * Returns the description given to this bookmark.
	 * 
	 * @return the <code>linkDescription</code> for this bookmark.
	 */
	public String  getLinkDescription()     { return linkDescription;    }


	/**
	 * Returns the permissions, either private or public, this bookmark is set
	 * to.
	 * 
	 * @return the <code>linkPermission</code> associated with this bookmark.
	 */
	public String  getLinkPermission()      { return linkPermission;     }

	/**
	 * Returns the id of the folder holding this bookmark.
	 * 
	 * @return the <code>linkFolderParentID</code> of this bookmark.\
	 */
	public int     getLinkFolderParentID()  { return linkFolderParentID; }


	/**
	 * Adds another label to list associated with this bookmark. The labels
	 * are Strings used to help categorize the bookmarks.  
	 *
	 * @param name the label to be added to the list of labels
	 */
	public void    addLabel(String name) { labelNames.add(name);   }

	/**
	 * Gets the <code>Iterator</code> for the list of labels for this bookmark.
	 * 
	 * @return an <code>Iterator</code> to the list of labels in 
	 * <code>labelNames</code>
	 * @see #getLabelsArray()
	 */
	public Iterator<String> getLabels() { return labelNames.iterator(); }

	/**
	 * Gets the <code>ArrayList</code> for the <code>labelNames</code>
	 * for this bookmark.  This returns the actually list of names where as 
	 * <code>getLabels()</code> returns an <code>Iterator</code> to the 
	 * list.
	 * 
	 * @return the list of labels for this bookmark.
	 * @see #getLabels()
	 */
	public ArrayList<String> getLabelsArray()       { return labelNames; }

	/**
	 * Returns the <code>toolbarPosition</code> for this bookmark.
	 * 
	 * @return the <code>toolbarPosition</code> of this bookmark.
	 */
	public int getToolbarPosition() { return toolbarPosition; }
	
	/**
	 * Sets the <code>toolbarPosition</code> of the Bookmark.
	 * @param position The position of the Bookmark, counting from the left. <code>0</code> means that it is not on the toolbar.
	 */
	public void setToolbarPosition(int position) { toolbarPosition = position; }
	
	/**
	 * Sets the description to be associated with this bookmark.  The 
	 * discription is intended to be a longer comment explaining this bookmark.
	 *
	 * @param description the text to be set as the description
	 */
	public void setDescription(String description) { linkDescription = description; }

	/**
	 * Counts the number of matching labels passed in to the labels for this
	 * bookmark.  An <code>ArrayList</code> of possible labels is passed in 
	 * this method for matching against the labels for this bookmark.  
	 *
	 * @param labelsToMatch a list of labels to try and match
	 * @return a count of how many labels passed in matches the labels
	 * for this bookmark
	 */
	public int labelMatches(ArrayList labelsToMatch) {
		int matches = 0;
		// This is a heuristic to speed up this function: maximum number
		// of iterations is labelsToMatch.size() * labelNames.size(), but
		// contains() will usually perform less than the full number of
		// iterations while the iterator loop will not.
		if(labelsToMatch.size() > labelNames.size()) {
			for(Iterator it = getLabels();it.hasNext();) {
				String label = (String) it.next();
				if(labelsToMatch.contains(label)) {
					matches++;
				}
			}
		}
		else {
			for(Iterator it = labelsToMatch.iterator();it.hasNext();) {
				String label = (String) it.next();
				if(labelNames.contains(label)) {
					matches++;
				}
			}
		}
		return matches;
	}

	/**
	 * Converts the list of labels this bookmark to single String.  
	 * 
	 * @return a String containing all the labels
	 */
	public String labelsToString() { return labelNames.toString(); }
}
