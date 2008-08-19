/**
 * This package holds all classes involved in creating URL recommendations for users.
 */
package bookmarks.recommender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookmarks.BookmarkXML;
import bookmarks.ClientEntry;
import bookmarks.DatabaseWrapper;
import bookmarks.LinkNameUrlObject;
import bookmarks.Utilities;

/**
 * This servlet retrieves recommendations for a particular user
 * and returns them much like mostrecentlyadded does.
 * 
 * @author Joshua Fleck
 *
 */
public class RecommenderServlet extends HttpServlet{
	
	/**
	 * Ensure the base directory is set correctly
	 */
	static
	{
		String cBase = System.getProperty("catalina.base");
		
		if(cBase != null) {
			
			if(cBase.endsWith("/") || cBase.endsWith("\\")) {
				CATALINA_BASE = cBase;
			} else {
				CATALINA_BASE = cBase + "/";
			}
			
		} else {
			
			System.out.println("RECOMMENDER> Could not generate recommendations. JVM arg CATALINA_BASE not set");
			
		}
	}

	/**
	 * A unique identifier for this class
	 */
	private static final long serialVersionUID = -6667423127850451195L;
	
	/** */
	private static String CATALINA_BASE;
	
	/** The thread that updates the recommendations */
	private static RecommenderUpdateThread recommendationsUpdate = null;
	
	/** The amount of time to wait between updating the recommender in milliseconds. 604800000m ~ 1 week*/
	protected static final long UPDATE_TIMEOUT = 604800000;
	
	/** The name of the script that will export recommendations */
	protected static final String EXPORT_LINKS_COMMAND = "sh "+CATALINA_BASE+"webapps/ROOT/src/bookmarks/recommender/exportRecommendations.sh "+CATALINA_BASE;
	
	/** The name of the flat file holding the user-links data used to extrapolate recommendations */
	protected static final String USER_LINKS_LOCATION = CATALINA_BASE+"webapps/ROOT/src/bookmarks/recommender/exportRecommendations.out";

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Utilities.prepareRequest(request);
		
		boolean agent = Utilities.eliminateNull((String) request.getParameter("agent")).equals(Utilities.AGENT_EXT);

		DatabaseWrapper db = DatabaseWrapper.getDatabaseWrapper();

		ClientEntry user = Utilities.validateUser(request, db);

		BookmarkXML xml = new BookmarkXML();
		
		if(user == null) {//Ensure the user is valid and logged in
			
			if (agent) {
				Utilities.prepareResponseAgent(response);
				Utilities.reportInvalidUserAgent(xml);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.reportInvalidUserWeb(xml);
			}


		} else {//Retrieve recommendations and display them accordingly
			
			ArrayList<LinkNameUrlObject> bookmarks = null;
			
			try {
				
				bookmarks = db.retrieveRecommendationsForClient(user);
				
			} catch (SQLException e) {
				Utilities.wrapChipmarkException(e);
			}

			Utilities.cleanseUrlList(bookmarks, user, !agent);
			
			xml.constructRecommendations(user.getClientName(), bookmarks);
			
			if(agent) {
				Utilities.prepareResponseAgent(response);
			} else {
				Utilities.prepareResponseWeb(response);
				Utilities.styleXML(xml, "recommender", user);
			}
			
		}
		
		Utilities.returnResponse(response, xml);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		recommendationsUpdate.interrupt();
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		recommendationsUpdate = new RecommenderUpdateThread();
		recommendationsUpdate.setDaemon(true);
		recommendationsUpdate.start();
		super.init();
	}
	
	/**
	 * This class will export the link table, and update the recommendations.
	 * @author Joshua Fleck
	 *
	 */
	private class RecommenderUpdateThread extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
	/*	public void run() {

			while(true) { //deaktiviert
				
				try {//Export the link table
					
					Process p = Runtime.getRuntime().exec(EXPORT_LINKS_COMMAND);
					
					// any error message?
		            StreamGobbler errorGobbler = new 
		                StreamGobbler(p.getErrorStream(), "ERROR");            
		            
		            // any output?
		            StreamGobbler outputGobbler = new 
		                StreamGobbler(p.getInputStream(), "OUTPUT");
		                
		            // kick them off
		            errorGobbler.start();
		            outputGobbler.start();
		                                    
		            // any error???
		            int exitVal = p.waitFor();
					
		            System.out.println("RECOMMENDER> exit value of recommender export script: " + exitVal);  
		            
				} catch (Exception e) {
					Utilities.ignoreChipmarkException(e);
				}
				
				USearch[] uSearch = null;
				
				try {//Read the exported link table
					uSearch = Parser.readFile(RecommenderServlet.USER_LINKS_LOCATION);
				} catch (IOException e) {
					System.out.println("RECOMMENDER> Could not run recommender. Ensure the JVM argument 'CATALINA_BASE' is set to your chipmark directory.");
					Utilities.ignoreChipmarkException(e);
					uSearch = new USearch[]{};
				}
				
				//Run the recommendation generator
				RecommenderSplitter.runRecommender(uSearch);
				
				try {//Wait a while then do it again
					Thread.sleep(RecommenderServlet.UPDATE_TIMEOUT);
				} catch (InterruptedException e) {
					return;
				}
				
			}*/

		}

		/**
		 * 
		 
		public RecommenderUpdateThread() {
			super("RecommenderUpdateThread");			
		}	
		*/
		/**
		 * This class will print any errors output by the script that exports the links for recommendations
		 * @author Joshua Fleck
		 *
		 */
		private class StreamGobbler extends Thread
		{
		    InputStream is;
		    String type;
		    
		    StreamGobbler(InputStream is, String type)
		    {
		        this.is = is;
		        this.type = type;
		    }
		    
		    public void run()
		    {
		        try
		        {
		            InputStreamReader isr = new InputStreamReader(is);
		            BufferedReader br = new BufferedReader(isr);
		            String line=null;
		            while ( (line = br.readLine()) != null)
		                System.out.println(type + ">" + line);    
		            } catch (IOException ioe)
		              {
		                ioe.printStackTrace();  
		              }
		    }
		}
		
	}
