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

/** Object to store result/error messages in. */
public class ResultMessage {
    /** Message to display to client. */    
   private String _message;
   /** Type of message.  This is used to determine the text color (red for
    * error/unsuccessful, green for successful).
    */   
   private String _type;

   /** Constructor for Result message object.
    * @param message Error/Result message to send to client.
    * @param type Type of message.
    */   
   public ResultMessage(String message, String type){
	   _message = message;
	   _type = type;
   }

   /** Set message.
    * @param message Message to set.
    */   
   public void setMessage(String message){ _message = message; }
   /** Set message type.
    * @param type Message type to set.
    */   
   public void setType(String type){ _type = type; }
   /** Get Message from Message object.
    * @return Returns string of message.
    */   
   public String getMessage(){ return _message; }
   /** Get Type from Message object.
    * @return Returns string of type from Message object.
    */   
   public String getType(){ return _type; }


}
