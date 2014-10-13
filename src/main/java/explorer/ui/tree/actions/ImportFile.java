package explorer.ui.tree.actions;

import static org.osgi.service.event.EventConstants.EVENT_TOPIC;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.jcr.contentloader.ContentImportListener;
import org.apache.sling.jcr.contentloader.ContentImporter;
import org.apache.sling.jcr.contentloader.ImportOptions;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import explorer.core.api.SessionProvider;
import explorer.node.NodeTypeUtil;
import explorer.ui.EventTypes;

@SuppressWarnings("serial")
@Component(name = "Sling Explorer Menu Action - Import File", description = "Updates the Editor Pane with the correct view")
@Service(value={AbstractAction.class,EventHandler.class})
@Properties(value = { @Property(name = EVENT_TOPIC, value = EventTypes.NEW_SELECTION),@Property(name="menuType",value="TREEMENU") })
public class ImportFile extends AbstractAction implements EventHandler {

	private Resource selectedResource;
	
	@Reference
	MimeTypeService mimes;
	
	@Reference
	SessionProvider sessionProvider;
	
	@Reference
	ContentImporter importer;
	
	@Reference
	ContentImportListener listener;
	
	public ImportFile() {
		super("Import Files");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {

		Node node = selectedResource.adaptTo(Node.class);
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	
			try {
				if (node.isNodeType("nt:folder")){
					File file = fc.getSelectedFile();
					if (file.isFile()){
						importFile2(node,file);
						//Dictionary<String,Object> props = new Hashtable<String,Object>();
						//props.put("source", event.getProperty("source"));
						//props.put("path",fileNode.getPath());
						//eventQueue.postEvent(new Event("explorer/gui/RESOURCE_MODIFIED",props));
					} else if (file.isDirectory()){
						importFolder(node, file);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		}
	}
	
	public Node importFile3(Node parentnode, File file)
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
		sessionProvider.save();
		return fileNode;
	}
	
	public void importFile2(Node parentnode, File file)
			throws RepositoryException, IOException {
		importer.importContent(parentnode, file.getName(),(InputStream) new FileInputStream(file), new ImportOptions() {
			
			@Override
			public boolean isPropertyOverwrite() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isOverwrite() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean isIgnoredImportProvider(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isCheckin() {
				// TODO Auto-generated method stub
				return false;
			}
		}, listener);
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
				sessionProvider.save();
				importFolder(childnode, direntry);
			} else {
				importFile2(parentnode, direntry);
			}
		}

	}

	@Override
	public void handleEvent(Event event) {
		selectedResource = (Resource) event.getProperty("data");
		setEnabled(NodeTypeUtil.isType(selectedResource, "nt:folder"));
	}
}
