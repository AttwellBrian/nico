package com.chabomakers.nico.controllers;

import com.chabomakers.nico.gamestate.GameStateMachine.GamePhase;
import com.chabomakers.nico.gamestate.PowerPlantCard;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import org.immutables.gson.Gson;
import org.immutables.value.Generated;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(allowedClasspathAnnotations = {Generated.class}) // Because ErrorProne+JDK8.
public interface GameStateResponse {
  List<User> users();

  GamePhase gamePhase();

  Map<UUID, List<PowerPlantCard>> userPowerPlants();

  Map<UUID, Integer> userMoney();

  Integer gameRound();

  @Nullable
  UUID currentUser();

  @Nullable
  List<UUID> currentPlayerOrder();

  @Nullable
  Integer currentPowerPlantBid();

  @Nullable
  PowerPlantCard currentBidPowerPlant();

  @Nullable
  UUID userWithHighestPowerplantBid();

  /** Which players have passed in the current round of bidding. */
  @Nullable
  Set<UUID> usersPassedFromBidding();

  @Nullable
  List<PowerPlantCard> actualMarket();

  @Nullable
  List<PowerPlantCard> futureMarket();
}
