package explorer.ui.contentview;

import javax.swing.JTabbedPane;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component(name="Sling Explorer UI - Tabbed Pane ",description="UI component that represent the Tab Area")
@Service(value=TabContainer.class)
public class TabContainer extends JTabbedPane {

	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;

	public TabContainer() {
		super(JTabbedPane.TOP);
		setFocusable(false);
	}

}
