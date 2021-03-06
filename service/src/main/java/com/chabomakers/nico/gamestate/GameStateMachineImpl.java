package com.chabomakers.nico.gamestate;

import static com.chabomakers.nico.gamestate.AuctionAction.ActionType.CHOOSE_PLANT;
import static com.chabomakers.nico.gamestate.AuctionAction.ActionType.PASS;
import static com.chabomakers.nico.gamestate.GameStateMachine.GamePhase.AUCTION_BIDDING;
import static com.chabomakers.nico.gamestate.GameStateMachine.GamePhase.AUCTION_PICK_PLANT;
import static com.chabomakers.nico.gamestate.GameStateMachine.GamePhase.BUYING_RESOURCES;
import static com.chabomakers.nico.gamestate.GameStateMachine.GamePhase.LOBBY;

import com.chabomakers.nico.BadRequestException;
import com.chabomakers.nico.controllers.GameStateResponse;
import com.chabomakers.nico.controllers.ImmutableGameStateResponse;
import com.chabomakers.nico.controllers.User;
import com.chabomakers.nico.gamestate.AuctionAction.ActionType;
import com.chabomakers.nico.gamestate.GameStateMachine.GamePhase;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles state manipulation and game actions.
 *
 * <p>Mostly handles bidding. Additional rules not yet implemented: 1. A user cannot have more than
 * 3 power plants. If they buy a forth they need to discard one. 2. If no one buys a single power
 * plant card in a game round, then we should discard the lowest power plant. 3. We don't do
 * anything special to determine player order before bidding.
 */
@Singleton
public class GameStateMachineImpl {

  private static final Logger LOGGER = LoggerFactory.getLogger(GameStateMachineImpl.class);
  private static final int USER_STARTING_MONEY = 50;

  private final Map<UUID, List<PowerPlantCard>> usersCards = Maps.newHashMap();
  private final Map<UUID, UserRow> users = Maps.newHashMap();
  private GamePhase gamePhase = LOBBY;
  private PowerPlantMarket powerPlantMarket = PowerPlantMarket.createFreshDeck();
  private int gameRound = 0;
  private Map<UUID, Integer> userMoney = Maps.newHashMap();

  // Auction + Bidding state
  private List<UUID> userOrder;
  private int currentBidIndex;
  private Set<UUID> currentBidPassedUsers;
  private PowerPlantCard currentBidPlant;
  private Integer currentPowerPlantBid;
  private UUID highestBidUser;
  private Set<UUID> usersWhoHaveWonAuctions = Sets.newHashSet();
  private Set<UUID> usersWhoHaveStartedAuctions = Sets.newHashSet();

  // Resource buying state
  private int resourceBuyNumber;

  public GameStateMachineImpl() {}

  public UserRow createUser(String username, String color) {
    ImmutableUserRow newUser =
        ImmutableUserRow.builder().color(color).id(UUID.randomUUID()).name(username).build();
    users.put(newUser.id(), newUser);
    usersCards.put(newUser.id(), Lists.newArrayList());
    userMoney.put(newUser.id(), USER_STARTING_MONEY);
    return newUser;
  }

  public void performAuctionAction(AuctionAction action) {
    if (gamePhase == AUCTION_PICK_PLANT) {
      if (action.actionType() == CHOOSE_PLANT) {
        if (currentBidPlant != null) {
          throw new BadRequestException("Choosing a plant when a plant is already chosen.");
        }
        Integer currentBid = action.bid();
        if (currentBid == null) {
          throw new BadRequestException("Bid value cannot be null when submitting a bid.");
        }
        validateBid(action.userId(), currentBid);
        currentPowerPlantBid = action.bid();
        highestBidUser = action.userId();
        currentBidPlant = powerPlantMarket.getCard(action.choosePlantId());
        selectNextBidUser();
        gamePhase = AUCTION_BIDDING;
        if (playersWhoCantBidInCurrentAuction().size() == userOrder.size() - 1) {
          handleAllBiddersDone();
        }
      } else if (action.actionType() == PASS) {
        if (gameRound == 0) {
          throw new BadRequestException("Cannot pass during a user's first AUCTION_PICK_PLANT.");
        }
        // TODO: test this case once we have a test that can complete an entire game round.
        selectNextBidUser();
      } else {
        throw new BadRequestException("Can only pass or choose plant during this phase.");
      }
      return;
    }
    if (gamePhase == AUCTION_BIDDING) {
      UUID currentUser = currentBidUser();
      if (action.userId().equals(currentUser)) {
        if (action.actionType() == ActionType.BID) {
          Integer bidValue = action.bid();
          if (bidValue == null || bidValue <= currentPowerPlantBid) {
            throw new BadRequestException("Invalid bid value.");
          }
          validateBid(currentUser, bidValue);
          currentPowerPlantBid = bidValue;
          highestBidUser = currentUser;
          selectNextBidUser();
        } else if (action.actionType() == ActionType.PASS) {
          currentBidPassedUsers.add(currentUser);
          if (playersWhoCantBidInCurrentAuction().size() == userOrder.size() - 1) {
            handleAllBiddersDone();
          } else {
            // Let the next user have a shot at bidding.
            selectNextBidUser();
          }
        }
      }
      return;
    }
    throw new BadRequestException("Not in auction powerplant phase or bid phase.");
  }

  private void validateBid(UUID user, int bid) {
    Integer currentMoney = userMoney.get(user);
    if (currentMoney < bid) {
      throw new BadRequestException("User has too little money for this bid.");
    }
  }

  private Collection<UserRow> getPlayers() {
    return users.values();
  }

  private Set<UUID> playersWhoCantBidInCurrentAuction() {
    HashSet<UUID> uuids = Sets.newHashSet(currentBidPassedUsers);
    uuids.addAll(usersWhoHaveWonAuctions);
    return uuids;
  }

  /** Handle the transition that occurs when all the current auction's bidders are done. */
  private void handleAllBiddersDone() {
    LOGGER.info("All user's but one has passed on the current auction.");
    gamePhase = AUCTION_PICK_PLANT;
    powerPlantMarket.removeCard(currentBidPlant);
    usersCards.get(highestBidUser).add(currentBidPlant);
    usersWhoHaveWonAuctions.add(highestBidUser);
    userMoney.put(highestBidUser, userMoney.get(highestBidUser) - currentPowerPlantBid);
    currentBidPlant = null;
    currentPowerPlantBid = null;
    highestBidUser = null;
    currentBidPassedUsers = Sets.newHashSet();
    boolean userFoundToStartNextAuction = updateBidIndexAfterAuctionWon();
    if (!userFoundToStartNextAuction) {
      gamePhase = BUYING_RESOURCES;
      resourceBuyNumber = 0;
    }
  }

  private void selectNextBidUser() {
    do {
      currentBidIndex = (currentBidIndex + 1) % users.size();
    } while (playersWhoCantBidInCurrentAuction().contains(currentBidUser()));
  }

  public GamePhase gamePhase() {
    return gamePhase;
  }

  public GameStateResponse gameState() {
    Collection<UserRow> userRows = getPlayers();
    final List<User> users = userRows.stream().map(User::toUser).collect(Collectors.toList());
    return ImmutableGameStateResponse.builder()
        .addAllUsers(users)
        .currentPlayerOrder(userOrder)
        .currentUser(currentUser())
        .actualMarket(powerPlantMarket.actualMarket())
        .futureMarket(powerPlantMarket.futureMarket())
        .currentPowerPlantBid(currentPowerPlantBid)
        .currentBidPowerPlant(currentBidPlant)
        .userMoney(userMoney)
        .gameRound(gameRound)
        .userPowerPlants(usersCards)
        .userWithHighestPowerplantBid(highestBidUser)
        .usersPassedFromBidding(currentBidPassedUsers)
        .gamePhase(gamePhase())
        .build();
  }

  public void startGame() {
    assignUserOrder();
    initializeAuctionPhase();
  }

  @Nullable
  private UUID currentUser() {
    switch (gamePhase) {
      case AUCTION_PICK_PLANT:
      case AUCTION_BIDDING:
        return currentBidUser();
      case BUYING_RESOURCES:
        return currentResourceBuyUser();
      default:
        return null;
    }
  }

  private UUID currentResourceBuyUser() {
    return userOrder.get(userOrder.size() - 1 - resourceBuyNumber);
  }

  /**
   * Updates the bid index to point to the user that should start an auction. If no such user can be
   * found returns false.
   */
  private boolean updateBidIndexAfterAuctionWon() {
    if (gameRound == 0) {
      UUID playerWithoutWin =
          userOrder
              .stream()
              .filter(playerUuid -> !usersWhoHaveWonAuctions.contains(playerUuid))
              .findFirst()
              .orElse(null);
      if (playerWithoutWin == null) {
        return false;
      }
      currentBidIndex = userOrder.indexOf(playerWithoutWin);
    } else {
      // TODO: need to test this logic thoroughly once we have the ability to test
      //   multiple game rounds since this is totally different in the second game round.
      UUID playerWithoutWin =
          userOrder
              .stream()
              .filter(
                  playerUuid ->
                      !usersWhoHaveWonAuctions.contains(playerUuid)
                          && !usersWhoHaveStartedAuctions.contains(playerUuid))
              .findFirst()
              .orElse(null);
      if (playerWithoutWin == null) {
        return false;
      }
      currentBidIndex = userOrder.indexOf(playerWithoutWin);
    }
    usersWhoHaveStartedAuctions.add(currentBidUser());
    return true;
  }

  private UUID currentBidUser() {
    return userOrder == null ? null : userOrder.get(currentBidIndex);
  }

  private void assignUserOrder() {
    // Pick arbitrary order for users that have same amount of resources.
    userOrder =
        users
            .keySet()
            .stream()
            .map(key -> Pair.create(key, Math.random()))
            .sorted(Comparator.comparing(Pair::getValue))
            .map(Pair::getKey)
            .collect(Collectors.toList());
  }

  private void initializeAuctionPhase() {
    usersWhoHaveStartedAuctions = Sets.newHashSet();
    usersWhoHaveWonAuctions = Sets.newHashSet();
    currentBidIndex = 0;
    currentBidPassedUsers = Sets.newHashSet();
    gamePhase = AUCTION_PICK_PLANT;
    currentBidPlant = null;
    currentPowerPlantBid = 0;
  }
}
