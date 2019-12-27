package com.chabomakers.nico.gamestate;

import javax.annotation.Generated;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(allowedClasspathAnnotations = {Generated.class}) // Because ErrorProne+JDK8.
public interface PowerPlantCard {
  // TODO: add a uuid ID() field. Using `minimumAcceptableBid` as the ID is brittle.

  /** The minimum acceptable bid is also the identifier for the card. */
  int minimumAcceptableBid();

  /** How many resources the plant needs of any resource type. */
  ResourceType resourceType();

  int resourcesRequired();

  int homesPowered();

  enum ResourceType {
    COAL,
    OIL,
    GARBAGE,
    URANIUM,
    HYBRID,
    ECOLOGICAL
  }
}
