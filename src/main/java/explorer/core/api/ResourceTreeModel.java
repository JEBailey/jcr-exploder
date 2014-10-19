package explorer.core.api;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.sling.api.resource.Resource;

public interface ResourceTreeModel extends TreeModel {
	
	public void fireStructureChanged(TreePath path);
	
	public void fireStructureChanged(Resource resource);

}
