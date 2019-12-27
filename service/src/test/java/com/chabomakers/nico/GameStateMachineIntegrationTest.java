package com.chabomakers.nico;

import com.chabomakers.nico.controllers.GameStateResponse;
import com.chabomakers.nico.gamestate.AuctionAction.ActionType;
import com.chabomakers.nico.gamestate.GameStateMachine;
import com.chabomakers.nico.gamestate.GameStateMachine.GamePhase;
import com.chabomakers.nico.gamestate.ImmutableAuctionAction;
import com.chabomakers.nico.gamestate.PowerPlantCard;
import com.google.common.truth.Truth;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateMachineIntegrationTest {

  private GameStateMachine gameStateMachine;

  @BeforeEach
  void setup() {
    ServerComponent serverComponent = DaggerServerComponent.create();
    gameStateMachine = serverComponent.memoryDatabase();
  }

  @Test
  void auction_bothUsersPass() {
    gameStateMachine.createUser("user1", "red");
    gameStateMachine.createUser("user2", "blue");
    gameStateMachine.createUser("user3", "green");

    GameStateResponse gameStateResponse = gameStateMachine.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.LOBBY);
    Assertions.assertEquals(gameStateResponse.users().size(), 3);

    gameStateMachine.startGame();
    gameStateResponse = gameStateMachine.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.AUCTION_PICK_PLANT);
    Assertions.assertEquals(gameStateResponse.currentPlayerOrder().size(), 3);

    UUID player1Id = gameStateResponse.currentPlayerOrder().get(0);
    UUID player2Id = gameStateResponse.currentPlayerOrder().get(1);
    UUID player3Id = gameStateResponse.currentPlayerOrder().get(2);

    //
    // FIRST USER PICKS A PLANT AND OTHER USER'S PASS ON IT
    //

    PowerPlantCard secondPowerPlantCard = gameStateResponse.actualMarket().get(1);
    ImmutableAuctionAction auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.CHOOSE_PLANT)
            .bid(5)
            .userId(player1Id)
            .choosePlantId(secondPowerPlantCard.id())
            .build();
    // Assert that the actual market has the card we're bidding on, before we bid on it.
    Assertions.assertTrue(
        gameStateResponse
            .actualMarket()
            .stream()
            .map(PowerPlantCard::minimumAcceptableBid)
            .collect(Collectors.toList())
            .contains(2));

    gameStateMachine.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameStateMachine.gamePhase(), GamePhase.AUCTION_BIDDING);
    Assertions.assertEquals(gameStateMachine.gameState().currentUser(), player2Id);
    Assertions.assertEquals(gameStateMachine.gameState().userWithHighestPowerplantBid(), player1Id);
    Truth.assertThat(gameStateMachine.gameState().usersPassedFromBidding()).containsExactly();

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player2Id).build();
    gameStateMachine.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameStateMachine.gameState().currentUser(), player3Id);
    Assertions.assertEquals(player1Id, gameStateMachine.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(gameStateMachine.gameState().usersPassedFromBidding())
        .containsExactly(player2Id);

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player3Id).build();
    gameStateMachine.performAuctionAction(auctionAction);

    // Since both users passed, we are now onto the phase where the second user gets to
    // choose a plant to begin bidding on.
    Assertions.assertEquals(gameStateMachine.gamePhase(), GamePhase.AUCTION_PICK_PLANT);
    Map<UUID, List<PowerPlantCard>> userPowerPlants =
        gameStateMachine.gameState().userPowerPlants();
    Assertions.assertEquals(userPowerPlants.get(player1Id).size(), 1);
  }

  @Test
  void auction_secondUserWins() {
    gameStateMachine.createUser("user1", "red");
    gameStateMachine.createUser("user2", "blue");
    gameStateMachine.createUser("user3", "green");

    GameStateResponse gameStateResponse = gameStateMachine.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.LOBBY);
    Assertions.assertEquals(gameStateResponse.users().size(), 3);

    gameStateMachine.startGame();
    gameStateResponse = gameStateMachine.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.AUCTION_PICK_PLANT);
    Assertions.assertEquals(gameStateResponse.currentPlayerOrder().size(), 3);

    UUID player1Id = gameStateResponse.currentPlayerOrder().get(0);
    UUID player2Id = gameStateResponse.currentPlayerOrder().get(1);
    UUID player3Id = gameStateResponse.currentPlayerOrder().get(2);

    //
    // FIRST USER PICKS A PLANT
    //

    PowerPlantCard secondPowerPlantCard = gameStateResponse.actualMarket().get(1);
    ImmutableAuctionAction auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.CHOOSE_PLANT)
            .bid(5)
            .userId(player1Id)
            .choosePlantId(secondPowerPlantCard.id())
            .build();
    // Assert that the actual market has the card we're bidding on, before we bid on it.
    Assertions.assertTrue(
        gameStateResponse
            .actualMarket()
            .stream()
            .map(PowerPlantCard::minimumAcceptableBid)
            .collect(Collectors.toList())
            .contains(2));

    gameStateMachine.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameStateMachine.gamePhase(), GamePhase.AUCTION_BIDDING);
    Assertions.assertEquals(gameStateMachine.gameState().currentUser(), player2Id);
    Assertions.assertEquals(player1Id, gameStateMachine.gameState().userWithHighestPowerplantBid());

    //
    // SECOND USER OUTBIDS
    //

    auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.BID)
            .bid(6)
            .userId(player2Id)
            .build();
    gameStateMachine.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameStateMachine.gameState().currentUser(), player3Id);
    Assertions.assertEquals(gameStateMachine.gameState().currentPowerPlantBid(), 6);
    Assertions.assertEquals(player2Id, gameStateMachine.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(gameStateMachine.gameState().usersPassedFromBidding()).containsExactly();

    //
    // THIRD AND FIRST USER PASS ON THE BID
    //

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player3Id).build();
    gameStateMachine.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameStateMachine.gameState().currentUser(), player1Id);
    Assertions.assertEquals(player2Id, gameStateMachine.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(gameStateMachine.gameState().usersPassedFromBidding())
        .containsExactly(player3Id);

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player1Id).build();
    gameStateMachine.performAuctionAction(auctionAction);
    Assertions.assertNull(gameStateMachine.gameState().userWithHighestPowerplantBid());

    // Since the first user passsed on their second round of bidding, user two wins the auction.
    Assertions.assertEquals(gameStateMachine.gamePhase(), GamePhase.AUCTION_PICK_PLANT);
    Map<UUID, List<PowerPlantCard>> userPowerPlants =
        gameStateMachine.gameState().userPowerPlants();
    Assertions.assertEquals(userPowerPlants.get(player2Id).size(), 1);
  }
}
