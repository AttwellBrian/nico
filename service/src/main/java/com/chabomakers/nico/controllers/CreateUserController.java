package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.gamestate.GameStateMachine;
import com.chabomakers.nico.gamestate.UserRow;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class CreateUserController implements Controller {

  private GameStateMachine gameStateMachine;

  @Inject
  public CreateUserController(GameStateMachine realDatabase) {
    this.gameStateMachine = realDatabase;
  }

  @Override
  public String path() {
    return "/createuser";
  }

  @Override
  public CreateUserResponse post(Request request, Response response) {
    String name = request.queryParams("name");
    String color = request.queryParams("color");
    UserRow user = gameStateMachine.createUser(name, color);
    return ImmutableCreateUserResponse.builder().user(user).build();
  }
}
