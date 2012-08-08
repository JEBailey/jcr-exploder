package explorer.ide;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.jcr.Session;
import javax.swing.Action;

import org.apache.sling.api.resource.ResourceResolver;

public class Model {
	
	
	private static ResourceResolver resourceResolver;
	
	
	private static Session session;
	
	public static ResourceResolver getResourceResolver() {
		return resourceResolver;
	}

	public static void setResourceResolver(ResourceResolver resourceResolver) {
		Model.resourceResolver = resourceResolver;
		Model.session = resourceResolver.adaptTo(Session.class);
	}

	public static Session getSession() {
		checkLive();
		return session;
	}

	public static void setSession(Session session) {
		Model.session = session;
	}
	
	private static void checkLive() {
		if (!session.isLive()) {
			session.logout();
			session = resourceResolver.adaptTo(Session.class);
		}
	}

	public static Action importFiles = new Action() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void setEnabled(boolean b) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void putValue(String key, Object value) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public Object getValue(String key) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub
			
		}
	};
	

}
