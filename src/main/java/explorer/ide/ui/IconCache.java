package explorer.ide.ui;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IconCache {


	public enum Type { application, code, css, db, doc, esp, file, folder, folder_open, gif, html, node_select_child, png, txt}
	
    // cache for our icons
    private static Map<Type,Icon> mIconCache= new EnumMap<Type,Icon>(Type.class);
    private static final Logger log = LoggerFactory
			.getLogger(IconCache.class);
    /**
     * Loads an Icon for the handed (relative) name and modifier.
     * If no icon is found, null is returned.
     */
    public static Icon getIcon(Type type){
    	Icon reply = mIconCache.get(type);
  
        if (reply == null) {
        	try {
        		URL url= IconCache.class.getResource("/images/"+type.toString()+".png");
        		if (url != null){
            		reply = new ImageIcon(url);
            		mIconCache.put(type, reply);
            	}
        	} catch (Exception e) {
        		log.error(e.getMessage());
        	}
        }

        return reply;
    }
    
    
    
}
