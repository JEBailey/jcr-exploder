package explorer.ide.tree;

import java.util.Enumeration;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class JcrTreeNode extends DefaultMutableTreeNode {
	
	private Node currentNode;

	public JcrTreeNode(Node node) {
		this.currentNode = node;
	}

	public TreeNode getChildAt(int childIndex) {
		try {
			NodeIterator iterator = currentNode.getNodes();
			iterator.skip(childIndex);
			Node child = iterator.nextNode();
			return new JcrTreeNode(child);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}

	public int getChildCount() {
		try {
			return (int) currentNode.getNodes().getSize();
		} catch (RepositoryException e) {
			return 0;
		}
	}

	public TreeNode getParent() {
		try {
			return new JcrTreeNode(currentNode.getParent());
		} catch (Exception e) {
			return null;
		}
	}

	public int getIndex(TreeNode node) {
		int reply = -1;

		try {
			Node childNode = ((JcrTreeNode) node).getNode();
			NodeIterator iter = currentNode.getNodes();
			int index = 0;
			String childNodePath = childNode.getPath();
			while (iter.hasNext()){
				if (iter.nextNode().getPath().equals(childNodePath)){
					break;
				}
				++index;
			}
			reply = index;
		} catch (RepositoryException repositoryexception) {
			repositoryexception.printStackTrace();
		}
		return reply;
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		try {
			return !currentNode.hasNodes();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Enumeration<JcrTreeNode> children() {
		return new Enumeration<JcrTreeNode>() {
			
			{
				iter = null;
				try {
					iter = currentNode.getNodes();
				} catch (Exception exception) {
				}
			}
			
			
			public boolean hasMoreElements() {
				return iter.hasNext();
			}

			public JcrTreeNode nextElement() {
				return new JcrTreeNode(iter.nextNode());
			}

			private NodeIterator iter;

			
		};
	}

	public Node getNode() {
		//System.out.print(node.getName());
		return currentNode;
	}

	@Override
	public int hashCode() {
		try {
			return currentNode.getPath().hashCode();
		} catch (RepositoryException e) {
			return -1;
		}
	}

	@Override
	public boolean equals(Object obj) {
		try {
			Node other = ((JcrTreeNode)obj).getNode();
			return currentNode.getPath().equals(other.getPath());
		} catch (Exception e) {
		}
		return true;
	}

	@Override
	public String toString() {
		try {
			return currentNode.getName();
		} catch (RepositoryException e) {
			return "error";
		}
	}
}
