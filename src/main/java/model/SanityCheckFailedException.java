package model;
/**
 * An exception that is thrown when a sanity check is failed.
 * @author Joey McMaster
 * @author Linjie liu
 * @author Nicholas Fiegel
 * @author Wes Rodgers
 *
 */
public class SanityCheckFailedException extends RuntimeException {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SanityCheckFailedException(String message) {
	    super(message);
	  }
}