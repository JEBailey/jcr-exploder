package explorer.ide.tree;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class JcrTableModelImpl extends AbstractTableModel {

	private Node node;
	
	private String[] headers = new String[] {
			"Name", "Type", "Value"
		};
	
	@Override
	public int getRowCount() {
		try {
			return (int)node.getProperties().getSize();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String reply = "";
		try {
			PropertyIterator iter = node.getProperties();
			iter.skip(rowIndex);
			Property prop = iter.nextProperty();

			switch (columnIndex){
			case 0:
				reply = prop.getName();
				break;
			case 1:
				reply = PropertyType.nameFromValue(prop.getType());
				break;
			case 2:
				if (prop.getType() == PropertyType.BINARY){
					return "binary";
				} else {
					reply = prop.getString();
				}
			}
			
		} catch (RepositoryException e) {
		}
		return reply;
	}

	@Override
	public String getColumnName(int column) {
		return headers[column];
	}
	
	public void updateModel(Node node){
		this.node = node;
		fireTableDataChanged();
	}
	
	

}
