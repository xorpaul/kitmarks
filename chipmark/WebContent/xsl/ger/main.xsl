<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="UTF-8" method="html" indent="yes"/>

  <xsl:include href="templates.xsl"/>

  <xsl:template match="/">

    <!--
*************************************************************************************************
Copyright 2006 Chipmark.

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
    <xsl:choose>

      <xsl:when test="xml/Target = 'pluginManage'">
        <xsl:call-template name="PLUGIN_MANAGE"/>
      </xsl:when>
      
      <xsl:when test="xml/Target = 'facebookManage'">
      	<xsl:call-template name="FACEBOOK_MANAGE"/>
      </xsl:when>
      
      <xsl:when test="xml/Target = 'facebookLogin'">
    	<xsl:call-template name="FACEBOOK_LOGIN"/>	  	
      </xsl:when>
      
      <xsl:when test="xml/Target = 'facebookAuthentication'">
        <xsl:call-template name="FACEBOOK_AUTHENTICATION"/>
      </xsl:when>
            
      <xsl:otherwise>
        <xsl:call-template name="MAIN_LAYOUT"/>
      </xsl:otherwise>
      
    </xsl:choose> 
    
  </xsl:template>
</xsl:stylesheet>
