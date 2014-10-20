package explorer.ui.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;

@Component(name = "Sling Explorer UI - Tree Menu")
@Service(value = RightClickMenu.class)
@SuppressWarnings("serial")
public class RightClickMenu extends JPopupMenu {

	@Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = AbstractAction.class, bind = "bindAction", unbind = "unbindAction")
	List<AbstractAction> abstractActions;

	@Activate
	public void activate() {
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
