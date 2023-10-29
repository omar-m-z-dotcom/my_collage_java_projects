package interfaces;

/**
 * 
 * used to handel the constructor errors if a socket excption has happened
 *
 */
public interface ConstructErrorInterface {
	/**
	 * 
	 * @param e the Exception thrown when a socket error has has happened at
	 *          construction time
	 */
	public void handleConstructError(Exception e);
}
