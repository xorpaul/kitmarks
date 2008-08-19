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



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function listNode(      type,           // type of node (folder, link)
			  			id, 			// unique id
						name, 			// title of link
						url, 			// link url
						permission,		// permission
						desc,			// description						
						tags,			// array holding tags 
                        parentID,       // ID of the parent node
                        toolbarPosition)// Position on toolbar (optional)
{
    if (typeof toolbarPosition == "undefined")
        toolbarPosition = 0;

	//Make sure that permissions are set to something
	if(permission == '')
		permission = 'public';

	this.type = type;
	this.id = id;
	this.name = name;
	this.url = url;
	this.permission = permission;
	this.description = desc;
	this.tags = tags;	
	this.parentID = parentID;
	this.rowClass = '';
	this.trashed = false;
	this.toolbarPosition = toolbarPosition;
		
};

listNode.prototype.setParentID = function(parentId) {
	this.parentID = parentId;
};

listNode.prototype.getParentID = function() {
	return this.parentID;
};

listNode.prototype.getID = function(){
	return this.id;
};

listNode.prototype.setID = function(id){
	this.id = id;
};

listNode.prototype.hasLabel = function(label){
    for(var i = 0; i < this.tags.length; i++)
        if (trim(this.tags[i].toUpperCase()) == trim(label.toUpperCase()))
            return true;
    return false;
};

listNode.prototype.getURL = function(){
	return this.url;
};

/**
 * Is the node a draggable type?
 * 
 * @author Luke Preiner
 */
listNode.prototype.draggable = function(){    
    if (this.type == 'folder'){        
        if(getTreeNodeByID(this.getID()).type == 'local')
            return true;    
        else
	        return false;        
    }
    else if (getTreeNodeByID(currentlySelectedFolderID).type == 'label_root')
        return false;
    else
        return true;
    
}

/**
 * Is the node a valid droptarget?
 * 
 * @author Luke Preiner
 */
listNode.prototype.droppable = function(){
    if (this.type == 'folder'){
        var dragRef = getTreeNodeByID(this.getID());
        if(dragRef.type == 'local')
            return true;    
        else
	        return false;   
	} 
    else if (this.type == 'link'){
        return true;
    }
    else 
        return false;
}


/**
 * Should the node be moved, copied, or restored?
 * 
 * @author Luke Preiner
 */
listNode.prototype.dragType = function(){
    if (this.trashed == true)
        return 'restore';
    else if (this.permission == 'subscribed')
        return 'copy';
    else
        return 'move';
}


listNode.prototype.getSortString = function(){
    if(this.type == 'link')	{		
		return this.getID();
	}			
	else
	    return 0;
}

// Deletes a chipmark and copies the
// node to the trash
listNode.prototype.remove = function(){
    if (confirm("Are you sure you want to delete this chipmark?")){
        if (removeLink(this.getID())){
            this.removeFromLabelTree();
            var parentPanel = getPanelNodeByID(this.getParentID());
            var trashPanel = getPanelNodeByID(TRASH_ID);
            parentPanel.removeChild(this.getID());            
            trashPanel.addChild(this);                                    
            bookmarkCount--;
            showPanel(currentlySelectedFolderID,true);            
        }
    }    
};


// Restores a trashed bookmark
listNode.prototype.restore = function(parentID){

    if (!parentID || parentID == null){    
        // Make sure it's parent still exists
        var parentNode = getTreeNodeByID(this.getParentID());
        if (!parentNode)
            parentNode = chipmarkRoot;
    }
    else{
       var parentNode = getTreeNodeByID(parentID);
    }

    if (confirm("This chipmark will be restored to " + parentNode.name + ". Do you want to continue?")){
        var result = addLink(parentNode.getID(), this.permission, this.name, this.url, this.description, this.tags.join(','));
        if (result != false){            
            var parentPanel = getPanelNodeByID(parentNode.getID());
            var trashPanel = getPanelNodeByID(TRASH_ID);
            trashPanel.removeChild(this.getID());
            parentPanel.addChild(this);
            var newID = parseInt(result);           
            listLinkNodes[this.getID()] = null;
            listLinkNodes[newID] = this;            
            this.setID(newID);
            this.trashed = false;
            if (this.tags.length > 0){
                for (var i = 0; i < this.tags.length; i++){                        
                    var id = 'label-' + this.tags[i].toLowerCase();
                    var added = false;
                    if (!labelExists(this.tags[i]) && this.tags[i].length > 0){                            
                        // Label doesn't already exist
                        added = addLabel(this.tags[i]);
                        if (added){
                            var node = new treeNode('label', new Array(), 'closed', id, 'label_root', this.tags[i], 'normal', null);  
                            var panel = new panelNode('label_list', id, new Array()); 
                            labelRoot.addChild(node);
                            treeNodes[id] = node;		    
                            getPanelNodeByID('label_root').addChild(node);                        
                            panelNodes[id] = panel;
                            addLabel(this.tags[i]);
                        }
                    }
                    else{
                        added = true;
                        var panel = getPanelNodeByID(id); 
                    }	                    
                    if (added)
                        if(!panel.hasChild(this.getID())) panel.addChild(this);
                }
            }
            drawTree();
            bookmarkCount++;
            setSelection(TRASH_ID,'folder');
        }
    }
};


// Opens a chipmark
listNode.prototype.open = function (){        
    if(this.type == 'link'){
		//Open the URL in a new window if it is a link
		var win = window.open(this.getURL(), '', '');
		win.focus();
	}
}

// Edit a chipmark
listNode.prototype.edit = function (){    
    action = 'link_edit';
    showPanel('linkEditPanel');
}

// Move a chipmark
listNode.prototype.move = function (destination){        
    if (this.type == 'folder'){
        getTreeNodeByID(this.getID()).move(destination);
    }
    else if (getTreeNodeByID(currentlySelectedFolderID).type == 'label' && getTreeNodeByID(destination).type != 'trash' && getTreeNodeByID(destination).type != 'label'){
        alert("You cannot move a chipmark from a label view to a folder.  You must move directly from one folder to another");
        return false;
    }
    else{
        if (destination == TRASH_ID){
            this.remove();        
        }    
        else if (getTreeNodeByID(destination).type == 'label'){
                var labelName = new Array();
                labelName[0] = getTreeNodeByID(destination).name;
                this.addTags(labelName);
                var id = 'label-' + labelName[0].toLowerCase();
                if (!getPanelNodeByID(id).hasChild(this.getID())) getPanelNodeByID(id).addChild(this);
                if (updateLink(this.getID(), this.permission, this.name, this.url, this.description, this.tags.join(',')))
                    alert("The label '" + labelName[0] + "' has been added to '" + this.name + "'");           
                else
                    alert ("There was an error adding the label " + labelName[0] + " to your chipmark");
        }
        else{
            var result = moveLink(this.getID(), destination);
            if (result) {
                var parentPanel = getPanelNodeByID(this.getParentID());
                var destPanel = getPanelNodeByID(destination);
                parentPanel.removeChild(this.getID());
                this.setParentID(destination);
                destPanel.addChild(this);            
                //setSelection(currentlySelectedFolderID,'folder');     
                showPanel(currentlySelectedFolderID,true);
            }
            else{
                alert("There was an error moving your chipmark");
            } 
        }    
    }
}

// Send a chipmark
listNode.prototype.send = function (){    
    action = 'link_send';
    currentlySelectedItemID = this.getID();
    showPanel('linkSendPanel');
}

// Copy a chipmark
listNode.prototype.copy = function (destination){    
    if (destination == TRASH_ID){
        alert("You cannot copy a chipmark to the trash");
    }
    else if (getTreeNodeByID(destination).type == 'label')
        alert("You cannot add labels to chipmarks that aren't your own. Copy the chipmark to one of your own non-subscription folders before adding labels.");
    else{        
        var result = addLink(destination, DEFAULT_PERMISSION, this.name, this.url, this.description, this.tags.join(','));
        if (result) {                        
            var destPanel = getPanelNodeByID(destination);            
            var newListNode = new listNode('link',parseInt(result),this.name,this.url,DEFAULT_PERMISSION,this.description,this.tags,destination);
            listLinkNodes[newListNode.getID()] = newListNode;
            destPanel.addChild(newListNode);            
            //setSelection(currentlySelectedFolderID,'folder');
            showPanel(currentlySelectedFolderID,true);
            alert("The chipmark has been copied to your folder");                 
        }
        else{
            alert("There was an error copying your chipmark");
        } 
    }    
}



listNode.prototype.removeFromLabelTree = function(){

    for(var i = 0; i < this.tags.length; i++){
        var labelPanel = getPanelNodeByID('label-' + this.tags[i].toLowerCase());
        if (labelPanel){
            labelPanel.removeChild(this.getID());  
        
            // if label has no more chipmarks associated with it,
            // remove it
            if (labelPanel.children.length < 1){
                if (getTreeNodeByID(currentlySelectedFolderID).type == 'label')
                    currentlySelectedFolderID = getTreeNodeByID(labelPanel.treeNodeID).getParentID();
                removeLabel(this.tags[i].toLowerCase());                
            }
        }            
    }
    // Refresh the tree
    drawTree();
};






listNode.prototype.hideActionMenu = function (){
    var oldnode = document.getElementById("listitem" + this.type + this.getID());
    var element = document.getElementById("item_action_menu");
    
    if (element || element != null)
        element.parentNode.removeChild(element);
    
    if (oldnode || oldnode != null)
        oldnode.className = this.rowClass;
}


listNode.prototype.showActionMenu = function (){
    var container = document.getElementById("listitem" + this.type + this.getID());        
    
    // Build the action menu
    var attribs = {"id":"item_action_menu"};
    var itemTable = createElementIn(container,"table",attribs);
    var component = createElementIn(itemTable,"tbody", null);
    var row = createElementIn(component,"tr", null);
    var attribs = {"valign":"top","width":"20"};
    var spacerCell = createElementIn(row,"td", attribs);    

    if (this.trashed == true){    
        var attribs = {"valign":"top","align":"left"};
        var option1Cell = createElementIn(row,"td", attribs);
        var attribs = {"title": "Restore this chipmark"};
        var item = createElementIn(option1Cell,"a",attribs);
        item.className = "action_menu";
        item.onclick = function () { getListLinkNodeByID(currentlySelectedItemID).restore(); }
        var itemLabel = document.createTextNode("Restore");    	
	    item.appendChild(itemLabel); 
	    
        var attribs = {"valign":"top","align":"left"};
        var tableCell = createElementIn(row,"td", attribs);
        var attribs = {"title": "Open this chipmark: " + this.url};
        var item = createElementIn(tableCell,"a",attribs);
        item.className = "action_menu";
        item.onclick = function () { getListLinkNodeByID(currentlySelectedItemID).open(); }
        var itemLabel = document.createTextNode("Open");    	
        item.appendChild(itemLabel); 
    }
    else {
        if (this.type == 'link' && this.permission != 'subscribed'){    
            var attribs = {"valign":"top","align":"left"};
            var option1Cell = createElementIn(row,"td", attribs);
            var attribs = {"title": "Open this chipmark: " + this.url};
            var item = createElementIn(option1Cell,"a",attribs);
            item.className = "action_menu";
            item.onclick = function() { getListLinkNodeByID(currentlySelectedItemID).open(); };
            var itemLabel = document.createTextNode("Open");    	
	        item.appendChild(itemLabel); 

            var attribs = {"valign":"top","align":"left"};
            var option2Cell = createElementIn(row,"td", attribs);
            var attribs = {"title":"Edit this chipmark"};
            var item = createElementIn(option2Cell,"a",attribs);
            item.className = "action_menu";
            item.onclick = function() { getListLinkNodeByID(currentlySelectedItemID).edit(); };
            var itemLabel = document.createTextNode("Edit...");    	
	        item.appendChild(itemLabel); 

            var attribs = {"valign":"top","align":"left"};
            var option3Cell = createElementIn(row,"td", attribs);
            var attribs = {"title":"Send this chipmark to a buddy"};
            var item = createElementIn(option3Cell,"a",attribs);
            item.className = "action_menu";
            item.onclick = function() { getListLinkNodeByID(currentlySelectedItemID).send(); };
            var itemLabel = document.createTextNode("Send...");    	
	        item.appendChild(itemLabel); 

            var attribs = {"valign":"top","align":"left"};
            var option4Cell = createElementIn(row,"td", attribs);
            var attribs = {"title":"Delete this chiparmk"};
            var item = createElementIn(option4Cell,"a",attribs);
            item.className = "action_menu";
            item.onclick = function() { getListLinkNodeByID(currentlySelectedItemID).remove(); };
            var itemLabel = document.createTextNode("Delete");    	
	        item.appendChild(itemLabel); 
	        
        }
         if (this.type == 'link' && this.permission == 'subscribed'){  
            var attribs = {"valign":"top","align":"left"};
            var option1Cell = createElementIn(row,"td", attribs);
            var attribs = {"title": "Open this chipmark: " + this.url};
            var item = createElementIn(option1Cell,"a",attribs);
            item.className = "action_menu";
            item.onclick = function() { getListLinkNodeByID(currentlySelectedItemID).open(); };
            var itemLabel = document.createTextNode("Open");    	
	        item.appendChild(itemLabel); 
	        
            var attribs = {"valign":"top","align":"left"};
            var option3Cell = createElementIn(row,"td", attribs);
            var attribs = {"title":"Send this chipmark to a buddy"};
            var item = createElementIn(option3Cell,"a",attribs);
            item.className = "action_menu";
            item.onclick = function() { getListLinkNodeByID(currentlySelectedItemID).send(); };
            var itemLabel = document.createTextNode("Send...");    	
	        item.appendChild(itemLabel); 
	                       	        	        
         }
    }
    
	container.className = "list_node_selected";

}




/**
 * Gets the appropriate node icon based
 * on node type
 * 
 * @author Luke Preiner
 */
listNode.prototype.getIcon = function (){
    if (this.type == 'folder'){
        return getTreeNodeByID(this.getID()).getIcon();
    }
    else if (this.type == 'link'){
        if (this.permission == 'private'){
            return IMG_LINK_PRIVATE;
        }
        else if (this.permission == 'public'){
            return IMG_LINK_PUBLIC;
        }
        else if (this.permission == 'subscribed'){
            return IMG_LINK_SUBSCRIBED;
        }
    }   

    else{
        // just show a safe default
        return IMG_FOLDER_CLOSED;
    }
}


//Node::addTags():
//	Adds tags to a node, removing duplicates and converting
//	to lowercase first.
listNode.prototype.addTags = function(tags){
    for (var j = 0; j < tags.length; j++){	
        var add = true;
        var labelName = trim(tags[j]);
        if (labelName.length > 0){
            for (var i = 0; i < this.tags.length; i++){
                if (this.tags[i] == labelName)
                    add = false;
            }
            if (add == true){
                this.tags.push(labelName);
                this.tags.sort();
            }
        }
    }	
};


