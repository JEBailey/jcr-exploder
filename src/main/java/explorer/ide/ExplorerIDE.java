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
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.commands.RemoveNodeCommand;
import explorer.commands.UpdateEditorPane;
import explorer.commands.UpdateTableModel;
import explorer.commands.UpdateTree;
import explorer.events.Delete;
import explorer.events.FindFiles;
import explorer.events.NodeModified;
import explorer.events.NodeSelected;
import explorer.ide.table.JcrTableModelImpl;
import explorer.ide.tree.JcrJTree;
import flack.commands.api.Command;
import flack.commands.api.MultipleCommand;
import flack.control.EventControllerDefaultImpl;
import flack.control.api.EventController;

@Component(description="Swing based explorer",label="Explorer IDE", name="ExplorerIDE")
public class ExplorerIDE implements Runnable {

	
	private JFrame frmJcrExploder;
	
	private RSyntaxTextArea editorTextArea;
	
	private BundleContext context;
	
	private ResourceResolver resourceResolver;
	
	private ResourceFactoryTracker resourceTracker;
	
	@SuppressWarnings("unused")
	private EventController controller;

	private static final Logger log = LoggerFactory
			.getLogger(ExplorerIDE.class);

	JcrTableModelImpl model = new JcrTableModelImpl();
	/**
	 * Create the application.
	 */
	public ExplorerIDE() {
		initialize();
	}
	

	
	@Reference(target="(type=fileImport)",policy=ReferencePolicy.STATIC)
	Command fileImport;
	
	private void bundleInitialize() {
		controller = new EventControllerDefaultImpl(){{
			addCommand(NodeSelected.class, new MultipleCommand(){{
				add(new UpdateTableModel(model));
				add(new UpdateEditorPane(editorTextArea));
			}});
			addCommand(FindFiles.class, fileImport);
			addCommand(NodeModified.class, new UpdateTree(tree));
			addCommand(Delete.class, new RemoveNodeCommand());
		}};
		resourceTracker = new ResourceFactoryTracker(context);
		resourceTracker.open();
	}
	
	JTree tree;
	private JTable table;

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		frmJcrExploder = new JFrame();
		frmJcrExploder.setTitle("JCR Exploder");
		frmJcrExploder.setBounds(100, 100, 800, 600);
		frmJcrExploder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(3);
		frmJcrExploder.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setDividerSize(2);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		
		
		
		JScrollPane propertiesScrollPane = new JScrollPane();
		splitPane_1.setRightComponent(propertiesScrollPane);
		
		table = new JTable();
		table.setFocusable(false);
		table.setIntercellSpacing(new Dimension(0, 1));
		table.setBounds(new Rectangle(1, 1, 1, 1));
		table.setShowVerticalLines(false);
		String[] headers = new String[] {
				"Name", "Type", "Value"
			};
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"jcr:contentType", "String", "nt:resource"},
				{null, null, null},
				{null, null, null},
			}, headers
		));
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
		
		tree = new JcrJTree();
		tree.setToolTipText("");
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("JTree") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("colors");
						node_1.add(new DefaultMutableTreeNode("blue"));
						node_1.add(new DefaultMutableTreeNode("violet"));
						node_1.add(new DefaultMutableTreeNode("red"));
						node_1.add(new DefaultMutableTreeNode("yellow"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("sports");
						node_1.add(new DefaultMutableTreeNode("basketball"));
						node_1.add(new DefaultMutableTreeNode("soccer"));
						node_1.add(new DefaultMutableTreeNode("football"));
						node_1.add(new DefaultMutableTreeNode("hockey"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("food");
						node_1.add(new DefaultMutableTreeNode("hot dogs"));
						node_1.add(new DefaultMutableTreeNode("pizza"));
						node_1.add(new DefaultMutableTreeNode("ravioli"));
						node_1.add(new DefaultMutableTreeNode("bananas"));
					add(node_1);
				}
			}
		));
		scrollPane.setViewportView(tree);
		
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
	
	
	class ResourceFactoryTracker extends ServiceTracker{
		
		
		public ResourceFactoryTracker(BundleContext context) {
			super(context, ResourceResolverFactory.class.getName(), null);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object addingService(ServiceReference reference) {
			if (resourceResolver != null){
				return null;
			}
			
			ResourceResolverFactory resourceResolverFactory = (ResourceResolverFactory)super.addingService(reference);
			try {
				resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
				((JcrJTree)tree).configureTree(resourceResolver);
				table.setModel(model);
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
			
			return resourceResolverFactory;
		}
		
	}
	
	ComponentContext componentContext;
	
	@Activate
	public void activate(BundleContext bc, ComponentContext context) throws Exception{
		this.context = bc;
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
		//frameInstance = new ExplorerIDE().frmJcrExploder;
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
