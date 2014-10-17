package explorer.ui.tree.actions;

import static org.osgi.service.event.EventConstants.EVENT_TOPIC;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JTree;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.jcr.contentloader.ContentImportListener;
import org.apache.sling.jcr.contentloader.ContentImporter;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import explorer.core.api.ResourceTreeModel;
import explorer.core.api.SessionProvider;
import explorer.node.NodeTypeUtil;
import explorer.ui.EventTypes;

@SuppressWarnings("serial")
@Component(name = "Sling Explorer Menu Action - Import File", description = "Updates the Editor Pane with the correct view")
@Service(value = { AbstractAction.class, EventHandler.class })
@Properties(value = { @Property(name = EVENT_TOPIC, value = EventTypes.NEW_SELECTION),
		@Property(name = "menuType", value = "TREEMENU") })
public class ImportFile extends AbstractAction implements EventHandler {

	private Resource selectedResource;

	private static final Logger log = LoggerFactory.getLogger(ImportFile.class);

	@Reference
	MimeTypeService mimes;

	@Reference
	SessionProvider sessionProvider;

	@Reference
	ContentImporter importer;

	@Reference
	ContentImportListener listener;

	@Reference
	ResourceTreeModel treeModel;

	@Reference
	JTree jTree;

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
				if (node.isNodeType("nt:folder")) {
					File file = fc.getSelectedFile();
					if (file.isFile()) {
						importFile3(node, file);
					} else if (file.isDirectory()) {
						importFolder(node, file);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public Node importFile3(Node parentnode, File file) throws RepositoryException, IOException {

		String mimeTypes = null;
		try {
			mimeTypes = determineMimeType(new FileInputStream(file));
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // mimes.getMimeType(file.getName());
		Node fileNode = parentnode.addNode(file.getName(), "nt:file");

		Node resNode = fileNode.addNode("jcr:content", "nt:resource");
		Binary binary = parentnode.getSession().getValueFactory().createBinary(new FileInputStream(file));
		Calendar lastModified = Calendar.getInstance();
		lastModified.setTimeInMillis(file.lastModified());

		resNode.setProperty("jcr:mimeType", mimeTypes);
		resNode.setProperty("jcr:encoding", "");
		resNode.setProperty("jcr:data", binary);
		resNode.setProperty("jcr:lastModified", lastModified);
		sessionProvider.save();

		treeModel.fireStructureChanged(jTree.getSelectionPath());

		return fileNode;
	}

	private String determineMimeType(InputStream is) throws TikaException, IOException {
		TikaConfig tika = new TikaConfig();
		Metadata data = new Metadata();
		MediaType mimetype = tika.getDetector().detect(TikaInputStream.get(is), data);
		log.error("Stream " + is + " is " + mimetype);
		log.error("Stream " + is + " is " + Arrays.toString(data.names()));
		return mimetype.toString();
	}

	public void importFolder(Node parentNode, File directory) throws RepositoryException, IOException {
		File children[] = directory.listFiles();
		Node folderNode = parentNode.addNode(directory.getName(), "sling:Folder");
		sessionProvider.save();
		System.out.println(parentNode.getPath());
		for (int i = 0; i < children.length; i++) {
			File child = children[i];
			if (child.isDirectory()) {
				Node childnode = parentNode.addNode(child.getName(), "nt:folder");
				sessionProvider.save();
				importFolder(childnode, child);
			} else {
				importFile3(parentNode, child);
			}
		}

	}

	@Override
	public void handleEvent(Event event) {
		selectedResource = (Resource) event.getProperty("data");
		setEnabled(NodeTypeUtil.isType(selectedResource, "nt:folder"));
	}
}
