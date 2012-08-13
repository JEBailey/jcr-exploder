package explorer.ide;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.io.IOException;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
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
import javax.swing.border.CompoundBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.ide.table.JcrTableModelImpl;
import explorer.ide.tree.JcrJTree;
import explorer.ide.tree.JcrTreeNode;
import explorer.ide.tree.JcrTreeNodeRenderer;


public class ExplorerIDE {

	public JFrame frmJcrExploder;
	
	private RSyntaxTextArea editorTextArea;
	
	private BundleContext context;
	
	private ResourceResolver resourceResolver;
	
	private ResourceFactoryTracker resourceTracker;

	private static final Logger log = LoggerFactory
			.getLogger(ExplorerIDE.class);

	
	String[] headers = new String[] {
			"Name", "Type", "Value"
		};

	JcrTableModelImpl model = new JcrTableModelImpl();
	/**
	 * Create the application.
	 */
	public ExplorerIDE() {
		initialize();
	}
	
	public ExplorerIDE(BundleContext context) {
		this.context = context;
		initialize();
		resourceTracker = new ResourceFactoryTracker(context);
		resourceTracker.open();
	}
	
	
	private void configureTree(Session session) throws Exception{
		tree.setModel(new DefaultTreeModel(new JcrTreeNode(session.getRootNode())));
		tree.setCellRenderer(new JcrTreeNodeRenderer());
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Node node = ((JcrTreeNode)tree.getLastSelectedPathComponent()).getNode();
				if (node == null) return;
				model.updateModel(node);
				updateEditorPane(node);
			}
			
		});
		
	}
	
	
	private void updateEditorPane(Node node){
		Property prop = null;
		String reply = "";
		try {
			if (node.isNodeType("nt:file")){
				prop = node.getProperty("jcr:content/jcr:mimeType");
				if (prop.getString().contains("text") || prop.getString().contains("application")){
					Property prop2 = node.getProperty("jcr:content/jcr:data");
					Binary binary = prop2.getBinary();
					byte[] temp = new byte[(int)binary.getSize()];
					binary.read(temp, 0);
					reply = new String(temp);
				}
			} else {
				reply = "non content node";
			}
		} catch (PathNotFoundException e) {
			reply = "non-conforming node";
		} catch (RepositoryException e) {
			reply = "problem occured in repository";
		} catch (IOException e) {
			reply = "problem reading stream";
		}
		editorTextArea.setText(reply);
		editorTextArea.setCaretPosition(0);
		String syntax = null;
		if (prop != null){
			try {
				syntax = prop.getString();
				syntax = syntax.replace("application/","text/");
			} catch (ValueFormatException e) {
			} catch (RepositoryException e) {

			}
		}
		//custom set
		editorTextArea.setSyntaxEditingStyle(syntax);
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
				configureTree(resourceResolver.adaptTo(Session.class));
				table.setModel(model);
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
			}
			
			return resourceResolverFactory;
		}
		
	}


}
