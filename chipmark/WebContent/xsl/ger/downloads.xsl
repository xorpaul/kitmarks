<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="utf-8" method="html" indent="yes"/>

  <xsl:template match="/">

    <!--
*************************************************************************************************
Copyright 2007 Chipmark.

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
  <!-- BODY_DOWNLOAD                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_DOWNLOAD">
  <p class="sub_header">downloads</p>
          <p class="sub_content">
			  The plugins and extensions allow you to use chipmark without having to visit the web site.  Instead, all your bookmarks are accessible via a simple menu or toolbar.</p>
        

<p class="sub_header">browser detected: <span id="browser_slot" class="download_option">&#160;</span></p><p class="sub_content">We base our recommendation on the browser (indicated above) that you are using.</p>

                <p class="sub_header">our recommendation: 
<span id="FF_title" class="download_option">Firefox Extension</span>
<span id="IE_title" class="download_option">IE Plugin</span>
<span id="None_title" class="download_option">Web Interface</span>
</p>

<div id="FF_body" class="download_body">  
<p class="sub_content"><a style="color:green"  href="Main?target=downloadFirefox">Download</a> the FF extension.</p>
</div>

<div id="None_body" class="download_body">  
<p class="sub_content">There are currently no chipmark extensions available for your browser.  We recommend you use our web interface, which offers the same functionality as the plug-ins.</p>
</div>

<div id="IE_body" class="download_body">  
<p class="sub_content"><a style="color:green"  href="Main?target=downloadIE">Download</a> the IE plug-in.</p>
</div>

<div id="IE_body_CLR" class="download_body">  
<p class="sub_content"><a style="color:green" href="Main?target=downloadIE">Download</a> the IE plug-in. The most recent chipmark toolbar also requires <a style="color:green" href="http://www.microsoft.com/downloads/details.aspx?familyid=0856EACB-4362-4B0D-8EDD-AAB15C5E04F5&#38;displaylang=en">.NET Framework 2.0</a> to run.</p>
</div>        
         <p class="sub_header">all downloads:</p>
         <p class="sub_content">Feel free to download different plugins for use with different browsers.  Note: assuming our script correctly identified your browser, using a plugin other than our recommendation with it may not work correctly, or at all.</p>
         <ul><li><a class="content" href="Main?target=downloadIE">Internet Explorer plugin</a></li>
         <li><a class="content" href="Main?target=downloadFirefox">Firefox extension</a></li>
         <li><a class="content" href="/chipmark_source.tar.gz">Source code</a> (<a class="content" href="/chipmark_source.md5sum">md5</a>)</li>
         </ul>

	  <p class="sub_content">
		  Tutorial videos are available for the following actions:<br/>
		  <ul>
			  <li>
				  <a class="content" href="Main?target=video_ffinstall" title="Installing the Firefox Extension">Installing the Firefox Extension</a>
			  </li>
			  <li>
				  <a class="content" href="Main?target=video_ieinstall" title="Installing the IE Plugin">Installing the IE Plugin</a>
			  </li>
		  </ul>
	  </p>
	
    <script language="JavaScript" type="text/javascript">
      addReport('download_list');
    </script>
  </xsl:template>


  <!-- ===================================================================== -->
  <!-- BODY_DOWNLOAD_FIREFOX                                                 -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_DOWNLOAD_FIREFOX">

   
          <p class="sub_header">Installing the Firefox Extension</p>
        
          <p class="sub_content">
            Firefox will not install software from websites unless you add the sites to the browser's "Allowed Sites List."  Follow the steps below to install the Chipmark Firefox extension. (These instructions are based upon the default install of <a href="http://www.mozilla.org/products/firefox/">Firefox 2</a>.)
          </p>
          <ol class="content">
            <li>
              <b>Download the extension</b>
              <p>
                <a href="/firefox/chipmark-v.3.2.xpi">Download the extension</a> to begin the installation.
              </p>
            </li>
            <li>
              <b>Does the "Software Installation" window appear?</b>
              <p>
                <b>Yes:</b> Click the "Install Now" button.  When the installation finishes, you will need to restart your browser before using Chipmark.  You are now finished installing the Chipmark extension.
              </p>
              <p>
                <b>No:</b> Continue to step 3.
              </p>
            </li>
            <li>
              <b>Locate the "Edit Options..." button</b>
              <p>Look for a yellow bar at the top of the page and click the "Edit Options..." button.</p>
            </li>
            <li>
              <b>Click Allow</b>
              <p>Click the "Allow" button to add Chipmark.com to the "Allowed List;" then click the "OK" button to close the window.</p>
            </li>
            <li>
              <b>Download the extension</b>
              <p>Now that Chipmark.com is in the "Allowed List," click the 'Download Extension' button to the right to begin the installation.</p>
            </li>
            <li>
              <b>Click Install Now</b>
              <p>Click the "Install Now" button.</p>
            </li>
            <li>
              <b>Restart the browser</b>
              <p>Installation of the Chipmark extension is complete; close all Firefox windows, and restart the browser to begin enjoying Chipmark!</p>
            </li>
          </ol>

          <p>
            <small>
              <a href="/firefox/chipmark-v.3.2.md5sum">Extension MD5 Sum</a>
            </small>
          </p>

  </xsl:template>

  <!-- ===================================================================== -->
  <!-- BODY_DOWNLOAD_IE                                                         -->
  <!-- ===================================================================== -->
  <xsl:template name="BODY_DOWNLOAD_IE">
  

          <p class="sub_header">Installing the Internet Explorer Toolbar</p>
          <p class="sub_content"><strong>Note For Vista Users:</strong> While the IE toolbar works for some users of Windows Vista, it is not officially supported due to numerous known bugs. Feel free to use it at your own risk and if you find any bugs, we welcome error submissions at <a href="mailto:bugs@chipmark.com">bugs@chipmark.com</a> or the <a href="http://www.chipmark.com:8082/forum">forum</a>.</p>
          <p class="sub_content"><strong>Known Vista Issues:</strong></p>
          <ul class="content">
           <li>The toolbar will not work correctly if <a href="http://www.microsoft.com/windows/products/windowsvista/features/details/useraccountcontrol.mspx" target="_new">User Account Control</a> is turned off.</li>
            <li>In most cases, preferences are not stored across sessions (such as persistent login)</li>
          </ul>
          <ol class="content">
            <li>
              <b>Download the toolbar installer</b>
              <p><a href="/ie/ChipmarkSetup-v.3.2.msi">Download the toolbar installer</a> and click save to save it to your computer.</p>
            </li>
            <li>
              <b>Begin the installation process</b>
              <p>Once the download is complete, begin the installation process by double clicking on the installer that you downloaded. Follow the on-screen instructions of the installation wizard.</p>
            </li>
            <li>
              <b>Close Internet Explorer</b>
              <p>You need to close all instances of Internet Explorer and consequently Explorer for your toolbar to work properly.</p>
            </li>
            <li>
              <b>Reopen Internet Explorer</b>
              <p>When you first launch Internet Explorer after installing the toolbar, the Chipmark Toolbar will not be visible. You must click View -> Toolbars -> ChipmarkBar to enable the toolbar</p>
            </li>
            <li>
              <b>Locate the Toolbar</b>
              <p>In Internet Explorer, the ChipmarkBar is likely to be placed in a non-desired position. You can drag the toolbar down to place it on its own space. If you can not move the toolbar then you might need to "unlock" your Internet Explorer toolbar. Do this by right clicking on the toolbar area and de-checking "Lock Toolbars". Then you are all set to begin chipmarking!</p>
            </li>
          </ol>

  </xsl:template>


</xsl:stylesheet>
