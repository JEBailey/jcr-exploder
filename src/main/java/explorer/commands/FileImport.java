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
import flack.commands.api.Command;
import flack.control.Dispatcher;
import flack.control.DispatcherDefaultImpl;
import flack.control.EventDefaultImpl;

public class FileImport implements Command {

	private ServiceTracker mimeTypeTracker;
	
	private Dispatcher dispatcher = DispatcherDefaultImpl.getInstance();

	public FileImport(BundleContext context) {
		super();
		mimeTypeTracker = new ServiceTracker(context,
				MimeTypeService.class.getName(), null);
		mimeTypeTracker.open();
	}

	@Override
	public void process(EventDefaultImpl event) {
		Node node = (Node)event.getData();
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	
			try {
				if (node.isNodeType("nt:folder")){
					File file = fc.getSelectedFile();
					if (file.isFile()){
						Node fileNode = importFile(node,file);
						dispatcher.asynchEvent(new NodeModified(event.getSource(), fileNode));
					} else if (file.isDirectory()){
						importFolder(node, file);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		}

	}

	public Node importFile(Node parentnode, File file)
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
		return fileNode;
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
