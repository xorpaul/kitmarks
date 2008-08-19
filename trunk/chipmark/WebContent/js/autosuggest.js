/*
The autosuggest code was taken from:
http://www.webreference.com/programming/javascript/ncz/column2/4.html

It was authored by:
Nicholas C. Zakas
http://www.nczonline.net/
*/



var labelSuggestions = new LabelSuggestions();
var userURLs = new UserURLs();


/**
 *
 */
function initAutosuggest () {
	initHelper(document.getElementById("labels"));
}

/**
 *
 */
function initAutosuggestElement (oElement /*:HTMLInputElement*/) {
	initHelper(oElement);
}

/**
 * 
 *
 */
function initHelper (oElement /*:HTMLInputElement*/) {
	if(!oElement.createTextRange) {
		labelSuggestions.retrieveLabelSuggestions();
		var oTextboxAL = new AutoSuggestControl(oElement, labelSuggestions);
	}
	
	//userURLs.retrieveURLs();
}


function checkAlreadyChipmarked(url){
    userURLs.retrieveURLs();
    if (userURLs.doChipmark(url))
        return true;
    else
        return false;
}

/**
 * An autosuggest textbox control.
 * @class
 * @scope public
 */
function AutoSuggestControl(oTextbox /*:HTMLInputElement*/, 
                            oProvider /*:SuggestionProvider*/) {
    
    /**
     * The currently selected suggestions.
     * @scope private
     */   
    this.cur /*:int*/ = -1;

    /**
     * The dropdown list layer.
     * @scope private
     */
    this.layer = null;
    
    /**
     * Suggestion provider for the autosuggest feature.
     * @scope private.
     */
    this.provider /*:SuggestionProvider*/ = oProvider;
    
    /**
     * The textbox to capture.
     * @scope private
     */
    this.textbox /*:HTMLInputElement*/ = oTextbox;
    
    //initialize the control
    this.init();
    
}

/**
 * Autosuggests one or more suggestions for what the user has typed.
 * If no suggestions are passed in, then no autosuggest occurs.
 * @scope private
 * @param aSuggestions An array of suggestion strings.
 * @param bTypeAhead If the control should provide a type ahead suggestion.
 */
AutoSuggestControl.prototype.autosuggest = function (aSuggestions /*:Array*/,
                                                     bTypeAhead /*:boolean*/,
                                                     index /*:int*/) {
    
    //make sure there's at least one suggestion
    if (aSuggestions.length > 0) {
        if (bTypeAhead) {
           this.typeAhead(aSuggestions[0], index);
        }
        
        this.showSuggestions(aSuggestions);
    } else {
        this.hideSuggestions();
    }
};

/**
 * Creates the dropdown layer to display multiple suggestions.
 * @scope private
 */
AutoSuggestControl.prototype.createDropDown = function () {

    var oThis = this;

    //create the layer and assign styles
    this.layer = document.createElement("div");
    this.layer.className = "suggestions";
    this.layer.style.visibility = "hidden";
    this.layer.style.width = this.textbox.offsetWidth;
    
    //when the user clicks on the a suggestion, get the text (innerHTML)
    //and place it into a textbox
    this.layer.onmousedown = 
    this.layer.onmouseup = 
    this.layer.onmouseover = function (oEvent) {
        oEvent = oEvent || window.event;
        oTarget = oEvent.target || oEvent.srcElement;

        if (oEvent.type == "mousedown") {        
        	var index = -1;
    		var value = "";
        
			if((index = oThis.textbox.value.lastIndexOf(", ")) > -1) {
        		index +=2;
        		value = oThis.textbox.value.substring(0, index);
       		}
       		else if((index = oThis.textbox.value.lastIndexOf(",")) > -1) {
        		index +=1;
    	  		value = oThis.textbox.value.substring(0, index);
    		}
    
            oThis.textbox.value = value + oTarget.firstChild.nodeValue;
            oThis.hideSuggestions();
        } else if (oEvent.type == "mouseover") {
            	oThis.highlightSuggestion(oTarget);
        } else {
            	oThis.textbox.focus();
        }
    };
    
    
    document.body.appendChild(this.layer);

};

/**
 * Gets the left coordinate of the textbox.
 * @scope private
 * @return The left coordinate of the textbox in pixels.
 */
AutoSuggestControl.prototype.getLeft = function () /*:int*/ {

    var oNode = this.textbox;
    var iLeft = 0;
    
    while(oNode.tagName != "BODY") {
        iLeft += oNode.offsetLeft;
        oNode = oNode.offsetParent;        
    }
    
    return iLeft;
};

/**
 * Gets the top coordinate of the textbox.
 * @scope private
 * @return The top coordinate of the textbox in pixels.
 */
AutoSuggestControl.prototype.getTop = function () /*:int*/ {

    var oNode = this.textbox;
    var iTop = 0;
    
    while(oNode.tagName != "BODY") {
        iTop += oNode.offsetTop;
        oNode = oNode.offsetParent;
    }
    
    return iTop;
};

/**
 * Handles three keydown events.
 * @scope private
 * @param oEvent The event object for the keydown event.
 */
AutoSuggestControl.prototype.handleKeyDown = function (oEvent /*:Event*/) {

    switch(oEvent.keyCode) {
        case 38: //up arrow
            this.previousSuggestion();
            break;
        case 40: //down arrow 
            this.nextSuggestion();
            break;
        case 13: //enter
            this.hideSuggestions();
            break;
    }

};

/**
 * Handles keyup events.
 * @scope private
 * @param oEvent The event object for the keyup event.
 */
AutoSuggestControl.prototype.handleKeyUp = function (oEvent /*:Event*/) {

    var iKeyCode = oEvent.keyCode;

    //for backspace (8) and delete (46), shows suggestions without typeahead
    if (iKeyCode == 8 || iKeyCode == 46) {
        this.provider.requestSuggestions(this, false);
        
    //make sure not to interfere with non-character keys
    } else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode < 46) || (iKeyCode >= 112 && iKeyCode <= 123)) {
        //ignore
    } else {
        //request suggestions from the suggestion provider with typeahead
        this.provider.requestSuggestions(this, true);
    }
};

/**
 * Hides the suggestion dropdown.
 * @scope private
 */
AutoSuggestControl.prototype.hideSuggestions = function () {
    this.layer.style.visibility = "hidden";

    //use text ranges for Internet Explorer
    if (this.textbox.createTextRange) {
        var oRange = this.textbox.createTextRange(); 
        oRange.moveStart("character", this.textbox.value.length); 
        oRange.moveEnd("character", this.textbox.value.length);      
        oRange.select();
        
    //use setSelectionRange() for Mozilla
    } else if (this.textbox.setSelectionRange) {
        this.textbox.setSelectionRange(this.textbox.value.length, this.textbox.value.length);
    }     
  
  	this.textbox.focus()
};

/**
 * Highlights the given node in the suggestions dropdown.
 * @scope private
 * @param oSuggestionNode The node representing a suggestion in the dropdown.
 */
AutoSuggestControl.prototype.highlightSuggestion = function (oSuggestionNode) {
    
    for (var i=0; i < this.layer.childNodes.length; i++) {
        var oNode = this.layer.childNodes[i];
        if (oNode == oSuggestionNode) {
            oNode.className = "current"
        } else if (oNode.className == "current") {
            oNode.className = "";
        }
    }
};

/**
 * Initializes the textbox with event handlers for
 * auto suggest functionality.
 * @scope private
 */
AutoSuggestControl.prototype.init = function () {

    //save a reference to this object
    var oThis = this;
    
    //assign the onkeyup event handler
    this.textbox.onkeyup = function (oEvent) {
    
        //check for the proper location of the event object
        if (!oEvent) {
            oEvent = window.event;
        }    
        
        //call the handleKeyUp() method with the event object
        oThis.handleKeyUp(oEvent);
    };
    
    //assign onkeydown event handler
    this.textbox.onkeydown = function (oEvent) {
    
        //check for the proper location of the event object
        if (!oEvent) {
            oEvent = window.event;
        }    
        
        //call the handleKeyDown() method with the event object
        oThis.handleKeyDown(oEvent);
    };
    
    //assign onblur event handler (hides suggestions)    
    this.textbox.onblur = function () {
        oThis.hideSuggestions();
    };
    
    //create the suggestions dropdown
    this.createDropDown();
};

/**
 * Highlights the next suggestion in the dropdown and
 * places the suggestion into the textbox.
 * @scope private
 */
AutoSuggestControl.prototype.nextSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;
    var index = -1;
    var value = "";

    if (cSuggestionNodes.length > 0 && this.cur < cSuggestionNodes.length-1) {
    
    	if((index = this.textbox.value.lastIndexOf(", ")) > -1) {
        	index +=2;
        	value = this.textbox.value.substring(0, index);
        }
        else if((index = this.textbox.value.lastIndexOf(",")) > -1) {
        	index +=1;
    	  	value = this.textbox.value.substring(0, index);
    	}
	
        var oNode = cSuggestionNodes[++this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = value + oNode.firstChild.nodeValue; 
    }
};

/**
 * Highlights the previous suggestion in the dropdown and
 * places the suggestion into the textbox.
 * @scope private
 */
AutoSuggestControl.prototype.previousSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;
    var index = -1;
    var value = "";

    if (cSuggestionNodes.length > 0 && this.cur > 0) {
    
		if((index = this.textbox.value.lastIndexOf(", ")) > -1) {
        	index +=2;
        	value = this.textbox.value.substring(0, index);
        }
        else if((index = this.textbox.value.lastIndexOf(",")) > -1) {
        	index +=1;
    	  	value = this.textbox.value.substring(0, index);
    	}
	
        var oNode = cSuggestionNodes[--this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = value + oNode.firstChild.nodeValue;   
    }
};

/**
 * Selects a range of text in the textbox.
 * @scope public
 * @param iStart The start index (base 0) of the selection.
 * @param iLength The number of characters to select.
 */
AutoSuggestControl.prototype.selectRange = function (iStart /*:int*/, iLength /*:int*/) {

    //use text ranges for Internet Explorer
    if (this.textbox.createTextRange) {
        var oRange = this.textbox.createTextRange(); 
        oRange.moveStart("character", iStart); 
        oRange.moveEnd("character", iLength - this.textbox.value.length);      
        oRange.select();
        
    //use setSelectionRange() for Mozilla
    } else if (this.textbox.setSelectionRange) {
        this.textbox.setSelectionRange(iStart, iLength);
    }     

    //set focus back to the textbox
    this.textbox.focus();      
}; 

/**
 * Builds the suggestion layer contents, moves it into position,
 * and displays the layer.
 * @scope private
 * @param aSuggestions An array of suggestions for the control.
 */
AutoSuggestControl.prototype.showSuggestions = function (aSuggestions /*:Array*/) {
    
    var oDiv = null;
    this.layer.innerHTML = "";  //clear contents of the layer
    
    for (var i=0; i < aSuggestions.length; i++) {
        oDiv = document.createElement("div");
        oDiv.appendChild(document.createTextNode(aSuggestions[i]));
        this.layer.appendChild(oDiv);
    }
    
    this.layer.style.left = this.getLeft() + "px";
    this.layer.style.top = (this.getTop()+this.textbox.offsetHeight) + "px";
    this.layer.style.visibility = "visible";

};

/**
 * Inserts a suggestion into the textbox, highlighting the 
 * suggested part of the text.
 * @scope private
 * @param sSuggestion The suggestion for the textbox.
 */
AutoSuggestControl.prototype.typeAhead = function (sSuggestion /*:String*/,
                                                     index /*:int*/) {
                                                     
    var value = "";
    var iEnd = -1;
    
    if(index > -1) {
    	value = this.textbox.value.substring(0, index) + sSuggestion;
    	iEnd = index + sSuggestion.length;
    } else {
    	value = sSuggestion;    	
    	iEnd = sSuggestion.length;
    }

    //check for support of typeahead functionality
    if (this.textbox.createTextRange || this.textbox.setSelectionRange){    
    	var iLen = this.textbox.value.length;
        this.textbox.value = value; 
        this.selectRange(iLen, iEnd);
    }
};


/**
 * Provides suggestions for label names.
 * @class
 * @scope public
 */
function LabelSuggestions() {
    this.labels = new Array();
}

/**
 * Provides storage for the users existing URLs
 * @class
 * @scope public
 */
function UserURLs() {
    this.urls = new Array();
}

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
LabelSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
    var aSuggestions = [];
    var sTextboxValue = oAutoSuggestControl.textbox.value;
    var index = -1;
    var value = "";
    
    if (sTextboxValue.length > 0){
    
		if((index = sTextboxValue.lastIndexOf(", ")) > -1) {
    		index +=2;
    		value = sTextboxValue.substring(index, sTextboxValue.length);
    	}
    	
    	else if((index = sTextboxValue.lastIndexOf(",")) > -1) {
    		index +=1;
	    	value = sTextboxValue.substring(index, sTextboxValue.length);
		} else {
			value = sTextboxValue
		}
    
        //search for matching labels
        for (var i=0; i < this.labels.length; i++) { 
            if (this.labels[i].indexOf(value) == 0) {
                aSuggestions.push(this.labels[i]);
            } 
        }
    }

    //provide suggestions to the control
    oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead, index);
}

/**
 * Add a suggestion to the autosuggest control.
 * @scope protected
 * @param oLabel A new label to be added to the suggest.
 */
LabelSuggestions.prototype.addLabel = function (oLabel /*:String*/) {
    
    for(var i = 0; i < this.labels.length; i++) {
    	if(this.labels[i] == oLabel) {
    	    return;
    	}
    }
    this.labels.push(oLabel);
};

/**
 * Add a URL to the user's urls.
 * @scope protected
 * @param oLabel A new url to be added to the list.
 */
UserURLs.prototype.addURL = function (oURL /*:String*/) {
    
    for(var i = 0; i < this.urls.length; i++) {
    	if(this.urls[i] == oURL) {
    	    return;
    	}
    }
    this.urls.push(oURL);
};

/**
 * Checks to see if a user already has a url chipmarked
 * @scope protected
 * @param oLabel The URL to test.
 * @return True, if the user already has the url chipmarked
 */
UserURLs.prototype.containsURL = function (oURL /*:String*/) {

    for(var i = 0; i < this.urls.length; i++) {

    	if(this.urls[i] == oURL) {
    	    return true;
    	}
    	
    }
    return false;
};

UserURLs.prototype.doChipmark = function (oURL /*:String*/) {
	if(userURLs.containsURL(oURL)) {
		return confirm("You already have that URL chipmarked. Are you sure you want to chipmark it again?");
	}	
	return true;

}

/**
 *Retrieves users labels from the server.
 *
 *
 */
LabelSuggestions.prototype.retrieveLabelSuggestions = function () {
    var getLabels = false;
    try{
        LABELS.length;
    }
    catch(err){
        getLabels = true;
    }

    if (getLabels){
	    var request = getXMLHTTPObject();	
	    var response = execXMLQuery(request, 'POST', 'AutosuggestServlet', 'suggest=1');
    	
	    if (response != null){        
            var xml = request.responseXML;
            request = null;
		    if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE'){		    
		    }
		    else {		
			    var labels = xml.getElementsByTagName("Object");
			    if(labels) {
				    for(var i = 0; i < labels.length; i++) {				
					    labelSuggestions.addLabel(trim(labels[i].firstChild.nodeValue));					
				    }
			    }
		    }
    		
	    }
	}
	else{
	    for(var i = 0; i < LABELS.length; i++) {				
            labelSuggestions.addLabel(LABELS[i]);					
        }	    
    }
};


/**
 *Retrieves users urls from the server.
 *
 *
 */
UserURLs.prototype.retrieveURLs = function () {

	var request = getXMLHTTPObject();	
	var response = execXMLQuery(request, 'POST', 'AutosuggestServlet', 'suggest=2');	
	
	if (response != null){        
        var xml = request.responseXML;
        request = null;
		if (xml.getElementsByTagName("Result")[0].firstChild.nodeValue == 'FAILURE'){
		}
		else {		
			var urls = xml.getElementsByTagName("Object");
			if(urls) {
				for(var i=0; i<urls.length; i++) {
					userURLs.addURL(urls[i].firstChild.nodeValue);					
				}
			}
		}
		
	}
};


