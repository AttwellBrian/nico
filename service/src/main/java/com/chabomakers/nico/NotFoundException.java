package com.chabomakers.nico;

/** Represents an HTTP status 404 result. */
public class NotFoundException extends RuntimeException {

  /**
   * Constructs a not-found exception with the specified detail message.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public NotFoundException(String message) {
    super(message);
  }
}
