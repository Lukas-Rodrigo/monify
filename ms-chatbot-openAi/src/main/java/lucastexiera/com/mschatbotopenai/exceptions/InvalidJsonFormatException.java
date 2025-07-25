package lucastexiera.com.mschatbotopenai.exceptions;

public class InvalidJsonFormatException extends RuntimeException {
  public InvalidJsonFormatException(String message, Throwable cause) {
    super(message, cause);
  }
}
