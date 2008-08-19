package de.fzk.iwr.ads;

import java.io.Serializable;


/**
 * @author  florianroth
 */
@SuppressWarnings("serial")
public class Domain implements Serializable {

	private String name;
	private String host;
	private String site;
	private int port;
	private String baseDn;
	
	private DomainController dc = new DomainController();
	
	public String getBaseDn() {
		return baseDn;
	}
	public void setBaseDn(String baseDn) {
		this.baseDn = baseDn;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
	public String getDC(){
		return dc.getDomainController(host, site);
	}
}
