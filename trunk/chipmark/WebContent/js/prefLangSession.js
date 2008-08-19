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
 * Diese JavaScript Funktionen werden nur dazu benutzt mit der Session zu agieren.
 * 
 * Es wird nicht der Datenbankeintrag umgesetzt.
 * 
 */

var prefLangSession = null;

function getprefLangSession() {
	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=getprefLangSession');

//	if (result == null)
//		alert("Error: Result von action=getprefLangSession ist null!")

	if (result != null) {
		var xml = request.responseXML;
		// alert("Preferred Language Session: " +
		// xml.getElementsByTagName("Message")[0].firstChild.nodeValue)
		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			prefLangSession = null;
			return false;
		}
		if (xml.getElementsByTagName("Message")[0].firstChild) {
			if (xml.getElementsByTagName("Message")[0].firstChild.nodeValue == 'eng')
				prefLangSession = "eng";
			else
				prefLangSession = "ger";
		}
		return true;
	}
	prefLangSession = null;
	return false;
}

function setprefLangSession(newprefLangSession) {

	// alert(newprefLangSession);
	newprefLangSession = false || newprefLangSession;

	var request = getXMLHTTPObject();
	var result = execXMLQuery(request, 'POST', 'NodeAction',
			'action=setprefLangSession&prefLangSession=' + newprefLangSession);

	if (result != null) {
		var xml = request.responseXML;

		// we can ditch the request object now
		request = null;

		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE') {
			if (xml.getElementsByTagName("Message")[0].firstChild) {
				alert(xml.getElementsByTagName("Message")[0].firstChild.nodeValue);
			}
			prefLangSession = null;
			return false;
		}
		prefLangSession = newprefLangSession;
		return true;
	}
	prefLangSession = null;
	return false;
}

function showprefLangSession(container) {
	container = document.getElementById(container);

	var success = getprefLangSession();

	// alert("Success: " + success);

	var box = createElementIn(container, "div", null);
	//box.className = 'graybox'
	box.style.width = '100%';
	box.style.minWidth = '100%';

	// alert(prefLangSession);

	var attribs = {
		"width" :"100%;",
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

	var attribs = {
		"id" :"status3"
	};
	var status = createElementIn(statusCell, "span", attribs);

	// updateprefLangSession();

	var toggleFlag = document.createElement("img");

	toggleFlag.ID = "ToggleFlagge";

	if (prefLangSession == "eng") {
		toggleFlag.src = "images/ger.png"
	}

	if (prefLangSession == "ger") {
		toggleFlag.src = "images/eng.gif"
	}

	toggleFlag.onclick = function() {
		if (prefLangSession != "ger") {
			setprefLangSession("ger");
			toggleFlag.src = "images/eng.gif";
		} else {
			setprefLangSession("eng");
			toggleFlag.src = "images/ger.png"
		}
		// updateprefLangSession();
		toggleCell.appendChild(toggleFlag);
		window.location.reload();
	}
	toggleCell.appendChild(toggleFlag);

}

function updateprefLangSession() {
	var toggleFlag = document.getElementBySrc("ToggleFlagge");

	if (toggleFlag == null)
		alert("document.getElementById('ToggleFlagge') ist null!");

	if (prefLangSession == "ger") {
		toggleFlag.src = "images/eng.gif";
	} else {
		if (prefLangSession == "eng")
			toggleFlag.src = "images/eng.gif";
	}

}
