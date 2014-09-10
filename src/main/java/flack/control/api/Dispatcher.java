package flack.control.api;

import flack.control.EventControllerDefaultImpl;
import flack.control.EventDefaultImpl;

public interface Dispatcher {

	/**
	 * Adds an event controller.
	 */
	public abstract void addController(Class<?> type, EventControllerDefaultImpl listener);

	/**
	 * Removes an event controller.
	 */
	public abstract boolean removeController(Class<?> type, EventController listener);

	/**
	 * Dispatches an event.
	 */
	public abstract boolean dispatchEvent(EventDefaultImpl event);

	
	/**
	 * Dispatches event asynchronously into the SwingInvoker
	 * 
	 * @param event
	 */
	public abstract void asynchEvent(EventDefaultImpl event);

	/**
	 * Returns whether an event controller exists.
	 */
	public abstract boolean hasController(String type);

}