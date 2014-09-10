package flack.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import flack.control.api.Dispatcher;
import flack.control.api.EventController;

public class DispatcherDefaultImpl implements Dispatcher {

	private static Dispatcher instance;

	private Map<Class<?>, List<EventControllerDefaultImpl>> handlers;

	public static Dispatcher getInstance() {
		if (instance == null) {
			instance = new DispatcherDefaultImpl();
		}
		return instance;
	}

	public DispatcherDefaultImpl() {
		handlers = new HashMap<Class<?>, List<EventControllerDefaultImpl>>();
	}

	/* (non-Javadoc)
	 * @see flack.control.Dispatcher#addController(java.lang.Class, flack.control.EventControllerDefaultImpl)
	 */
	@Override
	public void addController(Class<?> type, EventControllerDefaultImpl listener) {
		List<EventControllerDefaultImpl> listeners = handlers.get(type);
		if (listeners == null) {
			listeners = new ArrayList<EventControllerDefaultImpl>();
			handlers.put(type, listeners);
		}
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see flack.control.Dispatcher#removeController(java.lang.Class, flack.control.EventControllerDefaultImpl)
	 */
	@Override
	public boolean removeController(Class<?> type, EventController listener) {
		List<EventControllerDefaultImpl> listeners = handlers.get(type);
		if (listeners != null) {
			return listeners.remove(listener);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see flack.control.Dispatcher#dispatchEvent(flack.control.EventDefaultImpl)
	 */
	@Override
	public boolean dispatchEvent(EventDefaultImpl event) {
		List<EventControllerDefaultImpl> listeners = handlers.get(event.getClass());
		for (EventController listener:listeners){
			listener.executeCommand(event);
		}
		return false;
		// return eventDispatcher.dispatchEvent( event );
	}
	
	/* (non-Javadoc)
	 * @see flack.control.Dispatcher#asynchEvent(flack.control.EventDefaultImpl)
	 */
	@Override
	public void asynchEvent(final EventDefaultImpl event) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				DispatcherDefaultImpl.getInstance().dispatchEvent(event);
				
			}
		});
	}

	/* (non-Javadoc)
	 * @see flack.control.Dispatcher#hasController(java.lang.String)
	 */
	@Override
	public boolean hasController(String type) {
		return handlers.get(type) != null;
	}

}
