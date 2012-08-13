package explorer.ide.ui;

import java.io.*;
import java.util.Calendar;
import javax.jcr.*;
import org.apache.sling.commons.mime.MimeTypeService;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Util extends ServiceTracker
{

    private static MimeTypeService mimeType;
    
    public Util(BundleContext context)
    {
    	super(context,MimeTypeService.class.getName(),null);
    }

    public static void importFile(Node parentnode, File file)
        throws RepositoryException, IOException
    {
        String mimeTypes = mimeType.getMimeType(file.getName());
        Node fileNode = parentnode.addNode(file.getName(), "nt:file");
        Node resNode = fileNode.addNode("jcr:content", "nt:resource");
        resNode.setProperty("jcr:mimeType", mimeTypes);
        resNode.setProperty("jcr:encoding", "");
        javax.jcr.Binary binary = parentnode.getSession().getValueFactory().createBinary(new FileInputStream(file));
        resNode.setProperty("jcr:data", binary);
        Calendar lastModified = Calendar.getInstance();
        lastModified.setTimeInMillis(file.lastModified());
        resNode.setProperty("jcr:lastModified", lastModified);
        System.out.println(fileNode.getPath());
        parentnode.getSession().save();
    }

    private static void importFolder(Node parentnode, File directory)
        throws RepositoryException, IOException
    {
        File direntries[] = directory.listFiles();
        System.out.println(parentnode.getPath());
        for(int i = 0; i < direntries.length; i++)
        {
            File direntry = direntries[i];
            if(direntry.isDirectory())
            {
                Node childnode = parentnode.addNode(direntry.getName(), "nt:folder");
                importFolder(childnode, direntry);
            } else
            {
                importFile(parentnode, direntry);
            }
        }

    }


}
