package explorer.core.api;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public interface ResourceTreeModel extends TreeModel {
	
	public void fireStructureChanged(TreePath path);

}
