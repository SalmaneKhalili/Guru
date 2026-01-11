package pt.procurainterna.guru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ShutdownListener extends ListenerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(ShutdownListener.class);

  private final Runnable onShutdown;

  public ShutdownListener(Runnable onShutdown) {
    this.onShutdown = onShutdown;
  }

  @Override
  public void onShutdown(ShutdownEvent event) {
    logger.info("Shutdown event received: {}", event.getClass().getSimpleName());
    onShutdown.run();
  }

}
