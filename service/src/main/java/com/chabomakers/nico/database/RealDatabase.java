package com.chabomakers.nico.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.List;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.immutables.JdbiImmutables;
import org.jdbi.v3.guava.GuavaPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RealDatabase class exposed to higher levels of the app. This is a facade around the DAO. All
 * methods on this class should execute in transactions.
 */
@Singleton
public class RealDatabase {

  private static final Logger LOGGER = LoggerFactory.getLogger(RealDatabase.class);

  private final DataSource dataSource;
  private final Jdbi jdbi;

  @Inject
  RealDatabase(DataSource dataSource) {
    this.dataSource = dataSource;
    this.jdbi = Jdbi.create(dataSource);
    jdbi.installPlugin(new GuavaPlugin());
    jdbi.installPlugin(new SqlObjectPlugin());
    JdbiImmutables jdbiImmutables = jdbi.getConfig(JdbiImmutables.class);
    Dao.MODELS.forEach(jdbiImmutables::registerImmutable);
  }

  /** Builds a {@link DataSource} from a {@link DbSecrets}. */
  public static DataSource buildDataSource(DbSecrets secrets) {
    Properties props = new Properties();
    props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");

    HikariConfig hikari = new HikariConfig();
    hikari.setJdbcUrl(secrets.databaseServer());
    hikari.setUsername(secrets.databaseUser());
    hikari.setPassword(secrets.databasePassword());
    hikari.setDataSourceProperties(props);
    hikari.setTransactionIsolation("TRANSACTION_SERIALIZABLE");
    return new HikariDataSource(hikari);
  }

  public List<UserRow> getUsers() {
    return jdbi.onDemand(Dao.class).getUsers();
  }

  /**
   * Time consuming initialization step. This is broken out from the class construction so we can
   * explicitly call this method while the pod is in its NOT_SERVING startup phase.
   */
  public void initialize() {
    Flyway.configure().dataSource(dataSource).load().migrate();
  }
}
