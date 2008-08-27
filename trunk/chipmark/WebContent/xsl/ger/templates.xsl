<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" method="html" indent="yes" />
	<xsl:include href="titles.xsl" />
	<xsl:include href="common.xsl" />
	<xsl:include href="about.xsl" />
	<xsl:include href="downloads.xsl" />
	<xsl:include href="features.xsl" />
	<xsl:include href="tour.xsl" />
	<xsl:include href="search.xsl" />
	<xsl:include href="functions.xsl" />
	<xsl:include href="management.xsl" />
	<xsl:include href="breadcrumbs.xsl" />
	<xsl:include href="modules.xsl" />
	<xsl:include href="developers.xsl" />
	<xsl:include href="chipmarkit.xsl" />
	<xsl:include href="help.xsl" />
	<xsl:template match="/">
		<!--
			*************************************************************************************************
			Copyright 2004-2007 Chipmark. This file is part of Chipmark. Chipmark
			is free software; you can redistribute it and/or modify it under the
			terms of the GNU General Public License as published by the Free
			Software Foundation; either version 2 of the License, or (at your
			option) any later version. Chipmark is distributed in the hope that
			it will be useful, but WITHOUT ANY WARRANTY; without even the implied
			warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
			the GNU General Public License for more details. You should have
			received a copy of the GNU General Public License along with
			Chipmark; if not, write to the Free Software Foundation, Inc., 59
			Temple Place, Suite 330, Boston, MA 02111-1307 USA
			*************************************************************************************************
		-->
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		CONTENT (inserts appropriate content template)
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="CONTENT">

		<!-- If the page has no target, show the home page -->
		<xsl:if test="xml/Target=''">
			<xsl:call-template name="BODY_MAIN" />
		</xsl:if>

		<!-- If the page is the manage page, insert the manage page body -->
		<xsl:if test="xml/Target='admin'">
			<xsl:call-template name="ADMIN" />
		</xsl:if>

		<!-- If the page is the manage page, insert the manage page body -->
		<xsl:if test="xml/Target='manage'">
			<xsl:call-template name="BODY_MANAGE_CHIPMARKS_FOLDER_VIEW" />
		</xsl:if>
		<!--
			If the page is the searchLinks page, insert the search page body
		-->
		<xsl:if test="xml/Target='searchLinks'">
			<xsl:call-template name="BODY_SEARCH_LINKS" />
		</xsl:if>

		<!-- If the target is the about page, insert the about page body -->
		<xsl:if test="xml/Target='about'">
			<xsl:call-template name="BODY_ABOUT_CHIPMARK" />
		</xsl:if>
		<!--
			If the target is the about dev team page, insert the dev team page
			body
		-->
		<xsl:if test="xml/Target='team'">
			<xsl:call-template name="BODY_ABOUT_TEAM" />
		</xsl:if>
		<!--
			If the target is the about us_06 page, insert the about us_06 page
			body
		-->
		<xsl:if test="xml/Target='about_us_06'">
			<xsl:call-template name="BODY_ABOUT_US_06" />
		</xsl:if>
		<!--
			If the target is the about us_05 page, insert the about us_05 page
			body
		-->
		<xsl:if test="xml/Target='about_us_05'">
			<xsl:call-template name="BODY_ABOUT_US_05" />
		</xsl:if>
		<!--
			If the target is the about us page, insert the about us page body
		-->
		<xsl:if test="xml/Target='about_us_04'">
			<xsl:call-template name="BODY_ABOUT_US_04" />
		</xsl:if>
		<!--
			If the target is the about us page, insert the about us page body
		-->
		<xsl:if test="xml/Target='buddies'">
			<xsl:call-template name="BODY_BUDDIES" />
		</xsl:if>

		<!-- If the target is the faqs page, insert the faqs page body -->
		<xsl:if test="xml/Target='faqs'">
			<xsl:call-template name="BODY_FAQS" />
		</xsl:if>

		<!-- If the target is the contact page, insert the contact page body -->
		<xsl:if test="xml/Target='contact'">
			<xsl:call-template name="BODY_CONTACT" />
		</xsl:if>
		<!--
			If the target is the features page, insert the features page body
		-->
		<xsl:if test="xml/Target='features'">
			<xsl:call-template name="BODY_FEATURES" />
		</xsl:if>
		<!--
			If the target is the updates page, insert the features page body
		-->
		<xsl:if test="xml/Target='updates'">
			<xsl:call-template name="BODY_UPDATES" />
		</xsl:if>

		<!-- If the target is the license page, insert the license page body -->
		<xsl:if test="xml/Target='license'">
			<xsl:call-template name="BODY_LICENSE" />
		</xsl:if>

		<!-- If the target is the license page, insert the license page body -->
		<xsl:if test="xml/Target='tour'">
			<xsl:call-template name="BODY_TOUR" />
		</xsl:if>

		<!-- If the target is the license page, insert the license page body -->
		<xsl:if test="xml/Target='tour_web'">
			<xsl:call-template name="BODY_TOUR_WEB" />
		</xsl:if>

		<!-- If the target is the license page, insert the license page body -->
		<xsl:if test="xml/Target='tour_ie'">
			<xsl:call-template name="BODY_TOUR_IE" />
		</xsl:if>

		<!-- If the target is the license page, insert the license page body -->
		<xsl:if test="xml/Target='tour_firefox'">
			<xsl:call-template name="BODY_TOUR_FIREFOX" />
		</xsl:if>
		<!--
			If the target is the features web page, insert the features web page
			body
		-->
		<xsl:if test="xml/Target='features_web'">
			<xsl:call-template name="BODY_FEATURES_WEB" />
		</xsl:if>
		<!--
			If the target is the features ie page, insert the features ie page
			body
		-->
		<xsl:if test="xml/Target='features_ie'">
			<xsl:call-template name="BODY_FEATURES_IE" />
		</xsl:if>
		<!--
			If the target is the features firefox page, insert the features
			firefox page body
		-->
		<xsl:if test="xml/Target='features_firefox'">
			<xsl:call-template name="BODY_FEATURES_FIREFOX" />
		</xsl:if>

		<!-- If the target is import, insert the import body -->
		<xsl:if test="xml/Target='importsuccess'">
			<xsl:call-template name="BODY_IMPORT_SUCCESS" />
		</xsl:if>

		<!-- If the target is import, insert the import body -->
		<xsl:if test="xml/Target='importerror'">
			<xsl:call-template name="BODY_IMPORT_ERROR" />
		</xsl:if>

		<!-- If the target is registration, insert the registration body -->
		<xsl:if test="xml/Target='registration'">
			<xsl:call-template name="BODY_ADD_CLIENT" />
		</xsl:if>

		<!-- If the target is general about, insert the general about body -->
		<xsl:if test="xml/Target='generalAbout'">
			<xsl:call-template name="BODY_ABOUT_GENERAL" />
		</xsl:if>

		<!-- If the target is download, insert the download body -->
		<xsl:if test="xml/Target='download'">
			<xsl:call-template name="BODY_DOWNLOAD" />
		</xsl:if>
		<!--
			If the target is forgottenusernamepassword, insert the
			forgottenusernamepassword body
		-->
		<xsl:if test="xml/Target='forgottenusernamepassword'">
			<xsl:call-template name="BODY_FORGOTTEN_USERNAME_PASSWORD" />
		</xsl:if>

		<!-- If the target is privacy, insert the privacy policy body -->
		<xsl:if test="xml/Target='privacy'">
			<xsl:call-template name="BODY_PRIVACY_POLICY" />
		</xsl:if>

		<!-- If the target is sponsor, insert the sponsor policy body -->
		<xsl:if test="xml/Target='sponsor'">
			<xsl:call-template name="BODY_SPONSOR" />
		</xsl:if>

		<!-- If the target is the help page, insert the help page body -->
		<xsl:if test="xml/Target='help'">
			<xsl:call-template name="BODY_HELP" />
		</xsl:if>
		<!--
			If the target is underConstruction, insert the under constructin body
		-->
		<xsl:if test="xml/Target='underConstruction'">
			<xsl:call-template name="BODY_UNDER_CONSTRUCTION" />
		</xsl:if>
		<xsl:if test="xml/Target='advancedSearch'">
			<!-- Use simple search since adv is apparently broken -->
			<xsl:call-template name="SEARCH_SIMPLE" />
		</xsl:if>
		<xsl:if test="xml/Target='sharing'">
			<xsl:call-template name="BODY_SHARING" />
		</xsl:if>
		<xsl:if test="xml/Target='downloadFirefox'">
			<xsl:call-template name="BODY_DOWNLOAD_FIREFOX" />
		</xsl:if>
		<xsl:if test="xml/Target='downloadIE'">
			<xsl:call-template name="BODY_DOWNLOAD_IE" />
		</xsl:if>
		<xsl:if test="xml/Target='jshowto'">
			<xsl:call-template name="BODY_HOWTO_ENABLE_JAVASCRIPT" />
		</xsl:if>
		<xsl:if test="xml/Target='developers'">
			<xsl:call-template name="BODY_DEVELOPERS" />
		</xsl:if>
		<xsl:if test="xml/Target='devapi'">
			<xsl:call-template name="BODY_DEV_API" />
		</xsl:if>


		<!-- If the target is the browse page, insert the browse page body -->
		<xsl:if test="xml/Target='browse'">
			<xsl:call-template name="BODY_BROWSE" />
		</xsl:if>

		<!-- The following are templates targetted by servlets -->
		<xsl:if test="xml/Target='top10bookmarked'">
			<xsl:call-template name="BODY_TOP_10" />
		</xsl:if>
		<xsl:if test="xml/Target='manageBuddies'">
			<xsl:call-template name="BODY_BUDDIES" />
		</xsl:if>
		<xsl:if test="xml/Target='mostrecentlyadded'">
			<xsl:call-template name="BODY_MOST_RECENTLY_ADDED" />
		</xsl:if>
		<xsl:if test="xml/Target='recommender'">
			<xsl:call-template name="BODY_RECOMMENDER" />
		</xsl:if>
		<xsl:if test="xml/Target='addclientresult'">
			<xsl:call-template name="BODY_ADD_CLIENT_RESULT" />
		</xsl:if>
		<xsl:if test="xml/Target='accountpreferences'">
			<xsl:call-template name="BODY_ACCOUNT_PREFS" />
		</xsl:if>
		<xsl:if test="xml/Target='addlink'">
			<xsl:call-template name="BODY_ADD_LINK" />
		</xsl:if>
		<xsl:if test="xml/Target='editlink'">
			<xsl:call-template name="BODY_EDIT_LINK" />
		</xsl:if>
		<xsl:if test="xml/Target='error'">
			<xsl:call-template name="BODY_ERROR" />
		</xsl:if>
		<xsl:if test="xml/Target='export'">
			<xsl:call-template name="BODY_EXPORT" />
		</xsl:if>
		<xsl:if test="xml/Target='forgotusernamepassword'">
			<xsl:call-template name="BODY_FORGOT_USERNAME_PASSWORD" />
		</xsl:if>
		<xsl:if test="xml/Target='import'">
			<xsl:call-template name="BODY_IMPORT" />
		</xsl:if>
		<xsl:if test="xml/Target='searchlinks'">
			<xsl:call-template name="BODY_SEARCH_LINKS" />
		</xsl:if>
		<xsl:if test="xml/Target='searchuser'">
			<xsl:call-template name="BODY_SEARCH_USER" />
		</xsl:if>
		<xsl:if test="xml/Target='viewlinks'">
			<!-- <xsl:call-template name="BODY_VIEW_LINKS" /> -->
			<xsl:call-template name="BODY_MANAGE_CHIPMARKS_FOLDER_VIEW" />
		</xsl:if>
		<xsl:if test="xml/Target='userHome'">
			<xsl:call-template name="BODY_USER_HOME" />
		</xsl:if>
		<xsl:if test="xml/Target='api'">
			<xsl:call-template name="BODY_API" />
		</xsl:if>
		<xsl:if test="xml/Target='api_login'">
			<xsl:call-template name="BODY_API_LOGIN" />
		</xsl:if>
		<xsl:if test="xml/Target='api_logout'">
			<xsl:call-template name="BODY_API_LOGOUT" />
		</xsl:if>
		<xsl:if test="xml/Target='api_get'">
			<xsl:call-template name="BODY_API_GET" />
		</xsl:if>
		<xsl:if test="xml/Target='api_addlink'">
			<xsl:call-template name="BODY_API_ADDLINK" />
		</xsl:if>
		<xsl:if test="xml/Target='api_deletelink'">
			<xsl:call-template name="BODY_API_DELETELINK" />
		</xsl:if>
		<xsl:if test="xml/Target='api_toptenbookmarked'">
			<xsl:call-template name="BODY_API_TOPTENBOOKMARKED" />
		</xsl:if>
		<xsl:if test="xml/Target='api_random'">
			<xsl:call-template name="BODY_API_RANDOM" />
		</xsl:if>
		<xsl:if test="xml/Target='api_editlink'">
			<xsl:call-template name="BODY_API_EDITLINK" />
		</xsl:if>
		<xsl:if test="xml/Target='api_buddylist_display'">
			<xsl:call-template name="BODY_API_BUDDYLIST_DISPLAY" />
		</xsl:if>
		<xsl:if test="xml/Target='api_buddylist_add'">
			<xsl:call-template name="BODY_API_BUDDYLIST_ADD" />
		</xsl:if>
		<xsl:if test="xml/Target='api_buddylist_remove'">
			<xsl:call-template name="BODY_API_BUDDYLIST_REMOVE" />
		</xsl:if>
		<xsl:if test="xml/Target='api_buddylist_edit'">
			<xsl:call-template name="BODY_API_BUDDYLIST_EDIT" />
		</xsl:if>
		<xsl:if test="xml/Target='api_buddylist_getbuddyrequests'">
			<xsl:call-template name="BODY_API_BUDDYLIST_GETBUDDYREQUESTS" />
		</xsl:if>
		<xsl:if test="xml/Target='api_buddylist_setbuddyrequests'">
			<xsl:call-template name="BODY_API_BUDDYLIST_SETBUDDYREQUESTS" />
		</xsl:if>
		<xsl:if test="xml/Target='api_buddylist_viewpendingbuddyrequests'">
			<xsl:call-template name="BODY_API_BUDDYLIST_VIEWPENDINGBUDDYREQUESTS" />
		</xsl:if>
		<xsl:if test="xml/Target='api_buddylist_viewtransitivebuddies'">
			<xsl:call-template name="BODY_API_BUDDYLIST_VIEWTRANSITIVEBUDDIES" />
		</xsl:if>
		<xsl:if test="xml/Target='video_registration'">
			<xsl:call-template name="VIDEO_REGISTRATION" />
		</xsl:if>
		<xsl:if test="xml/Target='video_ieinstall'">
			<xsl:call-template name="VIDEO_IE_INSTALL" />
		</xsl:if>
		<xsl:if test="xml/Target='video_ffinstall'">
			<xsl:call-template name="VIDEO_FF_INSTALL" />
		</xsl:if>
		<xsl:if test="xml/Target='video_ffimport'">
			<xsl:call-template name="VIDEO_FF_IMPORT" />
		</xsl:if>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		XML_DUMP
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="XML_DUMP">
		<p class="header">XML DUMP</p>
		<br />
		<xsl:copy-of select="." />
	</xsl:template>
	<!--
		=====================================================================
	-->
	
	<!--
		ADMIN
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="ADMIN">
		<xsl:choose>
			<xsl:when
				test="not(boolean(xml/LoggedInAs)) or
			not(xml/LoggedInAs = 'admin')">
				<xsl:call-template name="BODY_MAIN" />
			</xsl:when>
			<xsl:otherwise>
				<p class="title">Newsletter</p>
				<table width="60%" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td>
							<p class="body">
								Diese Nachricht wird an alle einzigartigen Emailadressen
								versendet, die sich in der lokalen Datenbank befinden.
								<form action="AccountPreferences" method="POST">
									<table width="100%" border="0">
										<tr>
											<td class="form_elements">Betreff:</td>
											<td>
												<xsl:element name="input">
													<xsl:attribute name="type">TEXT</xsl:attribute>
													<xsl:attribute name="name">newsLetterSubject</xsl:attribute>
													<xsl:attribute name="value"></xsl:attribute>
													<xsl:attribute name="maxlength">255</xsl:attribute>
												</xsl:element>
											</td>
										</tr>
										<tr>
											<td class="form_elements">Text:</td>
											<td>
												<xsl:element name="textarea">
													<xsl:attribute name="type">TEXT</xsl:attribute>
													<xsl:attribute name="name">newsLetterText</xsl:attribute>
													<xsl:attribute name="value"></xsl:attribute>
													<xsl:attribute name="cols">40</xsl:attribute>
													<xsl:attribute name="rows">20</xsl:attribute>
												</xsl:element>
											</td>
										</tr>
										<tr>
											<td></td>
											<td>
												<INPUT TYPE="SUBMIT" name="submitBtn" VALUE="send newsletter" />
												<BR />
											</td>
										</tr>
									</table>
								</form>
							</p>
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	
	<!--
		BODY_USER_HOME
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_USER_HOME">
		<xsl:choose>
			<xsl:when test="not(boolean(xml/LoggedInAs)) or xml/LoggedInAs = ''">
				<xsl:call-template name="BODY_MAIN" />
			</xsl:when>
			<xsl:otherwise>
				<script type="text/javascript"> showFolderInvites(); showBuddyRequests(0);
				</script>
				<table width="100%" border="0">
					<tr>
						<td valign="top" width="50%">
							<p class="sub_header">Ihre Optionen:</p>
							<a href="Main?target=import">
								<img src="images/acorn16.png" border="0" />
								Bookmarks importieren
							</a>
							<br />
							<span class="subheader">Zuvor exportierte Bookmarks hochladen
							</span>
							<br /><br />
							<a href="Export">
								<img src="images/acorn16.png" border="0" />
								Bookmarks exportieren
							</a>
							<br />
							<span class="subheader">Sicherungsdatei der hochgeladenen
								Chipmarks erstellen und herunterladen</span>
							<br /><br />
							<a href="AddLink">
								<img src="images/acorn16.png" border="0" />
								Ein Chipmark hinzufügen
							</a>
							<br />
							<span class="subheader"> Einen neuen Link als Chipmark hinzufügen
							</span>
							<br /><br />
							<a href="Manage">
								<img src="images/acorn16.png" border="0" />
								Eigene Chipmarks verwalten
							</a>
							<br />
							<span class="subheader">Eigene Chipmarks anzeigen, löschen,
								editieren, versenden etc.</span>
							<br />
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_ACCOUNT_PREFS
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_ACCOUNT_PREFS">
		<p class="title">Account Einstellungen</p>
		<!--
			<div id="content_filter" style="width:100%;"> <script
			language="JavaScript"> showFilter('content_filter');</script> </div>
			<br /> <br /> <xsl:for-each select="xml/Result/Message"> <xsl:if
			test="Value!='SUCCESS'"> <xsl:if test="Type='error'"> <p> <font
			color="red"> <xsl:value-of select="Value" /> </font> </p> </xsl:if>
			<xsl:if test="Type!='error'"> <p> <font color="green"> <xsl:value-of
			select="Value" /> </font> </p> </xsl:if> </xsl:if> </xsl:for-each>
		-->
		<div id="preferred_lang" style="width:100%;">
			<script language="JavaScript"> showPrefLang('preferred_lang');</script>
		</div>
		<br />
		<br />
		<table width="60%" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td>
					<p class="body">
						Hier können die Kontoeinstellungen berabeitet werden. Für eine
						wirksame Änderung muss das aktuelle Passwort angegeben werden.
						<form action="AccountPreferences" method="POST">
							<table width="100%" border="0">
								<tr>
									<td class="form_elements">Neue E-Mailadresse:</td>
									<td>
										<xsl:element name="input">
											<xsl:attribute name="type">TEXT</xsl:attribute>
											<xsl:attribute name="name">newClientMail</xsl:attribute>
											<xsl:attribute name="value"></xsl:attribute>
											<xsl:attribute name="maxlength">255</xsl:attribute>
										</xsl:element>
									</td>
								</tr>
								<tr>
									<td class="form_elements">Neues Passwort:</td>
									<td>
										<xsl:element name="input">
											<xsl:attribute name="type">PASSWORD</xsl:attribute>
											<xsl:attribute name="name">newPassword</xsl:attribute>
											<xsl:attribute name="value"></xsl:attribute>
											<xsl:attribute name="maxlength">32</xsl:attribute>
										</xsl:element>
									</td>
								</tr>
								<tr>
									<td class="form_elements">Passwort wiederholen:</td>
									<td>
										<xsl:element name="input">
											<xsl:attribute name="type">PASSWORD</xsl:attribute>
											<xsl:attribute name="name">newPassword2</xsl:attribute>
											<xsl:attribute name="value"></xsl:attribute>
											<xsl:attribute name="maxlength">32</xsl:attribute>
										</xsl:element>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<br />
									</td>
								</tr>
								<tr>
									<td class="form_elements">Aktuelles Passwort:</td>
									<td>
										<xsl:element name="input">
											<xsl:attribute name="type">PASSWORD</xsl:attribute>
											<xsl:attribute name="name">oldPassword</xsl:attribute>
											<xsl:attribute name="value"></xsl:attribute>
											<xsl:attribute name="maxlength">32</xsl:attribute>
										</xsl:element>
									</td>
								</tr>
								<tr>
									<td></td>
									<td>
										<INPUT TYPE="SUBMIT" name="submitBtn" VALUE="Einstellungen speichern" />
										<BR />
									</td>
								</tr>
							</table>
						</form>
					</p>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_ADD_CLIENT (CREATE A NEW ACCOUNT)
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_ADD_CLIENT">
		<p class="title">Ein neues Konto erstellen</p>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<p class="body">
						Durch die Erstellung eines neuen Benutzerkontos können Sie Ihre
						Bookmarks von überall aus erreichen. Bitte lesen Sie zuvor die
						<a href="Main?target=privacy"> Datenschutzerklärung</a>
						um zu erfahren, wie mit Ihren persönlichen Daten umgegangen wird.
					</p>
					<form name="register_form" method="post" action="AddClient">
						<table border="0" cellpadding="0" cellspacing="5">
							<tr>
								<td width="150" align="right" valign="top"></td>
								<td class="form_elements" align="left" valign="top">Benötigte
									Informationen:</td>
							</tr>
							<tr>
								<td class="form_elements" width="150" align="right" valign="top">
									Benutzername:</td>
								<td class="form_elements" align="left" valign="top">
									<input name="clientName" type="text" id="username"
										maxlength="32" />
								</td>
							</tr>
							<tr>
								<td class="form_elements" width="150" align="right" valign="top">
									Passwort:</td>
								<td class="form_elements" align="left" valign="top">
									<input name="clientPass" type="password" id="password"
										maxlength="32" />
								</td>
							</tr>
							<tr>
								<td class="form_elements" width="250" align="right" valign="top">
									Passwort wiederholen:</td>
								<td class="form_elements" align="left" valign="top">
									<input name="clientPass2" type="password" id="password_again"
										maxlength="32" />
								</td>
							</tr>
							<tr>
								<td class="form_elements" width="150" align="right" valign="top">
									E-Mailadresse:</td>
								<td align="left" valign="top">
									<input name="clientMail" type="text" id="email"
										maxlength="255" />
								</td>
							</tr>
							<tr>
								<td width="150"></td>
								<td width="150">
									<p class="body"> Für den Fall, dass Sie Ihre Kontodaten vergessen
										haben, können Sie sich Ihre Daten an folgende E-Mailadresse
										schicken lassen.</p>
								</td>
							</tr>
							<tr>
								<td width="150" align="right" valign="top"></td>
								<td align="left" valign="top"></td>
							</tr>
							<tr>
								<td width="150" align="right" valign="top"></td>
								<td class="form_elements" align="left" valign="top">
									<input name="submitBtn" type="submit" id="create_user"
										value="Registrieren" />
								</td>
							</tr>
						</table>
					</form>
					<p></p>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_ADD_CLIENT_RESULT
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_ADD_CLIENT_RESULT">
		<p class="title">registration confirmation</p>
		<xsl:if test="xml/Result = 'SUCCESS'">
			<p>
				<font color="green">Ihr Benutzerkonto wurde erfolgreich erstellt.
					Sie können sich nun anmelden.</font>
			</p>
		</xsl:if>
		<xsl:if test="xml/Result != 'SUCCESS'">
			<p>
				<font color="red">
					<xsl:value-of select="xml/Result" />
				</font>
			</p>
		</xsl:if>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_LICENSE
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_LICENSE">
		<p class="sub_header">Documentation</p>
		<p class="sub_content">
			Chipmark is an open source project. As a result, we allow and
			encourage people to modify chipmark's source code, found in the
			download section. Modification and distribution of Chipmark must
			adhere to the terms set forth in the GPLv2 license, available
			<a href="COPYING">here</a>
			.
		</p>
		<p class="sub_header">Questions</p>
		<p class="sub_content">
			At times, it can be diffcult to find the answer to your question in
			the license. Feel free to visit our
			<a href="Main?target=developers">developers</a>
			section for more general help or questions about our API.
		</p>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_FAQS
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_FAQS">
		<p class="sub_header">general questions</p>
		<p class="sub_content">
			<a href="#Q1">What is Chipmark?</a>
			<br />
			<a href="#Q2">Why should I use Chipmark?</a>
			<br />
			<a href="#Q3">What do I need to use Chipmark?</a>
			<br />
			<a href="#Q4">What is a plug-in/extension?</a>
			<br />
			<a href="#Q5">How does Chipmark work?</a>
			<br />
			<a href="#Q6">Is Chipmark safe?</a>
			<br />
			<a href="#Q7">Who runs Chipmark?</a>
			<br />
			<a href="#Q8">May I help with Chipmark?</a>
			<br />
		</p>
		<p class="sub_header">technical questions</p>
		<p class="sub_content">
			<a href="#Q9">Can I load my exitsing bookmarks into Chipmark?</a>
			<br />
			<a href="#Q10">Why would I want to make my Chipmarks "public"?</a>
			<br />
			<a href="#Q11">What are "labels"?</a>
			<br />
			<a href="#Q12">What does "Error opening file for writing" mean when
				installing the Internet Explorer toolbar?</a>
			<br />
			<a href="#Q13">Why is the Chipmarks bar in the Firefox extension blank?
			</a>
			<br />
			<a href="#Q14">Where did all my Chipmarks go?</a>
			<br />
			<a href="#Q15">Will my account ever expire?</a>
			<br />
			<a href="#Q16">What if my question is not in the FAQ?</a>
			<br />
		</p>
		<table border="0" cellspacing="10" cellpadding="0" align="center"
			width="100%">
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q1">
						<p class="sub_header">Was ist Chipmark?</p>
					</a>
					<p> Chipmark ist ein Online-Bookmarkverwaltungsdienst, der Ihnen
						den Zugriff auf ihre Lesezeichen von jedem Computer und von
						überall ermöglicht.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q2">
						<p class="sub_header">Why should I use Chipmark?</p>
					</a>
					<p>Chipmark allows you to take your bookmarks with you
						wherever you go. Wether your at home, at work, or at school,
						Chipmark allows you to see, store, search, and share your
						bookmarks. Chipmark is also totally free and free of annoying
						advertising.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q3">
						<p class="sub_header">What do I need to use Chipmark?</p>
					</a>
					<p>Almost nothing! Chipmark has three interfaces: the web, a
						FireFox extension, and an Internet Explorer plug-in. Any modern
						broswer with javascript and cookies enabled should be able to use
						one of our interfaces. We do not currently support
						plug-ins/extensions for any browsers but Mozilla, FireFox, and
						Internet Explorer.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q4">
						<p class="sub_header">What is a plug-in/extension?</p>
					</a>
					<p>Basically, they are software which enables you to use
						chipmark directly from your toolbar instead of visiting the
						website. This is generally a more familar and flexible way to use
						your bookmarks. In fact, it is just like browser's built-in
						bookmarking capabilites. Most of our users use either the FF
						extension or the IE plug-in.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q5">
						<p class="sub_header">How does Chipmark work?</p>
					</a>
					<p> When you add a bookmark (or import your existing bookmarks),
						the information is sent to our server. We then store the bookmark
						in a database. When you access your bookmarks on the Chipmark
						server, they are retrieved from the database.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q6">
						<p class="sub_header">Is Chipmark safe?</p>
					</a>
					<p>
						Yes. First, we use SSL on our website and with the browser plugins
						to ensure that all information sent to and from the server is
						encrypted. Second, we have a strict
						<a href="Main?target=privacy">privacy policy</a>
						that ensures your bookmarks and personal information is private.
					</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q7">
						<p class="sub_header">Who runs Chipmark?</p>
					</a>
					<p>Chipmark is run by a group of students at the University
						of Minnesota as part of a year long class on open-source software
						development. To learn more about us, please check out the about
						section.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q8">
						<p class="sub_header">May I help with Chipmark?</p>
					</a>
					<p>
						We are an opn-source project so we welcome feedback and help. We
						also have a public API avaible in our
						<a href="Main?target=developer">developers</a>
						section. You can use our
						<a href="/Main?target=contact">contact</a>
						section and our
						<a href="http://www.chipmark.com:8082/">blog</a>
						to get in touch with us.
					</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q9">
						<p class="sub_header">Can I load my exitsing bookmarks into Chipmark?</p>
					</a>
					<p> Yes.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q10">
						<p class="sub_header">Why would I want to make my Chipmarks "public"?</p>
					</a>
					<p> Chipmark gives you the option to specify whether your bookmarks
						are "Public" or "Private". Making your bookmarks "Public" allows
						other Chipmark users to make use of them through our built-in
						searching capabilities. Also, only bookmarks marked "Public" are
						included in the "Recently Added Chipmarks" and "Random" lists. We
						encourage you to designate as 'Private' any bookmarks that others
						might find offensive.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q11">
						<p class="sub_header">What are "labels"?</p>
					</a>
					<p>
						"Labels" are another form of organizing your bookmarks - we
						currently support both a hierarchical folder structure and labels.
						Labels can currently be used in our built-in search features and
						can be used to filter your Chipmarks on the
						<a href="ViewLinks">label view</a>
						of our Website. As well, our Firefox and Internet Explorer browser
						extensions offer the ability to use specific labels to create
						toolbar links like is common in most browsers. We are continuing
						to expand our use of labels in Chipmark and they will provide
						additional bookmarking power in the future.
					</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q12">
						<p class="sub_header">What does "Error opening file for writing" mean
							when installing the Internet Explorer toolbar?</p>
					</a>
					<p> The Internet Explorer Toolbar uses a DLL file that is used by
						both Internet Explorer and Windows Explorer, so if any folders or
						browser windows are opened, you will not be able to update the
						file. Close all the folder and browser windows and press retry;
						installation should continue normally then.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q13">
						<p class="sub_header">Why is the Chipmarks bar in the Firefox extension
							blank?</p>
					</a>
					<p> To get chipmarks to show up on the chipmark toolbar in Firefox,
						right click on the chipmark you want to add (in the FireFox
						extension) and click on "properties." Check the box called
						"Toolbar", press OK, and it will appear on the bar the next time
						you refresh your Chipmarks (via the options menu of the
						extension).</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q14">
						<p class="sub_header">Where did all my Chipmarks go?</p>
					</a>
					<p>
						If you cannot display your chipmarks, the first thing to try is to
						look at recently added chipmarks through the "label view" on the
						website. You may have added a chipmark which contains "special
						characters" (non-English characters). In some cases, these
						characters break the display scripts. Edit recent chipmarks in
						label view to remove these characters, and attempt to view your
						chipmarks again. If you are still unable to view your chipmarks,
						send email to
						<a href="mailto:support@chipmark.com">contact us.</a>
					</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q15">
						<p class="sub_header">Will my account ever expire?</p>
					</a>
					<p>Not if you use it! As of January '07, we will delete any user
						who is inactive for at least a year. So, all you have to do is use
						Chipmark once every twelve months and you are okay! If your
						account gets deleted, you can always register a new account and
						import all your old bookmarks.</p>
				</td>
			</tr>
			<tr valign="top">
				<td>
					<img src="images/qa.gif" />
				</td>
				<td>
					<a name="Q16">
						<p class="sub_header">What if my question is not in the FAQ?</p>
					</a>
					<p>
						Please
						<a href="/Main?target=contact">contact</a>
						us or vist our
						<a href="http://www.chipmark.com:8082/">blog</a>
						. We are always happy to help!
					</p>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_FORGOTTEN_USERNAME_PASSWORD
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_FORGOTTEN_USERNAME_PASSWORD">
		<xsl:choose>
			<xsl:when test="xml/Result = 'SUCCESS'">
				<p>
					<font color="green">Die angeforderten Benutzerinformationen wurden
						an Ihre E-Mailadresse versendet.</font>
				</p>
			</xsl:when>
			<xsl:otherwise>
				<p>
					<span style="color:red;">
						<xsl:value-of select="xml/Result" />
					</span>
				</p>
				<p class="title">Benutzername oder Passwort vergessen?</p>
				<p class="body"> Bitte geben Sie hier die E-Mailadresse an, die zu Ihrem
					lokalen Account gehört.</p>
				<p class="body">
					You can also
					<a href="/Main?target=registration">Neues Benutzerkonto erstellen</a>
					Wenn Sie noch keines besitzen.
				</p>
				<form name="lostpassword_form" method="get" action="ForgotPassword">
					<table border="0" cellpadding="0" cellspacing="5">
						<tr>
							<td class="form_elements" align="right" valign="top">
								<input name="retrieveUsername" type="checkbox" id="retrieveUsername" />
							</td>
							<td class="form_elements" width="150" align="left" valign="top">
								Benutzername anfordern</td>
						</tr>
						<tr>
							<td class="form_elements" align="right" valign="top">
								<input name="resetPassword" type="checkbox" id="resetPassword" />
							</td>
							<td class="form_elements" width="150" align="left" valign="top">
								Passwort zurücksetzten</td>
						</tr>
						<tr>
							<td class="form_elements" width="150" align="right" valign="top">
								E-Mailadresse:</td>
							<td class="form_elements" align="left" valign="top">
								<input name="clientMail" type="text" id="username2"
									maxlength="255" />
							</td>
						</tr>
						<tr>
							<td width="150" align="right" valign="top"></td>
							<td class="form_elements" align="left" valign="top">
								<input name="submitBtn" type="submit" id="get_password"
									value="Benutzerinformation anfodern" />
							</td>
						</tr>
					</table>
				</form>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_FORGOTTEN_USERNAME
	-->
	<!--
		=====================================================================
		<xsl:template name="BODY_FORGOTTEN_USERNAME"> <xsl:choose> <xsl:when
		test="xml/Result = 'SUCCESS'"> <p> <font color="green">Your username
		has been sent to your email account.</font> </p> </xsl:when>
		<xsl:otherwise> <p> <span style="color:red;"> <xsl:value-of
		select="xml/Result"/> </span> </p> <p class="title">forgot your
		username?</p> <p class="body"> Enter your email address you used when
		you signed up for a chipmark account. </p> <p class="body"> You can
		also <a href="/Main?target=addclient">register a new account</a> if
		you don't have one already. </p> <form name="lostusername_form"
		method="post" action="ForgotUsername"> <table border="0"
		cellpadding="0" cellspacing="5"> <tr> <td class="form_elements"
		width="150" align="right" valign="top">email address:</td> <td
		class="form_elements" align="left" valign="top"> <input
		name="clientMail" type="text" id="username2" maxlength="255"/> </td>
		</tr> <tr> <td width="150" align="right" valign="top"></td> <td
		class="form_elements" align="left" valign="top"> <input
		name="submitBtn" type="submit" id="get_username" value="get username"
		/> </td> </tr> </table> </form> </xsl:otherwise> </xsl:choose>
		</xsl:template>
	-->
	<!--
		=====================================================================
	-->
	<!--
		LOGIN FORM
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="LOGIN_FORM">
		<form name="login" method="POST" action="Login">
			<table border="0" width="125" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<span style="font-size:12px;color:#AAAAAA;">Benutzername</span>
						<br />
						<input style="width: 190px" name="clientName" class="inputtext"
							type="text" id="Username" size="16" maxlength="32" />
						<br />
						<span style="font-size:12px;color:#AAAAAA;">Passwort</span>
						<br />
						<input style="width: 190px" name="clientPass" class="inputtext"
							type="password" id="Password" size="16" maximum="" />
						<br />
						<input name="keepCookie" type="checkbox" id="always" value="checkbox" />
						<span style="font-size:10px;color:#AAAAAA">Cookie speichern</span>
						<br />
						<div align="center">
							<input name="Login" type="submit" id="Login" value="Anmelden"
								style="border: 1px solid #CCCCCC; background-color: #EEEEEE; width: 190px" />
						</div>
						<div align="right">
							<a class="copyright" href="Main?target=registration">Neues Benutzerkonto anlegen</a>
							<br />
							<a class="copyright" href="Main?target=forgottenusernamepassword">Benutzernamen oder
								Passwort vergessen?</a>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_UPDATES
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_UPDATES">
		<table width="100%" cellspacing="0" cellpadding="10" align="center">
			<tr>
				<td>
					<p class="subheader">fall 2006</p>
					<p class="body">
						New Website (11/11)
						<BLOCKQUOTE> Chipmark has graduated to its third web design. The
							new design gives chipmark a more modern look and feel.
						</BLOCKQUOTE>
					</p>
					<p class="body">
						Release (11/7)
						<BLOCKQUOTE> Chipmark 2.1 has been released! With it comes support
							for unicode and various bug fixes.</BLOCKQUOTE>
					</p>
					<p class="body">
						Improved Backend (10/25)
						<BLOCKQUOTE> The chipmark team has been working hard to offer
							better and more consistent backend support for all of the
							chipmark interfaces.</BLOCKQUOTE>
					</p>
					<p class="body">
						New Team (9/1)
						<BLOCKQUOTE> A new team of students has taken over chipmark and
							will be working on it for this academic year.</BLOCKQUOTE>
					</p>
				</td>
			</tr>
		</table>
		<br />
		<table width="100%" cellspacing="0" cellpadding="10" align="center">
			<tr>
				<td>
					<p class="subheader">spring 2006</p>
					<p class="body">
						Graduation (5/10)
						<BLOCKQUOTE> The 2005 chipmark team graduated and moved on to jobs
							and graduate school. They left an indelible mark on Chipmark.
							Among other things, they changed the new web interface, added
							buddies, and imporved searching. This year's team has much to
							thank last year's group for.</BLOCKQUOTE>
					</p>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_HELP
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_HELP">
		<p class="sub_header">having a problem?</p>
		<p class="sub_content">
			Please read the
			<a href="Main?target=faqs">FAQ</a>
			section. In the FAQ we address the common problems and frustrations
			users expierence.
		</p>
		<p class="sub_content">
			Tutorial videos are also available for the following actions:
			<br />
			<ul>
				<li>
					<a class="content" href="Main?target=video_registration" title="Registering a New User">
						Registering a new user</a>
				</li>
				<li>
					<a class="content" href="Main?target=video_ffinstall" title="Installing the Firefox Extension">
						Installing the Firefox Extension</a>
				</li>
				<li>
					<a class="content" href="Main?target=video_ffimport" title="Importing with the Firefox Extension">
						Importing with the Firefox Extension</a>
				</li>
				<li>
					<a class="content" href="Main?target=video_ieinstall" title="Installing the IE Plugin">
						Installing the IE Plugin</a>
				</li>
			</ul>
		</p>
		<p class="sub_header">curious about a feature?</p>
		<p class="sub_content">
			Please read the
			<a href="Main?target=features">features</a>
			section. In the features section we list all the abilties of
			Chipmark's three interfaces. So, if you want to do something not
			listed, we have not yet added that feature.
		</p>
		<p class="sub_header">need more specfic help?</p>
		<p class="sub_content">
			Please visit the
			<a href="http://www.chipmark.com:8082/forum">forum</a>
			or the
			<a href="Main?target=contact">contact</a>
			section. In the the contact section we list four ways of
			communicating with the Chipmark team.
		</p>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_CONTACT
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_CONTACT">
		<p class="sub_header">contacting chipmark</p>
		<p class="sub_content"> Chipmark is an ever-evolving open-source project. We want
			your help making it the best bookmarking experience possible. Even if
			we can not solve your concern, we will be happy to discuss it with
			you.</p>
		<p class="sub_header">found a bug?</p>
		<p class="sub_content">
			Please contact us at the
			<a href="http://www.chipmark.com:8082/forum">forum</a>
			or
			<a href="mailto:bugs@chipmark.com">bugs@chipmark.com</a>
		</p>
		<p class="sub_header">need some help?</p>
		<p class="sub_content">
			Please contact us at the
			<a href="http://www.chipmark.com:8082/forum">forum</a>
			or
			<a href="mailto:support@chipmark.com">support@chipmark.com</a>
		</p>
		<p class="sub_header">wondering about something else?</p>
		<p class="sub_content">
			Please contact us at the
			<a href="http://www.chipmark.com:8082/forum">forum</a>
			or
			<a href="mailto:webmaster@chipmark.com">webmaster@chipmark.com</a>
		</p>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_MAIN
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_MAIN">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			align="center">
			<tr>
				<td valign="top">
					<p class="sub_header">Willkommen zum Prototypbetrieb von Chipmark
					</p>
					<p class="content">
						Benutzer mit FZKA oder IAI Account müssen sich
						<font color='red'>
							<b>nicht registrieren</b>
						</font>
						,
						<br />
						sondern könen sich direkt mit ihren Login Daten anmelden.
						<br />
						<br />
						Beispiel:
						<b>FZKA/musterman-m</b>
						oder
						<b>IAI/musterman-m</b>
					</p>
					<br />
					<br />
					<p class="sub_header">Allgemeiner Hinweis</p>
					<p class="content">chipmarks = bookmarks</p>
					<p class="content">
						Hier ist die ALPHA Version des Firefox 3 Browseradd-ons:
						<a href="https://www.chipmark.com/firefox/chipmark-3.3pre1.xpi"
							target="_blank">Download von chipmark.com</a>
						<br />
						Da es sich dabei um die unveränderte original Version der
						eigentlichen Chipmark Entwickler handelt, muss
						<b>nach</b>
						der Installation unter
						<br />
						Extras -> Add-ons -> Chipmark -> Einstellungen -> Chipmark
						<br />
						anstatt 'https://www.chipmark.com'
						'https://iwrpaul.ka.fzk.de:8433' als Chipmark Host eingetragen
						werden.
					</p>
				</td>
				<td valign="top" style="height:100%; overflow:hidden;" align="center"
					width="200">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						align="center" style="height:100%;">
						<tr>
							<td valign="top" class="front_page" height="30" width="100%"
								onMouseOver="this.style.background='lightgreen';this.style.cursor='pointer';"
								onMouseOut="this.style.background='white';" onClick="document.location.href='Main?target=tour';">
								<span class="front_title">Was bietet Chipmark?</span>
							</td>
						</tr>
						<tr>
							<td style="height:10px;">
								<img src="images/spacer.gif" height="10" />
							</td>
						</tr>
						<xsl:choose>
							<xsl:when test="not(boolean(xml/LoggedInAs)) or xml/LoggedInAs = ''">
								<tr>
									<td valign="top" class="front_page" height="30" width="100%"
										onMouseOver="this.style.background='lightgreen';this.style.cursor='pointer';"
										onMouseOut="this.style.background='white';"
										onClick="document.location.href='Main?target=registration';">
										<span class="front_title">Registrieren</span>
										<br />
										<span class="front_sub_title">Jeder kann sich anmelden!</span>
									</td>
								</tr>
							</xsl:when>
							<xsl:otherwise>
								<!-- logged in -->
							</xsl:otherwise>
						</xsl:choose>
						<tr>
							<td align="right" valign="bottom" style="vertical-align: bottom;">
								<br />
								<br />
								<br />
								<br />
								<br />
								<br />
								<br />
								<br />
								<br />
								<br />
								<img src="images/tour/web_shared.png" />
								<br />
								<img src="images/home_image.png" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_SHARING
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_SHARING">
		<p class="title">sharing your chipmarks</p>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table cellpadding="5">
						<tr>
							<td valign="top" width="275">
								<p class="subheader">sharing</p>
								<p class="body">Chipmark now makes it easier than ever to
									share webpages with family, friends, and co-workers. Similiar
									to email, chipmark allows you to send webpages to the inbox of
									other chipmark users.</p>
							</td>
							<td align="center" width="275">
								<img src="images/inbox1.JPG" />
							</td>
						</tr>
						<tr height="25">
							<td>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td align="center" valign="top" width="275">
								<img src="/images/buddies2.JPG" />
							</td>
							<td width="275" valign="top">
								<p class="subheader">buddies</p>
								<p class="body">To begin sharing with friends simply add your
									buddy's chipmark username and a nickname. You can add, delete,
									and edit your buddies from the "my buddies" page on the
									website. You can also add buddies from the firefox extension
									when you choose to send a page.</p>
							</td>
						</tr>
						<tr height="25">
							<td>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td width="275">
								<p class="subheader">send it</p>
								<p class="body">
									Sending webpages to your buddies can now be done in a few
									clicks. If you are using the
									<a href="/Main?target=downloadFirefox">Firefox extension</a>
									, or the
									<a href="/Main?target=downloadIE">Internet Explorer toolbar</a>
									, you can send pages to your buddies while you browse the
									internet! Simply choose "Send this page..." from the chipmark
									menu. In addition, from the "my chipmarks" page, you can simply
									highlight a chipmark you would like to send and choose "send
									to".
								</p>
							</td>
							<td width="275" align="center">
								<img src="/images/sendpage1.JPG" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_PRIVACY_POLICY
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_PRIVACY_POLICY">
		<p class="sub_header">our commitment to privacy</p>
		<p class="sub_content">
			Your privacy is important to us. To better protect your privacy, we
			provide this notice explaining our online information practices and
			the choices you can make about the way your information is collected
			and used. To make this notice easy to find, we make it available on
			our homepage and at every point where personally identifiable
			information may be requested. Chipmark agrees to notify you of:
			<ul class="list">
				<li>
					<span class="list_hack">What personally identifiable information of
						yours is collected by Chipmark.</span>
				</li>
				<li>
					<span class="list_hack">The organization collecting the information.
					</span>
				</li>
				<li>
					<span class="list_hack">How the information is used.</span>
				</li>
				<li>
					<span class="list_hack">With whom the information may be shared.
					</span>
				</li>
				<li>
					<span class="list_hack">The choices available regarding the collection,
						use and distribution of information.</span>
				</li>
				<li>
					<span class="list_hack">The security procedures that are in place to
						protect the loss, misuse or alteration of information under
						Chipmark's control.</span>
				</li>
				<li>
					<span class="list_hack">How you can correct any inaccuracies in the
						information.</span>
				</li>
			</ul>
		</p>
		<p class="sub_header">general</p>
		<p class="sub_content"> The privacy practices of this statement apply to our
			services available under the domain and sub-domains of
			www.chipmark.com (the &quot;Site&quot;). By visiting
			this website, you agree to be bound by the terms and conditions of this Privacy
			Policy. If you do not agree, please do not use or access our Site.
		</p>
		<p class="sub_content">
			This Privacy Policy describes the information we collect from you as part of
			the normal operation of our services and what may happen to that information.
			Although this policy may seem long, we have prepared a detailed policy because
			we believe you should know as much as possible about Chipmark's practices so
			that you can make informed decisions.
		</p>
		<p class="sub_content">
			By accepting the Privacy Policy and the User Agreement in registration, you
			expressly consent to our use and disclosure of your anonymous information in
			accordance with this Privacy Policy. Chipmark will never release your personal
			information to anyone for any reason. This Privacy Policy is incorporated into
			and subject to the terms of the Chipmark User Agreement. This Privacy Policy
			is effective upon acceptance in registration for new registering users, and
			is otherwise effective on October 31st, 2004 for all users. The previous amendment
			to this Privacy Policy was effective for all users on October 31st, 2004.
		</p>

		<p class="sub_header">definitions</p>
		<p class="sub_content">
			Information collected online is usually defined as being either anonymous or
			personally identifiable.
		</p>
		<p class="sub_content">
			Anonymous Information refers to data that cannot be tied back to a specific
			individual. For example, Chipmark collects some information each time a visitor
			comes to Chipmark's site, so we can improve the overall quality of the visitor's
			online experience. Chipmark collects the visitor's IP address (i.e., whether
			the user is logged on from ibm.com or aol.com), referral data (i.e., the Internet
			address of the last Web page visited by a user before clicking over to the Chipmark
			site), and browser and platform type (e.g., a Netscape browser on a Macintosh
			platform). You do not have to register with Chipmark before we can collect this
			anonymous information. Chipmark's collection of bookmark information (e.g. url,
			name, description, hit count, etc.) is also considered anonymous information.
		</p>
		<p class="sub_content">
			Personally Identifiable Information refers to data that tells us specifically
			who you are, such as your name and postal address, which may be collected in
			addition to the anonymous data described in the above paragraph.
		</p>

		<p class="sub_header">cookies</p>
		<p class="sub_content">
			A cookie is a small amount of data, which often includes an anonymous unique
			identifier, that is sent to your browser from a website's computers and stored
			on your computer's hard drive.
		</p>
		<p class="sub_content">
			Each website can send its own cookie to your browser if your browser's preferences
			allow it, but (to protect your privacy) your browser only permits a website
			to access the cookies it has already sent to you, not the cookies sent to you
			by other sites.
		</p>

		<p class="sub_header">choices about cookies</p>
		<p class="sub_content">
			You can configure your browser to accept all cookies, reject all cookies, or
			notify you when a cookie is set. (Each browser is different, so check the &quot;Help&quot;
			menu of your browser to learn how to change your cookie preferences.)
		</p>
		<p class="sub_content">
			If you reject all cookies, you will not be able to use Chipmark products or
			services that require you to &quot;sign in,&quot; and you may not be able to
			take full advantage of all offerings. However, many Chipmark products and services
			do not require that you accept cookies.
		</p>
		<p class="sub_header">chipmark's practices regarding cookies</p>
		<p class="sub_content">
			Chipmark uses its own cookies in order to:<br/>
			Maintain your login information if you select the auto-login feature.
		</p>

		<p class="sub_header">the information we collect</p>
		<p class="sub_content">
			This notice applies to all information collected or submitted on the Chipmark
			website. On some pages, you can register for our Web bookmarks service. The
			types of personal information collected at these pages are: <br/>
			Email address<br/>
			A valid e-mail address is required at resistration for the purpose of retrieving 
			your password should you forget it. On some pages, you can submit Web bookmark 
			information. Under this circumstance, no personal information is collected.
		</p>

		<p class="sub_header">the way we use information</p>
		<p class="sub_content">
			We use non-identifying and aggregate information to better design our website
			and to share with researchers. For example, we may tell a researcher that X
			number of individuals visited a certain website, or that Y number of users filled
			out our registration form, but we would not disclose anything that could be
			used to identify those individuals.
		</p>
		<p class="sub_content">
			We use your email address provided during registration only to contact you
			about important information related to Chipmark or to send you a lost password.
			Currently we don't disseminate information by email. Should we ever decide to do this
            you will have the option to receive emails or decline them.
		</p>
		<p class="sub_content">
			We use return email addresses to answer the email we receive. Such addresses
			are not used for any other purpose and are not shared with outside parties.
		</p>
		<p class="sub_content">
			Finally, we never use or share the personally identifiable information provided
			to us online in ways unrelated to the ones described above without also providing
			you an opportunity to opt-out or otherwise prohibit such unrelated uses.
		</p>

		<p class="sub_header">our commitment to children's privacy</p>
		<p class="sub_content">
			Protecting the privacy of the very young is especially important. For that
			reason, we never collect or maintain information at our website from those we
			actually know are under 13, and no part of our website is structured to attract
			anyone under 13.
		</p>

		<p class="sub_header">our commitment to data security</p>
		<p class="sub_content">
			To prevent unauthorized access, maintain data accuracy, and ensure the correct
			use of information, we have put in place appropriate physical, electronic, and
			managerial procedures to safeguard and secure the information we collect online.
		</p>

		<p class="sub_header">use of email tools</p>
		<p class="sub_content">
			You may not use the "Email This Item to a Friend" service or other email services
			that we offer to send spam or otherwise send content that would violate our
			User Agreement. We do not permanently store email messages or the email addresses
			sent from these tools.
		</p>
		<p class="sub_content">
			If you send and email using the Email This Item to a Friend service to an email
			address that is not registered in our community, we do not use that email address
			for any purpose other than to send your email. We do not rent or sell these
			email addresses.
		</p>

		<p class="sub_header">use of other user's information</p>
		<p class="sub_content">
			In order to facilitate Web browsing, Chipmark allows you limited access to
			other users' Web bookmark information. As a member, you do not have access to
			other user's personal information.
		</p>
		<p class="sub_content">
			By entering into our User Agreement, you agree that, with respect to other users'
			anonymous information that you obtain through the Site, Chipmark hereby grants
			to you a license to use such information only for: (a)Web browsing.
		</p>

		<p class="sub_header">control of your password</p>
		<p class="sub_content">
			You are responsible for all actions taken with your user id and password. Therefore,
			we do not recommend that you disclose your Chipmark password to any third parties.
			If you choose to share your user id and password or your personal information
			with third parties, you are responsible for all actions taken with your account.
			If you lose control of your password, you may lose substantial control over
			your personal information and may be subject to legally binding actions taken
			on your behalf. Therefore, if your password has been compromised for any reason,
			you should immediately change your password in the account section of your members
			area.
		</p>

		<p class="sub_header">how you can access or correct your information</p>
		<p class="sub_content">
			You can access all your personally identifiable information that we collect
			online and maintain by visiting the account area of your member section . We
			use this procedure to better safeguard your information.
		</p>
		<p class="sub_content">
			You can correct errors in your personally identifiable information by visiting
			the account section of your members area.
		</p>
		<p class="sub_content">
			To protect your privacy and security, we will also take reasonable steps to
			verify your identity before granting access or making corrections.
		</p>

		<p class="sub_header">changes to privacy policy</p>
		<p class="sub_content">
			We may amend this Privacy Policy at any time by posting the amended terms on the
			Site. All amended terms shall automatically be effective 30 days after they
			are initially posted on the Site. In addition, we will notify you by email if
			you have supplied an email address during registration and have not opted out
			of our informational mailing list.
		</p>

		<p class="sub_header">how to contact us</p>
		<p class="sub_content">
			Should you have other questions or concerns about these privacy policies, please
			send us an email at <a href="mailto:privacy@chipmark.com">privacy@chipmark.com</a>.
		</p>
	</xsl:template>


	<!-- ===================================================================== -->
	<!-- BODY_UNDER_CONSTRUCTION                                               -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_UNDER_CONSTRUCTION">

		<table width="100%" cellspacing="0" cellpadding="0">
			<tr>
				<td align="center" valign="middle">
					<p class="subheader">sorry, this page is under construction.</p>
				</td>
			</tr>
		</table>

	</xsl:template>


	<!-- ===================================================================== -->
	<!-- BODY_HOWTO_ENABLE_JAVASCRIPT                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_HOWTO_ENABLE_JAVASCRIPT">
		<span class="sub_header">Firefox</span>
		<ol class="sub_content">
			<li>From the tools menu, select Options</li>
			<li>Click the Content tab with the Earth graphic.</li>
			<li>Check Enable JavaScript.</li>
			<li>Click OK.</li>
			<li>Reload the page by either clicking Reload or by hitting F5 on the keyboard.</li>
		</ol>
		<span class="sub_header">Internet Explorer</span>
		<ol class="sub_content">
			<li>Open Internet Explorer.</li>
			<li>
				Open the Internet Options dialog.
				<ol>
					<li>
						For Internet Explorer 7, Click on the Tools button which is a picture of the gear)
						or “Tools” from the program menu.
					</li>
					<li>For Internet Explorer 5.X and 6.X, click on Internet Options under the tools Menu</li>
				</ol>
			</li>
			<li>Click on Internet Options. </li>
			<li>Click the Security tab.</li>
			<li>Click on Custom level.</li>
			<li>Scroll down toward the bottum until you see Scripting.</li>
			<li>Enable active scripting. </li>
			<li>Click OK two time to close the dialogues. </li>
			<li>Reload the page by either clicking Refresh or by hitting F5 on the keyboard.</li>
		</ol>

	</xsl:template>





	<!-- ===================================================================== -->
	<!-- BODY_TOP_10                                                           -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_TOP_10">
		<p class="subheader">top 10 chipmarks</p>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>

					<!-- start bookmark list -->
					<xsl:for-each select="xml/BookmarkList/Bookmark">
						<xsl:sort order="descending" select="HitCountToSortBy"/>

						<p>
							<table border="0">
								<tr>
									<td valign="top" width="20" align="right">
										<span class="body" style="font-weight:bold;">
											<xsl:value-of select="Rank"/>.
										</span>
									</td>

									<td>

										<span class="body">
											<!-- Insert Link -->
											<xsl:element name="a">
												<xsl:attribute name="href">
													<xsl:value-of select="Link" />
												</xsl:attribute>
												<xsl:apply-templates select="Link" mode="addBreaks" />
											</xsl:element>
										</span>

										<span class="body">
											bookmarked by <xsl:value-of select="HitCount"/> users.
										</span>
										<table border="0">
											<tr>
												<td>
													<div style="background-image: url(images/rating_off.png); background-repeat: no-repeat; width:80px; height:16px; overflow:hidden;">
														<xsl:element name="div">
															<xsl:attribute name="style">
																background-image: url(images/rating_on.png); background-repeat: no-repeat; width: <xsl:value-of select="Percentage"/>%; height: 16px;
															</xsl:attribute>
														</xsl:element>
													</div>
												</td>
												<td>
													<span class="body">
														<xsl:value-of select="Percentage"/>%
													</span>
													<xsl:if test="boolean(LoggedIn)">
														<xsl:element name="a">
															<xsl:attribute name="href">
																/AddLink?submitBtn=prefilled&amp;linkName=<xsl:value-of select="EncodedName"/>&amp;linkURL=<xsl:value-of select="EncodedLink"/>
															</xsl:attribute>
															<xsl:attribute name="title">
																Add This Link
															</xsl:attribute>
															<xsl:attribute name="style">font-size: smaller;</xsl:attribute>
															<img src="images/add_link_15.png" border="0"/>
														</xsl:element>
													</xsl:if>
												</td>
											</tr>

										</table>

									</td>
								</tr>

							</table>
						</p>

					</xsl:for-each>
					<!-- end bookmark list -->

				</td>

			</tr>
		</table>

	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_MOST_RECENTLY_ADDED                                              -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_MOST_RECENTLY_ADDED">

		<p class="title">recently added chipmarks</p>
		<table width="100%" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td>
					<xsl:for-each select="xml/BookmarkList/Bookmark">
						<p>
							<span class="body">
								<!--<xsl:value-of select="Client"/><xsl:text> added </xsl:text>-->
								<xsl:element name="a">
									<xsl:attribute name="href">
										<xsl:value-of select="URL"/>
									</xsl:attribute>
									<xsl:apply-templates select="Title" mode="addBreaksString" />
								</xsl:element> was added at <xsl:value-of select="Date"/>
							</span>
							<br />
							<span class="url">
								<xsl:apply-templates select="URL" mode="addBreaks" />
							</span>
							<xsl:if test="boolean(LoggedIn)">
								<span class="body">
									<xsl:text> </xsl:text>
									<xsl:element name="a">
										<xsl:attribute name="href">
											/AddLink?submitBtn=prefilled&amp;linkName=<xsl:value-of select="EncodedName"/>&amp;linkURL=<xsl:value-of select="EncodedLink"/>
										</xsl:attribute>
										<xsl:attribute name="style">font-size: smaller;</xsl:attribute>[Add]
									</xsl:element>
								</span>
							</xsl:if>
						</p>
					</xsl:for-each>
				</td>
			</tr>
		</table>

	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_RECOMMENDER                                             -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_RECOMMENDER">

		<p class="title">recommended chipmarks</p>
		<table width="100%" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td>
					<xsl:for-each select="xml/BookmarkList/Bookmark">
						<p>
							<span class="body">
								<!--<xsl:value-of select="Client"/><xsl:text> added </xsl:text>-->
								<xsl:element name="a">
									<xsl:attribute name="href">
										<xsl:value-of select="URL"/>
									</xsl:attribute>
									<xsl:apply-templates select="Title" mode="addBreaksString" />
								</xsl:element>
							</span>
							<br />
							<span class="url">
								<xsl:apply-templates select="URL" mode="addBreaks" />
							</span>
							<xsl:if test="boolean(LoggedIn)">
								<span class="body">
									<xsl:text> </xsl:text>
									<xsl:element name="a">
										<xsl:attribute name="href">
											/AddLink?submitBtn=prefilled&amp;linkName=<xsl:value-of select="EncodedName"/>&amp;linkURL=<xsl:value-of select="EncodedLink"/>
										</xsl:attribute>
										<xsl:attribute name="style">font-size: smaller;</xsl:attribute>[Add]
									</xsl:element>
								</span>
							</xsl:if>
						</p>
					</xsl:for-each>
				</td>
			</tr>
		</table>

	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_IMPORT_SUCCESS                                                   -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_IMPORT_SUCCESS">
		imported successfully
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_IMPORT_ERROR                                                     -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_IMPORT_ERROR">
		import failed
		<BR/>
		<xsl:choose>
			<xsl:when test="xml/Result!='SUCCESS'">
				<font color="red">
					<xsl:value-of select="xml/Result"/>
				</font>
			</xsl:when>
			<xsl:otherwise>
				Something strange has happened.  If you're seeing this message, please report
				it, along with a detailed description of what you were doing prior to seeing
				it to us <a href="?target=contact">here</a>.
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_ERROR                                                            -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_ERROR">
		<p class="title">error</p>
		<xsl:choose>
			<xsl:when test="xml/Result!='SUCCESS'">
				<font color="red">
					<xsl:value-of select="xml/Result"/>
				</font>
			</xsl:when>
			<xsl:otherwise>
				Something strange has happened.  If you're seeing this message, please report
				it, along with a detailed description of what you were doing prior to seeing
				it to us <a href="?target=contact">here</a>.
			</xsl:otherwise>
		</xsl:choose>
		<br/><br/>
		If you feel you are seeing this message when you shouldn't be, please submit a
		bug report to us <a href="/Main?target=contact">here</a>.
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_FORGOT_USERNAME_PASSWORD (result)                                -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_FORGOT_USERNAME_PASSWORD">
		forgot username password result page goes here
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_VIEW_LINKS                                                       -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_VIEW_LINKS">
		user label view goes here
	</xsl:template>


	<!-- ===================================================================== -->
	<!-- BODY_BROWSE                                                           -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_BROWSE">
		if possible, it would nice to have a way to browse all public chipmarks
		(by label) here.
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API                                                       -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API">
		<p class="sub_header">chipmark API</p>
		<p class="sub_content">Chipmark now allows all registered users to make remote procedure calls via the Chipmark API. Users are free to use the API to develop their own modifications as long as they stay with in the bounds of Chipmark's license.  The following methods are supported by a REST interface.</p>
		<ul>
			<li>
				Authentication
				<ul>
					<li>
						<a href="/Main?target=api_login">Login</a>
					</li>
					<li>
						<a href="/Main?target=api_logout">Logout</a>
					</li>
				</ul>
			</li>
			<li>
				Bookmarks
				<ul>
					<li>
						<a href="/Main?target=api_addlink">AddLink</a>
					</li>
					<li>
						<a href="/Main?target=api_deletelink">DeleteLink</a>
					</li>
					<li>
						<a href="/Main?target=api_editlink">EditLink</a>
					</li>
					<li>
						<a href="/Main?target=api_get">GetFoldersAndLinks</a>
					</li>
					<li>
						<a href="/Main?target=api_random">Random</a>
					</li>
					<li>
						<a href="/Main?target=api_toptenbookmarked">Top10Bookmarked</a>
					</li>
				</ul>
			</li>
			<li>
				Buddies
				<ul>
					<li>
						<a href="/Main?target=api_buddylist_add">Buddylist::Add</a>
					</li>
					<li>
						<a href="/Main?target=api_buddylist_display">Buddylist::Display</a>
					</li>
					<li>
						<a href="/Main?target=api_buddylist_edit">Buddylist::Edit</a>
					</li>
					<li>
						<a href="/Main?target=api_buddylist_getbuddyrequests">Buddylist::GetBuddyRequests</a>
					</li>
					<li>
						<a href="/Main?target=api_buddylist_remove">Buddylist::Remove</a>
					</li>
					<li>
						<a href="/Main?target=api_buddylist_setbuddyrequests">Buddylist::SetBuddyRequests</a>
					</li>
					<li>
						<a href="/Main?target=api_buddylist_viewpendingbuddyrequests">Buddylist::ViewPendingBuddyRequests</a>
					</li>
					<li>
						<a href="/Main?target=api_buddylist_viewtransitivebuddies">Buddylist::ViewTransitiveBuddies</a>
					</li>
				</ul>
			</li>
		</ul>
	</xsl:template>



	<!-- ===================================================================== -->
	<!-- BODY_API_LOGIN                                                       -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_LOGIN">
		<p class="sub_header">login</p>
		<p class="sub_content">This will log a registered user into Chipmark. Login must be called before using any of the other API methods.</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			<ol>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext </li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>

				<li>
					clientName
					<ul>
						<li>type: string</li>
						<li>value: any registered user's name</li>
						<li>comment: this is how the username is sent to the server</li>
					</ul>
				</li>
				<li>
					clientPass
					<ul>
						<li>type: string</li>
						<li>value: the above user's password </li>
						<li>comment: this is how the password is sent to the server</li>
					</ul>
				</li>
				<li>
					keepCookie
					<ul>
						<li>type: string</li>
						<li>value: yes/no</li>
						<li>comment: this parameter allows you to choose whether or not a cookie shall be returned in the response. In either case, the session is maintained internally by Chipmark</li>
					</ul>
				</li>
				<li>
					encrypted
					<ul>
						<li>type: string</li>
						<li>value: yes/no</li>
						<li>comment: specify whether or not the 'clientPass' parameter is sent in MD5 hash or not</li>
					</ul>
				</li>
			</ol>


		</p>
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com&#8203;/Login?agent=ext&amp;clientName=&#8203;username&amp;client&#8203;Pass=password&amp;keepCookie=no&amp;&#8203;encrypted=no</p>

		<p class="sub_header">response </p>
		<ol>
			<li>
				success: if a correct username/password combination was provided, the username will be returned in the response. <a href="/xml/LoginSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: if an incorrect username/password combination was provided or the parameters were not constructed properly, there will be no username in the response. <a href="/xml/LoginFail.xml">Sample</a>.
			</li>
		</ol>
	</xsl:template>


	<!-- ===================================================================== -->
	<!-- BODY_API_LOGOUT                                                      -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_LOGOUT">
		<p class="sub_header">logout</p>
		<p class="sub_content">This will end your Chipmark session and invalidate any Chipmark cookies.</p>

		<p class="sub_header">parameters (by name)</p>
		<p class="sub_content">None.</p>
		<p class="sub_header">response</p>
		<p class="sub_content">None.</p>
		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/Logout</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_GET                                                      -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_GET">
		<p class="sub_header">GetFoldersAndLinks</p>
		<p class="sub_content">This will return all information regarding your Chipmarks including labels and folders.</p>

		<p class="sub_header">parameters (by name)</p>
		<p class="sub_content">None.  Note, this only works when you are logged in.</p>
		<p class="sub_header">response</p>

		<ol>
			<li>
				success: The 'Result' tags will contain the words 'SUCCESS'. There will be a list of labels, followed by a list of bookmarks, followed by a list of folders. The bookmarks contain their enclosing folder id's. The folders are listed with their parent-folder id's. The root folder is named 'Your Chipmarks' and has no parent. <a href="/xml/GetFoldersAndLinksSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'Result' tags will contain the words 'FAILURE'. <a href="/xml/GetFoldersAndLinksFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/GetFoldersAndLinks</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_ADDLINK                                                      -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_ADDLINK">
		<p class="sub_header">AddLink</p>
		<p class="sub_content">Adds a chipmark</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
				<li>
					labelNames
					<ul>
						<li>type: string</li>
						<li>value: Labels to apply to the link</li>
						<li>comment: Separate each label with comma (i.e. Technology,Hobbies,Electronics)</li>
					</ul>
				</li>
				<li>
					linkDescription
					<ul>
						<li>type: string</li>
						<li>value: Description of the link.</li>
						<li>comment: this is a description of the link to be stored</li>
					</ul>
				</li>
				<li>
					linkName
					<ul>
						<li>type: string</li>
						<li>value: title of the link</li>
						<li>comment: this is the name the link will be aliased as</li>
					</ul>
				</li>
				<li>
					linkPermission
					<ul>
						<li>type: string</li>
						<li>value: public / private</li>
						<li>comment: public links are available to anyone, private links are only visible to the user who adds them</li>
					</ul>
				</li>
				<li>
					linkURL
					<ul>
						<li>type: string</li>
						<li>value: URL of the link</li>
						<li>comment: this is the link to be stored</li>
					</ul>
				</li>
			</ol>
		</p>

		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'LoggedInAs' tag will contain the logged in user's username. There will be a list of all used labels followed by a list of bookmarks.
				<a href="/xml/AddLinkSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'result/status' tag will contain the words 'FAILURE'. There will also be a 'result/message' tag with a description of the error.
				<a href="/xml/AddLinkFail.xml">Sample</a>.
			</li>
		</ol>
		
		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/AddLink</p>

		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/AddLink?agent=ext&amp;&#8203;linkName=Chipmark&amp;&#8203;linkURL=www%2Echipmark%2Ecom&amp;&#8203;linkPermission=public&amp;&#8203;linkDescription=Chipmark+Website&amp;&#8203;labelNames=Technology%2CUtilities</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_DELETELINK                                                   -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_DELETELINK">
		<p class="sub_header">DeleteLink</p>
		<p class="sub_content">Note, this only works when you are logged in.</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
				<li>
					delete
					<ul>
						<li>type: string</li>
						<li>value: set / all</li>
						<li>comment: all will delete all chipmarks. Set will delete only a set of chipmarks.</li>
					</ul>
				</li>
				<li>
					linkID
					<ul>
						<li>type: integer</li>
						<li>value: linkID of a chipmark to delete</li>
						<li>comment: to delete multiple links, specify this parameter multiple times</li>
					</ul>
				</li>
			</ol>
		</p>

		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'Result' tag will contain the words 'SUCCESS'.
				<a href="/xml/DeleteLinkSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'Result' tag will contain the words 'FAILURE'. There will also be a 'Message' tag with a description of the error.
				<a href="/xml/DeleteLinkFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/DeleteLink</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/DeleteLink?agent=ext&amp;&#8203;delete=set&amp;&#8203;linkID=12345&amp;&#8203;linkID=12346</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_TOPTENBOOKMARKED                                             -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_TOPTENBOOKMARKED">
		<p class="sub_header">Top10Bookmarked</p>
		<p class="sub_content">Returns a list of the top ten chipmarks</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			<ol>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
			</ol>
		</p>

		<p class="sub_header">response</p>
		<ol>
			<li>
				The 'LoggedInAs' tag will contain the logged in user's username. A list of the top ten chipmarks follows.
				<a href="/xml/Top10BookmarkedSuccess.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/Top10Bookmarked</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/Top10Bookmarked?agent=ext</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_RANDOM                                                       -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_RANDOM">
		<p class="sub_header">Random</p>
		<p class="sub_content">Returns a random chipmark. If your safe browsing preference is toggled on, potentially offensive chipmarks will be not be returned.</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			None
		</p>
		
		<p class="sub_header">response</p>
		<ol>
			<li>
				The 'LoggedInAs' tag will contain the logged in user's username. The 'Result' tag will contain the word 'SUCCESS'. A single random bookmark will follow with several tags describing it.
				<a href="/xml/RandomSuccess.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/Random</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/Random</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_EDITLINK                                                     -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_EDITLINK">
		<p class="sub_header">EditLink</p>
		<p class="sub_content">Edits a chipmark</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
				<li>
					labelNames
					<ul>
						<li>type: string</li>
						<li>value: Labels to apply to the link, old labels will be removed</li>
						<li>comment: Separate each label with comma (i.e. Technology,Hobbies,Electronics)</li>
					</ul>
				</li>
				<li>
					linkDescription
					<ul>
						<li>type: string</li>
						<li>value: Description of the link.</li>
						<li>comment: this is a description of the link to be stored</li>
					</ul>
				</li>
				<li>
					linkID
					<ul>
						<li>type: integer</li>
						<li>value: link ID</li>
						<li>comment: this is link ID of the link to the changed</li>
					</ul>
				</li>
				<li>
					linkName
					<ul>
						<li>type: string</li>
						<li>value: title of the link</li>
						<li>comment: this is the name the link will be aliased as</li>
					</ul>
				</li>
				<li>
					linkPermission
					<ul>
						<li>type: string</li>
						<li>value: public / private</li>
						<li>comment: public links are available to anyone, private links are only visible to the user who adds them</li>
					</ul>
				</li>
				<li>
					linkURL
					<ul>
						<li>type: string</li>
						<li>value: URL of the link</li>
						<li>comment: this is the link to be stored</li>
					</ul>
				</li>
				<li>
					submitBtn
					<ul>
						<li>type: string</li>
						<li>value: save_chipmark</li>
						<li>comment: this is used to keep the old linkID intact.</li>
					</ul>
				</li>
			</ol>
		</p>	

		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'LoggedInAs' tag will contain the logged in user's username. There will be a list of all used labels followed by a list of bookmarks.
				<a href="/xml/EditLinkSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'result/status' tag will contain the words 'FAILURE'. There will also be a 'result/message' tag with a description of the error.
				<a href="/xml/EditLinkFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/EditLink</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/EditLink?agent=ext&amp;&#8203;linkID=12345&amp;&#8203;linkName=Chipmark&amp;&#8203;linkURL=www%2Echipmark%2Ecom&amp;&#8203;linkPermission=public&amp;&#8203;linkDescription=Chipmark+Website&amp;&#8203;labelNames=Technology%2CUtilities&amp;&#8203;submitBtn=save_chipmark</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_BUDDYLIST_DISPLAY                                            -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_BUDDYLIST_DISPLAY">
		<p class="sub_header">BuddyList::Display</p>
		<p class="sub_content">Returns a list of your buddies</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					action
					<ul>
						<li>type: string</li>
						<li>value: display</li>
						<li>comment: this is name of the buddylist action that will be performed</li>
					</ul>
				</li>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
			</ol>
		</p>

		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'result/status' tag will contain the words 'SUCCESS'. A buddy list consisting of usernames and nicknames follows.
				<a href="/xml/BuddyList_DisplaySuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'Result' tag will contain the words 'FAILURE'. There will also be a 'Message' tag with a description of the error.
				<a href="/xml/BuddyList_DisplayFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList?agent=ext&amp;&#8203;action=display</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_BUDDYLIST_ADD                                                -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_BUDDYLIST_ADD">
		<p class="sub_header">BuddyList::Add</p>
		<p class="sub_content">Adds a buddy</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					action
					<ul>
						<li>type: string</li>
						<li>value: add</li>
						<li>comment: this is name of the buddylist action that will be performed</li>
					</ul>
				</li>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
				<li>
					name
					<ul>
						<li>type: string</li>
						<li>value: username</li>
						<li>comment: this is the Chipmark username of the buddy</li>
					</ul>
				</li>
				<li>
					nick
					<ul>
						<li>type: string</li>
						<li>value: nickname (optional)</li>
						<li>comment: the buddy will appear as this nickname</li>
					</ul>
				</li>
			</ol>
		</p>
		
		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'result/status' tag will contain the words 'SUCCESS'.
				<a href="/xml/BuddyList_AddSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'result/status' tag will contain the words 'FAILURE'. There will also be a 'result/message' tag with a description of the error.
				<a href="/xml/BuddyList_AddFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList?agent=ext&amp;&#8203;action=add&amp;&#8203;name=username&amp;&#8203;nick=bestfriend</p>
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- BODY_API_BUDDYLIST_REMOVE                                             -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_BUDDYLIST_REMOVE">
		<p class="sub_header">BuddyList::Remove</p>
		<p class="sub_content">Removes a buddy</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					action
					<ul>
						<li>type: string</li>
						<li>value: remove</li>
						<li>comment: this is name of the buddylist action that will be performed</li>
					</ul>
				</li>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
				<li>
					name
					<ul>
						<li>type: string</li>
						<li>value: username</li>
						<li>comment: this is the Chipmark username of the buddy to remove</li>
					</ul>
				</li>
			</ol>
		</p>
		
		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'result/status' tag will contain the words 'SUCCESS'.
				<a href="/xml/BuddyList_RemoveSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'result/status' tag will contain the words 'FAILURE'. There will also be a 'result/message' tag with a description of the error.
				<a href="/xml/BuddyList_RemoveFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList?agent=ext&amp;&#8203;action=remove&amp;&#8203;name=username</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_BUDDYLIST_EDIT                                               -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_BUDDYLIST_EDIT">
		<p class="sub_header">BuddyList::Edit</p>
		<p class="sub_content">Edit a buddy's nickname</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					action
					<ul>
						<li>type: string</li>
						<li>value: edit</li>
						<li>comment: this is name of the buddylist action that will be performed</li>
					</ul>
				</li>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
				<li>
					name
					<ul>
						<li>type: string</li>
						<li>value: username</li>
						<li>comment: this is the Chipmark username of the buddy</li>
					</ul>
				</li>
				<li>
					nick
					<ul>
						<li>type: string</li>
						<li>value: nickname</li>
						<li>comment: the buddy will appear as this nickname, replacing any previous nicknames</li>
					</ul>
				</li>
			</ol>
		</p>
		
		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'result/status' tag will contain the words 'SUCCESS'.
				<a href="/xml/BuddyList_EditSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'result/status' tag will contain the words 'FAILURE'. There will also be a 'result/message' tag with a description of the error.
				<a href="/xml/BuddyList_EditFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList?agent=ext&amp;&#8203;action=edit&amp;&#8203;name=username&amp;&#8203;nickname=oldbestfriend</p>
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- BODY_API_BUDDYLIST_GETBUDDYREQUESTS                                                     -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_BUDDYLIST_GETBUDDYREQUESTS">
		<p class="sub_header">BuddyList::GetBuddyRequests</p>
		<p class="sub_content">Returns a list of your pending buddy requests from other users</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					action
					<ul>
						<li>type: string</li>
						<li>value: getBuddyRequests</li>
						<li>comment: this is name of the buddylist action that will be performed</li>
					</ul>
				</li>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
			</ol>
		</p>

		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'result' tag will contain the words 'SUCCESS'. A list of buddies that are waiting to be accepted or declined follows.
				<a href="/xml/BuddyList_GetBuddyRequestsSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'Result' tag will contain the words 'FAILURE'. There will also be a 'Message' tag with a description of the error.
				<a href="/xml/BuddyList_GetBuddyRequestsFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList?agent=ext&amp;&#8203;action=getBuddyRequests</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_BUDDYLIST_SETBUDDYREQUESTS                                                     -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_BUDDYLIST_SETBUDDYREQUESTS">
		<p class="sub_header">BuddyList::SetBuddyRequests</p>
		<p class="sub_content">Accepts or rejects a buddy request</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					Accepted
					<ul>
						<li>type: integer</li>
						<li>value: 0 / 1</li>
						<li>comment: 0 to reject, 1 to accept the user as a buddy</li>
					</ul>
				</li>
				<li>
					action
					<ul>
						<li>type: string</li>
						<li>value: setBuddyRequests</li>
						<li>comment: this is name of the buddylist action that will be performed</li>
					</ul>
				</li>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
				<li>
					UserName
					<ul>
						<li>type: string</li>
						<li>value: username</li>
						<li>comment: this is the Chipmark username of the buddy to accept</li>
					</ul>
				</li>
			</ol>
		</p>
	
		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'result/status' tag will contain the words 'SUCCESS'.
				<a href="/xml/BuddyList_SetBuddyRequestsSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'result/status' tag will contain the words 'FAILURE'. There will also be a 'result/message' tag with a description of the error.
				<a href="/xml/BuddyList_SetBuddyRequestsFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList?agent=ext&amp;&#8203;action=setBuddyRequests&amp;&#8203;UserName=username&amp;&#8203;Accepted=1</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BODY_API_BUDDYLIST_VIEWPENDINGBUDDYREQUESTS                                                     -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_BUDDYLIST_VIEWPENDINGBUDDYREQUESTS">
		<p class="sub_header">BuddyList::ViewPendingBuddyRequests</p>
		<p class="sub_content">Returns a list of pending buddy requests you have made to other people</p>
	
		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					action
					<ul>
						<li>type: string</li>
						<li>value: viewPendingBuddyRequests</li>
						<li>comment: this is name of the buddylist action that will be performed</li>
					</ul>
				</li>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
			</ol>
		</p>
			
		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'Result' tag will contain the words 'SUCCESS'. A list of buddies who have not yet accepted or declined your request follows.
				<a href="/xml/BuddyList_ViewPendingBuddyRequestsSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'Result' tag will contain the words 'FAILURE'. There will also be a 'Message' tag with a description of the error.
				<a href="/xml/BuddyList_ViewPendingBuddyRequestsFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList?agent=ext&amp;&#8203;action=viewPendingBuddyRequests</p>
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- BODY_API_BUDDYLIST_VIEWTRANSITIVEBUDDIES                                                     -->
	<!-- ===================================================================== -->
	<xsl:template name="BODY_API_BUDDYLIST_VIEWTRANSITIVEBUDDIES">
		<p class="sub_header">BuddyList::ViewTransitiveBuddies</p>
		<p class="sub_content">Returns a list of buddies you have in common with another user</p>

		<p class="sub_header">parameters (by name)</p>
		<p>
			Note, this only works when you are logged in.
			<ol>
				<li>
					action
					<ul>
						<li>type: string</li>
						<li>value: viewTransitiveBuddies</li>
						<li>comment: this is name of the buddylist action that will be performed</li>
					</ul>
				</li>
				<li>
					agent
					<ul>
						<li>type: string</li>
						<li>value: ext</li>
						<li>comment: when agent is set to 'ext' the xml will be returned in its original format. There will be no XSL processing.</li>
					</ul>
				</li>
				<li>
					name
					<ul>
						<li>type: string</li>
						<li>value: username</li>
						<li>comment: this is the Chipmark username of the buddy to check for transitive buddies with</li>
					</ul>
				</li>
			</ol>
		</p>
	
		<p class="sub_header">response</p>
		<ol>
			<li>
				success: The 'result/status' tag will contain the words 'SUCCESS'. A list of common buddy usernames and nicknames will follow.
				<a href="/xml/BuddyList_ViewTransitiveBuddiesSuccess.xml">Sample</a>.
			</li>
			<li>
				failure: The 'result/status' tag will contain the words 'FAILURE'. There will also be a 'result/message' tag with a description of the error.
				<a href="/xml/BuddyList_ViewTransitiveBuddiesFail.xml">Sample</a>.
			</li>
		</ol>

		<p class="sub_header">url</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList</p>
		
		<p class="sub_header">example</p>
		<p class="sub_content">https://www.chipmark.com/BuddyList?agent=ext&amp;&#8203;action=viewTransitiveBuddies&amp;&#8203;name=username</p>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- FACEBOOK_LOGIN		                                                   -->
	<!-- ===================================================================== -->
	<xsl:template name="FACEBOOK_LOGIN">
      	<html>
			<!-- valid XHTML 1.0 strict -->
			<head>
				<!-- title -->
				<xsl:call-template name="MAIN_TITLE" />

				<!-- links -->
				<link rev="made" href="mailto:support@chipmark.com" />
				<link rev="start" href="./" title="chipmark.com" />

				<!-- metatags -->
				<xsl:call-template name="META_TAGS" />

				<!-- common head elements (javascript, css import, etc) -->
				<xsl:call-template name="COMMON_HEAD_ELEMENTS" />
			</head>
			<body>
				<div style="background:#FFFFFF; height: 100%; width: 100%;">
						<br/><br/><br/>						
						<table style="table-layout: fixed;" width="200" cellspacing="0" cellpadding="0" align="center">
						<tr>
							<td class="module_title_left">
								<img src="images/spacer.gif"/>
							</td>
							<td class="module_title_mid">
								<div style="width: 190px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
											Chipmark Anmeldung
								</div>
							</td>
							<td class="module_title_right">
								<img src="images/spacer.gif"/>
							</td>
						</tr>
						<tr>
							<td class="module_frame_content_left">
								<img src="images/spacer.gif"/>
							</td>
							<td class="module_frame_content_mid">
											<form name="login" id="loginForm" method="POST" onsubmit="return facebookAjaxLogin();">
												<table border="0" width="125" cellpadding="0" cellspacing="0">
													<tr>
														<td>
															<span style="font-size:12px;color:#AAAAAA;">username</span>
															<br/>
																<input>
																	<xsl:attribute name="name">clientName</xsl:attribute>
																	<xsl:attribute name="style">width: 190px</xsl:attribute>
																	<xsl:attribute name="class">inputtext</xsl:attribute>
																	<xsl:attribute name="type">text</xsl:attribute>
																	<xsl:attribute name="id">Username</xsl:attribute>
																	<xsl:attribute name="size">16</xsl:attribute>
																	<xsl:attribute name="maxlength">32</xsl:attribute>
																	<xsl:attribute name="value"><xsl:value-of select="xml/ChipmarkUser"/></xsl:attribute>
																	
                                                                     									<xsl:if test="xml/ChipmarkUser!=''">
																	<xsl:attribute name="disabled">true</xsl:attribute>
																	</xsl:if>
																</input>
															<br />
															<span style="font-size:12px;color:#AAAAAA;">password</span>
															<br/>
															<input style="width: 190px" name="clientPass" class="inputtext" type="password" id="Password" size="16" maximum=""/>
															<br />
															<div align="center">
																<input name="Login" type="submit" id="Login" value="login" style="border: 1px solid #CCCCCC; background-color: #EEEEEE; width: 190px" />
															</div>
															<div align="right">
																<a class="copyright" href="Main?target=forgottenusernamepassword" target="_blank">forgot your password?</a>
															</div>
														</td>
													</tr>
												</table>
											</form>
							</td>
							<td class="module_frame_content_right">
								<img src="images/spacer.gif"/>
							</td>
						</tr>
						<tr>
							<td class="module_frame_ll">
								<img src="images/spacer.gif"/>
							</td>
							<td class="module_frame_lm">
								<img src="images/spacer.gif"/>
							</td>
							<td class="module_frame_lr">
								<img src="images/spacer.gif"/>
							</td>
						</tr>
					</table>
				</div>
			</body>
		</html>
	</xsl:template>


	<!-- ===================================================================== -->
	<!-- FACEBOOK_AUTHENTICATION                                               -->
	<!-- ===================================================================== -->
	<xsl:template name="FACEBOOK_AUTHENTICATION">
		<html>
			<!-- valid XHTML 1.0 strict -->
			<head>
				<!-- title -->
				<xsl:call-template name="MAIN_TITLE" />

				<!-- links -->
				<link rev="made" href="mailto:support@chipmark.com" />
				<link rev="start" href="./" title="chipmark.com" />

				<!-- metatags -->
				<xsl:call-template name="META_TAGS" />

				<!-- common head elements (javascript, css import, etc) -->
				<xsl:call-template name="COMMON_HEAD_ELEMENTS" />
			</head>
			<body>
				<center>	
					
				
					
					<table width="80%" border="0" cellspacing="1" cellpadding="1">
						<tr>
							<td>
								<xsl:call-template name="HEADER"/>
								<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
									<tr>
										<td id="mainbox_ul"></td>
										<td id="mainbox_um">
											<img src="images/spacer.gif"/>
										</td>
										<td id="mainbox_ur"></td>
									</tr>
									<tr>
										<td id="mainbox_ml"></td>
										<td id="mainbox_mm">
											<xsl:call-template name="BREADCRUMBS" />

											<xsl:for-each select="xml/Result/Message">
												<xsl:if test="Value!='SUCCESS'">
													<xsl:if test="Type='error'">
														<p>
															<font color="red">
																<xsl:value-of select="Value"/>
															</font>
														</p>
													</xsl:if>
													<xsl:if test="Type!='error'">
														<p>
															<font color="green">
																<xsl:value-of select="Value"/>
															</font>
														</p>
													</xsl:if>
												</xsl:if>
											</xsl:for-each>
											Please provide your account name and password so we can link your Facebook account with your Chipmark account.
											
											<form action="FacebookRegistration" method="POST">
												<input>
													<xsl:attribute name="name"><xsl:value-of select="xml/FacebookSessionKeyName"/></xsl:attribute>
													<xsl:attribute name="type">hidden</xsl:attribute>
													<xsl:attribute name="value"><xsl:value-of select="xml/FacebookSessionKey"/></xsl:attribute>
												</input>
												<table width="60%" border="0">
													<tr>
														<td class="form_elements" width="150" align="right" valign="top">
																Account Name:
														</td>
														<td align="left" valign="top">
															<xsl:element name="input">
																<xsl:attribute name="type">TEXT</xsl:attribute>
																<xsl:attribute name="name">userName</xsl:attribute>
																<xsl:attribute name="value"></xsl:attribute>
																<xsl:attribute name="maxlength">255</xsl:attribute>
															</xsl:element>
														</td>
													</tr>
													<tr>
														<td class="form_elements" width="150" align="right" valign="top">
																Password:
														</td>
														<td align="left" valign="top">
															<xsl:element name="input">
																<xsl:attribute name="type">PASSWORD</xsl:attribute>
																<xsl:attribute name="name">password</xsl:attribute>
																<xsl:attribute name="value"></xsl:attribute>
																<xsl:attribute name="maxlength">32</xsl:attribute>
															</xsl:element>
														</td>
													</tr>
													<tr>
														<td></td>
														<td>
															<input type="submit" name="submitBtn" value="link my accounts"/>
															<br/>
														</td>
													</tr>
													<tr>
														<td>
															<p class="body">
																Are you new to Chipmark?
																<br/>
																<a href="/Main?target=registration" target="_blank">Create an account!</a>
															</p>
														</td>
													</tr>
												</table>
											</form>
										</td>
										<td id="mainbox_mr"></td>
									</tr>
									<tr>
										<td id="mainbox_ll"></td>
										<td id="mainbox_lm">
											<img src="images/spacer.gif"/>
										</td>
										<td id="mainbox_lr"></td>
									</tr>
								</table>
								<xsl:call-template name="FOOTER"/>
							</td>
						</tr>				
					</table>					
				</center>
			</body>
		</html>
	</xsl:template>


</xsl:stylesheet>
