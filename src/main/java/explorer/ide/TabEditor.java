package explorer.ide;

import javax.swing.JTabbedPane;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component(name="Sling Explorer UI - Tabbed Pane ",description="Updates the Editor Pane with the correct view")
@Service(value=TabEditor.class)
public class TabEditor extends JTabbedPane {

	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;

	public TabEditor() {
		super(JTabbedPane.TOP);
	}

}
