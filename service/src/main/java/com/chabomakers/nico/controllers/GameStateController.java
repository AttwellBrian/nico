package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.database.MemoryDatabase;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class GameStateController implements Controller {

  private MemoryDatabase memoryDatabase;

  @Inject
  public GameStateController(MemoryDatabase realDatabase) {
    this.memoryDatabase = realDatabase;
  }

  @Override
  public String path() {
    return "/state";
  }

  /** {@inheritDoc} */
  @Override
  public GameStateResponse get(Request request, Response response) {
    return memoryDatabase.gameState();
  }
}
