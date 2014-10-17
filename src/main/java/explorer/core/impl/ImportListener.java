package explorer.core.impl;

import java.awt.EventQueue;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.contentloader.ContentImportListener;

import explorer.core.api.ResourceTreeModel;
import explorer.core.api.SessionProvider;

@Component
@Service
public class ImportListener implements ContentImportListener {

	EventQueue queue;
	
	@Reference
	ResourceTreeModel model;
	
	@Reference
	JTree jcrTree;
	
	@Reference
	SessionProvider provider;
	
	public void activate(){
		queue = java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue();
	}
	
	
	@Override
	public void onCheckin(String srcPath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckout(String srcPath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCopy(String srcPath, String destPath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(String srcPath) {
		TreePath path = jcrTree.getSelectionPath();
		model.valueForPathChanged(path, null);
	}

	@Override
	public void onDelete(String srcPath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onModify(String srcPath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMove(String srcPath, String destPath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReorder(String orderedPath, String beforeSibbling) {
		// TODO Auto-generated method stub

	}

}
