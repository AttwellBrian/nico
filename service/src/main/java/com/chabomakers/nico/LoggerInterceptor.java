package com.chabomakers.nico;

import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

/** An interceptor that logs server request and responses, and emits metrics. */
class LoggerInterceptor extends Interceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoggerInterceptor.class);

  private final Clock clock = Clock.systemUTC();
  private final Map<Request, Long> timestamps = new ConcurrentHashMap<>();

  @Inject
  LoggerInterceptor() {}

  @Override
  protected void before(Request request, Response response) {
    LOGGER.info(
        "RequestStart: METHOD={} URL={} QUERY={} BODY={}",
        request.requestMethod(),
        request.url(),
        request.queryString(),
        request.body());
    timestamps.put(request, clock.millis());
  }

  @Override
  protected void afterAfter(Request request, Response response) {
    Long start = timestamps.remove(request);
    if (start == null) {
      return;
    }
    long latencyMs = clock.millis() - start;
    String url = request.url();
    String method = request.requestMethod();
    int status = response.status();

    if (status / 100 == 2) {
      LOGGER.info(
          "RequestEnd: METHOD={} URL={} STATUS={} LATENCY={}ms ", method, url, status, latencyMs);
    } else {
      LOGGER.warn(
          "RequestEnd: METHOD={} URL={} STATUS={} LATENCY={}ms", method, url, status, latencyMs);
    }
  }
}
