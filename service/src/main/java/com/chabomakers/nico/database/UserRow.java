package com.chabomakers.nico.database;

import java.util.UUID;
import javax.annotation.Generated;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allowedClasspathAnnotations = {Generated.class}) // Because ErrorProne+JDK8.
public interface UserRow {
  UUID id();

  String name();

  String color();
}
