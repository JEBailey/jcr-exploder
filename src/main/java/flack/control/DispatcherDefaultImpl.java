package flack.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import flack.control.api.Dispatcher;
import flack.control.api.Event;
import flack.control.api.EventController;

@Component(name="Dispatcher - Default Impl")
@Service
public class DispatcherDefaultImpl implements Dispatcher {

	List<EventController> eventControllers;
	
	private Map<Class<?>, List<EventController>> handlers;

	public DispatcherDefaultImpl() {
		handlers = new HashMap<Class<?>, List<EventController>>();
	}

	/* (non-Javadoc)
	 * @see flack.control.Dispatcher#addController(java.lang.Class, flack.control.EventControllerDefaultImpl)
	 */
	
	public void addController(Class<?> type, EventController listener) {
		List<EventController> listeners = handlers.get(type);
		if (listeners == null) {
			listeners = new ArrayList<EventController>();
			handlers.put(type, listeners);
		}
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see flack.control.Dispatcher#removeController(java.lang.Class, flack.control.EventControllerDefaultImpl)
	 */
	public boolean removeController(Class<?> type, EventController listener) {
		List<EventController> listeners = handlers.get(type);
		if (listeners != null) {
			return listeners.remove(listener);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see flack.control.Dispatcher#dispatchEvent(flack.control.EventDefaultImpl)
	 */
	
	public boolean dispatchEvent(Event event) {
		List<EventController> listeners = handlers.get(event.getClass());
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
	public void asynchEvent(final Event event) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dispatchEvent(event);
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
