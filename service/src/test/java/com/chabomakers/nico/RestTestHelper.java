package com.chabomakers.nico;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class RestTestHelper {

  // public static final int PORT = 8362;

  public static String doubleQuote(String singleQuoted) {
    return singleQuoted.replace('\'', '"');
  }

  // public static RequestSpecification request() {
  // return RestAssured.given().port(PORT);
  // }

  public static Matcher<String> containsAll(String... singleQuoted) {
    return Matchers.allOf(
        Arrays.stream(singleQuoted)
            .map(RestTestHelper::doubleQuote)
            .map(Matchers::containsString)
            .collect(Collectors.toList()));
  }

  public static Matcher<String> containsAllOrdered(String... singleQuoted) {
    return Matchers.stringContainsInOrder(
        Arrays.stream(singleQuoted)
            .map(RestTestHelper::doubleQuote)
            .collect(Collectors.toList())
            .toArray(new String[singleQuoted.length]));
  }

  public static Matcher<String> matchesRegex(String regexSingleQuotes) {
    return Matchers.matchesRegex(doubleQuote(regexSingleQuotes));
  }
}
