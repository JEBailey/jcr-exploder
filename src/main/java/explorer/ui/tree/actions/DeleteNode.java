package explorer.ui.tree.actions;

import static org.osgi.service.event.EventConstants.EVENT_TOPIC;

import java.awt.event.ActionEvent;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.core.api.ResourceTreeModel;
import explorer.core.api.SessionProvider;
import explorer.ui.EventTypes;
import explorer.ui.contentview.TabContainer;

@SuppressWarnings("serial")
@Component(name = "Sling Explorer Menu Action - Delete Node", description = "Delete a Node")
@Service(value = { AbstractAction.class, EventHandler.class })
@Properties(value = { @Property(name = EVENT_TOPIC, value = EventTypes.NEW_SELECTION),
		@Property(name = "menuType", value = "TREEMENU") })
public class DeleteNode extends AbstractAction implements EventHandler {

	private Resource selectedResource;

	@Reference
	private TabContainer editorTab;
	
	@Reference
	private ResourceTreeModel treeModel;
	
	@Reference
	SessionProvider sessionProvider;

	public DeleteNode() {
		super("Delete Node");
	}
	private static final Logger log = LoggerFactory.getLogger(DeleteNode.class);
	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			int selection = JOptionPane.showConfirmDialog(editorTab, "Delete Node?", "Delete Node", JOptionPane.YES_NO_OPTION);
			switch (selection) {
			case JOptionPane.OK_OPTION:
				Node parent = selectedResource.adaptTo(Node.class);
				parent.remove();
				sessionProvider.save();
				treeModel.fireStructureChanged(selectedResource.getParent());
				break;
			}
		} catch (RepositoryException e) {
			log.error(e.getMessage());
		}


	}

	@Override
	public void handleEvent(Event event) {
		selectedResource = (Resource) event.getProperty("data");
		setEnabled(selectedResource.adaptTo(Node.class) != null);
	}
}
