package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.gamestate.GameStateMachine;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class StartGameController implements Controller {

  private GameStateMachine gameStateMachine;

  @Inject
  public StartGameController(GameStateMachine gameStateMachine) {
    this.gameStateMachine = gameStateMachine;
  }

  @Override
  public String path() {
    return "/startgame";
  }

  @Override
  public GameStateResponse post(Request request, Response response) {
    gameStateMachine.startGame();
    return gameStateMachine.gameState();
  }
}
