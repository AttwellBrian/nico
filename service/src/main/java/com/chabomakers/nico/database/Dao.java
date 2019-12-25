package com.chabomakers.nico.database;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

/** Single DAO used for all queries. */
@UseClasspathSqlLocator
interface Dao {

  ImmutableSet<Class<?>> MODELS = ImmutableSet.of(UserRow.class);

  @SqlQuery
  List<UserRow> getUsers();
}
