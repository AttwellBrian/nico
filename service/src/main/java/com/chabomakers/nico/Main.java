package com.chabomakers.nico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  /** Entrypoint to application. */
  public static void main(String[] args) {
    // All servers should contain the following. This should be extracted into a logging library.
    LOGGER.info("Register uncaught exception handler.");
    Thread.setDefaultUncaughtExceptionHandler(
        (t, e) -> {
          LOGGER.error("Uncaught exception in thread " + t.getName(), e);

          // Fast fail when an exception occurs on any thread that doesn't handle uncaught
          // exceptions. We don't want to enter a situation where this server is
          // silently failing to operate correctly. See D2821 for discussion.
          System.exit(1);
        });

    ServerComponent serverComponent = DaggerServerComponent.builder().build();
    serverComponent.server().runBlocking();
  }
}
