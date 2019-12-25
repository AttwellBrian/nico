package com.chabomakers.nico;

import spark.Request;
import spark.Response;

/** Controllers handle server routing. */
public interface Controller {

  /**
   * Returns the controller URI path.
   *
   * @return the URI path.
   */
  String path();

  /**
   * Get.
   *
   * @param request the poll {@link Request}.
   * @param response the poll {@link Response}.
   * @return the poll response body.
   */
  default Object get(Request request, Response response) {
    throw invalidMethod();
  }

  /**
   * Post.
   *
   * @param request the peek {@link Request}.
   * @param response the peek {@link Response}.
   * @return the peek response body.
   */
  default Object post(Request request, Response response) {
    throw invalidMethod();
  }

  static BadRequestException invalidMethod() {
    return new BadRequestException("Invalid method for this URL.");
  }
}
