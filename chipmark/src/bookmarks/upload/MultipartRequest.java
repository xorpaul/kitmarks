package bookmarks.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import bookmarks.Utilities;

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

    @author Jason Pell

	@version 1.30rc1- Updated the getFileContent() method to return an instance of EmptyInputStream,
					where a filename was provided in the upload process, but the file was empty.
					- Updated trimQuotes(...) method to fix bug #693432
	@version 1.30b4 - Minor fixes to setEncoding(String enc), thanks to Rob Gaunt & ZiP (zip_dev)
					- Fix to readAndWriteFile(...) method to ensure the out file is always closed, even if an
	    			  exception occurs, thanks to David Thompson.
					- Revise some of the changes made in 1.3-jspWiki
    @version 1.3-jspWiki 2003-01-07, Torsten Hildebrandt (THildebrandt@gmx.de)
                    Fixes
                     - TempFile.createTempFile() caused an endless loop on Windows if filename is a 
                        URL with Parameters. 
                     - using fixed scheme for temporary file-names now ("multPartReq"<integer>".tmp"). Old scheme 
                       not only triggered the endless loop above, but also throws an exception with short filenames 
                       (like "ab.txt").
                    Enhancements
                     - Encoding can be specified for each object independently.
                     - method getRawFilename() added, that returns the filename exactly as transmitted by the browser

    @version 1.30b2 A minor fix to readAndWriteFile(...) method.  We need to check for whether 'out' object is null 
                    before trying to call the close method.
    @version 1.30b1 Added functionality to save uploaded files under a temporary file name.  I 'borrowed' the TempFile
                    class functionality from Java 2 java.io.File to ensure that this class is still compatible with
                    JDK 1.1.x.  I also modified some of the string comparisons ('Content-Type') to be case-insensitive,
                    just in case some idiot decides to write a browser which does not initcap the Content and the Type
                    words.
    @version 1.21   A minor fix for IE4 on Mac which added extra newline after last boundary.  This 'fooled' previous
                    versions of MultipartRequest into thinking another block was present.
    @version 1.20   A very minor fix, but one apparently that was causing some serious issues for some users.  The intTotalRead
                    integer was being set to -1, which meant that it was always off by one when comparing to Content-Length.
                    Thanks to David Tuma for this fix.  19/10/2001
    @version 1.19   Moved the MultipartRequest into a package, and thus into a java archive for easier dissemination.
                    Fixed a bug, where in netscape if a file was uploaded with an unrecognised extension, no Content-Type
                    was specified, so the first line of the file was chopped off.  Also modified the structure of the parse
                    method to make it easier to manage.  I was checking strFileName length and nullness in two if blocks,
                    so I moved them together.  This should make the parse(...) method easier to understand as well. 26/07/2001
    @version 1.18   Fixed some serious bugs.  A new method readAndWrite(InputStream in, OutputStream out) which now does
                    the generic processing in common for readAndWriteFile and readFile.  The differences are that now
                    the two extra bytes at the end of a file upload are processed once, instead of after each line.  Also
                    if an empty file is encountered, an outputstream is opened, but then deleted if no data written to it.
                    The getCharArray() method has been removed.  Replaced by the new String(bytes, encoding) method using
                    ISO8859_1 encoding to ensure that extended characters are supported.  All creation of strings is done
                    using this encoding.  The addition of static methods setEncoding(String) and getEncoding() to allow the
                    use of MultipartRequest with a specific encoding type.  All instances of MultipartRequest will utilise
                    the static charEncoding variable value, that the setEncoding() method can be used to set.  
                    Hopefully this will not introduce any latent problems. Started to introduce support for multiple file 
                    uploads with the same form field name, but not completed for v1.18.  26/06/2001
    @version 1.17   A few _very_ minor fixes.  Plus a cool new feature added.  The ability to save files into memory.
                    <b>Thanks to Mark Latham for the idea and some of the code.</b> 11/04/2001
    @version 1.16   Added support for multiple parameter values.  Also fixed getCharArray(...) method to support 
                    parameters with non-english ascii values (ascii above 127).  Thanks to Stefan Schmidt & 
                    Michael Elvers for this.  (No fix yet for reported problems with Tomcat 3.2 or a single extra 
                    byte appended to uploads of certain files).  By 1.17 hopefully will have a resolution for the
                    second problem.  14/03/2001
    @version 1.15   A new parameter added, intMaxReadBytes, to allow arbitrary length files.  Released under
                    the LGPL (Lesser General Public License).   03/02/2001
    @version 1.14   Fix for IE problem with filename being empty.  This is because IE includes a default Content-Type
                    even when no file is uploaded.  16/02/2001
    @version 1.13   If an upload directory is not specified, then all file contents are sent into oblivion, but the
                    rest of the parsing works as normal.
    @version 1.12   Fix, was allowing zero length files.  Will not even create the output file until there is
                    something to write.  getFile(String) now returns null, if a zero length file was specified.  06/11/2000
    @version 1.11   Fix, in case Content-type is not specified.
    @version 1.1    Removed dependence on Servlets.  Now passes in a generic InputStream instead.
                    "Borrowed" readLine from Tomcat 3.1 ServletInputStream class,
                    so we can remove some of the dependencies on ServletInputStream.
                    Fixed bug where a empty INPUT TYPE="FILE" value, would cause an exception.
    @version 1.0    Initial Release.
 */

public class MultipartRequest
{
	
	/**
    Prevent a denial of service by defining this, will never read more data.
    If Content-Length is specified to be more than this, will throw an exception.

    This limits the maximum number of bytes to the value of an int, which is 2 Gigabytes.
	 */
	private static final int MAX_READ_BYTES = 2 * (1024 * 1024); // 2MB!

	/**
    Defines the number of bytes to read per readLine call. 128K
	 */
	private static final int READ_LINE_BLOCK = 1024 * 128;

	/**
        Define Character Encoding method here.
	 */
	private String charEncoding;

	/**
	 * URL Parameters
	 */
	private HashMap<String, ArrayList<String>> htParameters = null;
	
	/**
	 * File Parameters
	 */
	private HashMap<String, ArrayList<FileParameter>> htFiles = null;

	/**
	 * 
	 */
	private String strBoundary = null;

	/**
	 * If this Directory spec remains null, writing of files will be disabled...
	 */
	private File fileOutPutDirectory = null;
	
	/**
	 * 
	 */
	private boolean loadIntoMemory = false;

	/**
	 * The maximum content length
	 */
	private long intMaxContentLength;
	
	/**
	 * The actual content length
	 */
	private long intContentLength;
	
	/**
	 * This stores the actual total read including all multipart boundaries, etc.
	 */
	private long intTotalRead;

	/**
        Store a read from the input stream here.  Global so we do not keep creating new arrays each read.
	 */
	private byte[] blockOfBytes = null;

	/**
	 * 
	 * @param strContentTypeText The &quot;Content-Type&quot; HTTP header value.
	 * @param intContentLength The &quot;Content-Length&quot; HTTP header value.
	 * @param in The InputStream to read and parse.
	 * @param intMaxReadBytes Overrides the MAX_BYTES_READ value, to allow arbitrarily long files.
	 * @param encoding Sets the encoding to use. If null, UTF-8 will be used.
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public MultipartRequest(
			String strContentTypeText, 
			int intContentLength, 
			InputStream in, 
			int intMaxReadBytes,
			String encoding) throws IllegalArgumentException, IOException, UnsupportedEncodingException
			{
		
		this.initParser( 
				strContentTypeText, 
				intContentLength, 
				in, 
				true, //loadIntoMemory
				null, // strSaveDirectory
				intMaxReadBytes,
				encoding);
		
			}
	
	/** 
	 * Initialise the parser.
	 *
	 * @param strContentTypeText    The &quot;Content-Type&quot; HTTP header value.
	 * @param intContentLength      The &quot;Content-Length&quot; HTTP header value.
	 * @param in                    The InputStream to read and parse.
	 * @param loadIntoMemory        Is this parser loading its output into memory only.
	 * @param strSaveDirectory      The temporary directory to save the file from where they can then be moved to wherever by the
	 *                              calling process.  <b>If you specify <u>null</u> for this parameter, then any files uploaded
	 *                              will be silently ignored.</B>
	 * @param intMaxReadBytes       Overrides the MAX_BYTES_READ value, to allow arbitrarily long files.
	 * @param maxBytesExceededMode	This controls how the parser will process a request which is in excess of the intMaxReadBytes
	 * 								parameter.  The possible modes are:
	 *                              <ul>
	 *                              <li>MultipartRequest.ABORT_IF_MAX_BYES_EXCEEDED - The parser will throw a MaxBytesReadException</li>
	 *                              <li>MultipartRequest.IGNORE_FILES_IF_MAX_BYES_EXCEEDED - All parameters will be processed, but any file
	 *                              content will be discarded.  <b><u>WARNING: There is still potential for a Denial-of-Service.  For instance, an attacker can send
	 *                              many megabytes of non-file form data.</u></b></li>
	 * 								<ul>
	 * @param encoding              Sets the encoding to use. If null, UTF-8 will be used.
	 *
	 * @exception IllegalArgumentException  If the strContentTypeText does not contain a Content-Type of "multipart/form-data" or the boundary is not found.
	 * @exception IOException               If the intContentLength is higher than MAX_READ_BYTES or strSaveDirectory is invalid or cannot be written to.
	 * @exception UnsupportedEncodingException If the encoding is invalid.
	 *
	 * @see #MAX_READ_BYTES
	 */
	private void initParser( 
			String strContentTypeText, 
			int intContentLength, 
			InputStream in,
			boolean loadIntoMemory,
			String strSaveDirectory,
			int intMaxReadBytes,
			String encoding) throws IllegalArgumentException, IOException, UnsupportedEncodingException
			{

		// If true - ignore the directory specification.		
		this.loadIntoMemory = loadIntoMemory;

		// IF strSaveDirectory == NULL, then we should ignore any files uploaded.
		if (!loadIntoMemory && strSaveDirectory!=null)
		{
			this.fileOutPutDirectory = new File(strSaveDirectory);
			if (!this.fileOutPutDirectory.exists())
				throw new IOException("Directory ["+strSaveDirectory+"] is invalid.");
			else if (!this.fileOutPutDirectory.canWrite())
				throw new IOException("Directory ["+strSaveDirectory+"] is readonly.");
		}

		// Set the encoding.
		this.setEncoding(encoding);

		if (strContentTypeText!=null && strContentTypeText.startsWith("multipart/form-data") && strContentTypeText.indexOf("boundary=")!=-1)
		{
			this.strBoundary = strContentTypeText.substring(strContentTypeText.indexOf("boundary=")+"boundary=".length()).trim();
		}
		else
		{
			throw new IllegalArgumentException("Invalid Content Type of " + strContentTypeText);
		}

		this.intContentLength = intContentLength;
		this.intMaxContentLength = intMaxReadBytes;

		if(this.isMaxBytesExceeded())// FIX: 1.15
		{
			throw new MaxReadBytesException(this.intContentLength, this.intMaxContentLength);
		}

		// Instantiate the hashtable...
		this.htParameters = new HashMap<String, ArrayList<String>>();
		this.htFiles = new HashMap<String, ArrayList<FileParameter>>();
		this.blockOfBytes = new byte[READ_LINE_BLOCK];

		// Even though this method would never normally be called more than once
		// in the objects lifetime, we will initialise it here anyway.
		this.intTotalRead=0; // <David Tuma> - fix: 1.20

		// Now parse the data.
		this.parse(new BufferedInputStream(in));

		// No need for these once parse is complete.
		this.blockOfBytes=null;
		this.strBoundary=null;
			}

	/**
	 * If this class was constructed with a maxBytesExceeded mode of 
	 * MultipartRequest.IGNORE_FILES_IF_MAX_BYES_EXCEEDED, this method
	 * will indicate whether the process is ignoring file content because
	 * the content-length was exceeded.
	 */
	private boolean isMaxBytesExceeded()
	{
		return (this.intContentLength > this.intMaxContentLength);
	}

	/**
	 * 
	 * @param strName String key used to retrieve the parameter
	 * @return String
	 */
	public String getURLParameter(String strName)
	{                                        
		ArrayList<String> value = htParameters.get(strName);
		return value != null && value.size() > 0 ? value.get(0) : null;
	}

	/**
	 * 
	 * @param strName String key used to retrieve the parameter
	 * @return FileParameter
	 */
	private FileParameter getFileParameter(String strName)
	{                                        
		ArrayList<FileParameter> value = htFiles.get(strName);
		return value != null && value.size() > 0 ? value.get(0) : null;
	}

	/**
        So we can put the logic for supporting multiple parameters with the same
        form field name in the one location.
	 */
	private void addURLParameter(String key, String value)
	{
		// Fix 1.16: for multiple parameter values.
		ArrayList<String> objParams = htParameters.get(key);

		if(objParams != null) {
			objParams.add(value);
		} else {
			ArrayList<String> newParams = new ArrayList<String>();
			newParams.add(value);
			htParameters.put(key, newParams);
		}
	}

	/**
        So we can put the logic for supporting multiple files with the same
        form field name in the one location.

        Assumes that this method will never be called with a null fileObj or strFilename.
	 */
	private void addFileParameter(String key, FileParameter value)
	{
		ArrayList<FileParameter> objParams = htFiles.get(key);

		if(objParams != null) {
			objParams.add(value);
		} else {
			ArrayList<FileParameter> newParams = new ArrayList<FileParameter>();
			newParams.add(value);
			htFiles.put(key, newParams);
		}
	}

	/**
	 * 
	 * @param strName
	 * @return
	 */
	public InputStream getFileContents(String strName)
	{

		FileParameter fileParam = getFileParameter(strName);
		if(fileParam != null) {
			if (fileParam.getFileContents()!=null) {
				return new ByteArrayInputStream(fileParam.getFileContents());
			}
			else if(fileParam.getFileName()!=null) {
				return new EmptyInputStream(); // empty file, but lets return an inputstream anyway.
			}
		}

		return null;
	}

	/**
        Sets up the encoding for this instance of multipartrequest. You can set the encoding 
        to null, in which case the default encoding will be applied.  The default encoding if 
        this method is not called has been set to ISO-8859-1, which seems to offer the best hope
        of support for international characters, such as german "Umlaut" characters.
	 */
	private void setEncoding(String enc)throws UnsupportedEncodingException
	{
		if (enc==null || enc.trim()=="")
			charEncoding = Utilities.REQUEST_ENCODING;
		else
		{
			// This will test the encoding for validity.
			new String(new byte[]{(byte)'\n'}, enc);

			charEncoding = enc;
		}
	}

	/**
	 * 
	 * @param in
	 * @throws IOException
	 */
	private void parse(InputStream in) throws IOException
	{
		String strContentType = null;
		String strName = null;
		String strFilename = null;
		String strRawFilename = null;
		String strLine = null;
		int read = -1;

		// First run through, check that the first line is a boundary, otherwise throw a exception as format incorrect.
		read = this.readLine(in, blockOfBytes);
		strLine = read>0? new String(blockOfBytes, 0, read, charEncoding): null;

		// Must be boundary at top of loop, otherwise we have finished.
		if (strLine==null || strLine.indexOf(this.strBoundary)==-1)
		{
			throw new IOException("Invalid Form Data, no boundary encountered.");
		}

		// At the top of loop, we assume that the Content-Disposition line is next, otherwise we are at the end.
		while (true)
		{
			// Get Content-Disposition line.
			read = this.readLine(in, blockOfBytes);
			if (read<=0)
				break; // Nothing to do.
			else
			{
				strLine = new String(blockOfBytes, 0, read, charEncoding);

				// Mac IE4 adds extra line after last boundary - 1.21
				if(strLine==null || strLine.length() == 0 || strLine.trim().length() == 0)
					break;

				// TODO: Improve performance by getting both the name and filename from strLine in one go...
				strName = trimQuotes(getValue("name", strLine));
				// If this is not null, it indicates that we are processing a filename.
				// Now if not null, strip it of any directory information.
				strFilename = trimQuotes(getValue("filename", strLine));

				// No filename specified at all - parameter
				if (strFilename==null)
				{
					// Skip blank line.
					this.readLine(in, this.blockOfBytes);

					String param = this.readParameter(in);
					this.addURLParameter(strName, param);
				}
				else// (strFilename!=null)
				{
					// Fix: did not check whether filename was empty string indicating a FILE was not passed.
					if (strFilename.length()==0)
					{
						// FIX 1.14: IE problem with empty filename.
						read = this.readLine(in, this.blockOfBytes);
						strLine = read>0? new String(this.blockOfBytes, 0, read, this.charEncoding): null;

						// FIX 1.14 IE Problem still: Check for content-type and extra line even though no file specified.
						if (strLine!=null && strLine.toLowerCase().startsWith("content-type:"))
							this.readLine(in, this.blockOfBytes);

						// Skip blank line.
						this.readLine(in, this.blockOfBytes);

						this.readLine(in, this.blockOfBytes); 
					}
					else // File uploaded, or at least a filename was specified, it could still be spurious.
					{
						// Need to get the content type.
						read = this.readLine(in, this.blockOfBytes);
						strLine = read>0? new String(this.blockOfBytes, 0, read, this.charEncoding): null;

						strContentType = "application/octet-stream";
						// Fix 1.11: If not null AND strLine.length() is long enough.
						// Modified in 1.19, as we should be checking if it is actually a Content-Type.
						if (strLine!=null && strLine.toLowerCase().startsWith("content-type:"))//Changed 1.19
						{
							strContentType = strLine.substring("content-type:".length()).trim();// Changed 1.13

							// Skip blank line, but only if a Content-Type was specified.
							this.readLine(in, this.blockOfBytes);
						}

						long filesize = -1;

						// Will remain null for read onto file system uploads.
						byte[] contentsOfFile = null;

						// Will remain null if files are loaded into memory.
						File outFile = null;

						// Get the BASENAME version of strFilename.
						strRawFilename = strFilename;
						strFilename = getBasename(strFilename);

						// Are we loading files into memory instead of the filesystem?
						if (this.loadIntoMemory)
						{
							contentsOfFile = this.readFile(in);
							if (contentsOfFile!=null)
								filesize = contentsOfFile.length;
						}
						else// Read the file onto file system.
						{
							// If nowhere to write file, then we pass a null file to 
							// readAndWriteFile, in which case the uploaded file contents
							// will silently be processed and discarded.
							if(this.fileOutPutDirectory != null)
							{
								outFile = TempFile.createTempFile("multPartReq", null, this.fileOutPutDirectory);
							}
							filesize = this.readAndWriteFile(in, outFile);
						}

						// Fix 1.18 for multiple FILE parameter values.
						FileParameter fileParam = new FileParameter(strFilename, strContentType, filesize > 0 ? new Long(filesize) : new Long(0), contentsOfFile, outFile, strRawFilename);
						this.addFileParameter(strName, fileParam);
						
					}
				}
			}
		}// while 
	}

	/**
        Read parameters, assume already passed Content-Disposition and blank line.

        @return the value read in.
	 */
	private String readParameter(InputStream in) throws IOException
	{
		StringBuffer buf = new StringBuffer();
		int read=-1;

		String line = null;
		while(true)
		{
			read = readLine(in, blockOfBytes);
			if (read<0)
			{
				throw new IOException("Stream ended prematurely.");
			}

			// Change v1.18: Only instantiate string once for performance reasons.
			line = new String(blockOfBytes, 0, read, charEncoding);
			if (read<blockOfBytes.length && line.indexOf(this.strBoundary)!=-1)
				break; // Boundary found, we need to finish up.
			else 
				buf.append(line);
		}

		if (buf.length()>0)
			buf.setLength(getLengthMinusEnding(buf));
		return buf.toString();
	}

	/**
        Read from in, write to out, minus last two line ending bytes.
	 */
	private long readAndWrite(InputStream in, OutputStream out) throws IOException
	{
		long fileSize = 0;
		int read = -1;

		// This variable will be assigned the bytes actually read.
		byte[] secondLineOfBytes = new byte[blockOfBytes.length];
		// So we do not have to keep creating the second array.
		int sizeOfSecondArray = 0;

		while(true)
		{
			read = readLine(in, blockOfBytes);
			if (read<0)
				throw new IOException("Stream ended prematurely.");

			// Found boundary.
			if (read<blockOfBytes.length && new String(blockOfBytes, 0, read, charEncoding).indexOf(this.strBoundary)!=-1)
			{
				// Write the line, minus any line ending bytes.
				//The secondLineOfBytes will NEVER BE NON-NULL if out==null, so there is no need to included this in the test
				if(sizeOfSecondArray!=0)
				{
					// Only used once, so declare here.
					int actualLength = getLengthMinusEnding(secondLineOfBytes, sizeOfSecondArray);
					if (actualLength>0 && out!=null)
					{
						out.write(secondLineOfBytes, 0, actualLength);
						// Update file size.
						fileSize+=actualLength;
					}
				}
				break;
			}
			else
			{
				// Write out previous line.
				//The sizeOfSecondArray will NEVER BE ZERO if out==null, so there is no need to included this in the test
				if(sizeOfSecondArray!=0)
				{
					out.write(secondLineOfBytes, 0, sizeOfSecondArray);
					// Update file size.
					fileSize+=sizeOfSecondArray;
				}

				// out will always be null, so there is no need to reset sizeOfSecondArray to zero each time.
				if(out!=null)
				{
					//Copy the read bytes into the array.
					System.arraycopy(blockOfBytes,0,secondLineOfBytes,0,read);
					// That is how many bytes to read from the secondLineOfBytes
					sizeOfSecondArray=read;
				}
			}
		}

		//Return the number of bytes written to outstream.
		return fileSize;
	}

	/**
        Read a Multipart section that is a file type.  Assumes that the Content-Disposition/Content-Type and blank line
        have already been processed.  So we read until we hit a boundary, then close file and return.

        @exception IOException if an error occurs writing the file.

        @return the number of bytes read.
	 */
	private long readAndWriteFile(InputStream in, File outFile) throws IOException
	{
		BufferedOutputStream out = null;

		try
		{
			// 1.30rc1 - if max content is larger or equal to actual content length, then we
			// can continue, otherwise, all file content should be silently ignored.
			if(this.intContentLength <= this.intMaxContentLength) // file not available.
			{
				// Because the outFile should be a temporary file provided by the TempFile
				// class, it should already exist and should be writable.
				if(outFile!=null && outFile.exists() && outFile.canWrite())
				{
					out = new BufferedOutputStream(new FileOutputStream(outFile));
				}
			}

			long count = readAndWrite(in, out);
			// Count would NOT be larger than zero if 'out' was null.
			if (count==0)
			{
				// Delete file as empty.
				if (outFile != null) 
					outFile.delete();
			}

			return count;
		}
		finally
		{
			if(out!=null)
			{
				out.flush();
				out.close();
			}
		}			
	}

	/**
	 *  If the fileOutPutDirectory wasn't specified, just read the file to memory.
	 *
	 *  @param strName - Url parameter this file was loaded under.
	 *  @return contents of file, from which you can garner the size as well.
	 */
	private byte[] readFile(InputStream in) throws IOException
	{
		ByteArrayOutputStream out = null;

		// 1.30rc1 - if max content is larger or equal to actual content length, then we
		// can continue, otherwise, all file content should be silently ignored.
		if(this.intContentLength <= this.intMaxContentLength)
		{
			// In this case, we do not need to worry about a outputdirectory.
			out = new ByteArrayOutputStream();
		}

		long count = readAndWrite(in, out);
		// Count would NOT be larger than zero if out was null.
		if (count>0)
		{
			// Return contents of file to parse method for inclusion in htFiles object.
			if(out!=null)
				return out.toByteArray();
			else
				return null;
		}
		else
			return null;
	}

	/**
        Reads at most READ_BLOCK blocks of data, or a single line whichever is smaller.
        Returns -1, if nothing to read, or we have reached the specified content-length.

        Assumes that bytToBeRead.length indicates the block size to read.

        @return -1 if stream has ended, before a newline encountered (should never happen) OR
        we have read past the Content-Length specified.  (Should also not happen).  Otherwise
        return the number of characters read.  You can test whether the number returned is less
        than bytesToBeRead.length, which indicates that we have read the last line of a file or parameter or 
        a border line, or some other formatting stuff.
	 */
	private int readLine(InputStream in, byte[] bytesToBeRead) throws IOException 
	{
		// Ensure that there is still stuff to read...
		if(this.intTotalRead >= this.intContentLength) 
			return -1;

		// Get the length of what we are wanting to read.
		int length = bytesToBeRead.length;

		// End of content, but some servers (apparently) may not realise this and end the InputStream, so
		// we cover ourselves this way.
		if (length > (this.intContentLength - this.intTotalRead))
		{
			length = (int) (this.intContentLength - this.intTotalRead);  // So we only read the data that is left.
		}

		int result = readLine(in, bytesToBeRead, 0, length);
		// Only if we actually read something, otherwise something weird has happened, such as the end of stream.
		if (result > 0) 
		{
			this.intTotalRead += result;
		}

		return result;  
	}

	/**
        Returns the length of the line minus line ending.

        @param endOfArray   This is because in many cases the byteLine will have garbage data at the end, so we
                            act as though the actual end of the array is this parameter.  If you want to process
                            the complete byteLine, specify byteLine.length as the endOfArray parameter.
	 */
	private static final int getLengthMinusEnding(byte byteLine[], int endOfArray)
	{
		if (byteLine==null)
			return 0;

		if (endOfArray>=2 && byteLine[endOfArray-2] == '\r' && byteLine[endOfArray-1] == '\n')
			return endOfArray-2;
		else if (endOfArray>=1 && byteLine[endOfArray-1] == '\n' || byteLine[endOfArray-1] == '\r')
			return endOfArray-1;
		else
			return endOfArray;
	}

	/**
	 * 
	 * @param buf
	 * @return
	 */
	private static final int getLengthMinusEnding(StringBuffer buf)
	{
		if (buf.length()>=2 && buf.charAt(buf.length()-2) == '\r' && buf.charAt(buf.length()-1) == '\n')
			return buf.length()-2;
		else if (buf.length()>=1 && buf.charAt(buf.length()-1) == '\n' || buf.charAt(buf.length()-1) == '\r')
			return buf.length()-1;
		else
			return buf.length();
	}

	/**
        This needs to support the possibility of a / or a \ separator.

        Returns strFilename after removing all characters before the last
        occurence of / or \.
	 */
	private static final String getBasename(String strFilename)
	{
		if (strFilename==null)
			return strFilename;

		int intIndex = strFilename.lastIndexOf("/");
		if (intIndex==-1 || strFilename.lastIndexOf("\\")>intIndex)
			intIndex = strFilename.lastIndexOf("\\");

		if (intIndex!=-1)
			return strFilename.substring(intIndex+1);
		else
			return strFilename;
	}

	/**
        trimQuotes trims any quotes from the start and end of a string and returns the trimmed string...
	 */
	private static final String trimQuotes (String strItem)
	{
		// Saves having to go any further....
		if (strItem==null || strItem.indexOf("\"")==-1)
			return strItem;

		// Get rid of any whitespace..
		strItem = strItem.trim();

		if (strItem.length()>0 && strItem.charAt(0) == '\"')
			strItem = strItem.substring(1);

		if (strItem.length()>0 && strItem.charAt(strItem.length()-1) == '\"')
			strItem = strItem.substring(0, strItem.length()-1);

		return strItem;
	}

	/**
        Format of string name=value; name=value;

        If not found, will return null.
	 */
	private static final String getValue(String strName, String strToDecode)
	{
		strName = strName + "=";

		int startIndexOf=0;
		while (startIndexOf<strToDecode.length())
		{
			int indexOf = strToDecode.indexOf(strName, startIndexOf);
			// Ensure either first name, or a space or ; precedes it.
			if (indexOf!=-1)
			{
				if (indexOf==0 || Character.isWhitespace(strToDecode.charAt(indexOf-1)) || strToDecode.charAt(indexOf-1)==';')
				{
					int endIndexOf = strToDecode.indexOf(";", indexOf+strName.length());
					if (endIndexOf==-1) // May return an empty string...
						return strToDecode.substring(indexOf+strName.length());
					else
						return strToDecode.substring(indexOf+strName.length(), endIndexOf);
				}
				else
					startIndexOf=indexOf+strName.length();
			}
			else
				return null;
		}
		return null;
	}

	/**
	 * <I>Tomcat's ServletInputStream.readLine(byte[],int,int)  Slightly Modified to utilise in.read()</I>
	 * <BR>
	 * Reads the input stream, one line at a time. Starting at an
	 * offset, reads bytes into an array, until it reads a certain number
	 * of bytes or reaches a newline character, which it reads into the
	 * array as well.
	 *
	 * <p>This method <u><b>does not</b></u> returns -1 if it reaches the end of the input
	 * stream before reading the maximum number of bytes, it returns -1, if no bytes read.
	 *
	 * @param b         an array of bytes into which data is read
	 *
	 * @param off       an integer specifying the character at which
	 *                  this method begins reading
	 *
	 * @param len       an integer specifying the maximum number of 
	 *                  bytes to read
	 *
	 * @return          an integer specifying the actual number of bytes 
	 *                  read, or -1 if the end of the stream is reached
	 *
	 * @exception IOException   if an input or output exception has occurred
	 *

        Note: We have a problem with Tomcat reporting an erroneous number of bytes, so we need to check this.
        This is the method where we get an infinite loop, but only with binary files.
	 */
	private int readLine(InputStream in, byte[] b, int off, int len) throws IOException 
	{
		if (len <= 0) 
			return 0;

		int count = 0, c;

		while ((c = in.read()) != -1) 
		{
			b[off++] = (byte)c;
			count++;
			if (c == '\n' || count == len) 
				break;
		}

		return count > 0 ? count : -1;
	}

	/**
	 * 
	 * @author Joshua Fleck
	 *
	 */
	private class FileParameter {
		
		private String fileName;
		
		private String contentType;
		
		private long fileSize;
		
		private byte[] fileContents;
		
		private File outFile;
		
		private String rawFileName;

		/**
		 * @param fileName String 
		 * @param contentType String
		 * @param fileSize long
		 * @param fileContents byte[]
		 * @param outFile File
		 * @param rawFileName String
		 */
		public FileParameter(String fileName, String contentType, long fileSize, byte[] fileContents, File outFile, String rawFileName) {
			this.fileName = fileName;
			this.contentType = contentType;
			this.fileSize = fileSize;
			this.fileContents = fileContents;
			this.outFile = outFile;
			this.rawFileName = rawFileName;
		}

		/**
		 * @return the contentType
		 */
		public String getContentType() {
			return contentType;
		}

		/**
		 * @param contentType the contentType to set
		 */
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		/**
		 * @return the fileContents
		 */
		public byte[] getFileContents() {
			return fileContents;
		}

		/**
		 * @param fileContents the fileContents to set
		 */
		public void setFileContents(byte[] fileContents) {
			this.fileContents = fileContents;
		}

		/**
		 * @return the fileName
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * @param fileName the fileName to set
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		/**
		 * @return the fileSize
		 */
		public long getFileSize() {
			return fileSize;
		}

		/**
		 * @param fileSize the fileSize to set
		 */
		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}

		/**
		 * @return the outFile
		 */
		public File getOutFile() {
			return outFile;
		}

		/**
		 * @param outFile the outFile to set
		 */
		public void setOutFile(File outFile) {
			this.outFile = outFile;
		}

		/**
		 * @return the rawFileName
		 */
		public String getRawFileName() {
			return rawFileName;
		}

		/**
		 * @param rawFileName the rawFileName to set
		 */
		public void setRawFileName(String rawFileName) {
			this.rawFileName = rawFileName;
		}
		
		
	}
}
