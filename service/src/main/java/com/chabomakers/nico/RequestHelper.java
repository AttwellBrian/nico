package com.chabomakers.nico;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import spark.Request;

/** Contains static methods for dealing with {@link Request}s. */
public class RequestHelper {

  /**
   * Parses a UUID from a path parameter.
   *
   * @throws BadRequestException on invalid input
   */
  public static UUID parsePathUuid(Request request, String name) {
    String value = request.params(name);
    try {
      return UUID.fromString(value);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(
          String.format("Bad path parameter for '%s': '%s'", name, value));
    }
  }

  /**
   * Parses a UUID from a query parameter.
   *
   * @throws BadRequestException on invalid input
   */
  public static UUID parseQueryUuid(Request request, String name) {
    String value = request.queryParams(name);
    try {
      return UUID.fromString(value);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(
          String.format("Bad query parameter for '%s': '%s'", name, value));
    }
  }

  /**
   * Parses an array of UUIDs from a query parameter.
   *
   * @throws BadRequestException on invalid input
   */
  public static Collection<UUID> parseQueryUuids(Request request, String name) {
    String value = request.queryParams(name);
    try {
      return Arrays.stream(value.split(",")).map(UUID::fromString).collect(Collectors.toList());
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(
          String.format("Bad query parameter for '%s': '%s'", name, value));
    }
  }

  /**
   * Parses a request body into a POJO.
   *
   * @throws BadRequestException on invalid input
   */
  public static <T> T parseBody(Gson gson, Request request, Class<T> classOfT) {
    try {
      return gson.fromJson(request.body(), classOfT);
    } catch (Exception e) {
      throw invalidRequestBody(e.getMessage());
    }
  }

  /** Creates a BadRequestException with an invalid-request-body message prefix. */
  public static BadRequestException invalidRequestBody(String message) {
    return new BadRequestException("Invalid request body: " + message);
  }

  /**
   * Validates that a parent object is non-null;
   *
   * @param parent the parent object
   * @param resourceName used in exception text
   * @param resourceId used in exception text
   * @throws BadRequestException on null parent
   */
  public static <T> T validateParentResource(T parent, String resourceName, Object resourceId) {
    if (parent == null) {
      throw new NotFoundException(String.format("%s '%s' not found.", resourceName, resourceId));
    }
    return parent;
  }

  public static void checkLength(String fieldName, String value, int maxLength) {
    if (Strings.nullToEmpty(value).length() > maxLength) {
      throw new BadRequestException(
          String.format(
              "Field '%s' with value '%s' is longer than %d.", fieldName, value, maxLength));
    }
  }
}
