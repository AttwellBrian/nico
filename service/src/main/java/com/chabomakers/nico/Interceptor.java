package com.chabomakers.nico;

import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * {@link Interceptor} is a filter that is registered with the {@link spark.Spark#before(String,
 * Filter)} and {@link spark.Spark#afterAfter(String, Filter)} request hooks.
 */
public abstract class Interceptor {

  /**
   * Triggered before a request is executed.
   *
   * @param request The request.
   * @param response The response.
   */
  protected void before(Request request, Response response) {}

  /**
   * Triggered after all requests.
   *
   * @param request The request.
   * @param response The response.
   */
  protected void afterAfter(Request request, Response response) {}
}
