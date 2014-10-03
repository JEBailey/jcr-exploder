package explorer.filehandlers;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

import explorer.core.api.MimeProvider;

@org.apache.felix.scr.annotations.Component(immediate=true,name="Sling Explorer Renderer - Graphic Files",description="Provides the UI for Image Files")
@Service
@Property(name="mimeType",value={"image/png","image/gif","image/jpeg"})
public class ImageView implements MimeProvider {

	@Override
	public Component createComponent(Resource resource, String syntax) {
		byte[] temp;
		try {
			InputStream prop2 = resource.adaptTo(InputStream.class);
			temp = new byte[(int) prop2.available()];
			prop2.read(temp);
			return new JScrollPane(new JLabel(new ImageIcon(temp))); 
		} catch (IOException e) {
			return null;
		}
	}

}
