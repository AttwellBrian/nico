package com.chabomakers.nico.database;

import static com.chabomakers.nico.database.AuctionAction.ActionType.CHOOSE_PLANT;

import com.chabomakers.nico.BadRequestException;
import com.chabomakers.nico.controllers.GameStateResponse;
import com.chabomakers.nico.controllers.ImmutableGameStateResponse;
import com.chabomakers.nico.controllers.User;
import com.chabomakers.nico.database.AuctionAction.ActionType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MemoryDatabase {

  private static final Logger LOGGER = LoggerFactory.getLogger(MemoryDatabase.class);

  private final Map<UUID, List<PowerPlantCard>> usersCards = Maps.newHashMap();
  private final Map<UUID, UserRow> users = Maps.newHashMap();
  private GamePhase gamePhase = GamePhase.LOBBY;
  private PowerPlantMarket powerPlantMarket = PowerPlantMarket.createFreshDeck();

  // Auction + Bidding state
  private int auctionNumber = 0;
  private List<UUID> playerOrder;
  private int currentBidIndex;
  private Set<UUID> currentBidPassedUsers;
  private PowerPlantCard currentBidPlant;
  private int currentPowerPlantBid;
  private UUID highestBidUser;

  @Inject
  public MemoryDatabase() {}

  public Collection<UserRow> getUsers() {
    return users.values();
  }

  public UserRow createUser(String username, String color) {
    ImmutableUserRow newUser =
        ImmutableUserRow.builder().color(color).id(UUID.randomUUID()).name(username).build();
    users.put(newUser.id(), newUser);
    usersCards.put(newUser.id(), Lists.newArrayList());
    return newUser;
  }

  public void performAuctionAction(AuctionAction action) {
    if (gamePhase == GamePhase.AUCTION_POWERPLANT) {
      if (action.actionType() == CHOOSE_PLANT) {
        if (currentBidPlant != null) {
          throw new BadRequestException("Choosing a plant when a plant is already chosen.");
        }
        if (action.bid() == null) {
          throw new BadRequestException("Bid value cannot be null when submitting a bid.");
        }
        currentPowerPlantBid = action.bid();
        highestBidUser = action.userId();
        currentBidPlant = powerPlantMarket.getCard(action.choosePlantId());
        currentBidIndex = (auctionNumber + 1) % users.size();
        gamePhase = GamePhase.POWERPLANT_BIDDING;
      }
      return;
    }
    if (gamePhase == GamePhase.POWERPLANT_BIDDING) {
      UUID currentUser = currentBidUser();
      if (action.userId().equals(currentUser)) {
        if (action.actionType() == ActionType.PASS) {
          currentBidPassedUsers.add(currentUser);
          if (currentBidPassedUsers.size() == playerOrder.size() - 1) {
            LOGGER.info("All user's but one has passed on the current auction.");
            gamePhase = GamePhase.AUCTION_POWERPLANT;
            powerPlantMarket.removeCard(currentBidPlant);
            usersCards.get(highestBidUser).add(currentBidPlant);
            currentBidPlant = null;
          } else {
            // Let the next user have a shot at bidding.
            do {
              currentBidIndex = (currentBidIndex + 1) % users.size();
            } while (currentBidPassedUsers.contains(currentBidUser()));
          }
        }
      }
      return;
    }
    throw new BadRequestException("Not in auction powerplant phase or bid phase.");
  }

  public GamePhase gamePhase() {
    return gamePhase;
  }

  public GameStateResponse gameState() {
    Collection<UserRow> userRows = getUsers();
    final List<User> users = userRows.stream().map(User::toUser).collect(Collectors.toList());
    UUID currentUser = currentBidUser(); // will need to refine this in a bit.
    return ImmutableGameStateResponse.builder()
        .addAllUsers(users)
        .currentPlayerOrder(playerOrder)
        .currentUser(currentUser)
        .actualMarket(powerPlantMarket.actualMarket())
        .futureMarket(powerPlantMarket.futureMarket())
        .currentPowerPlantBid(currentPowerPlantBid)
        .currentBidPowerPlant(currentBidPlant)
        .userPowerPlants(usersCards)
        .gamePhase(gamePhase())
        .build();
  }

  public void startGame() {
    assignUserOrder();
    initializeAuctionPhase();
  }

  private UUID currentBidUser() {
    return playerOrder == null ? null : playerOrder.get(currentBidIndex);
  }

  private void assignUserOrder() {
    // Pick arbitrary order for users that have same amount of resources.
    playerOrder =
        users
            .keySet()
            .stream()
            .map(key -> Pair.create(key, Math.random()))
            .sorted(Comparator.comparing(Pair::getValue))
            .map(Pair::getKey)
            .collect(Collectors.toList());
  }

  private void initializeAuctionPhase() {
    currentBidIndex = 0;
    currentBidPassedUsers = Sets.newHashSet();
    gamePhase = GamePhase.AUCTION_POWERPLANT;
    currentBidPlant = null;
    currentPowerPlantBid = 0;
  }

  public enum GamePhase {
    LOBBY,
    AUCTION_POWERPLANT,
    POWERPLANT_BIDDING,
    BUYING_RESOURCES
  }
}
