package com.chabomakers.nico.database;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MemoryDatabase {
  private final Map<UUID, UserRow> users = Maps.newHashMap();
  private GamePhase gamePhase = GamePhase.LOBBY;

  @Inject
  public MemoryDatabase() {}

  public Collection<UserRow> getUsers() {
    return users.values();
  }

  public UserRow createUser(String username, String color) {
    ImmutableUserRow newUser =
        ImmutableUserRow.builder().color(color).id(UUID.randomUUID()).name(username).build();
    users.put(newUser.id(), newUser);
    return newUser;
  }

  public GamePhase gamePhase() {
    return gamePhase;
  }

  public void startGame() {
    gamePhase = GamePhase.IN_GAME;
  }

  public enum GamePhase {
    LOBBY,
    IN_GAME
  }
}
