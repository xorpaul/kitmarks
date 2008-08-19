package de.fzk.iwr.ads;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

public class ADSConnection {
	
	public ADSConnection(){
	}

	public String login(Domain domain, String user, String password){
		if(domain == null){
			return null;
		}
		
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	    env.put(Context.PROVIDER_URL, "ldap://" + domain.getDC() + ":" + domain.getPort() + "/" + domain.getBaseDn());
	    env.put(Context.SECURITY_AUTHENTICATION, "simple");
	    env.put(Context.SECURITY_PRINCIPAL, domain.getName() + "\\" + user);
	    env.put(Context.SECURITY_CREDENTIALS, password);
	    
	    InitialLdapContext ctx = null;
	    try{
	    	ctx = new InitialLdapContext(env, null);
	    	SearchControls sc;
	    	NamingEnumeration<SearchResult> results;
	    	
	    	sc = new SearchControls(SearchControls.SUBTREE_SCOPE, 0, 0, new String[]{"name", "mail", "telephoneNumber"}, true, false);
			results = ctx.search("", "(&(objectClass=user)(sAMAccountName=" + user + "))", sc);
			
			if(results.hasMore()){
				SearchResult r = results.next();
				Attributes atts = r.getAttributes();
				
				return atts.get("mail").get().toString();
				
			}
	    	
	    }
	    catch(AuthenticationException ex){}
	    catch(NamingException ex){}
	    finally{
	    	if(ctx != null){
	    		try{
	    			ctx.close();
	    		}catch(NamingException ex){}
	    	}
	    }
		
		return null;
	}
}
