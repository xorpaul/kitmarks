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
		BODY_TOUR
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_TOUR">
		<p class="sub_header">Overview</p>
		<table border="0">
			<tr>
				<td valign="top">
					<p class="content">
						Chipmark ist ein Online-Bookmarkverwaltungsdienst, der Ihnen den
						Zugriff auf ihre Lesezeichen von jedem Computer und von überall
						ermöglicht. Es erleichtert das Verwalten und den Zugriff auf die
						eigenen Lesezeichen durch eine zentrale online Speicherung
						erheblich.
						<br />
						<br />
						<img src="images/web_logo.gif" />
						<img src="images/ff_logo.gif" />
						<img src="images/ie7_logo.gif" />
						<p class="content">
							Chipmark besteht aus 3 Elementen:
							<ul>
								<li>
									<span class="sub_content">Ein Web-Interface</span>
								</li>
								<li>
									<span class="sub_content">Ein Firefox Plug-in</span>
								</li>
								<li>
									<span class="sub_content">Ein Internet Explorer Plug-in</span>
								</li>
							</ul>
						</p>
					</p>
				</td>
				<td align="right" width="205" valign="top">
					<img src="images/tour/web_shared.png" />
				</td>
			</tr>
		</table>
		<p class="content"> Jeder dieser Schnittstellen ermöglich Ihnen den schnellen
			Zugriff auf ihre Bookmarksammlung unabhängig von verwendetem Browser
			oder Computer.</p>
		<div style="width:100%" align="right">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="right">
						<span class="small_text">
							<a href="Main?target=tour_web">weiter</a>
						</span>
					</td>
					<td align="center" valign="middle">
						<img src="images/circle_rightarrow.png" border="0" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<!--
		=====================================================================
	-->
	<!--
		BODY_TOUR_WEB
	-->
	<!--
		=====================================================================
	-->
	<xsl:template name="BODY_TOUR_WEB">
		<p class="sub_header">Web Interface</p>
		<table width="100%" border="0">
			<tr>
				<td>
					<img src="images/tour/web_labels.png" align="right" />
					<span class="content">
						Das Web-Interface bietet den vollen Funktionsumfang. Man kann
						schnell durch umfangreiche Lesezeichensammlungen navigieren. Dazu
						muss ein Internetbrowser verwendet werden, der JavaScript
						untersützt und aktiviert ist. Das Web-Interface unterstütz
						folgende Internetbrowser:
						<br />
						<br />&#160;&#160;<img src="images/ff_logo_sm.png" alt="Firefox"/> Firefox<br/>
					  &#160;&#160;<img src="images/ie_logo_sm.png" alt="Internet Explorer"/> Internet Explorer<br/>
					  &#160;&#160;<img src="images/safari_logo_sm.png" alt="Safari"/> Safari<br/>
				      &#160;&#160;<img src="images/opera_logo_sm.png" alt="Opera"/> Opera<br/>
				      &#160;&#160;<img src="images/flock_logo_sm.png" alt="Flock"/> Flock<br/>
					  <br/><br/>
					  Chipmark unterstütz die gewohnte hierarchische Struktur beim Anlegen von Linklisten, als auch eine neuere Methode, bekannt als
					  <a href="Main?target=faqs#Q11">tagging oder labeling</a>).
				  </span>
			  </td>
		  </tr>
	  </table>
	  <br/>
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/web_dragdrop.png" align="left"/>
				  <span class="content">
				  Die außerordentlich benutzerfreundliche grafische Oberfläche besticht durch die Möglichkeit Labels, 
				  Bookmarks und ganze Ordner durch
				  intuitives Drag&amp;Drop zu sortieren, löschen und manipulieren.
				  Dies verringert den zeitlichen Aufwand die lokalen Favoriten in die Chipmark-Datenbank einzupflegen.
					  <br/><br/>
					  Drag &amp; Drop Ausrichtung und die Möglichkeit Ordner und Favoriten aufgrund von Kriterien, 
					  wie Datum oder Name, anzuordnen, ermöglichen es dem Benutzer seine Bookmarks so anzuordnern, wie er es will.
					  <br/><br/>
					  Die schnelle Erweiterung der Navigationsmöglichkeit durch Labels wird durch die Möglichkeit, 
					  bereits vorhandene Labels einfach per Drag &amp; Drop auf andere Lesezeichen anzuwenden, ermöglicht.
					  (Für weitergehende Fragen bezüglich Labels oder Tags bitte in die <a href="Main?target=faqs#Q11">Frequently Asked Questions</a> schauen)
				  </span>
			  </td>
		  </tr>
	  </table>
	  <br/>	  
	  
		  <div style="width:100%" align="right">
		  <table border="0" cellpadding="0" cellspacing="0">
			  <tr>
				  <td valign="right">
					  <span class="small_text">
						  <a href="Main?target=tour_firefox">weiter </a>
					  </span>
				  </td>
				  <td align="center" valign="middle">
					  <img src="images/circle_rightarrow.png" border="0"/>
				  </td>
			  </tr>
		  </table>
	  </div>
  </xsl:template>

  <!-- ===================================================================== -->
  <!-- BODY_TOUR_IE                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_TOUR_IE">
  
	 	  
					  <p class="sub_header">Internet Explorer Plug-In</p>
				 
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/ie_buttons.png" align="right"/>
				  <span class="content">
				  Das Plug-In integriert sich nahtlos in den Internet Explorer. 
				  Es wird als eigenständige Toolbar angezeigt, wie viele andere IE Plug-ins.
				  Die meisten Funktionen können schnell und einfach durch das "Chipmarks" Menü aufgerufen werden.
				  Ähnlich der Firefox Erweiterung lassen sich zusätzliche Internetseiten schnell der Chipmarksammlung hinzufügen und Freunden per Mausklick einfach zusenden.
				  </span>
			  </td>
		  </tr>
	  </table>
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/ie_browse.png" align="left"/>
				  <span class="content">
				  Die Chipmarks werden in der bekannten hierarchischen Ordnerstruktur angezeigt, wie sie auch im Internet
					Explorer selbst zum Einsatz kommt.
					  Your chipmarks are displayed in a familar folder structure- much the same 
					  +way IE normally shows them.  You browse them by navigating through
					  cascading menus.  Additionally, any bookmarks you designate to be shown on the 
					  "toolbar" will give you easy 1-click access to chipmarks you use frequently.  You
					  get quick access to charts such as the Top 10, Recommended and Recently Added
					   Chipmarks, as well as to randomly selected links.
				  </span>
			  </td>
		  </tr>
	  </table>
	  <br/>
	  <div style="width:100%" align="right">
		  <table border="0" cellpadding="0" cellspacing="0">
			  <tr>
				  <td valign="right">
					  <span class="small_text">
						  <a href="Main?target=registration">sign me up! </a>
					  </span>
				  </td>
				  <td align="center" valign="middle">
					  <img src="images/circle_rightarrow.png" border="0"/>
				  </td>
			  </tr>
		  </table>
	  </div>
  </xsl:template>

  <!-- ===================================================================== -->
  <!-- BODY_TOUR_FIREFOX                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_TOUR_FIREFOX">
	 
				  <p class="sub_header">Firefox Extension</p>
			
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/ff_buttons.png" align="right"/>
				  <span class="content">
					  The extension slides right into Firefox.  It mimics much of the behavior of the regular Firefox Bookmarks menu.  
					  You can select most actions quickly and easily by clicking on the "Chipmarks" menu.					  
					  Quick-access toolbar buttons allow to you add pages to your chipmark collection and send pages to your buddies with a single click.
				  </span>
			  </td>
		  </tr>
	  </table>
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/ff_browse.png" align="left"/>
				  <span class="content">
					  Your chipmarks are displayed in the same style that Firefox shows your native bookmarks.  You browse them by navigating through
					  cascading menus.  Additionally, you can enable the Chipmark Toolbar to get easy access to chipmarks you use frequently.  You
					  get quick access to charts such as the Top 10, Recommended and Recently Added Chipmarks, as well as to randomly selected links.  
				  </span>
			  </td>
		  </tr>
	  </table>
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/ff_alreadychipmarked.png" align="right"/>
				  <span class="content">
					  We know it's next to impossible to remember every web page you've ever bookmarked, and Chipmark is ready to help.  When you visit a page
					  that you've already chipmarked, an icon is displayed in the status bar of your browser indicating that you've added the page before.
				  </span>
			  </td>
		  </tr>
	  </table>
	  <br/>
	  <div style="width:100%" align="right">
		  <table border="0" cellpadding="0" cellspacing="0">
			  <tr>
				  <td valign="right">
					  <span class="small_text">
						  <a href="Main?target=tour_ie">continue </a>
					  </span>
				  </td>
				  <td align="center" valign="middle">
					  <img src="images/circle_rightarrow.png" border="0"/>
				  </td>
			  </tr>
		  </table>
	  </div>
  </xsl:template>

</xsl:stylesheet>