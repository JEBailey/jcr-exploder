package explorer.ide.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class JcrTreeModel extends DefaultTreeModel {

	public JcrTreeModel(TreeNode root) {
		super(root);
	}

	@Override
	protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
		TreeNode[] retNodes;

		if (aNode == null) {
			return (depth == 0) ? null : new TreeNode[depth];
		} else {
			depth++;
			retNodes = ( aNode == root) ? new TreeNode[depth] : getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}

}
