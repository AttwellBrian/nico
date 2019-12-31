package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.gamestate.GameStateMachine;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class ResetController implements Controller {

  private final GameStateMachine gameStateMachine;

  @Inject
  public ResetController(GameStateMachine gameStateMachine) {
    this.gameStateMachine = gameStateMachine;
  }

  @Override
  public String path() {
    return "/reset";
  }

  @Override
  public GameStateResponse post(Request request, Response response) {
    gameStateMachine.resetGame();
    return gameStateMachine.gameState();
  }
}
