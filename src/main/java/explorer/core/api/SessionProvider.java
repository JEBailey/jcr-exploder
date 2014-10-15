package explorer.core.api;

import javax.jcr.Session;

public interface SessionProvider extends Session {

	public Session getSession(String key);
	
	public void createSession(String name, char[] credentials, String key);
	
	public void save();
	
}
