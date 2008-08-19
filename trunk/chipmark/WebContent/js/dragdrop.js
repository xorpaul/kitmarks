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

var cursorStarted = false;				
var currentDragElementID = null;
var currentDragElementType = null;
var imageContainer;
var dragging = false;
var sortLink = false;

var dragLineStyle = "2px blue dotted";

function initDragAndDrop(){
    document.getElementsByTagName("html")[0].onclick = mouseClick;
	document.getElementsByTagName("html")[0].onmouseup = mouseClick;
    imageContainer = document.getElementById('imageContainer');
}


function cursorHandler(evt){  
    if (BROWSER && BROWSER != "IE"){
        imageContainer.style.left = evt.clientX + window.pageXOffset + 17 + 'px';
        imageContainer.style.top = evt.clientY + window.pageYOffset + 17 +'px';
    }
    else{
        imageContainer.style.left = event.clientX + document.body.scrollLeft + 17 + 'px';
        imageContainer.style.top = event.clientY + document.body.scrollTop + 17 +'px';    
    }
    imageContainer.style.visibility = 'visible';    
    dragging = true;
}



function restartImageCursor(){    
        document.onmousemove = cursorHandler; 
}

function stopImageCursor(){
  imageContainer.style.visibility = 'hidden';
  document.onmousemove = null;
  dragging = false;
}



//  mouseHover():
//	Event handler for onmouseover and onmouseout.  Is used to add
//	the horizontal bar when dragging and dropping, and also
//	controls the background color of the items.
function mouseHover(hoverNodeID, action, hoverNodeType){

    // Get the node of the appropriate type    
    if (hoverNodeType == 'list_node_folder')
	    var node = getListFolderNodeByID(hoverNodeID);	 
	else if (hoverNodeType == 'list_node_link')
        var node = getListLinkNodeByID(hoverNodeID);
    else
        var node = getTreeNodeByID(hoverNodeID);
	    
	var dragNode = getTreeNodeByID(currentDragElementID);
		
	if (dragging){
		// we're dragging in the list area
		if(hoverNodeID != currentlySelectedItemID && hoverNodeType != 'tree_node') {
            var element = document.getElementById("listitem" + node.type + hoverNodeID);
			if(action == 'in') { 
				if(element){
				    if (hoverNodeType == 'list_node_link' && currentDragElementType == 'list_node_link'){
					    //element.className = 'list_node_hover_reorder';
					    element.style.borderTop = dragLineStyle;
					}
					else{
					    element.style.borderTop = '0px solid white';
					    element.className = 'list_node_hover';
					}
				}	
			}
			else if(action == 'out') {
				if(element){
				    if (hoverNodeType == 'list_node_folder')
					    element.className = getListFolderNodeByID(hoverNodeID).rowClass;
					else if (hoverNodeType == 'list_node_link'){
					    element.style.borderTop = '0px solid white';
					    element.className = getListLinkNodeByID(hoverNodeID).rowClass;
					}    
			    }
			}
		}
        // we're dragging over the tree area
		if(hoverNodeID != currentlySelectedFolderID && hoverNodeType != 'list_node_folder' && hoverNodeType != 'list_node_link') {
            var element = document.getElementById("nodelabel" + hoverNodeID);
            if(element) {
                if(action == 'in') {
                    element.className = 'tree_drag_over';
                }
                else if(action == 'out') {
                    element.style.color = "";
                    element.className = 'tree_nodes';
                }
            }
        }
		return;
	}
    else{
        currentlySelectedDragID = null;        
    }
}

/**
 * Event handler for onmousedown
 *
 * @param dragNodeID
 * @param dragNodeType
 *
 * @author Luke Preiner
 */
function mouseDown(dragNodeID, dragNodeType) {
    //Store this element as the current drag element
	currentDragElementID = dragNodeID;
    currentDragElementType = dragNodeType;
    var node = null;
    //Get the dragged node
    if(dragNodeType == 'tree_node'){    
		node = getTreeNodeByID(dragNodeID);    
    }
    else if (dragNodeType == 'list_node_folder'){
        node = getListFolderNodeByID(dragNodeID);
    }
    else if (dragNodeType == 'list_node_link'){
        node = getListLinkNodeByID(dragNodeID);
    }
    else if (dragNodeType == 'list_node_label'){
        node = getListFolderNodeByID(dragNodeID);
    }
    else
        alert("mouseDown(): dragNodeType is not recognized: '" + dragNodeType + "'");

    //Set the cursor image
    if(node != null){        
	    var image = document.getElementById("cursorImage");        
        image.src = node.getIcon();    	
	    restartImageCursor();    	
	}
}



/**
 * Event handler for onmouseup
 *
 * @param dropNodeID
 * @param dropNodeType
 *
 * @author Luke Preiner
 */
function mouseUp(dropNodeID, dropNodeType){
    // Exit if we weren't dragging
	if(currentDragElementID == 0 || currentDragElementID == null || dragging == false)
		return;
	
	// Kill the drag image icon
	stopImageCursor();

    // Get the drop node
    if (dropNodeType == 'tree_node')	
	    dropNode = getTreeNodeByID(dropNodeID);
    else if (dropNodeType == 'list_node_folder')
         dropNode = getListFolderNodeByID(dropNodeID);
    else if (dropNodeType == 'list_node_link')
         dropNode = getListLinkNodeByID(dropNodeID);
    
    // Declare vars
    var dragNode;
    var dropNode;    
    var actionType = null;
    
    // Get the drag node
    if (currentDragElementType == 'tree_node'){
	    dragNode = getTreeNodeByID(currentDragElementID);	
	    actionType = 'move';    
	}
    else if (currentDragElementType == 'list_node_folder'){
        dragNode = getListFolderNodeByID(currentDragElementID); 
        actionType = dragNode.dragType();       
    }            
    else if (currentDragElementType == 'list_node_link'){
        dragNode = getListLinkNodeByID(currentDragElementID); 
        actionType = dragNode.dragType();       
    }
    else
        alert("mouseUp(): Drag element type is not recognized");
    
    if (dropNodeType == 'list_node_folder' || dropNodeType == 'tree_node')
        var dropType = 'folder';
    else if (dropNodeType == 'label')
        var dropType = 'label';
    else
        var dropType = 'link';

    if (currentDragElementType == 'list_node_folder' || currentDragElementType == 'tree_node')
        var dragType = 'folder';
    else 
        var dragType = 'link';
        

    // Make sure source/destinations are valid sources/targets
    var validated = (actionType != null) && (dragNode.draggable() && dropNode.droppable());
    validated = validated && (((currentDragElementID != dropNodeID) && (dragType == dropType)) || ((currentDragElementID == dropNodeID) && (dragType != dropType)) || ((currentDragElementID != dropNodeID) && (dragType != dropType)));

    //Make sure we're not dragging to the same exact node
    if ((currentDragElementID == dropNodeID) && (dragType == dropType)){
        validated = false;
    }    
    else if ((dragType == 'link') && (dropType == 'link')){        
        // Reorder the chipmarks
        getPanelNodeByID(currentlySelectedFolderID).moveNode(dragNode.getID(),dropNode.getID());
        showPanel(currentlySelectedFolderID,true);
        updateSortOrder(getPanelNodeByID(currentlySelectedFolderID).getSortString());
        validated = false;
    }
        
    if (validated){            
		    if (actionType == 'move'){
		        dragNode.move(dropNode.getID());
		    }
		    else if (actionType == 'copy'){
		        dragNode.copy(dropNode.getID());
		    }
		    else if (actionType == 'restore'){
		        dragNode.restore(dropNode.getID());
		    }
    }
    else {
        if ((currentDragElementID == dropNodeID) && (dragType == dropType) && (currentDragElementType != dropNodeType))
            alert("You cannot move a folder to itself");
        else if ((currentDragElementID == dropNodeID) && (dragType == dropType)){
            // just do nothing.  this is a lame hack to suppress the alert in this case
        }
        else if (dragNode.draggable() == false)
            alert("That item cannot be moved");            
        else if (dropNode.droppable() == false)
            alert("You cannot move that to that location");        
    }
    
    // Reset the drag element	
	currentDragElementID = null;
    currentDragElementType = null;
	
	// Get the appropriate node type    
    if (dropNodeType == 'list_node_folder')
	    var node = getListFolderNodeByID(dropNodeID);	 
	else if (dropNodeType == 'list_node_link')
        var node = getListLinkNodeByID(dropNodeID);
    else
        var node = getTreeNodeByID(dropNodeID);
	
	if (dropNodeType == 'tree_node')
	    var element = document.getElementById("nodelabel" + dropNodeID);
	else
        var element = document.getElementById("listitem" + node.type + dropNodeID);
    
    if(element){
        if (dropNodeType == 'list_node_folder')
	        element.className = getListFolderNodeByID(dropNodeID).rowClass;
        else if (dropNodeType == 'list_node_link'){
		    element.className = getListLinkNodeByID(dropNodeID).rowClass;
            element.style.borderTop = "";
            getListLinkNodeByID(dropNodeID).hideActionMenu();
        }
		else if (dropNodeType == 'tree_node'){
		    element.style.color = "";
		    element.className = 'tree_nodes';
		}
    }
			
}		

//mouseClick():
//	Supposed to handle cases where mouseup is not caught.
function mouseClick(){
    if (!sortLink)
	       ELEMENTS['SORT_MENU'].style.visibility = 'hidden';   
	sortLink = false;
	
	cancelDrag();
	currentDragElementID = 0;	
}


//cancelDrag():
//	Just removes the drag icon image.
function cancelDrag(){
	stopImageCursor();
}

