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

var alertFolderCount = 0;
var alertBuddyCount = 0;
var alertInboxCount = 0;

function getBuddyAlert(containerDiv, textDiv){
    var dest = document.getElementById(textDiv);
    var cont = document.getElementById(containerDiv);
    alertBuddyCount = getBuddyAlertCount();
    if (alertBuddyCount > 0){
        setElementText(dest,alertBuddyCount + ' new buddy request(s)');
        toggleDivDisplay(cont,true);
    }
}


function getInboxAlert(containerDiv, textDiv){
    var dest = document.getElementById(textDiv);
    var cont = document.getElementById(containerDiv);
    alertInboxCount = getInboxAlertCount();
    if (alertInboxCount > 0){
        setElementText(dest,alertInboxCount + ' received chipmark(s)');
        toggleDivDisplay(cont,true);
    }
}

function getInviteAlert(containerDiv, textDiv){
    var dest = document.getElementById(textDiv);
    var cont = document.getElementById(containerDiv);
    alertFolderCount = getInviteAlertCount();
    if (alertFolderCount > 0){
        setElementText(dest,alertFolderCount + ' folder invitations(s)');
        toggleDivDisplay(cont,true);
    }
}


/**
 * Gets the buddy request count 
 *
 * @author Luke Preiner
 */
function getBuddyAlertCount(){
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'BuddyList','agent=ext&action=getBuddyRequests');
	if (result != null){
	    var xml = request.responseXML;
        request = null;
        return xml.getElementsByTagName("buddy").length;
	}
    return 0;
}	


/**
 * Gets the invite count 
 *
 * @author Luke Preiner
 */
function getInviteAlertCount() {	
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'ShareFolder','agent=ext&action=listinvites');
    if (result != null){
	    var xml = request.responseXML;
        request = null;
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue != 'FAILURE'){
		    return xml.getElementsByTagName("folder").length;      
		}   
	}
	return 0;
}


/**
 * Gets the new inbox item count 
 * (not implemented yet)
 * @author Luke Preiner
 */
function getInboxAlertCount(){
    return 0;
}

