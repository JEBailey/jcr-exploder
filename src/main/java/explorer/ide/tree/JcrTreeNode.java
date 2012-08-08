package explorer.ide.tree;

import java.util.Enumeration;
import javax.jcr.*;
import javax.swing.tree.TreeNode;

public class JcrTreeNode
    implements TreeNode
{

    public JcrTreeNode(Node node)
    {
        this.node = node;
    }

    public TreeNode getChildAt(int childIndex)
    {
        try
        {
            NodeIterator iterator = node.getNodes();
            iterator.skip(childIndex);
            return new JcrTreeNode(iterator.nextNode());
        }
        catch(RepositoryException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public int getChildCount()
    {
        try
        {
            return (int)node.getNodes().getSize();
        }
        catch(RepositoryException e)
        {
            return 0;
        }
    }

    public TreeNode getParent()
    {
        try
        {
            return new JcrTreeNode(node.getParent());
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public int getIndex(TreeNode node)
    {
        try
        {
            ((JcrTreeNode)node).getNode().getIndex();
        }
        catch(RepositoryException repositoryexception) { }
        return 0;
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public boolean isLeaf()
    {
        try
        {
            return !node.hasNodes();
        }
        catch(RepositoryException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public Enumeration children()
    {
        new Enumeration() {

            public boolean hasMoreElements()
            {
                return iter.hasNext();
            }

            public JcrTreeNode nextElement()
            {
                return new JcrTreeNode(iter.nextNode());
            }

            private NodeIterator iter;
            final JcrTreeNode treeNode;

            
            {
            	treeNode = JcrTreeNode.this;
                iter = null;
                try
                {
                    iter = node.getNodes();
                }
                catch(Exception exception) { }
            }
        }
;
        return null;
    }

    public Node getNode()
    {
        return node;
    }

    private Node node;

}
