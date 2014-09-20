package explorer.ide.tree;

import java.util.Iterator;

import javax.jcr.Session;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

@Component(name="Sling Explorer UI - Tree Model",description="Models resource tree")
@Service
public class CoreTreeModel implements TreeModel {

	@Reference
	private ResourceResolverFactory factory;
	
	private ResourceResolver resourceResolver;
	private Session session;
	
	private final EventListenerList listenerList = new EventListenerList();

	@Activate
	private void activate() throws LoginException {
		resourceResolver = factory.getAdministrativeResourceResolver(null);
		session = resourceResolver.adaptTo(Session.class);
	}

	@Deactivate
	private void deactivate(){
		resourceResolver.close();
	}
	

	@Override
	public Object getRoot() {
		checkLive();
		return resourceResolver.getResource("/");
	}

	@Override
	public Object getChild(Object parent, int index) {
		checkLive();
		Iterator<Resource> ni = ((Resource) parent).listChildren();
		while (index > 0){
			ni.next();
			--index;
		}
		return ni.next();
	}

	@Override
	public int getChildCount(Object parent) {
		checkLive();
		int reply = 0;
		Resource resource = (Resource) parent;
		Iterator<Resource> it = resource.listChildren();
		while (it.hasNext()){
			it.next();
			++reply;
		}
		return reply;
	}

	@Override
	public boolean isLeaf(Object node) {
		Resource resource = (Resource)node;
		checkLive();
		return !resource.listChildren().hasNext();

	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// This doesn't need to be implemented other then firing off a
		// message that the node has changed

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		Resource resource = ((Resource)parent);
		Resource childResource = ((Resource)child);
		int index = 0;
		boolean found = false;

		String childPath = childResource.getPath();
		Iterator<Resource> iter = resource.listChildren();
		while (iter.hasNext()){
			String currentPath = iter.next().getPath();
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
    	/*
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
		*/
    	
    }
    
    public void insertNode(Resource node){
    	/*
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
		}*/
    }
    
    
    public void updateStructure(Resource node){
    	/*
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
		}*/
    }
    
    protected Object[] convertToPath(Resource node){
    	/*
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
    	return reply;*/return null;
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
