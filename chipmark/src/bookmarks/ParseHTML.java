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

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/** Breakapart an HTML document.  Extract Labels (or Folders), Titles, URL's,
 * Descriptions, and Permissions if present.
 * @author Ross Anderson
 */
public class ParseHTML extends HTMLEditorKit.ParserCallback {
	/** Flag to tell if a bookmark is currently being worked with. */    
	private boolean bookmark_flag = false;           /* This flag prevents the description field of a folder from being used */
	/** Flag to tell if a folder is currently being worked with. */    
	private boolean folder_flag = false;
	/** Tag that is currently being processed. */    
	private HTML.Tag currentTag;
	/** Title of bookmark. */    
	private String title_s = "";
	/** Url of bookmark. */    
	private String url_s = "";
	/** Description of bookmark. */    
	private String description_s = "";
	/** Privacy setting of bookmark. */    
	private String privacy_s = "";
	/** Labels (as a string) of bookmark. */    
	private String labels_s = "";
	/** Folder title of bookmark. */    
	private String folder_s = "";
	/** Stack of folders.  Push folders as they are encountered, pop them as they are
	 * finished.
	 */    
	private Stack<Integer> folderStack = new Stack<Integer>();
	/** Database Wrapper used to insert bookmarks. */    
	private DatabaseWrapper db = null;
	/** User object. */    
	private ClientEntry user = null;
	/** Output used for displaying errors. */    
	//private PrintWriter out = null;
	/** Previous bookmark processed. */    
	private Bookmark mostRecentBookmark = null;
	/** Permission to be used if permissions are not present in HTML file. */    
	private String defaultPermission = "public";

	/** maximum number of chipmarks to import */
	private int maxImportNum;
	
	/** the starting max number of chipmarks to import */
	private int totalImportNum;

	/** The list of urls chipmarked by the user. Used to ensure URLs are not saved twice. */
	private ArrayList<String> urlList = new ArrayList<String>();
	
	/** The list of folders owned by the user. Used to ensure folders are not saved twice. */
	private ArrayList<Folder> folderList = null;
	
	/** Construct an HTML parser that will parse a maximum of max chipmarks.  The rest will be ignored*/    

	public ParseHTML(int max){ maxImportNum = max; totalImportNum = max; }

	/** Extracts data between opening and closing tag.
	 * @param data Incoming Text
	 * @param pos Current Parsing position
	 */    
	public void handleText(char[] data, int pos){
		if (maxImportNum < 1){ return;}

		/* Link Tag */
		if (currentTag == HTML.Tag.A){
			title_s = new String(data);
		}

		/* Description Tag */
		if (currentTag == HTML.Tag.DD){
			description_s = new String(data);
		}

		/* Folder Tag */
		if (currentTag == HTML.Tag.H3) {
			folder_s = new String(data);
		}
	}

	/** Handler function for each time a start tag is detected
	 * @param t Current tag
	 * @param a Set of HTML attributes
	 * @param pos Current Parsing position
	 */    
	public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos){

		if (maxImportNum < 1){ return;}

		currentTag = t;
		if (t == HTML.Tag.A){
			bookmark_flag = true;
			url_s = (String)a.getAttribute(HTML.Attribute.HREF);
			privacy_s = (String)a.getAttribute((String) "privacy");
			labels_s = (String)a.getAttribute((String) "labels");
		}
		else if ((t == HTML.Tag.DD) && bookmark_flag){
			bookmark_flag = true;
		}
		else{
			bookmark_flag = false;
		}

		if (t == HTML.Tag.H3){
			folder_flag = true;
		}
	}

	/** Handler function for each time an end tag is detected
	 * @param t Current tag
	 * @param pos Current Parsing position
	 */    
	public void handleEndTag(HTML.Tag t, int pos){
		//    throws NoSuchFieldException {

		if (maxImportNum < 1){ return; }

		if (t == HTML.Tag.A){
			maxImportNum--;

			ArrayList<String> labels_a = null;
			privacy_s = Utilities.eliminateNull(privacy_s);
			title_s = Utilities.eliminateNull(title_s);
			url_s = Utilities.eliminateNull(url_s);
			labels_s = Utilities.eliminateNull(labels_s);
			url_s = Utilities.addURLProtocol(url_s);

			if (!privacy_s.equals("public") && !privacy_s.equals("private")){
				privacy_s = new String(defaultPermission);
			}

			if (!labels_s.equals("")){
				labels_s = labels_s.replaceFirst("^\\[", "");
				labels_s = labels_s.replaceFirst("\\]$", "");
				labels_a = new ArrayList<String>();
				String[] split = labels_s.split(",");
				for (int i = 0; i < split.length; i++){
					labels_a.add(split[i]);
				}
			}

			if (!title_s.equals("") && !url_s.equals("") && !this.urlList.contains(url_s)){
				//If folderStack.size() is 0, then this bookmark appears outside of the netscape bookmark
				//file structure.
				if (folderStack.size() > 0){
					Integer temp = (Integer) folderStack.peek();
					int parent = temp.intValue();					
					mostRecentBookmark = addLink(title_s, url_s, privacy_s, labels_a, parent);
				}
			}

			url_s         = "";
			title_s       = "";
			description_s = "";
			privacy_s     = "";
			labels_s      = "";
		}

		/* Only read in description if the previous tag was for a Link */
		if ((t == HTML.Tag.DD) && bookmark_flag && (mostRecentBookmark != null)){
			addDescription(mostRecentBookmark, description_s);
			description_s = "";
		}

		/* End of Folder, Pop folder off the stack */
		if ((t == HTML.Tag.DL)){
			if (!folderStack.empty()){
				folderStack.pop();
			}
		}
		
		/* Folder Tag */
		if (currentTag == HTML.Tag.H3  && folder_flag) {
			// Create folder, push onto Folder Stack
			Integer temp = (Integer) folderStack.peek();
			int parent = temp.intValue();

			folder_s = Utilities.eliminateNull(folder_s);
			
			Folder folder = this.containsFolder(parent, this.folder_s);
			
			if(folder == null) {
				folder = addFolder(folder_s, parent);
			}
			
			folderStack.push(new Integer( folder.getFolderID()));
			folder_s = "";
			folder_flag = false;
		} 
	}

	/** Handler is not currently used
	 * @param t Current tag
	 * @param a HTML Attribute Set
	 * @param pos Current Parsing position
	 */    
	public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos){ }

	/** Opens file and sets up HTML parser
	 * @param file InputStream of bookmark file
	 * @param in DatabaseWrapper to be used to add folders and bookmarks
	 * @param importUser ClientEntry object to be used by database
	 * @param writer output used to report errors
	 * @param permission defaultPermission to be used if no permission are present in file
	 */    


	/* There are some funky things going on with this method.  Why construct a new parseHTML?  Why not pass 'this'? Look into this later */ 

	public String ParseFile(InputStream file, DatabaseWrapper in, ClientEntry importUser, String permission) throws ServletException{ 
		if (maxImportNum < 1){ return "Error! Maximum number of chipmarks exceeded."; }

		db = in;
		user = importUser;
		int root = -1;

		try {
			root = db.getRootFolderID(user);
		} catch (Exception e) {
			Utilities.wrapChipmarkException(e);
		}

		folderStack.push(new Integer(root));
		
		/*
		 * This will initialize the already bookmarked feature
		 */
		ArrayList<Bookmark> existingChipmarks = null;
		try {
			existingChipmarks = db.getAllUserBookmarks(importUser);
		} catch (Exception e) {
			Utilities.ignoreChipmarkException(e);
			existingChipmarks = new ArrayList<Bookmark>();
		} 
		
		for(Bookmark b: existingChipmarks) {
			this.urlList.add(b.getLinkURL());
		}
		
		try {
			this.folderList = db.getUserFolders(importUser);
		} catch (Exception e) {
			Utilities.ignoreChipmarkException(e);
			this.folderList = new ArrayList<Folder>();
		} 
		

		/* Due to a bug in FireFox, the <HR> tags to to be removed.
		 * In FireFox the <HR> tags are generated when a "seperator"
		 * is used between items.  If this seperator falls between
		 * bookmarks, it fools the parser into thinking that a </DL>
		 * tag has been encountered and eventually causes the folder
		 * stack to bottom out.  To the best of my knowledge, and from
		 * what I've seen using the testbench for this, it is impossible
		 * to get this type of a parser to detect and recover from this
		 * error.  The workaround for this problem is to remove all of 
		 * the <HR> tags before we go to the parser.
		 */

		try {
			BufferedReader cleanUP = new BufferedReader(new InputStreamReader(file));
			CharArrayWriter myWriter = new CharArrayWriter();
			String line = null;
			while ((line = cleanUP.readLine()) != null){
				line = line.replaceAll("<HR>", "");
				myWriter.write(line,0,line.length());
			}

			defaultPermission = new String(permission);

			ParserDelegator parser = new ParserDelegator();
			//HTMLEditorKit.ParserCallback callback = new ParseHTML(maxImportNum);
			parser.parse(new CharArrayReader(myWriter.toCharArray()), this, false);

			cleanUP.close();
			myWriter.close();
		} catch (IOException e) {
			Utilities.ignoreChipmarkException(e);
			return "Error! Parsing import file failed.";
		}

		if(this.totalImportNum == this.maxImportNum) {
			return "Error! No chipmarks were found in imported file.";
		}
		
		return null;
	}


	/** Find the folder ID of the folder with the supplied parent and name
	 * @param folderName Name of the folder being searched for
	 * @param parent ParentId of the folder being searched for
	 * @return Return id of folder, or -1 if folder does not exist
	 */

	/* Code is not in use at this time.  If it is ever desired to
	 * import links into existing folders, this function would be useful
	 *
    private int findFolder(String folderName, int parent){
	for (ListIterator i=folderList.listIterator(); i.hasNext(); ) {
	    Folder current = (Folder) i.next();
	    if ((parent == current.getFolderParentID()) && (folderName.equals(current.getFolderName()))){
		return current.getFolderID();
	    }
	}
	return -1;
    }
	 */

	/** Add Folder folder to database and current folder list
	 * @param title Title of new folder
	 * @param parent parentID of new folder
	 * @return Return Folder object just created in the database
	 */
	private Folder addFolder(String title, int parent){
		Folder insert = null;
		try {
			if(title == null)
				title = "unnamed";
			insert = db.addFolder(user, parent, title);
		} catch (Exception e) {
			Utilities.ignoreChipmarkException(e);
			return insert;
		}
		this.folderList.add(insert);
		return insert;
	}
	
	/**
	 * Returns the folder if the folderList contains an identical folder.
	 * @param parent the parent id
	 * @param title the folder name
	 * @return the folder, if the folder exists. Else, null
	 */
	private Folder containsFolder(int parent, String title) {
		
		for(Folder f: this.folderList) {
			if(f.getFolderName().equals(title) && f.getFolderParentID() == parent) {
				return f;
			}
		}			
		
		return null;
	}

	/** Add new link to database
	 * @param title Title of new link
	 * @param url URL of new link
	 * @param privacy Privacy of new link
	 * @param labels ArrayList of labels for link
	 * @param parent ParentID of new link
	 * @return Return Bookmark object that was just added to the database
	 */
	private Bookmark addLink(String title, String url, String privacy, ArrayList<String> labels, int parent){
		Bookmark insert = null;
		this.urlList.add(url);
		try {
			insert = db.addBookmark(user, title, url, privacy, "", parent, labels, 0);
		} catch (Exception e) {
			Utilities.ignoreChipmarkException(e);
			return insert;
		}

		return insert;
	}

	/** Adds Description field to bookmark in the database
	 * @param chipmark Bookmark object to be modified
	 * @param description Description field to be added to database
	 * @return Return Bookmark object that was just modified in the database
	 */
	private Bookmark addDescription(Bookmark chipmark, String description){
		description = Utilities.eliminateNull(description);
		Bookmark insert = null;

		try {
			insert = db.editBookmark(user,
					chipmark.getLinkID(),
					chipmark.getLinkName(),
					chipmark.getLinkURL(),
					chipmark.getLinkPermission(),
					description,
					chipmark.getLabelsArray(),
					chipmark.getToolbarPosition());
		} catch (Exception e) {
			Utilities.ignoreChipmarkException(e);
			return insert;
		}
		return insert;
	} 
}
