package renderer;

/**
 * A special exception class for validation exceptions
 */
public class validationException extends Exception {
    public validationException() { super(); }
      public validationException(String message) { super(message); }
      public validationException(String message, Throwable cause) { super(message, cause); }
      public validationException(Throwable cause) { super(cause); }
}
