package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.gamestate.GameState;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class StartGameController implements Controller {

  private GameState gameState;

  @Inject
  public StartGameController(GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public String path() {
    return "/startgame";
  }

  @Override
  public GameStateResponse post(Request request, Response response) {
    gameState.startGame();
    return gameState.gameState();
  }
}
