package com.chabomakers.nico;

/** Represents an HTTP status 400 result. */
public class BadRequestException extends RuntimeException {

  /**
   * Constructs a bad-request exception with the specified detail message.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public BadRequestException(String message) {
    super(message);
  }
}
