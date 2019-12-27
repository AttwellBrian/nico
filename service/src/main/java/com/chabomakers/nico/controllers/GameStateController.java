package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.gamestate.GameState;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class GameStateController implements Controller {

  private GameState gameState;

  @Inject
  public GameStateController(GameState realDatabase) {
    this.gameState = realDatabase;
  }

  @Override
  public String path() {
    return "/state";
  }

  /** {@inheritDoc} */
  @Override
  public GameStateResponse get(Request request, Response response) {
    return gameState.gameState();
  }
}
