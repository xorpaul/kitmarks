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


var refreshPage = 0;


function initBuddies(){
    showBuddyList();    
    document.write('<div align="center" id="buddylist_buttons" width="95%" style="text-align:center;">&nbsp;</div>');
    
    document.write('<div id="addbuddy_div" style="display:none;">');
    document.write('<br/><table class="graybox" width="100%" cellspacing="0" cellpadding="0" align="center"><tr><td align="center">');
    document.write('<form onsubmit="addBuddy()" name="addForm" action="javascript: //">');
	document.write('Username:<input name="buddyUser" type="text onfocus=userBox.blur()" maxLength="32"><br/>');
	document.write('Nickname:<input name="buddyNick" type="text" maxLength="32"><br/><br/>');
    document.write('<input name="Submit" type="submit" value="Send Buddy Request">');
	document.write('</form>');
    document.write('</td></tr></table>');
	document.write('</div>');

    document.write('<div id="editbuddy_div" style="display:none;">');
    document.write('<br/><table class="graybox" width="100%" cellspacing="0" cellpadding="0" align="center"><tr><td align="center">');
    document.write('<form onsubmit="editBuddy()" name="editForm" action="javascript: //">');
	document.write('Username:<input name="buddyUser" type="text onfocus=userBox.blur()" maxLength="32" disabled><br/>');
	document.write('Nickname:<input name="buddyNick" type="text" maxLength="32"><br/><br/>');
    document.write('<input name="Submit" type="submit" value="Edit Buddy">');
	document.write('</form>');
    document.write('</td></tr></table>');
	document.write('</div>');

    addButton("buddylist_buttons","add a buddy",showAddBuddyForm);   
}


////
// Display buddy list if there are buddies
//
// @author Luke Preiner
//
function showBuddyList(strMessage){

    strMessage = strMessage || null;

    getBuddyList()
    document.write('<br/><div id="buddylist_div" width="100%" class="graybox">');
    document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">');
    document.write('<tr><td><div id="buddycount"></div><br/><br/></td></tr>');
    document.write('<tr><td align="center"><div id="buddymessage" style="display:none;"></div></td></tr>');

    document.write('<tr><td><div id="buddyheadings"><table width="80%" cellpadding="0" border="0" cellspacing="0" align="center"><tr><td align="left"><span style="color:black;font-weight:bold;font-size:12px;">Username (nickname)</span></td><td align="right"><span style="color:black;font-weight:bold;font-size:12px;">Available Actions</span></td></tr></table></div></td></tr>');

    if (BUDDIES.length > 0){
        for (var i = 0; i < BUDDIES.length; i++){
            var quotedname = "'" + BUDDIES[i].username + "'";
            var editStr = '<a href="javascript:showEditBuddyForm(' + quotedname + ')">edit</a>';
            var deleteStr = '<a href="javascript:deleteBuddy(' + quotedname + ')">delete</a>';    
            
            document.write('<tr><td align="center">');
            document.write('<div id="buddylist_'+BUDDIES[i].username+'_div">');
            document.write('<table width="80%" cellpadding="0" border="0" cellspacing="0" align="center"><tr>');
            document.write('<td align="left"><span style="color:black;font-weight:normal;font-size:12px;">'+BUDDIES[i].username+' ('+BUDDIES[i].nickname+')</span></td>');
            document.write('<td align="right"><span style="color:black;font-weight:normal;font-size:12px;">'+editStr+' &nbsp; &nbsp; &nbsp; '+deleteStr+'</span></td>');
            document.write('</td></tr></table>');
            document.write('</div>');
        }
    }    
    document.write('</table><br/><br/>');    
    document.write('</div>'); 

    updateBuddyCount();    
    if (strMessage != null)
        setBuddyMessage(strMessage);
}



function addButton(divIDtoAddTo, name, onclick_action){
    buttonDiv = document.getElementById(divIDtoAddTo);
    var addBuddyBut = document.createElement("input");
	addBuddyBut.setAttribute("className","buttons");
	addBuddyBut.setAttribute("type","button");
	addBuddyBut.setAttribute("value",name);
	addBuddyBut.onclick = onclick_action;
	buttonDiv.appendChild(addBuddyBut);
}


////
// Update and show buddy status message
//
// @author Luke Preiner
//
function setBuddyMessage(messageStr){
    var message = document.getElementById("buddymessage");
    message.innerHTML = '<span style="color:red;font-size:14px;">'+messageStr+'</span><br/><br/>';
    toggleDivDisplayById("buddymessage",true);
}


function updateBuddyCount(){
    var countDiv = document.getElementById("buddycount");
    var buddyStr = "budd";
    if (BUDDIES.length == 0)
        buddyStr += "ies";
    else if (BUDDIES.length == 1)
        buddyStr += "y:";
    else
        buddyStr += "ies:";

    if (BUDDIES.length == 0){
        toggleDivDisplayById("buddyheadings",false);
    }
    countDiv.innerHTML = '<span style="color:black;font-weight:bold;font-size:14px;">&nbsp;You have '+ BUDDIES.length +' '+buddyStr+'</span>';
}



function showAddBuddyForm(){
    toggleDivDisplayById("buddylist_buttons",false);
    document.addForm.buddyUser.value = "";
    document.addForm.buddyNick.value = "";    
    toggleDivDisplayById("addbuddy_div",true);
}

function showEditBuddyForm(buddyName){
    toggleDivDisplayById("buddylist_buttons",false);
    toggleDivDisplayById("addbuddy_div",false);
    
    document.editForm.buddyUser.value = buddyName;
    for (var i = 0; i < BUDDIES.length; i++){
        if (buddyName == BUDDIES[i].username){
            var nick = BUDDIES[i].nickname;            
            break;
        }
    }
    document.editForm.buddyNick.value = nick;    
    toggleDivDisplayById("editbuddy_div",true);
}
