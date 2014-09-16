package explorer.ide.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.commons.mime.MimeTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.ide.ui.IconCache;
import explorer.ide.ui.IconCache.Type;
import explorer.node.NodeTypeUtil;

@org.apache.felix.scr.annotations.Component(name="Sling Explorer UI - Tree Node Renderer",description="Displays the correct image for the node in the tree")
@Service(value=DefaultTreeCellRenderer.class)
@Property(name="type", value="updatePane")
public class JcrTreeNodeRenderer extends DefaultTreeCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Reference
	MimeTypeService mimes;
	
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
				if (NodeTypeUtil.isType(resource,"nt:folder")){
					type = expanded ? Type.folder_open: Type.folder;
				} else if (NodeTypeUtil.isType(resource,"nt:file")) {
					ResourceMetadata metaData = resource.getResourceMetadata();
					String prop = metaData.getContentType();
					if (prop == null){
						prop = mimes.getMimeType(resource.getName());
					}
					if (prop != null) {
						String mime[] = prop.split("/");
						Type base = getType(mime[0]);
						if (mime.length > 1) {
							Type extended = getType(mime[1]);
							if (extended != null){
								base = extended;
							}
						}
						if (base != null) {
							type = base;
						}
					}
				} else {
					type = Type.node_select_child;
				}
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
