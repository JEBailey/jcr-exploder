package explorer.ui.preview;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

@SuppressWarnings("serial")
@Component
@Service(value = PreviewPanel.class)
public class PreviewPanel extends JPanel {

	public PreviewPanel() {
		super(new BorderLayout());
	}
	
	JScrollPane propertiesPane;
	
	JScrollPane contentPane;
	
	@Reference
	JTable propertyTable;

	@Activate
	public void activate() {
		JToolBar bar = new JToolBar();
		bar.add(Box.createHorizontalGlue());
		bar.add(new JButton("Properties"));
		bar.add(new JButton("File"));
		bar.setFloatable(false);

		propertiesPane = new JScrollPane(propertyTable);
		
		add("North", bar);
		add("Center", propertiesPane);
	}

	public PreviewPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public PreviewPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public PreviewPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

}
