package explorer.ide.tree;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.sling.api.resource.ResourceResolver;

public class JcrNodeTreeModel implements TreeModel {

	
	
	private ResourceResolver resourceResolver;
	private Session session;

	public JcrNodeTreeModel(ResourceResolver resourceResolver){
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
			ni = ((Node)parent).getNodes();
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
			if (((Node)parent).isNodeType("nt:file")){
				return 0;
			}
			int reply = (int)((Node)parent).getNodes().getSize();
			return reply < 1 ? 0 : reply ;
		} catch (RepositoryException e) {
			return 0;
		}
	}

	@Override
	public boolean isLeaf(Object node) {
		checkLive();
			try {
				if (((Node)node).isNodeType("nt:file")){
					return true;
				}
				return !((Node)node).getNodes().hasNext();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}
	
	private void checkLive(){
		if (!session.isLive()){
			session.logout();
			session = resourceResolver.adaptTo(Session.class);
		}
	}

}
