package explorer.ui.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

@Component(name="Sling Explorer UI - Tree Menu")
@Service(value=RightClickMenu.class)
@SuppressWarnings("serial")
public class RightClickMenu extends JPopupMenu {

	
	@Reference
	JTree tree;
	
	@Reference
	AbstractAction importFiles;
	
	Resource resource;
	
	public void setResource(Resource resource){
		this.resource = resource;
	}

	JMenuItem mntmCopy = new JMenuItem("Copy");
	JMenuItem mntmPaste = new JMenuItem("Paste");
	JMenuItem mntmDelete = new JMenuItem("Delete");

	@Activate
	public void activate(){
		add(mntmCopy);
		add(mntmPaste);
		add(new JMenuItem(importFiles));
		add(mntmDelete);

		mntmDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					//Node parentNode = treeNode.getParent();
					//treeNode.remove();
					//parentNode.getSession().save();
					//((CoreTreeModel) tree.getModel()).updateStructure(null);
			}
		});

	}
}
