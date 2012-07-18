package explorer.ide.tree;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.JTree;

@SuppressWarnings("serial")
public class JcrJTree extends JTree {

	@Override
	public String convertValueToText(Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if(value != null) {
			if (value instanceof Node){
				Node node = (Node)value;
				try {
					return node.getName();
				} catch (RepositoryException e) {
					//
				}
			}
        }
		return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
	}

}
