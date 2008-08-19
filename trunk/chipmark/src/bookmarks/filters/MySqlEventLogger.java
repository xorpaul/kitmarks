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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import bookmarks.ConnectionPool;
import bookmarks.Utilities;

/**
 * 
 * @author flec0025
 *
 */
public class MySqlEventLogger implements EventLogger{
	
	/**
	 * 
	 */
	private static final String COMLINK_FAILURE = "08S01";
	
	/**
	 * 
	 */
	private static final int MAX_LOGGING_ATTEMPTS = 4;

	/**
	 * 
	 */
	public MySqlEventLogger(){
		
	}
	
	/**
	 * @return the unique Event ID of the logged event
	 */
	public int log(long timestamp, String servletName, int responseTime, int userID, String action,
			boolean exception, String ipAddress, String browserType) throws IOException{
		
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet key = null;
		int eventId=0;
		
		for (int i=0 ; i < MAX_LOGGING_ATTEMPTS ; i++){
			try {
							
				connection = ConnectionPool.getConn();
				st = connection.prepareStatement("INSERT INTO log VALUES (?,?,?,?,?,?,?,?,?);");
				st.setInt(1, 0);
				st.setTimestamp(2, new Timestamp(timestamp));
				st.setString(3, servletName);
				st.setShort(4, (short)responseTime);
				
				if (userID == -1)
					st.setNull(5, Types.INTEGER);
				else
					st.setInt(5, userID);
				
				st.setString(6, action);
				st.setBoolean(7, exception);
				st.setString(8, ipAddress);
				st.setString(9, browserType);
				
				st.executeUpdate();
				
				key = st.getGeneratedKeys();
				key.first();
				eventId = key.getInt(1);
				
				break;
				
			} catch (SQLException e){
				
				//try it again if we haven't used up all our attempts yet
				if ((e.getErrorCode() == 0) && 
						(e.getSQLState().equals(COMLINK_FAILURE)) &&
						(i < MAX_LOGGING_ATTEMPTS - 1))
				{					
					continue;
				}
				else {
					//throws new IOException
					throw new IOException("Database error - Error code: " +
							e.getErrorCode() + ", State: " +
							e.getSQLState() + ", Reason: " + e.getMessage());
				}
				
			} finally {
				
				if (key != null) {
					try {
						key.close();
					} catch (SQLException e) {
						Utilities.ignoreChipmarkException(e);
					}
				}
				if(st != null) {
					try {
						st.close();
					} catch (SQLException e) {
						Utilities.ignoreChipmarkException(e);
					}
				}
				if(connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						Utilities.ignoreChipmarkException(e);
					}
				}
				
			}
			
		} //end for
		
		return eventId;
	}
	
}
