<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" method="html" indent="yes" />
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
		COMMON_HEAD_ELEMENTS
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="COMMON_HEAD_ELEMENTS">

		<!-- import cascading style sheets -->
		<style type="text/css" media="all">@import "css/default.css";
		</style>
		<xsl:choose>
			
			<!-- resize the manage window to fit the facebook layout -->
			<xsl:when test="xml/Target='facebookManage'">
				<style type="text/css" media="all">@import
					"css/manageFacebook.css";</style>
			</xsl:when>
			<xsl:otherwise>
				<style type="text/css" media="all">@import "css/manage.css";
				</style>
			</xsl:otherwise>
		</xsl:choose>
		<style type="text/css" media="all">@import
			"css/autosuggest.css";</style>
		<script src="js/ajax.js" language="javascript" type="text/javascript">;</script>
		<script src="js/browser.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/utils.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/alerts.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/dom.js" language="javascript" type="text/javascript">;</script>
		<script src="js/autosuggest.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/sharedfolders.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/manageBuddies.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/randomChipmark.js" language="javascript" type="text/javascript">
			;</script>
		<script src="js/recentlyAddedChipmarks.js" language="javascript"
			type="text/javascript">;</script>
		<script src="js/recommendedChipmarks.js" language="javascript"
			type="text/javascript">;</script>
		<script src="js/topChipmarks.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/buddies.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/filter.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/prefLang.js" language="javascript" type="text/javascript">;
		</script>
		<script src="js/prefLangSession.js" language="javascript" type="text/javascript">
			;</script>
		<script src="js/newsLetter.js" language="javascript" type="text/javascript"> ;
		</script>
		<xsl:if
			test="xml/Target='manage' or xml/Target='pluginManage' or xml/Target='facebookManage'">
			<script src="js/treeNode.js" language="javascript" type="text/javascript">;
			</script>
			<script src="js/panelNode.js" language="javascript" type="text/javascript">;
			</script>
			<script src="js/listNode.js" language="javascript" type="text/javascript">;
			</script>
			<script src="js/node.js" language="javascript" type="text/javascript">;
			</script>
			<script src="js/dragdrop.js" language="javascript" type="text/javascript">;
			</script>
			<script src="js/manage.js" language="javascript" type="text/javascript">;
			</script>
		</xsl:if>
		<xsl:if test="xml/Target='facebookLogin' or xml/Target='facebookManage'">
			<script type="text/javascript" src="js/facebookApp.js">;</script>
		</xsl:if>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		HEADER
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="HEADER">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td id="headleft"></td>
				<td id="headmid">
					<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td height="91" style="width: 15px;">
								<img src="images/spacer.gif" />
							</td>
							<td height="91" align="left" valign="middle">
								<a href="http://www.kit.edu">
									<img src="images/logo.png" alt="Chipmark" border="0" />
								</a>
							</td>
							<td height="91" style="width: 15px;">
								<img src="images/spacer.gif" />
							</td>
							<td height="91" align="right" valign="middle">
								<xsl:call-template name="QUICKSEARCH" />
							</td>
							<td height="91" align="right" valign="middle">
								<xsl:call-template name="PREFERREDLANG" />
							</td>
						</tr>
					</table>
				</td>
				<td id="headright"></td>
			</tr>
		</table>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		PREFERREDLANG
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="PREFERREDLANG">
		<div id="preferred_lang_session" style="width:100%;">
			<script language="JavaScript">
				showprefLangSession('preferred_lang_session');</script>
		</div>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		QUICKSEARCH
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="QUICKSEARCH">
		<form id="QuickSearch" name="QuickSearch" method="POST" action="SearchLinks">
			<input name="submit" type="hidden" value="search" />
			<input name="userid" type="hidden" value="" />
			<input name="url" type="hidden" value="on" />
			<input name="desc" type="hidden" value="on" />
			<input name="name" type="hidden" value="on" />
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<xsl:element name="input">
							<xsl:attribute name="type">text</xsl:attribute>
							<xsl:attribute name="name">search</xsl:attribute>
							<xsl:attribute name="class">inputtext inputsearch</xsl:attribute>
							<xsl:attribute name="value">
								<xsl:value-of select="xml/SearchString" />
							</xsl:attribute>
							<xsl:attribute name="size">30</xsl:attribute>
						</xsl:element>
					</td>
					<tr>
						<td>
							<xsl:choose>
								<xsl:when test="not(boolean(xml/LoggedInAs)) or xml/LoggedInAs = ''">
								</xsl:when>
								<xsl:otherwise>
									<xsl:element name="input">
										<xsl:attribute name="type">CheckBox</xsl:attribute>
										<xsl:attribute name="name">searchAllUserLinks</xsl:attribute>
										<xsl:attribute name="value">yes_searchAllUserLinks</xsl:attribute>
										<span class="small_text"> search only my chipmarks</span>
									</xsl:element>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
				</tr>
				<!--
					<tr> <td> <table border="0" cellspacing="0" cellpadding="0"
					width="100%"> <tr> <td align="left" width="50%"> <span
					class="small_text"> &#160; </span> </td> <td align="right"
					width="50%"> <span class="small_text"> <a
					href="/Main?target=advancedSearch">advanced</a> </span> </td> </tr>
					</table> </td> </tr>
				-->
			</table>
		</form>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		USER STATUS
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="USER_STATUS">
		<span class="loginstatus">
			<xsl:choose>
				<xsl:when test="not(boolean(xml/LoggedInAs)) or xml/LoggedInAs = ''">
					(
					<a href="Main?target=login">login</a>
					)
				</xsl:when>
				<xsl:otherwise>
					welcome
					<xsl:value-of select="substring(xml/LoggedInAs,0,32)" />
					!
				</xsl:otherwise>
			</xsl:choose>
		</span>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		NAVIGATION
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="NAVIGATION">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td id="navbarleft"></td>
				<td id="navbarmid" align="center">
					<table width="100%" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td class="table_navigation" valign="middle" align="center"
								width="60">
								<a href="Main" class="navigation">home</a>
							</td>
							<!--
								<td class="table_navigation" valign="middle" align="center"
								width="75"> <a href="Main?target=browse"
								class="navigation">browse</a> </td>
							-->
							<td class="table_navigation" valign="middle" align="center"
								width="100">
								<a href="Main?target=download" class="navigation">downloads</a>
							</td>
							<td class="table_navigation" valign="middle" align="center"
								width="75">
								<a href="Main?target=about" class="navigation">about</a>
							</td>
							<td class="table_navigation" valign="middle" align="center">&#160;
							</td>
							<td class="table_navigation" valign="middle" align="center" width="75">
								<a href="Main?target=help" class="navigation">help</a>
							</td>
						</tr>
					</table>
				</td>
				<td id="navbarright"></td>
			</tr>
		</table>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- MAIN LAYOUT                                                           -->
	<!-- ===================================================================== -->
	<xsl:template name="MAIN_LAYOUT">
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
					<table width="80%" border="0" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
						<tr>
							<td>

								<xsl:call-template name="HEADER"/>
								<xsl:call-template name="NAVIGATION"/>

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
											<table width="98%" border="0" cellspacing="0" cellpadding="0" align="center" style="table-layout: fixed;">
												<tr>
													<td valign="top" align="left">
														<xsl:call-template name="CONTENT" />
													</td>
													<td style="width: 15px;">
														<img src="images/spacer.gif"/>
													</td>
													<td style="width: 200px;" valign="top">
														<xsl:call-template name="LOGIN_MODULE"/>
														<xsl:call-template name="MOST_POPULAR_MODULE"/>
														<xsl:call-template name="RANDOM_CHIPMARK_MODULE"/>
														<xsl:call-template name="RECENTLY_ADDED_MODULE"/>
													</td>
												</tr>
											</table>
											<br/>
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


	<!-- ===================================================================== -->
	<!-- FOOTER                                                                -->
	<!-- ===================================================================== -->
	<xsl:template name="FOOTER">
		<table width="97%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td height="5">
				</td>
			</tr>
			<tr>
				<td width="50%" align="left" valign="top">
					<span class="copyright">&#169; 2004-2007 chipmark. all rights reserved</span>
				</td>
				<td width="50%" align="right">
					<span class="small_text">
						<a href="Main?target=sponsor">
							<img class="img_link" src="images/umn.png" align="right" />
						</a>
						<a href="Main?target=contact">contact</a>&#160;
						<a href="Main?target=license">license</a>&#160;
						<a href="Main?target=privacy">privacy</a>
					</span>
					<br />
					<span class="copyright">University Of Minnesota</span>
				</td>
			</tr>
		</table>
	</xsl:template>

	<!-- ===================================================================== -->
	<!-- BUG_LINK                                                              -->
	<!-- ===================================================================== -->
	<xsl:template name="BUG_LINK">

		<a href="mailto:bugs@chipmark.com">report a bug</a>

	</xsl:template>

	<!-- ===================================================================== -->
	<!-- META_TAGS                                                             -->
	<!-- ===================================================================== -->
	<xsl:template name="META_TAGS">

		<meta name="description" content="
Chipmark is an on-line bookmark manager that allows you to access your bookmarks
from any computer. Chipmark fits seamlessly into your browser so you don't even
have to change your bookmarking habits. We are a group of Computer Science
students at the University of Minnesota working on an open source project under 
the guidance of a Professor while learning about the process of software
engineering and also creating software that can be used by a large userbase." />
		<meta name="keywords" content="
chipmark, bookmark, bookmarks, bookmark manager, favorites, storing information,
open source, university of minnesota, firefox extension, internet explorer
extension, see bookmarks, view bookmarks, add bookmarks, edit bookmarks,
download bookmarks, import bookmarks, export bookmarks." />
		<meta name="revision" content="v2" />

	</xsl:template>


</xsl:stylesheet>
