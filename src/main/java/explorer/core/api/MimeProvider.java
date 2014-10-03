package explorer.core.api;

import java.awt.Component;

import org.apache.sling.api.resource.Resource;

public interface MimeProvider {
	
	Component createComponent(Resource resource, String syntax);

}
