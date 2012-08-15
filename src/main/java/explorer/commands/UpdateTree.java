package explorer.commands;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

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
		tree.getSelectionPath();
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		model.nodeChanged(treeNode);
		model.reload(treeNode);
		tree.treeDidChange();
		tree.invalidate();
		tree.expandPath(null);
		System.out.print("updating model");
	}

}
