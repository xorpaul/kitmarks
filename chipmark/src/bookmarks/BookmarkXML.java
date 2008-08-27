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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import bookmarks.buddylist.BuddyList.Buddy;

/**
 * 
 * @author Joshua Fleck
 * 
 */
public class BookmarkXML {

	private static final String XML = "xml";

	public static final String SUCCESS = "SUCCESS";

	public static final String FAILURE = "FAILURE";

	/**
	 * 
	 */
	Document xmlDoc;

	/**
	 * 
	 * 
	 */
	public BookmarkXML() {
		xmlDoc = DocumentHelper.createDocument();
	}

	/**
	 * 
	 * @param xslFileName
	 * */

	public void styleDocument(String xslFileName, String prefLang) {
		try {
			TransformerFactory factory = TransformerFactory.newInstance();

			if (prefLang.equals("eng")) {
				Transformer transformer = factory
						.newTransformer(new StreamSource("file:///"
								+ PropertyManager.CATALINA_BASE
								+ PropertyManager.XSLPATHENG + xslFileName));

				String test2 = PropertyManager.CATALINA_BASE
						+ PropertyManager.XSLPATHENG + xslFileName;

				transformer.setOutputProperty(OutputKeys.ENCODING,
						Utilities.REQUEST_ENCODING);
				DocumentSource source = new DocumentSource(xmlDoc);
				DocumentResult result = new DocumentResult();

				transformer.transform(source, result);

				xmlDoc = result.getDocument();
			} else {

				String test2 = PropertyManager.CATALINA_BASE
						+ PropertyManager.XSLPATHGER + xslFileName;

				Transformer transformer = factory
						.newTransformer(new StreamSource("file:///"
								+ PropertyManager.CATALINA_BASE
								+ PropertyManager.XSLPATHGER + xslFileName));

				transformer.setOutputProperty(OutputKeys.ENCODING,
						Utilities.REQUEST_ENCODING);
				DocumentSource source = new DocumentSource(xmlDoc);
				DocumentResult result = new DocumentResult();

				transformer.transform(source, result);

				xmlDoc = result.getDocument();

			}
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new RuntimeException(e);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a Target element to the XML for use by XSL templates
	 * 
	 * @param target
	 */
	@SuppressWarnings("unused")
	public void addXSLTarget(String target) {
		Element root = (Element) xmlDoc.getRootElement();

		if (root == null) {
			root = xmlDoc.addElement(XML);
		}

		if (target == null) {
			target = new String();
		}
		Element targetXml = root.addElement("Target").addText(target);
	}

	/**
	 * 
	 * @param userName
	 * @param resultMsg
	 */
	@SuppressWarnings("unused")
	public void constructWithResultMsg(String userName, String resultMsg) {
		Element root = xmlDoc.addElement(XML);
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);
		}
		if (resultMsg == null) {
			resultMsg = new String();
		}

		Element resultXml = root.addElement("Result").addText(resultMsg);

	}

	/**
	 * 
	 * @param userName
	 * @param resultMsgs
	 */
	@SuppressWarnings("unused")
	public void constructWithMultResultMsg(String userName,
			ArrayList<ResultMessage> resultMsgs, String newClientMail,
			String newPassword, String newPassword2, String oldPassword) {
		Element root = xmlDoc.addElement(XML);
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);
		}
		if (resultMsgs == null) {
			resultMsgs.add(new ResultMessage("", ""));
		}

		Element resultXml = root.addElement("Result");

		for (Iterator it = resultMsgs.iterator(); it.hasNext();) {
			ResultMessage resultMsg = (ResultMessage) it.next();
			Element resultXmlMsg = resultXml.addElement("Message");
			Element resultXmlMsgVal = resultXmlMsg.addElement("Value").addText(
					resultMsg.getMessage());

			Element resultXmlMsgType = resultXmlMsg.addElement("Type").addText(
					resultMsg.getType());

		}

		Element name = root.addElement("NewClientMail").addText(newClientMail);

		Element pass = root.addElement("NewPassword").addText(newPassword);

		Element pass2 = root.addElement("NewPassword2").addText(newPassword2);

		Element mail = root.addElement("OldPassword").addText(oldPassword);
	}

	/**
	 * 
	 * @param userName
	 * @param error
	 */
	@SuppressWarnings("unused")
	public void constructAddClientResult(String userName, String error,
			String clientName, String clientMail) {
		Element root = xmlDoc.addElement(XML);
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);

			if (error == null) {
				error = new String();
			}

			if (clientName == null) {
				clientName = new String();
			}

			if (clientMail == null) {
				clientMail = new String();
			}

			Element errorXml = root.addElement("Result").addText(error);

			Element name = root.addElement("ClientName").addText(clientName);

			Element mail = root.addElement("ClientMail").addText(clientMail);
		}
	}

	/**
	 * 
	 * @param userName
	 * @param url
	 */
	@SuppressWarnings("unused")
	public void constructAddLink(String userName, String url) {
		Element root = xmlDoc.addElement(XML);
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);
		}
		if (url == null) {
			url = new String("");
		}

		Element urlE = root.addElement("URL").addText(url);
	}

	/**
	 * 
	 * @param userName
	 * @param linkName
	 * @param linkURL
	 * @param linkPermission
	 * @param linkDescription
	 * @param linkLabels
	 * @param toolbarPosition
	 * @param error
	 */
	@SuppressWarnings("unused")
	public void constructAddLinkResult(String userName, String linkName,
			String linkURL, String linkPermission, String linkDescription,
			String linkLabels, String toolbarPosition, String error) {
		Element root = xmlDoc.addElement(XML);
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element theLinkName = root.addElement("Name").addText(linkName);

			Element theLinkURL = root.addElement("URL").addText(linkURL);

			Element theLinkPermission = root.addElement("Permission").addText(
					linkPermission);

			Element theLinkDescription = root.addElement("Description")
					.addText(linkDescription);

			Element theLinkLabels = root.addElement("Labels").addText(
					linkLabels);

			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);

			Element errorXml = root.addElement("Result").addText(error);

			Element theToolbarPosition = root.addElement("ToolbarPosition")
					.addText(toolbarPosition);
		}
	}

	/**
	 * 
	 * @param userName
	 * @param error
	 */
	@SuppressWarnings("unused")
	public void constructAddLinkResult(String userName, String error) {
		Element root = xmlDoc.addElement(XML);
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);

			Element errorXml = root.addElement("Result").addText(error);
		}
	}

	/**
	 * 
	 * @param userName
	 * @param target
	 * @param error
	 */
	@SuppressWarnings("unused")
	public void constructImportResult(String userName, String target,
			String error) {
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element root = xmlDoc.addElement(XML);

			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);

			Element errorXml = root.addElement("Result").addText(error);

			Element targetXml = root.addElement("Target").addText(target);
		}
	}

	/**
	 * 
	 * @param userName
	 * @param bookmark
	 */
	public void constructRandom(String userName, LinkNameUrlObject random) {
		Element root = xmlDoc.addElement(XML);
		root.addElement("LoggedInAs").addText(userName);
		root.addElement("Result").addText("SUCCESS");
		Element bookmarkList = root.addElement("BookmarkList");
		Element bookmark = bookmarkList.addElement("Bookmark");
		bookmark.addElement("Title").addText(random.getLinkName());
		bookmark.addElement("URL").addText(random.getLinkURL());
		bookmark.addElement("EncodedLink").addText(
				Utilities.encodeURIComponent(random.getLinkURL()));
		bookmark.addElement("EncodedName").addText(
				Utilities.encodeURIComponent(random.getLinkName()));
		if (!userName.equals("")) {
			bookmark.addElement("LoggedIn");
		}
	}

	/**
	 * @author Brian Terlson
	 * @param folders
	 *            Shared Folders
	 */
	public void constructSharedFolderInvites(ArrayList<SharedFolder> folders) {
		Element root = xmlDoc.addElement(XML);
		root.addElement("Result").addText(SUCCESS);
		Element folderList = root.addElement("folders");
		for (int i = 0; i < folders.size(); i++) {
			Element folder = folderList.addElement("folder");
			folder.addElement("name").addText(folders.get(i).getFolderName());
			folder.addElement("id").addText(
					String.valueOf(folders.get(i).getFolderID()));
			folder.addElement("ownerName").addText(
					folders.get(i).getOwnerName());
		}
	}

	/**
	 * 
	 * @param userName
	 * @param bookmarks
	 */
	public void constructMostRecentlyAdded(String userName,
			ArrayList<LinkNameUrlObject> bookmarks) {
		Element root = xmlDoc.addElement(XML);

		root.addElement("LoggedInAs").addText(userName);

		Element bookmarkList = root.addElement("BookmarkList");

		for (int i = 0; i < bookmarks.size(); i++) {
			LinkNameUrlObject temp = bookmarks.get(i);

			Element bookmark = bookmarkList.addElement("Bookmark");

			if (!userName.equals("")) {
				bookmark.addElement("LoggedIn");
			}

			bookmark.addElement("Title").addText(temp.getLinkName());

			bookmark.addElement("URL").addText(temp.getLinkURL());

			bookmark.addElement("Date").addText(temp.getLinkDate());

			bookmark.addElement("EncodedLink").addText(
					Utilities.encodeURIComponent(temp.getLinkURL()));

			bookmark.addElement("EncodedName").addText(
					Utilities.encodeURIComponent(temp.getLinkName()));

		}
	}

	/**
	 * 
	 * @param userName
	 * @param bookmarks
	 */
	public void constructRecommendations(String userName,
			ArrayList<LinkNameUrlObject> bookmarks) {
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element root = xmlDoc.addElement(XML);

			root.addElement("LoggedInAs").addText(userName);

			Element bookmarkList = root.addElement("BookmarkList");

			for (int i = 0; i < bookmarks.size(); i++) {
				LinkNameUrlObject temp = bookmarks.get(i);

				Element bookmark = bookmarkList.addElement("Bookmark");

				if (!userName.equals("")) {
					bookmark.addElement("LoggedIn");
				}

				if (temp.getLinkName() != null) {
					bookmark.addElement("Title").addText(temp.getLinkName());
				} else {
					bookmark.addElement("Title").addText(temp.getLinkURL());
				}

				bookmark.addElement("URL").addText(temp.getLinkURL());

				bookmark.addElement("EncodedLink").addText(
						Utilities.encodeURIComponent(temp.getLinkURL()));

				bookmark.addElement("EncodedName").addText(
						Utilities.encodeURIComponent(temp.getLinkName()));

			}
		}
	}

	/**
	 * 
	 * @param userName
	 * @param bookmarks
	 * @param searchString
	 */
	public void constructSearchLinks(String userName,
			ArrayList<Bookmark> bookmarks, String searchString) {
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element root = xmlDoc.addElement(XML);

			root.addElement("LoggedInAs").addText(userName);

			Element bookmarkList = root.addElement("BookmarkList");

			String size = "" + bookmarks.size();

			// if (searchString.length() < 3)
			// root.addElement("ResultSize").addText("-1");
			// else
			root.addElement("ResultSize").addText(size);
			root.addElement("SearchString").addText(searchString);

			for (int i = 0; i < bookmarks.size(); i++) {

				Bookmark temp = bookmarks.get(i);

				Element bookmark = bookmarkList.addElement("Bookmark");

				bookmark.addElement("Title").addText(temp.getLinkName());

				bookmark.addElement("URL").addText(temp.getLinkURL());

				bookmark.addElement("Description").addText(
						temp.getLinkDescription());

				bookmark.addElement("EncodedLink").addText(
						Utilities.encodeURIComponent(temp.getLinkURL()));

				bookmark.addElement("EncodedName").addText(
						Utilities.encodeURIComponent(temp.getLinkName()));

				if (!userName.equals("")) {
					bookmark.addElement("LoggedIn");
				}
			}

		}
	}

	public void constructFacebookLogin(String userName) {

		Element root = xmlDoc.addElement(XML);
		root.addElement("ChipmarkUser").addText(userName);

		addXSLTarget("facebookLogin");

	}

	/**
	 * 
	 * @param userName
	 * @param target
	 */
	public void constructMain(String userName, String target) {
		Element root = xmlDoc.addElement(XML);
		if (userName == null) {
			userName = new String();
		}
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged"))
			root.addElement("LoggedInAs").addText(userName);

		if (target != null) {
			root.addElement("Target").addText(target);
		}

	}

	/**
	 * 
	 * @param userName
	 * @param count
	 * @param target
	 */
	@SuppressWarnings("unused")
	public void constructMainCount(String userName, int count, String target) {
		Element root = xmlDoc.addElement(XML);
		if (userName == null) {
			userName = new String();
		}

		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);

			Element userBookmarkCount = root.addElement("userBookmarkCount")
					.addText((new Integer(count)).toString());
		}
		if (target != null) {
			Element targetXml = root.addElement("Target").addText(target);
		}
	}

	/**
	 * 
	 * @param userName
	 * @param errorMsg
	 */
	@SuppressWarnings("unused")
	public void constructLogin(String userName, String errorMsg) {
		Element root = xmlDoc.addElement(XML);

		Element loggedInAs = root.addElement("LoggedInAs").addText(userName);

		Element errorLog = root.addElement("Error").addText(errorMsg);

	}

	/**
	 * this is added to fix bug 665
	 * 
	 * @param userName
	 * @param linkID
	 * @param linkName
	 * @param linkURL
	 * @param linkPermission
	 * @param linkDescription
	 * @param linkLabels
	 * @param linkToolbarPosition
	 * @param error
	 */
	@SuppressWarnings("unused")
	public void constructEditLinkResult(String userName, String linkID,
			String linkName, String linkURL, String linkPermission,
			String linkDescription, ArrayList linkLabels,
			String linkToolbarPosition, String error) {
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element root = xmlDoc.addElement(XML);

			Element resultError = root.addElement("Result").addText(error);

			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);

			Element bookmarkElem = root.addElement("Bookmark").addAttribute(
					"id", linkID);

			Element title = bookmarkElem.addElement("Title").addText(linkName);

			Element url = bookmarkElem.addElement("Link").addText(linkURL);

			Element permission = bookmarkElem.addElement("Permission").addText(
					linkPermission);

			Element description = bookmarkElem.addElement("Description")
					.addText(linkDescription);

			Element labelList = bookmarkElem.addElement("LabelList");

			if (linkLabels != null) {
				for (int i = 0; i < linkLabels.size(); i++) {
					String curLabel = (String) linkLabels.get(i);
					Element label = labelList.addElement("Label").addAttribute(
							"name", curLabel);
				}
			}

			Element toolbarPosition = bookmarkElem
					.addElement("ToolbarPosition").addText(linkToolbarPosition);
		}
	}

	/**
	 * 
	 * @param bookmark
	 * @param userName
	 */
	@SuppressWarnings("unused")
	public void constructEditLink(Bookmark bookmark, String userName) {
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element root = xmlDoc.addElement(XML);

			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);

			Integer bookmarkID = new Integer(bookmark.getLinkID());
			Element bookmarkElem = root.addElement("Bookmark").addAttribute(
					"id", bookmarkID.toString()).addAttribute("permission",
					bookmark.getLinkPermission());

			Element title = bookmarkElem.addElement("Title").addText(
					bookmark.getLinkName());

			Element url = bookmarkElem.addElement("Link").addText(
					bookmark.getLinkURL());

			Element permission = bookmarkElem.addElement("Permission").addText(
					bookmark.getLinkPermission());

			Element description = bookmarkElem.addElement("Description")
					.addText(bookmark.getLinkDescription());

			Element labelList = bookmarkElem.addElement("LabelList");

			Iterator labelsIter = bookmark.getLabels();
			while (labelsIter.hasNext()) {
				String curLabel = (String) labelsIter.next();
				Element label = labelList.addElement("Label").addAttribute(
						"name", curLabel);
			}

			Element toolbarPosition = bookmarkElem
					.addElement("ToolbarPosition").addText(
							String.valueOf(bookmark.getToolbarPosition()));
		}
	}

	/**
	 * 
	 * @param matches
	 * @param userName
	 * @param labels
	 */
	// @SuppressWarnings("unused")
	public void constructViewLinks(ArrayList<Bookmark> matches,
			String userName, ArrayList<String> labels) {
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element root = xmlDoc.addElement(XML);

			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);

			Element allLabelList = root.addElement("AllLabelList");

			Iterator<String> labelsIter = labels.iterator();
			while (labelsIter.hasNext()) {
				String curLabel = labelsIter.next();
				Element label = allLabelList.addElement("Label").addAttribute(
						"name", curLabel);
			}

			Element bookmarkList = root.addElement("BookmarkList");

			for (Iterator<Bookmark> it = matches.iterator(); it.hasNext();) {
				Bookmark x = it.next();

				Integer bookmarkID = new Integer(x.getLinkID());
				Element bookmark = bookmarkList.addElement("Bookmark")
						.addAttribute("id", bookmarkID.toString())
						.addAttribute("permission", x.getLinkPermission());

				Element title = bookmark.addElement("Title").addText(
						x.getLinkName());

				Element url = bookmark.addElement("Link").addText(
						x.getLinkURL());

				Element description = bookmark.addElement("Description")
						.addText(x.getLinkDescription());

				Element labelList = bookmark.addElement("LabelList");

				labelsIter = x.getLabels();
				while (labelsIter.hasNext()) {
					String curLabel = (String) labelsIter.next();
					Element label = labelList.addElement("Label").addAttribute(
							"name", curLabel);
				}

				Element toolbarPosition = bookmark
						.addElement("ToolbarPosition").addText(
								String.valueOf(x.getToolbarPosition()));
			}
		}
	}

	/**
	 * 
	 * @param matches
	 * @param userName
	 * @param labels
	 * @param msg
	 * @param rejects
	 * @param search
	 */
	public void constructSearchUser(ArrayList<Bookmark> matches,
			String userName, String search) {
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element root = xmlDoc.addElement(XML);

			root.addElement("LoggedInAs").addText(userName);

			root.addElement("SearchString").addText(search);

			if (matches.size() > 0) {
				root.addElement("Message").addText("");
			}

			else {
				root.addElement("Message").addText(
						"Your search for '" + search + "' yielded no results.");
			}

			// Element allLabelList = root.addElement("AllLabelList");

			Iterator labelsIter;// = labels.iterator();
			/*
			 * while(labelsIter.hasNext()){ String curLabel =
			 * (String)labelsIter.next();
			 * allLabelList.addElement("Label").addAttribute("name", curLabel);
			 * }
			 */

			Element bookmarkList = root.addElement("BookmarkList");

			for (Iterator<Bookmark> it = matches.iterator(); it.hasNext();) {
				Bookmark x = it.next();

				Integer bookmarkID = new Integer(x.getLinkID());
				Element bookmark = bookmarkList.addElement("Bookmark")
						.addAttribute("id", bookmarkID.toString())
						.addAttribute("permission", x.getLinkPermission());

				bookmark.addElement("Title").addText(x.getLinkName());

				bookmark.addElement("Link").addText(x.getLinkURL());

				bookmark.addElement("Description").addText(
						x.getLinkDescription());

				Element labelList = bookmark.addElement("LabelList");

				labelsIter = x.getLabels();
				while (labelsIter.hasNext()) {
					String curLabel = (String) labelsIter.next();
					labelList.addElement("Label")
							.addAttribute("name", curLabel);
				}

				bookmark.addElement("ToolbarPosition").addText(
						String.valueOf(x.getToolbarPosition()));
			}

		}
	}

	/**
	 * 
	 * @param userName
	 * @param numberOfUsers
	 * @param matches
	 */
	@SuppressWarnings("unused")
	public void constructTop10Bookmarked(String userName, int numberOfUsers,
			ArrayList<TopBookmarkInfo> matches) {
		Element root = xmlDoc.addElement(XML);

		if (numberOfUsers == 0) {
			numberOfUsers = 1;
		}

		int length = String.valueOf(numberOfUsers).length();

		Element loggedInAs = root.addElement("LoggedInAs").addText(userName);

		Element bookmarkList = root.addElement("BookmarkList");

		for (Iterator<TopBookmarkInfo> it = matches.iterator(); it.hasNext();) {
			TopBookmarkInfo x = it.next();

			Element bookmark = bookmarkList.addElement("Bookmark");

			if (!userName.equals("")) {
				bookmark.addElement("LoggedIn");
			}

			Element url = bookmark.addElement("Link").addText(x.getLinkURL());

			Element encodedUrl = bookmark.addElement("EncodedLink").addText(
					Utilities.encodeURIComponent(x.getLinkURL()));

			Element hitcount = bookmark.addElement("HitCount").addText(
					String.valueOf(x.getRepetitions()));

			Element hitcountsort = bookmark.addElement("HitCountToSortBy")
					.addText(paddedValueOf(x.getRepetitions(), length, '0'));

			Element rank = bookmark.addElement("Rank").addText(
					String.valueOf(x.getRank()));

			Element numberOfUsers_e = bookmark.addElement("NumberOfUsers")
					.addText(String.valueOf(numberOfUsers));

			Element percentage = bookmark.addElement("Percentage").addText(
					String.valueOf((x.getRepetitions() * 100) / numberOfUsers));

		}
	}

	/**
	 * 
	 * @param links
	 * @param folders
	 * @param labels
	 */
	@SuppressWarnings("unused")
	public void constructFoldersAndLinks(ArrayList<Bookmark> links,
			ArrayList<Folder> folders, ArrayList<String> labels) {
		Element root = xmlDoc.addElement(XML);

		Element result = root.addElement("Result").addText(SUCCESS);

		Element allLabelList = root.addElement("AllLabelList");

		Iterator<String> labelsIter = labels.iterator();
		while (labelsIter.hasNext()) {
			String curLabel = labelsIter.next();
			Element label = allLabelList.addElement("Label").addAttribute(
					"name", curLabel);
		}

		Element bookmarkList = root.addElement("BookmarkList");

		for (Iterator<Bookmark> it = links.iterator(); it.hasNext();) {
			Bookmark x = it.next();

			Integer bookmarkID = new Integer(x.getLinkID());
			Element bookmark = bookmarkList.addElement("Bookmark")
					.addAttribute("id", bookmarkID.toString()).addAttribute(
							"permission", x.getLinkPermission());

			Element title = bookmark.addElement("Title").addText(
					stripNonValidXMLCharacters(x.getLinkName()));

			Element url = bookmark.addElement("Link").addText(x.getLinkURL());

			Element description = bookmark.addElement("Description").addText(
					stripNonValidXMLCharacters(x.getLinkDescription()));

			Element labelList = bookmark.addElement("LabelList");

			labelsIter = x.getLabels();
			while (labelsIter.hasNext()) {
				String curLabel = labelsIter.next();
				Element label = labelList.addElement("Label").addAttribute(
						"name", curLabel);
			}

			Element linkFolderParentID = bookmark.addElement(
					"linkFolderParentID").addText(
					String.valueOf(x.getLinkFolderParentID()));

			Element icon = bookmark.addElement("Icon").addText("");

			Element toolbarPosition = bookmark.addElement("ToolbarPosition")
					.addText(
							stripNonValidXMLCharacters(String.valueOf(x
									.getToolbarPosition())));
		}

		Element folderList = root.addElement("FolderList");

		for (Iterator<Folder> it = folders.iterator(); it.hasNext();) {
			Folder x = it.next();

			Element folder = folderList.addElement("Folder").addAttribute("id",
					String.valueOf(x.getFolderID())).addAttribute("parent",
					String.valueOf(x.getFolderParentID())).addAttribute("type",
					x.getFolderType());

			Element folderID = folder.addElement("FolderName").addText(
					Utilities.eliminateNull(x.getFolderName()));
			Element subbedUsers = folder.addElement("SubscribedUsers");
			if (x.subscribedUsers != null) {
				for (int i = 0; i < x.subscribedUsers.length; i++) {
					subbedUsers.addElement("username").addText(
							x.subscribedUsers[i]);
				}
			}
			Element toolbarPosition = folder.addElement("ToolbarPosition")
					.addText(
							Utilities.eliminateNull(String.valueOf(x
									.getToolbarPosition())));
		}
	}

	/**
	 * 
	 * @param result
	 * @param message
	 */
	@SuppressWarnings("unused")
	public void constructNodeAction(String result, String message) {
		Element root = xmlDoc.getRootElement();

		if (root != null)
			root.detach();

		root = xmlDoc.addElement(XML);
		Element resultE = root.addElement("Result").addText(result);
		Element messageE = root.addElement("Message").addText(message);
	}

	/**
	 * Constructs a Node Action success
	 */
	@SuppressWarnings("unused")
	public void constructNodeActionSuccess() {
		Element root = xmlDoc.getRootElement();

		if (root != null)
			root.detach();

		root = xmlDoc.addElement(XML);
		Element resultE = root.addElement("Result").addText(SUCCESS);
		Element messageE = root.addElement("Message").addText("");
	}

	/**
	 * 
	 * Constructs a Node Action failure
	 * 
	 * @param message
	 */
	@SuppressWarnings("unused")
	public void constructNodeActionFailure(String message) {
		Element root = xmlDoc.getRootElement();

		if (root != null)
			root.detach();

		root = xmlDoc.addElement(XML);
		Element resultE = root.addElement("Result").addText(FAILURE);
		Element messageE = root.addElement("Message").addText(message);
	}

	/**
	 * This will create an generic success message.
	 */
	public void constructGenericSuccess() {
		Document document = this.xmlDoc;
		Element root = document.addElement(XML);
		Element status = root.addElement("result");

		status.addElement("status").addText(SUCCESS);
		status.addElement("message");
	}

	/**
	 * 
	 * This will create an generic failure message.
	 * 
	 * @param message
	 */
	public void constructGenericFailure(String errMessage) {
		Document document = this.xmlDoc;
		Element root = document.addElement(XML);
		Element status = root.addElement("result");

		status.addElement("status").addText(FAILURE);
		status.addElement("message").addText(errMessage);
	}

	/**
	 * 
	 * @param userName
	 */
	@SuppressWarnings("unused")
	public void constructExport(String userName) {
		if (!userName.equals("SessionDummy")
				&& !userName.equals("SessionDummyChanged")) {
			Element root = xmlDoc.addElement(XML);

			Element loggedInAs = root.addElement("LoggedInAs")
					.addText(userName);
		}
	}

	/**
	 * 
	 * @return
	 */
	public String xml_out() {
		// return stripNonValidXMLCharacters(xmlDoc.asXML());

		OutputFormat outFormat = new OutputFormat();
		outFormat.setEncoding(Utilities.REQUEST_ENCODING);
		StringWriter writer = new StringWriter();
		XMLWriter out = new XMLWriter(writer, outFormat);
		try {
			out.write(xmlDoc);
		} catch (Exception e) {

		}
		String s = writer.toString();
		return s;
	}

	public String toString() {
		return xml_out();
	}

	/**
	 * Pads string representations of numbers with a specified character, up to
	 * fieldSize characters. This is used in bookmarkXML to display the top 10
	 * bookmarks in proper sorted order.
	 * 
	 * @param num
	 *            The number to pad.
	 * @param fieldSize
	 *            The size of the text field in which the number will be
	 *            printed.
	 * @param pad
	 *            The character with which to pad.
	 * @return A string of length fieldSize, consisting of num padded with the
	 *         appropriate number of occurrences of pad.
	 * @author schwerdf
	 */
	public static String paddedValueOf(int num, int fieldSize, char pad) {
		String rv = String.valueOf(num);
		while (rv.length() < fieldSize) {
			rv = pad + rv;
		}
		return rv;
	}

	public void constructBuddyRequests(ArrayList<Buddy> buddyRequests) {
		Element root = xmlDoc.getRootElement();

		if (root != null)
			root.detach();

		root = xmlDoc.addElement(XML);
		root.addElement("result").addText(SUCCESS);
		for (int i = 0; i < buddyRequests.size(); i++) {
			Element buddy = root.addElement("buddy");
			buddy.addElement("username").addText(buddyRequests.get(i).username);
		}
	}

	/**
	 * Creates a xml list using the objects in the array.
	 * 
	 * @param objects
	 *            An array of strings.
	 */
	@SuppressWarnings("unused")
	public void constructNodeActionList(ArrayList<String> objects) {
		Element root = xmlDoc.getRootElement();

		if (root != null)
			root.detach();

		root = xmlDoc.addElement(XML);

		Element resultE = root.addElement("Result").addText(SUCCESS);

		for (Iterator<String> it = objects.iterator(); it.hasNext();) {
			String x = it.next();

			Element icon = root.addElement("Object").addText(x);

		}

	}

	/**
	 * This method ensures that the output String has only valid XML unicode
	 * characters as specified by the XML 1.0 standard. For reference, please
	 * see <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
	 * standard</a>. This method will return an empty String if the input is
	 * null or empty.
	 * 
	 * @param in
	 *            The String whose non-valid characters we want to remove.
	 * @return The in String, stripped of non-valid characters.
	 */
	public static String stripNonValidXMLCharacters(String in) {
		StringBuffer out = new StringBuffer(); // Used to hold the output.
		char current; // Used to reference the current character.

		if (in == null || ("".equals(in)))
			return ""; // vacancy test.
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
			// here; it should not happen.
			if ((current == 0x9) || (current == 0xA) || (current == 0xD)
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}

}
