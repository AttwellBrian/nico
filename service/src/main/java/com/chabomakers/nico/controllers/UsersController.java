package com.chabomakers.nico.controllers;

import com.chabomakers.nico.Controller;
import com.chabomakers.nico.database.RealDatabase;
import com.chabomakers.nico.database.UserRow;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import spark.Request;
import spark.Response;

public class UsersController implements Controller {

  private RealDatabase realDatabase;

  @Inject
  public UsersController(RealDatabase realDatabase) {
    this.realDatabase = realDatabase;
  }

  @Override
  public String path() {
    return "/users";
  }

  /** {@inheritDoc} */
  @Override
  public UsersResponse get(Request request, Response response) {
    List<UserRow> userRows = realDatabase.getUsers();
    final List<User> users = userRows.stream().map(User::toUser).collect(Collectors.toList());
    return ImmutableUsersResponse.builder().addAllUsers(users).build();
  }
}
