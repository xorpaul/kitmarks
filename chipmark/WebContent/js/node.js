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

/**
 * If a link, label or folder is clicked, this function is invoked.
 * 
 * @param elementID
 *            the HTML element of what was clicked by the user
 * @param type
 *            the type of node being clicked
 * @author Luke Preiner
 */
function itemClick(elementID, type) {

	// Hide the sort menu
	ELEMENTS['SORT_MENU'].style.visibility = 'hidden';

	// Cancel any dragging related business.
	mouseClick();
	if (type == 'link') {
		setSelection(elementID, type);
	} else { // if (type == 'folder' || type == 'inbox' || type ==
				// 'chipmark_root' || type == 'trash' || type == 'local' || type
				// == 'label' || type == 'label_root'){
		setSelection(elementID, 'folder');
	}

};

function itemDoubleClick(nodeID, type) {
	// Perform a double click
	alert("this function has use?");

}

/**
 * Selects the current item
 * 
 * @author Luke Preiner
 */
function setSelection(nodeID, type) {

	if (type == 'folder') {
		if (currentlySelectedFolderID != null) {
			var node = treeNodes[currentlySelectedFolderID];
			var nodeTitle = document.getElementById("nodelabel"
					+ currentlySelectedFolderID);

			if (nodeTitle) {
				nodeTitle.className = "tree_nodes";

				// Compensate for IE6 bug
				nodeTitle.style.color = "";
			}
		}

		var node = treeNodes[nodeID];
		var nodeTitle = document.getElementById("nodelabel" + nodeID);

		if (nodeTitle) {
			nodeTitle.className = "tree_selected";

			// Compensate IE6 bug
			nodeTitle.style.color = "white";
		}

		var parent = getTreeNodeByID(node.getParentID());
		if (parent) {
			if (parent.status == 'closed')
				toggleExpander(parent.getID());
		}

		currentlySelectedFolderID = nodeID;
		currentlySelectedItemID = null;

		updateTopBar(currentlySelectedFolderID);
		showPanel(currentlySelectedFolderID);
	} else if (type == 'link') {
		if (currentlySelectedItemID)
			getListLinkNodeByID(currentlySelectedItemID).hideActionMenu();
		getListLinkNodeByID(nodeID).showActionMenu();
		currentlySelectedItemID = nodeID;
	}
}

/**
 * Selects the current item
 * 
 * @author Luke Preiner
 */
function updateTopBar(treeNodeID) {

	var container = ELEMENTS['MANAGE_BAR'];
	removeElementContents(container);

	var node = getTreeNodeByID(treeNodeID);
	// alert(node.type);
	if (node.type == 'local' || node.type == 'chipmark_root'
			|| node.type == 'inbox' || node.type == 'shared') {
		var attribs = {
			"id" :"manage_bar_table",
			"width" :"100%"
		};
		var itemTable = createElementIn(container, "table", attribs);
		var component = createElementIn(itemTable, "tbody", null);
		var row = createElementIn(component, "tr", null);
		var attribs = {
			"valign" :"middle",
			"width" :"20",
			"align" :"center"
		};
		var iconCell = createElementIn(row, "td", attribs);
		var attribs = {
			"valign" :"middle",
			"align" :"left"
		};
		var nameCell = createElementIn(row, "td", attribs);
		var nameSpan = createElementIn(nameCell, "span", null);
		nameSpan.className = "manage_bar_title";
		var attribs = {
			"src" :node.getIcon(),
			"width" :"15",
			"height" :"15"
		};
		var icon = createElementIn(iconCell, "img", attribs);

		var itemLabel = document.createTextNode(node.name);
		nameSpan.appendChild(itemLabel);

		var tableCell = createElementIn(row, "td", attribs);
		var itemLabel = document.createTextNode(" ");
		tableCell.appendChild(itemLabel);

		if (node.type == 'chipmark_root') {
			var attribs = {
				"valign" :"middle",
				"align" :"left",
				"width" :"20"
			};
			var tableCell = createElementIn(row, "td", attribs);
			var attribs = {
				"href" :"#"
			};
			var item = createElementIn(tableCell, "a", attribs);
			item.className = "manage_bar_actions";
			item.onclick = function() {
				getTreeNodeByID(currentlySelectedFolderID).remove();
				return false;
			};
			var prefLangSession = getprefLangSession();
			if (prefLangSession == "eng")
				item.appendChild(document.createTextNode("[Remove All]"));
			else
				item.appendChild(document.createTextNode("[Alles entfernen]"));
		}

		if (node.type != 'shared') {
			var attribs = {
				"valign" :"middle",
				"align" :"left",
				"width" :"20"
			};
			var tableCell = createElementIn(row, "td", attribs);
			var attribs = {
				"href" :"#"
			};
			var item = createElementIn(tableCell, "a", attribs);
			item.className = "manage_bar_actions";
			item.onclick = function() {
				getTreeNodeByID(currentlySelectedFolderID).rename();
				return false;
			};
			item.appendChild(document.createTextNode("[Edit]"));
		}

		if (node.type != 'shared') {
			var attribs = {
				"valign" :"middle",
				"align" :"left",
				"width" :"20"
			};
			var tableCell = createElementIn(row, "td", attribs);
			var attribs = {
				"href" :"#",
				"id" :"sort_menu_link"
			};
			var item = createElementIn(tableCell, "a", attribs);
			item.className = "manage_bar_actions";
			item.onclick = function() {
				getPanelNodeByID(currentlySelectedFolderID).sortMenu();
				return false;
			};
			item.appendChild(document.createTextNode("[Sort]"));
		}

		if (node.type == 'local') {
			var attribs = {
				"valign" :"middle",
				"align" :"left",
				"width" :"20"
			};
			var tableCell = createElementIn(row, "td", attribs);
			var attribs = {
				"href" :"#"
			};
			var item = createElementIn(tableCell, "a", attribs);
			item.className = "manage_bar_actions";
			item.onclick = function() {
				getTreeNodeByID(currentlySelectedFolderID).remove();
				return false;
			};
			item.appendChild(document.createTextNode("[Delete]"));
		}

		if (node.type != 'shared' && node.flag != 'shared'
				&& node.type != 'chipmark_root') {
			var attribs = {
				"valign" :"middle",
				"align" :"left",
				"width" :"20"
			};
			var tableCell = createElementIn(row, "td", attribs);
			var attribs = {
				"href" :"#"
			};
			var item = createElementIn(tableCell, "a", attribs);
			item.className = "manage_bar_actions";
			item.onclick = function() {
				getTreeNodeByID(currentlySelectedFolderID).share();
				return false;
			};
			item.appendChild(document.createTextNode("[Share]"));
		}

		if (node.flag == 'shared') {
			var attribs = {
				"valign" :"middle",
				"align" :"left",
				"width" :"20"
			};
			var tableCell = createElementIn(row, "td", attribs);
			var attribs = {
				"href" :"#"
			};
			var item = createElementIn(tableCell, "a", attribs);
			item.className = "manage_bar_actions";
			item.onclick = function() {
				getTreeNodeByID(currentlySelectedFolderID).share();
				return false;
			};
			item.appendChild(document.createTextNode("[Sharing]"));
		}

		if (node.type != 'inbox' && node.type != 'shared') {
			var attribs = {
				"valign" :"middle",
				"align" :"center",
				"width" :"20"
			};
			var tableCell = createElementIn(row, "td", attribs);
			var attribs = {
				"href" :"#"
			};
			var item = createElementIn(tableCell, "a", attribs);
			item.className = "manage_bar_actions";
			item.onclick = function() {
				getTreeNodeByID(currentlySelectedFolderID).addfolder();
				return false;
			};
			item.appendChild(document.createTextNode("[New Folder]"));
		}

		if (node.type != 'inbox' && node.type != 'shared') {
			var attribs = {
				"valign" :"middle",
				"align" :"center",
				"width" :"20"
			};
			var tableCell = createElementIn(row, "td", attribs);
			var attribs = {
				"href" :"#"
			};
			var item = createElementIn(tableCell, "a", attribs);
			item.className = "manage_bar_actions";
			item.onclick = function() {
				getPanelNodeByID(currentlySelectedFolderID).addlink();
				return false;
			};
			item.appendChild(document.createTextNode("[New Chipmark]"));
		}

		if (node.type == 'shared') {
			var attribs = {
				"valign" :"middle",
				"align" :"left",
				"width" :"20"
			};
			var tableCell = createElementIn(row, "td", attribs);
			var attribs = {
				"href" :"#"
			};
			var item = createElementIn(tableCell, "a", attribs);
			item.className = "manage_bar_actions";
			item.onclick = function() {
				getTreeNodeByID(currentlySelectedFolderID).remove();
				return false;
			};
			item.appendChild(document.createTextNode("[Unsubscribe]"));
		}

	} else if (node.type == 'trash') {
		var attribs = {
			"id" :"manage_bar_table",
			"width" :"100%"
		};
		var itemTable = createElementIn(container, "table", attribs);
		var component = createElementIn(itemTable, "tbody", null);
		var row = createElementIn(component, "tr", null);
		var attribs = {
			"valign" :"middle",
			"width" :"20",
			"align" :"center"
		};
		var iconCell = createElementIn(row, "td", attribs);
		var attribs = {
			"valign" :"middle",
			"align" :"left"
		};
		var nameCell = createElementIn(row, "td", attribs);
		var nameSpan = createElementIn(nameCell, "span", null);
		nameSpan.className = "manage_bar_title";

		var attribs = {
			"src" :node.getIcon(),
			"width" :"15",
			"height" :"15"
		};
		var icon = createElementIn(iconCell, "img", attribs);

		var itemLabel = document.createTextNode(node.name);
		nameSpan.appendChild(itemLabel);

		var tableCell = createElementIn(row, "td", attribs);
		var itemLabel = document.createTextNode(" ");
		tableCell.appendChild(itemLabel);

		var attribs = {
			"valign" :"middle",
			"align" :"left",
			"width" :"20"
		};
		var tableCell = createElementIn(row, "td", attribs);
		var attribs = {
			"href" :"#"
		};
		var item = createElementIn(tableCell, "a", attribs);
		item.onclick = function() {
			emptyTrash();
			return false;
		};
		item.className = "manage_bar_actions";

		var itemLabel = document.createTextNode("[Empty]");
		item.appendChild(itemLabel);
	} else if (node.type == 'label') {

	}

}
