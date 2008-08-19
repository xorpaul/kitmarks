/*
 -------------------------------------------------------------------------------------------------
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
 -------------------------------------------------------------------------------------------------
 */

package bookmarks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *This is a representation of the data that defines the order in which a
 *user's bookmarks should be displayed.
 *@author Carmen Wick
 */
public class SortString{
	public SortString(){return;}
	
	/**
	 *Creates a new sort string.  The order represented by the sort string 
	 *will be the same as the order in which bookmarks appear in the specified 
	 *arraylist of Bookmarks.
	 */
	public SortString(ArrayList<Bookmark> bookmarks){
		
		order = new ArrayList<Integer>(bookmarks.size());
		
		for (int i=0 ; i < bookmarks.size() ; i++){
			order.add(i, new Integer((bookmarks.get(i)).getLinkID()));
		}
		
	}
	
	/**
	 *Creates a new sort string using a binary stream that was previously  
	 *created by this.getInputString().
	 */
	public SortString(InputStream in)
	throws IOException{
		
		order = new ArrayList<Integer>();
		
		DataInputStream dataIn = new DataInputStream(in);
		
		try{
			while (true){  //consume int's from the input stream until EOF is reached
				order.add( new Integer(dataIn.readInt()) );
			}
		}
		
		catch (EOFException eof){}
		catch (IOException ex){ throw ex;} 
	}
	
	
	/**
	 *@returns The display order of the specified bookmark.  The order of a bookmark
	 *starts from 0.
	 *@returns -1 if the specified bookmark is not represented in this sort string.
	 */
	public int getPosition(Bookmark mark){
		
		return order.indexOf(new Integer(mark.getLinkID()));
		
		
	}
	
	/**
	 *@returns the bookmark ID of the bookmark at the specified location in the sort string.
	 */
	public int getPosition(int index){ 
		
		return ((Integer)order.get(index)).intValue(); 
		
	}
	
	/**
	 *@returns the number of bookmark IDs stored in this sort string
	 */
	public int size(){ return order.size(); }
	
	
	/**
	 *@returns an InputStream representation of the bookmark IDs stored in the sort string.
	 *This can be used as input into the database wrapper's storeSortString method
	 */
	public InputStream getBinaryStream(){
		
		//create a buffer in memory to store a string of bytes
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(0);
		
		//create dataoutputstream object to write Integers as a sequence of bytes
		DataOutputStream out = new DataOutputStream(buffer);
		
		//write all the Integers in the sort string to the output stream
		try{
			for (int i=0 ; i<order.size() ; i++){
				out.writeInt( ((Integer)order.get(i)).intValue() );
			}
		}
		catch (IOException ex){}  //This will never happen
		
		//return buffer as an inputstream that can be read from
		return (InputStream) new ByteArrayInputStream(buffer.toByteArray());
		
	}    
	
	//List of bookmark IDs(ints) in proper display order
	private ArrayList<Integer> order;
	
}
