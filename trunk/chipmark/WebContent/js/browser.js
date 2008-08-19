var BrowserDetect = {
	init: function () {
		this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
		this.version = this.searchVersion(navigator.userAgent)
			|| this.searchVersion(navigator.appVersion)
			|| "an unknown version";
		this.OS = this.searchString(this.dataOS) || "an unknown OS";
	},
	searchString: function (data) {
		for (var i=0;i<data.length;i++)	{
			var dataString = data[i].string;
			var dataProp = data[i].prop;
			this.versionSearchString = data[i].versionSearch || data[i].identity;
			if (dataString) {
				if (dataString.indexOf(data[i].subString) != -1)
					return data[i].identity;
			}
			else if (dataProp)
				return data[i].identity;
		}
	},
	searchVersion: function (dataString) {
		var index = dataString.indexOf(this.versionSearchString);
		if (index == -1) return;
		return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
	},
	dataBrowser: [
		{ 	string: navigator.userAgent,
			subString: "OmniWeb",
			versionSearch: "OmniWeb/",
			identity: "OmniWeb"
		},
		{
			string: navigator.vendor,
			subString: "Apple",
			identity: "Safari"
		},
		{
			prop: window.opera,
			identity: "Opera"
		},
		{
			string: navigator.vendor,
			subString: "iCab",
			identity: "iCab"
		},
		{
			string: navigator.vendor,
			subString: "KDE",
			identity: "Konqueror"
		},
		{
			string: navigator.userAgent,
			subString: "Firefox",
			identity: "Firefox"
		},
		{
			string: navigator.vendor,
			subString: "Camino",
			identity: "Camino"
		},
		{		// for newer Netscapes (6+)
			string: navigator.userAgent,
			subString: "Netscape",
			identity: "Netscape"
		},
		{
			string: navigator.userAgent,
			subString: "MSIE",
			identity: "Internet Explorer",
			versionSearch: "MSIE"
		},
		{
			string: navigator.userAgent,
			subString: "Gecko",
			identity: "Mozilla",
			versionSearch: "rv"
		},
		{ 		// for older Netscapes (4-)
			string: navigator.userAgent,
			subString: "Mozilla",
			identity: "Netscape",
			versionSearch: "Mozilla"
		}
	],
	dataOS : [
		{
			string: navigator.platform,
			subString: "Win",
			identity: "Windows"
		},
		{
			string: navigator.platform,
			subString: "Mac",
			identity: "Mac"
		},
		{
			string: navigator.platform,
			subString: "Linux",
			identity: "Linux"
		}
	]

};

function addReport(where){
  BrowserDetect.init();
  var b_result=BrowserDetect.browser;
  var v_result=BrowserDetect.version;
  var os_result=BrowserDetect.OS;

  var recommend=0;

  var title;
  var output;
  var altdls;
  var tdclass;

  var netclr;
  var netclr_version;

  if (b_result == "Internet Explorer"){
	if ((navigator.userAgent.indexOf(".NET CLR 3.")>-1)){
		netclr = true;
		netclr_v = 2;
	}else if ((navigator.userAgent.indexOf(".NET CLR 2.")>-1)){
		netclr = true;
		netclr_v = 2;
	}else if ((navigator.userAgent.indexOf(".NET CLR 1.")>-1)){
		netclr = true;
		netclr_v = 1;
	}else{
		netclr = false;
	}
  }
 
  if ((b_result == "Internet Explorer") && (os_result=="Windows")){
	if (netclr == false){
		recommend = 1;
	} else {
		if (netclr_v == 1){
			recommend = 2;
		}else if (netclr_v == 2){
			recommend = 3;
		}
	}
  }

  if (((b_result == "Firefox") && (v_result >= 1)) || (b_result == "Mozilla")){
	recommend = 4;
  }


  // recommend value key:
  // 0 = Unsupported browser
  // 1 = IE6+/Windows with no .NET CLR
  // 2 = IE6+/Windows with .NET CLR 1
  // 3 = IE6+/Windows with .NET CLR 2 or 3
  // 4 = FF1+/Any

    var on_title, on_body;

  switch(recommend){
    case 0:
        on_title="None_title";
        on_body="None_body";
	break;
    case 1:
	on_title="IE_title";
        on_body="IE_body_CLR";
	break;
    case 2:
	on_title="IE_title";
        on_body="IE_body_CLR";
	break;
    case 3:
	on_title="IE_title";
        on_body="IE_body";
	break;
    case 4:
        on_title="FF_title";
        on_body="FF_body";     
	break;
  }

  document.getElementById(on_title).style.display="inline";
  document.getElementById(on_body).style.display="inline";
  document.getElementById("browser_slot").style.display="inline"; 
  document.getElementById("browser_slot").appendChild(document.createTextNode(b_result+" version "+v_result));

}