<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="utf-8" method="html" indent="yes"/>

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
	<!-- LOGIN_MODULE                                                          -->
	<!-- ===================================================================== -->
	<xsl:template name="LOGIN_MODULE">

		<table style="table-layout: fixed;" width="200" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="module_title_left">
					<img src="images/spacer.gif"/>
				</td>
				<td class="module_title_mid">
					<div style="width: 190px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
						<xsl:choose>
							<xsl:when test="not(boolean(xml/LoggedInAs)) or xml/LoggedInAs = ''">
								Chipmark Anmeldung
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="USER_STATUS"/>
							</xsl:otherwise>
						</xsl:choose>
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
					<xsl:choose>
						<xsl:when test="not(boolean(xml/LoggedInAs)) or xml/LoggedInAs = ''">
							<xsl:call-template name="LOGIN_FORM"/>
						</xsl:when>
						<xsl:otherwise>
							<table width="100%" cellpadding="2" cellspacing="3" border="0">
								<tr>
									<td width="15">
										<img src="images/home16.png"/>
									</td>
									<td>
										<a class="login_module_actions" href="Main?target=userHome">my home</a>
									</td>
								</tr>
								<tr>
									<td width="15">
										<img src="images/add_link_15.png"/>
									</td>
									<td>
										<a class="login_module_actions" href="Manage">chipmarks</a>
									</td>
								</tr>
								<tr>
									<td width="15">
										<img src="images/send_link_15.png"/>
									</td>
									<td>										
										<a class="login_module_actions" href="BuddyList">buddies</a>
									</td>
								</tr>
								<tr>
									<td width="15">
										<img src="images/spacer.gif" height="16" width="16"/>
									</td>
									<td>
										<a class="login_module_actions" href="AccountPreferences">account</a>
									</td>
								</tr>
							</table>
							<br/>
							<center>
								<div id="buddy_alert" style="height: 19px; text-align:left; width:100%; border: 1px solid #BBBBBB; background-color:#FAFAFA; margin-top:3px; display:none;">
									<img style="vertical-align: middle;" src="images/alert16.png"/>
									<span class="small_text">
										&#160;
										<a id="buddy_alert_text" href="Main?target=userHome">
											buddy alerts
										</a>
										&#160;
									</span>
								</div>
								<div id="invite_alert" style="height: 19px; text-align:left; width:100%; border: 1px solid #BBBBBB; background-color:#FAFAFA; margin-top:3px; display:none;">
									<img style="vertical-align: middle;" src="images/alert16.png"/>
									<span class="small_text">
										&#160;
										<a id="invite_alert_text" href="Main?target=userHome">
											invite alerts
										</a>
										&#160;
									</span>
								</div>
								<div id="inbox_alert" style="height: 19px; text-align:left; width:100%; border: 1px solid #BBBBBB; background-color:#FAFAFA; margin-top:3px; display:none;">
									<img style="vertical-align: middle;" src="images/alert16.png"/>
									<span class="small_text">
										&#160;
										<a id="inbox_alert_text" href="Main?target=userHome">
											inbox alerts
										</a>
										&#160;
									</span>
								</div>
							</center>
							<script type="text/javascript">
								getBuddyAlert('buddy_alert','buddy_alert_text');
								getInviteAlert('invite_alert','invite_alert_text');
								getInboxAlert('inboxy_alert','inbox_alert_text');
							</script>
							<br/>
							<div align="right">
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td align="center" valign="middle">
											<img src="images/circle_rightarrow.png" border="0"/>
										</td>
										<td valign="middle">
											<span class="small_text">
												<a href="Logout">Abmelden</a>
											</span>
										</td>
									</tr>
								</table>
							</div>
						</xsl:otherwise>
					</xsl:choose>
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

	</xsl:template>

	<!-- ===================================================================== -->
	<!-- MOST_POPULAR_MODULE                                                   -->
	<!-- ===================================================================== -->
	<xsl:template name="MOST_POPULAR_MODULE">

		<table class="module" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="module_title_left">
					<img src="images/spacer.gif"/>
				</td>
				<td class="module_title_mid">
					Populäre Chipmarks
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
					<script language="javascript">
						getTopChipmarks(5,true);
					</script>
					<noscript>
						<span class="small_text">
							This module requires that javascript is enabled.  You may still view the most popular chipmarks by clicking "more." If you wish to use this module, please <a href="Main?target=jshowto">enable javascript</a> in your browser.
							<br/>
						</span>
					</noscript>
					<br/>
					<div align="right">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td align="center" valign="middle">
									<img src="images/circle_rightarrow.png" border="0"/>
								</td>
								<td valign="middle">
									<span class="small_text">
										<a href="Top10Bookmarked">more...</a>
									</span>
								</td>
							</tr>
						</table>
					</div>
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

	</xsl:template>

	<!-- ===================================================================== -->
	<!-- RANDOM_CHIPMARK_MODULE                                                -->
	<!-- ===================================================================== -->
	<xsl:template name="RANDOM_CHIPMARK_MODULE">

		<table class="module" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="module_title_left">
					<img src="images/spacer.gif"/>
				</td>
				<td class="module_title_mid">
					Zufällige Chipmark
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
					<span class="small_text">
						<div id="randomlink" align="left" style="width: 190px; overflow: hidden; text-overflow: ellipsis; white-space: normal;">
							<script language="javascript">
								getRandomChipmark('randomlink');
							</script>
						</div>
					</span>
					<noscript>
						<span class="small_text">
							This module requires that javascript is enabled in order to function properly.  If you wish to use this module, please <a href="Main?target=jshowto">enable javascript</a> in your browser.
						</span>
						<br/>
					</noscript>
					<br/>
					<div align="right">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td align="center" valign="middle">
									<img src="images/circle_rightarrow.png" border="0"/>
								</td>
								<td valign="middle">
									<span class="small_text">
										<a href="#" onclick="getRandomChipmark('randomlink');">find another</a>
									</span>
								</td>
							</tr>
						</table>
					</div>
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

	</xsl:template>


	<!-- ===================================================================== -->
	<!-- RECENTLY_ADDED_MODULE                                                 -->
	<!-- ===================================================================== -->
	<xsl:template name="RECENTLY_ADDED_MODULE">

		<table class="module" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="module_title_left">
					<img src="images/spacer.gif"/>
				</td>
				<td class="module_title_mid">
					Kürzlich hinzugefügt
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
					<script language="javascript">
						getRecentChipmarks(3,true);
					</script>
					<noscript>
						<span class="small_text">
							This module requires that javascript is enabled.  You may still view recently added chipmarks by clicking "more." If you wish to use this module, please <a href="Main?target=jshowto">enable javascript</a> in your browser.
							<br/>
						</span>
					</noscript>
					<br/>
					<div align="right">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td align="center" valign="middle">
									<img src="images/circle_rightarrow.png" border="0"/>
								</td>
								<td valign="middle">
									<span class="small_text">
										<a href="MostRecentlyAdded">more...</a>
									</span>
								</td>
							</tr>
						</table>
					</div>
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

	</xsl:template>


</xsl:stylesheet>