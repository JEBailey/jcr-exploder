package explorer.ide.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import org.apache.sling.api.resource.Resource;

import explorer.events.FindFiles;
import flack.control.DispatcherDefaultImpl;

@SuppressWarnings("serial")
public class RightClickMenu extends JPopupMenu {

	JTree tree;

	public RightClickMenu(JTree tree) {
		super();
		this.tree = tree;
	}

	JMenuItem mntmCopy = new JMenuItem("Copy");
	JMenuItem mntmPaste = new JMenuItem("Paste");
	JMenuItem mntmDelete = new JMenuItem("Delete");
	final JMenuItem mntmImportFiles = new JMenuItem("Import Files");

	{
		add(mntmCopy);
		add(mntmPaste);
		add(mntmImportFiles);
		add(mntmDelete);
		mntmImportFiles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Resource treeNode = (Resource) tree.getLastSelectedPathComponent();
				DispatcherDefaultImpl.getInstance().dispatchEvent(new FindFiles(this, treeNode));
			}
		});

		mntmDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Node treeNode = (Node) tree.getLastSelectedPathComponent();
				try {
					Node parentNode = treeNode.getParent();
					treeNode.remove();
					parentNode.getSession().save();
					((CoreTreeModel) tree.getModel()).updateStructure(null);
				} catch (AccessDeniedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ItemNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RepositoryException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}
}
