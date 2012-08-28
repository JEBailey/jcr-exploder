package explorer.commands;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.JTree;

import explorer.ide.tree.CoreTreeModel;
import flack.commands.Command;
import flack.control.Event;

public class UpdateTree implements Command {
	
	private JTree tree;

	public UpdateTree(JTree tree) {
		super();
		this.tree = tree;
	}

	@Override
	public void process(Event event) {
		Node treeNode = (Node)event.getData();
		CoreTreeModel model = (CoreTreeModel)tree.getModel();
		model.updateStructure(treeNode);

		//model.nodeStructureChanged(treeNode);
		//tree.expandRow(tree.getRowForPath(new TreePath(treeNode.getPath())));
	}

}
