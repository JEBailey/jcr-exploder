package explorer.commands;

import java.io.IOException;
import java.io.InputStream;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import explorer.node.NodeTypeUtil;
import flack.commands.api.Command;
import flack.control.EventDefaultImpl;

public class UpdateEditorPane implements Command {

	private RSyntaxTextArea textArea;
	
	public UpdateEditorPane(RSyntaxTextArea textArea) {
		super();
		this.textArea = textArea;
	}

	@Override
	public void process(EventDefaultImpl event) {
		Resource resource = (Resource)event.getData();
		ResourceMetadata metaData = resource.getResourceMetadata();
		String prop = null;
		String reply = "";
		try {
			if (NodeTypeUtil.isType(resource, "nt:file")){
				prop = metaData.getContentType();
				if (prop.contains("text") || prop.contains("application")){
					InputStream prop2 = resource.adaptTo(InputStream.class);
					byte[] temp = new byte[(int)prop2.available()];
					prop2.read(temp);
					reply = new String(temp);
				}
			} else {
				reply = "non content node";
			}
		
		} catch (IOException e) {
			reply = "problem reading stream";
		}
		textArea.setText(reply);
		textArea.setCaretPosition(0);
		String syntax = null;
		if (prop != null){
			syntax = prop;
			syntax = syntax.replace("application/","text/");
		}
		//custom set
		textArea.setSyntaxEditingStyle(syntax);

	}

}
