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
  <!-- BODY_BUDDIES                                                          -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_BUDDIES">
    <p class="title">buddies</p>
    <center>
      <script language="JavaScript">
        showBuddyRequests(1);
        initBuddies();
      </script>
      <noscript>
        <span class="small_text">
          This feature requires that javascript is enabled.  If you wish to use this feature, please <a href="Main?target=jshowto">enable javascript</a> in your browser.
          <br/>
        </span>
      </noscript>
    </center>
  </xsl:template>


  <!-- ===================================================================== -->
  <!-- BODY_MANAGE_CHIPMARKS (LABEL VIEW)                                    -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_MANAGE_CHIPMARKS_LABEL_VIEW">

	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="header" width="200">&#160;&#160;my chipmarks</td>
        <td class="body">
          <a href="Manage">folder view</a>
          <b> :: label view</b>
        </td>
      </tr>
    </table>

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>
          <br/>
          <span class="body">
            <a href="/AddLink">add chipmark</a>&#160;&#160;
          </span>
          <span class="sm_text">- chipmark a page.</span>
          <hr/>
          <!-- start bookmark list -->
          <xsl:for-each select="xml/BookmarkList/Bookmark">
            <xsl:sort order="ascending" select="Title"/>
            <p>
              <span class="body">
                <!-- Insert Link -->
                <xsl:element name="a">
                  <!-- <xsl:attribute name="onClick">window.location="/HitCount?linkID=<xsl:value-of select="@id"/>";return false</xsl:attribute> -->
                  <!-- <xsl:attribute name="href">/HitCount?linkID=<xsl:value-of select="@id"/></xsl:attribute> -->
                  <xsl:attribute name="href">
                    <xsl:value-of select="Link" />
                  </xsl:attribute>
                  <xsl:apply-templates select="Title" mode="addBreaksString" />

                </xsl:element>

                <!-- Add A Space -->
                <xsl:text> </xsl:text>
              </span>
              <!-- Insert Edit -->
              <xsl:element name="a">

                <xsl:attribute name="href">
                  /EditLink?linkID=<xsl:value-of select="@id"/>
                </xsl:attribute>

                <span class="edit">
                  <xsl:text>[edit]</xsl:text>
                </span>
              </xsl:element>

              <!-- Add A Space -->
              <xsl:text> </xsl:text>

              <!-- Insert Delete -->
              <xsl:element name="a">

                <xsl:attribute name="href">javascript:void(0);</xsl:attribute>

                <xsl:attribute name="onClick">
                  javascript:if(confirm('Are you sure you want to delete this chipmark?')) document.location.href='/RemoveLink?linkID=<xsl:value-of select="@id"/>'
                </xsl:attribute>

                <span class="edit">
                  <xsl:text>[delete]</xsl:text>

                </span>
              </xsl:element>

              <br/>

              <!-- Insert URL if there is non-whitespace text. -->
              <xsl:if test="Link != ''">
                <span class="url">
                  <xsl:apply-templates select="Link" mode="addBreaks" />
                  <br/>
                </span>
              </xsl:if>

              <!-- Insert Description if there is non-whitespace text. -->
              <xsl:if test="Description != ''">
                <span class="body">
                  <xsl:apply-templates select="Description" mode="addBreaksString" />
                  <br/>
                </span>
              </xsl:if>

              <!-- List Tags -->
              <xsl:for-each select="LabelList/Label">
                <xsl:sort order="ascending" select="@name"/>
                <xsl:element name="a">
                  <xsl:attribute name="href">
                    ViewLinks?labelNames=
                    <xsl:call-template name="replace-string">
                      <xsl:with-param name="text" select="@name"/>
                      <xsl:with-param name="from" select="'&amp;'"/>
                      <xsl:with-param name="to" select="'%26'"/>
                    </xsl:call-template>
                  </xsl:attribute>
                  <span class="tags">
                    <xsl:choose>
                      <xsl:when test="string-length(@name)&lt;15">
                        <xsl:value-of select="@name"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of select="substring(@name,0,15)"/>...
                      </xsl:otherwise>
                    </xsl:choose>
                  </span>
                </xsl:element>

                <!-- If this is not the last node, add a comma and a space. -->
                <xsl:if test="position() != last()">
                  ,<xsl:text> </xsl:text>
                </xsl:if>
              </xsl:for-each>

            </p>

          </xsl:for-each>
          <!-- end bookmark list -->
        </td>
      </tr>
    </table>
    <br/>
  </xsl:template>

  <!-- ===================================================================== -->
  <!-- FACEBOOK MANAGE                                                       -->
  <!-- ===================================================================== -->
  <xsl:template name="FACEBOOK_MANAGE">
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
        <style type="text/css">
         
          body {
  	        background-color: #FFFFFF;
            height:400px;
            margin: 0px;
            padding:0px;
            width:635px;
          }
          html {  	        
            height:400px;
            margin:0px;
            padding:0px;
            width:100%;
          }

        
        </style>
      </head>
      <body>
        <!--
	Some javascript necessary for this page to work is loaded
	automatically in the COMMON_HEAD_ELEMENTS template only
	when this page is targeted.
	-->

        <span id="imageContainer">
          <img id="cursorImage" name="cursorImage" src="images/folder_closed.png" />
        </span>
        <div id="manage_container" style="height: 600px;">
          <script language="JavaScript" type="text/javascript">
            setupManagement('manage_container');
          </script>
          <noscript>
            <span class="small_text">
              This feature requires that javascript is enabled.  If you wish to use this feature, please <a href="Main?target=jshowto">enable javascript</a> in your browser.
              <br/>
            </span>
          </noscript>
        </div>
        <div style="width: 99.9%;">
        	<table align="right" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" valign="middle">
						<img src="images/circle_rightarrow.png" border="0"/>
					</td>
					<td valign="middle">
						<span class="small_text">
							<a href="javascript:facebookChipmarkLogout();">log <xsl:value-of select="xml/LoggedInAs"/> out from chipmark</a>
						</span>
					</td>
				</tr>
			</table>
			
		</div>
      </body>
    </html>
  </xsl:template>


  <!-- ===================================================================== -->
  <!-- PLUGIN MANAGE                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="PLUGIN_MANAGE">
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
        <style type="text/css">
         
          body {
  	        background-color: #FFFFFF;
            height:500px;
            width:100%;
          }
          html {  	        
            height:500px;
            width:100%;
          }

        
        </style>
      </head>
      <body>
        <!--
	Some javascript necessary for this page to work is loaded
	automatically in the COMMON_HEAD_ELEMENTS template only
	when this page is targeted.
	-->

        <span id="imageContainer">
          <img id="cursorImage" name="cursorImage" src="images/folder_closed.png" />
        </span>
        <div id="manage_container">
          <script language="JavaScript" type="text/javascript">
            setupManagement('manage_container');
          </script>
          <noscript>
            <span class="small_text">
              This feature requires that javascript is enabled.  If you wish to use this feature, please <a href="Main?target=jshowto">enable javascript</a> in your browser.
              <br/>
            </span>
          </noscript>
        </div>
      </body>
    </html>
  </xsl:template>
    
  
  
  <!-- ===================================================================== -->
  <!-- LABELS                                                                -->
  <!-- ===================================================================== -->
  <xsl:template name="LABELS">
    <table width="100%" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td>
          <!-- start tags list -->
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="20">
                <img src="images/spacer.gif" width="1" height="1" alt=""/>
              </td>
              <td width="121">
                <p class="body">
                  <a href="ViewLinks">all my chipmarks</a>
                  <br />

                  <xsl:for-each select="xml/AllLabelList/Label">

                    <xsl:sort order="ascending" select="@name"/>
                    <xsl:element name="a">
                      <xsl:attribute name="href">
                        /ViewLinks?labelNames=
                        <xsl:call-template name="replace-string">
                          <xsl:with-param name="text" select="@name"/>
                          <xsl:with-param name="from" select="'&amp;'"/>
                          <xsl:with-param name="to" select="'%26'"/>
                        </xsl:call-template>
                      </xsl:attribute>

                      <xsl:choose>
                        <xsl:when test="string-length(@name)&lt;15">
                          <xsl:value-of select="@name"/>
                        </xsl:when>
                        <xsl:otherwise>
                          <xsl:value-of select="substring(@name,0,15)"/>...
                        </xsl:otherwise>
                      </xsl:choose>
                    </xsl:element>
                    <br/>
                  </xsl:for-each>
                </p>
              </td>
            </tr>
          </table>
          <!-- end tags list -->
        </td>
      </tr>
    </table>
  </xsl:template>

  <!-- ===================================================================== -->
  <!-- BODY_MANAGE_CHIPMARKS (FOLDER VIEW)                                   -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_MANAGE_CHIPMARKS_FOLDER_VIEW">

    <!--
	Some javascript necessary for this page to work is loaded
	automatically in the COMMON_HEAD_ELEMENTS template only
	when this page is targeted.
	-->

	  <span id="imageContainer">
		  <img id="cursorImage" name="cursorImage" src="images/folder_closed.png" />
	  </span>

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

	  <div id="manage_container">
		  <script language="JavaScript" type="text/javascript">
			setupManagement('manage_container');
		</script>
      <noscript>
        <span class="small_text">
          This feature requires that javascript is enabled.  If you wish to use this feature, please <a href="Main?target=jshowto">enable javascript</a> in your browser.
          <br/>
        </span>
      </noscript>
	</div>
	  
  
  </xsl:template>


  <!-- ===================================================================== -->
  <!-- BODY_EDIT_LINK                                                        -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_EDIT_LINK">
    <p class="header">edit a bookmark</p>

    <table width="100%" border="0" cellspacing="1" cellpadding="1">
      <!-- If the user didn't put in a URL or Name -->
      <xsl:if test="xml/Result != ''">
        <tr>
          <td align="center">
            <font color="red">
              You must fill in the <xsl:value-of select="xml/Result"/> field.
            </font>
            <br />
          </td>
        </tr>
      </xsl:if>

      <tr>
        <td>
          <p class="body">
            <form action="EditLink" method="POST">

              <table border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="25"/>
                  <td class="form_elements">name: </td>
                  <td class="form_elements">
                    <xsl:element name="input">
                      <xsl:attribute name="type">TEXT</xsl:attribute>
                      <xsl:attribute name="name">linkName</xsl:attribute>
                      <xsl:attribute name="maxlength">255</xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:value-of select="xml/Bookmark/Title"/>
                      </xsl:attribute>
                    </xsl:element>

                  </td>
                </tr>
                <tr>
                  <td width="25"/>
                  <td class="form_elements">url: </td>
                  <td class="form_elements">
                    <xsl:element name="input">
                      <xsl:attribute name="type">TEXT</xsl:attribute>
                      <xsl:attribute name="name">linkURL</xsl:attribute>
                      <xsl:attribute name="maxlength">255</xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:value-of select="xml/Bookmark/Link"/>
                      </xsl:attribute>
                    </xsl:element>
                  </td>
                </tr>
                <tr>
                  <td width="25"/>
                  <td class="form_elements">permissions: </td>
                  <td class="form_elements">

                    <xsl:element name="input">
                      <xsl:attribute name="type">RADIO</xsl:attribute>
                      <xsl:attribute name="name">linkPermission</xsl:attribute>
                      <xsl:attribute name="value">private</xsl:attribute>
                      <xsl:if test="xml/Bookmark/Permission != 'public'">
                        <xsl:attribute name="checked">CHECKED</xsl:attribute>
                      </xsl:if>
                    </xsl:element> private
                    <xsl:element name="input">
                      <xsl:attribute name="type">RADIO</xsl:attribute>
                      <xsl:attribute name="name">linkPermission</xsl:attribute>
                      <xsl:attribute name="value">public</xsl:attribute>
                      <xsl:if test="xml/Bookmark/Permission != 'private'">
                        <xsl:attribute name="checked">CHECKED</xsl:attribute>
                      </xsl:if>
                    </xsl:element> public

                  </td>
                </tr>
                <tr>
                  <td width="25"/>
                  <td class="form_elements">description: </td>
                  <td class="form_elements">
                    <xsl:element name="input">
                      <xsl:attribute name="type">TEXT</xsl:attribute>
                      <xsl:attribute name="name">linkDescription</xsl:attribute>
                      <xsl:attribute name="maxlength">512</xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:value-of select="xml/Bookmark/Description"/>
                      </xsl:attribute>
                    </xsl:element>
                  </td>
                </tr>
                <tr>
                  <td width="25"/>
                  <td class="form_elements">labels (comma-separated): </td>
                  <td class="form_elements">
                    <xsl:element name="input">
                      <xsl:attribute name="type">TEXT</xsl:attribute>
                      <xsl:attribute name="name">labelNames</xsl:attribute>
                      <xsl:attribute name="maxlength">255</xsl:attribute>
                      <xsl:attribute name="value">
                        <xsl:for-each select="xml/Bookmark/LabelList/Label">
                          <xsl:sort order="ascending" select="@name"/>
                          <xsl:value-of select="@name"/>
                          <!-- If this is not the last node, add a comma -->
                          <xsl:if test="position() != last()">,</xsl:if>
                        </xsl:for-each>
                      </xsl:attribute>
                    </xsl:element>
                  </td>
                </tr>
                <tr>
                  <td align="right" colspan="3" class="form_elements">
                    <INPUT TYPE="SUBMIT" name="submitBtn" VALUE="save chipmark"/>
                    <BR/>
                  </td>
                </tr>
              </table>
              <xsl:element name="input">
                <xsl:attribute name="type">HIDDEN</xsl:attribute>
                <xsl:attribute name="name">linkID</xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:value-of select="xml/Bookmark/@id"/>
                </xsl:attribute>
              </xsl:element>
            </form>
          </p>
        </td>
      </tr>
    </table>
    <br/>
  </xsl:template>



  <!-- ===================================================================== -->
  <!-- BODY_IMPORT                                                           -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_IMPORT">
	  <xsl:choose>
		  <xsl:when test="not(boolean(xml/LoggedInAs)) or xml/LoggedInAs = ''">
			  <xsl:call-template name="BODY_MAIN"/>
		  </xsl:when>
		  <xsl:otherwise>
			  <p class="header">import your browser bookmarks TEST ENGLISH</p>
			  <xsl:if test="//Result != 'SUCCESS'">
				  <p>
					  <font color="red">
						  <xsl:value-of select="//Result"/>
					  </font>
				  </p>
			  </xsl:if>

			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
					  <td>

						  <form method="post" enctype="multipart/form-data" action="Import">
							  <input type="radio" name="permission" value="public" checked="checked" />
							  <span class="body" style="padding-right: 5ex;">import as public</span>
							  <input type="radio" name="permission" value="private" />
							  <span class="body">import as private</span>
							  <p class="body">
								  <a href="#publicprivate">What do public and private mean?</a>
								  <br/>
								  <i>note: If you are importing bookmarks that were exported from Chipmark, your public/private designations will be retained for each link.</i>
							  </p>
							  <table>
								  <tr>
									  <td>
										  <input type="file" size="20" name="fname" />
									  </td>
									  <td>
										  <input type="Submit" value="import" />
									  </td>
								  </tr>
							  </table>
						  	  <p class="body">
								<em>Important: This may take a while depending on the amount of bookmarks you have. The maximum allowed file size is 10 MB.</em>
						  	  </p>
						  </form>
						  <br/>

						  <table>
							  <tr>
								  <td valign="top">
									  <img style="border: 0;" alt="firefox" src="images/ff_logo.gif"/><br />firefox
								  </td>
								  <td>
									  <p class="subheader" id="firefox">
										  how to import bookmarks for firefox users:
									  </p>
									  <ol>
										  <li>Select "Manage Bookmarks..." from the Bookmarks menu in Firefox.</li>
										  <li>A window titled "Bookmarks Manager" should open.</li>
										  <li>In the "Bookmarks Manager" window, select "Export..." from the File menu.</li>
										  <li>Save the file to disk, remembering where you saved the file.</li>
										  <li>Select "import as public" or "import as private" from above. For more information on public vs. private chipmarks see below.</li>
										  <li>Press the "Browse..." button above.</li>
										  <li>Locate and select the file you saved in step 4, then press the "Open" button.</li>
										  <li>Press the "import" button to import your bookmarks.</li>
									  </ol>
									  <br/>
								  </td>
							  </tr>
							  <tr>
								  <td valign="top">
									  <img style="border: 0;" alt="mozilla" src="images/mozilla_logo.gif"/><br />mozilla<br /><br />
									  <img style="border: 0;" alt="netscape" src="images/netscape_logo.gif"/><br />netscape
								  </td>
								  <td>
									  <p class="subheader" id="mozilla">
										  how to import bookmarks for netscape/mozilla users:
									  </p>
									  <ol>
										  <li>Select "Manage Bookmarks" from the Bookmarks menu in Mozilla.</li>
										  <li>A window titled "Bookmarks Manager" should open.</li>
										  <li>In the "Bookmarks Manager" window, select "Export." from the Tools menu.</li>
										  <li>Save the file to disk, remembering where you saved the file.</li>
										  <li>Select "import as public" or "import as private" from above. For more information on public vs. private chipmarks see below.</li>
										  <li>Press the "Browse..." button above.</li>
										  <li>Locate and select the file you saved in step 4, then press the "Open" button.</li>
										  <li>Press the "import" button to import your bookmarks.</li>
									  </ol>
									  <br/>
								  </td>
							  </tr>
							  <tr>
								  <td valign="top">
									  <img style="border: 0pt none ;" alt="internet explorer" src="images/ie7_logo.gif" /><br />internet explorer
								  </td>
								  <td>
									  <p class="subheader" id="ie">
										  how to import bookmarks for internet explorer users:
									  </p>
									  <ol>
										  <li>Select "Import and Export." from the File menu in Internet Explorer.</li>
										  <li>Press the "Next" button.</li>
										  <li>In the left half of the window, select "Export Favorites" and press the "Next" button.</li>
										  <li>Select the folder which you would like to import (select Favorites to import all).</li>
										  <li>Save the file to disk by browsing to save point and pressing the "Next" button. (Remember where you saved the file for later on).</li>
										  <li>Press the "Finish" button.</li>
										  <li>Select "import as public" or "import as private" from above. For more information on public vs. private chipmarks see below.</li>
										  <li>Press the "Browse..." button above.</li>
										  <li>Locate and select the file you saved in step 5, then press the "Open" button.</li>
										  <li>Press the "import" button to import your bookmarks.</li>
									  </ol>
								  </td>
							  </tr>
						  </table>
						  <name id="publicprivate"/>
						  <p class="subheader">public vs. private chipmarks</p>
						  <p class="body">In Chipmark, you can have your bookmarks designated as public or private. If you mark them as public, there are certain features of Chipmark that will allow other users to see your bookmarks.</p>
						  <ol>
							  <li>
								  If someone knows your username, then they will be able to see all of your <em>public</em> bookmarks.
							  </li>
							  <li>
								  Your <em>public</em> bookmarks will appear in the "recently added chipmarks" list.
							  </li>
						  </ol>
						  <p class="body">You don't have to have all of your chipmarks be public or all of your chipmarks be private.  However, when importing a list of bookmarks, you need to specify whether they should default to public or to private.</p>

					  </td>
				  </tr>
			  </table>

		  </xsl:otherwise>
	  </xsl:choose>			  
  </xsl:template>


  <!-- ===================================================================== -->
  <!-- BODY_EXPORT                                                           -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_EXPORT">
	  <xsl:choose>
		  <xsl:when test="not(boolean(xml/LoggedInAs)) or xml/LoggedInAs = ''">
			  <xsl:call-template name="BODY_MAIN"/>
		  </xsl:when>
		  <xsl:otherwise>
			  <p class="header">export your chipmarks</p>
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
					  <td>
						  <table>
							  <tr>
								  <td valign="top">
								  </td>
								  <td>

								  </td>
							  </tr>
							  <tr>
								  <td valign="top">
									  <img style="border: 0;" alt="firefox" src="images/ff_logo.gif"/><br />firefox
								  </td>
								  <td>
									  <p class="subheader" id="firefox">how to export your chipmarks to firefox:</p>
									  <form method="post" action="Export">
										  <input type="submit" name="submitBtn" value="download my chipmarks"/>
									  </form>
									  <ol>
										  <li>Press the "download my chipmarks" button above, and save the file to disk. (Remember where you saved the file.)</li>
										  <li>Select "Manage Bookmarks..." from the Bookmarks menu in Firefox.</li>
										  <li>A window titled "Bookmarks Manager" should open.</li>
										  <li>In the "Bookmarks Manager" window, select "Import..." from the File menu.</li>
										  <li>Locate the file you saved in the first step and press the open button.</li>
										  <li>Your Chipmarks should now appear under your Bookmarks menu.</li>
									  </ol>
									  <br/>
								  </td>
							  </tr>
							  <tr>
								  <td valign="top">
									  <img style="border: 0;" alt="mozilla" src="images/mozilla_logo.gif"/><br />mozilla<br /><br />
									  <img style="border: 0;" alt="netscape" src="images/netscape_logo.gif"/><br />netscape
								  </td>
								  <td>
									  <p class="subheader" id="mozilla">
										  how to export your chipmarks to netscape/mozilla:
									  </p>
									  <form method="post" action="Export">
										  <input type="submit" name="submitBtn" value="download my chipmarks"/>
									  </form>
									  <ol>
										  <li>Press the "download my chipmarks" button above, and save the file to disk. (Remember where you saved the file.)</li>
										  <li>Select "Manage Bookmarks..." from the Bookmarks menu in Mozilla.</li>
										  <li>A window titled "Bookmarks Manager" should open.</li>
										  <li>In the "Bookmarks Manager" window, select "Import..." from the Tools menu.</li>
										  <li>Locate the file you saved in the first step and press the open button.</li>
										  <li>Your Chipmarks should now appear under your Bookmarks menu.</li>
									  </ol>
									  <br/>
								  </td>
							  </tr>
							  <tr>
								  <td valign="top">
									  <img style="border: 0pt none ;" alt="internet explorer" src="images/ie7_logo.gif" /><br />internet explorer
								  </td>
								  <td>
									  <p class="subheader" id="ie">
										  how to export your chipmarks to internet explorer:
									  </p>
									  <form method="post" action="Export">
										  <input type="submit" name="submitBtn" value="download my chipmarks"/>
									  </form>
									  <ol>
										  <li>Press the "download my chipmarks" button above, and save the file to disk. (Remember where you saved the file.)</li>
										  <li>Select "Import and Export..." from the File menu in Internet Explorer.</li>
										  <li>Press the "Next" button.</li>
										  <li>In the left half of the window, select "Import Favorites" and press the "Next" button.</li>
										  <li>Locate the file you saved in the first step and press the open button.</li>
										  <li>Press the next button.</li>
										  <li>If you would like your Chipmarks placed in a certain folder, select the folder, otherwise select Favorites.</li>
										  <li>Press the next button.</li>
										  <li>Press the "Finish" button.</li>
										  <li>A Success message should pop up.</li>
										  <li>Your Chipmarks should now appear under your Favorites menu.</li>
									  </ol>
								  </td>
							  </tr>
							  <tr>
								  <td valign="top">
								  </td>
							  </tr>
						  </table>
					  </td>
				  </tr>
			  </table>
		  </xsl:otherwise>
	  </xsl:choose>
  </xsl:template>


  <!-- ===================================================================== -->
  <!-- BODY_ADD_LINK                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_ADD_LINK">
    <p class="header">add a chipmark</p>

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <!-- If the user didn't put in a URL or Name -->
      <xsl:if test="xml/Result != ''">
        <tr>
          <td align="center">
            <font color="red">
              <xsl:value-of select="xml/Result"/>
            </font>
            <br />
          </td>
        </tr>
      </xsl:if>

      <tr>
        <td>
          <p class="body">
            <form method="POST" action="AddLink" name="addLinkForm" onsubmit="return userURLs.doChipmark(addProtocol(addLinkForm.elements['linkURL'].value));">
              <table border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="25"/>
                  <td class="form_elements">name: </td>
                  <td class="form_elements">

                    <xsl:element name="input">
                      <xsl:attribute name="type">TEXT</xsl:attribute>
                      <xsl:attribute name="name">linkName</xsl:attribute>
                      <xsl:attribute name="maxlength">255</xsl:attribute>
                      <xsl:if test="xml/Name != ''">
                        <xsl:attribute name="value">
                          <xsl:value-of select="xml/Name"/>
                        </xsl:attribute>
                      </xsl:if>
                    </xsl:element>
                  </td>
                </tr>
                <tr>
                  <td width="25"/>
                  <td class="form_elements">url: </td>
                  <td class="form_elements">

                    <xsl:element name="input">
                      <xsl:attribute name="type">TEXT</xsl:attribute>
                      <xsl:attribute name="name">linkURL</xsl:attribute>
                      <xsl:attribute name="maxlength">255</xsl:attribute>
                      <xsl:if test="xml/URL != ''">
                        <xsl:attribute name="value">
                          <xsl:value-of select="xml/URL"/>
                        </xsl:attribute>
                      </xsl:if>
                    </xsl:element>
                  </td>
                </tr>
                <tr>
                  <td width="25"/>
                  <td class="form_elements">permissions: </td>
                  <td class="form_elements">

                    <xsl:element name="input">
                      <xsl:attribute name="type">radio</xsl:attribute>
                      <xsl:attribute name="name">linkPermission</xsl:attribute>
                      <xsl:attribute name="value">private</xsl:attribute>
                    </xsl:element>
                    private
                    <xsl:element name="input">
                      <xsl:attribute name="type">radio</xsl:attribute>
                      <xsl:attribute name="name">linkPermission</xsl:attribute>
                      <xsl:attribute name="value">public</xsl:attribute>
                      <xsl:attribute name="checked">checked</xsl:attribute>
                    </xsl:element>
                    public
                  </td>
                </tr>
                <tr>
                  <td width="25"/>
                  <td class="form_elements">description: </td>
                  <td class="form_elements">
                    <!-- <INPUT NAME="linkDescription" TYPE="TEXT"/>-->
                    <xsl:element name="input">
                      <xsl:attribute name="type">TEXT</xsl:attribute>
                      <xsl:attribute name="name">linkDescription</xsl:attribute>
                      <xsl:attribute name="maxlength">65535</xsl:attribute>
                      <xsl:if test="xml/Description != ''">
                        <xsl:attribute name="value">
                          <xsl:value-of select="xml/Description"/>
                        </xsl:attribute>
                      </xsl:if>
                    </xsl:element>
                  </td>
                </tr>
                <tr>
                  <td width="25"/>
                  <td class="form_elements">labels (comma-separated): </td>
                  <td class="form_elements">
                    <!-- <INPUT NAME="labelNames" TYPE="TEXT"/>-->
                    <xsl:element name="input">
						<xsl:attribute name="id">labels</xsl:attribute>
						<xsl:attribute name="type">TEXT</xsl:attribute>
                      <xsl:attribute name="name">labelNames</xsl:attribute>
                      <xsl:attribute name="maxlength">255</xsl:attribute>
                      <xsl:if test="xml/Labels != ''">
                        <xsl:attribute name="value">
                          <xsl:value-of select="xml/Labels"/>
                        </xsl:attribute>
                      </xsl:if>
                    </xsl:element>
                  </td>
                </tr>
                <tr>
                  <td align="right" colspan="3" class="form_elements">
                    <INPUT VALUE="add chipmark" name="submitBtn" TYPE="SUBMIT"/>
                  </td>
                </tr>
              </table>
            </form>
          </p>
        </td>
      </tr>
    </table>
    <br/>
    <SCRIPT language="JavaScript"><![CDATA[
				initAutosuggestElement(document.getElementById("labels"));
      ]]></SCRIPT>
  </xsl:template>


</xsl:stylesheet>
