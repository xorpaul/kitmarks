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
  <!-- BODY_FEATURES                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_FEATURES">

          <p class="sub_header">what can Chipmark do?</p>
          <p class="sub_content">
            Below is a detailed list of all Chipmark's current capabilites and how they work.  If you think of a new feature for Chipmark you can contact  us at
            <a href="mailto:webmaster@chipmark.com">webmaster@chipmark.com</a>.  Click on the icons below to explore that interface's features.
          </p>
          <br/>
          <br/>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td width="33%" align="center">
          <a href="Main?target=features_web">
            <img src="images/web_logo.gif" style="border: 0;"/><br />Web
          </a>
        </td>
        <td width="33%" align="center">
          <a href="Main?target=features_firefox">
            <img src="images/ff_logo.gif" style="border: 0;"/><br />Firefox
          </a>
        </td>
        <td width="33%" align="center">
          <a href="Main?target=features_ie">
            <img src="images/ie7_logo.gif" style="border: 0;"/><br />Internet Explorer
          </a>
        </td>
      </tr>
    </table>

  </xsl:template>

  <!-- ===================================================================== -->
  <!-- BODY_FEATURES_WEB                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_FEATURES_WEB">

    
          <p class="sub_header">general</p><p class="sub_content">Although it is the most cubersome, the web interface
          offers the most functionality.  In fact, all the interfaces rely on the web interface.</p><p class="sub_header">specifics</p>
          <ul class="long_list">
            <li>There are two different views for managing your chipmarks.</li>
            <ol>
              <li>
                The default view is <b>the folder view</b>. In this view your chipmarks
                are organized within folders. From this view you can add a chipmark, edit a
                chipmark's properties, delete a chipmark, copy a chipmark, move a chipmark,
                send a chipmark to a buddy, add a folder, edit a folder's name, or move a folder.
              </li>
              <li>
                The other view is <b>the label view</b>. In this view you can sort through
                your chipmarks by label, in a similar fashion to gmail. If you add a link
                in this view, it will be placed directly in your My Chipmarks folder.
              </li>
            </ol>
            <li>Chipmark Properties - Each chipmark has the following properties fields:</li>
            <ol>
              <li>
                <b>Name</b> - The name of the chipmark that is displayed when the chipmark is
                dealt with.
              </li>
              <li>
                <b>URL</b> - The chipmark's url.
              </li>
              <li>
                <b>Permissions</b> - Determines whether the chipmark will be public or private
                for chipmark-wide searching functionalities.
              </li>
              <li>
                <b>Description</b> - An optional field for further description of the chipmark
              </li>
              <li>
                <b>Labels</b> - A comma-separated list of labels for the label view of your chipmarks.
              </li>

            </ol>
            <li>Changing your password and/or email address is available at the  my account page.</li>
            <li>
              A webpage with instructions for and links to download Chipmark's
              Firefox or Internet Explorer extension.
            </li>
            <li>
              There is a list of the Most Recently Added Chipmarks available from the link near the search bar.
              Chipmarks from this page can be easily added to a user's chipmarks.
            </li>
            <li>
              There is a list of the Top 10 Chipmarks available from the main page. Chipmarks
              from this page can be easily added to a user's chipmarks.
            </li>
            <li>
              From the manage page, you can reach a page with instructions on
              how to import your existing bookmarks from Firefox, Internet Explorer, another
              Chipmark account, or a few other browsers.
            </li>
            <li>
              From the manage page, you can reach a page with instructions on
              how to export your chipmarks for use in another Chipmark account, Firefox, Internet Explorer,
              or other browsers.
            </li>
            <li>
              If you forget your password, there is a link to a Forgot
              Password page where you can reset your email based upon the email address
              your account is registered to.
            </li>
            <li>
              From the main page you can search every Chipmark user's
              public chipmarks. From your manage page you can also do a quick search of
              your own links. The search supports quoted searching and operates in a similar manner to
              other search engines.
            </li>
            <li>From the my buddies page, you can add, edit, and delete buddies.</li>
            <li>
              From the manage page, you can send chipmarks to buddies and view chipmarks in your
              inbox.
            </li>
            <li>New items in your inbox that you have not clicked on are bold.</li>
          </ul>
  </xsl:template>


  <!-- ===================================================================== -->

  <!-- BODY_FEATURES_IE                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_FEATURES_IE">
          <p class="sub_header">general</p><p class="sub_content">The IE pluggin integrates all of Chipmark's abilties right into the world's most popular browser. Note, sometimes the pluggin will need to direct you back to the site.</p>
<p class="sub_header">specifics</p>
          <ul class="long_list">
            <li>View and navigate to your chipmarks from a drop down menu.</li>
            <li>
              Have chipmarks displayed in the toolbar that all have a configurable label
              for quick navigating.
            </li>
            <li>Add a chipmark of the page you are currently viewing.</li>
            <li>Obtain quick access to the many features of Chipmark.com.</li>
            <li>Add Folders.</li>
            <li>Add Labels.</li>
            <li>Send a chipmark of the page you are currently viewing to one or more buddies.</li>
            <li>Manage your buddies through the use of the web-based Chipmark.com.</li>
            <li>Reconnect button allows you to easily log back into chipmark. </li>
          </ul>
       
  </xsl:template>


  <!-- ===================================================================== -->
  <!-- BODY_FEATURES_FIREFOX                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_FEATURES_FIREFOX">

    
          <p class="sub_header">general</p><p class="sub_content">The Firefox extension integrates all of Chipmark's abilties right into Firefox's toolbar.  Note, sometimes the pluggin will need to direct you back to the site.</p>

<p class="sub_header">specifics</p>
          <ul class="long_list">
            <li>Register for the Chipmark service.</li>
            <li>Request a forgotten password.</li>
            <li>Login to multiple Chipmark accounts.</li>
            <li>View and navigate to your chipmarks from a drop down menu.</li>
            <li>
              Have chipmarks displayed in the Chipmark Toolbar, similar to Firefox's
              Bookmark Toolbar.
            </li>
            <li>Add a chipmark of the page you are currently viewing.</li>
            <li>Edit the properties of any of your chipmarks.</li>
            <li>Move and/or delete any of your chipmarks.</li>
            <li>Add, edit, move, and delete folders for the organization of your chipmarks.</li>
            <li>Add labels for the categorization of your chipmarks.</li>
            <li>Open all of the chipmarks in a single folder in a set of tabs.</li>
            <li>
              Search through your chipmarks using a filter that works as you type in
              the Chipmark sidebar.
            </li>
            <li>Filter your chipmarks by label in the Chipmark sidebar.</li>
            <li>
              Manage your chipmarks through the use of the web-based Chipmark.com chipmark
              manager.
            </li>
            <li>Import your bookmarks into Chipmark.</li>
            <li>Export your chipmarks as a HTML file.</li>
            <li>
              View (or choose not to view) the Top 10 Chipmarks right in your Chipmarks
              menu.
            </li>
            <li>
              View (or choose not to view) the most recently added Chipmarks right in
              your Chipmarks menu.
            </li>
            <li>Hide your browser's Bookmarks menu, if you choose to do so</li>
            <li>Obtain quick access to Chipmark.com.</li>
            <li>
              The Firefox plug-in auto-updates itself, so you don't have to worry about finding
              out if there is a new version available.
            </li>
            <li>Choose to view a websites icon next to your chipmarks.</li>
            <li>Send a chipmark of the page you are currently viewing to one or more buddies.</li>
            <li>Send a chipmark from your folders to one or more buddies.</li>
            <li>View chipmarks in your "inbox" that were sent to you from other users.</li>
            <li>New items in your inbox that you have not clicked on are bold.</li>
            <li>Add a buddy when you choose to send a page.</li>
            <li>Manage your buddies through the use of the web-based Chipmark.com.</li>
          </ul>
  </xsl:template>


</xsl:stylesheet>