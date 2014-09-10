package flack.control;

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

	public abstract void asynchEvent(EventDefaultImpl event);

	/**
	 * Returns whether an event controller exists.
	 */
	public abstract boolean hasController(String type);

}