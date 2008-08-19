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
 * Dient zum Umschalten der Sprache der Webseite.
 * 
 * Diese JavaScript Funktionen werden nur dazu benutzt mit der Datenbank zu agieren.
 * 
 * Es wird nicht die Session umgesetzt.
 * 
 */

var prefLang = null;

function getPrefLang() {
	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=getPrefLang');

//	if (result == null)
//		alert("Error: Result von action=getPrefLang ist null!")

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

function setPrefLang(newPrefLang) {

	// alert(newPrefLang);
	newPrefLang = false || newPrefLang;

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=setPrefLang&prefLang=' + newPrefLang);

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			prefLang = null;
			return false;
		}
		prefLang = newPrefLang;
		return true;
	}
	prefLang = null;
	return false;
}

function showPrefLang(container) {
	container = document.getElementById(container);

	var success = getPrefLang();

	// alert("Success: " + success);

	var box = createElementIn(container, "div", null);
	box.className = 'graybox'
	box.style.width = '80%';
	box.style.minWidth = '75%';

	// alert(prefLang);

	var attribs = {
		"width" :"80%;",
		"align" :"center"
	};
	var itemTable = createElementIn(box, "table", attribs);
	var component = createElementIn(itemTable, "tbody", null);
	var row = createElementIn(component, "tr", null);
	var attribs = {
		"align" :"left"
	};
	var statusCell = createElementIn(row, "td", attribs);
	var attribs = {
		"align" :"right",
		"width" :"50"
	};
	var toggleCell = createElementIn(row, "td", attribs);

	if (prefLang != "ger")
		statusCell.appendChild(document.createTextNode("Preferred Language: "));
	else
		statusCell
				.appendChild(document.createTextNode("Ausgewählte Sprache: "));
	var attribs = {
		"id" :"status2"
	};
	var status = createElementIn(statusCell, "span", attribs);

	updatePrefLang();

	var toggleButton = document.createElement("input");
	toggleButton.type = "button";

	if (prefLang != "ger")
		toggleButton.value = "change";
	else
		toggleButton.value = "Wechseln";

	toggleButton.onclick = function() {
		if (prefLang != "ger")
			setPrefLang("ger");
		else
			setPrefLang("eng");
		updatePrefLang();
		window.location.reload();
	}
	toggleCell.appendChild(toggleButton);

}

function updatePrefLang() {
	var statusspan = document.getElementById('status2');
	if (prefLang == "ger") {
		statusspan.style.color = 'green';
		setElementText("status2", "ger");
	} else {
		statusspan.style.color = 'red';
		if (prefLang == "eng")
			setElementText("status2", "eng");
		else
			setElementText("status2", "UNKNOWN");
	}
	// window.location.reload();

}
