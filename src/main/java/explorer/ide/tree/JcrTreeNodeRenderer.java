package explorer.ide.tree;

import java.awt.Component;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.ide.ui.IconCache;
import explorer.ide.ui.IconCache.Type;

// Referenced classes of package explorer.ide.tree:
//            JcrTreeNode

@SuppressWarnings("serial")
public class JcrTreeNodeRenderer extends DefaultTreeCellRenderer {

	public JcrTreeNodeRenderer() {
	}
	
	private static final Logger log = LoggerFactory.getLogger(JcrTreeNodeRenderer.class);

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		Type type = Type.file;
		if (value != null && (value instanceof Resource)) {
			Resource resource = (Resource) value;
			try {
				if (resource.isResourceType("nt:folder")) {
					type = expanded ? Type.folder_open: Type.folder;
				} else if (resource.isResourceType("nt:file")) {
					ResourceMetadata metaData = resource.getResourceMetadata();
					String prop = metaData.getContentType();
					if (prop != null) {
						String mime[] = prop.split("/");
						Type base = getType(mime[0]);
						if (mime.length > 1) {
							Type extended = getType(mime[1]);
							if (extended != null)
								base = extended;
						}
						if (base != null)
							type = base;
					}
				} else if (resource.isResourceType("nt:unstructured"))
					type = Type.node_select_child;
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		setIcon(IconCache.getIcon(type));
		return this;
	}

	private Type getType(String value) {
		try {
			return Type.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}


}
