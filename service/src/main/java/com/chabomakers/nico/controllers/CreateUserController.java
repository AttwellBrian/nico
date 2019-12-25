package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.database.MemoryDatabase;
import com.chabomakers.nico.database.UserRow;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class CreateUserController implements Controller {

  private MemoryDatabase memoryDatabase;

  @Inject
  public CreateUserController(MemoryDatabase realDatabase) {
    this.memoryDatabase = realDatabase;
  }

  @Override
  public String path() {
    return "/createuser";
  }

  @Override
  public CreateUserResponse post(Request request, Response response) {
    String name = request.queryParams("name");
    String color = request.queryParams("color");
    UserRow user = memoryDatabase.createUser(name, color);
    return ImmutableCreateUserResponse.builder().user(user).build();
  }
}
