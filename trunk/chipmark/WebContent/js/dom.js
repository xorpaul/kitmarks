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


function createElementIn(parent, tag_name, attributes){            
    var element = document.createElement(tag_name);
    if (attributes != null)
        for(attrib in attributes)
            element.setAttribute(attrib,attributes[attrib]);    

    parent.appendChild(element);
    return element;
}


function setElementText(element, message){
    var t_element = document.getElementById(element);
    if (!(t_element == null || t_element == 'undefined')){
        element = t_element;
    }
        
        
    var oldText = element.firstChild;
    var newText = document.createTextNode(unescape(message));
    if (oldText == null || oldText == 'undefined')
        element.appendChild(newText);
    else
        element.replaceChild(newText, oldText);
}

function replaceElementContents(element, contents){
    var t_element = document.getElementById(element);
    if (!(t_element == null || t_element == 'undefined')){
        element = t_element;
    }
    
    while (element.firstChild)
        element.removeChild(element.firstChild);
    
    element.appendChild(contents);
}

function removeElementContents(element){
   var t_element = document.getElementById(element);
    if (!(t_element == null || t_element == 'undefined')){
        element = t_element;
    }
    
    while (element.firstChild)
        element.removeChild(element.firstChild);
}
