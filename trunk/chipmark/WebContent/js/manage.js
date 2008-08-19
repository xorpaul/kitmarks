/*

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

 */

// Array containing references to important elements
var ELEMENTS = new Object();
ELEMENTS['CONTAINER'] = document.getElementById('manage_container');

// Define images
var IMG_EXPANDER_CLOSED = 'images/plus.png';
var IMG_EXPANDER_OPEN = 'images/minus.png';
var IMG_EXPANDER_CLOSED_OVER = 'images/plus_over.png';
var IMG_EXPANDER_OPEN_OVER = 'images/minus_over.png';
var IMG_EXPANDER_NONE = 'images/spacer.gif';
var IMG_FOLDER_OPEN = 'images/folder_open.png';
var IMG_FOLDER_CLOSED = 'images/folder_closed.png';
var IMG_FOLDER_INBOX = 'images/folder_inbox.png';
var IMG_FOLDER_SHARED = 'images/folder_shared.png';
var IMG_FOLDER_SUBSCRIBED = 'images/folder_subscribed.png';
var IMG_FOLDER_TRASH = 'images/folder_trash.png';
var IMG_LINK_PUBLIC = 'images/link_public.png';
var IMG_LINK_PRIVATE = 'images/link_private.png';
var IMG_LINK_SUBSCRIBED = 'images/link_subscribed.png';
var IMG_LINK_OPEN = 'images/link_open.png';
var IMG_BUDDY_ROOT = 'images/chipmunk.gif';
var IMG_BUDDY = 'images/chipmunk.gif';
var IMG_LABEL_ROOT = 'images/label_root.png';
var IMG_LABEL = 'images/label.png';
var IMG_LINK_ADD = 'images/add_link_15.png';
var IMG_LINK_SEND = 'images/send_link_15.png';

// Define input constraints
var MAX_TITLE_LENGTH = 255;
var MAX_DESC_LENGTH = 65535;
var MAX_URL_LENGTH = 255;
var MAX_FOLDER_NAME_LENGTH = 128;
var MAX_LABEL_NAME_LENGTH = 255;
var MAX_USERNAME_LENGTH = 32;
var MAX_BUDDY_NICKNAME_LENGTH = 64;
var BAD_USERNAME_EXPRESSION = /:|\s/i;
var MAX_PASSWORD_LENGTH = 32; // Check on this value
var MIN_PASSWORD_LENGTH = 6; // Check on this value
var GOOD_EMAIL_EXPRESSION = /^[\S]+[\@][\S]+[\.][\S]+$/;

var DEFAULT_PERMISSION = 'private';

var REFRESH_TIMEOUT = 60000; // how often to automatically refresh the
// chipmark list

// This variable holds the XMLRequest between
// calling and callback. It is destroyed
// after serving it's initial purpose but
// could be reused.
var XMLRequest;

// This variable holds the raw response XML
var XML;

// These story the folders, bookmarks, and labels
// in their XML form
var FOLDERS = new Array();
var BOOKMARKS = new Array();
var LABELS = new Array();

// These will be the root nodes in the tree
var chipmarkRoot = null;
var labelRoot = null;

// Hold special folder IDs
var INBOX_ID = null;
var TRASH_ID = null;
var SUBSCRIPTIONS_ID = null;

// Holds the selected sort type
var sortType = 0;

// Defines the browser for use in determining
// corrections for rendering quirks
var BROWSER = null;
var BLOCK_CONST;

// Holds any current action flag
var action = null;

// Hold current selection information
var currentlySelectedFolderID = null;
var currentlySelectedItemID = null;

// Arrays (plus an associative aspect, thus the
// object type instead of array) mapping node
// ids to the corresponding node object.
var treeNodes = new Object();
var panelNodes = new Object();
var listLinkNodes = new Object();
var listFolderNodes = new Object();
var labelNodes = new Object();

// Variables to hold overall counts
var bookmarkCount = null;
var folderCount = null;
var labelCount = null;

// Lookup functions
function getTreeNodeByID(id) {
	return treeNodes[id];
}
function getListLinkNodeByID(id) {
	return listLinkNodes[id];
}
function getListFolderNodeByID(id) {
	return listFolderNodes[id];
}
function getPanelNodeByID(id) {
	return panelNodes[id];
}
function getLabelNodeByID(id) {
	return labelNodes[id];
}

function setStatus(message) {
	var msgElement = document.getElementById("manage_loading_status");
	var oldMessage = msgElement.firstChild;
	var newMessage = document.createTextNode(message);
	msgElement.replaceChild(newMessage, oldMessage);
}

function setProgress(complete, total) {
	var progressBar = document.getElementById("manage_progress_fill");
	progressBar.style.width = Math.round((complete * 100) / total) + '%';
}

function displayStats() {
	if (bookmarkCount != 1)
		var chipmarks = 'chipmarks';
	else
		var chipmarks = 'chipmark';

	// update the status bar with current counts
	setElementText(ELEMENTS['MANAGE_STATUS_BAR'], "You have " + bookmarkCount
			+ " " + chipmarks + " in " + (folderCount - 1) + " folder(s)");
}

function setupManagement(container_div) {
	// Set container, if specified
	if (container_div != null)
		ELEMENTS['CONTAINER'] = document.getElementById(container_div);

	// Build and display loading screen
	showLoadingScreen();

	// Detect browser and set vars accordingly
	browserSetup();

	// Retrieve buddy list
	retrieveBuddies();

	// Build the rest of the interface
	buildInterface();

	// Retrieve XML
	getChipmarkXML();
}

function browserSetup() {

	setStatus("Detecting browser...");

	var userAgent = navigator.userAgent.toLowerCase();
	if (userAgent.indexOf("msie") + 1) {
		BROWSER = "IE";
		BLOCK_CONST = 'inline-block';
	} else {
		BROWSER = "NON-IE";
		BLOCK_CONST = 'block';
	}
}

function retrieveBuddies() {
	setStatus("Retrieving buddies...");
	if (!getBuddyList()) {
		alert("Failed to retrieve your buddy list");
	}
}

function showLoadingScreen() {
	// create parent div
	var attribs = {
		"id" :"manage_loading"
	};
	ELEMENTS['LOADING_CONTAINER'] = createElementIn(ELEMENTS['CONTAINER'],
			"div", attribs);
	// create the table
	var attribs = {
		"width" :"50%",
		"align" :"center"
	};
	var newelement = createElementIn(ELEMENTS['LOADING_CONTAINER'], "table",
			attribs);
	// create tbody
	var newelement = createElementIn(newelement, "tbody", null);
	// create tr
	var newelement = createElementIn(newelement, "tr", null);
	// create the td
	var attribs = {
		"id" :"manage_loading_status",
		"align" :"left",
		"valign" :"middle"
	};
	var newelement = createElementIn(newelement, "td", attribs);
	setElementText(newelement, "Loading...");
	var attribs = {
		"id" :"manage_progress"
	};
	var newelement = createElementIn(newelement, "div", attribs);
	var attribs = {
		"id" :"manage_progress_fill"
	};
	var newelement = createElementIn(newelement, "div", attribs);
}

/**
 * This function creates all of the interface elements with the exception of the
 * node trees (those are created by buildObjects)
 * 
 * @author Luke Preiner
 */
function buildInterface() {
	setStatus("Building interface...");

	// create parent div
	var attribs = {
		"id" :"manage_main",
		"style" :"display: none;"
	};
	ELEMENTS['MANAGE_FOLDER_CONTAINER'] = createElementIn(
			ELEMENTS['CONTAINER'], "div", attribs);

	// create div to contain top manage bar
	var attribs = {
		"id" :"manage_bar"
	};
	ELEMENTS['MANAGE_BAR'] = createElementIn(
			ELEMENTS['MANAGE_FOLDER_CONTAINER'], "div", attribs);

	// create the table
	var attribs = {
		"id" :"manage_panes",
		"width" :"100%",
		"cellpadding" :"0",
		"cellspacing" :"0"
	};
	var newelement = createElementIn(ELEMENTS['MANAGE_FOLDER_CONTAINER'],
			"table", attribs);
	// create tbody
	newelement = createElementIn(newelement, "tbody", null);
	// create tr
	newelement = createElementIn(newelement, "tr", null);
	// create the td
	var attribs = {
		"id" :"manage_tree"
	};
	var treeContainer = createElementIn(newelement, "td", attribs);

	var attribs = {
		"id" :"manage_tree_div"
	};
	ELEMENTS['MANAGE_FOLDER_TREE'] = createElementIn(treeContainer, "div",
			attribs);

	// create the td
	var attribs = {
		"id" :"manage_list"
	};
	var listContainer = createElementIn(newelement, "td", attribs);

	var attribs = {
		"id" :"manage_list_div"
	};
	ELEMENTS['MANAGE_FOLDER_LIST'] = createElementIn(listContainer, "div",
			attribs);

	setElementText(ELEMENTS['MANAGE_FOLDER_LIST'], "Item List");

	// create div to contain bottom status
	var attribs = {
		"id" :"manage_status_bar"
	};
	ELEMENTS['MANAGE_STATUS_BAR'] = createElementIn(
			ELEMENTS['MANAGE_FOLDER_CONTAINER'], "div", attribs);

	// create div to contain sort menu
	var attribs = {
		"id" :"manage_sort_menu"
	};
	ELEMENTS['SORT_MENU'] = createElementIn(
			ELEMENTS['MANAGE_FOLDER_CONTAINER'], "div", attribs);

	var tableCell = createElementIn(ELEMENTS['SORT_MENU'], "div", null);
	var attribs = {
		"href" :"#"
	};
	var item = createElementIn(tableCell, "a", attribs);
	item.className = "manage_bar_actions";
	item.onclick = function() {
		getPanelNodeByID(currentlySelectedFolderID).doSort('name');
		return false;
	};
	item.appendChild(document.createTextNode("[By Name]"));

	var tableCell = createElementIn(ELEMENTS['SORT_MENU'], "div", null);
	var attribs = {
		"href" :"#"
	};
	var item = createElementIn(tableCell, "a", attribs);
	item.className = "manage_bar_actions";
	item.onclick = function() {
		getPanelNodeByID(currentlySelectedFolderID).doSort('date');
		return false;
	};
	item.appendChild(document.createTextNode("[By Add Date]"));

	var tableCell = createElementIn(ELEMENTS['SORT_MENU'], "div", null);
	var attribs = {
		"href" :"#"
	};
	var item = createElementIn(tableCell, "a", attribs);
	item.className = "manage_bar_actions";
	item.onclick = function() {
		getPanelNodeByID(currentlySelectedFolderID).doSort('permission');
		return false;
	};
	item.appendChild(document.createTextNode("[By Permission]"));

	disableTextSelection(ELEMENTS['MANAGE_FOLDER_TREE']);
	disableTextSelection(ELEMENTS['MANAGE_FOLDER_LIST'])

	// compensate (maybe) for IE lameness
	if (BROWSER == "IE") {
		treeContainer.style.width = "33%";
		ELEMENTS['MANAGE_FOLDER_TREE'].style.maxWidth = "33%";
	}

}

// This function retrieves the XML from the server.
// The XML contains the Folders, Labels, and
function getChipmarkXML() {
	setStatus("Contacting chipmark server...");
	XMLRequest = getXMLHTTPObject();
	execXMLQuery(XMLRequest, "GET", "GetFoldersAndLinks", null, true, parseXML);
}

// This function parses the XML into the primary elements
function parseXML() {
	// alert("XMLHTTP readystate: " + XMLRequest.readyState);
	if (XMLRequest.readyState == 4) {
		if (XMLRequest.status == 200) {
			if (XMLRequest.responseXML != null) {
				setStatus("Loading your chipmarks...");
				XML = XMLRequest.responseXML;

				// we dont need the async request object any more
				XMLRequest = null;

				FOLDERS = XML.getElementsByTagName("Folder");
				BOOKMARKS = XML.getElementsByTagName("Bookmark");
				var labels = XML.getElementsByTagName("AllLabelList")[0]
						.getElementsByTagName("Label");

				folderCount = FOLDERS.length;
				bookmarkCount = BOOKMARKS.length;
				labelCount = labels.length;

				// Add the labels
				for ( var i = 0; i < labelCount; i++) {
					addLabel(labels[i].getAttribute('name'));
				}
				// Sort them
				LABELS.sort();

				setStatus("Successfully loaded...");
				buildObjects();

			} else {
				XMLRequest = null;
				setStatus("Server returned an empty response.  Try reloading the page.");
			}
		} else {
			XMLRequest = null;
			setStatus("Request to server failed.  Try reloading the page.");
		}
	}
}

// This function constructs the node objects for
// folders, bookmarks, and (possibly) labels
function buildObjects() {

	// build the folder tree
	buildFolderTree();

	// add the labels tree
	buildLabelTree();

	// build the folder/label contents
	buildBookmarks();

	// place the tree nodes in their spot
	drawTree();

	displayStats();

	showMainInterface();
}

function drawFolderTree() {
	chipmarkRoot.HTML(ELEMENTS['MANAGE_FOLDER_TREE']);
}

function drawLabelTree() {
	labelRoot.HTML(ELEMENTS['MANAGE_FOLDER_TREE']);
}

function drawTree() {
	removeElementContents(ELEMENTS['MANAGE_FOLDER_TREE']);
	chipmarkRoot.doSort();
	labelRoot.doSort();
	drawFolderTree();
	if (LABELS.length > 0)
		drawLabelTree();
}

// Shows the main interface
function showMainInterface() {
	toggleDivDisplay(ELEMENTS['LOADING_CONTAINER'], false);
	toggleDivDisplay(ELEMENTS['MANAGE_FOLDER_CONTAINER'], true);
	setSelection(chipmarkRoot.getID(), 'folder');
	initDragAndDrop();
}

// Builds the folder treenodes
function buildFolderTree() {

	// add the trash can element
	setStatus("Creating trash can...");
	TRASH_ID = 'trash'; // treeNodes.length + 1;
	trashCan = new treeNode('trash', new Array(), 'closed', TRASH_ID,
			chipmarkRoot, "Trash", false);
	treeNodes[TRASH_ID] = trashCan;

	// Set up the list panel
	var trashPanel = new panelNode('trash', trashCan.getID(), new Array());
	panelNodes[TRASH_ID] = trashPanel;

	// add the subscriptions element
	setStatus("Creating subscriptions folder...");
	SUBSCRIPTIONS_ID = 'subscriptions';
	subsFolder = new treeNode('subscriptions', new Array(), 'closed',
			SUBSCRIPTIONS_ID, chipmarkRoot, "Subscriptions", false);
	subsFolder.flag = 'subscribed';
	treeNodes[SUBSCRIPTIONS_ID] = subsFolder;

	// Set up the list panel
	var subsPanel = new panelNode('folder_list', subsFolder.getID(),
			new Array());
	panelNodes[SUBSCRIPTIONS_ID] = subsPanel;

	setStatus("Building folder hierarchy...");
	for ( var i = 0; i < FOLDERS.length; i++) {

		// Update the progress bar
		setProgress(i + 1, FOLDERS.length);

		// Give the folder a default name in case something goes wrong
		var folderName = "<Unnamed Folder>";
		var subscribedUsers = new Array();

		if (FOLDERS[i].childNodes[0].getElementsByTagName('FolderName')) {
			folderName = FOLDERS[i].getElementsByTagName('FolderName')[0].firstChild.nodeValue;
		}

		var folderID = parseInt(FOLDERS[i].getAttribute('id'));
		var folderParent = parseInt(FOLDERS[i].getAttribute('parent'));
		var folderType = FOLDERS[i].getAttribute('type');
		var folderToolbarPosition = parseInt(FOLDERS[i].getElementsByTagName(
				"ToolbarPosition").item(0).textContent);
		var flag = 'normal';

		// Change of terminology:
		// the value "shared" is a folder the user is subscribed to
		// but we want to use shared to describe a folder that the user
		// has personally shared themselves. We'll use 'subscribed'
		// to indicate that they are viewing a folder shared by another
		// user.
		if (folderType == 'shared') {
			flag = 'subscribed';
			folderParent = SUBSCRIPTIONS_ID;
		}
		if (FOLDERS[i].childNodes[0].getElementsByTagName('SubscribedUsers')) {
			var subscribedUsersXML = FOLDERS[i]
					.getElementsByTagName('SubscribedUsers')[0]
					.getElementsByTagName('username');
			if (subscribedUsersXML.length > 0) {
				for ( var v = 0; v < subscribedUsersXML.length; v++) {
					subscribedUsers
							.push(subscribedUsersXML[v].firstChild.nodeValue);
				}
				// see terminology change comment above
				flag = 'shared';
			}
		}

		var node = null;
		var panel = null;

		if (folderType == 'root') {
			if (treeNodes[folderID] == undefined) {
				if (!folderName.length)
					folderName = "Chipmarks";
				node = new treeNode('chipmark_root', new Array(), 'open',
						folderID, 0, folderName, flag, subscribedUsers);
				panel = new panelNode('folder_list', folderID, new Array());
				treeNodes[folderID] = node;
				panelNodes[folderID] = panel;
			} else {
				node = treeNodes[folderID];
				node.type = 'chipmark_root';
				node.name = folderName;
				node.status = 'open';
				node.users = subscribedUsers;
				node.flag = flag;
			}
			chipmarkRoot = node;
		} else {
			// It's not the root folder
			if (folderType == 'inbox') {
				INBOX_ID = folderID;
			}
			if (treeNodes[folderID] == undefined) {
				node = new treeNode(folderType, new Array(), 'closed',
						folderID, folderParent, folderName, flag,
						subscribedUsers, folderToolbarPosition);
				panel = new panelNode('folder_list', folderID, new Array());
				treeNodes[folderID] = node;
				panelNodes[folderID] = panel;
			} else {
				node = treeNodes[folderID];
				node.type = folderType;
				node.setParentID(folderParent);
				node.users = subscribedUsers;
				node.toolbarPosition = folderToolbarPosition;
				node.flag = flag;
			}

			node.name = folderName;

			// Now that we have the node created, we need
			// to add it's tree node to it's parent, if it exists.
			// If it doesn't exist, we'll create it, and then
			// add to it. the details of the container will be
			// filled in when that folder is loaded.
			if (treeNodes[folderParent] == undefined) {
				var parentNode = new treeNode(folderType, new Array(),
						'closed', folderParent, null, '', 'normal',
						new Array(), folderToolbarPosition);
				panel = new panelNode('folder_list', folderParent, new Array());

				treeNodes[folderParent] = parentNode;
				panelNodes[folderParent] = panel;
			} else {
				var parentNode = treeNodes[folderParent];
				panel = panelNodes[folderParent];
			}

			// Append the tree node to it's parent
			parentNode.addChild(node);

			// Append a list node for the folder to it's parent panel
			if (flag == 'shared')
				var perm = 'shared';
			else if (flag == 'subscribed')
				var perm = 'subscribed';
			else if (folderType == 'inbox')
				var perm = 'inbox';
			else
				var perm = 'normal';

			var lNode = new listNode('folder', node.getID(), node.name, null,
					perm, null, null, node.getParentID(), node.toolbarPosition);
			listFolderNodes[node.getID()] = lNode;
			panel.addChild(lNode);

		}
	}

	// Add special folders
	if (subsFolder.children.length > 0)
		chipmarkRoot.addChild(subsFolder);
	chipmarkRoot.addChild(trashCan);

	// Set up the action panels
	setStatus("Creating action panels...");
	panelNodes['linkEditPanel'] = new panelNode('link_edit', null, new Array());
	panelNodes['linkSendPanel'] = new panelNode('link_send', null, new Array());
	panelNodes['folderMovePanel'] = new panelNode('folder_move', null,
			new Array());
	panelNodes['folderSharePanel'] = new panelNode('folder_share', null,
			new Array());
	panelNodes['folderEditPanel'] = new panelNode('folder_edit', null,
			new Array());
}

// Builds the label list treenodes.
// There is no hierarchy yet.
function buildLabelTree() {

	setStatus("Creating label tree...");
	var node = new treeNode('label_root', new Array(), 'closed', 'label_root',
			0, 'Labels', 'normal', null);
	var panel = new panelNode('label_root', 'label_root', new Array());
	treeNodes['label_root'] = node;
	panelNodes['label_root'] = panel;
	labelRoot = node;

	// Create a category for chipmarks with no label
	/*
	 * var id = 'unlabeled'; var name = "< Unlabeled >"; var node = new
	 * treeNode('label', new Array(), 'closed', id, 'label_root', name,
	 * 'normal', null); var panel = new panelNode('label_list', id, new
	 * Array()); var parentNode = getTreeNodeByID('label_root');
	 * parentNode.addChild(node); getPanelNodeByID('label_root').addChild(node);
	 * treeNodes[id] = node; panelNodes[id] = panel;
	 */

	for ( var j = 0; j < LABELS.length; j++) {

		// Update the progress bar
		setProgress(j + 1, LABELS.length);

		// Make sure the array element contains a label
		if (LABELS[j] != null && LABELS[j].length > 0) {

			// Create the nodes
			var id = 'label-' + LABELS[j];
			id = id.toLowerCase();
			var name = LABELS[j];
			var node = new treeNode('label', new Array(), 'closed', id,
					'label_root', name, 'normal', null);
			var panel = new panelNode('label_list', id, new Array());

			// Append the tree node to it's parent tree and pnael
			var parentNode = getTreeNodeByID('label_root');
			var parentPanel = getPanelNodeByID('label_root');
			parentNode.addChild(node);
			parentPanel.addChild(node);
			treeNodes[id] = node;
			panelNodes[id] = panel;

		}
	}
}

// Builds the bookmark nodes
function buildBookmarks() {

	setStatus("Populating folders...");

	for ( var j = 0; j < BOOKMARKS.length; j++) {

		// Update the progress bar
		setProgress(j + 1, BOOKMARKS.length);

		var bookmarkPermission = BOOKMARKS[j].getAttribute('permission');
		var bookmarkID = parseInt(BOOKMARKS[j].getAttribute('id'));
		var bookmarkParent = '';
		var bookmarkName = '';
		var bookmarkURL = '';
		var bookmarkDesc = '';
		var bookmarkTags = new Array();

		for ( var e = 0; e < BOOKMARKS[j].childNodes[3].childNodes.length; e++) {
			bookmarkTags[e] = BOOKMARKS[j].childNodes[3].childNodes[e]
					.getAttribute('name');
		}

		if (BOOKMARKS[j].childNodes[4].firstChild) {
			bookmarkParent = parseInt(BOOKMARKS[j].childNodes[4].firstChild.nodeValue);
		}
		if (BOOKMARKS[j].childNodes[0].firstChild) {
			bookmarkName = BOOKMARKS[j].childNodes[0].firstChild.nodeValue;
		}
		if (BOOKMARKS[j].childNodes[1].firstChild) {
			bookmarkURL = BOOKMARKS[j].childNodes[1].firstChild.nodeValue;
		}
		if (BOOKMARKS[j].childNodes[2].firstChild) {
			bookmarkDesc = BOOKMARKS[j].childNodes[2].firstChild.nodeValue;
		}

		if (getTreeNodeByID(bookmarkParent).flag == 'subscribed')
			bookmarkPermission = 'subscribed';

		var lNode = new listNode('link', bookmarkID, bookmarkName, bookmarkURL,
				bookmarkPermission, bookmarkDesc, new Array(), bookmarkParent);
		lNode.addTags(bookmarkTags);

		listLinkNodes[bookmarkID] = lNode;

		var pPanel = getPanelNodeByID(bookmarkParent);
		if (pPanel)
			pPanel.addChild(lNode);
		else
			setStatus("There were errors loading your chipmarks. Chipmark id#"
					+ lNode.getID()
					+ " could not be added to it's parent container");

		if (lNode.tags.length > 0 && bookmarkPermission != 'subscribed') {
			for ( var i = 0; i < lNode.tags.length; i++) {
				var lPanel = getPanelNodeByID('label-' + lNode.tags[i]
						.toLowerCase());
				if (lPanel)
					lPanel.addChild(lNode);
			}
		} else {
			// Add it to the "unlabeled" group, if it exists
			var lPanel = getPanelNodeByID('unlabeled');
			if (lPanel)
				lPanel.addChild(lNode);
		}

	}

}

function addLabel(labelName) {
	var add = true;
	labelName = trim(labelName);
	if (labelName.length > 0) {
		for ( var i = 0; i < LABELS.length; i++) {
			if (LABELS[i].toUpperCase() == labelName.toUpperCase())
				add = false;
		}
		// Filter out reserved labels
		if (labelName.toUpperCase() == 'CHIPMARKTOOLBAR')
			add = false;
		if (labelName.toUpperCase() == 'NEW')
			add = false;

		if (add == true) {
			LABELS.push(labelName);
			LABELS.sort();
		}
	}
	return add;
}

function removeLabel(labelName) {
	for ( var i = 0; i < LABELS.length; i++) {
		if (LABELS[i].toLowerCase() == labelName.toLowerCase()) {
			LABELS.splice(i, 1);
			var labelPanel = getPanelNodeByID('label_root');
			labelPanel.removeChild('label-' + labelName);
			labelRoot.removeChild('label-' + labelName);
			panelNodes['label-' + labelName.toLowerCase()] = null;
			labelPanel = null;
			return;
		}
	}
}

function labelExists(labelName) {
	for ( var i = 0; i < LABELS.length; i++) {
		if (LABELS[i].toLowerCase() == labelName.toLowerCase())
			return true;
	}
	return false;
}

/**
 * Updates the sort order of a group of bookmarks
 * 
 * @param sortString
 *            The hex string used to sort
 * @author Luke Preiner
 */
function updateSortOrder(sortString) {

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'UpdateBookmarkOrder',
			'sortString=' + sortString);

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;
		return true;
	}

	request = null;
	return false;
}

function showPanel(panelID, scrollhold) {
	ELEMENTS['SORT_MENU'].style.visibility = 'hidden';
	getPanelNodeByID(panelID).sortFolders();
	var hold = scrollhold || false;
	removeElementContents(ELEMENTS['MANAGE_FOLDER_LIST']);
	getPanelNodeByID(panelID).HTML(ELEMENTS['MANAGE_FOLDER_LIST'], hold);
	displayStats();
}

/**
 * Remove a link
 * 
 * @param nodeID
 *            The DBID (aka ID) of the link to remove
 * @author Luke Preiner
 */
function removeLink(nodeID) {

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=deleteLink&linkID=' + encodeURIComponent(nodeID));

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return true;
	}
	return false;
}

/**
 * Add a link
 * 
 * @param parentID
 *            The ID of the folder to add to
 * @param permission
 *            The chipmark's permission setting (public/private)
 * @param name
 *            The chipmark's title
 * @param url
 *            The chipmark's URL
 * 
 * @author Luke Preiner
 */

function addLink(parentID, permission, name, url, description, labels) {

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=addLink&linkPermission=' + encodeURIComponent(permission)
					+ '&parentFolderID=' + encodeURIComponent(parentID)
					+ '&linkName=' + encodeURIComponent(name) + '&linkURL='
					+ encodeURIComponent(url) + '&linkDescription='
					+ encodeURIComponent(description) + '&labelNames='
					+ encodeURIComponent(labels));

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return xml.getElementsByTagName("Result")[0].firstChild.nodeValue;
	}
	return false;
}

/**
 * Update a link
 * 
 * @param The
 *            ID of the link to change
 * @param permission
 *            The chipmark's permission setting (public/private)
 * @param name
 *            The chipmark's title
 * @param url
 *            The chipmark's URL
 * @param labels
 *            The link's labels (comma delimited string)
 * 
 * @author Luke Preiner
 */

function updateLink(id, permission, name, url, description, labels) {

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=editLink&linkID=' + encodeURIComponent(id)
					+ '&linkPermission=' + encodeURIComponent(permission)
					+ '&linkName=' + encodeURIComponent(name) + '&linkURL='
					+ encodeURIComponent(url) + '&linkDescription='
					+ encodeURIComponent(description) + '&labelNames='
					+ encodeURIComponent(labels));

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return true;
	}
	return false;
}

/**
 * Add a folder
 * 
 * @param folderID
 *            The DBID (aka ID) of the folder to remove
 * @author Luke Preiner
 */
function addFolder(parentID, folderName, folderToolbarPosition) {
	if (typeof folderToolbarPosition == "undefined")
		folderToolbarPosition = 0;

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=addFolder&parentFolderID=' + encodeURIComponent(parentID)
					+ '&folderName=' + encodeURIComponent(folderName)
					+ '&toolbarPosition='
					+ encodeURIComponent(folderToolbarPosition));

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return xml.getElementsByTagName("Result")[0].firstChild.nodeValue;
	}
	return false;
}

/**
 * Remove a folder
 * 
 * @param folderID
 *            The DBID (aka ID) of the folder to remove
 * @author Luke Preiner
 */
function removeFolder(folderID) {

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=deleteFolder&folderID=' + encodeURIComponent(folderID));

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return true;
	}
	return false;
}

/**
 * Rename a folder
 * 
 * @param folderID
 *            The DBID (aka ID) of the folder to rename
 * @param folderName
 *            The new folder name
 * @author Luke Preiner
 */
function renameFolder(folderID, folderName, folderToolbarPosition) {
	if (typeof folderToolbarPosition == "undefined")
		folderToolbarPosition = 0;

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=editFolder&folderID=' + encodeURIComponent(folderID)
					+ '&folderName=' + encodeURIComponent(folderName)
					+ '&toolbarPosition='
					+ encodeURIComponent(folderToolbarPosition));

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return true;
	}
	return false;
}

/**
 * Send a link to buddies
 * 
 * @param buddies
 *            The DBID of the folder to share
 * @param users
 *            A colon delimited string of the usernames to revoke
 * 
 * @author Luke Preiner
 */

function sendLink(buddies, linkname, linkurl, linkdesc) {
	buddies = encodeURIComponent(buddies);
	linkname = encodeURIComponent(linkname);
	linkurl = encodeURIComponent(linkurl);
	linkdesc = encodeURIComponent(linkdesc);

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'Inbox', 'action=send&name='
			+ buddies + '&linkname=' + linkname + '&linkurl=' + linkurl
			+ '&linkdesc=' + linkdesc);

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("status")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("message")[0].firstChild) {
				alert(xml.getElementsByTagName("message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return true;
	}
	return false;
}

/**
 * Move a link
 * 
 * @param nodeID
 *            The DBID (aka ID) of the link to move
 * @param destFolderID
 *            The DBID of the destination folder
 * @author Luke Preiner
 */
function moveLink(nodeID, destFolderID) {

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=moveLink&linkID=' + encodeURIComponent(nodeID)
					+ '&destFolderID=' + encodeURIComponent(destFolderID));

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return true;
	}
	return false;
}

/**
 * Move a folder
 * 
 * @param nodeID
 *            The DBID (aka ID) of the folder to move
 * @param destFolderID
 *            The DBID of the destination folder
 * @author Luke Preiner
 */
function moveFolder(nodeID, destFolderID) {

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=moveFolder&folderID=' + encodeURIComponent(nodeID)
					+ '&destFolderID=' + encodeURIComponent(destFolderID));

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return true;
	}
	return false;
}

/**
 * Copy a link
 * 
 * @param nodeID
 *            The DBID (aka ID) of the link to copy
 * @param destFolderID
 *            The DBID of the destination folder
 * @author Luke Preiner
 */
function copyLink(nodeID, destFolderID) {

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=copyLink&linkID=' + encodeURIComponent(nodeID)
					+ '&destFolderID=' + encodeURIComponent(destFolderID));

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			return false;
		}
		return xml.getElementsByTagName("Result")[0].firstChild.nodeValue;
	}
	return false;
}

/**
 * Remove all folders and links a user has
 * 
 * @author Luke Preiner
 */
function removeAll() {
	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'DeleteLink',
			'delete=all&agent=ext');

	if (result != null) {
		var xml = request.responseXML;
		request = null;
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			} else {
				alert("Chipmark encountered an error while removing all of your chipmarks");
				window.location.reload();
			}
		} else {
			window.location.reload();
		}
	}
};

function emptyTrash() {
	if (confirm("This will permanently remove all items from the trash.")) {
		var trash = getPanelNodeByID(TRASH_ID);
		trash.children = new Array();
		setSelection(TRASH_ID, 'folder');
	}
}
