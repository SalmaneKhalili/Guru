package pt.procurainterna.guru;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class GuruBot {

  private static final Logger logger = LoggerFactory.getLogger(GuruBot.class);

  public Future<Void> start(final GuruParameters parameters) {
    logger.info("Initializing JDA with token: {}", parameters.apiToken != null ? "******" : "null");
    final CompletableFuture<Void> future = new CompletableFuture<>();

    final Jdbi jdbi = jdbi(parameters.jdbcConfig);

    final JDA jda = JDABuilder.createDefault(parameters.apiToken)
        .addEventListeners(new ShutdownListener(() -> future.complete(null)))
        .addEventListeners(new GuildReadyListener()).addEventListeners(new CommandListener(jdbi))
        .addEventListeners(new NewMemberEventListener(jdbi)).build();

    future.whenComplete((ok, ex) -> {
      if (ex != null) {
        jda.shutdown();

      } else {
        logger.info("GuruBot finished successfully");
      }
    });

    return future;
  }

  private Jdbi jdbi(JdbcConfig jdbcConfig) {
    final DataSource dataSource = dataSource(jdbcConfig);

    return Jdbi.create(dataSource);
  }

  private DataSource dataSource(JdbcConfig jdbcConfig) {
    final HikariConfig config = new HikariConfig();
    config.setJdbcUrl(jdbcConfig.url);
    config.setUsername(jdbcConfig.user);
    config.setPassword(jdbcConfig.password);
    config.setDriverClassName(jdbcConfig.driverClassName);

    return new HikariDataSource(config);
  }
}
