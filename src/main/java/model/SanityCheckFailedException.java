package model;
/**
 * An exception that is thrown when a sanity check is failed.
 * @author Joey McMaster
 *
 */
public class SanityCheckFailedException extends RuntimeException {
	  public SanityCheckFailedException(String message) {
	    super(message);
	  }
}