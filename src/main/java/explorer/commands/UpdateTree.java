package explorer.commands;

import javax.swing.JTree;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

import explorer.ide.tree.CoreTreeModel;
import flack.commands.api.Command;
import flack.control.EventDefaultImpl;

@Component(name="Sling Explorer Command - Update Tree",description="Updates Resource Tree")
@Service
@Property(name="type", value="updateTree")
public class UpdateTree implements Command {
	
	@Reference
	private JTree tree;

	@Override
	public void process(EventDefaultImpl event) {
		Resource treeNode = (Resource)event.getData();
		CoreTreeModel model = (CoreTreeModel)tree.getModel();
		model.updateStructure(treeNode);

		//model.nodeStructureChanged(treeNode);
		//tree.expandRow(tree.getRowForPath(new TreePath(treeNode.getPath())));
	}

}
