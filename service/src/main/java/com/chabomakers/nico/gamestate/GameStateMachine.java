package com.chabomakers.nico.gamestate;

import com.chabomakers.nico.controllers.GameStateResponse;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameStateMachine {

  private GameStateMachineImpl impl;

  @Inject
  public GameStateMachine() {
    impl = new GameStateMachineImpl();
  }

  public synchronized void resetGame() {
    impl = new GameStateMachineImpl();
  }

  public synchronized void performAuctionAction(AuctionAction action) {
    impl.performAuctionAction(action);
  }

  public synchronized UserRow createUser(String username, String color) {
    return impl.createUser(username, color);
  }

  public synchronized GamePhase gamePhase() {
    return impl.gamePhase();
  }

  public synchronized GameStateResponse gameState() {
    return impl.gameState();
  }

  public synchronized void startGame() {
    impl.startGame();
  }

  public enum GamePhase {
    LOBBY,
    AUCTION_PICK_PLANT,
    AUCTION_BIDDING,
    BUYING_RESOURCES
  }
}
