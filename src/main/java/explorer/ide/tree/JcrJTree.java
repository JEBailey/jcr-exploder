package explorer.ide.tree;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

import explorer.events.NodeSelected;
import flack.control.api.Dispatcher;

@org.apache.felix.scr.annotations.Component(name="Sling Explorer UI - Tree Renderer",description="Displays the node based tree")
@Service(value=JTree.class)
@SuppressWarnings("serial")
public class JcrJTree extends JTree {

	static {
		// Set the magic property which makes the first click outside the popup
		// capable of selecting tree nodes, as well as dismissing the popup.
		UIManager.put("PopupMenu.consumeEventOnClose", Boolean.FALSE);
	}
	
	@Reference
	DefaultTreeCellRenderer jcrTreeNodeRenderer;
	
	@Reference
	TreeModel treeModel;
	
	@Reference
	Dispatcher dispatcher;
	
	@Reference
	RightClickMenu rightClick;

	@Activate
	private void activate() {
		getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		addMouseListener(new MouseAdapter() {

			private JPopupMenu menu = rightClick;

			public void mousePressed(MouseEvent e) {
				showMenuIfPopupTrigger(e);
			}

			public void mouseClicked(MouseEvent e) {
				showMenuIfPopupTrigger(e);
			}

			public void mouseReleased(MouseEvent e) {
				showMenuIfPopupTrigger(e);
			}

			private void showMenuIfPopupTrigger(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					// set the new selections before showing the popup
					setSelectedItemsOnPopupTrigger(e);

					// show the menu, offsetting from the mouse click slightly
					menu.show((Component) e.getSource(), e.getX() + 3, e.getY() + 3);
				}
			}

			/**
			 * Fix for right click not selecting tree nodes - We want to
			 * implement the following behavior which matches windows explorer:
			 * If the item under the click is not already selected, clear the
			 * current selections and select the item, prior to showing the
			 * popup. If the item under the click is already selected, keep the
			 * current selection(s)
			 */
			private void setSelectedItemsOnPopupTrigger(MouseEvent e) {
				TreePath p = getPathForLocation(e.getX(), e.getY());
				if (!getSelectionModel().isPathSelected(p)) {
					getSelectionModel().setSelectionPath(p);
				}
			}
		});
		setCellRenderer(jcrTreeNodeRenderer);
		setToolTipText("");
		configureTree();
	}

	@Override
	public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		if (value != null) {
			if (value instanceof Resource) {
				Resource node = (Resource) value;
				return node.getName();
			}
		}
		return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
	}

	private void configureTree() {
		setModel(treeModel);

		getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Resource treeNode = (Resource) getLastSelectedPathComponent();
				if (treeNode != null) {
					dispatcher.dispatchEvent(new NodeSelected(this, treeNode));
					// TODO: handle a null selection which occurs when a
					// selection is deleted.
					// this should be passed into the handler which then
					// displays a default view
				}
			}

		});

	}

}
