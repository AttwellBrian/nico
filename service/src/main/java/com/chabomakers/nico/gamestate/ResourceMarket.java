package com.chabomakers.nico.gamestate;

import static com.chabomakers.nico.gamestate.PowerPlantCard.ResourceType.COAL;
import static com.chabomakers.nico.gamestate.PowerPlantCard.ResourceType.GARBAGE;
import static com.chabomakers.nico.gamestate.PowerPlantCard.ResourceType.OIL;
import static com.chabomakers.nico.gamestate.PowerPlantCard.ResourceType.URANIUM;

import com.chabomakers.nico.gamestate.PowerPlantCard.ResourceType;
import com.google.common.collect.Maps;
import java.util.Map;

public class ResourceMarket {

  private final static Map<Integer, Map<ResourceType, ResourcePerMajorPhase>> RESOURCES_PER_STEP_PER_PLAYER;

  private final int numberPlayers;
  //private final Map<Integer, Map<ResourceType, Integer>> resourceCountPerTypePerCost;

  static {
    RESOURCES_PER_STEP_PER_PLAYER = Maps.newHashMap();
    initializeResourcesForTwoPlayers();
    initializeResourcesForThreePlayers();
    initializeResourcesForFourPlayers();
    initializeResourcesForFivePlayers();
    initializeResourcesForSixPlayers();
  }

  public ResourceMarket(int numberPlayers) {
    this.numberPlayers = numberPlayers;
  }

  public void fillMarket() {
    
  }

  public int costToBuyResources(ResourceType resourceType, int count) {
    return 0;
  }

  /** Resource costs structured in an easy to display format. */
  public Map<Integer, Map<ResourceType, Integer>> resourceCountPerTypePerCost() {
    return null;
  }

  private static class ResourcePerMajorPhase {
    public final int step1;
    public final int step2;
    public final int step3;
    public ResourcePerMajorPhase(int step1, int step2, int step3) {
      this.step1 = step1;
      this.step2 = step2;
      this.step3 = step3;
    }
  }


  private static void initializeResourcesForTwoPlayers() {
    Map<ResourceType, ResourcePerMajorPhase> resourcesForTwoPlayers = Maps.newHashMap();
    resourcesForTwoPlayers.put(COAL, new ResourcePerMajorPhase(3, 4, 5));
    resourcesForTwoPlayers.put(OIL, new ResourcePerMajorPhase(2, 2, 4));
    resourcesForTwoPlayers.put(GARBAGE, new ResourcePerMajorPhase(1, 2, 3));
    resourcesForTwoPlayers.put(URANIUM, new ResourcePerMajorPhase(1, 1, 1));
    RESOURCES_PER_STEP_PER_PLAYER.put(2, resourcesForTwoPlayers);
  }

  private static void initializeResourcesForThreePlayers() {
    Map<ResourceType, ResourcePerMajorPhase> resourcesForThreePlayers = Maps.newHashMap();
    resourcesForThreePlayers.put(COAL, new ResourcePerMajorPhase(4, 5, 3));
    resourcesForThreePlayers.put(OIL, new ResourcePerMajorPhase(2, 3, 4));
    resourcesForThreePlayers.put(GARBAGE, new ResourcePerMajorPhase(1, 2, 3));
    resourcesForThreePlayers.put(URANIUM, new ResourcePerMajorPhase(1, 1, 1));
    RESOURCES_PER_STEP_PER_PLAYER.put(3, resourcesForThreePlayers);
  }

  private static void initializeResourcesForFourPlayers() {
    Map<PowerPlantCard.ResourceType, ResourcePerMajorPhase> resourcesForFourPlayers = Maps.newHashMap();
    resourcesForFourPlayers.put(COAL, new ResourcePerMajorPhase(5, 6, 4));
    resourcesForFourPlayers.put(OIL, new ResourcePerMajorPhase(3, 4, 5));
    resourcesForFourPlayers.put(GARBAGE, new ResourcePerMajorPhase(2, 3, 4));
    resourcesForFourPlayers.put(URANIUM, new ResourcePerMajorPhase(1, 2, 2));
    RESOURCES_PER_STEP_PER_PLAYER.put(4, resourcesForFourPlayers);
  }

  private static void initializeResourcesForFivePlayers() {
    Map<PowerPlantCard.ResourceType, ResourcePerMajorPhase> resources = Maps.newHashMap();
    resources.put(COAL, new ResourcePerMajorPhase(5, 7, 5));
    resources.put(OIL, new ResourcePerMajorPhase(4, 5, 6));
    resources.put(GARBAGE, new ResourcePerMajorPhase(3, 3, 5));
    resources.put(URANIUM, new ResourcePerMajorPhase(2, 3, 2));
    RESOURCES_PER_STEP_PER_PLAYER.put(5, resources);
  }

  private static void initializeResourcesForSixPlayers() {
    Map<PowerPlantCard.ResourceType, ResourcePerMajorPhase> resources = Maps.newHashMap();
    resources.put(COAL, new ResourcePerMajorPhase(7, 9, 6));
    resources.put(OIL, new ResourcePerMajorPhase(5, 6, 7));
    resources.put(GARBAGE, new ResourcePerMajorPhase(3, 5, 6));
    resources.put(URANIUM, new ResourcePerMajorPhase(2, 3, 3));
    RESOURCES_PER_STEP_PER_PLAYER.put(6, resources);
  }
}
