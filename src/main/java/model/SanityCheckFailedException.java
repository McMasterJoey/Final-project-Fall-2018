package model;
/**
 * An exception that is thrown when a sanity check is failed.
 * @author Joey McMaster
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