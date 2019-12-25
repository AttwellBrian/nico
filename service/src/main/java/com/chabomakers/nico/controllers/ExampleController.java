package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class ExampleController implements Controller {

  @Inject
  public ExampleController() {}

  @Override
  public String path() {
    return "/example";
  }

  /** {@inheritDoc} */
  @Override
  public ExampleResponse get(Request request, Response response) {
    return ImmutableExampleResponse.builder()
        .addItems("one")
        .addItems("two")
        .description("this is an example response")
        .build();
  }
}
