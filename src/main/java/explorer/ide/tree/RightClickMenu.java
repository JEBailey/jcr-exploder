package explorer.ide.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import explorer.events.Delete;
import explorer.events.FindFiles;
import flack.control.Dispatcher;

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
				JcrTreeNode treeNode = (JcrTreeNode)tree.getLastSelectedPathComponent();
				Dispatcher.getInstance().dispatchEvent(new FindFiles(this, treeNode));
			}
		});
		
		mntmDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JcrTreeNode treeNode = (JcrTreeNode)tree.getLastSelectedPathComponent();
				Dispatcher.getInstance().dispatchEvent(new Delete(this, treeNode));
			}
		});
					
	}
}
