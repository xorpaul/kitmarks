package de.fzk.iwr.ads;

import java.io.Serializable;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

@SuppressWarnings("serial")
public class DomainController implements Serializable {

	public DomainController(){
	}
	
	public String getDomainController(String domain, String site) {
		DirContext ictx = null;

		try {
			ictx = new InitialDirContext();
	
			Attributes attrs3 = ictx.getAttributes(
					"dns://dns.fzk.de/_ldap._tcp."+ site +"._sites.dc._msdcs." + domain,
					new String[] {"SRV"});
			Attribute srv = attrs3.get("SRV");
	
			if(srv.size() > 0){
				String firstServer = srv.get(0).toString();
				String[] values = firstServer.split("\\s");
				
				return values[3];
			}
		}catch (NamingException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(ictx != null) ictx.close();
			}catch(NamingException ex){}
		}

		return null;
	}

}
