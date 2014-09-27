package explorer.commands;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.event.EventHandler;

@Component(name="Sling Explorer Command - Remove Resource",description="Comamnd to remove a resource")
@Service
@Property(name="type", value="removeNode")
public class RemoveNodeCommand implements EventHandler {

	
	public void process(Object event) {
		Node node = null;//(Node)event.getData();
		try {
			Node parentNode = node.getParent();
			node.remove();
			parentNode.getSession().save();
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handleEvent(org.osgi.service.event.Event arg0) {
		// TODO Auto-generated method stub
		
	}

}
