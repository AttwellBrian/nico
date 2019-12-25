package com.chabomakers.nico;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** i18n web server. */
@Singleton
public class Server {

  private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

  private final Spark spark;
  private final Set<Controller> controllers;
  private final InterceptorFilter filter;
  private final Gson gson;
  private final Database database;

  @Inject
  Server(
      Spark spark,
      Set<Controller> controllers,
      InterceptorFilter filter,
      Gson gson,
      Database database) {
    this.spark = spark;
    this.controllers = controllers;
    this.filter = filter;
    this.gson = gson;
    this.database = database;
  }

  /** Runs the server until termination. */
  void runBlocking() {
    start();
    spark.awaitStop();
  }

  /** Starts the server and returns. */
  public void start() {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  LOGGER.info("Stopping web server...");
                  spark.stop();
                  LOGGER.info("Server is no longer serving traffic.");
                }));

    database.initialize();

    spark.staticFileLocation("/release/");

    // Registers and enables all routing for the server.
    for (Controller controller : controllers) {
      String path = controller.path();
      LOGGER.info(
          "Registering path: "
              + path
              + " to controller "
              + controller.getClass().getCanonicalName());
      // Check the path for common developer errors.
      Preconditions.checkState(path.startsWith("/"), "Path should start with slash.");
      Preconditions.checkState(!path.endsWith("/"), "Path should NOT end with slash.");
      spark.before(path, filter);
      spark.afterAfter(path, filter);
      spark.get(path, controller::get, gson::toJson);
      spark.post(path, controller::post, gson::toJson);
      spark.exception(
          BadRequestException.class,
          (exception, request, response) -> {
            LOGGER.warn("BadRequest: {}", exception.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
            response.body(gson.toJson(exception));
          });
      spark.exception(
          NotFoundException.class,
          (exception, request, response) -> {
            LOGGER.warn("NotFound: {}", exception.getMessage());
            response.status(HttpStatus.NOT_FOUND_404);
            response.body(gson.toJson(exception));
          });
      spark.exception(
          Exception.class,
          (exception, request, response) -> {
            LOGGER.error("InternalError", exception);
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            response.body(exception.toString());
          });
      spark.exception(
          AuthenticationException.class,
          (exception, request, response) -> {
            LOGGER.error("Authentication failure: {}", exception.getMessage());
            response.status(HttpStatus.UNAUTHORIZED_401);
            response.body(exception.toString());
          });
    }
  }

  /** Stops the server. */
  public void stop() {
    spark.stop();
    spark.awaitStop();
  }
}
