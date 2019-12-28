package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.gamestate.GameStateMachine;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class GameStateController implements Controller {

  private GameStateMachine gameStateMachine;

  @Inject
  public GameStateController(GameStateMachine realDatabase) {
    this.gameStateMachine = realDatabase;
  }

  @Override
  public String path() {
    return "/state";
  }

  /** {@inheritDoc} */
  @Override
  public GameStateResponse get(Request request, Response response) {
    return gameStateMachine.gameState();
  }
}
