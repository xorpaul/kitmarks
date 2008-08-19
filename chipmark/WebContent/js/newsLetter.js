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
 * Dient zum Benachrichtigen aller Benutzer per EMail.
 * 
 * 
 */

function sendNewsLetter() {
	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=sendNewsLetterToDistinctUserEmails');

	if (result != null) {
		var xml = request.responseXML;
		// alert("Preferred Language: " +
		// xml.getElementsByTagName("Message")[0].firstChild.nodeValue)
		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			prefLang = null;
			return false;
		}
		if (xml.getElementsByTagName("Message")[0].firstChild) {
			if (xml.getElementsByTagName("Message")[0].firstChild.nodeValue == 'eng')
				prefLang = "eng";
			else
				prefLang = "ger";
		}
		return true;
	}
	prefLang = null;
	return false;
}

function showNewsLetterForm(container) {
	container = document.getElementById(container);

	var box = createElementIn(container, "div", null);
	box.className = 'graybox'
	box.style.width = '100%';
	box.style.minWidth = '100%';

	var attribs = {
		"width" :"100%;",
		"align" :"center",
		"border" :"0"
	};
	var itemTable = createElementIn(box, "table", attribs);
	var component = createElementIn(itemTable, "tbody", null);
	var row = createElementIn(component, "tr", null);

	var attribs = {
		"align" :"left"
	};
	var td = createElementIn(row, "td", attribs);

	var attribs = {
		"align" :"right",
		"width" :"20"
	};
	var td2 = createElementIn(row, "td", attribs);
	td.appendChild(document.createTextNode("Newsletter: "));
	var td3 = createElementIn(row, "td", attribs);

	var attribs = {
		"align" :"right",
		"width" :"20"
	};
	var textAreaTR = createElementIn(component, "tr", null);
	var textAreaTD = createElementIn(textAreaTR, "td", attribs);
	var textArea = document.createElement("textarea");
	textArea.cols = "50";
	textArea.rows = "30"

	var attribs = {
		"align" :"right",
		"id" :"status2"
	};

	var sendButtonTR = createElementIn(component, "tr", null);
	var sendButtonTD = createElementIn(sendButtonTR, "td", attribs);

	var status = createElementIn(sendButtonTD, "span", attribs);

	var sendButton = document.createElement("input");
	sendButton.type = "button";
	sendButton.value = "Newsletter versenden";
	sendButton.onclick = function() {
		sendNewsLetter();
	}
	textAreaTD.appendChild(textArea);
	sendButtonTD.appendChild(sendButton);

}
