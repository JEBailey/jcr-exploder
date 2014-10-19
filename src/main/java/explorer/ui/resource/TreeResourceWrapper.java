package explorer.ui.resource;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceWrapper;

public class TreeResourceWrapper extends ResourceWrapper {

	public TreeResourceWrapper(Resource resource) {
		super(resource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Resource){
			return this.getPath().equals(((Resource)obj).getPath());
					}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getPath().hashCode();
	}

}
