<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="UTF-8" method="html" indent="yes"/>

<!--
*************************************************************************************************
Copyright 200-2007 Chipmark.

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


<xsl:template name="replace-string">
    <xsl:param name="text"/>
    <xsl:param name="from"/>
    <xsl:param name="to"/>
    <xsl:choose>
      <xsl:when test="contains($text, $from)">
	<xsl:variable name="before" select="substring-before($text, $from)"/>
	<xsl:variable name="after" select="substring-after($text, $from)"/>

	<xsl:value-of select="$before"/>
        <xsl:value-of select="$to"/>
        <xsl:call-template name="replace-string">
	  <xsl:with-param name="text" select="$after"/>
	  <xsl:with-param name="from" select="$from"/>
	  <xsl:with-param name="to" select="$to"/>
	</xsl:call-template>
      </xsl:when> 
      <xsl:otherwise>
        <xsl:value-of select="$text"/>  
      </xsl:otherwise>
    </xsl:choose>            
 </xsl:template>



<!-- ===================================================================== -->
<!-- addBreaks                                                             -->
<!-- ===================================================================== -->
<!-- recursive template to prevent long urls from ruining page -->
<xsl:template match="node()" mode="addBreaks"
              name="addBreaks">
  <xsl:param name="url" select="." />
<xsl:choose>
  <xsl:when test="string-length($url)&lt;30">
    <xsl:value-of select="$url"/>
  </xsl:when>
  <xsl:otherwise>
    <xsl:value-of select="substring($url,0,30)"/>  
    <xsl:choose>
      <xsl:when test="contains(substring($url,30,10), '&#47;')">
        <xsl:value-of select="substring-before(substring($url,30),'&#47;')"/>&#47;<img src="images/spacer.gif" width="0" height="0" border="0" />
        <xsl:call-template name="addBreaks">
          <xsl:with-param name="url"
                        select="substring-after(substring($url,30), '&#47;')"/>
        </xsl:call-template>
      </xsl:when>                                                                                                     
      <xsl:otherwise>
      <img src="images/spacer.gif" width="0" height="0" border="0" />
        <xsl:call-template name="addBreaks">
          <xsl:with-param name="url"
                        select="substring($url,30)"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:otherwise>
</xsl:choose>
</xsl:template>


<!-- ===================================================================== -->
<!-- addBreaksString                                                       -->
<!-- ===================================================================== -->
<!-- recursive template to prevent long title, description from ruining page -->
<xsl:template match="node()" mode="addBreaksString" name="addBreaksString">
  <xsl:param name="string" select="." />
   <xsl:choose>
     <xsl:when test="string-length($string)&lt;60">
       <xsl:value-of select="$string"/>
     </xsl:when>
     <xsl:otherwise>
       <xsl:value-of select="substring($string,0,60)"/><img src="images/spacer.gif" width="0" height="0" border="0" />
       <xsl:call-template name="addBreaksString">
         <xsl:with-param name="string" select="substring-after(substring($string,60), '')"/>
       </xsl:call-template>
     </xsl:otherwise>                          
  </xsl:choose>
</xsl:template>



</xsl:stylesheet>