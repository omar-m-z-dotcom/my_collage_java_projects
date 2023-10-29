package interfaces;

/**
 * 
 * used to handel the run time errors if a socket excption has happened
 *
 */
public interface RunTimeErrorInterface {
	/**
	 * 
	 * @param e the Exception thrown when a socket error has has happened at
	 *          run time
	 */
	public void handleRunTimeError(Exception e);
}
