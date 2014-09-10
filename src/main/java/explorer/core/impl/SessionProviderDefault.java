package explorer.core.impl;

import javax.jcr.Session;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import explorer.core.api.SessionProvider;
import org.apache.sling.jcr.api.SlingRepository;

@Component
@Service
public class SessionProviderDefault implements SessionProvider {

	@Reference
    protected SlingRepository repository;

    protected Session session;

    @Activate
    protected void activate() throws Exception {
        session = repository.loginAdministrative(null);
    }

    @Deactivate
    protected void deactivate() throws Exception {
        session.logout();
        session = null;
    }
	@Override
	public Session getSession(String key) {
		return session;
	}

	@Override
	public void createSession(String name, char[] credentials, String key) {
		// TODO Auto-generated method stub
		
	}

}
