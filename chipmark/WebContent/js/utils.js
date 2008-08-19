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

var INT_MAX = 4000000000;

/**
 * Replace special HTML characters with their encoded values
 * Note: & must be done first since the encoded values of all
 * contain an amperstand
 *
 * @param str string to be safely encoded
 * @author Scott Yilek
 */
function htmlspecialchars(str) {
	theStr = str;
	theStr = theStr.replace(/&/g, "&amp;");
	theStr = theStr.replace(/</g, "&lt;");
	theStr = theStr.replace(/>/g, "&gt;");	
	theStr = theStr.replace(/\"/g, "&quot;");
	theStr = theStr.replace(/\'/g, "&#039;");        
	return theStr;
}



/**
 * This function will not return until (at least)
 * the specified number of milliseconds have passed.
 * It does a busy-wait loop.
 */
function pause(numberMillis) {
	var now = new Date();
	var exitTime = now.getTime() + numberMillis;
	while (true) {
		now = new Date();
		if (now.getTime() > exitTime)
			return;
	}
}

// Trims spaces from a string
function trim (string) {
	return string.replace(/^\s+|\s+$/g,"");
}


// convertToHex
// Convert an int to a hex string
function convertToHex(i) {
	if(i < 0 || i > INT_MAX) {
		return "00000000";
	}
	else {	
		var s = i.toString(16);
		while(s.length < 8) {
			s = "0" + s;
		}
		return s;
	}
}

function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		curleft = obj.offsetLeft
		curtop = obj.offsetTop
		while (obj = obj.offsetParent) {
			curleft += obj.offsetLeft
			curtop += obj.offsetTop
		}
	}
	return [curleft,curtop];
}


/**
 * If a protocol is not in the url, add it to the front
 * @param protocol The protocol to prepend to the URI
 * @param uri The URI
 * @return URI with the specified protocol prepended
 * @author Luke Preiner
 */
function addProtocol(uri, protocol) {
	if (!protocol.length || protocol == null)
	    protocol = 'http://';

	if (uri.indexOf('://') == -1) {
		return protocol + uri;
	}
	else {
		return uri;
	}
}




/**
 * Toggle div visiblity or set explicitly
 * @param divID - div id to toggle/set
 * @param explicit - value if setting explicitly (boolean)
 * @author Luke Preiner
 */
function toggleDivDisplayById(divId, explicit, type){
    var d = document.getElementById(divId);
    
    if(!type)
        var displayType = "inline";
    else
        var displayType = type;    
        
    if(explicit == true){
        d.style.display = displayType;
    }
    else if(explicit == false){
        d.style.display = "none";
    }    
    else{        
        if(d.style.display == "none"){
            d.style.display = displayType;            
        }else{
            d.style.display = "none";
        }
    }
}



/**
 * Toggle div visiblity or set explicitly
 * @param divID - div
 * @param explicit - value if setting explicitly (boolean)
 * @author Luke Preiner
 */
function toggleDivDisplay(div, explicit){
    var d = div;
        
    if(explicit == true){
        d.style.display = "inline";
    }
    else if(explicit == false){
        d.style.display = "none";
    }    
    else{        
        if(d.style.display == "none"){
            d.style.display = "inline";            
        }else{
            d.style.display = "none";
        }
    }
}


function getFormItem(formID, fieldName){
    var form = document.getElementById(formID);
    if (form) {
        for (var i = 0; i < form.length; i++){
            if (form[i].name == fieldName)
                return form[i];
        }
    }
}


function existsInArray(item, array){
    for(var i = 0; i < array.length; i++)
        if (array[i] == item)
            return true;
    return false;
}



function disableTextSelection(element) {
    if (element) {
        element.unselectable = "on";
        element.style.MozUserSelect = "none";    
        element.onselectstart = function() { return false; };
    }
}


// Retrieves all non-function elements
// of a generic object.  This can be used
// to use an associative array or object
// like a normal array (with the length
// property.
// Usage: object.Elemements();
// Usage: object.Elemenets().length;
Object.prototype.Elements = function (type){
	var results = new Array();
	if (!type || type == null){
		for (var x in this){
			if (typeof(this[x]) != 'function')
				results.push(this[x]);
		}
	}
	else{
		for (var x in this)
			if (typeof(this[x]) == type)
				results.push(this[x]);
	}
	return results;
}