package explorer.node;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;


public class NodeTypeUtil {
	
	public static boolean  isType(Resource resource, String type){
		
		Session session = resource.getResourceResolver().adaptTo(Session.class);
		
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
			reply = session.getWorkspace().getNodeTypeManager().getNodeType(primaryNode).isNodeType(type);
		} catch (RepositoryException e) {
			return false;
		}
		return reply;
	}
	
}
