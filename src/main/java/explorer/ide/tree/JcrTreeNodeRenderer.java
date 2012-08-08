package explorer.ide.tree;

import explorer.ide.IconCache;
import java.awt.Component;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package explorer.ide.tree:
//            JcrTreeNode

public class JcrTreeNodeRenderer extends DefaultTreeCellRenderer {

	public JcrTreeNodeRenderer() {
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		explorer.ide.IconCache.Type type = explorer.ide.IconCache.Type.file;
		if (value != null && (value instanceof JcrTreeNode)) {
			Node node = ((JcrTreeNode) value).getNode();
			try {
				if (node.isNodeType("nt:folder")) {
					type = expanded ? explorer.ide.IconCache.Type.folder_open: explorer.ide.IconCache.Type.folder;
				} else if (node.isNodeType("nt:file")) {
					Property prop = node
							.getProperty("jcr:content/jcr:mimeType");
					if (prop != null) {
						String mime[] = prop.getString().split("/");
						explorer.ide.IconCache.Type base = getType(mime[0]);
						if (mime.length > 1) {
							explorer.ide.IconCache.Type extended = getType(mime[1]);
							if (extended != null)
								base = extended;
						}
						if (base != null)
							type = base;
					}
				} else if (node.isNodeType("nt:unstructured"))
					type = explorer.ide.IconCache.Type.node_select_child;
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		setIcon(IconCache.getIcon(type));
		return this;
	}

	private explorer.ide.IconCache.Type getType(String value) {
		try {
			return explorer.ide.IconCache.Type.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	private static final Logger log = LoggerFactory.getLogger(JcrTreeNodeRenderer.class);

}
