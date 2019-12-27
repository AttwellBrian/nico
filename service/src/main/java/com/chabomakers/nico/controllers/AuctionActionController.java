package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.RequestHelper;
import com.chabomakers.nico.gamestate.AuctionAction;
import com.chabomakers.nico.gamestate.GameStateMachine;
import com.google.gson.Gson;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

/**
 * To manually test this, use Postman with a body set to JSON type. The following body should work
 * as an example: <code>
 * {"userId":"ac4ffbf2-bb64-4a65-be25-831c768542d9","actionType":"PASS"}
 * </code>
 */
public class AuctionActionController implements Controller {

  private final GameStateMachine gameStateMachine;
  private final Gson gson;

  @Inject
  public AuctionActionController(GameStateMachine gameStateMachine, Gson gson) {
    this.gameStateMachine = gameStateMachine;
    this.gson = gson;
  }

  @Override
  public String path() {
    return "/auction/act";
  }

  @Override
  public Object post(Request request, Response response) {
    AuctionAction auctionAction = RequestHelper.parseBody(gson, request, AuctionAction.class);
    gameStateMachine.performAuctionAction(auctionAction);
    return gameStateMachine.gameState();
  }
}
