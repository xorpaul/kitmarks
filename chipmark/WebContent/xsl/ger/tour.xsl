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
					Auch hier gibt es eine Option eine zsätzliche Toolbar anzuzeigen, die die schnelle Navigation zu häufig besuchten Chipmarks ermöglicht.
					Die Zusatzfeatures, wie die Top 10 Liste, gerade hinzugefügte Chipmarks oder zufällige Chipmarks werden auch hier angeboten.
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
						  <a href="Main?target=registration">Jetzt registrieren </a>
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
	Die Erweiterung fügt sich sofort in Firefox ein. Sie besitzt viel des typischen Verhaltens des üblichen Firefox Lesezeichen-Menüs. 
	Sie können die meisten Aktionen schnell und leicht auswählen, in dem Sie auf das Chipmarks-Menü klicken. 
	"Quick-Access" Buttons erlauben es, mit einem einzigen Klick Seiten zu Ihrer Chipmark-Sammlung hinzuzufügen und Seiten an Ihre Konkakte zu schicken.</span>
			  </td>
		  </tr>
	  </table>
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/ff_browse.png" align="left"/>
				  <span class="content">
				  Ihre Lesezeichen werden im gleichen Stil, wie im Firefox Browser angezeigt. Man kann wie gewohnt durch die hierarchische Struktur navigieren.
				  Zusätzlich kann man eine Chipmark Toolbar aktivieren um auf häufig benutzte Favoriten noch einfacher aufzurufen.
				  Außerdem gibt es einen Zugang zu den zusätzlichen Feature, wie die Top 10 Liste, die gerade hinzugefügte Chipmarks oder die zufälligen Chipmarks.
				  </span>
			  </td>
		  </tr>
	  </table>
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/ff_alreadychipmarked.png" align="right"/>
				  <span class="content">
					  Es ist uns bekannt, dass es fast unmöglich ist, sich an alle Web-Seiten, die man gespeichert hat, zu erinnern - Chipmark ist bereit Ihnen zu helfen. 
					  Wenn Sie eine Seite besuchen, die Sie bereits gechipmarked haben, wird ein Symbol in der Statusleiste Ihres Browsers angezeigt und zeigt an, dass Sie die Seite bereits früher hinzugefügt haben.
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
						  <a href="Main?target=tour_ie">weiter </a>
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