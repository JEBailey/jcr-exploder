package explorer.commands;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import flack.commands.api.Command;
import flack.control.DispatcherDefaultImpl;
import flack.control.EventDefaultImpl;
import flack.control.api.Dispatcher;

@Component(name="Sling Explorer Command - Remove Resource",description="Comamnd to remove a resource")
@Service
@Property(name="type", value="removeNode")
public class RemoveNodeCommand implements Command {

	private Dispatcher dispatcher = DispatcherDefaultImpl.getInstance();
	
	@Override
	public void process(EventDefaultImpl event) {
		Node node = (Node)event.getData();
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

}
