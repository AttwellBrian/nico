package com.chabomakers.nico.gamestate;

import java.util.UUID;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(allowedClasspathAnnotations = {Generated.class}) // Because ErrorProne+JDK8.
public interface AuctionAction {
  UUID userId();

  ActionType actionType();

  /** When bidding, or choosing a plant you must pass in a bid. */
  @Nullable
  Integer bid();

  /** When choosing a plant, you must pass in the ID of the power plant. */
  @Nullable
  UUID choosePlantId();

  enum ActionType {
    PASS,
    BID,
    CHOOSE_PLANT
  }
}
