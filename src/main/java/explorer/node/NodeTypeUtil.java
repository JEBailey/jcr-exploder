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
package explorer.node;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;


public class NodeTypeUtil {
	
	public static boolean  isType(Resource resource, String type){
		
		if (resource.isResourceType(type)){
			return true;
		}
		ValueMap map = resource.adaptTo(ValueMap.class);
		if (map == null){
			return false;
		}
		boolean reply = false;
		String primaryNode = map.get("jcr:primaryType", String.class);
		if (primaryNode == null){
			return false;
		}
		try {
			Session session = resource.getResourceResolver().adaptTo(Session.class);
			reply = session.getWorkspace().getNodeTypeManager().getNodeType(primaryNode).isNodeType(type);
		} catch (RepositoryException e) {
			return false;
		}
		return reply;
	}
	
}
