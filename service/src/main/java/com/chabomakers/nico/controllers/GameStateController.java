package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.database.MemoryDatabase;
import com.chabomakers.nico.database.UserRow;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class GameStateController implements Controller {

  private MemoryDatabase memoryDatabase;

  @Inject
  public GameStateController(MemoryDatabase realDatabase) {
    this.memoryDatabase = realDatabase;
  }

  @Override
  public String path() {
    return "/state";
  }

  /** {@inheritDoc} */
  @Override
  public GameStateResponse get(Request request, Response response) {
    return getGameStateResponse(memoryDatabase);
  }

  public static GameStateResponse getGameStateResponse(MemoryDatabase memoryDatabase) {
    Collection<UserRow> userRows = memoryDatabase.getUsers();
    final List<User> users = userRows.stream().map(User::toUser).collect(Collectors.toList());
    return ImmutableGameStateResponse.builder()
        .addAllUsers(users)
        .gamePhase(memoryDatabase.gamePhase())
        .build();
  }
}
