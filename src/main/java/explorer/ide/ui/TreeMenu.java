package explorer.ide.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jcr.Session;
import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import org.apache.jackrabbit.commons.JcrUtils;

import explorer.ide.Model;

@SuppressWarnings("serial")
public class TreeMenu extends JPopupMenu {
	
	public TreeMenu() {
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		add(mntmCopy);
		JMenuItem mntmPaste = new JMenuItem("Paste");
		add(mntmPaste);
		final JMenuItem mntmImportFiles = new JMenuItem("Import Files");
		add(mntmImportFiles);
		
		mntmImportFiles.addActionListener(new ActionListener() {
			

			
			@Override
			public void actionPerformed(ActionEvent e) {
				Session session = Model.getSession();
				final JFileChooser fc = new JFileChooser();
				//fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

			    if( fc.showOpenDialog( mntmImportFiles ) == JFileChooser.APPROVE_OPTION )
			    {
			       // return fc.getSelectedFile().getAbsolutePath();
			    }
				
			}
		});
		
	}

}
