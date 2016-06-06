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

@org.apache.felix.scr.annotations.Component(immediate=true,name="Sling Explorer Renderer - Graphic Files")
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
