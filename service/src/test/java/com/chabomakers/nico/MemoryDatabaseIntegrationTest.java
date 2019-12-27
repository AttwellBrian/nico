package com.chabomakers.nico;

import com.chabomakers.nico.controllers.GameStateResponse;
import com.chabomakers.nico.database.AuctionAction.ActionType;
import com.chabomakers.nico.database.ImmutableAuctionAction;
import com.chabomakers.nico.database.MemoryDatabase;
import com.chabomakers.nico.database.MemoryDatabase.GamePhase;
import com.chabomakers.nico.database.PowerPlantCard;
import com.google.common.truth.Truth;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemoryDatabaseIntegrationTest {

  private MemoryDatabase memoryDatabase;

  @BeforeEach
  void setup() {
    ServerComponent serverComponent = DaggerServerComponent.create();
    memoryDatabase = serverComponent.memoryDatabase();
  }

  @Test
  void auction_bothUsersPass() {
    memoryDatabase.createUser("user1", "red");
    memoryDatabase.createUser("user2", "blue");
    memoryDatabase.createUser("user3", "green");

    GameStateResponse gameStateResponse = memoryDatabase.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.LOBBY);
    Assertions.assertEquals(gameStateResponse.users().size(), 3);

    memoryDatabase.startGame();
    gameStateResponse = memoryDatabase.gameState();
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

    memoryDatabase.performAuctionAction(auctionAction);
    Assertions.assertEquals(memoryDatabase.gamePhase(), GamePhase.POWERPLANT_BIDDING);
    Assertions.assertEquals(memoryDatabase.gameState().currentUser(), player2Id);
    Assertions.assertEquals(memoryDatabase.gameState().userWithHighestPowerplantBid(), player1Id);
    Truth.assertThat(memoryDatabase.gameState().usersPassedFromBidding()).containsExactly();

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player2Id).build();
    memoryDatabase.performAuctionAction(auctionAction);
    Assertions.assertEquals(memoryDatabase.gameState().currentUser(), player3Id);
    Assertions.assertEquals(player1Id, memoryDatabase.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(memoryDatabase.gameState().usersPassedFromBidding())
        .containsExactly(player2Id);

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player3Id).build();
    memoryDatabase.performAuctionAction(auctionAction);

    // Since both users passed, we are now onto the phase where the second user gets to
    // choose a plant to begin bidding on.
    Assertions.assertEquals(memoryDatabase.gamePhase(), GamePhase.AUCTION_POWERPLANT);
    Map<UUID, List<PowerPlantCard>> userPowerPlants = memoryDatabase.gameState().userPowerPlants();
    Assertions.assertEquals(userPowerPlants.get(player1Id).size(), 1);
  }

  @Test
  void auction_secondUserWins() {
    memoryDatabase.createUser("user1", "red");
    memoryDatabase.createUser("user2", "blue");
    memoryDatabase.createUser("user3", "green");

    GameStateResponse gameStateResponse = memoryDatabase.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.LOBBY);
    Assertions.assertEquals(gameStateResponse.users().size(), 3);

    memoryDatabase.startGame();
    gameStateResponse = memoryDatabase.gameState();
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

    memoryDatabase.performAuctionAction(auctionAction);
    Assertions.assertEquals(memoryDatabase.gamePhase(), GamePhase.POWERPLANT_BIDDING);
    Assertions.assertEquals(memoryDatabase.gameState().currentUser(), player2Id);
    Assertions.assertEquals(player1Id, memoryDatabase.gameState().userWithHighestPowerplantBid());

    //
    // SECOND USER OUTBIDS
    //

    auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.BID)
            .bid(6)
            .userId(player2Id)
            .build();
    memoryDatabase.performAuctionAction(auctionAction);
    Assertions.assertEquals(memoryDatabase.gameState().currentUser(), player3Id);
    Assertions.assertEquals(memoryDatabase.gameState().currentPowerPlantBid(), 6);
    Assertions.assertEquals(player2Id, memoryDatabase.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(memoryDatabase.gameState().usersPassedFromBidding()).containsExactly();

    //
    // THIRD AND FIRST USER PASS ON THE BID
    //

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player3Id).build();
    memoryDatabase.performAuctionAction(auctionAction);
    Assertions.assertEquals(memoryDatabase.gameState().currentUser(), player1Id);
    Assertions.assertEquals(player2Id, memoryDatabase.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(memoryDatabase.gameState().usersPassedFromBidding())
        .containsExactly(player3Id);

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player1Id).build();
    memoryDatabase.performAuctionAction(auctionAction);
    Assertions.assertNull(memoryDatabase.gameState().userWithHighestPowerplantBid());

    // Since the first user passsed on their second round of bidding, user two wins the auction.
    Assertions.assertEquals(memoryDatabase.gamePhase(), GamePhase.AUCTION_POWERPLANT);
    Map<UUID, List<PowerPlantCard>> userPowerPlants = memoryDatabase.gameState().userPowerPlants();
    Assertions.assertEquals(userPowerPlants.get(player2Id).size(), 1);
  }
}
