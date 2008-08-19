/**
 * 
 */
package bookmarks.recommender;

/**
 * The data object representing a user
 * @author Joshua Fleck
 *
 */
public class USearch {
	
	/** The id of the user */
	public int userID = -1;
	
	/** An array of the user's URLs */
	public int[] urls = null;

	/**
	 * @param userID
	 * @param urls
	 */
	public USearch(int userID, int[] urls) {
		this.userID = userID;
		this.urls = urls;
	}
		
}
