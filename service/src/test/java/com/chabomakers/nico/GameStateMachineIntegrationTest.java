package com.chabomakers.nico;

import static com.chabomakers.nico.gamestate.GameStateMachine.GamePhase.BUYING_RESOURCES;

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
  void auction_usersPassOnBids() {
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

    gameStateMachine.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameStateMachine.gamePhase(), GamePhase.AUCTION_BIDDING);
    Assertions.assertEquals(gameStateMachine.gameState().currentUser(), player2Id);
    Assertions.assertEquals(gameStateMachine.gameState().userWithHighestPowerplantBid(), player1Id);
    Truth.assertThat(gameStateMachine.gameState().usersPassedFromBidding()).containsExactly();
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player1Id)).isEqualTo(50);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player2Id)).isEqualTo(50);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player3Id)).isEqualTo(50);

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
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player1Id)).isEqualTo(45);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player2Id)).isEqualTo(50);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player3Id)).isEqualTo(50);

    // Since both users passed, we are now onto the phase where the second user gets to
    // choose a plant to begin bidding on.
    Assertions.assertEquals(gameStateMachine.gamePhase(), GamePhase.AUCTION_PICK_PLANT);
    Assertions.assertEquals(player2Id, gameStateMachine.gameState().currentUser());
    Map<UUID, List<PowerPlantCard>> userPowerPlants =
        gameStateMachine.gameState().userPowerPlants();
    Assertions.assertEquals(userPowerPlants.get(player1Id).size(), 1);

    //
    // SECOND USER INITIATES BIDDING ON A PLANT, OTHER USER PASSES
    //

    PowerPlantCard secondPowerPlant = gameStateMachine.gameState().actualMarket().get(2);
    UUID secondPlantId = secondPowerPlant.id();
    auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.CHOOSE_PLANT)
            .choosePlantId(secondPlantId)
            .userId(player2Id)
            .bid(12)
            .build();
    gameStateMachine.performAuctionAction(auctionAction);
    Truth.assertThat(gameStateMachine.gameState().currentUser()).isEqualTo(player3Id);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player1Id)).isEqualTo(45);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player2Id)).isEqualTo(50);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player3Id)).isEqualTo(50);

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player3Id).build();
    gameStateMachine.performAuctionAction(auctionAction);

    // Since the first user can't bid on a second plant and the third user passed, the second user
    // wins the power plant.
    userPowerPlants = gameStateMachine.gameState().userPowerPlants();
    Truth.assertThat(gameStateMachine.gameState().currentUser()).isEqualTo(player3Id);
    Truth.assertThat(gameStateMachine.gameState().gamePhase())
        .isEqualTo(GamePhase.AUCTION_PICK_PLANT);
    Truth.assertThat(userPowerPlants.get(player2Id)).containsExactly(secondPowerPlant);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player1Id)).isEqualTo(45);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player2Id)).isEqualTo(38);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player3Id)).isEqualTo(50);

    //
    // THIRD USER INITIATES AUCTION ON A PLANT AND WINS AUTOMATICALLY
    //
    PowerPlantCard auctionedPowerPLant = gameStateMachine.gameState().actualMarket().get(1);
    auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.CHOOSE_PLANT)
            .choosePlantId(auctionedPowerPLant.id())
            .userId(player3Id)
            .bid(13)
            .build();
    gameStateMachine.performAuctionAction(auctionAction);

    GameStateResponse gameState = gameStateMachine.gameState();
    Truth.assertThat(gameState.currentUser()).isEqualTo(player3Id);
    Truth.assertThat(gameState.gamePhase()).isEqualTo(GamePhase.BUYING_RESOURCES);
    userPowerPlants = gameState.userPowerPlants();
    Truth.assertThat(userPowerPlants.get(player3Id)).containsExactly(auctionedPowerPLant);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player1Id)).isEqualTo(45);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player2Id)).isEqualTo(38);
    Truth.assertThat(gameStateMachine.gameState().userMoney().get(player3Id)).isEqualTo(37);
  }

  @Test
  void auction_userLosesOnTheirFirstGameRoundPlantPick_userRepeatsPlantPick() {
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

    PowerPlantCard powerPlantCard = gameStateResponse.actualMarket().get(1);
    ImmutableAuctionAction auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.CHOOSE_PLANT)
            .bid(5)
            .userId(player1Id)
            .choosePlantId(powerPlantCard.id())
            .build();

    gameStateMachine.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameStateMachine.gamePhase(), GamePhase.AUCTION_BIDDING);
    Assertions.assertEquals(gameStateMachine.gameState().currentUser(), player2Id);
    Assertions.assertEquals(gameStateMachine.gameState().userWithHighestPowerplantBid(), player1Id);
    Truth.assertThat(gameStateMachine.gameState().usersPassedFromBidding()).containsExactly();

    //
    // SECOND USER INCREASES THE BID AND THEN WINS
    //

    auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.BID)
            .bid(6)
            .userId(player2Id)
            .build();
    gameStateMachine.performAuctionAction(auctionAction);
    Assertions.assertEquals(gameStateMachine.gameState().currentUser(), player3Id);
    Assertions.assertEquals(player2Id, gameStateMachine.gameState().userWithHighestPowerplantBid());
    Truth.assertThat(gameStateMachine.gameState().usersPassedFromBidding()).containsExactly();

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player3Id).build();
    gameStateMachine.performAuctionAction(auctionAction);
    Truth.assertThat(gameStateMachine.gameState().usersPassedFromBidding())
        .containsExactly(player3Id);

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player1Id).build();
    gameStateMachine.performAuctionAction(auctionAction);

    // Assert that the first player gets to choose another power plant.
    Truth.assertThat(gameStateMachine.gameState().currentUser()).isEqualTo(player1Id);
    Truth.assertThat(gameStateResponse.gamePhase()).isEqualTo(GamePhase.AUCTION_PICK_PLANT);
    Truth.assertThat(gameStateMachine.gameState().usersPassedFromBidding()).containsExactly();
    Truth.assertThat(gameStateMachine.gameState().userPowerPlants().get(player2Id))
        .containsExactly(powerPlantCard);
  }

  @Test
  void auction_secondUserWinsFirstUsersAuction() {
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

    // Verify: player 1 gets to start a new auction, since this is round 1 and
    // they lost their last auction.
    gameStateResponse = gameStateMachine.gameState();
    Assertions.assertEquals(gameStateResponse.currentUser(), player1Id);
  }

  @Test
  void auction_skipPreviouslyWonUser() {
    auction_secondUserWinsFirstUsersAuction();

    GameStateResponse gameStateResponse = gameStateMachine.gameState();
    Assertions.assertEquals(gameStateResponse.gamePhase(), GamePhase.AUCTION_PICK_PLANT);

    UUID player1Id = gameStateResponse.currentPlayerOrder().get(0);
    UUID player3Id = gameStateResponse.currentPlayerOrder().get(2);

    PowerPlantCard powerPlantCard = gameStateResponse.actualMarket().get(0);
    ImmutableAuctionAction auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.CHOOSE_PLANT)
            .bid(10)
            .userId(player1Id)
            .choosePlantId(powerPlantCard.id())
            .build();
    gameStateMachine.performAuctionAction(auctionAction);

    // Verify that we skip the second user, since they won the last auction.
    gameStateResponse = gameStateMachine.gameState();
    Truth.assertThat(gameStateResponse.currentUser()).isEqualTo(player3Id);

    auctionAction =
        ImmutableAuctionAction.builder().actionType(ActionType.PASS).userId(player3Id).build();
    gameStateMachine.performAuctionAction(auctionAction);
    Truth.assertThat(gameStateMachine.gameState().userWithHighestPowerplantBid()).isNull();

    // Verify the first user wins the auction. The only remaining auction user should be user 3.
    gameStateResponse = gameStateMachine.gameState();
    Truth.assertThat(gameStateResponse.currentUser()).isEqualTo(player3Id);
    Map<UUID, List<PowerPlantCard>> userPowerPlants =
        gameStateMachine.gameState().userPowerPlants();
    Truth.assertThat(userPowerPlants.get(player1Id)).containsExactly(powerPlantCard);

    powerPlantCard = gameStateResponse.actualMarket().get(0);
    auctionAction =
        ImmutableAuctionAction.builder()
            .actionType(ActionType.CHOOSE_PLANT)
            .bid(10)
            .userId(player3Id)
            .choosePlantId(powerPlantCard.id())
            .build();
    gameStateMachine.performAuctionAction(auctionAction);

    // Verify: we've finished all auctions.
    Truth.assertThat(gameStateMachine.gameState().gamePhase()).isEqualTo(BUYING_RESOURCES);
  }
}
