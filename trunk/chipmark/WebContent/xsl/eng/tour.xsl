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
  <!-- BODY_TOUR                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_TOUR">
    
      <p class="sub_header">Overview</p>
	  <table border="0">
		  <tr>
			  <td valign="top">				 
				  <p class="content">
					  Chipmark is an online bookmark manager that allows you to access your bookmarks from any computer, anywhere.  It gives you the simplicity of a single, centralized bookmark collection.
					  Whether you're at home, work, or school, Chipmark allows you to see, store, search, and share your bookmarks. Chipmark costs nothing and is free from annoying advertising.
					  <br/><br/>
					  <img src="images/web_logo.gif"/>
					  <img src="images/ff_logo.gif"/>
					  <img src="images/ie7_logo.gif"/>
					  <p class="content">
						  Chipmark comes in 3 flavors:
						  <ul>
							  <li>
								  <span class="sub_content">A Web Inteface</span>
							  </li>
							  <li>
								  <span class="sub_content">A Firefox Extension</span>
							  </li>
							  <li>
								  <span class="sub_content">An Internet Explorer Plug-in</span>
							  </li>
						  </ul>
					  </p>
				  </p>
			  </td>
			  <td align="right" width="205" valign="top">
				  <img src="images/tour/web_shared.png"/>
			  </td>
		  </tr>
	  </table>

	  <p class="content">
		  Each of the interfaces allows you to easily find and manipulate your bookmarks across multiple browsers and computers.
	  </p>
	  
	  <div style="width:100%" align="right">
		  <table border="0" cellpadding="0" cellspacing="0">
			  <tr>
				  <td valign="right">
					  <span class="small_text">
						  <a href="Main?target=tour_web">continue </a>
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
  <!-- BODY_TOUR_WEB                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_TOUR_WEB">
	 
				  <p class="sub_header">Web Interface</p>
			
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/web_labels.png" align="right"/>
				  <span class="content">
					  The web interface gives you all the functionality Chipmark has to offer.  You can browse your
					  entire bookmark collection quickly and easily from most modern JavaScript-enabled web browsers.  The web
					  interface currently runs in the following browsers:<br/><br/>
					  &#160;&#160;<img src="images/ff_logo_sm.png" alt="Firefox"/> Firefox<br/>
					  &#160;&#160;<img src="images/ie_logo_sm.png" alt="Internet Explorer"/> Internet Explorer<br/>
					  &#160;&#160;<img src="images/safari_logo_sm.png" alt="Safari"/> Safari<br/>
				      &#160;&#160;<img src="images/opera_logo_sm.png" alt="Opera"/> Opera<br/>
				      &#160;&#160;<img src="images/flock_logo_sm.png" alt="Flock"/> Flock<br/>
					  <br/><br/>
					  Chipmark supports the traditional folder-based organization styles as well as the popular categorization method known as
					  <a href="Main?target=faqs#Q11">tagging or labeling</a>).  With the web interface, you have the power to choose.  You can
					  browse and manipulate your chipmarks based on either organizational system.
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
					  A great feature unique to the web interface is the ability to drag and drop your chipmarks, folders
					  and labels.  This allows you to easily arrange your bookmarks into folders even if you didn't at the
					  time you added them.
					  <br/><br/>
					  Drag &amp; drop reorganization (yes, even bookmarks within a folder!) plus full-folder sorting options give you the freedom to
					  arrange your chipmarks as you see fit.
					  <br/><br/>
					  New to the chipmark web interface is the ability to drag and drop labels to
					  chipmarks to apply them (for more about labeling, see the <a href="Main?target=faqs#Q11">Frequently Asked Questions</a>)
					  and to move folders and chipmarks to the trash can.  Keeping your bookmarks organized has never been easier.
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
						  <a href="Main?target=tour_firefox">continue </a>
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
					  The plugin slides right into IE.  It shows up as its own toolbar, much like other IE plugins.
					  You can select most actions quickly and easily by clicking on the "Chipmarks" menu.
					  Similar to the Firefox extension, quick-access toolbar buttons allow to you add pages to your chipmark collection and send pages to your buddies with a single click.
				  </span>
			  </td>
		  </tr>
	  </table>
	  <table width="100%" border="0">
		  <tr>
			  <td>
				  <img src="images/tour/ie_browse.png" align="left"/>
				  <span class="content">
					  Your chipmarks are displayed in a familar folder structure- much the same way IE normally shows them.  You browse them by navigating through
					  cascading menus.  Additionally, any bookmarks you designate to be shown on the "toolbar" will give you easy 1-click access to chipmarks you use frequently.  You
					  get quick access to charts such as the Top 10, Recommended and Recently Added Chipmarks, as well as to randomly selected links.
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