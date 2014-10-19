package explorer.core.api;

import javax.jcr.Session;

public interface SessionProvider extends Session {
	
	public void authenticate();

}
