/*
Copyright 2016 JE Bailey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package explorer.ui.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
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
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.core.api.ResourceTreeModel;
import explorer.core.api.SessionProvider;
import explorer.ui.resource.TreeResourceWrapper;

@Component(name = "Sling Explorer UI - Tree Model")
@Service
public class CoreTreeModel implements ResourceTreeModel {

	@Reference
	private ResourceResolverFactory factory;

	@Reference
	SessionProvider provider;

	ResourceResolver resourceResolver;

	private final EventListenerList listenerList = new EventListenerList();

	@Activate
	private void activate() throws LoginException {
		Map<String, Object> authInfo = new HashMap<String, Object>();
		authInfo.put(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, provider);
		resourceResolver = factory.getResourceResolver(authInfo);
	}

	@Deactivate
	private void deactivate() {
		resourceResolver.close();
	}

	@Override
	public Object getRoot() {
		return new TreeResourceWrapper(resourceResolver.getResource("/"));
	}

	@Override
	public Object getChild(Object parent, int index) {
		Iterator<Resource> ni = ((Resource) parent).listChildren();
		while (index > 0) {
			ni.next();
			--index;
		}
		return new TreeResourceWrapper(ni.next());
	}

	@Override
	public int getChildCount(Object parent) {
		int reply = 0;
		Resource resource = (Resource) parent;
		Iterator<Resource> it = resource.listChildren();
		while (it.hasNext()) {
			it.next();
			++reply;
		}
		return reply;
	}

	@Override
	public boolean isLeaf(Object node) {
		Resource resource = (Resource) node;
		return !resource.listChildren().hasNext();

	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// This doesn't need to be implemented other then firing off a
		// message that the node has changed

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		Resource resource = ((Resource) parent);
		Resource childResource = ((Resource) child);
		int index = 0;
		boolean found = false;

		String childPath = childResource.getPath();
		Iterator<Resource> iter = resource.listChildren();
		while (iter.hasNext()) {
			String currentPath = iter.next().getPath();
			if (currentPath.equals(childPath)) {
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

	// / custom firing methods to notify listeners that things have happened

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
	public void fireTreeNodesChanged(Object[] path, int[] childIndices, Object[] children) {
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
	private static final Logger log = LoggerFactory.getLogger(CoreTreeModel.class);

	public TreePath convertToPath(Resource resource) {
		List<Resource> list = new ArrayList<Resource>();
		
		while (resource != null){
			if (!(resource instanceof TreeResourceWrapper)){
				resource = new TreeResourceWrapper(resource);
			}
			list.add(0,resource);
			resource = resource.getParent();
		}
		TreePath reply = new TreePath(list.toArray());
		log.error(reply.toString());
		return new TreePath(list.toArray());
	}



	public void fireStructureChanged(TreePath path) {
		TreeModelListener[] listeners = listenerList.getListeners(TreeModelListener.class);
		TreeModelEvent event = new TreeModelEvent(this, path);
		for (TreeModelListener lis : listeners) {
			lis.treeStructureChanged(event);
		}
	}


	@Override
	public void fireStructureChanged(Resource resource) {
		fireStructureChanged(convertToPath(resource));
	}

}
