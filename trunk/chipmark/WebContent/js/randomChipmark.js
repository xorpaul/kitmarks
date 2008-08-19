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


/* 
 * getRandomChipmark
 *
 * @param div_id The ID of the div to place the link into
 * @param linktext The display text for the link
 *
 * @author Luke Preiner
 */
function getRandomChipmark(div_id, linktext){
	  
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, "GET", "Random");
	
    if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;

 	    var random = xml.getElementsByTagName("BookmarkList")[0].getElementsByTagName("Bookmark");    
     	
	    var url = random[0].getElementsByTagName('URL')[0].firstChild.nodeValue; 
        var title = random[0].getElementsByTagName('Title')[0].firstChild.nodeValue; 		

        
        if (linktext == null){
            linktext = title;
        }
        
        var div = document.getElementById(div_id);
        if (div_id != null && div != 'undefined'){    

            // this should really be converted to use DOM methods
            div.innerHTML='<a href="' + url + '" title="' + url + '" target="_blank">' + linktext + '</a>';
        }
        else{
            document.write('RC: target div not found<br/>');
            document.write('<a href="' + url + '" title="' + url + '">' + linktext + '</a>');
        }
    } 
    else {
        document.write("This module is currently unavailable");
    }
}