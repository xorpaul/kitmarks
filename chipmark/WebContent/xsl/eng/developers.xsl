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
  <!-- BODY_DEVELOPERS                                                       -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_DEVELOPERS">
     <p class="sub_header">forum</p>
     <p class="sub_content">Chipmark now has a <a href="http://www.chipmark.com:8082/forum/">forum</a>! This is the place to go if you have comments or suggestions for the chipmark developers. There is an area to discuss bugs, as well an a section for more general discussion. Best of all, if you are already registered with chipmark then you can use the same login and password to leave comments on the forum!</p>
     <p class="sub_header">blog</p>
    <p class="sub_content">Chipmark also has a development <a 
href="http://www.chipmark.com:8082/blog/">blog</a>. The blog is public and anyone can register. This is where the chipmark team will post information about updates, releases and new features.</p>
    <p class="sub_header">api</p>
    <p class="sub_content">As an open-source project, chipmark has an open <a href="/Main?target=api">API</a>.  Please feel free to explore the API and develop your own personal modifications or applications.  For questions about the API, please check out the <a href="http://www.chipmark.com:8082/">blog</a> or the <a href="/Main?target=license">license</a>.</p>
    <p class="sub_header">files</p>
    <p class="sub_content">The source code for chipmark can be found <a href="/chipmark_source.tar.gz">here</a> (<a href="/chipmark_source.md5sum">md5</a>)</p>
  </xsl:template>


  <!-- ===================================================================== -->
  <!-- BODY_DEV_API                                                          -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_DEV_API">
    public API info goes here
  </xsl:template>

</xsl:stylesheet>
