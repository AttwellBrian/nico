package com.chabomakers.nico.database;

/*
public class AuctionStateMachine {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuctionStateMachine.class);

  private List<UUID> playerOrder;
  private int auctionNumber = 0;
  private int currentAuctionIndex = 0;
  private Set<UUID> currentAuctionPassedUsers = Sets.newHashSet();

  public AuctionStateMachine(List<UUID> playerOrder) {
    this.playerOrder = playerOrder;
  }

  public void performAction(AuctionAction action) {
    UUID currentUser = currentUser();
    if (action.userId().equals(currentUser)) {
      if (action.actionType() == ActionType.PASS) {
        currentAuctionPassedUsers.add(currentUser);
        if (currentAuctionPassedUsers.size() == playerOrder.size() - 1) {
          LOGGER.info("All user's but one has passed on the current auction.");
        }
      }
    }
  }
}
*/
