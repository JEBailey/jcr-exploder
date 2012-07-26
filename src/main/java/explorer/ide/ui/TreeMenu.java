package explorer.ide.ui;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class TreeMenu extends JPopupMenu {
	
	public TreeMenu() {
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		add(mntmCopy);
		JMenuItem mntmPaste = new JMenuItem("Paste");
		add(mntmPaste);
		JMenuItem mntmImportFiles = new JMenuItem("Import Files");
		add(mntmImportFiles);
	}

}
