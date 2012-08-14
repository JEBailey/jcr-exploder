package flack.control;

import java.util.EventObject;

@SuppressWarnings("serial")
public class Event extends EventObject {
	
	private Object data;

	public Event(Object source) {
		super(source);
	}
	
	public Event(Object source, Object data) {
		super(source);
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	

}
