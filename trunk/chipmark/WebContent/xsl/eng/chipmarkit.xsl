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
  <!-- BODY_ADD_LINK                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="CHIPMARKIT_ADD_LINK">
    <xsl:choose>
      <xsl:when test="not(boolean(xml/LoggedInAs)) or xml/LoggedInAs = ''">
        login to chipmark
      </xsl:when>
      <xsl:otherwise>
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
                <form method="POST" action="/AddLink" name="addLinkForm" onsubmit="return userURLs.doChipmark(addProtocol(addLinkForm.elements['linkURL'].value));">
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
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>