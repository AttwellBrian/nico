package com.chabomakers.nico.gamestate;

import com.chabomakers.nico.gamestate.PowerPlantCard.ResourceType;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PowerPlantMarket {

  private final Set<PowerPlantCard> cards;

  public static PowerPlantMarket createFreshDeck() {
    Set<PowerPlantCard> cards =
        Sets.newHashSet(
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .homesPowered(1)
                .minimumAcceptableBid(1)
                .resourcesRequired(2)
                .resourceType(ResourceType.COAL)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .homesPowered(1)
                .minimumAcceptableBid(2)
                .resourcesRequired(2)
                .resourceType(ResourceType.COAL)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .homesPowered(1)
                .minimumAcceptableBid(3)
                .resourcesRequired(2)
                .resourceType(ResourceType.COAL)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .homesPowered(1)
                .minimumAcceptableBid(4)
                .resourcesRequired(2)
                .resourceType(ResourceType.COAL)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .homesPowered(1)
                .minimumAcceptableBid(5)
                .resourcesRequired(2)
                .resourceType(ResourceType.COAL)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .homesPowered(1)
                .minimumAcceptableBid(6)
                .resourcesRequired(2)
                .resourceType(ResourceType.COAL)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .homesPowered(1)
                .minimumAcceptableBid(7)
                .resourcesRequired(2)
                .resourceType(ResourceType.COAL)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .homesPowered(1)
                .minimumAcceptableBid(8)
                .resourcesRequired(2)
                .resourceType(ResourceType.COAL)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .homesPowered(1)
                .minimumAcceptableBid(9)
                .resourcesRequired(2)
                .resourceType(ResourceType.COAL)
                .build());
    return new PowerPlantMarket(cards);
  }

  private PowerPlantMarket(Set<PowerPlantCard> cards) {
    this.cards = cards;
  }

  public List<PowerPlantCard> actualMarket() {
    return sortedStream().limit(4).collect(Collectors.toList());
  }

  public List<PowerPlantCard> futureMarket() {
    return cards.stream().skip(4).limit(4).collect(Collectors.toList());
  }

  public void removeCard(PowerPlantCard currentPlant) {
    cards.remove(currentPlant);
  }

  public PowerPlantCard getCard(UUID choosePlantId) {
    return cards.stream().filter(card -> card.id().equals(choosePlantId)).findFirst().get();
  }

  private Stream<PowerPlantCard> sortedStream() {
    return cards.stream().sorted(Comparator.comparingInt(PowerPlantCard::minimumAcceptableBid));
  }
}
