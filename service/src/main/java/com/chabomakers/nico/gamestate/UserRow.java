package com.chabomakers.nico.gamestate;

import java.util.UUID;
import javax.annotation.Generated;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(allowedClasspathAnnotations = {Generated.class}) // Because ErrorProne+JDK8.
public interface UserRow {
  UUID id();

  String name();

  String color();
}
