<?xml version="1.0" encoding="utf-8"?>
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
	<!-- MAIN_TITLE (Titles for standard pages)                                -->
	<!-- ===================================================================== -->
	<xsl:template name="MAIN_TITLE">

		<xsl:if test="not(boolean(xml/Target)) or xml/Target=''">
			<title>chipmark</title>
		</xsl:if>

		<xsl:if test="xml/Target='about'">
			<title>chipmark :: about</title>
		</xsl:if>

		<xsl:if test="xml/Target='about_us_04'">
			<title>chipmark :: about us</title>
		</xsl:if>

		<xsl:if test="xml/Target='about_us_05'">
			<title>chipmark :: about us</title>
		</xsl:if>

		<xsl:if test="xml/Target='about_us_06'">
			<title>chipmark :: about us</title>
		</xsl:if>

		<xsl:if test="xml/Target='faqs'">
			<title>chipmark :: faqs</title>
		</xsl:if>

		<xsl:if test="xml/Target='features'">
			<title>chipmark :: features</title>
		</xsl:if>

		<xsl:if test="xml/Target='import'">
			<title>chipmark :: import bookmarks</title>
		</xsl:if>

		<xsl:if test="xml/Target='addclient'">
			<title>chipmark :: register</title>
		</xsl:if>

		<xsl:if test="xml/Target='download'">
			<title>chipmark :: downloads</title>
		</xsl:if>

		<xsl:if test="xml/Target='updates'">
			<title>chipmark :: updates</title>
		</xsl:if>

		<xsl:if test="xml/Target='forgottenusernamepassword'">
			<title>chipmark :: forgot password?</title>
		</xsl:if>

		<xsl:if test="xml/Target='privacy'">
			<title>chipmark :: privacy policy</title>
		</xsl:if>

		<xsl:if test="xml/Target='underConstruction'">
			<title>chipmark :: under construction</title>
		</xsl:if>

		<xsl:if test="xml/Target='advancedSearch'">
			<title>chipmark :: advanced search</title>
		</xsl:if>

		<xsl:if test="xml/Target='sharing'">
			<title>chipmark :: sharing chipmarks</title>
		</xsl:if>

		<xsl:if test="xml/Target='downloadFirefox'">
			<title>chipmark :: download firefox extension</title>
		</xsl:if>

		<xsl:if test="xml/Target='downloadIE'">
			<title>chipmark :: download IE toolbar</title>
		</xsl:if>

		<xsl:if test="xml/Target='developers'">
			<title>chipmark :: developers</title>
		</xsl:if>

		<xsl:if test="xml/Target='api'">
			<title>chipmark :: API</title>
		</xsl:if>

		<xsl:if test="xml/Target='api_login'">
			<title>chipmark :: API - Login</title>
		</xsl:if>

		<xsl:if test="xml/Target='api_logout'">
			<title>chipmark :: API - Logout</title>
		</xsl:if>

		<xsl:if test="xml/Target='api_get'">
			<title>chipmark :: API - GetFoldersAndLinks</title>
		</xsl:if>

		<xsl:if test="xml/Target='api_addlink'">
			<title>chipmark :: API - AddLink</title>
		</xsl:if>

		<xsl:if test="xml/Target='api_deletelink'">
			<title>chipmark :: API - DeleteLink</title>
		</xsl:if>

		<xsl:if test="xml/Target='api_toptenbookmarked'">
			<title>chipmark :: API - Top10Bookmarked</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='api_random'">
			<title>chipmark :: API - Random</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='api_editlink'">
			<title>chipmark :: API - EditLink</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='api_buddylist_display'">
			<title>chipmark :: API - BuddyList::Display</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='api_buddylist_add'">
			<title>chipmark :: API - BuddyList::Add</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='api_buddylist_remove'">
			<title>chipmark :: API - BuddyList::Remove</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='api_buddylist_edit'">
			<title>chipmark :: API - BuddyList::Edit</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='api_buddylist_getbuddyrequests'">
			<title>chipmark :: API - BuddyList::GetBuddyRequests</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='api_buddylist_setbuddyrequests'">
			<title>chipmark :: API - BuddyList::SetBuddyRequests</title>
		</xsl:if>

		<xsl:if test="xml/Target='api_buddylist_viewpendingbuddyrequests'">
			<title>chipmark :: API - BuddyList::ViewPendingBuddyRequests</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='api_buddylist_viewtransitivebuddies'">
			<title>chipmark :: API - BuddyList::ViewTransitiveBuddies</title>
		</xsl:if>

		<xsl:if test="xml/Target='manage'">
			<title>chipmark :: manage</title>
		</xsl:if>

		<xsl:if test="xml/Target='accountpreferences'">
			<title>chipmark :: account</title>
		</xsl:if>

		<xsl:if test="xml/Target='searchlinks'">
			<title>chipmark :: search</title>
		</xsl:if>

		<xsl:if test="xml/Target='manageBuddies'">
			<title>chipmark :: buddies</title>
		</xsl:if>
		
		<xsl:if test="xml/Target='userHome'">
			<title>chipmark :: personal home</title>
		</xsl:if>

		<xsl:if test="xml/Target='jshowto'">
			<title>chipmark :: enabling javascript</title>
		</xsl:if>

		<xsl:if test="xml/Target='top10bookmarked'">
			<title>chipmark :: top 10 chipmarks</title>
		</xsl:if>

		<xsl:if test="xml/Target='mostrecentlyadded'">
			<title>chipmark :: recently added chipmarks</title>
		</xsl:if>

		<xsl:if test="xml/Target='addlink'">
			<title>chipmark :: add chipmark</title>
		</xsl:if>

		<xsl:if test="xml/Target='tour' or xml/Target='tour_web' or xml/Target='tour_firefox' or xml/Target='tour_ie'">
			<title>chipmark :: tour</title>
		</xsl:if>

		<xsl:if test="xml/Target='video_ieinstall' or xml/Target='video_registration' or xml/Target='video_ffinstall' or xml/Target='video_ffimport'">
			<title>chipmark :: tutorial video</title>
		</xsl:if>
		
</xsl:template>


</xsl:stylesheet>
