/**
 * 
 */
package bookmarks;

/**
 * @author Joshua Fleck
 *
 */
public class LinkNameUrlObject {

	/**
	 * Contains the name of this bookmark.
	 */
	private String linkName;

	/**
	 * Contains the URL for this bookmark.
	 */
	private String linkURL;

	/**
	 * Contains the date the url was added
	 */
	private String linkDate;

	/**
	 * @param linkName
	 * @param linkURL
	 * @param linkDate
	 */
	public LinkNameUrlObject(String linkName, String linkURL, String linkDate) {
		this.linkName = linkName;
		this.linkURL = linkURL;
		this.linkDate = linkDate;
	}

	/**
	 * @param linkName
	 * @param linkURL
	 */
	public LinkNameUrlObject(String linkName, String linkURL) {
		this.linkName = linkName;
		this.linkURL = linkURL;
	}

	/**
	 * @param linkURL
	 */
	public LinkNameUrlObject(String linkURL) {
		this.linkURL = linkURL;
	}

	/**
	 * @return the linkDate
	 */
	public String getLinkDate() {
		return linkDate;
	}

	/**
	 * @param linkDate the linkDate to set
	 */
	public void setLinkDate(String linkDate) {
		this.linkDate = linkDate;
	}

	/**
	 * @return the linkName
	 */
	public String getLinkName() {
		return linkName;
	}

	/**
	 * @param linkName the linkName to set
	 */
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	/**
	 * @return the linkURL
	 */
	public String getLinkURL() {
		return linkURL;
	}

	/**
	 * @param linkURL the linkURL to set
	 */
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}	
	
}
