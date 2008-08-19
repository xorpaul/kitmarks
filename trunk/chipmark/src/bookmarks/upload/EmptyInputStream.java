package bookmarks.upload;
import java.io.InputStream;
import java.io.IOException;

/**
	A Multipart form data parser.  Parses an input stream and writes out any files found, 
    making available a hashtable of other url parameters.  As of version 1.17 the files can
    be saved to memory, and optionally written to a database, etc.
    
    <BR>
    <BR>
    Copyright (c)2001-2003 Jason Pell.
    <BR>

    <PRE>
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    <BR>
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    <BR>
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
    <BR>    
    Email:  jasonpell@hotmail.com
    Url:    http://www.geocities.com/jasonpell
    </PRE>

	A empty InputStream class for getFileContents() method.
*/
public class EmptyInputStream extends InputStream 
{
	public EmptyInputStream()
	{
	}

	public int read() throws IOException
	{
		return -1;
	}

	public int available() throws IOException
	{
		return 0;
	}
}
