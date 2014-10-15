package explorer.ui.tree.actions;

import static org.osgi.service.event.EventConstants.EVENT_TOPIC;

import java.awt.event.ActionEvent;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.NodeTypeManager;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.mime.MimeTypeService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import explorer.core.api.SessionProvider;
import explorer.ui.EventTypes;
import explorer.ui.contentview.TabContainer;

@SuppressWarnings("serial")
@Component(name = "Sling Explorer Menu Action - Create Node", description = "Adds a New Node")
@Service(value = { AbstractAction.class, EventHandler.class })
@Properties(value = { @Property(name = EVENT_TOPIC, value = EventTypes.NEW_SELECTION),
		@Property(name = "menuType", value = "TREEMENU") })
public class CreateNode extends AbstractAction implements EventHandler {

	private Resource selectedResource;

	@Reference
	private TabContainer editorTab;

	public CreateNode() {
		super("Add Node");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JComboBox<String> comboBox = new JComboBox<String>();
		JTextField textField = new JTextField();

		NodeTypeManager ntm = null;
		try {
			ntm = selectedResource.getResourceResolver().adaptTo(Session.class).getWorkspace().getNodeTypeManager();

			NodeTypeIterator iterator = ntm.getAllNodeTypes();
			while (iterator.hasNext()) {
				NodeType type = iterator.nextNodeType();
				comboBox.addItem(type.getName());
			}

			Object[] fields = { "Name", textField, "Select Type", comboBox };

			int selection = JOptionPane.showConfirmDialog(editorTab, fields, "Add Node", JOptionPane.OK_CANCEL_OPTION);

			switch (selection) {
			case JOptionPane.OK_OPTION:
				Node parent = selectedResource.adaptTo(Node.class);
				NodeType nt = parent.getPrimaryNodeType();
				if (nt.canAddChildNode(textField.getText(), comboBox.getSelectedItem().toString())){
					parent.addNode(textField.getText(), comboBox.getSelectedItem().toString());
					parent.save();
				} else {
					JOptionPane.showConfirmDialog(editorTab, "Unable To Add Node");
				}
				break;
			case JOptionPane.CANCEL_OPTION:
				System.out.println("CANCEL");
				break;
			default:
				System.out.println("Others");
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@Override
	public void handleEvent(Event event) {
		selectedResource = (Resource) event.getProperty("data");
		setEnabled(selectedResource.adaptTo(Node.class) != null);
	}
}
