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

/**
 * This class holds a top 10 URL
 * @author Joshua Fleck
 *
 */
public class TopBookmarkInfo extends LinkNameUrlObject {
	
	/**
	 * The number of times the URL shows up in the database
	 */
	private int repetitions;
	
	/**
	 * The overall rank of this URL
	 */
	private int rank;

	/**
	 * @param linkURL String URL
	 * @param repetitions int number of times the URL shows up in the database
	 * @param rank int overall rank of this URL
	 */
	public TopBookmarkInfo(String linkURL, int repetitions, int rank) {
		super(linkURL);
		this.repetitions = repetitions;
		this.rank = rank;
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return the repetitions
	 */
	public int getRepetitions() {
		return repetitions;
	}

	/**
	 * @param repetitions the repetitions to set
	 */
	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}


}
