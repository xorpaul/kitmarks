package bookmarks.recommender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Parser {
	
	public static ArrayList<Integer> links = new ArrayList<Integer>();
	public static int count = 0;

	public static USearch[] readFile(String file) throws IOException{
		
		/*Stores the current list of users and their
		 * corrisponding location in the arraylist lists
		 */
		Hashtable<Integer, ArrayList<Integer>> users = new Hashtable<Integer, ArrayList<Integer>>();
		
		//Read in the file and store it in a hashtable we will do clean up later
		BufferedReader in = new BufferedReader(new FileReader(file));
		in.readLine();//We don't want the first line
		
		String aLine;
		while ((aLine = in.readLine()) != null) {
			
			StringTokenizer tokens = new StringTokenizer(aLine, "\t");
		
			assert( tokens.countTokens() == 2);
			String strUserID = tokens.nextToken();
			String strLinkID = tokens.nextToken();
			
			//strUserID = strUserID.replaceAll("\"", "");
			//strLinkID = strLinkID.replaceAll("\"", "");
			
			Integer userID = new Integer(strUserID);
			Integer linkID = new Integer(strLinkID);
					
			
			if(users.containsKey(userID)){
				users.get(userID).add(linkID);
			}else{
				ArrayList<Integer> links = new ArrayList<Integer>();
				links.add(linkID);
				users.put(userID, links);
			}
		}
		
		//Change the hashtable in to an Array of USearch objects
		Iterator<Integer> userIDIter = users.keySet().iterator();
		USearch[] USearchArray = new USearch[users.keySet().size()];
		int k = 0;
		while(userIDIter.hasNext()){
			Integer uID = userIDIter.next();
			ArrayList<Integer> links = users.get(uID);
			int[] linkArray = new int[links.size()];
			
			for(int i = 0; i < links.size(); i++){ 
				linkArray[i] = links.get(i);
			}
			
			USearchArray[k] = new USearch(uID.intValue(),linkArray);
			k++;
		}
				
		return USearchArray;
		
	}
	
	public static void main(String[] args) throws IOException {
		
		USearch[] usearch = Parser.readFile(args[0]);
		
		System.out.println(usearch.toString());
		
	}
	
}
