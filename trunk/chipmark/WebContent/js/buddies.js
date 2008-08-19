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


// This stores the list of buddies
var BUDDIES = new Array();

// The buddy object
function buddy (username, nickname){
    this.username = username;
    this.nickname = nickname;
}

var buddy_request_list = new Array();
var buddy_request_count = 0;

/**
 * Retrieve the list of buddies
 *
 * @author Luke Preiner
 */
function getBuddyList() {
	
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'BuddyList','action=display&agent=ext');
	
    if (result != null){
	    var xml = request.responseXML;
        BUDDIES = new Array();
        // we can ditch the request object now
        request = null;
	
		if (xml.getElementsByTagName("result")[0].textContent == 'FAILURE'){
			if (xml.getElementsByTagName("message")[0].firstChild){
      	        alert(xml.getElementsByTagName("message")[0].firstChild.nodeValue);
            } 
            return false;
		}        

        var buddies = xml.getElementsByTagName("buddy");
        
        for (var j = 0; j < buddies.length; j++){
            var bName = '';
            var bUsername = buddies[j].getElementsByTagName('username')[0].firstChild.nodeValue;
            if (buddies[j].getElementsByTagName('nickname')[0].firstChild == null)
                bName = bUsername;
            else
                bName = buddies[j].getElementsByTagName('nickname')[0].firstChild.nodeValue;
    
            BUDDIES.push(new buddy(bUsername,bName));    
        }        
        return true;      
	}
	return false;
}



function getBuddyRequests(){
	  // Check the server for buddy requests
    
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'BuddyList','action=getBuddyRequests');
	
	if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;
	
		var buddies = xml.getElementsByTagName("buddy");
        buddy_request_count = buddies.length;
		for (var j = 0; j < buddies.length; j++){	
		    var bUsername = buddies[j].getElementsByTagName('username')[0].firstChild.nodeValue; 
        	buddy_request_list.push(bUsername)
        }	       
		
        return true;      
	}
	return false;
	
}	






////
// Display request list if there are requests
//
// @author Luke Preiner
//
function showBuddyRequests(refreshFlag){
    refreshPage = refreshFlag;
    getBuddyRequests()
    if (buddy_request_list.length > 0){
        document.write('<div id="buddyapproval_alert" width="100%" style="background-color:#FAFAFA; border:1px dotted black;">');
        document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">');
        document.write('<tr><td><span style="color:black;font-weight:bold;font-size:14px;">&nbsp;You have new buddy requests:</span><br/><br/></td></tr>');
        document.write('<tr><td><table width="80%" cellpadding="0" border="0" cellspacing="0" align="center"><tr><td align="left"><span style="color:black;font-weight:bold;font-size:12px;">From Username</span></td><td align="right"><span style="color:black;font-weight:bold;font-size:12px;">Your Response</span></td></tr></table></td></tr>');

        for (var i = 0; i<buddy_request_list.length;i++){
            var quotedname = "'" + buddy_request_list[i] + "'";
            var approveStr = '<a href="javascript:approveBuddy(' + quotedname + ')">approve</a>';
            var denyStr = '<a href="javascript:denyBuddy(' + quotedname + ')">deny</a>';            

            
            document.write('<tr><td align="center">');
            document.write('<div id="buddyapproval_'+buddy_request_list[i]+'_div">');
            document.write('<table width="80%" cellpadding="0" border="0" cellspacing="0" align="center"><tr>');
            document.write('<td align="left"><span style="color:black;font-weight:normal;font-size:12px;">'+buddy_request_list[i]+'</span></td>');
            document.write('<td align="right"><span style="color:black;font-weight:normal;font-size:12px;">'+approveStr+' &nbsp; &nbsp; &nbsp; '+denyStr+'</span></td>');
            document.write('</td></tr></table>');
            document.write('</div>');
        }
        document.write('</table><br/><br/>');    
        document.write('</div>');    
    }
 

}


////
// Approves a buddy request
//
// @author Luke Preiner
// 
function approveBuddy(name){
     
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'BuddyList','action=setBuddyRequests&UserName='+name+"&Accepted=1");

	
	if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;

		if (xml.getElementsByTagName("result")[0].firstChild.nodeValue == 'FAILURE'){
    			if (xml.getElementsByTagName("Message")[0].firstChild){
          			alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
          		}
		}
		else{
		    toggleDivDisplayById("buddyapproval_"+name+"_div",false);   
		    alertBuddyCount--;
		    if (alertBuddyCount < 1){
		        toggleDivDisplayById("buddyapproval_alert",false); 
		        toggleDivDisplayById("buddy_alert",false); 
 		    } 

            if (refreshPage > 0){
                document.location = document.location; 
            }

		}
	}
}


////
// Deny a buddy request
//
// @author Luke Preiner
//
function denyBuddy(name){
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'BuddyList','action=setBuddyRequests&UserName='+name+"&Accepted=0");

	
	if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;
 
		if (xml.getElementsByTagName("result")[0].firstChild.nodeValue == 'FAILURE'){
    			if (xml.getElementsByTagName("Message")[0].firstChild){
                    alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
          		}
		}
		else{
		    toggleDivDisplayById("buddyapproval_"+name+"_div",false);  
            alertBuddyCount--;
		    if (alertBuddyCount < 1){
		        toggleDivDisplayById("buddyapproval_alert",false); 
		        toggleDivDisplayById("buddy_alert",false); 
		        document.location = document.location;
 		    } 		    
		}
	}

}


function deleteBuddy(buddyName){
    if (confirm("Are you sure you want to delete this buddy?")){
	
        if (removeBuddy(buddyName)){        
            setBuddyMessage(buddyName + " has been removed from your buddy list");
            document.location = document.location;
        }
    }
}



/*
 * remove a buddy
 */
function removeBuddy(buddyName){	
	
	var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'BuddyList','action=remove&name=' + encodeURIComponent(buddyName));

	
	if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;
 
	
		if (xml.getElementsByTagName("status")[0].firstChild.nodeValue == 'FAILURE'){
			if (xml.getElementsByTagName("message")[0].firstChild){
				setBuddyMessage(reqb.responseXML.getElementsByTagName("message")[0].firstChild.nodeValue);
				return false;
			} 
			else {
                setBuddyMessage("There was an unknown error removing that buddy");
				return false;
			}
		} 
		else {			
            getBuddyList();
            return true; 
		}
	}
};



function addBuddy(){
    toggleDivDisplayById("addbuddy_div",false);

    var bReload = false;
    var theForm = document.addForm    
    
    if(theForm.buddyUser.value == ''){
        alert("Username can not be blank");
    }
    else{        
          var request = getXMLHTTPObject();
       var result = execXMLQuery(request, 'POST', 'BuddyList','action=add&name='  + encodeURIComponent(theForm.buddyUser.value) + '&nick=' + encodeURIComponent(theForm.buddyNick.value));
        
        if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;
            if (xml.getElementsByTagName("status")[0].firstChild.nodeValue == 'FAILURE'){
                if (xml.getElementsByTagName("message")[0].firstChild){
                    setBuddyMessage(xml.getElementsByTagName("message")[0].firstChild.nodeValue);
                } 
                else {
                    window.location.reload();
                }
            }
            else {
                setBuddyMessage("A request has been sent to your buddy.  You will not be able to send, receive or view chipmarks from your buddy until the request is approved.");
            }
        }
    }
    
    toggleDivDisplayById("buddylist_buttons",true);
}
	
function editBuddy(){
    toggleDivDisplayById("editbuddy_div",false);
    
    var bReload = false;
    var theForm = document.editForm    
    
      var request = getXMLHTTPObject();
       var result = execXMLQuery(request, 'POST', 'BuddyList','action=edit&name='+ encodeURIComponent(theForm.buddyUser.value) + '&nick=' + encodeURIComponent(theForm.buddyNick.value));
        
        if (result != null){
	        var xml = request.responseXML;
        
            // we can ditch the request object now
            request = null;

            if (xml.getElementsByTagName("status")[0].firstChild.nodeValue == 'FAILURE'){
                if (xml.getElementsByTagName("message")[0].firstChild){
                    setBuddyMessage(xml.getElementsByTagName("message")[0].firstChild.nodeValue);
                } 
                else {
                    window.location.reload();
                }
            }
            else {
                setBuddyMessage("Your buddy information has been modified.");
                window.location.reload();
            }
        }

    
    toggleDivDisplayById("buddylist_buttons",true);
}


