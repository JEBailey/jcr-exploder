package explorer.ide.table;

import java.lang.reflect.Array;
import java.util.Arrays;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;

@SuppressWarnings("serial")
@Component
@Service(value = JcrTableModelImpl.class)
public class JcrTableModelImpl extends AbstractTableModel {

	private Resource resource;

	private ValueMap map;

	private String[] propertyNames;

	private String[] headers = new String[] { "Name", "Type", "Value" };

	public void setResource(Resource resource) {
		this.resource = resource;
		this.map = ResourceUtil.getValueMap(resource);
		Object[] keys = map.keySet().toArray();
		this.propertyNames = new String[Array.getLength(keys)];
		for (int i = 0; i < keys.length; ++i) {
			propertyNames[i] = keys[i].toString();
		}
		Arrays.sort(propertyNames);
	}

	@Override
	public int getRowCount() {
		return propertyNames == null ? 0 : propertyNames.length;
	}

	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		StringBuilder reply = new StringBuilder();
		Node node = resource.adaptTo(Node.class);
		boolean hasNode = node != null;
		try {
			Property prop = null;
			if (hasNode) {
				prop = node.getProperty(propertyNames[rowIndex]);
			}
			switch (columnIndex) {
			case 0:
				reply.append(propertyNames[rowIndex]);
				break;
			case 1:
				if (hasNode) {
					reply.append(PropertyType.nameFromValue(prop.getType()));
					if (prop.isMultiple()) {
						reply.append("[]");
					}
				} else {
					Class<?> klass = map.get(propertyNames[rowIndex]).getClass();
					reply.append(klass.getSimpleName());
					if (klass.isArray()) {
						reply.append("[]");
					}
				}
				break;
			case 2:
				if (hasNode) {
					if (prop.getType() == PropertyType.BINARY) {
						return "binary";
					} else {
						if (prop.isMultiple()) {
							Value[] values = prop.getValues();
							int i = 0;
							reply.append("[");
							for (Value value : prop.getValues()) {
								if (i++ > 0) {
									reply.append(", ");
								}
								reply.append(value.getString());
							}
							reply.append("]");
						} else {
							reply.append(prop.getString());
						}
					}
				} else {
					reply.append(map.get(propertyNames[rowIndex], String.class));
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

}
