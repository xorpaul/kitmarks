/*
-------------------------------------------------------------------------------------------------
Copyright, 2006, Chipmark.

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

package bookmarks.filters;

import java.io.IOException;

public interface EventLogger {
	
	/**
	 * 
	 * @param timestamp
	 * @param servletName
	 * @param responseTime
	 * @param userID
	 * @param action
	 * @param exception
	 * @param ipAddress
	 * @return
	 * @throws IOException
	 */
    public int log(long timestamp, String servletName, int responseTime, int userID, String action,
                    boolean exception, String ipAddress, String browserType) throws IOException;

}
