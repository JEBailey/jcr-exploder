package explorer.filehandlers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import explorer.core.api.MimeProvider;

@org.apache.felix.scr.annotations.Component(immediate=true, metatype=true,label="Sling GUI Explorer - Editor Options", name="Sling Explorer Renderer - Text Files",description="Provides the UI for Text Files")
@Service
@Property(name="mimeType",label="Supported MimeTypes",description="Mimetypes that this renderer will accept",value={"text/css","text/x-java-source","text/html","application/esp","text/javascript","application/javascript"})
public class EditorView implements MimeProvider {

	@Override
	public Component createComponent(Resource resource, String syntax) {
		String content = "";
		try {
			InputStream prop2 = resource.adaptTo(InputStream.class);
			byte[] temp = new byte[(int) prop2.available()];

			prop2.read(temp);
			content = new String(temp);
		} catch (IOException e) {
			content = "problem reading stream";
		}
		 
		RSyntaxTextArea editorTextArea = new RSyntaxTextArea(RSyntaxTextArea.INSERT_MODE);
		//TextEditorPane editorTextArea = new TextEditorPane(RSyntaxTextArea.INSERT_MODE);
		editorTextArea.setAntiAliasingEnabled(true);
		editorTextArea.setEditable(true);
		editorTextArea.setText(content);
		editorTextArea.setCaretPosition(0);
		editorTextArea.setCodeFoldingEnabled(true);
		editorTextArea.setEditable(true);
		editorTextArea.setSyntaxEditingStyle(normalize(syntax));
		RTextScrollPane editorScrollPane = new RTextScrollPane(editorTextArea);
		
		JToolBar bar = new JToolBar();
		bar.add(Box.createHorizontalGlue());
        bar.add(new JButton("save"));
        bar.add(new JButton("edit"));
		bar.setFloatable(false);
		
		JPanel pane = new JPanel();
        BorderLayout bord = new BorderLayout();
        pane.setLayout(bord);
        pane.add("North", bar);
        pane.add("Center", editorScrollPane);
		
		return pane;
	}
	
	private String normalize(String syntax){
		return syntax.replace("x-", "").replace("-source","").replace("application", "text");
	}

}
