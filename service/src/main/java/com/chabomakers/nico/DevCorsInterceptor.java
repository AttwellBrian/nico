package com.chabomakers.nico;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

/** An interceptor that adds CORS headers to enable development on localhost. */
class DevCorsInterceptor extends Interceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(DevCorsInterceptor.class);

  @Inject
  DevCorsInterceptor() {}

  @Override
  protected void afterAfter(Request request, Response response) {
    if (request.host().startsWith("localhost:")) {
      LOGGER.info("Adding CORS headers.");
      response.header("Access-Control-Allow-Origin", "http://localhost:3000");
      response.header("Access-Control-Allow-Methods", "*");
    }
  }
}
