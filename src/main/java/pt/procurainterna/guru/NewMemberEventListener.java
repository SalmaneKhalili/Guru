package pt.procurainterna.guru;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NewMemberEventListener extends ListenerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(NewMemberEventListener.class);

  private final String roleToAssign;

  public NewMemberEventListener(String roleToAssign) {
    this.roleToAssign = roleToAssign;
  }

  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    logger.info("New member joined: {} | {}", event.getMember().getUser().getId(),
        event.getMember().getUser().getName());

    final Member member = event.getMember();
    final Guild guild = event.getGuild();

    final Role role = role(guild);

    logger.info("Assigning role {} to member {}", role.getId(), member.getUser().getId());
    guild.addRoleToMember(member, role)
        .onSuccess(ignore -> logger.info("Role assigned successfully")).queue();
  }

  private Role role(final Guild guild) throws IllegalStateException {
    logger.info("Looking for role: {}", roleToAssign);

    final Iterator<Role> roles = guild.getRolesByName(roleToAssign, false).iterator();

    if (!roles.hasNext()) {
      throw new IllegalStateException("Role " + roleToAssign + " not found");
    }

    final Role role = roles.next();
    if (roles.hasNext()) {
      throw new IllegalStateException("Multiple roles found for " + roleToAssign);
    }

    logger.info("Found role: {}", role.getId());

    return role;
  }

}
