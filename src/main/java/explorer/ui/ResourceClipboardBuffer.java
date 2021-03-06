/*
Copyright 2016 JE Bailey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package explorer.ui;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

@Component(name="Sling Explorer - Buffer")
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
