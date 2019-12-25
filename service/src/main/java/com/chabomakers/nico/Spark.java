package com.chabomakers.nico;

import javax.inject.Inject;
import javax.inject.Singleton;
import spark.ExceptionHandler;
import spark.Filter;
import spark.ResponseTransformer;

/** Wrapper for {@link spark.Spark}. */
@Singleton
public class Spark {

  @Inject
  Spark() {
    spark.Spark.port(8080);
  }

  /** @see spark.Spark#get(String, spark.Route, ResponseTransformer) */
  public void get(String path, spark.Route route, ResponseTransformer transformer) {
    spark.Spark.get(path, route, transformer);
  }

  /** @see spark.Spark#post(String, spark.Route, ResponseTransformer) */
  public void post(String path, spark.Route route, ResponseTransformer transformer) {
    spark.Spark.post(path, route, transformer);
  }

  /** @see spark.Spark#before(String, Filter) */
  public void before(String path, Filter filter) {
    spark.Spark.before(path, filter);
  }

  /** @see spark.Spark#afterAfter(String, Filter) */
  public void afterAfter(String path, Filter filter) {
    spark.Spark.afterAfter(path, filter);
  }

  /** @see spark.Spark#stop() */
  public void stop() {
    spark.Spark.stop();
  }

  /** @see spark.Spark#awaitStop() */
  public void awaitStop() {
    spark.Spark.awaitStop();
  }

  /** @see spark.Spark#exception(Class, ExceptionHandler) */
  public <T extends Exception> void exception(
      Class<T> exceptionClass, ExceptionHandler<? super T> handler) {
    spark.Spark.exception(exceptionClass, handler);
  }

  /** @see spark.Spark#staticFileLocation(String) */
  public void staticFileLocation(String s) {
    spark.Spark.staticFileLocation(s);
  }
}
