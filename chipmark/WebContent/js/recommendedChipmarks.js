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
 * Gets recommended chipmarks
 *
 * @param count The number of them to display
 * @param random Should they be randomized?
 *
 * @author Luke Preiner
 */
function getRecommended(count,random){
		  
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, "POST", 'RecommenderServlet', 'agent=ext');
	
    if (result != null){
    	var xml = request.responseXML;
 	    request = null;
 	    var recos = xml.getElementsByTagName("BookmarkList")[0].getElementsByTagName("Bookmark");     	
	    
	    if (!count || count == null)
	        count = recos.length;
	    else if (recos.length < count)
	        count = recos.length;
	
	    var rand = new Array();
	    for (var i = 0; i < count; i++){
            var index = 0;            
	        if (random){                
                var good = false;                
                index = Math.round((Math.random()*(recos.length - 1)));

                // make sure we don't select the same one twice
                while (!good && rand != null){
                    index = Math.round((Math.random()*(recos.length - 1)));
                    var match = false;
                    for(var j = 0; j <= i; j++){
                        if (index == rand[j])
                            match = match || true;
                    }
                    good = !match;
                }

                rand[i] = index;
             }
	         else
	            index = i;
    	        
	        var url = recos[index].getElementsByTagName('URL')[0].firstChild.nodeValue;             
            var linktext = recos[index].getElementsByTagName('Title')[0].firstChild.nodeValue;
            
            document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed;"><tr><td width="16"><img src="images/acorn16.png"/></td><td style="overflow:hidden;text-overflow:ellipsis;"><span class="small_text"><a href="' + url + '" title="' + url + '" target="_blank">' + linktext + '</a></span></td></tr></table>');
	    } 	
        if (count == 0){
            document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed;"><tr><td width="16"><img src="images/acorn16.png"/></td><td style="overflow:hidden;text-overflow:ellipsis;"><span class="small_text">You currently have no recommendations.</span></td></tr></table>');
        }
	
    }
    else{
        document.write('this module is currently unavailable');
    }
}