package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.gamestate.GameState;
import com.chabomakers.nico.gamestate.UserRow;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class CreateUserController implements Controller {

  private GameState gameState;

  @Inject
  public CreateUserController(GameState realDatabase) {
    this.gameState = realDatabase;
  }

  @Override
  public String path() {
    return "/createuser";
  }

  @Override
  public CreateUserResponse post(Request request, Response response) {
    String name = request.queryParams("name");
    String color = request.queryParams("color");
    UserRow user = gameState.createUser(name, color);
    return ImmutableCreateUserResponse.builder().user(user).build();
  }
}
