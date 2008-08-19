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

function getXMLHTTPObject() {
    var xmlhttpobj;

	if(typeof window.ActiveXObject != 'undefined'){
		xmlhttpobj = new ActiveXObject("Microsoft.XMLHTTP");
	}else{
		xmlhttpobj = new XMLHttpRequest();		
	}
	
	return xmlhttpobj;
}


function execXMLQuery(reqObj, method, reqString, data, async, callback){

    data = data || null;
    
    if (method.toUpperCase() != 'POST'){
        method = 'GET';
    }
    
    if (data == null && method.toUpperCase() != 'POST' ){
        var rando = (Math.round((Math.random()*9999)+1));
	    reqString += '?seed=' + rando;
	}
	
    if(reqObj != null){
        try {            
            if (async != true || callback == null || callback == 'undefined'){
                async = false;
            }
            else{
                if(typeof window.ActiveXObject != 'undefined'){
		            reqObj.onReadyStateChange = callback;
	            }else{
                    // If we're not using IE, use onLoad event instead
		            reqObj.onload = callback;
	            }
            }                
	        reqObj.open(method, reqString, async);
            reqObj.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			reqObj.send(data);
        } catch (e) {	        
			return null;
        }

		if (reqObj.readyState == 4 && async == false){
			if (reqObj.status == 200){
				if(reqObj.responseXML == null){
					return null;
				} else {
					return reqObj.responseXML;
				}
			}
			else {
				return null;
			}
		}


	}

}


function reverseContentFiltering(switchTo) {

	var request = "/NodeAction?action=setFilter&filter=";

	if( switchTo ) {
	
		request = request + "true";
	
	} else {
	
		request = request + "false";
	
	}
	
	var xml = execXMLQuery( getXMLHTTPObject() , 'POST' , request, "" );

	window.location.reload( true );
	
}