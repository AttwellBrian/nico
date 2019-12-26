package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.database.MemoryDatabase;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class StartGameController implements Controller {

  private MemoryDatabase memoryDatabase;

  @Inject
  public StartGameController(MemoryDatabase memoryDatabase) {
    this.memoryDatabase = memoryDatabase;
  }

  @Override
  public String path() {
    return "/startgame";
  }

  @Override
  public GameStateResponse post(Request request, Response response) {
    memoryDatabase.startGame();
    return memoryDatabase.gameState();
  }
}
