package explorer.ide.tree;


import java.util.LinkedList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.sling.api.resource.ResourceResolver;

public class JcrNodeTreeModel implements TreeModel {

	private ResourceResolver resourceResolver;
	private Session session;
	private EventListenerList list = new EventListenerList();

	public JcrNodeTreeModel(ResourceResolver resourceResolver) {
		this.resourceResolver = resourceResolver;
		this.session = this.resourceResolver.adaptTo(Session.class);
	}

	@Override
	public Object getRoot() {
		checkLive();
		try {
			return session.getRootNode();
		} catch (RepositoryException e) {
			return null;
		}
	}

	@Override
	public Object getChild(Object parent, int index) {
		checkLive();
		NodeIterator ni;
		try {
			ni = ((Node) parent).getNodes();
			ni.skip(index);
			return ni.next();
		} catch (RepositoryException e) {
			return null;
		}

	}

	@Override
	public int getChildCount(Object parent) {
		checkLive();
		try {
			if (((Node) parent).isNodeType("nt:file")) {
				return 0;
			}
			int reply = (int) ((Node) parent).getNodes().getSize();
			return reply < 1 ? 0 : reply;
		} catch (RepositoryException e) {
			return 0;
		}
	}

	@Override
	public boolean isLeaf(Object node) {
		checkLive();
		try {
			Node currentNode = (Node) node;
			return currentNode.isNodeType("nt:file") ? true : !currentNode
					.getNodes().hasNext();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		list.add(TreeModelListener.class, l);

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		list.remove(TreeModelListener.class, l);
	}

	private void checkLive() {
		if (!session.isLive()) {
			session.logout();
			session = resourceResolver.adaptTo(Session.class);
		}
	}
	
	public void updateNode(Node node){
		List<Node>nodes = new LinkedList<Node>();
		try {
			Node child = node;
			while (true) {
				nodes.add(child);
				child = child.getParent();
			} 
		} catch (RepositoryException e1) {
			// swallow
		}
		
		Node[] paths = paths(nodes);
		
		 // Guaranteed to return a non-null array
		TreeModelListener[] listeners = list.getListeners(TreeModelListener.class);//List(ListenerList();
        TreeModelEvent e =  new TreeModelEvent(node, paths);
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (TreeModelListener listener:listeners){
        	listener.treeStructureChanged(e);     
        }
	}
	
	private Node[] paths(List<Node> nodes){
		Node[] reply = new Node[nodes.size()];
		int index = nodes.size();
		while (!nodes.isEmpty()){
			reply[--index] = nodes.remove(0);
		}
		return reply;
	}

}
