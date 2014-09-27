package explorer.ide;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

@Component(name="Sling Explorer UI - Text Editor",description="Provides a text editor view")
@Service(value=TextArea.class)
public class TextArea extends RSyntaxTextArea {

	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;

	public TextArea() {
	}

	public TextArea(RSyntaxDocument doc) {
		super(doc);
	}

	public TextArea(String text) {
		super(text);
	}

	public TextArea(int textMode) {
		super(textMode);
	}

	public TextArea(int rows, int cols) {
		super(rows, cols);
	}

	public TextArea(String text, int rows, int cols) {
		super(text, rows, cols);
	}

	public TextArea(RSyntaxDocument doc, String text, int rows, int cols) {
		super(doc, text, rows, cols);
	}

}
