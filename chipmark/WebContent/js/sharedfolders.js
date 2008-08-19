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
 * Share a folder 
 *
 * @param folderID The DBID of the folder to share
 * @param users A colon delimited string of the usernames to invite 
 *
 * @author Luke Preiner
 */
function shareFolder(folderID, users) {
	
	var userlist = users.split(':');
	for (var i = 0; i < userlist.length; i++)
	    userlist[i] = "username=" + encodeURIComponent(userlist[i]);
	
	users = userlist.join('&');
	
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'ShareFolder','agent=ext&action=invite&folderid=' + encodeURIComponent(folderID) + '&' + users);
	
    if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;
	
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE'){
			if (xml.getElementsByTagName("Message")[0].firstChild){
      	        alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
            } 
            return false;
		}        
        return true;      
	}
	return false;
}


/**
 * Unshare a folder 
 *
 * @param folderID The DBID of the folder to share
 * @param users A colon delimited string of the usernames to revoke
 *
 * @author Luke Preiner
 */
function unshareFolder(folderID, users) {
	
	var userlist = users.split(':');
	for (var i = 0; i < userlist.length; i++)
	    userlist[i] = "username=" + encodeURIComponent(userlist[i]);
	
	users = userlist.join('&');
	
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'ShareFolder','agent=ext&action=revoke&folderid=' + encodeURIComponent(folderID) + '&' + users);
	
    if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;
	
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE'){
			if (xml.getElementsByTagName("Message")[0].firstChild){
      	        alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
            } 
            return false;
		}        
        return true;      
	}
	return false;
}

////
// Display invites if there are requests
//
// @author Luke Preiner
//
function showFolderInvites(){
    
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'ShareFolder','agent=ext&action=listinvites');
    if (result != null){
	    var xml = request.responseXML;
        request = null;
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue != 'FAILURE'){
        
            if(xml.getElementsByTagName("folder").length > 0){
                alertsFolderCount = xml.getElementsByTagName("folder").length;
                document.write('<div id="folderapproval_alert" width="100%" style="background-color:#FAFAFA; border:1px dotted black;">');
                document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">');
                document.write('<tr><td><span style="color:black;font-weight:bold;font-size:14px;">&nbsp;You have new folder invites:</span><br/><br/></td></tr>');
                document.write('<tr><td><table width="80%" cellpadding="0" border="0" cellspacing="0" align="center"><tr><td align="left"><span style="color:black;font-weight:bold;font-size:12px;">Username / Folder</span></td><td align="right"><span style="color:black;font-weight:bold;font-size:12px;">Your Response</span></td></tr></table></td></tr>');            

                for (var i = 0; i < xml.getElementsByTagName("folder").length; i++){
                    var ownerName = xml.getElementsByTagName("folder")[i].getElementsByTagName("ownerName")[0].firstChild.nodeValue;
                    var folderID = parseInt(xml.getElementsByTagName("folder")[i].getElementsByTagName("id")[0].firstChild.nodeValue);
                    var folderName = xml.getElementsByTagName("folder")[i].getElementsByTagName("name")[0].firstChild.nodeValue;
                    var quotedname = "'" + ownerName + "'";
                    var approveStr = '<a href="javascript:acceptInvite(' + folderID + ')">accept</a>';
                    var denyStr = '<a href="javascript:rejectInvite(' + folderID + ')">reject</a>';            
                    
                    document.write('<tr><td align="center">');
                    document.write('<div id="folderinvites_'+folderID+'_div">');
                    document.write('<table width="80%" cellpadding="0" border="0" cellspacing="0" align="center"><tr>');
                    document.write('<td align="left"><span style="color:black;font-weight:normal;font-size:12px;">' + ownerName + ' / ' + folderName + '</span></td>');
                    document.write('<td align="right"><span style="color:black;font-weight:normal;font-size:12px;">'+approveStr+' &nbsp; &nbsp; &nbsp; '+denyStr+'</span></td>');
                    document.write('</td></tr></table>');
                    document.write('</div>');
                }
                document.write('</table><br/><br/>');    
                document.write('</div>');    
            }
        }
    }
}


function acceptInvite(folderID){
  
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'ShareFolder','agent=ext&action=accept&folderid='+encodeURIComponent(folderID));	
	if (result != null){
	    var xml = request.responseXML;    
        // we can ditch the request object now
        request = null;
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE'){
    			if (xml.getElementsByTagName("Message")[0].firstChild){
          			alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
          		}
          		else{
          		    alert("An error occured while attempting to accept the invite.  Please try again");
          		}
		}   
		else{
		    toggleDivDisplayById("folderinvites_"+folderID+"_div",false); 
		    alertFolderCount--;
		    if (alertFolderCount < 1){
		        toggleDivDisplayById("folderapproval_alert",false); 
		        toggleDivDisplayById("invite_alert",false); 
 		    } 
		}
	}
}


function rejectInvite(folderID){
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'ShareFolder','agent=ext&action=reject&folderid='+encodeURIComponent(folderID));	
	if (result != null){
	    var xml = request.responseXML;    
        // we can ditch the request object now
        request = null;
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE'){
    			if (xml.getElementsByTagName("Message")[0].firstChild){
          			alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
          		}
          		else{
          		    alert("An error occured while attempting to reject the invite.  Please try again");
          		}
		}   
		else{
		    toggleDivDisplayById("folderinvites_"+folderID+"_div",false); 
		    alertFolderCount--;
		    if (alertFolderCount < 1){
		        toggleDivDisplayById("folderapproval_alert",false);
		        toggleDivDisplayById("invite_alert",false);  
 		    } 
		}
	}

}