package explorer.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.JFileChooser;
import javax.swing.JTree;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.mime.MimeTypeService;

import explorer.events.NodeModified;
import flack.commands.api.Command;
import flack.control.api.Dispatcher;
import flack.control.api.Event;

@Component(name="Sling Explorer Command - File Import",description="Provides the UI to import a file or files")
@Service
@Property(name="type", value="fileImport")
public class FileImport implements Command {

	@Reference
	private MimeTypeService mimes;
	
	@Reference
	private Dispatcher dispatcher;
	

	@Override
	public void process(Event event) {
		Resource resource = (Resource)event.getData();
		Node node = resource.adaptTo(Node.class);
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

}
