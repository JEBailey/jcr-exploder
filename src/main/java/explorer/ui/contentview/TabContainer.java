package explorer.ui.contentview;

import javax.swing.JTabbedPane;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import explorer.ui.preview.PreviewPanel;

@Component(name="Sling Explorer UI - Tabbed Pane ",description="UI component that represent the Tab Area")
@Service(value=TabContainer.class)
public class TabContainer extends JTabbedPane {

	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;

	@Reference
	PreviewPanel panel;

	public TabContainer() {
		super(JTabbedPane.TOP);
		setFocusable(false);
	}
	
	@Activate
	private void activate(){
		addTab("Preview", null, panel, null);
		setSelectedComponent(panel);
	}

}
