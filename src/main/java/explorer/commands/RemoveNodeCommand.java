package explorer.commands;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import flack.commands.Command;
import flack.control.Dispatcher;
import flack.control.Event;

public class RemoveNodeCommand implements Command {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	@Override
	public void process(Event event) {
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
