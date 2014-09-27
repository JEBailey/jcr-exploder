package explorer.ide;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.text.JTextComponent;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.ide.table.JcrTableModelImpl;
import flack.control.api.EventController;

@Component(description = "Swing based Sling explorer", label = "Sling Explorer IDE", name = "ExplorerIDE")
public class ExplorerIDE implements Runnable {

	private JFrame frmJcrExploder;

	private ComponentContext componentContext;

	@Reference
	private EventController controller;

	private static final Logger log = LoggerFactory.getLogger(ExplorerIDE.class);

	@Reference
	private JcrTableModelImpl tableModel;

	@Reference
	private JTree jTree;
	
	@Reference
	private TabEditor editorTab;

	private JTable table;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJcrExploder = new JFrame();
		frmJcrExploder.setTitle("Sling Explorer GUI");
		frmJcrExploder.setBounds(100, 100, 800, 600);
		frmJcrExploder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(3);
		frmJcrExploder.getContentPane().add(splitPane, BorderLayout.CENTER);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setDividerSize(4);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);

		JScrollPane propertiesScrollPane = new JScrollPane();
		splitPane_1.setRightComponent(propertiesScrollPane);

		table = new JTable();
		table.setFocusable(false);
		table.setIntercellSpacing(new Dimension(0, 1));
		table.setBounds(new Rectangle(1, 1, 1, 1));
		table.setShowVerticalLines(false);
		table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		propertiesScrollPane.setViewportView(table);

		splitPane_1.setLeftComponent(editorTab);
		splitPane_1.setDividerLocation(400);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		scrollPane.setViewportView(jTree);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		scrollPane.setColumnHeaderView(panel);

		JButton button = new JButton("+");
		button.setToolTipText("expand all");
		button.setContentAreaFilled(false);
		button.setBorder(new CompoundBorder());
		panel.add(button);
		splitPane.setDividerLocation(120);

		JMenuBar menuBar = new JMenuBar();
		frmJcrExploder.setJMenuBar(menuBar);
	}

	@Activate
	public void activate(ComponentContext context) throws Exception {
		this.componentContext = context;
		try {
			javax.swing.SwingUtilities.invokeAndWait(this);
		} catch (Exception ex) {
			log.error("non event dispatch thread errored");
			throw ex;
		}
	}

	@Deactivate
	public void deactivate() throws Exception {
		UIManager.put("RTextAreaUI.actionMap", null);
		UIManager.put("RSyntaxTextAreaUI.actionMap", null);
		JTextComponent.removeKeymap("RTextAreaKeymap");

		frmJcrExploder.setVisible(false);
		frmJcrExploder.dispose();
		frmJcrExploder = null;
	}

	@Override
	public void run() {
		initialize();
		frmJcrExploder.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmJcrExploder.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				componentContext.disableComponent("ExplorerIDE");
			}
		});
		frmJcrExploder.setVisible(true);
		log.info("application is running");
	}

}
