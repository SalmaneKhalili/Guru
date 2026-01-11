package pt.procurainterna.guru;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CommandListener extends ListenerAdapter {

  private final Jdbi jdbi;

  public CommandListener(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    event.deferReply(false).queue();

    switch (event.getName()) {
      case "setrole":
        final OptionMapping option = event.getOption("role");
        if (option == null) {
          event.getHook().sendMessage("Please provide a role").setEphemeral(true).queue();
          return;
        }

        final Role role = option.getAsRole();
        if (role == null) {
          event.getHook().sendMessage("Please provide a valid role").setEphemeral(true).queue();
          return;
        }

        try {
          setConfig(event.getGuild().getId(), role.getId());

        } catch (Exception e) {
          event.getHook().sendMessage("Failed to set role").setEphemeral(true).queue();
          throw new RuntimeException(e);
        }
        event.getHook().sendMessage("Role set").setEphemeral(true).queue();

        break;

      default:
        event.getHook().sendMessage("Unknown command").setEphemeral(true).queue();
        break;
    }
  }

  private void setConfig(final String guildId, final String roleId) {
    jdbi.useHandle(handle -> {
      final Query query = handle.createQuery("SELECT 1 FROM guild_starting_role WHERE guild_id = ?")
          .bind(0, guildId);
      final boolean exists = query.mapTo(Integer.class).findFirst().isPresent();

      final Update update;
      if (exists) {
        update =
            handle.createUpdate("UPDATE guild_starting_role SET role_id = ? WHERE guild_id = ?");

      } else {
        update = handle
            .createUpdate("INSERT INTO guild_starting_role (role_id, guild_id) VALUES (?, ?)");
      }
      update.bind(0, roleId).bind(1, guildId).execute();

      update.execute();
    });
  }

}
