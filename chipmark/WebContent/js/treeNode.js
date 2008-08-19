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



function treeNode(      type,           // local / chipmark_root / trash / inbox / subscriptions / label
                        children,    	// array of child Node objects, if type is folder
			  			status, 		// open / closed
			  			id, 			// unique id to access the correct div or object in the nodArr
                        parent,         // parent id
						name, 			// title of tree node
                        flag,           // normal / shared / subscribed
                        users,          // array holding subscribed users for shared folders
                        toolbarPosition)// position on the toolbar (optional)
{
    if (typeof toolbarPosition == "undefined")
        toolbarPosition = 0;
    this.type = type;
	this.children = children;
	this.status = status;
	this.id = id;
	this.parentID = parent;
	this.name = name;
    this.flag = flag;
	this.users = users;
	this.toolbarPosition = toolbarPosition;
};



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Accessor functions
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
treeNode.prototype.getID = function(){
	return this.id;
};

treeNode.prototype.setParentID = function(parentId) {
	this.parentID = parentId;
};

treeNode.prototype.getParentID = function() {
	return this.parentID;
};


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




/**
 * Toggles a tree node state between
 * open and closed, and updates images
 * accordingly
 * 
 * @param nodeID The array ID of the node to toggle
 * @author Luke Preiner
 */
function toggleExpander (nodeID) {
    var currNode = getTreeNodeByID(nodeID);
    if (currNode.status  == 'open')
        currNode.status  = 'closed';
    else
        currNode.status  = 'open';
    
    var div = document.getElementById("tree_children" + currNode.id);
    if (div)
        toggleDivDisplayById('tree_children' + currNode.id, null, "block");

    var expanderIcon = document.getElementById("exp" + currNode.id);
    if (expanderIcon)
        expanderIcon.src = currNode.getExpanderImage();
}

//Sort by name
function treeSort(n1, n2) {

    if (n1.type == 'trash' && n2.type == 'subscriptions')
        return 1;

    if (n2.type == 'trash' && n1.type == 'subscriptions')
        return -1;

    if (n1.type == 'trash' || n1.type == 'subscriptions')
        return 1;
    
    if (n2.type == 'trash' || n2.type == 'subscriptions')
        return -1;
        


	//Compare by name
	if(n1.name.toLowerCase() > n2.name.toLowerCase())
		return 1;
	
	if(n1.name.toLowerCase() < n2.name.toLowerCase())
		return -1;
	
	//Stable sort:
	if(n1.getID() > n2.getID())
		return 1;
	
	if(n1.getID() < n2.getID())
		return -1;

	return 0;
}

//	Just sort so that folders are always at the top,
//	and are always in sorted order.
treeNode.prototype.doSort = function() {
	
	//Sort sub folders and assign temp ids for a stable sort.
	for(var i = 0; i < this.children.length; i++)	{
		this.children[i].doSort();
	}

	//Sort children.
	this.children.sort(treeSort);
	
	return;
}


treeNode.prototype.getSortString = function(){	
    return getPanelNodeByID(this.getID()).getSortString();
}
	    
/**
 * Generates tree objects for output
 *
 * @author Luke Preiner
 */
treeNode.prototype.HTML = function(parent) {

    var hasChildren = (this.children.length > 0);    
    var onclick = '';
    var expandPointer = '';    
    var nodeName = this.name;
    
    // Create node div
    var attribs = {"id":"tree_" + this.getID(),"style":"display:" + BLOCK_CONST + ";"};    
    var nodeObj = createElementIn(parent, "div", attribs);
    disableTextSelection(nodeObj);
    var expanderIcon = this.getExpanderImage();
    var nodeIcon = this.getIcon();

    // Use innerHTML if IE, DOM if otherwise.
    // This is due to IE's inability to properly
    // set DHTML attributes (onclick, etc) via
    // DOM methods.
    if (BROWSER != null && BROWSER == 'NON-IE'){
        if (hasChildren){
            onclick = "toggleExpander('"+this.getID()+"');";
            expandPointer = "cursor:pointer;";
        }
        else{
            onclick = ";"
            expandPointer = ";";
        }
        var attribs = {"id":"exp"+this.getID(),"src": expanderIcon, "width":"9","height":"9","onclick":onclick,"style":expandPointer};
        var expander = createElementIn(nodeObj, "img", attribs);        

        var attribs = {"id":"icon"+this.getID(),"src": nodeIcon, "width":"15","height":"15"};    
        var icon = createElementIn(nodeObj, "img", attribs);
            
	    var attribs = {"id":"nodelabel"+this.getID(),"class":"tree_nodes",	                    
	                    "onclick":"itemClick('" + this.getID() + "','" + this.type + "')",
	                    "onmouseover":"setElementText('manage_status_bar','" + escape(nodeName) + "');",
                        "onmousedown":"mouseDown('" + this.getID() + "','tree_node')",
                        "onmouseup":"mouseUp('" + this.getID() + "','tree_node')",
                        "onmouseover":"mouseHover('" + this.getID() + "','in','tree_node')",                                
                        "onmouseout":"mouseHover('" + this.getID() + "','out','tree_node')"};     
	    var nodeLabel = createElementIn(nodeObj,"a",attribs);
        disableTextSelection(nodeLabel);
        var nodeLabelText = document.createTextNode(nodeName);    	
	    nodeLabel.appendChild(nodeLabelText);
    		    
	    nodeObj.appendChild(nodeLabel);

        var attribs = {"id":"tree_children" + this.getID()};    
        var childNode = createElementIn(nodeObj, "div", attribs);
        childNode.className = 'folder_child';
    }
    else{
        if (hasChildren) {
            onclick = 'onclick="toggleExpander(\'' + this.getID() + '\');"';
            //onclick = ' ';
            expandPointer = "cursor:pointer;";
        }
        
        var html = '<img id="exp' + this.getID() + '" src="' + expanderIcon + '" width="9" height="9" style="' + expandPointer + '" ' + onclick + '/>';
        html +=  '<img id="icon' + this.getID() + '" src="' + nodeIcon + '" width="15" height="15"/>';
        html += '<a href="#" ondragstart="return false;" id="nodelabel' + this.getID() + '" class="tree_nodes" onclick="itemClick(\'' + this.getID() + '\',\'folder\'); return false;" ';
        //html += '<a href="#" ondragstart="return false;" id="nodelabel' + this.getID() + '" class="tree_nodes" ';
        html += 'onmousedown="mouseDown(\'' + this.getID() + '\',\'tree_node\');" ';
        html += 'onmouseup="mouseUp(\'' + this.getID() + '\',\'tree_node\');" ';
        html += 'onmouseover="mouseHover(\'' + this.getID() + '\',\'in\',\'tree_node\');" ';                                
        html += 'onmouseout="mouseHover(\'' + this.getID() + '\',\'out\',\'tree_node\');">';   
        html += htmlspecialchars(nodeName) + '</a>';
        html += '<div id="tree_children' + this.getID() + '" class="folder_child"></div>';
        
        nodeObj.innerHTML = html;
        
        var nodeName = document.getElementById("node_name" + this.getID());
        if (nodeName) nodeName.className = "tree_nodes";
       
        var childNode = document.getElementById("tree_children" + this.getID());
        
    }

    // don's show it open if it's not supposed to be
    if (this.status == 'closed')
        childNode.style.display = "none";
        
    // only recurse if needed
    if (hasChildren){    
        for (var i = 0; i < this.children.length; i++){
            this.children[i].HTML(childNode);         
        }
    }
    else{
        // get rid of the unnecessary child node
        nodeObj.removeChild(childNode);
    }
    
	return nodeObj;	
}



// Add a child to the end of the node's list of children.
treeNode.prototype.addChild = function(child) {
	this.children.push(child);
};


// Node::removeChild():
treeNode.prototype.removeChild = function(childID){
	for(var t = 0; t < this.children.length; t++){
		if (this.children[t].getID() == childID){
			this.children.splice(t,1);
			return;
		}
	}
	alert("treeNode.removeChild(): Could not find node: " + childID);
};



/**
 * Is the node a draggable type?
 * 
 * @author Luke Preiner
 */
treeNode.prototype.draggable = function(){
    if(this.type == 'chipmark_root')
	    return false;    
    else if (this.type == 'label_root')
        return false;
    else if (this.type == 'inbox')
	    return false;
    else if (this.type == 'trash')
        return false;	  
    else if (this.flag == 'subscribed')
        return false;    
    else
        return true;
}

/**
 * Is the node a drop target?
 * 
 * @author Luke Preiner
 */
treeNode.prototype.droppable = function(){
    if (this.type == 'inbox')
	    return false;
    else if (this.flag == 'subscribed')
        return false;
    else if (this.type == 'label_root')
        return false;
    else
        return true;
}



/**
 * Gets the appropriate expander image based
 * on node state and children
 * 
 * @author Luke Preiner
 */
treeNode.prototype.getExpanderImage = function() {
	if (this.children.length > 0) {
		for (var i = 0; i < this.children.length; i++) {
			if (this.status == 'closed') 
				return IMG_EXPANDER_CLOSED;				
			else
				return IMG_EXPANDER_OPEN;					
		}
	}
	return IMG_EXPANDER_NONE;
};


/**
 * Gets the appropriate node icon based
 * on node type and state
 * 
 * @author Luke Preiner
 */
treeNode.prototype.getIcon = function (){
    
    if (this.flag == 'shared'){
        return IMG_FOLDER_SHARED;
    }
    else if (this.flag == 'subscribed'){
        return IMG_FOLDER_SUBSCRIBED;
    }
    else if (this.type == 'trash'){
        return IMG_FOLDER_TRASH;
    }
    else if (this.type == 'label_root'){
        return IMG_LABEL_ROOT;
    }
    else if (this.type == 'label'){
        return IMG_LABEL;
    }
    else if (this.type == 'inbox'){
        return IMG_FOLDER_INBOX;
    }
    else if (this.type == 'buddy'){
        return IMG_BUDDY;
    }
    else if (this.type == 'chipmark_root'){
        if (this.status == 'closed')
            return IMG_FOLDER_CLOSED;
         else
            return IMG_FOLDER_OPEN;
    }
    else if (this.type == 'buddy_root'){
        return IMG_BUDDY_ROOT;
    }
    else{
        // just show a safe default
        return IMG_FOLDER_CLOSED;
    }

}

treeNode.prototype.hasChild = function (childID){    
    if (this.type == 'trash')
        return false;
    var currNode = getTreeNodeByID(childID);
    while(currNode && currNode.type != 'chipmark_root'){
        if (this.getID() == currNode.getID())
            return true;
        else
            currNode = getTreeNodeByID(currNode.getParentID());
    }
    return false;
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Actions
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

treeNode.prototype.rename = function(){
    action = 'folder_edit';    
    showPanel('folderEditPanel');
}

treeNode.prototype.share = function(){
    action = 'folder_share';
    showPanel('folderSharePanel');
}

treeNode.prototype.move = function(destination){
    // make sure we aren't trying to move it to one of it's own children
    if (this.hasChild(destination)){
       alert("You cannot move a folder to one of it's own subfolders");
        return false;
    }        
    if (destination.toString().indexOf('label-') >= 0){
        alert("You cannot assign labels to that item");
        return false;
    }        
    if (this.type == 'label'){
        var destinationLink = getListLinkNodeByID(destination);
        var destinationFolder = getListFolderNodeByID(destination);
        var destinationTree = getTreeNodeByID(destination);
        if (destinationTree || destinationFolder){
            if (destinationTree.type == 'label' || destinationFolder){
                alert("You cannot assign labels to that item");
                return false;
            }    
        }    
        else if (destinationLink != null || destinationLink != 'undefined'){
            var labelName = new Array();
            labelName[0] = this.name;
            destinationLink.addTags(labelName);                        
            if (updateLink(destinationLink.getID(), destinationLink.permission, destinationLink.name, destinationLink.url, destinationLink.description, destinationLink.tags.join(','))){
                var id = 'label-' + labelName[0].toLowerCase();
                if (!getPanelNodeByID(id).hasChild(this.getID())) getPanelNodeByID(id).addChild(destinationLink);
                alert("The label '" + labelName[0] + "' has been added to '" + getListLinkNodeByID(destination).name + "'");       
                return true;
            }
            else{
                alert ("There was an error adding the label " + labelName[0] + " to your chipmark");            
                return false;
            }
        }    
    }    
    else{
        if (destination == TRASH_ID){
            this.remove();
        }
        else{
            var result = moveFolder(this.getID(), destination);
            if (result != false) {
                
                    var thisElement = document.getElementById('tree_' + this.getID());
                    thisElement.parentNode.removeChild(thisElement);
                    var destElement = document.getElementById('tree_' + destination);
                    destElement.appendChild(thisElement); 
                    
                    var currentParentTree = getTreeNodeByID(this.getParentID());
                    var destParentTree = getTreeNodeByID(destination);
                    currentParentTree.removeChild(this.getID());
                    destParentTree.addChild(this);
                                    
                    var currentParentPanel = getPanelNodeByID(this.getParentID());
                    var destParentPanel = getPanelNodeByID(destination);
                    currentParentPanel.removeChild(this.getID());
                    destParentPanel.addChild(getListFolderNodeByID(this.getID()));

                    this.setParentID(destination);
                    
                    //treeNodes[this.getID()] = null;

                    // Refresh the folder tree
	                drawTree();
	                setSelection(destination,'folder');                                        
                
            }
            else{
                alert("There was an error moving your folder");
            }                              
        }  
    }  
}


treeNode.prototype.addfolder = function(){
    action = 'folder_add';
    showPanel('folderEditPanel');
}


// mode = null : confirm and call NodeAction Servlet
// mode = 'noserver' : no confirm and no servlet call
treeNode.prototype.remove = function(mode){
    if (this.type == 'chipmark_root'){
        if (confirm("THIS ACTION WILL REMOVE ALL FOLDERS, CHIPMARKS AND SUBSCRIPTIONS!")){
            if (confirm("Are you sure you want to remove ALL folders, chipmarks and subscriptions?")){
                removeAll();
            }
        }    
    }
    else if (this.flag == 'subscribed'){
        if (confirm("Are you sure you want to remove your subscription to this folder?")){
            if(removeFolder(this.getID())){
                var thisElement = document.getElementById('tree_' + this.getID());
                thisElement.parentNode.removeChild(thisElement);
                
                var parentTree = getTreeNodeByID(this.getParentID());
                parentTree.removeChild(this.getID());
                var parentPanel = getPanelNodeByID(this.getParentID());
                parentPanel.removeChild(this.getID());
                
                // Remove subscriptions folder if none are left
                if (getTreeNodeByID(SUBSCRIPTIONS_ID).children.length < 1){
                    chipmarkRoot.removeChild(SUBSCRIPTIONS_ID);   
                    parentTree = chipmarkRoot;                 
                }
                
                // Remove any shared links from the label views
                // (currently disabled since links in subscribed
                // folders aren't placed into the label tree)
                for (var i = 0; i < this.children.length; i++){
                    if (this.children[i].type == 'link'){
                        // this.children[i].removeFromLabelTree();
                    }                
                }
                                
                drawTree();
                setSelection(parentTree.getID(),'folder');                        
                treeNodes[this.getID()] = null;
            }
            else{
                alert("There was an error removing your subscription");
            }    
        }        
    }
    else{
        var confirmation = false;
        var success = false;
        // The 'noserver' mode is for subfolders that have
        // already been removed by the servlet when the delete
        // call on the parent folder was made
        if (mode != 'noserver'){
            confirmation = confirm("Are you sure you want to delete this folder and all chipmarks it contains?");
            if (confirmation) success = removeFolder(this.getID());
        }
        else{
            confirmation = true;
            success = true;
        }    
        if (confirmation){
            if(success){
                var trash = getPanelNodeByID(TRASH_ID);
                var thispanel = getPanelNodeByID(this.getID());                
                for (var i = 0; i < thispanel.children.length; i++){
                    if (thispanel.children[i].type != 'folder'){ 
                        trash.addChild(thispanel.children[i]);
                        thispanel.children[i].removeFromLabelTree();
                        bookmarkCount--;
                    }    
                    else{
                        getTreeNodeByID(thispanel.children[i].getID()).remove('noserver');                                                
                    }    
                }
                for (var i = 0; i < thispanel.children.length; i++){                                        
                    thispanel.removeChild(thispanel.children[i].getID());                    
                }
		if(this.type != 'chipmark_root') folderCount--;
                if(this.type != 'chipmark_root' && mode != 'noserver'){
                    var thisElement = document.getElementById('tree_' + this.getID());
                    thisElement.parentNode.removeChild(thisElement);
                    
                    var parentTree = getTreeNodeByID(this.getParentID());
                    parentTree.removeChild(this.getID());
                    var parentPanel = getPanelNodeByID(this.getParentID());
                    parentPanel.removeChild(this.getID());
                    
                    treeNodes[this.getID()] = null;
		    
		    // Refresh the folder tree
		    drawTree();
                    setSelection(this.getParentID(),'folder');
		    displayStats();
		}
            }
            else{
                alert("There was an error removing your folder");
            }
        }    
    }
}
