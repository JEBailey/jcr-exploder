package explorer.commands;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Binary;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.apache.sling.api.resource.Resource;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import flack.commands.Command;
import flack.control.Event;

public class UpdateEditorPane implements Command {

	private RSyntaxTextArea textArea;
	
	public UpdateEditorPane(RSyntaxTextArea textArea) {
		super();
		this.textArea = textArea;
	}

	@Override
	public void process(Event event) {
		Resource node = (Resource)event.getData();
		String prop = null;
		String reply = "";
		try {
			if (node.isResourceType("nt:file")){
				prop = node.getChild("jcr:content/jcr:mimeType").adaptTo(String.class);
				if (prop.contains("text") || prop.contains("application")){
					InputStream prop2 = node.getChild("jcr:content/jcr:data").adaptTo(InputStream.class);
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
