package flack.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dispatcher {

	private static Dispatcher instance;

	private Map<Class<?>, List<EventController>> handlers;

	public static Dispatcher getInstance() {
		if (instance == null) {
			instance = new Dispatcher();
		}
		return instance;
	}

	public Dispatcher() {
		handlers = new HashMap<Class<?>, List<EventController>>();
	}

	/**
	 * Adds an event controller.
	 */
	public void addController(Class<?> type, EventController listener) {
		List<EventController> listeners = handlers.get(type);
		if (listeners == null) {
			listeners = new ArrayList<EventController>();
			handlers.put(type, listeners);
		}
		listeners.add(listener);
	}

	/**
	 * Removes an event controller.
	 */
	public boolean removeController(Class<?> type, EventController listener) {
		List<EventController> listeners = handlers.get(type);
		if (listeners != null) {
			return listeners.remove(listener);
		}
		return false;
	}

	/**
	 * Dispatches a event.
	 */
	public boolean dispatchEvent(Event event) {
		List<EventController> listeners = handlers.get(event.getClass());
		for (EventController listener:listeners){
			listener.executeCommand(event);
		}
		return false;
		// return eventDispatcher.dispatchEvent( event );
	}

	/**
	 * Returns whether an event controller exists.
	 */
	public boolean hasController(String type) {
		return handlers.get(type) != null;
	}

}
