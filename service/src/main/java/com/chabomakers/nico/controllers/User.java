package com.chabomakers.nico.controllers;

import com.chabomakers.nico.database.UserRow;
import java.util.UUID;
import org.immutables.gson.Gson;
import org.immutables.value.Generated;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(allowedClasspathAnnotations = {Generated.class}) // Because ErrorProne+JDK8.
public interface User {
  UUID id();

  String name();

  String color();

  static User toUser(UserRow userRow) {
    return ImmutableUser.builder()
        .color(userRow.color())
        .id(userRow.id())
        .name(userRow.name())
        .build();
  }
}
