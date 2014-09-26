package explorer.commands;

import static org.osgi.service.event.EventConstants.EVENT_TOPIC;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.JFileChooser;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.mime.MimeTypeService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;

@Component(name="Sling Explorer Command - File Import",description="Provides the UI to import a file or files")
@Service
@Property(name=EVENT_TOPIC, value="explorer/gui/IMPORT_FILE")
public class FileImport implements EventHandler {

	@Reference
	private MimeTypeService mimes;
	
	@Reference
	private EventAdmin eventQueue;
	

	public Node importFile(Node parentnode, File file)
			throws RepositoryException, IOException {

		String mimeTypes = mimes.getMimeType(file.getName());
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

	@Override
	public void handleEvent(final Event event) {
		Resource resource = (Resource)event.getProperty("resource");
		Node node = resource.adaptTo(Node.class);
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	
			try {
				if (node.isNodeType("nt:folder")){
					File file = fc.getSelectedFile();
					if (file.isFile()){
						Node fileNode = importFile(node,file);
						Dictionary<String,Object> props = new Hashtable<String,Object>();
						props.put("source", event.getProperty("source"));
						props.put("path",fileNode.getPath());
						eventQueue.postEvent(new Event("explorer/gui/RESOURCE_MODIFIED",props));
					} else if (file.isDirectory()){
						importFolder(node, file);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		}
		
	}



}
