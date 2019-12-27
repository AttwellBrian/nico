package com.chabomakers.nico;

import com.chabomakers.nico.controllers.GameStateResponse;
import com.chabomakers.nico.gamestate.AuctionAction.ActionType;
import com.chabomakers.nico.gamestate.GameState;
import com.chabomakers.nico.gamestate.GameState.GamePhase;
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

public class GameStateIntegrationTest {

  private GameState gameState;

  @BeforeEach
  void setup() {
    ServerComponent serverComponent = DaggerServerComponent.create();
    gameState = serverComponent.memoryDatabase();
  }

  @Test
  void auction_bothUsersPass() {
    gameState.createUser("user1", "red");
    gameState.createUser("user2", "blue");
    gameState.createUser("user3", "green");

    GameStateResponse gameStateResponse = gameState.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.LOBBY);
    Assertions.assertEquals(gameStateResponse.users().size(), 3);

    gameState.startGame();
    gameStateResponse = gameState.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.AUCTION_POWERPLANT);
    Assertions.assertEquals(gameStateResponse.currentPlayerOrder().size(), 3);

    UUID player1Id = gameStateResponse.currentPlayerOrder().get(0);
    UUID player2Id = gameStateResponse.currentPlayerOrder().get(1);
    UUID player3Id = gameStateResponse.currentPlayerOrder().get(2);

    //
    // FIRST USER PICKS A PLANT AND OTHER USER'S PASS ON IT
    //

    ImmutableAuctionAction auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.CHOOSE_PLANT)
            .bid(5)
            .userId(player1Id)
            .choosePlantId(2)
            .build();
    // Assert that the actual market has the card we're bidding on, before we bid on it.
    Assertions.assertTrue(
        gameStateResponse
            .actualMarket()
            .stream()
            .map(PowerPlantCard::minimumAcceptableBid)
            .collect(Collectors.toList())
            .contains(2));

    gameState.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameState.gamePhase(), GamePhase.POWERPLANT_BIDDING);
    Assertions.assertEquals(gameState.gameState().currentUser(), player2Id);
    Assertions.assertEquals(gameState.gameState().userWithHighestPowerplantBid(), player1Id);
    Truth.assertThat(gameState.gameState().usersPassedFromBidding()).containsExactly();

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player2Id).build();
    gameState.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameState.gameState().currentUser(), player3Id);
    Assertions.assertEquals(player1Id, gameState.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(gameState.gameState().usersPassedFromBidding()).containsExactly(player2Id);

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player3Id).build();
    gameState.performAuctionAction(auctionAction);

    // Since both users passed, we are now onto the phase where the second user gets to
    // choose a plant to begin bidding on.
    Assertions.assertEquals(gameState.gamePhase(), GamePhase.AUCTION_POWERPLANT);
    Map<UUID, List<PowerPlantCard>> userPowerPlants = gameState.gameState().userPowerPlants();
    Assertions.assertEquals(userPowerPlants.get(player1Id).size(), 1);
  }

  @Test
  void auction_secondUserWins() {
    gameState.createUser("user1", "red");
    gameState.createUser("user2", "blue");
    gameState.createUser("user3", "green");

    GameStateResponse gameStateResponse = gameState.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.LOBBY);
    Assertions.assertEquals(gameStateResponse.users().size(), 3);

    gameState.startGame();
    gameStateResponse = gameState.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.AUCTION_POWERPLANT);
    Assertions.assertEquals(gameStateResponse.currentPlayerOrder().size(), 3);

    UUID player1Id = gameStateResponse.currentPlayerOrder().get(0);
    UUID player2Id = gameStateResponse.currentPlayerOrder().get(1);
    UUID player3Id = gameStateResponse.currentPlayerOrder().get(2);

    //
    // FIRST USER PICKS A PLANT
    //

    ImmutableAuctionAction auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.CHOOSE_PLANT)
            .bid(5)
            .userId(player1Id)
            .choosePlantId(2)
            .build();
    // Assert that the actual market has the card we're bidding on, before we bid on it.
    Assertions.assertTrue(
        gameStateResponse
            .actualMarket()
            .stream()
            .map(PowerPlantCard::minimumAcceptableBid)
            .collect(Collectors.toList())
            .contains(2));

    gameState.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameState.gamePhase(), GamePhase.POWERPLANT_BIDDING);
    Assertions.assertEquals(gameState.gameState().currentUser(), player2Id);
    Assertions.assertEquals(player1Id, gameState.gameState().userWithHighestPowerplantBid());

    //
    // SECOND USER OUTBIDS
    //

    auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.BID)
            .bid(6)
            .userId(player2Id)
            .build();
    gameState.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameState.gameState().currentUser(), player3Id);
    Assertions.assertEquals(gameState.gameState().currentPowerPlantBid(), 6);
    Assertions.assertEquals(player2Id, gameState.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(gameState.gameState().usersPassedFromBidding()).containsExactly();

    //
    // THIRD AND FIRST USER PASS ON THE BID
    //

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player3Id).build();
    gameState.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameState.gameState().currentUser(), player1Id);
    Assertions.assertEquals(player2Id, gameState.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(gameState.gameState().usersPassedFromBidding()).containsExactly(player3Id);

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player1Id).build();
    gameState.performAuctionAction(auctionAction);
    Assertions.assertNull(gameState.gameState().userWithHighestPowerplantBid());

    // Since the first user passsed on their second round of bidding, user two wins the auction.
    Assertions.assertEquals(gameState.gamePhase(), GamePhase.AUCTION_POWERPLANT);
    Map<UUID, List<PowerPlantCard>> userPowerPlants = gameState.gameState().userPowerPlants();
    Assertions.assertEquals(userPowerPlants.get(player2Id).size(), 1);
  }
}
