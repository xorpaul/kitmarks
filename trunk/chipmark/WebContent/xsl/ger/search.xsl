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
  <!-- BODY_SEARCH_LINKS                                                     -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_SEARCH_LINKS">

    <span class="header">search results</span>

    <table width="100%" border="0" cellspacing="1" cellpadding="1">
      <tr>

        <td>

          <xsl:if test="xml/RejectSize = '1'">
            <span class="body">
              <xsl:text>The following word is considered common and was removed from your search: </xsl:text>
            </span>
          </xsl:if>

          <xsl:if test="xml/RejectSize = '2'">
            <span class="body">
              The following words were considered common and were removed from your search:
            </span>
          </xsl:if>

          <xsl:for-each select="xml/RejectList/RejectedString">
            <span class="body">
              <b>
                <xsl:apply-templates select="RejectedStr" mode="addBreaksString" />
              </b>
              <xsl:text> </xsl:text>
            </span>
          </xsl:for-each>
 <xsl:if test="xml/ResultSize = '-1'">
            <p>
              <span class="body">
                <font color="red">
                  Bitte geben Sie mindestens einen Suchbegriff mit 3 Zeichen ein!
                </font>
                <br />
              </span>
            </p>
          </xsl:if>
          <xsl:if test="xml/ResultSize = '0'">
            <p>
              <span class="body">
                <font color="black">
                  Your search for:
                  <xsl:apply-templates select="xml/SearchString" mode="addBreaks" />
                  - yielded no results.
                </font>
                <br />
              </span>
            </p>
          </xsl:if>

          <xsl:for-each select="xml/BookmarkList/Bookmark">
            <p>
				<xsl:if test="boolean(LoggedIn)">
					<span class="body">
						<xsl:element name="a">
							<xsl:attribute name="href">
								AddLink?submitBtn=prefilled&amp;linkName=<xsl:value-of select="EncodedName"/>&amp;linkURL=<xsl:value-of select="EncodedLink"/>
							</xsl:attribute>
							<xsl:attribute name="title">
								Add This Link
							</xsl:attribute>
							<xsl:attribute name="style">font-size: smaller;</xsl:attribute>
							<img src="images/add_link_15.png" border="0"/>
						</xsl:element>
					</span>
				</xsl:if>
				<span class="body">
                <xsl:element name="a">
                  <xsl:attribute name="href">
                    <xsl:value-of select="URL"/>
                  </xsl:attribute>
                  <xsl:apply-templates select="Title" mode="addBreaksString" />
                </xsl:element>
              </span>
              <br />
				<!-- Insert Description if there is non-whitespace text. -->
				<xsl:if test="Description != ''">					
					<span class="body">
						<xsl:apply-templates select="Description" mode="addBreaksString" />
						<br/>
					</span>
				</xsl:if>				
				<span class="url">
                <xsl:apply-templates select="URL" mode="addBreaks" />
              </span>              

              


            </p>
          </xsl:for-each>
        </td>

      </tr>
    </table>
    <br />
  </xsl:template>


  <!-- ===================================================================== -->
  <!-- SEARCH_SIMPLE                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="SEARCH_SIMPLE">
    <form id="form1" name="form1" method="get" action="/SearchLinks">
      <table>
        <tr id="search_row">
          <td width="110" align="center">
            <xsl:element name="input">
              <xsl:attribute name="type">text</xsl:attribute>
              <xsl:attribute name="name">search</xsl:attribute>
              <xsl:attribute name="value">
                <xsl:value-of select="xml/SearchString"/>
              </xsl:attribute>
              <xsl:attribute name="size">20</xsl:attribute>
            </xsl:element>
          </td>
          <td>
            <input type="submit" name="submit" value="search"/>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <p class="asearch_text">
              <a href="Main?target=advancedSearch">advanced search</a>
            </p>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <p class="asearch_text">
              <a href="MostRecentlyAdded">recently added chipmarks</a>
            </p>
          </td>
        </tr>
        <tr id="username_row" width="80" style="display: none;">
          <td align="right" width="110">
            <p class="form_elements">username</p>
          </td>
          <td>
            <xsl:element name="input">
              <xsl:attribute name="type">text</xsl:attribute>
              <xsl:attribute name="name">userid</xsl:attribute>
              <xsl:attribute name="value">
                <xsl:value-of select="xml/UserNameBox"/>
              </xsl:attribute>
              <xsl:attribute name="size">10</xsl:attribute>
            </xsl:element>
          </td>
        </tr>
        <tr id="checkbox_row1" style="display: none;">
          <td align="right">
            <xsl:element name="input">
              <xsl:attribute name="type">checkbox</xsl:attribute>
              <xsl:attribute name="name">url</xsl:attribute>
              <xsl:attribute name="checked">checked</xsl:attribute>
              <xsl:attribute name="id">url_cb</xsl:attribute>
              <xsl:if test="/xml/LinkURLBox='checked'">
                <xsl:attribute name="checked">
                  <xsl:value-of select="xml/LinkURLBox"/>
                </xsl:attribute>
              </xsl:if>
            </xsl:element>
          </td>
          <td class="form_elements">url</td>
        </tr>
        <tr id="checkbox_row2" style="display: none;">
          <td align="right">
            <xsl:element name="input">
              <xsl:attribute name="type">checkbox</xsl:attribute>
              <xsl:attribute name="checked">checked</xsl:attribute>
              <xsl:attribute name="name">desc</xsl:attribute>
              <xsl:attribute name="id">descr_cb</xsl:attribute>
              <xsl:if test="/xml/DescriptionBox='checked'">
                <xsl:attribute name="checked">
                  <xsl:value-of select="xml/DescriptionBox"/>
                </xsl:attribute>
              </xsl:if>
            </xsl:element>
          </td>
          <td class="form_elements">
            description
          </td>
        </tr>
        <tr id="checkbox_row3" style="display: none;">
          <td align="right">
            <xsl:element name="input">
              <xsl:attribute name="type">checkbox</xsl:attribute>
              <xsl:attribute name="name">name</xsl:attribute>
              <xsl:attribute name="id">name_cb</xsl:attribute>
              <xsl:attribute name="checked">checked</xsl:attribute>
              <xsl:if test="/xml/LinkNameBox='checked'">
                <xsl:attribute name="checked">
                  <xsl:value-of select="xml/LinkNameBox"/>
                </xsl:attribute>
              </xsl:if>
            </xsl:element>
          </td>
          <td class="form_elements">
            link name
          </td>
        </tr>
      </table>
    </form>

  </xsl:template>


  <!-- ===================================================================== -->
  <!-- SEARCH_ADVANCED                                                       -->
  <!-- ===================================================================== -->
  <xsl:template name="SEARCH_ADVANCED">
    <p class="header">&#160;&#160;advanced search</p>
    <form id="form1" name="form1" method="get" action="SearchLinks">
      <table>
        <tr id="search_row">
          <td align="right" width="80">
            <p class="form_elements">search</p>
          </td>
          <td width="110">
            <xsl:element name="input">
              <xsl:attribute name="type">text</xsl:attribute>
              <xsl:attribute name="name">search</xsl:attribute>
              <xsl:attribute name="value">
                <xsl:value-of select="xml/SearchString"/>
              </xsl:attribute>
              <xsl:attribute name="size">10</xsl:attribute>
            </xsl:element>
          </td>
        </tr>
        <tr id="username_row_a" width="80">
          <td align="right" width="110">
            <p class="form_elements">username</p>
          </td>
          <td>
            <xsl:element name="input">
              <xsl:attribute name="type">text</xsl:attribute>
              <xsl:attribute name="name">userid</xsl:attribute>
              <xsl:attribute name="value">
                <xsl:value-of select="xml/UserNameBox"/>
              </xsl:attribute>
              <xsl:attribute name="size">10</xsl:attribute>
            </xsl:element>
          </td>
        </tr>
        <tr id="checkbox_row1_a">
          <td align="right">
            <xsl:element name="input">
              <xsl:attribute name="type">checkbox</xsl:attribute>
              <xsl:attribute name="name">url</xsl:attribute>
              <xsl:attribute name="checked">checked</xsl:attribute>
              <xsl:attribute name="id">url_cb_a</xsl:attribute>
              <xsl:if test="/xml/LinkURLBox='checked'">
                <xsl:attribute name="checked">
                  <xsl:value-of select="xml/LinkURLBox"/>
                </xsl:attribute>
              </xsl:if>
            </xsl:element>
          </td>
          <td class="form_elements">url</td>
        </tr>
        <tr id="checkbox_row2_a">
          <td align="right">
            <xsl:element name="input">
              <xsl:attribute name="type">checkbox</xsl:attribute>
              <xsl:attribute name="checked">checked</xsl:attribute>
              <xsl:attribute name="name">desc</xsl:attribute>
              <xsl:attribute name="id">descr_cb_a</xsl:attribute>
              <xsl:if test="/xml/DescriptionBox='checked'">
                <xsl:attribute name="checked">
                  <xsl:value-of select="xml/DescriptionBox"/>
                </xsl:attribute>
              </xsl:if>
            </xsl:element>
          </td>
          <td class="form_elements">
            description
          </td>
        </tr>
        <tr id="checkbox_row3_a">
          <td align="right">
            <xsl:element name="input">
              <xsl:attribute name="type">checkbox</xsl:attribute>
              <xsl:attribute name="name">name</xsl:attribute>
              <xsl:attribute name="id">name_cb_a</xsl:attribute>
              <xsl:attribute name="checked">checked</xsl:attribute>
              <xsl:if test="/xml/LinkNameBox='checked'">
                <xsl:attribute name="checked">
                  <xsl:value-of select="xml/LinkNameBox"/>
                </xsl:attribute>
              </xsl:if>
            </xsl:element>
          </td>
          <td class="form_elements">
            link name
          </td>
        </tr>
        <tr id="submit_row">
          <td align="right"></td>
          <td>
            <input type="submit" name="submit" value="Search" />
          </td>
        </tr>
        <tr>
          <td></td>
        </tr>
      </table>
    </form>

  </xsl:template>

  <!-- ===================================================================== -->
  <!-- SEARCH_USER                                                           -->
  <!-- ===================================================================== -->
  <xsl:template name="SEARCH_USER">

    <span class="body">search my chipmarks only</span>
    <form method="post" action="SearchUser" name="SearchUserChipmarks">
      <table>
        <tr>
          <td>
            <xsl:element name="input">
              <xsl:attribute name="type">text</xsl:attribute>
              <xsl:attribute name="name">search</xsl:attribute>
              <xsl:attribute name="value">
                <xsl:value-of select="xml/SearchString"/>
              </xsl:attribute>
            </xsl:element>
          </td>
          <td>
            <input type="hidden" name="url" value="checked" />
            <input type="hidden" name="desc" value="checked" />
            <input type="hidden" name="name" value="checked" />
            <input type="submit" name="search" value="search" />
            <br />
          </td>
        </tr>
      </table>
    </form>

  </xsl:template>



  <!-- ===================================================================== -->
  <!-- BODY_SEARCH_USER                                                      -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_SEARCH_USER">

    <p class="header">search my chipmarks</p>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>

          <!-- Add Search -->
          <xsl:call-template name="SEARCH_USER" />

          <xsl:if test="xml/RejectSize = '1'">
            <span class="body">
              <xsl:text>The following word is considered common and was removed from your search: </xsl:text>
            </span>
          </xsl:if>

          <xsl:if test="xml/RejectSize = '2'">
            <span class="body">
              The following words were considered common and were removed from your search:
            </span>
          </xsl:if>

          <xsl:for-each select="xml/RejectList/RejectedString">
            <span class="body">
              <b>
                <xsl:apply-templates select="RejectedStr" mode="addBreaksString" />
              </b>
              <xsl:text> </xsl:text>
            </span>
          </xsl:for-each>


          <xsl:if test="xml/Message != ''">
            <p>
              <span class="body">
                <font color="black">
                  <xsl:apply-templates select="xml/Message" mode="addBreaks" />
                </font>
                <br />
              </span>
            </p>
          </xsl:if>

          <!-- start bookmark list -->
          <xsl:for-each select="xml/BookmarkList/Bookmark">

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
                  EditLink?linkID=<xsl:value-of select="@id"/>
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
                  javascript:if(confirm('Diese Lesezeichen wirklich löschen?')) document.location.href='RemoveLink?linkID=<xsl:value-of select="@id"/>'
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
                    /ViewLinks?labelNames=
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


</xsl:stylesheet>