package explorer.ide;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.io.IOException;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import explorer.ide.tree.JcrJTree;
import explorer.ide.tree.JcrNodeTreeModel;
import explorer.ide.tree.JcrTableModelImpl;
import explorer.ide.tree.JcrTreeCellRenderer;

public class ExplorerIDE {

	public JFrame frmJcrExploder;
	
	private RSyntaxTextArea editorPane;
	
	private ResourceResolver resourceResolver;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExplorerIDE window = new ExplorerIDE();
					window.frmJcrExploder.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
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
	
	public ExplorerIDE(ResourceResolver resourceResolver) {
		initialize();
		this.resourceResolver = resourceResolver;
		tree.setModel(new JcrNodeTreeModel(resourceResolver));
		tree.setCellRenderer(new JcrTreeCellRenderer());
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Node node = (Node)tree.getLastSelectedPathComponent();
				if (node == null) return;
				model.updateModel(node);
				updateEditorPane(node);
			}
			
		});
		table.setModel(model);
	}
	
	private void updateEditorPane(Node node){
		Property prop = null;
		String reply = "";
		try {
			prop = node.getProperty("jcr:primaryType");
			if (prop.getString().endsWith("nt:file")){
				prop = node.getProperty("jcr:content/jcr:mimeType");
				if (prop.getString().contains("text")){
					prop = node.getProperty("jcr:content/jcr:data");
					Binary binary = prop.getBinary();
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
		editorPane.setText(reply);
		editorPane.setCaretPosition(0);
	}
	
	
	JTree tree;
	private JTable table;

	/**
	 * Initialize the contents of the frame.
	 */
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
		
		RTextScrollPane editorScrollPane = new RTextScrollPane();
		splitPane_1.setLeftComponent(editorScrollPane);
		
		//set syntax kit
		jsyntaxpane.DefaultSyntaxKit.initKit();
		
		editorPane = new RSyntaxTextArea();
		editorPane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		editorPane.setCodeFoldingEnabled(true);
		editorPane.setAntiAliasingEnabled(true);
		//set to edit java by default
		//editorPane.setEditorKit();
		editorPane.setText("public static void main(String[] args) {\n}");
		

		editorScrollPane.add(editorPane);
		editorScrollPane.setFoldIndicatorEnabled(true);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane_2);
		
		table = new JTable();
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
		scrollPane_2.setViewportView(table);
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
	


}
