/**
 * 
 */
package bookmarks.recommender;

import java.io.IOException;

/**
 * Creates a pool of recommender threads.
 * NOTE: You must have at least 2 users w/chipmarks in order to generate recommendations.
 * @author Joshua Fleck
 *
 */
public class RecommenderSplitter extends Thread {
	
	/** The number of threads to create */
	public static int NUMBER_THREADS = 4;
	
	/** The users to generate recommendations for */
	public USearch[] uSearch = null;
	
	/**
	 * 
	 * @param search The users to generate recommendations for
	 */
	private RecommenderSplitter(USearch[] search) {
		super("RecommenderSplitterThread");
		this.uSearch = search;
	}

	/**
	 * Creates the thread pools and runs the recommender
	 * @return true, on successful exit
	 */
	public static boolean runRecommender(USearch[] search) {
		
		RecommenderSplitter rs = new RecommenderSplitter(search);
		
		if(rs.uSearch.length == 0) {
			
			System.out.println("RECOMMENDER> no users to generate recommendations for, exiting...");
			
			return false;
			
		}
		
		if(rs.uSearch.length < NUMBER_THREADS) {
			NUMBER_THREADS = rs.uSearch.length;
		}
		
		rs.start();
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		double chunkSize = Math.floor(this.uSearch.length / NUMBER_THREADS);
		
		Recommender r = null;
		for(int i=0; i<NUMBER_THREADS; i++) {
			
			if(i != NUMBER_THREADS-1) {
				
				r = new Recommender(this.uSearch,(new Double(i*chunkSize)).intValue(),(new Double(i*chunkSize+chunkSize)).intValue(),"RecommenderThread"+i);
				
			} else {//In case the number of threads does not evenly divide the array length, grab the last extra users.
				
				r = new Recommender(this.uSearch,(new Double(i*chunkSize)).intValue(),this.uSearch.length,"RecommenderThread"+i);
			
			}			
			
			r.start();

		}
		
	}

	/**
	 * @param args arg0 should be name of input file
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		if(args.length != 1) {
			System.out.println("Missing input file argument!");
			return;
		}
				
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

		RecommenderSplitter.runRecommender(uSearch);
	}

}
