package explorer.commands;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import explorer.ide.tree.JcrTreeNode;
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
		JcrTreeNode treeNode = (JcrTreeNode)event.getData();
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		model.nodeStructureChanged(treeNode.getParent());
		tree.expandRow(tree.getRowForPath(new TreePath(treeNode.getPath())));
	}

}
