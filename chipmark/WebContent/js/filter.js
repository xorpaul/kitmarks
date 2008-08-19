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

var filterState = null;

function getFilterState(){    	
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'NodeAction','action=getFilter');
	
    if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;
	
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE'){
			if (xml.getElementsByTagName("Message")[0].firstChild){
      	        alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
            } 
            filterState = null;
            return false;
		}        
        if (xml.getElementsByTagName("Message")[0].firstChild){
            if (xml.getElementsByTagName("Message")[0].firstChild.nodeValue == 'true')
                filterState = true;
            else
                filterState = false;
        }         
        return true;
	}
	filterState = null;
	return false;
}


function setFilterState(newFilterState){    	    
    newFilterState = false || newFilterState;
    
    var request = getXMLHTTPObject();
    var result = execXMLQuery(request, 'POST', 'NodeAction','action=setFilter&filter=' + newFilterState);
	
    if (result != null){
	    var xml = request.responseXML;
    
        // we can ditch the request object now
        request = null;
	
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE'){
			if (xml.getElementsByTagName("Message")[0].firstChild){
      	        alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
            } 
            filterState = null;
            return false;
		}        
        filterState = newFilterState;
        return true;
	}
	filterState = null;
	return false;
}



function showFilter(container){    
    container = document.getElementById(container);
    
    var success = getFilterState();
        
   
    var box = createElementIn(container, "div", null);
    box.className = 'graybox'
    box.style.width = '100%';
    box.style.minWidth = '100%';
    

    var attribs = {"width":"100%;","align":"center"};
    var itemTable = createElementIn(box,"table",attribs);
    var component = createElementIn(itemTable,"tbody", null);
    var row = createElementIn(component,"tr", null);
    var attribs = {"align":"left"};
    var statusCell = createElementIn(row,"td", attribs);
    var attribs = {"align":"right","width":"50"};
    var toggleCell = createElementIn(row,"td", attribs);
    
    statusCell.appendChild(document.createTextNode("Content filter is currently: ")); 
    var attribs = {"id":"status"};
    var status = createElementIn(statusCell,"span",attribs);
    
    updateFilterStatus();
    
    var toggleButton = document.createElement("input");
    toggleButton.type = "button";
    toggleButton.value = "Toggle";
    toggleButton.onclick = function(){
        if (filterState != true)
            setFilterState(true);        
        else
            setFilterState(false)                    
        updateFilterStatus();
    }
    toggleCell.appendChild(toggleButton);
    
}


function updateFilterStatus(){
    var statusspan = document.getElementById('status');
    if (filterState == true){
        statusspan.style.color = 'green';
        setElementText("status","ON");
    }    
    else{
        statusspan.style.color = 'red';
        if (filterState == false)
            setElementText("status","OFF");
        else
            setElementText("status","UNKNOWN");
    }

}

