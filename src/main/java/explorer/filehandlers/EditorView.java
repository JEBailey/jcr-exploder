/*
Copyright 2016 JE Bailey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package explorer.filehandlers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Session;
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
import explorer.node.NodeTypeUtil;

@org.apache.felix.scr.annotations.Component(immediate = true, metatype = true, label = "Sling GUI Explorer - Editor Options", name = "Sling Explorer Renderer - Text Files")
@Service
@Property(name = "mimeType", label = "Supported MimeTypes", description = "Mimetypes that this renderer will accept", value = {
		"text/css", "text/x-java-source", "text/html", "application/esp",
		"text/javascript", "application/javascript" })
public class EditorView implements MimeProvider {

	@Override
	public Component createComponent(final Resource resource, String syntax) {
		String content = "";
		try {
			InputStream prop2 = resource.adaptTo(InputStream.class);
			byte[] temp = new byte[(int) prop2.available()];

			prop2.read(temp);
			content = new String(temp);
		} catch (IOException e) {
			content = "problem reading stream";
		}

		final RSyntaxTextArea editorTextArea = new RSyntaxTextArea(
				RSyntaxTextArea.INSERT_MODE);
		// TextEditorPane editorTextArea = new
		// TextEditorPane(RSyntaxTextArea.INSERT_MODE);
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
		bar.add(new JButton("Save") {
			boolean playState = true;
			{
				setFocusable(false);
				// setIcon(pause);
				addActionListener(evt -> {
					String content = editorTextArea.getText();
					if (NodeTypeUtil.isType(resource, "nt:file")) {
						Resource nodeContent = resource.getChild("jcr:content");
						if (nodeContent != null) {
							try {
								Node node = nodeContent.adaptTo(Node.class);
								Session session = node.getSession();
								Binary binary = session.getValueFactory()
										.createBinary(
												new ByteArrayInputStream(
														content.getBytes()));
								node.setProperty("jcr:data", binary);
								session.save();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					if (playState) {
						// setIcon(play);
					} else {
						// setIcon(pause);
					}
					// vb.setPause(playState);
					playState = !playState;
				});
			}
		});
		bar.add(new JButton("edit"));
		bar.setFloatable(false);

		JPanel pane = new JPanel();
		BorderLayout bord = new BorderLayout();
		pane.setLayout(bord);
		pane.add("North", bar);
		pane.add("Center", editorScrollPane);

		return pane;
	}

	private String normalize(String syntax) {
		return syntax.replace("x-", "").replace("-source", "")
				.replace("application", "text");
	}

}
