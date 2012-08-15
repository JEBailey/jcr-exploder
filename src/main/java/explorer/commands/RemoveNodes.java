package explorer.commands;

import javax.jcr.Node;

import explorer.events.NodeModified;
import explorer.ide.tree.JcrTreeNode;
import flack.commands.Command;
import flack.control.Dispatcher;
import flack.control.Event;

public class RemoveNodes implements Command {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	@Override
	public void process(Event event) {
		JcrTreeNode treeNode = (JcrTreeNode)event.getData();
		Node node = treeNode.getNode();
		try {
			Node parentNode = node.getParent();
			node.remove();
			parentNode.getSession().save();
			dispatcher.dispatchEvent(new NodeModified(event.getSource(), treeNode));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
