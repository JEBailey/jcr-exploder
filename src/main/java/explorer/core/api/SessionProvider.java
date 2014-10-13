package explorer.core.api;

import javax.jcr.Session;

public interface SessionProvider {

	public Session getSession(String key);
	
	public void createSession(String name, char[] credentials, String key);
	
	public void save();
	
}
