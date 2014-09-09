package explorer.commands;

import javax.swing.JTree;

import org.apache.sling.api.resource.Resource;

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
		Resource treeNode = (Resource)event.getData();
		CoreTreeModel model = (CoreTreeModel)tree.getModel();
		model.updateStructure(treeNode);

		//model.nodeStructureChanged(treeNode);
		//tree.expandRow(tree.getRowForPath(new TreePath(treeNode.getPath())));
	}

}
