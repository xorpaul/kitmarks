<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" method="html" indent="yes"/>

	<xsl:template match="/">
		<!--
*************************************************************************************************
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
*************************************************************************************************
-->
	</xsl:template>


	<!-- ===================================================================== -->
	<!-- BREADCRUBMS (inserts appropriate crumb trail)                         -->
	<!-- ===================================================================== -->
	<xsl:template name="BREADCRUMBS">
		<table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td>
					<span class="breadcrumbs">
						<xsl:if test="xml/Target=''">
							<xsl:call-template name="CRUMBS_HOME" />
						</xsl:if>

						<xsl:if test="xml/Target='download'">
							<xsl:call-template name="CRUMBS_DOWNLOADS" />
						</xsl:if>

						<xsl:if test="xml/Target='about'">
							<xsl:call-template name="CRUMBS_ABOUT" />
						</xsl:if>

						<xsl:if test="xml/Target='faqs'">
							<xsl:call-template name="CRUMBS_FAQS" />
						</xsl:if>

						<xsl:if test="xml/Target='help'">
							<xsl:call-template name="CRUMBS_HELP" />
						</xsl:if>

						<xsl:if test="xml/Target='license'">
							<xsl:call-template name="CRUMBS_LICENSE" />
						</xsl:if>

						<xsl:if test="xml/Target='contact'">
							<xsl:call-template name="CRUMBS_CONTACT" />
						</xsl:if>

						<xsl:if test="xml/Target='registration'">
							<xsl:call-template name="CRUMBS_REGISTRATION" />
						</xsl:if>

						<xsl:if test="xml/Target='privacy'">
							<xsl:call-template name="CRUMBS_PRIVACY" />
						</xsl:if>

						<xsl:if test="xml/Target='developers'">
							<xsl:call-template name="CRUMBS_DEVELOPERS" />
						</xsl:if>

						<xsl:if test="xml/Target='devapi'">
							<xsl:call-template name="CRUMBS_DEVELOPER_API" />
						</xsl:if>

						<xsl:if test="xml/Target='features'">
							<xsl:call-template name="CRUMBS_FEATURES" />
						</xsl:if>

						<xsl:if test="xml/Target='forgottenusernamepassword'">
							<xsl:call-template name="CRUMBS_FORGOTTENPASSWORD" />
						</xsl:if>

						<xsl:if test="xml/Target='underConstruction'">
							<xsl:call-template name="CRUMBS_UNDERCONSTRUCTION" />
						</xsl:if>

						<xsl:if test="xml/Target='error'">
							<xsl:call-template name="CRUMBS_ERROR" />
						</xsl:if>

						<xsl:if test="xml/Target='top10bookmarked'">
							<xsl:call-template name="CRUMBS_TOP10" />
						</xsl:if>

						<xsl:if test="xml/Target='mostrecentlyadded'">
							<xsl:call-template name="CRUMBS_RECENTLY_ADDED" />
						</xsl:if>

						<xsl:if test="xml/Target='recommender'">
							<xsl:call-template name="CRUMBS_RECOMMENDER" />
						</xsl:if>

						<xsl:if test="xml/Target='addclientresult'">
							<xsl:call-template name="CRUMBS_REGISTRATION_RESULT" />
						</xsl:if>

						<xsl:if test="xml/Target='downloadIE'">
							<xsl:call-template name="CRUMBS_DOWNLOAD_IE" />
						</xsl:if>

						<xsl:if test="xml/Target='downloadFirefox'">
							<xsl:call-template name="CRUMBS_DOWNLOAD_FIREFOX" />
						</xsl:if>

						<xsl:if test="xml/Target='api'">
							<xsl:call-template name="CRUMBS_API" />
						</xsl:if>

						<xsl:if test="xml/Target='api_login'">
							<xsl:call-template name="CRUMBS_API_LOGIN" />
						</xsl:if>

						<xsl:if test="xml/Target='api_logout'">
							<xsl:call-template name="CRUMBS_API_LOGOUT" />
						</xsl:if>

						<xsl:if test="xml/Target='api_get'">
							<xsl:call-template name="CRUMBS_API_GET" />
						</xsl:if>

						<xsl:if test="xml/Target='api_addlink'">
							<xsl:call-template name="CRUMBS_API_ADDLINK" />
						</xsl:if>

						<xsl:if test="xml/Target='api_deletelink'">
							<xsl:call-template name="CRUMBS_API_DELETELINK" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_toptenbookmarked'">
							<xsl:call-template name="CRUMBS_API_TOPTENBOOKMARKED" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_random'">
							<xsl:call-template name="CRUMBS_API_RANDOM" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_editlink'">
							<xsl:call-template name="CRUMBS_API_EDITLINK" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_buddylist_display'">
							<xsl:call-template name="CRUMBS_API_BUDDYLIST_DISPLAY" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_buddylist_add'">
							<xsl:call-template name="CRUMBS_API_BUDDYLIST_ADD" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_buddylist_remove'">
							<xsl:call-template name="CRUMBS_API_BUDDYLIST_REMOVE" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_buddylist_edit'">
							<xsl:call-template name="CRUMBS_API_BUDDYLIST_EDIT" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_buddylist_getbuddyrequests'">
							<xsl:call-template name="CRUMBS_API_BUDDYLIST_GETBUDDYREQUESTS" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_buddylist_setbuddyrequests'">
							<xsl:call-template name="CRUMBS_API_BUDDYLIST_SETBUDDYREQUESTS" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_buddylist_viewpendingbuddyrequests'">
							<xsl:call-template name="CRUMBS_API_BUDDYLIST_VIEWPENDINGBUDDYREQUESTS" />
						</xsl:if>
						
						<xsl:if test="xml/Target='api_buddylist_viewtransitivebuddies'">
							<xsl:call-template name="CRUMBS_API_BUDDYLIST_VIEWTRANSITIVEBUDDIES" />
						</xsl:if>

						<xsl:if test="xml/Target='tour' or xml/Target='tour_web' or xml/Target='tour_firefox' or xml/Target='tour_ie'">
							<xsl:call-template name="CRUMBS_TOUR" />
						</xsl:if>

						<xsl:if test="xml/Target='features_web'">
							<xsl:call-template name="CRUMBS_FEATURES_WEB" />
						</xsl:if>

						<xsl:if test="xml/Target='features_firefox'">
							<xsl:call-template name="CRUMBS_FEATURES_FIREFOX" />
						</xsl:if>

						<xsl:if test="xml/Target='features_ie'">
							<xsl:call-template name="CRUMBS_FEATURES_IE" />
						</xsl:if>

						<xsl:if test="xml/Target='about_us_04'">
							<xsl:call-template name="CRUMBS_ABOUT_04" />
						</xsl:if>

						<xsl:if test="xml/Target='about_us_05'">
							<xsl:call-template name="CRUMBS_ABOUT_05" />
						</xsl:if>

						<xsl:if test="xml/Target='about_us_06'">
							<xsl:call-template name="CRUMBS_ABOUT_06" />
						</xsl:if>

						<xsl:if test="xml/Target='sponsor'">
							<xsl:call-template name="CRUMBS_SPONSOR" />
						</xsl:if>

						<xsl:if test="xml/Target='userHome'">
							<xsl:call-template name="CRUMBS_USERHOME" />
						</xsl:if>

						<xsl:if test="xml/Target='manage'">
							<xsl:call-template name="CRUMBS_MANAGE" />
						</xsl:if>
						
						<xsl:if test="xml/Target='accountpreferences'">
							<xsl:call-template name="CRUMBS_ACCOUNT_PREFS" />
						</xsl:if>

						<xsl:if test="xml/Target='manageBuddies'">
							<xsl:call-template name="CRUMBS_BUDDIES" />
						</xsl:if>

						<xsl:if test="xml/Target='import'">
							<xsl:call-template name="CRUMBS_IMPORT" />
						</xsl:if>

						<xsl:if test="xml/Target='export'">
							<xsl:call-template name="CRUMBS_EXPORT" />
						</xsl:if>

						<xsl:if test="xml/Target='searchlinks'">
							<xsl:call-template name="CRUMBS_SEARCH" />
						</xsl:if>

						<xsl:if test="xml/Target='jshowto'">
							<xsl:call-template name="CRUMBS_JSHOWTO" />
						</xsl:if>

						<xsl:if test="xml/Target='addlink'">
							<xsl:call-template name="CRUMBS_ADDLINK" />
						</xsl:if>

						<xsl:if test="xml/Target='video_ieinstall' or xml/Target='video_registration' or xml/Target='video_ffinstall' or xml/Target='video_ffimport'">
							<xsl:call-template name="CRUMBS_VIDEO" />
						</xsl:if>
					</span>
				</td>
			</tr>
		</table>
		<hr/>

	</xsl:template>


	<!-- ===================================================================== -->
	<!-- CRUMBS_HOME                                                           -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_HOME">
		<a class="breadcrumbs" href="Main">home</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_ABOUT                                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_ABOUT">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=about">about</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_FAQS                                                           -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_FAQS">
		<xsl:call-template name="CRUMBS_ABOUT"/>
		<a class="breadcrumbs" href="Main?target=faqs">faqs</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_HELP                                                           -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_HELP">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=help">help</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_CONTACT                                                        -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_CONTACT">
		<xsl:call-template name="CRUMBS_HELP"/>
		<a class="breadcrumbs" href="Main?target=contact">contact</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_LICENSE                                                        -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_LICENSE">
		<xsl:call-template name="CRUMBS_ABOUT"/>
		<a class="breadcrumbs" href="Main?target=license">license</a> &#187;
	</xsl:template>


	<!-- ===================================================================== -->
	<!-- CRUMBS_DOWNLOADS                                                      -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_DOWNLOADS">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=download">downloads</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_ADDCLIENT                                                      -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_REGISTRATION">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=registration">registration</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_PRIVACY                                                        -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_PRIVACY">
		<xsl:call-template name="CRUMBS_ABOUT"/>
		<a class="breadcrumbs" href="Main?target=privacy">privacy policy</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_DEVELOPERS                                                     -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_DEVELOPERS">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=developers">developer resources</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_FEATURES                                                       -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_FEATURES">
		<xsl:call-template name="CRUMBS_ABOUT"/>
		<a class="breadcrumbs" href="Main?target=features">features</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_FEATURES                                                       -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_FORGOTTENPASSWORD">
		<xsl:call-template name="CRUMBS_HELP"/>
		<a class="breadcrumbs" href="Main?target=forgottenusernamepassword">password reset</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_UNDERCONSTRUCTION                                              -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_UNDERCONSTRUCTION">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=underConstruction">under construction</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_TOP10                                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_TOP10">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Top10Bookmarked">top 10 chipmarks</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_RECENTLY_ADDED                                                 -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_RECENTLY_ADDED">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="MostRecentlyAdded">recently added chipmarks</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_RECOMMENDER                                            -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_RECOMMENDER">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="RecommenderServlet">recommended chipmarks</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_ADD_CLIENT_RESULT                                              -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_REGISTRATION_RESULT">
		<xsl:call-template name="CRUMBS_REGISTRATION"/>
		registration result &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_ERROR                                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_ERROR">
		<xsl:call-template name="CRUMBS_HOME"/>
		error &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_DEVELOPER_API                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_DEVELOPER_API">
		<xsl:call-template name="CRUMBS_DEVELOPERS"/>
		<a class="breadcrumbs" href="?target=devapi">api</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_DOWNLOAD_IE                                                 -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_DOWNLOAD_IE">
		<xsl:call-template name="CRUMBS_DOWNLOADS"/>
		<a class="breadcrumbs" href="?target=downloadIE">IE</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_DOWNLOAD_FIREFOX                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_DOWNLOAD_FIREFOX">
		<xsl:call-template name="CRUMBS_DOWNLOADS"/>
		<a class="breadcrumbs" href="?target=downloadFirefox">FF</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_API                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API">
		<xsl:call-template name="CRUMBS_DEVELOPERS"/>
		<a class="breadcrumbs" href="?target=api">API</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_API_LOGIN                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_LOGIN">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_login">Login</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_API_LOGOUT                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_LOGOUT">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_logout">Logout</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_API_GET                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_GET">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_get">GetFoldersAndLinks</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_API_ADDLINK                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_ADDLINK">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_addlink">AddLink</a> &#187;
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- CRUMBS_API_DELETELINK                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_DELETELINK">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_deletelink">DeleteLink</a> &#187;
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- CRUMBS_API_TOPTENBOOKMARKED                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_TOPTENBOOKMARKED">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_toptenbookmarked">Top10Bookmarked</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_API_RANDOM                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_RANDOM">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_random">Random</a> &#187;
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- CRUMBS_API_EDITLINK                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_EDITLINK">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_editlink">EditLink</a> &#187;
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- CRUMBS_API_BUDDYLIST_DISPLAY                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_BUDDYLIST_DISPLAY">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_buddylist_display">BuddyList::Display</a> &#187;
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- CRUMBS_API_BUDDYLIST_ADD                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_BUDDYLIST_ADD">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_buddylist_add">BuddyList::Add</a> &#187;
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- CRUMBS_API_BUDDYLIST_REMOVE                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_BUDDYLIST_REMOVE">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_buddylist_remove">BuddyList::Remove</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_API_BUDDYLIST_EDIT                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_BUDDYLIST_EDIT">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_buddylist_edit">BuddyList::Edit</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_API_BUDDYLIST_GETBUDDYREQUESTS                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_BUDDYLIST_GETBUDDYREQUESTS">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_buddylist_getbuddyrequests">BuddyList::GetBuddyRequests</a> &#187;
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- CRUMBS_API_BUDDYLIST_SETBUDDYREQUESTS                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_BUDDYLIST_SETBUDDYREQUESTS">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_buddylist_setbuddyrequests">BuddyList::SetBuddyRequests</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_API_BUDDYLIST_VIEWPENDINGBUDDYREQUESTS                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_BUDDYLIST_VIEWPENDINGBUDDYREQUESTS">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_buddylist_viewpendingbuddyrequests">BuddyList::ViewPendingBuddyRequests</a> &#187;
	</xsl:template>
	
	<!-- ===================================================================== -->
	<!-- CRUMBS_API_BUDDYLIST_VIEWTRANSITIVEBUDDIES                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_API_BUDDYLIST_VIEWTRANSITIVEBUDDIES">
		<xsl:call-template name="CRUMBS_API"/>
		<a class="breadcrumbs" href="?target=api_buddylist_viewtransitivebuddies">BuddyList::ViewTransitiveBuddies</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_TOUR                                                  -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_TOUR">
		<xsl:call-template name="CRUMBS_ABOUT"/>
		<a class="breadcrumbs" href="?target=tour">tour</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_FEATURES_WEB                                                       -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_FEATURES_WEB">
		<xsl:call-template name="CRUMBS_FEATURES"/>
		<a class="breadcrumbs" href="Main?target=features_web">web</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_FEATURES_FIREFOX                                                       -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_FEATURES_FIREFOX">
		<xsl:call-template name="CRUMBS_FEATURES"/>
		<a class="breadcrumbs" href="Main?target=features_firefox">FF</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_FEATURES_IE                                                       -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_FEATURES_IE">
		<xsl:call-template name="CRUMBS_FEATURES"/>
		<a class="breadcrumbs" href="Main?target=features_ie">IE</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_ABOUT_04                                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_ABOUT_04">
		<xsl:call-template name="CRUMBS_ABOUT"/>
		<a class="breadcrumbs" href="Main?target=about_us_04">'04</a> &#187;
	</xsl:template>


	<!-- ===================================================================== -->
	<!-- CRUMBS_ABOUT_05                                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_ABOUT_05">
		<xsl:call-template name="CRUMBS_ABOUT"/>
		<a class="breadcrumbs" href="Main?target=about_us_05">'05</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_ABOUT_06                                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_ABOUT_06">
		<xsl:call-template name="CRUMBS_ABOUT"/>
		<a class="breadcrumbs" href="Main?target=about_us_06">'06</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_SPONSOR                                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_SPONSOR">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=sponsor">sponsor</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_USER_HOME                                                         -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_USERHOME">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=userHome">my home</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_MANAGE                                                         -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_MANAGE">
		<xsl:call-template name="CRUMBS_USERHOME"/>
		<a class="breadcrumbs" href="Manage">chipmark management</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_BUDDIES                                                         -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_BUDDIES">
		<xsl:call-template name="CRUMBS_USERHOME"/>
		<a class="breadcrumbs" href="BuddyList">buddy management</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_ACCOUNT_PREFS                                                         -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_ACCOUNT_PREFS">
		<xsl:call-template name="CRUMBS_USERHOME"/>
		<a class="breadcrumbs" href="AccountPreferences">account management</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_IMPORT                                                         -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_IMPORT">
		<xsl:call-template name="CRUMBS_USERHOME"/>
		<a class="breadcrumbs" href="Main?target=import">import bookmarks</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_EXPORT                                                         -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_EXPORT">
		<xsl:call-template name="CRUMBS_USERHOME"/>
		<a class="breadcrumbs" href="Export">export chipmarks</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_SEARCH                                                         -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_SEARCH">
		<xsl:call-template name="CRUMBS_HOME"/>
		search results &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_JSHOWTO                                                         -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_JSHOWTO">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=jshowto">enabling javascript</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_ADDLINK                                                         -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_ADDLINK">
		<xsl:call-template name="CRUMBS_HOME"/>
		<a class="breadcrumbs" href="Main?target=addlink">add a chipmark</a> &#187;
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- CRUMBS_VIDEO                                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="CRUMBS_VIDEO">
		<xsl:call-template name="CRUMBS_HELP"/>
		tutorial video &#187;
	</xsl:template>
</xsl:stylesheet>
