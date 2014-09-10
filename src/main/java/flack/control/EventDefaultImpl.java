package flack.control;

import java.util.EventObject;

import flack.control.api.Event;

@SuppressWarnings("serial")
public class EventDefaultImpl extends EventObject implements Event {
	
	private Object data;

	public EventDefaultImpl(Object source) {
		super(source);
	}
	
	public EventDefaultImpl(Object source, Object data) {
		super(source);
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see flack.control.Event#getData()
	 */
	@Override
	public Object getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see flack.control.Event#setData(java.lang.Object)
	 */
	@Override
	public void setData(Object data) {
		this.data = data;
	}
	

}
