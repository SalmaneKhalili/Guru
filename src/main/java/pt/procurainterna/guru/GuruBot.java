package pt.procurainterna.guru;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class GuruBot {

  private static final Logger logger = LoggerFactory.getLogger(GuruBot.class);

  public Future<Void> start(final GuruParameters parameters) {
    logger.info("Initializing JDA with token: {}", parameters.apiToken != null ? "******" : "null");
    final CompletableFuture<Void> future = new CompletableFuture<>();

    final JDA jda = JDABuilder.createDefault(parameters.apiToken)
        .addEventListeners(new ShutdownListener(() -> future.complete(null)))
        .addEventListeners(new NewMemberEventListener(parameters.roleToAssign)).build();

    future.whenComplete((ok, ex) -> {
      if (ex != null) {
        jda.shutdown();

      } else {
        logger.info("GuruBot finished successfully");
      }
    });

    return future;
  }
}
