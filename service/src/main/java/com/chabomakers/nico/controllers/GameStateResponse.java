package com.chabomakers.nico.controllers;

import com.chabomakers.nico.database.MemoryDatabase.GamePhase;
import com.chabomakers.nico.database.PowerPlantCard;
import java.util.List;
import java.util.Map;
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

  @Nullable
  UUID currentUser();

  @Nullable
  List<UUID> currentPlayerOrder();

  @Nullable
  Integer currentPowerPlantBid();

  @Nullable
  PowerPlantCard currentBidPowerPlant();

  @Nullable
  List<PowerPlantCard> actualMarket();

  @Nullable
  List<PowerPlantCard> futureMarket();
}
