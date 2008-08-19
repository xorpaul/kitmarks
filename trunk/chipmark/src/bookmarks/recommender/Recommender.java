/**
 * 
 */
package bookmarks.recommender;

import java.io.IOException;
import java.sql.SQLException;

import bookmarks.DatabaseWrapper;
import bookmarks.Utilities;

/**
 * This class is the recommender engine.
 * @author Joshua Fleck
 *
 */
public class Recommender extends Thread {

	/** The number of recommendations to retrieve per user */
	public static final int RECOMMEND_SIZE = 5;

	/** The users to generate recommendations for */
	public USearch[] uSearch = null;

	/** The number of users to generate recommendations for */
	public int length = -1;

	/** The start point in the array for this thread */
	public int start = -1;

	/** The end point in the array for this thread  */
	public int end = -1;

	/** A DatabaseWrapper object */
	public DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

	public int numberUrlsCommon = 0;

	/**
	 * 
	 * @param search The users to generate recommendations for
	 * @param start The start point in the array
	 * @param end The end point in the array
	 * @param threadName The name of this thread
	 */
	public Recommender(USearch[] search, int start, int end, String threadName) {
		super(threadName);
		uSearch = search;
		this.length = uSearch.length;
		this.start = start;
		this.end = end;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		//Date startDate = new Date();
		//System.out.println(this.getName()+" START\nnumber of users:"+(this.end - this.start)+"\ntime:"+startDate.toString()+"\n");

		USearch u1 = null;
		USearch u2 = null;
		int[] recommendations = null;
		for(int i=this.start; i<this.end; i++) {

			u1 = this.uSearch[i];

			u2 = this.getBestMatch(u1);

			recommendations = this.retrieveRecommendations(u1, u2);

			//System.out.println("recommendations for "+i+" of "+this.end+" user:"+u1.userID);
			//for(int s: recommendations) {
			//	System.out.println(s);
			//}

			this.saveRecommendations(u1.userID, recommendations);
		}

		//System.out.println(this.getName()+" END\nnumber of users:"+(this.end - this.start)+"\nstart time:"+startDate+"\nend time:"+(new Date()).toString()+"\n\n");
	}

	/**
	 * Returns the number of URLS the 2 users have in common
	 * @param u1 
	 * @param u2
	 * @return
	 */
	public int urlsInCommon(USearch u1, USearch u2) {

		int count = 0;

		int[] u1Url = u1.urls;
		int[] u2Url = u2.urls;

		int currentU1Url = -1;		
		for(int i=0; i<u1Url.length; i++) {

			currentU1Url = u1Url[i];

			for(int j=0; j<u2Url.length; j++) {

				if(currentU1Url == u2Url[j]) {

					count++;

				}

			}

		}		

		return count;
	}

	/**
	 * Locates the user in the list with the most corresponding urls. 
	 * @param u1
	 * @return
	 */
	public USearch getBestMatch(USearch u1) {

		USearch bestMatch = null;
		int numberMatches = -1;

		for(int i=0; i<this.length; i++) {

			USearch u2 = this.uSearch[i];

			if(u2.userID != u1.userID) {

				int match = this.urlsInCommon(u1, u2);

				if(match > numberMatches) {
					numberMatches = match;
					bestMatch = u2;
				}

			}

		}	

		if(numberMatches > 0) {
			this.numberUrlsCommon++;
		}

		//System.out.println("best match for user:"+u1.userID+" is user:"+bestMatch.userID+" with "+numberMatches+" urls in common");

		//System.out.println("number of people with at least one URL in common for this thread:"+this.numberUrlsCommon);

		return bestMatch;
	}

	/**
	 * Determines which URLs the users do not have in common.
	 * @param u1
	 * @param u2
	 * @return An array of URL recommendations, NOTE, not all recommendations may be set.
	 */
	public int[] retrieveRecommendations(USearch u1, USearch u2) {

		int[] recommendations = new int[RECOMMEND_SIZE];

		int count = 0;

		int[] u1Url = u1.urls;
		int[] u2Url = u2.urls;

		boolean urlFound = false;
		int currentU1Url = -1;		
		int currentU2Url = -1;
		for(int i=0; i<u2Url.length; i++) {//Go through their urls

			currentU2Url = u2Url[i];

			for(int j=0; j<u1Url.length; j++) {//See if we both have it chipmarked

				currentU1Url = u1Url[j];

				if(currentU1Url == currentU2Url) {// This is a URL we have in common

					urlFound = true;
					break;

				}

			}

			if(!urlFound) {// The url is not already chipmarked by the user

				boolean recommendationFound = false;
				for(int j=0; j<count; j++ ) {//See if it is already in the recommendations
					
					if(currentU2Url == recommendations[j]) {
						recommendationFound = true;
						break;
					}
					
				}

				if(!recommendationFound) {//The url is not already in the recommendations, save it
					
					recommendations[count] = currentU2Url;

					count++;

					if(count == RECOMMEND_SIZE) {// We have filled up the recommendations
						return recommendations;
					}

				}
				
				recommendationFound = false;

			}

			urlFound = false;

		}

		return recommendations;
	}

	/**
	 * This will save the recommendations for the user in the database, and also delet their old recommendations.
	 * @param uid User id
	 * @param recommendations
	 */
	public void saveRecommendations(int uid, int[] recommendations) {

		try {

			db.deleteRecommendationsForClient(uid);

			for(int i:recommendations) {

				if(i != 0) {
					db.insertRecommendation(uid, i);
				}

			}

		} catch (SQLException e) {
			Utilities.ignoreChipmarkException(e);
		}		

		//System.out.println("recommendations saved\n");

	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		USearch[] uSearch = Parser.readFile(args[0]);/*new USearch[] {
				(new USearch(0, new int[] {})),
				(new USearch(1, new int[] {0})),
				(new USearch(2, new int[] {0,1})),
				(new USearch(3, new int[] {0,1,2})),
				(new USearch(4, new int[] {0,1,2,3})),
				(new USearch(5, new int[] {0,1,2,3,4})),
				(new USearch(6, new int[] {0,1,2,3,4,5})),
				(new USearch(7, new int[] {0,1,2,3,4,5,6})),
				(new USearch(8, new int[] {0,1,2,3,4,5,6,7})),
				(new USearch(9, new int[] {0,1,2,3,4,5,6,7,8})),
				(new USearch(10, new int[] {0,1,2,3,4,5,6,7,8,9})),};*/

		Recommender recommender = new Recommender(uSearch,0,uSearch.length,"Recommender 1");

		recommender.start();

	}

}
