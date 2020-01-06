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
                .resourceType(ResourceType.OIL)
                .minimumAcceptableBid(3)
                .resourcesRequired(2)
                .homesPowered(1)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.COAL)
                .minimumAcceptableBid(4)
                .resourcesRequired(2)
                .homesPowered(1)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.HYBRID)
                .minimumAcceptableBid(5)
                .resourcesRequired(2)
                .homesPowered(1)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.GARBAGE)
                .minimumAcceptableBid(6)
                .resourcesRequired(1)
                .homesPowered(1)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.OIL)
                .minimumAcceptableBid(7)
                .resourcesRequired(3)
                .homesPowered(2)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.COAL)
                .minimumAcceptableBid(8)
                .resourcesRequired(3)
                .homesPowered(2)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.OIL)
                .minimumAcceptableBid(9)
                .resourcesRequired(1)
                .homesPowered(1)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.COAL)
                .minimumAcceptableBid(10)
                .resourcesRequired(2)
                .homesPowered(2)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.URANIUM)
                .minimumAcceptableBid(11)
                .resourcesRequired(1)
                .homesPowered(2)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.HYBRID)
                .minimumAcceptableBid(12)
                .resourcesRequired(2)
                .homesPowered(2)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.ECOLOGICAL)
                .minimumAcceptableBid(13)
                .resourcesRequired(0)
                .homesPowered(1)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.GARBAGE)
                .minimumAcceptableBid(14)
                .resourcesRequired(2)
                .homesPowered(2)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.COAL)
                .minimumAcceptableBid(15)
                .resourcesRequired(2)
                .homesPowered(3)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.OIL)
                .minimumAcceptableBid(16)
                .resourcesRequired(2)
                .homesPowered(3)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.URANIUM)
                .minimumAcceptableBid(17)
                .resourcesRequired(1)
                .homesPowered(2)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.ECOLOGICAL)
                .minimumAcceptableBid(18)
                .resourcesRequired(0)
                .homesPowered(2)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.GARBAGE)
                .minimumAcceptableBid(19)
                .resourcesRequired(2)
                .homesPowered(3)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.COAL)
                .minimumAcceptableBid(20)
                .resourcesRequired(3)
                .homesPowered(5)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.HYBRID)
                .minimumAcceptableBid(21)
                .resourcesRequired(2)
                .homesPowered(4)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.ECOLOGICAL)
                .minimumAcceptableBid(22)
                .resourcesRequired(0)
                .homesPowered(2)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.URANIUM)
                .minimumAcceptableBid(23)
                .resourcesRequired(1)
                .homesPowered(3)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.GARBAGE)
                .minimumAcceptableBid(24)
                .resourcesRequired(2)
                .homesPowered(4)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.COAL)
                .minimumAcceptableBid(25)
                .resourcesRequired(2)
                .homesPowered(5)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.OIL)
                .minimumAcceptableBid(26)
                .resourcesRequired(2)
                .homesPowered(5)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.ECOLOGICAL)
                .minimumAcceptableBid(27)
                .resourcesRequired(0)
                .homesPowered(3)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.URANIUM)
                .minimumAcceptableBid(28)
                .resourcesRequired(1)
                .homesPowered(4)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.HYBRID)
                .minimumAcceptableBid(29)
                .resourcesRequired(1)
                .homesPowered(4)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.GARBAGE)
                .minimumAcceptableBid(30)
                .resourcesRequired(3)
                .homesPowered(6)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.COAL)
                .minimumAcceptableBid(31)
                .resourcesRequired(3)
                .homesPowered(6)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.OIL)
                .minimumAcceptableBid(32)
                .resourcesRequired(3)
                .homesPowered(6)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.ECOLOGICAL)
                .minimumAcceptableBid(33)
                .resourcesRequired(0)
                .homesPowered(4)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.URANIUM)
                .minimumAcceptableBid(34)
                .resourcesRequired(1)
                .homesPowered(5)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.OIL)
                .minimumAcceptableBid(35)
                .resourcesRequired(1)
                .homesPowered(5)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.COAL)
                .minimumAcceptableBid(36)
                .resourcesRequired(3)
                .homesPowered(7)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.ECOLOGICAL)
                .minimumAcceptableBid(37)
                .resourcesRequired(0)
                .homesPowered(4)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.GARBAGE)
                .minimumAcceptableBid(38)
                .resourcesRequired(3)
                .homesPowered(7)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.URANIUM)
                .minimumAcceptableBid(39)
                .resourcesRequired(1)
                .homesPowered(6)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.OIL)
                .minimumAcceptableBid(40)
                .resourcesRequired(2)
                .homesPowered(6)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.COAL)
                .minimumAcceptableBid(42)
                .resourcesRequired(2)
                .homesPowered(6)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.ECOLOGICAL)
                .minimumAcceptableBid(44)
                .resourcesRequired(0)
                .homesPowered(5)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.HYBRID)
                .minimumAcceptableBid(46)
                .resourcesRequired(3)
                .homesPowered(7)
                .build(),
            ImmutablePowerPlantCard.builder()
                .id(UUID.randomUUID())
                .resourceType(ResourceType.ECOLOGICAL)
                .minimumAcceptableBid(50)
                .resourcesRequired(0)
                .homesPowered(6)
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
