package explorer.ide.tree;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import explorer.ide.ui.TreeMenu;

@SuppressWarnings("serial")
public class JcrJTree extends JTree {

	static {
		// Set the magic property which makes the first click outside the popup
		// capable of selecting tree nodes, as well as dismissing the popup.
		UIManager.put("PopupMenu.consumeEventOnClose", Boolean.FALSE);
	}

	public JcrJTree(TreeModel newModel) {
		super(newModel);
		init();
	}

	public JcrJTree() {
		super();
		init();
	}

	private void init() {
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		addMouseListener(new MouseAdapter() {
			
			private JPopupMenu menu = new TreeMenu();
			
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
					menu.show((Component) e.getSource(), e.getX() + 3,
							e.getY() + 3);
				}
			}

			/**
			 * Fix for right click not selecting tree nodes - We want to
			 * implement the following behaviour which matches windows explorer:
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
	}
	
	

	@Override
	public String convertValueToText(Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if (value != null) {
			if (value instanceof Node) {
				Node node = (Node) value;
				try {
					return node.getName();
				} catch (RepositoryException e) {
					//
				}
			}
		}
		return super.convertValueToText(value, selected, expanded, leaf, row,
				hasFocus);
	}

}
