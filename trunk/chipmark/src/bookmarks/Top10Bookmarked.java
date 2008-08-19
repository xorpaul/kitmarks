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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class will retrieve the top 10 bookmarks.
 * @author Joshua Fleck
 *
 */
public final class Top10Bookmarked extends HttpServlet {

	/**
	 * The unique identifier for this version of the class
	 */
	private static final long serialVersionUID = 5762476613362426732L;

	/**
	 * The time in milliseconds between updates of the top ten bookmarked (about 15 minutes)
	 */
	private static final long CACHE_TIMEOUT = 900000;

	/**
	 * The top ten bookmarks cached in memory
	 */
	private static ArrayList<TopBookmarkInfo> topTenBookmarks;

	/**
	 * The current number of chipmark users cached in memory
	 */
	private static int currentNumberOfUsers;

	/**
	 * The write lock protecting the cache
	 */
	private static WriteLock cacheWriteLock;

	/**
	 * The read lock protecting the cache
	 */
	private static ReadLock cacheReadLock;

	/**
	 * The thread that periodically updates the cache
	 */
	private static Top10BookmarkedUpdateThread thread;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		//Create the locks
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

		cacheWriteLock = lock.writeLock();

		cacheReadLock = lock.readLock();

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		//Prepopulate the cache
		Top10Bookmarked.writeTopTenBookmarked(db);

		//Start the cache update thread
		thread = new Top10BookmarkedUpdateThread("Top10BookmarkUpdateThread");
		thread.setDaemon(true); //Run the thread in the background		
		thread.start();

		super.init();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		//We clean up after ourselves by stopping the thread
		thread.interrupt();

		super.destroy();
	}

	/**
	 * Writes the cached top ten bookmarks to the xml
	 * @param user The current user
	 * @param xml The current xml
	 */
	private void read(String user, BookmarkXML xml) {
		cacheReadLock.lock();//We don't want the cache to be changed in this block of code

		try {
			xml.constructTop10Bookmarked(user, currentNumberOfUsers, topTenBookmarks);

		}
		catch(Exception e) {}
		finally {
			cacheReadLock.unlock();
		}
	}

	/**
	 * Updates the top ten bookmarked cache and the current number of users cache.
	 * @param db
	 */
	@SuppressWarnings("unchecked")
	protected static void writeTopTenBookmarked(DatabaseWrapper db) {
		cacheWriteLock.lock();//We are updating the cache in this block of code

		try {

			topTenBookmarks = db.getTop10Bookmarked();

			currentNumberOfUsers = db.countUsers();

		} catch (Exception e) {

			Utilities.ignoreChipmarkException(e);

		} finally {
			cacheWriteLock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		Utilities.prepareRequest(request);

		String agent = Utilities.eliminateNull(request.getParameter("agent"));	

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		String user = Utilities.getUsername(request, db);

		BookmarkXML xml = new BookmarkXML();

		this.read(user, xml);//Write the top ten bookmarks to the xml

		if(agent.equals(Utilities.AGENT_EXT)){
			Utilities.prepareResponseAgent(response);

		} else {
			Utilities.prepareResponseWeb(response);
			
			ClientEntry userInfo = Utilities.validateUser(request, db);
			
			Utilities.styleXML(xml, "top10bookmarked", userInfo);
		}


		Utilities.returnResponse(response, xml);
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	/**
	 * This thread will periodically update the cached values in the Top10Bookmarked servlet
	 * @author Joshua Fleck
	 *
	 */
	private class Top10BookmarkedUpdateThread extends Thread {

		/**
		 * Holds the instance of the database wrapper
		 */
		private DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		/**
		 * @param name The name of the thread
		 */
		public Top10BookmarkedUpdateThread(String name) {
			super(name);
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			while(true) {

				try {
					Thread.sleep(Top10Bookmarked.CACHE_TIMEOUT);
				} catch (InterruptedException e) {
					return;
				}

				Top10Bookmarked.writeTopTenBookmarked(this.db);
			}
		}

	}

}

