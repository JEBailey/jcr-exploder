package explorer.filehandlers;

import java.awt.Component;

import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import explorer.core.api.MimeProvider;

@org.apache.felix.scr.annotations.Component(name="Sling Explorer Renderer - Text Files",description="Provides the UI for Text Files")
@Service
@Property(name="mimeType",value="text/*")
public class EditorView implements MimeProvider {

	@Override
	public Component render(Resource resource) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private java.awt.Component addEditor(String title,String content, String syntax) {
		RSyntaxTextArea editorTextArea = new RSyntaxTextArea(RSyntaxTextArea.INSERT_MODE);
		editorTextArea.setAntiAliasingEnabled(true);
		editorTextArea.setEditable(true);
		editorTextArea.setText(content);
		editorTextArea.setCaretPosition(0);
		editorTextArea.setCodeFoldingEnabled(true);
		editorTextArea.setEditable(true);
		editorTextArea.setSyntaxEditingStyle(syntax);
		RTextScrollPane editorScrollPane = new RTextScrollPane(editorTextArea);

		return editorScrollPane;
	}

}
