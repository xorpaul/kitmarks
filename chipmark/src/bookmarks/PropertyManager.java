/*
-------------------------------------------------------------------------------------------------
Copyright, 2005, Chipmark.

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
-------------------------------------------------------------------------------------------------
 */

package bookmarks;

/**
 * Diese Angaben müssen bei einem Serverumzug verändert werden. Zusätzlich muss
 * die Datei context.xml unter META-INF verändert werden verändert werden!
 */

public class PropertyManager {
	// Define instance specific information
	static public String CATALINA_BASE =  "/srv/apache-tomcat-6.0.18/";
	/**
	 * CATALINA_BASE = unter Linux: "/srv/devtomcat/"; unter Windows:
	 * "C:\\\\Program Files\\\\Apache Software Foundation\\\\Tomcat 6.0\\\\webapps\\\\"
	 * ; ; debug in eclipse: "/home/paul/workspace/cm4-linux/";
	 */

	public static final String XSLPATHGER =  "webapps/chipmark/xsl/ger/"; 

	public static final String XSLPATHENG =  "webapps/chipmark/xsl/eng/"; 
	/**
	 * Diese Variablen geben den Pfad zu den xsl Dateien an. Der erste Ordner
	 * ist der Applikationsname im Tomcat hier chipmark
	 * 
	 * Bsp. unter Linux: "webapps/chipmark/xsl/eng/"; für debug in eclipse:
	 * "WebContent/xsl/ger/"; Bsp. unter Windows: "chipmark\\\\xsl\\eng\\\\";
	 */

	// Define private information used in the DatabaseWrapper Servlet
	static public String database_domain = "localhost";
	static public String database_name = "";
	static public String database_user = "";
	static public String database_password = "";
	static public int database_max_chipmarks = 5000;

	// Define private information used in the ForgotPassword Servlet
	/**
	 * Hier muss der Email account, der für die PW vergessen und den Newletter
	 * Nachrichten versendet eingetragen werden
	 * 
	 */
	static public String email_from = "SCC-Chipmark@iwr.fzk.de";
	static public String email_host = "smtp.fzk.de";
	static public int email_port = 25;

	// Facebook api settings
	static public String facebook_api_key = "ANY_THING";
	static public String facebook_secret = "ANY_THING";
	static public String facebook_infinite_key = "ANY_THING";

	public static final String ADS_PASSWORD = "jsdLdjsi2ASlsdasdnmxclayxhjsieasjkd";
}
