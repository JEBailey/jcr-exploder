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
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.commands.UpdateEditorPane;
import explorer.commands.UpdateTree;
import explorer.events.Delete;
import explorer.events.FindFiles;
import explorer.events.NodeModified;
import explorer.events.NodeSelected;
import explorer.ide.table.JcrTableModelImpl;
import flack.commands.api.Command;
import flack.commands.api.MultipleCommand;
import flack.control.EventControllerDefaultImpl;
import flack.control.api.EventController;

@Component(description="Swing based explorer",label="Explorer IDE", name="ExplorerIDE")
public class ExplorerIDE implements Runnable {
	
	private JFrame frmJcrExploder;
	
	private RSyntaxTextArea editorTextArea;
	
	ComponentContext componentContext;
	
	@SuppressWarnings("unused")
	private EventController controller;

	private static final Logger log = LoggerFactory
			.getLogger(ExplorerIDE.class);

	@Reference
	JcrTableModelImpl tableModel;
	
	@Reference
	JTree jTree;
	
	@Reference(target="(type=fileImport)",policy=ReferencePolicy.STATIC)
	Command fileImport;
	
	@Reference(target="(type=updatePane)",policy=ReferencePolicy.STATIC)
	Command updatePane;
	
	@Reference(target="(type=removeNode)",policy=ReferencePolicy.STATIC)
	Command removeNode;
	
	@Reference(target="(type=updateTable)",policy=ReferencePolicy.STATIC)
	Command updateTable;
	
	@Reference(target="(type=updateTree)",policy=ReferencePolicy.STATIC)
	Command updateTree;
	
	private void bundleInitialize() {
		((UpdateEditorPane)updatePane).setEditorPane(editorTextArea);
		controller = new EventControllerDefaultImpl(){{
			addCommand(NodeSelected.class, new MultipleCommand(){{
				add(updateTable);
				add(updatePane);
			}});
			addCommand(FindFiles.class, fileImport);
			addCommand(NodeModified.class, updateTree);
			addCommand(Delete.class,removeNode);
		}};
	}
	
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
		
		JTabbedPane editorTab = new JTabbedPane(JTabbedPane.TOP);
		splitPane_1.setLeftComponent(editorTab);
		
		
		editorTextArea = new RSyntaxTextArea(RSyntaxTextArea.INSERT_MODE);
		editorTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		editorTextArea.setAntiAliasingEnabled(true);
		editorTextArea.setEditable(true);
		
		RTextScrollPane editorScrollPane = new RTextScrollPane(editorTextArea);
		editorTab.addTab("New tab", null, editorScrollPane, null);
		
		editorTextArea.setText("public static void main(String[] args) {\n}");
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
	public void activate(ComponentContext context) throws Exception{
		this.componentContext = context;
		try {
			javax.swing.SwingUtilities.invokeAndWait(this);
		} catch (Exception ex) {
			log.error("non event dispatch thread errored");
			throw ex;
		}
	}
	
	@Deactivate
	public void deactivate() throws Exception{
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
		bundleInitialize();
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
