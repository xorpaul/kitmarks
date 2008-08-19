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

function panelNode(     type,           // folder listing, trash, link_edit, folder_share, 
                                        //      folder_edit, buddy
                        treeNodeID,     // the ID of the tree node that owns this panel
                        children)       // array holding child listNodes
{

    this.type = type;
    this.treeNodeID = treeNodeID;
    this.children = children;
                    
};


panelNode.prototype.addChild = function (child) {
    this.children.push(child);
};


panelNode.prototype.removeChild = function(childID){
    for(var t = 0; t < this.children.length; t++){
        if (this.children[t].getID() == childID){
            this.children.splice(t,1);
            return;
        }
    }
    alert("panelNode.removeChild(): Could not find node: " + childID);
};




//    Moves n1 from its current location to n2's location,
//    and shifts everything else.
panelNode.prototype.moveNode = function(id1, id2) {
    //Don't bother swapping equal nodes.
    if(id1 == id2) {
        return;
    }

    var iN1 = -1;
    var iN2 = -1;

    //Find the first index
    for(var i = 0; i < this.children.length; i++) {
        if(this.children[i].getID() == id1) {
            iN1 = i;
        }
    }

    //Make sure both nodes exist
    if(iN1 == -1) {
        alert("listNode.moveNode(): Attempted to move links that do not exist.");
        return;
    }

    //Remove n1
    var n1 = this.children[iN1];
    for(i = iN1; i < this.children.length - 1; i++) {
        this.children[i] = this.children[i+1];
    }
    this.children.length--;


    //If id2 is zero, then just move the link to the end.
    if(id2 == 0)    {
        this.children.push(n1);
        return;
    }

    //Find the next index
    for(var i = 0; i < this.children.length; i++) {
        if(this.children[i].getID() == id2) {
            iN2 = i;
        }
    }

    //Make sure the other index was found.
    if(iN2 == -1)    {
        alert("listNode.moveNode(): Attempted to move links that do not exist.");
        return;
    }

    //Insert n1
    var temp1 = 0;
    var temp2 = n1;
    
    for(i = iN2; i < this.children.length; i++)    {
        temp1 = this.children[i];
        this.children[i] = temp2;
        temp2 = temp1;
    };
    this.children[this.children.length] = temp2;
};



//    Returns the node's sort string.
panelNode.prototype.getSortString = function(){    
        var sortString = "";
        for(var i = 0; i < this.children.length; i++){
            sortString += '|' + this.children[i].getSortString();
        };

        if (sortString.charAt(0) == '|')
                sortString = sortString.substring(1, sortString.length);
        
        return sortString;
}



panelNode.prototype.hasChild = function (childID){    
    for (var i = 0; i < this.children.length; i++){
        if (this.children[i].getID() == childID)
            return true;
    }    
    return false;
}





panelNode.prototype.sortMenu = function (){
    // the mouseClick() handler should take care of the rest
        sortLink = true;    
        
        var link = document.getElementById("sort_menu_link");
        if (link){       
            link.style.position = 'relative';
            ELEMENTS['SORT_MENU'].style.left = findPos(link)[0] - ((ELEMENTS['SORT_MENU'].offsetWidth - link.offsetWidth) / 2);
            ELEMENTS['SORT_MENU'].style.top  = findPos(link)[1] + link.offsetHeight;
            
            if (ELEMENTS['SORT_MENU'].style.visibility != 'visible')
                ELEMENTS['SORT_MENU'].style.visibility = 'visible';            
            else
                ELEMENTS['SORT_MENU'].style.visibility = 'hidden';            
        }        
    
    
}



//Note:
//    Instead of just enumerating the sort types,
//    a list of sort functions could be applied:
//        A generic sort function accesses a list
//        or sort functions.  It applies the first
//        one.  If the result is zero, it applies
//        the next, otherwise it returns the result.
//        If it reaches the end it returns 0.

//Sort by type
function sortType(n1, n2) {
    //Both folders?
    if(n1.type != 'link' && n2.type != 'link')
        return 0;
    
    //Both links?
    if(n1.type == n2.type)
        return 0;
    
    //First folder, second link.
    if(n1.type == 'folder')
        return -1;
    
    //Vice versa.
    return 1;
}

//Sorting by ID ascending is the same as sorting by date desc.
function sortDate(n1, n2) {
    //Just compare ID
    if(n1.getID() > n2.getID())
        return 1;
    if(n1.getID() < n2.getID())
        return -1;
    
    return 0;
}

//Sort by name
function sortName(n1, n2) {
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

//Sort by name for folders
function sortFolderName(n1, n2) {
    //Compare by name
    if(n1.type == 'link' && n2.type == 'link') {
        if(n1.tempid < n2.tempid)
            return -1;
        if(n1.tempid > n2.tempid)
            return 1;
        return 0;
    }

    if(n1.type == 'link' && n2.type != 'link')
        return 1;
    if(n1.type != 'link' && n2.type == 'link')
        return -1;

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

//Sort by permission
function sortPermission(n1, n2) {
    //If the permissions are equal:
    if(n1.permission == n2.permission)
        return 0;

    //Public is considered larger
    if(n1.permission == 'private')
        return -1;
    return 1;
}





//Sort the node by the parameter string
function sortByParams(n1, n2) {    
    var r = 0;

    //Iterate over the list, sorting by each function.
    for(var i = 0; i < sortFuncs.length; i++) {
        //Apply the function
        var f = sortFuncs[i];
        r = f(n1, n2);
        
        //If not equal, return
        if(r != 0) {
            return r;
        }
    }
    
    //Absolutely equal.
    return 0;
}



//Sort all children of a node.
panelNode.prototype.sortChildren = function(sortString) {
    //Iterate through children, sorting.
    for(var i = 0; i < this.children.length; i++) {
        this.children[i].sortNode(sortString);
    }
}


panelNode.prototype.doSort = function(sortType){
    sortType = sortType.toLowerCase();

    if (sortType == 'type')
        this.children.sort(sortType);
    else if (sortType == 'date')
        this.children.sort(sortDate);
    else if (sortType == 'name')
        this.children.sort(sortName);
    else if (sortType == 'permission')
        this.children.sort(sortPermission);
    else
        return;  
        
    // make sure folders are always first
    this.sortFolders();
        
    updateSortOrder(getPanelNodeByID(currentlySelectedFolderID).getSortString());    
    setSelection(currentlySelectedFolderID,'folder');
       
}



//    Just sort so that folders are always at the top,
//    and are always in sorted order.
panelNode.prototype.sortFolders = function() {
    //Can only sort this if it is a folder.
    if(this.type == 'link')
        return;

    //Sort sub folders and assign temp ids for a stable sort.
    for(var i = 0; i < this.children.length; i++)    {
        this.children[i].tempid = i;
        //this.children[i].sortFolders();
    }

    //Sort children.
    this.children.sort(sortFolderName);
    return;
}

panelNode.prototype.addlink = function () {
    action = 'link_add';
    showPanel('linkEditPanel');
}

panelNode.prototype.HTML = function (container, scrollhold) {
    // Define the alternating row styles
    var nodeclass = new Array();
    nodeclass[0] = "list_node_1";
    nodeclass[1] = "list_node_2";
        
    if (this.type == 'folder_list' || this.type == 'label_list'){
        
        // Check to see if folder is empty and display a message
        // if it is.
        if (this.children.length == 0){                                
            var emptyMessage = "This folder contains no items.";
            var nodeLabelText = document.createTextNode(emptyMessage);        
            container.appendChild(nodeLabelText);    
            return;
        }

        // Use innerHTML if IE, DOM otherwise.
        // This is due to IE's inability to properly
        // set DHTML event attributes (onclick, etc) via
        // standard DOM methods.
        if (BROWSER != null && BROWSER == 'NON-IE'){
            for (var i = 0; i < this.children.length; i++){
                var child = this.children[i];
                var nodeIcon = child.getIcon();
                var nodeName = child.name;
                var nodeType = child.type;
                
                child.rowClass = nodeclass[i % 2];
                
                if (nodeType == 'folder')
                    var toolTip = "Click to open this folder";                 
                else if (nodeType == 'label')
                    var toolTip = "Click to filter by this label";    
                else
                    var toolTip = "Click to edit this bookmark";
                    
                // create the div
                var attribs = { "id":"listitem" + nodeType + child.getID(),
                                "class": nodeclass[i % 2],                                 
                                "onclick":"itemClick('" + child.getID() + "','" + nodeType + "')",
                                "onmousedown":"mouseDown('" + child.getID() + "','list_node_" + nodeType + "')",
                                "onmouseup":"mouseUp('" + child.getID() + "','list_node_" + nodeType + "')",
                                "onmouseover":"mouseHover('" + child.getID() + "','in','list_node_" + nodeType + "')",                                
                                "onmouseout":"mouseHover('" + child.getID() + "','out','list_node_" + nodeType + "')"};
                var itemDiv = createElementIn(container, "div", attribs);        
                disableTextSelection(itemDiv);
                // create a table
                var attribs = {"class":"listitem_table"};
                var itemTable = createElementIn(itemDiv,"table",attribs);
                var component = createElementIn(itemTable,"tbody", null);
                var row = createElementIn(component,"tr", null);
                var attribs = {"class":"listitem_table_icon","valign":"middle"};
                var iconCell = createElementIn(row,"td", attribs);
                var attribs = {"class":"listitem_table_text","valign":"middle"};
                var textCell = createElementIn(row,"td", attribs);
                disableTextSelection(textCell);
                var attribs = {"class":"listitem_table_action","valign":"middle"};
                var actionCell = createElementIn(row,"td", attribs);
                                
                var attribs = { "id":"listicon"+child.getID(),
                                "src": nodeIcon, 
                                "width":"15",
                                "height":"15",
                                "onclick":"itemClick('" + child.getID() + "','" + nodeType + "')",
                                "ondragstart":"return false;",
                                "alt":toolTip,
                                "title":toolTip};
                var icon = createElementIn(iconCell, "img", attribs);        

                var attribs = { "id":"itemlabel"+child.getID(),
                                "class":"list_nodes",
                                "title":toolTip, 
                                "onclick":"itemClick('" + child.getID() + "','" + nodeType + "')",
                                "onmouseover":'setElementText("manage_status_bar", "' + escape(nodeName) + '");'};         
                var listLabel = createElementIn(textCell,"a",attribs);
                disableTextSelection(listLabel);
                var nodeLabelText = document.createTextNode(nodeName);        
                listLabel.appendChild(nodeLabelText);               

                textCell.appendChild(listLabel);

                if (nodeType == 'link'){                
                    var attribs = {"src": IMG_LINK_SEND, "width":"15","height":"15","onclick":"getListLinkNodeByID('" + child.getID() + "').send();","alt":"Send this chipmark", "title": "Send this chipmark"};
                    var quicklink = createElementIn(actionCell, "img", attribs);

                    var attribs = {"src": IMG_LINK_OPEN, "width":"13","height":"13","onclick":"getListLinkNodeByID('" + child.getID() + "').open();","alt":"Open this chipmark: " + child.url, "title": "Open this chipmark: " + child.url};
                    var quicklink = createElementIn(actionCell, "img", attribs);
                }
            }
            if (!scrollhold) container.scrollTop = 0;
        }
        else {
             var totalhtml = '';
             for (var i = 0; i < this.children.length; i++){
                    var child = this.children[i];
                    var nodeIcon = child.getIcon();
                    var nodeName = htmlspecialchars(child.name);
                    var nodeType = child.type;

                    child.rowClass = nodeclass[i % 2];
                    
                    if (nodeType == 'folder')
                        var toolTip = "Click to open this folder";                 
                    else
                        var toolTip = "Click to edit this bookmark";
                        
                    // create the div
                    var html = '<div id="listitem' + nodeType + child.getID() + '" class="' + nodeclass[i % 2] + '" onselectstart="return false;" ondragstart="return false;" ';
                                        
                    html += 'onmousedown="mouseDown(\'' + child.getID() + "','list_node_" + nodeType + "');" + '" ';
                    html += 'onmouseup="mouseUp(\'' + child.getID() + '\',\'list_node_' + nodeType + '\');" ';
                    html += 'onmouseover="mouseHover(\'' + child.getID() + '\',\'in\',\'list_node_' + nodeType + '\');" ';                                
                    html += 'onmouseout="mouseHover(\'' + child.getID() + '\',\'out\',\'list_node_' + nodeType + '\');">';   
                                        
                    html += '<table class="listitem_table"  cellpadding="0" cellspacing="0" border="0"><tr>';
                    
                    html += '<td class="listitem_table_icon" onselectstart="return false;" valign="middle"><img src="' + nodeIcon + '" width="15" height="15" onclick="itemClick(\'' + child.getID() + "','" + nodeType + '\');" alt="' + toolTip + '" title="' + toolTip + '"/></td>';

                    html += '<td id="listitemtext' + nodeType + child.getID() + '" class="listitem_table_text"  valign="middle">';
                    html += '<a id="listitemlink' + nodeType + child.getID() + '" ondragstart="return false;" onselectstart="return false;" href="#" class="list_nodes" title="' + toolTip + '" onclick="itemClick(\'' + child.getID() + "','" + nodeType + '\');return false;">' + nodeName + '</a></td>';
                    
                    html += '<td class="listitem_table_action"  valign="middle">';

                    if (nodeType == 'link'){                
                        html += '<img src="' + IMG_LINK_SEND + '" width="15" height="15" onclick="getListLinkNodeByID(\'' + child.getID() + '\').send();" alt="Send this chipmark" title="Send this chipmark"/>';
                        html += '<img src="' + IMG_LINK_OPEN + '" width="13" height="13" onclick="getListLinkNodeByID(\'' + child.getID() + '\').open();" alt="Open this chipmark: ' + child.url + '" title="Open this chipmark: ' + child.url + '" />';
                    }

                    html += '</td></tr></table></div>';                
                    
                    totalhtml += html;                    
             }            
            container.innerHTML = totalhtml;
            if (!scrollhold) container.scrollTop = 0;
        }    
    
    }
     else if (this.type == 'label_root'){
        var description = "Labels are another form of organizing your bookmarks - we currently support both a hierarchical folder structure and labels. A single chipmark can be given many labels.  The chipmark will show up under the label views for each of the labels assigned to it.  Labels can currently be used with our search features and can be used to filter your Chipmarks on this label view.  We are continuing to expand our use of labels in Chipmark and they will provide additional bookmarking power in the future.";        
        var attribs = {"style":"width: 100%;"};
        var itemTable = createElementIn(container,"table",attribs);
        var component = createElementIn(itemTable,"tbody", null);
        var row = createElementIn(component,"tr", null);
        var attribs = {"align":"left","width":"100%","class":"panel_title_text"};
        var titleCell = createElementIn(row,"td", attribs);
        titleCell.className = "panel_title_text";
        var row = createElementIn(component,"tr",null);
        var attribs = {"align":"left","width":"100%"};
        var descCell = createElementIn(row,"td", attribs);
        descCell.className = "panel_description_text";
        
        var nodeLabelText = document.createTextNode("Labels");        
        titleCell.appendChild(nodeLabelText);          
        
        var nodeLabelText = document.createTextNode(description);        
        descCell.appendChild(nodeLabelText); 
    }            
    else if (this.type == 'trash'){
        var description = "This folder holds a list of chipmarks you've deleted in case you change your mind.  It is cleared automatically when you leave the management page.";
        if (BROWSER != null && BROWSER == 'NON-IE'){ 
            var attribs = {"style":"width: 100%;"};
            var itemTable = createElementIn(container,"table",attribs);
            var component = createElementIn(itemTable,"tbody", null);
            var row = createElementIn(component,"tr", null);
            var attribs = {"align":"left","width":"100%","class":"panel_title_text"};
            var titleCell = createElementIn(row,"td", attribs);

            var row = createElementIn(component,"tr",null);
            var attribs = {"align":"left","width":"100%"};
            var descCell = createElementIn(row,"td", attribs);
            descCell.className = "panel_description_text";
            
            var nodeLabelText = document.createTextNode("Trash");        
            titleCell.appendChild(nodeLabelText);          
            
            var nodeLabelText = document.createTextNode(description);        
            descCell.appendChild(nodeLabelText); 
            
            for (var i = 0; i < this.children.length; i++){
                var child = this.children[i];
                var nodeIcon = child.getIcon();
                var nodeName = child.name;
                var nodeType = child.type;
                
                child.rowClass = nodeclass[i % 2];

                var toolTip = "Click to view options";
                    
                // create the div
                var attribs = {"id":"listitem" + nodeType + child.getID(),"class": nodeclass[i % 2], "onmouseover":'setElementText("manage_status_bar", "' + nodeName + '");'};
                var itemDiv = createElementIn(container, "div", attribs);        
                disableTextSelection(itemDiv);
                            
                // create a table
                var attribs = {"class":"listitem_table"};
                var itemTable = createElementIn(itemDiv,"table",attribs);
                var component = createElementIn(itemTable,"tbody", null);
                var row = createElementIn(component,"tr", null);
                var attribs = {"class":"listitem_table_icon","valign":"middle"};
                var iconCell = createElementIn(row,"td", attribs);
                var attribs = {"class":"listitem_table_text","valign":"middle"};
                var textCell = createElementIn(row,"td", attribs);
                disableTextSelection(textCell);
                var attribs = {"class":"listitem_table_action","valign":"middle"};
                var actionCell = createElementIn(row,"td", attribs);
                
                var attribs = {"id":"listicon"+child.getID(),"src": nodeIcon, "width":"15","height":"15","onclick":"itemClick(" + child.getID() + ",'" + nodeType + "')","alt":toolTip,"title":toolTip};
                var icon = createElementIn(iconCell, "img", attribs);

                var attribs = {"id":"itemlabel"+child.getID(),"class":"list_nodes","title":toolTip, "onclick":"itemClick(" + child.getID() + ",'" + nodeType + "')","onmouseover":'setElementText("manage_status_bar", "' + nodeName + '");'};         
                var listLabel = createElementIn(textCell,"a",attribs);
                disableTextSelection(listLabel);
                var nodeLabelText = document.createTextNode(nodeName);        
                listLabel.appendChild(nodeLabelText);               

                textCell.appendChild(listLabel);
                    
                // Let the link know it's in the trash
                child.trashed = true;
            }
            container.scrollTop = 0;
        }
        else{
            html = '<table style="width:100%"><tr><td align="left" width="100%" class="panel_title_text">Trash</td></tr>';
            html += '<tr><td align="left" width="100%" class="panel_description_text">' + description + '</td></tr></table>';

            for (var i = 0; i < this.children.length; i++){
                var child = this.children[i];
                var nodeIcon = child.getIcon();
                var nodeName = child.name;
                var nodeType = child.type;
                
                child.rowClass = nodeclass[i % 2];

                var toolTip = "Click to view options";
                    
                // create the div
                html += '<div id="listitem' + nodeType + child.getID() + '" class="' + nodeclass[i % 2] + '" onselectstart="return false;" >'
                html += '<table class="listitem_table"><tr>';
                html += '<td class="listitem_table_icon" valign="middle"><img id="listicon' + child.getID() + '" src="' + nodeIcon + '" width="15" height="15" onclick="itemClick(' + child.getID() + ',\'' + nodeType + '\')" alt="' + toolTip + '" title="' + toolTip + '"/></td>';
                html += '<td class="listitem_table_text" valign="middle"><a id="itemlabel'+child.getID()+'" class="list_nodes" title="'+toolTip+'" onclick="itemClick(' + child.getID() + ',\'' + nodeType + '\')">' + nodeName + '</a></td>';
                html += '</tr></table></div>';
                // Let the link know it's in the trash
                child.trashed = true;

            }
            container.innerHTML = html;
            container.scrollTop = 0;
        }
    }
    else if (this.type == 'link_edit'){       
        
        var panel = createElementIn(container,"div", null);
        panel.className = "action_panel";
        
        if (action == "link_add"){
            var nodeLabelText = document.createTextNode("Add A New Chipmark");
            var link = new listNode('link',0,'','http://','public','','',currentlySelectedFolderID);
        }
        else{
            var nodeLabelText = document.createTextNode("Edit A Chipmark");
            var link = getListLinkNodeByID(currentlySelectedItemID);      
        }
        panel.appendChild(nodeLabelText);
        
        // create a table
        var attribs = {"id":"editform"};
        var form = createElementIn(panel,"form", attribs);
        form.onsubmit = function(){ return false; };
        var attribs = {"width":"95%","border":"0","align":"center"};
        var itemTable = createElementIn(form,"table",attribs);
        var component = createElementIn(itemTable,"tbody", null);
        var row1 = createElementIn(component,"tr", null);
        var row2 = createElementIn(component,"tr", null);
        var attribs = {"width":"67%","valign":"top"};
        var left = createElementIn(row1,"td", attribs);
        var attribs = {"width":"33%","valign":"top"};
        var right = createElementIn(row1,"td", attribs);
        var attribs = {"width":"67%","align":"left"};
        var cancelCell = createElementIn(row2,"td", attribs);
        var attribs = {"width":"33%","align":"right"};
        var OKCell = createElementIn(row2,"td", attribs);


        
        var attribs = {"width":"100%","border":"0","cellpadding":"0","cellspacing":"0"};
        var itemTable = createElementIn(left,"table",attribs);
        var component = createElementIn(itemTable,"tbody", null);
        var row1 = createElementIn(component,"tr", null);
        var row2 = createElementIn(component,"tr", null);
        var row3 = createElementIn(component,"tr", null);
        var row4 = createElementIn(component,"tr", null);
        var row5 = createElementIn(component,"tr", null);
        var row6 = createElementIn(component,"tr", null);
        var row7 = createElementIn(component,"tr", null);
        var row8 = createElementIn(component,"tr", null);
        var row9 = createElementIn(component,"tr", null);
        var row10 = createElementIn(component,"tr", null);


        var attribs = {"width":"100%"};
        var cell1 = createElementIn(row1,"td", attribs);
        var attribs = {"width":"100%"};
        var cell2 = createElementIn(row2,"td", attribs);
        var attribs = {"width":"100%"};
        var cell3 = createElementIn(row3,"td", attribs);
        var attribs = {"width":"100%"};
        var cell4 = createElementIn(row4,"td", attribs);
        var attribs = {"width":"100%"};
        var cell5 = createElementIn(row5,"td", attribs);
        var attribs = {"width":"100%"};
        var cell6 = createElementIn(row6,"td", attribs);
        var attribs = {"width":"100%"};
        var cell7 = createElementIn(row7,"td", attribs);
        var attribs = {"width":"100%"};
        var cell8 = createElementIn(row8,"td", attribs);
        var attribs = {"width":"100%"};
        var cell9 = createElementIn(row9,"td", attribs);
        var attribs = {"width":"100%"};
        var cell10 = createElementIn(row10,"td", attribs);

        
        var nodeLabelText = document.createTextNode("Title");        
        cell1.appendChild(nodeLabelText);       
        cell1.className = 'panel_label';
        
        var textBox = document.createElement("input");
        textBox.setAttribute("type","text");
        textBox.setAttribute("name","linktitle");
        textBox.setAttribute("value",link.name);
        textBox.setAttribute("maxlength","255");        
        textBox.style.width = '100%';
        cell2.appendChild(textBox);        
        
        var nodeLabelText = document.createTextNode("URL");        
        cell3.appendChild(nodeLabelText);
        cell3.className = 'panel_label';

        var textBox = document.createElement("input");
        textBox.setAttribute("type","text");
        textBox.setAttribute("name","linkurl");
        textBox.setAttribute("value",link.url);
        textBox.setAttribute("maxlength","255");
        textBox.style.width = '100%';
        cell4.appendChild(textBox);
        
        var nodeLabelText = document.createTextNode("Description");        
        cell5.appendChild(nodeLabelText);
        cell5.className = 'panel_label';
        
        var textBox = document.createElement("textarea");
        textBox.setAttribute("name","linkdescription");
        textBox.setAttribute("rows","3");                                
        var nodeLabelText = document.createTextNode(link.description);        
        textBox.appendChild(nodeLabelText);
        textBox.style.width = "100%";
        cell6.appendChild(textBox);      
        cell6.className = 'panel_label';
        
        cell7.appendChild(document.createTextNode("Permission "));
        var perm = createElementIn(cell7,"select",null);
        perm.setAttribute("name","permission");
        cell7.className = 'panel_label';
                
        var attribs = {"value":"public"};
        var option1 = createElementIn(perm,"option",attribs);
        var attribs = {"value":"private"};
        var option2 = createElementIn(perm,"option",attribs);
        option1.appendChild(document.createTextNode("Public"));
        option2.appendChild(document.createTextNode("Private"));
        if (link.permission == 'public')
            option1.selected = true;
        else
            option2.selected = true;
        cell8.className = 'panel_label';
        
        var toolbar = document.createElement("input");
        toolbar.setAttribute("type","checkbox");
        toolbar.setAttribute("name","toolbar");                
        cell7.appendChild(toolbar);        
        cell7.appendChild(document.createTextNode("Show on toolbar"));

        if (link.hasLabel('chipmarktoolbar')){
            getFormItem('editform','toolbar').checked = true;        
        }

        cell8.appendChild(document.createTextNode("   "));
        
        var nodeLabelText = document.createTextNode("New Labels (comma separated)");        
        cell9.appendChild(nodeLabelText);
        cell9.className = 'panel_label';            

        var textBox = document.createElement("input");
        textBox.setAttribute("type","text");
        textBox.setAttribute("name","newlabels");
        textBox.style.width = '100%';
        cell10.appendChild(textBox);       
        initAutosuggestElement(textBox);
        
        var nodeLabelText = document.createTextNode("Existing Labels");        
        right.appendChild(nodeLabelText);
        right.className = 'panel_label';
    
        var attribs = {"name":"labels","size":"15","multiple":"multiple"};
        var selectBox = createElementIn(right,"select",attribs);
        selectBox.style.width = '100%';
        
        for(var i = 0; i < LABELS.length; i++){
            var attribs = {"value":LABELS[i]};
            var optionItem = createElementIn(selectBox,"option",attribs);
            var nodeLabelText = document.createTextNode(LABELS[i]);        
            if (link.hasLabel(LABELS[i]))
                optionItem.selected = true;
            optionItem.appendChild(nodeLabelText);            
        }

        var okButton = document.createElement("input");
        okButton.type = "button";
        okButton.value = "Save";
        okButton.onclick = function(){             
            
            var linkTitle = trim(getFormItem('editform','linktitle').value).substr(0,255);
            var linkURL = trim(getFormItem('editform','linkurl').value).substr(0,255);
            var linkDescription = trim(getFormItem('editform','linkdescription').value).substr(0,65535);
            var linkPermission = getFormItem('editform','permission').value;
            var newLabels = trim(getFormItem('editform','newlabels').value);
            
            var labels = '';
            for (var i = 0; i < LABELS.length; i++)
                if (getFormItem('editform','labels').options[i].selected)
                    labels += ',' + getFormItem('editform','labels').options[i].text;
            
            if (getFormItem('editform','toolbar').checked)
                labels += ',chipmarktoolbar';
            
            if (newLabels.length > 0){
                var labellist = newLabels.split(',');
                for (var i = 0; i < labellist.length; i++){
                    if (trim(labellist[i]).length > 0){
                        labels += ',' + trim(labellist[i]);
                        //addLabel(labellist[i]);                        
                    }
                }
            }        
            
            if (labels.charAt(0) == ',')
                labels = labels.substring(1, labels.length);            

            if (linkTitle.length < 1){
                alert("Please enter a title");
                return;
            }
            if (linkURL.length < 1){
                alert("All chipmarks must have a valid URL.  Please enter a URL.");
                return;
            }

            if (action == 'link_edit'){
                if(updateLink(link.getID(),linkPermission, linkTitle, linkURL, linkDescription, labels)){

                    // remove link from all previous labels
                    link.removeFromLabelTree();

                    link.name = linkTitle;
                    link.url = linkURL;
                    link.description = linkDescription;
                    link.permission = linkPermission;
                    link.tags = new Array();
                    link.addTags(labels.split(','));        
                    var newTags = labels.split(',');
                    // create the new label tree nodes and add this chipmark to them
                    if (link.tags.length > 0){
                        for (var i = 0; i < newTags.length; i++){                        
                            var id = 'label-' + newTags[i].toLowerCase();
                            var added = false;
                            if (!labelExists(newTags[i]) && newTags[i].length > 0){                            
                                // Label doesn't already exist
                                added = addLabel(newTags[i]);
                                if (added){
                                    var node = new treeNode('label', new Array(), 'closed', id, 'label_root', newTags[i], 'normal', null);  
                                    var panel = new panelNode('label_list', id, new Array()); 
                                
                                    labelRoot.addChild(node);
                                    treeNodes[id] = node;            
                                    getPanelNodeByID('label_root').addChild(node);                        
                                    panelNodes[id] = panel;
                                }
                            }
                            else{
                                added = true;
                                var panel = getPanelNodeByID(id); 
                            }                        
                            if (added)
                                if(!panel.hasChild(link.getID())) panel.addChild(link);
                        }
                    }
                    drawTree();
                    action = null;                        
                    showPanel(currentlySelectedFolderID);
                }            
                else{
                    alert("There was an error saving your chipmark");
                }            
            }
            else if (action == 'link_add' && checkAlreadyChipmarked(linkURL)){
                var result = addLink(currentlySelectedFolderID,linkPermission,linkTitle,linkURL,linkDescription,labels);
                 if(result != false){
                    link.setID(parseInt(result));
                    link.name = linkTitle;
                    link.url = linkURL;
                    link.description = linkDescription;
                    link.permission = linkPermission;
                    link.tags = new Array();
                    link.addTags(labels.split(','));
                    var newTags = labels.split(',');
                    if (newTags == "") newTags = new Array;
                    // create the new label tree nodes and add this chipmark to them
                    for (var i = 0; i < newTags.length; i++){                        
                        var id = 'label-' + newTags[i].toLowerCase();
                        var added = false;
                            if (!labelExists(newTags[i]) && newTags[i].length > 0){                            
                                // Label doesn't already exist
                                added = addLabel(newTags[i]);
                                if (added){
                                    var node = new treeNode('label', new Array(), 'closed', id, 'label_root', newTags[i], 'normal', null);  
                                    var panel = new panelNode('label_list', id, new Array()); 
                                
                                    labelRoot.addChild(node);
                                    treeNodes[id] = node;            
                                    getPanelNodeByID('label_root').addChild(node);                        
                                    panelNodes[id] = panel;
                                }
                            }
                            else{
                                added = true;
                                var panel = getPanelNodeByID(id); 
                            }                        
                        if (added)
                            if(!panel.hasChild(link.getID())) panel.addChild(link);
                    }
                    drawTree();
                                        
                    // Add the link to the node and panel arrays
                    var linkPanel = getPanelNodeByID(currentlySelectedFolderID);
                    linkPanel.addChild(link);
                    listLinkNodes[link.getID()] = link;
                    bookmarkCount++;
                    action = null;                        
                    showPanel(currentlySelectedFolderID);
                }            
                else{
                    alert("There was an error adding your chipmark");
                }                   
            }            
        }
        OKCell.appendChild(okButton);
        
        var cancelButton = document.createElement("input");
        cancelButton.type = "button";
        cancelButton.value = "Cancel";
        cancelButton.onclick = function() { action = null; showPanel(currentlySelectedFolderID, true); };
        cancelCell.appendChild(cancelButton);
        
            
    }
    else if (this.type == 'link_send'){
        
        var link = getListLinkNodeByID(currentlySelectedItemID);        
        var panel = createElementIn(container, "div", null);
        panel.className = "action_panel";
        
        var nodeLabelText = document.createTextNode("Send Link");        
        panel.appendChild(nodeLabelText);
        
        // create a table
        var attribs = {"id":"sendform"};
        var form = createElementIn(panel,"form", attribs);
        form.onsubmit = function(){ return false; };
        var attribs = {"width":"95%","border":"0","align":"center"};
        var itemTable = createElementIn(form,"table",attribs);
        var component = createElementIn(itemTable,"tbody", null);
        var row1 = createElementIn(component,"tr", null);
        var row2 = createElementIn(component,"tr", null);
        var attribs = {"colspan":"2","valign":"top"};
        var top = createElementIn(row1,"td", attribs);
        var attribs = {"width":"50%","align":"left"};
        var cancelCell = createElementIn(row2,"td", attribs);
        var attribs = {"width":"50%","align":"right"};
        var OKCell = createElementIn(row2,"td", attribs);

        var nodeLabelText = document.createTextNode("Select buddies to send to:");        
        top.appendChild(nodeLabelText);
        var attribs = {"style":"width:100%;","name":"buddies","size":"15","multiple":"multiple"};
        var selectBox = createElementIn(top,"select",attribs);
        selectBox.style.width = '100%';
        top.className = 'panel_label';
        
        for(var i = 0; i < BUDDIES.length; i++){
            var attribs = {"value":BUDDIES[i].username};
            var optionItem = createElementIn(selectBox,"option",attribs);
            var nodeLabelText = document.createTextNode(BUDDIES[i].username + " (" + BUDDIES[i].nickname + ")");
            optionItem.appendChild(nodeLabelText);
        }

        var okButton = document.createElement("input");
        okButton.type = "button";
        okButton.value = "Send";
        okButton.onclick = function(){
            var buddystring = '';
            for (var i = 0; i < BUDDIES.length; i++)
                if (getFormItem('sendform','buddies').options[i].selected)
                    buddystring += ':' + getFormItem('sendform','buddies').options[i].value;
            if (buddystring.charAt(0) == ':')
                buddystring = buddystring.substring(1, buddystring.length);
            
            if (buddystring.length == 0){
                alert("You must select a buddy");
            }
            else if (sendLink(buddystring,link.name, link.url, link.description)){
                action = null;
                alert ("Your chipmark was sent successfully");
                showPanel(currentlySelectedFolderID);
            }
            else{
                alert("There was an error sending your chipmark");
            }
        }
        OKCell.appendChild(okButton);
 
        var cancelButton = document.createElement("input");
        cancelButton.type = "button";
        cancelButton.value = "Cancel";
        cancelButton.onclick = function() { action = null; showPanel(currentlySelectedFolderID, true); };
        cancelCell.appendChild(cancelButton);
        
    }        
    else if (this.type == 'folder_share'){
        var panel = createElementIn(container,"div",null);
        panel.className = "action_panel";
        
        var nodeLabelText = document.createTextNode("Folder Sharing");
        panel.appendChild(nodeLabelText);
        
            
        // create a table
        var attribs = {"id":"shareform"};
        var form = createElementIn(panel,"form", attribs);
        form.onsubmit = function(){ return false; };
        var attribs = {"width":"95%","border":"0","align":"center"};
        var itemTable = createElementIn(form,"table",attribs);
        var component = createElementIn(itemTable,"tbody", null);
        var row1 = createElementIn(component,"tr", null);
        var row2 = createElementIn(component,"tr", null);
        var attribs = {"width":"40%","valign":"top"};
        var left = createElementIn(row1,"td", attribs);
        var attribs = {"width":"20%","valign":"middle","align":"center","style":"min-width:40px;"};
        var middle = createElementIn(row1,"td", attribs);
        var attribs = {"width":"40%","valign":"top"};
        var right = createElementIn(row1,"td", attribs);
        var attribs = {"width":"40%","align":"left"};
        var cancelCell = createElementIn(row2,"td", attribs);
        var attribs = {"width":"20%","valign":"top","style":"min-width:40px;"};
        var empty = createElementIn(row2,"td", attribs);
        var attribs = {"width":"40%","align":"right"};
        var OKCell = createElementIn(row2,"td", attribs);
        middle.style.minWidth = '40px';
        empty.style.minWidth = '40px';
        
        var attribs = {"type":"button","value":">>"};
        var button = createElementIn(middle,"input",attribs);
        button.style.width = "100%";
        button.style.minWidth = '30px';
        button.onclick = function() {
            var end = getFormItem('shareform','share_buddy_list').length - 1;
            for (var i = end; i >= 0; i--)
                if (getFormItem('shareform','share_buddy_list').options[i].selected == true){                    
                    var movedUser = getFormItem('shareform','share_buddy_list').options[i];                                                            
                    getFormItem('shareform','share_buddy_list').options[i] = null;                                                          
                    var j = getFormItem('shareform','share_invite_list').options.length;
                    getFormItem('shareform','share_invite_list').options[j] = new Option(movedUser.text,movedUser.value);
                }
        }
        var attribs = {"type":"button","value":"<<"};
        var button = createElementIn(middle,"input",attribs);
        button.style.width = "100%";
        button.style.minWidth = '30px';
        button.onclick = function() {
            var end = getFormItem('shareform','share_invite_list').length - 1;
            for (var i = end; i >= 0; i--)
                if (getFormItem('shareform','share_invite_list').options[i].selected == true){                    
                    var movedUser = getFormItem('shareform','share_invite_list').options[i];                    
                    getFormItem('shareform','share_invite_list').options[i] = null;   
                    var j = getFormItem('shareform','share_buddy_list').options.length;
                    getFormItem('shareform','share_buddy_list').options[j] = new Option(movedUser.text,movedUser.value);                       
                }
        }
        
             
        left.appendChild(document.createTextNode("Available"));            
        left.className = 'panel_label';
        var attribs = {"id":"share_buddy_list","name":"share_buddy_list","size":"10","multiple":"multiple"};
        var selectBox = createElementIn(left,"select",attribs);
        selectBox.style.width = '100%';
        var usernames = getTreeNodeByID(currentlySelectedFolderID).users;
        var invites = new Array();
        for(var i = 0; i < BUDDIES.length; i++){
            var invited = false;
            for (var j = 0; j < usernames.length; j++){
                if (BUDDIES[i].username == usernames[j]){
                    invites.push(new buddy(usernames[j],BUDDIES[i].nickname));
                    invited = true;
                }
            }
            if (!invited){
                var attribs = {"value":BUDDIES[i].username};
                var optionItem = createElementIn(selectBox,"option",attribs);
                var nodeLabelText = document.createTextNode(BUDDIES[i].username + ' (' + BUDDIES[i].nickname + ')');        
                optionItem.appendChild(nodeLabelText);            
            }
        }

        right.appendChild(document.createTextNode("Invited")); 
        right.className = 'panel_label';
        var attribs = {"id":"share_invite_list","name":"share_invite_list","size":"10","multiple":"multiple"};        
        var selectBox = createElementIn(right,"select",attribs);
        selectBox.style.width = '100%';
        var users = getTreeNodeByID(currentlySelectedFolderID).users;
        for(var i = 0; i < invites.length; i++){
            var attribs = {"value":invites[i].username};
            var optionItem = createElementIn(selectBox,"option",attribs);
            var nodeLabelText = document.createTextNode(invites[i].username + ' (' + invites[i].nickname + ')');        
            optionItem.appendChild(nodeLabelText);            
        }
  
            var okButton = document.createElement("input");
            okButton.type = "button";
            okButton.value = "Save";
            okButton.onclick = function(){ 
                var newInvites = new Array();
                var unInvites = new Array();
                var newUsers = new Array();
                // check for new users to invite
                for (var i = 0; i < getFormItem('shareform','share_invite_list').length; i++){
                    var inviteUser = true;
                    for(var j = 0; j < invites.length; j++)
                        if (getFormItem('shareform','share_invite_list').options[i].value == invites[j].username)
                            inviteUser = false;
                    if (inviteUser){
                        newInvites.push(getFormItem('shareform','share_invite_list').options[i].value);
                        newUsers.push(getFormItem('shareform','share_invite_list').options[i].value);
                    }
                }
                // check for invites to revoke
                for (var i = 0; i < invites.length; i++){
                    var revokeUser = false;
                    for(var j = 0; j < getFormItem('shareform','share_buddy_list').length; j++)
                        if (getFormItem('shareform','share_buddy_list').options[j].value == invites[i].username)
                            revokeUser = true;
                    if (revokeUser)
                        unInvites.push(invites[i].username);
                    else
                        newUsers.push(invites[i].username);  
                }
            
                var success = true;
                if (newInvites.length > 0){
                    if(!shareFolder(currentlySelectedFolderID,newInvites.join(':'))){
                        alert("There was an error inviting buddies to your folder");
                        success = false;
                    }
                }
                if (unInvites.length > 0){
                    if(!unshareFolder(currentlySelectedFolderID,unInvites.join(':'))){
                        alert("There was an error revoking buddy access to your folder");
                        success = false;
                    }
                }  
                if (success){
                
                    if(newUsers.length > 0){
                        getTreeNodeByID(currentlySelectedFolderID).users = newUsers;                    
                        getTreeNodeByID(currentlySelectedFolderID).flag = 'shared';                        
                    }
                    else {
                        getTreeNodeByID(currentlySelectedFolderID).users = new Array();
                        getTreeNodeByID(currentlySelectedFolderID).flag = 'normal';
                        
                    }                    
                    drawTree();
                    setSelection(currentlySelectedFolderID,'folder');
                }                    

                
            
            }
            OKCell.appendChild(okButton);
            
            var cancelButton = document.createElement("input");
            cancelButton.type = "button";
            cancelButton.value = "Cancel";
            cancelButton.onclick = function() { showPanel(currentlySelectedFolderID, true); };
            cancelCell.appendChild(cancelButton);

    }    
    else if (this.type == 'folder_edit'){
        var panel = createElementIn(container,"div",null);
        panel.className = "action_panel";
            
        if (action == 'folder_edit')
            var nodeLabelText = document.createTextNode("Folder Details");        
        else
            var nodeLabelText = document.createTextNode("Create New Folder");        
        panel.appendChild(nodeLabelText);

        
        // create a table
        var attribs = {"id":"editfolderform"};
        var form = createElementIn(panel,"form", attribs);
        form.onsubmit = function(){ return false; };
        var attribs = {"width":"95%","border":"0","align":"center"};
        var itemTable = createElementIn(form,"table",attribs);
        var component = createElementIn(itemTable,"tbody", null);
        var row1 = createElementIn(component,"tr", null);
        var rowMiddle = createElementIn(component,"tr", null);
        var row2 = createElementIn(component,"tr", null);
        var attribs = {"colspan":"2","valign":"top"};
        var top = createElementIn(row1,"td", attribs);
        var toolbarCell = createElementIn(rowMiddle,"td");
        var attribs = {"width":"50%","align":"left"};
        var cancelCell = createElementIn(row2,"td", attribs);
        var attribs = {"width":"50%","align":"right"};
        var OKCell = createElementIn(row2,"td", attribs);

        var nodeLabelText = document.createTextNode("Folder name:");        
        top.appendChild(nodeLabelText);
        var textBox = document.createElement("input");
        textBox.setAttribute("type","text");
        textBox.setAttribute("name","foldername");
        textBox.setAttribute("maxlength","128");
        textBox.style.width = '100%';
        if (action == 'folder_edit')
            textBox.setAttribute("value",getTreeNodeByID(currentlySelectedFolderID).name);
        top.appendChild(textBox);
        
        if (action == 'folder_edit'){
            if (getTreeNodeByID(currentlySelectedFolderID).type == 'chipmark_root')
                var nodeLabelText = document.createTextNode("The is your root folder");        
            else
                var nodeLabelText = document.createTextNode("The folder exists in " +  getTreeNodeByID(getTreeNodeByID(currentlySelectedFolderID).getParentID()).name);        
        }
        else
            var nodeLabelText = document.createTextNode("The folder will be created in " +  getTreeNodeByID(currentlySelectedFolderID).name);        
        top.appendChild(nodeLabelText);
        top.className = 'panel_label';
        
        var toolbar = document.createElement("input");
        toolbar.setAttribute("type","checkbox");
        toolbar.setAttribute("name","toolbar");
        
        if(getTreeNodeByID(currentlySelectedFolderID).type != 'chipmark_root' || action != 'folder_edit'){
            toolbarCell.appendChild(toolbar);
            toolbarCell.appendChild(document.createTextNode("Show on toolbar"));
            
            if (getTreeNodeByID(currentlySelectedFolderID).toolbarPosition != 0)
                getFormItem('editfolderform','toolbar').checked = true;
        }
        
        if (action != 'folder_edit')
            getFormItem('editfolderform','toolbar').checked = false;
        
        toolbarCell.className = 'panel_label';
        
        var okButton = document.createElement("input");
        okButton.type = "button";
        if (action == 'folder_edit')
            okButton.value = "Edit Folder";
        else
            okButton.value = "Create Folder";
        okButton.onclick = function(){ 
            var folderName = trim(getFormItem('editfolderform','foldername').value);
            var folderToolbarPosition = 0;
            if(getFormItem('editfolderform','toolbar').checked)
                folderToolbarPosition = 1;
                
            if (folderName.length < 1){
                alert("Your folder must have a title");
                return;
            }
            if (action == 'folder_add'){                        
                var result = addFolder(currentlySelectedFolderID, folderName, folderToolbarPosition);
                if(result != false){
                    result = parseInt(result);
                    action = null;
                    // create the list node in parent panel
                    var addToPanel = getPanelNodeByID(currentlySelectedFolderID);
                    var newListNode = new listNode('folder',result,folderName,null,'normal',null,null,currentlySelectedFolderID, folderToolbarPosition);
                    addToPanel.addChild(newListNode);
                    
                    // create the folder tree node in parent folder
                    var addToFolder = getTreeNodeByID(currentlySelectedFolderID);
                    var newFolderNode = new treeNode('local',new Array(),'closed',result,currentlySelectedFolderID,folderName,null,new Array(),
                                                     folderToolbarPosition);               
                    addToFolder.addChild(newFolderNode);

                    // create a new panel for the folder
                    var newPanelNode = new panelNode('folder_list', result, new Array());                    

                    // add to node arrays                    
                    treeNodes[result] = newFolderNode;
                    panelNodes[result] = newPanelNode;
                    listFolderNodes[result] = newListNode;
                                        

                    // Refresh the folder tree and select the new folder
                    folderCount++;
                    drawTree();
                    setSelection(result,'folder');
                }            
                else{
                    alert("There was an unknown error creating your folder");
                }
            }
            else {
                if(renameFolder(currentlySelectedFolderID, folderName, folderToolbarPosition)){
                    action = null;
                    var node = getTreeNodeByID(currentlySelectedFolderID);
                    node.name = folderName;    
                    node.toolbarPosition = folderToolbarPosition;                
                    drawTree();
                    setSelection(currentlySelectedFolderID,'folder');
                }            
                else{
                    alert("There was an unknown error creating your folder");
                }
            
            }                                        
        }
        OKCell.appendChild(okButton);               
 
        var cancelButton = document.createElement("input");
        cancelButton.type = "button";
        cancelButton.value = "Cancel";
        cancelButton.onclick = function() { action = null; showPanel(currentlySelectedFolderID,true); };
        cancelCell.appendChild(cancelButton);

    }            
};






