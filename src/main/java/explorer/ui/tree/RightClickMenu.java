package explorer.ui.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

@Component(name = "Sling Explorer UI - Tree Menu")
@Service(value = RightClickMenu.class)
@SuppressWarnings("serial")
public class RightClickMenu extends JPopupMenu {

	@Reference
	JTree tree;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = AbstractAction.class, bind = "bindAction", unbind = "unbindAction")
	List<AbstractAction> abstractActions;

	Resource resource;

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	JMenuItem mntmCopy = new JMenuItem("Copy");
	JMenuItem mntmPaste = new JMenuItem("Paste");

	@Activate
	public void activate() {
		add(mntmCopy);
		add(mntmPaste);
		for (AbstractAction action : abstractActions) {
			add(new JMenuItem(action));
		}

	}

	public void bindAction(AbstractAction filter) {
		if (abstractActions == null) {
			abstractActions = new ArrayList<AbstractAction>();
		}
		abstractActions.add(filter);
	}

	public void unbindAction(AbstractAction filter) {
		abstractActions.remove(filter);
	}
}
