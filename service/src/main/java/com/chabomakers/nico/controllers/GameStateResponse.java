package com.chabomakers.nico.controllers;

import com.chabomakers.nico.database.MemoryDatabase.GamePhase;
import java.util.List;
import org.immutables.gson.Gson;
import org.immutables.value.Generated;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(allowedClasspathAnnotations = {Generated.class}) // Because ErrorProne+JDK8.
interface GameStateResponse {
  List<User> users();

  GamePhase gamePhase();
}
