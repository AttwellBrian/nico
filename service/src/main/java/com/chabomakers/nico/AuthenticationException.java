package com.chabomakers.nico;

/** Represents an HTTP status 401 result. */
public class AuthenticationException extends RuntimeException {

  /**
   * Constructs a authentication exception with the specified detail message.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public AuthenticationException(String message) {
    super(message);
  }
}
