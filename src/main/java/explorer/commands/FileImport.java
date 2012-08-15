package explorer.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.JFileChooser;

import org.apache.sling.commons.mime.MimeTypeService;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import explorer.events.NodeModified;
import explorer.ide.tree.JcrTreeNode;
import flack.commands.Command;
import flack.control.Dispatcher;
import flack.control.Event;

public class FileImport implements Command {

	private ServiceTracker mimeTypeTracker;
	
	private Dispatcher dispatcher = Dispatcher.getInstance();

	public FileImport(BundleContext context) {
		super();
		mimeTypeTracker = new ServiceTracker(context,
				MimeTypeService.class.getName(), null);
		mimeTypeTracker.open();
	}

	@Override
	public void process(Event event) {
		JcrTreeNode treeNode = (JcrTreeNode)event.getData();
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	
			Node node = treeNode.getNode();
			try {
				if (node.isNodeType("nt:folder")){
					File file = fc.getSelectedFile();
					if (file.isFile()){
						importFile(node,file);
						dispatcher.dispatchEvent(new NodeModified(event.getSource(), treeNode));
					} else if (file.isDirectory()){
						importFolder(node, file);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		}

	}

	public void importFile(Node parentnode, File file)
			throws RepositoryException, IOException {
		MimeTypeService mimeType = (MimeTypeService) mimeTypeTracker
				.getService();
		String mimeTypes = mimeType.getMimeType(file.getName());
		Node fileNode = parentnode.addNode(file.getName(), "nt:file");
		Node resNode = fileNode.addNode("jcr:content", "nt:resource");
		resNode.setProperty("jcr:mimeType", mimeTypes);
		resNode.setProperty("jcr:encoding", "");
		Binary binary = parentnode.getSession().getValueFactory()
				.createBinary(new FileInputStream(file));
		resNode.setProperty("jcr:data", binary);
		Calendar lastModified = Calendar.getInstance();
		lastModified.setTimeInMillis(file.lastModified());
		resNode.setProperty("jcr:lastModified", lastModified);
		System.out.println(fileNode.getPath());
		parentnode.getSession().save();
	}

	public void importFolder(Node parentnode, File directory)
			throws RepositoryException, IOException {
		File direntries[] = directory.listFiles();
		System.out.println(parentnode.getPath());
		for (int i = 0; i < direntries.length; i++) {
			File direntry = direntries[i];
			if (direntry.isDirectory()) {
				Node childnode = parentnode.addNode(direntry.getName(),
						"nt:folder");
				importFolder(childnode, direntry);
			} else {
				importFile(parentnode, direntry);
			}
		}

	}

}
