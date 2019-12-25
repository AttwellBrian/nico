package com.chabomakers.nico.database;

import javax.inject.Inject;

/**
 * Fake database secrets. Eventually, the values for these files will be pulled from the runtime
 * environment. During local dev, simple values like pwd=password will be used. During prod dev,
 * highly secret passwords will be used. *
 */
public class DbSecrets {

  @Inject
  public DbSecrets() {}

  /** Returns the database password. */
  public String databasePassword() {
    return "password";
  }

  /** Returns the database server. */
  public String databaseServer() {
    return "jdbc:postgresql://postgres_nico/nico?sslMode=disable";
  }

  /** Returns the database user. */
  public String databaseUser() {
    return "root";
  }
}
