package explorer.ui;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

@Component(name="Sling Explorer - Buffer",description="Core Service that mainatins the Copy/Paste Item")
@Service(value=ResourceClipboardBuffer.class)
public class ResourceClipboardBuffer {
	
	private Resource resource;
	
	private boolean move;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public void setResourceToMove(Resource resource) {
		this.resource = resource;
		this.move = true;
	}
	
	public boolean hasResource(){
		return (resource != null);
	}
	
	public boolean isMove(){
		return move;
	}
	
	public void clear(){
		resource = null;
		move = false;
	}
	


}
