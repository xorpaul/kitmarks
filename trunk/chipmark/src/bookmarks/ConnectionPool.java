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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * This class should be used to retrieve database connections.
 * Please remember to close ALL ResultSet PreparedStatement and
 * Connection objects. If you fail to do so you WILL create a memory
 * leak and then chip 06 will spank you with moon rocks. Connection pooling requires 
 * all resources to be released before the connection can be returned 
 * to the pool.
 * 
 * @author Joshua Fleck
 *
 */
public class ConnectionPool {


	/* **** FOR JUNIT TESTING ONLY **** //
	 *Set this stuff ONLY FOR JUNIT TESTING
	 *For regular use this is IGNORED! (values in server.xml are used instead)
	*/
	
	/**
	 * Set to true for JUnit, false for Tomcat (default)
	 */
	private static boolean isJunit = false;

	/**
	 * The default database domain.
	 */
	private static final String defaultDomain = "localhost";

	/**
	 * The name of the default database.
	 * Note: this must change between the testing and the production database
	 */
	private static final String defaultName   = "chipmark_dev";

	/**
	 * The default database username.
	 * Note: this must change between the testing and the production database
	 */
	private static final String defaultUser   = "chipmark_dev";

	/**
	 * The default database password.
	 * Note: this must change between the testing and the production database
	 */
	private static final String defaultPass   = "WC3UT2K4HL2";
	// **** END JUNIT TESTING ONLY **** //

	/**
	 * Provides the database connections.
	 */
	private static DataSource ds;

	/**
	 * Here we instantiate the single instance of DatabaseWrapper. We instantiate here rather than
	 * in the retrieval method to avoid dealing with synchronization issues.
	 */
	static
	{
		try {
			//If we are running a jUnit test, connection pooling may not be used. 
			//Add this argument to the JVM to enable a direct database connection: -Dchipmark_test="true"
			if(System.getProperty("chipmark_test") != null) {
				ConnectionPool.isJunit = true;
			}

			if(!isJunit) {
				try {
					Context ctx = new InitialContext();
					ds = (DataSource)ctx.lookup("java:comp/env/jdbc/ChipmarkDB");
				} catch(Exception ex) {
					throw new SQLException("Failed to open ChipmarkDB DataSource for Connection Pooling.  Is Tomcat correctly configured? Is mysql running? : " + ex.getMessage());
				}
			}

		} catch (SQLException e) {
			System.out.println("Error creating ConnectionPool. If this is a jUnit test add this argument to the JVM: -Dchipmark_test=\"true\"");
			Utilities.ignoreChipmarkException(e);
		}
	}

	/**
	 *  @return A new connection either:
	 * 		from Tomcat's connection pool
	 * 		OR a totally new connection for JUnit testing
	 *		depending on how the isJunit variable is set
	 * @author andyk
	 * @throws SQLException
	 */
	public static Connection getConn()
	throws SQLException {
		if(!isJunit) { //Check if we are using JUnit or Tomcat
			try {
				return ds.getConnection();
			} catch (Exception ex) {
				throw new SQLException("Unable to get DB Connection from Pool: " + ex.getMessage());
			}
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				return DriverManager.getConnection("jdbc:mysql://" + defaultDomain + "/" + defaultName + "?user=" + defaultUser + "&password=" + defaultPass);
			} catch (Exception ex) {
				throw new SQLException("Unable to open new DB Connection. Is mysql running? : " + ex.getMessage());
			}
		}
	}


}
