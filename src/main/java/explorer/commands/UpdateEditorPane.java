package explorer.commands;

import java.io.IOException;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

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
		Node node = (Node)event.getData();
		Property prop = null;
		String reply = "";
		try {
			if (node.isNodeType("nt:file")){
				prop = node.getProperty("jcr:content/jcr:mimeType");
				if (prop.getString().contains("text") || prop.getString().contains("application")){
					Property prop2 = node.getProperty("jcr:content/jcr:data");
					Binary binary = prop2.getBinary();
					byte[] temp = new byte[(int)binary.getSize()];
					binary.read(temp, 0);
					reply = new String(temp);
				}
			} else {
				reply = "non content node";
			}
		} catch (PathNotFoundException e) {
			reply = "non-conforming node";
		} catch (RepositoryException e) {
			reply = "problem occured in repository";
		} catch (IOException e) {
			reply = "problem reading stream";
		}
		textArea.setText(reply);
		textArea.setCaretPosition(0);
		String syntax = null;
		if (prop != null){
			try {
				syntax = prop.getString();
				syntax = syntax.replace("application/","text/");
			} catch (ValueFormatException e) {
			} catch (RepositoryException e) {

			}
		}
		//custom set
		textArea.setSyntaxEditingStyle(syntax);

	}

}
