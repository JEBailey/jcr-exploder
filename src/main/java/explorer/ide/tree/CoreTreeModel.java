package explorer.ide.tree;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
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

public class CoreTreeModel implements TreeModel {

	private ResourceResolver resourceResolver;
	private Session session;
	
	private final EventListenerList listenerList = new EventListenerList();

	public CoreTreeModel(ResourceResolver resourceResolver) {
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
		// This doesn't need to be implemented other then firing off a
		// message that the node has changed

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		int index = 0;
		boolean found = false;
		try {
			String childPath = ((Node)child).getPath();
			NodeIterator iter = ((Node)parent).getNodes();
			while (iter.hasNext()){
				String currentPath = ((Node)iter.nextNode()).getPath();
				if (currentPath.equals(childPath)){
					// we need to do a path comparison here
					// because there's no assurance that the
					// child object is the identical object that
					// is found when we access the child nodes
					found = true;
					break;
				}
				++index;
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return found ? index : -1;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(TreeModelListener.class, l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(TreeModelListener.class, l);
	}

	private void checkLive() {
		if (!session.isLive()) {
			session.logout();
			session = resourceResolver.adaptTo(Session.class);
		}
	}
	
	/// custom firing methods to notify listeners that things have happened
	
    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source
     *            the node being changed
     * @param path
     *            the path to the root node
     * @param childIndices
     *            the indices of the changed elements
     * @param children
     *            the changed elements
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object[] path, int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(this, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source
     *            the node where new elements are being inserted
     * @param path
     *            the path to the root node
     * @param childIndices
     *            the indices of the new elements
     * @param children
     *            the new elements
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object[] path, int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(this, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source
     *            the node where elements are being removed
     * @param path
     *            the path to the root node
     * @param childIndices
     *            the indices of the removed elements
     * @param children
     *            the removed elements
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(Object[] path, int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(this, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source
     *            the node where the tree model has changed
     * @param path
     *            the path to the root node
     * @param childIndices
     *            the indices of the affected elements
     * @param children
     *            the affected elements
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(Object[] path, int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(this, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /**
     * The only event raised by this model is TreeStructureChanged with the root
     * as path, i.e. the whole tree has changed.
     */
    protected void fireRootNodeChanged(Object previousRoot) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(this, new Object[] { previousRoot });
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * The only event raised by this model is TreeStructureChanged with the root
     * as path, i.e. the whole tree has changed.
     */
    protected void fireRootTreeStructureChanged(Object previousRoot) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(this, new Object[] { previousRoot });
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }
    
    public void removeNode(Object node){
    	Node workingNode = ((Node)node);
    	try {
			Node parentNode = workingNode.getParent();
			int indexOfNode = getIndexOfChild(parentNode, workingNode);
			fireTreeNodeRemoved(convertToPath(parentNode),indexOfNode,workingNode);
			workingNode.remove();
			parentNode.getSession().save();
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void insertNode(Node node){
    	Node parentNode;
		try {
			parentNode = node.getParent();
	    	fireTreeNodeInserted(convertToPath(parentNode), getIndexOfChild(parentNode, node), node);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public void updateStructure(Node node){
    	Node parentNode;
		try {
			parentNode = node.getParent();
			fireTreeStructureChanged(convertToPath(parentNode), getIndexOfChild(parentNode, node), node);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    protected Object[] convertToPath(Node node){
    	Node workNode = node;
    	Object[] reply = {};
    	try {
    		int depth = node.getDepth();
			reply = new Object[depth+1];//depth starts at 0 for root
			while(depth > 0){
				reply[depth] = workNode;
				workNode = workNode.getParent();
				depth = workNode.getDepth();
			}
			reply[depth]= workNode;//root
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
    	return reply;
    }
    

    protected void fireTreeNodeRemoved(Object[] path, int index, Object child) {
        fireTreeNodesRemoved(path, new int[] { index }, new Object[] { child });
    }

    protected void fireTreeNodeInserted(Object[] path, int index, Object child) {
        fireTreeNodesInserted(path, new int[] { index }, new Object[] { child });
    }

    protected void fireTreeStructureChanged(Object[] path, int index, Object child) {
        fireTreeStructureChanged(path, new int[] { index }, new Object[] { child });
    }

    protected void fireTreeNodeChanged(Object[] path, int index, Object child) {
        fireTreeNodesChanged(path, new int[] { index }, new Object[] { child });
    }

}
