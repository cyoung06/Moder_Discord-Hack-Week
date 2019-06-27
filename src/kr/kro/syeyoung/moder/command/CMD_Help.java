package kr.kro.syeyoung.moder.command;

import java.time.Instant;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CMD_Help extends CommandBase {
	public String getPrefix() {
		return "help";
	}

	@Override
	public boolean execute(MessageReceivedEvent e) {
		e.getChannel().sendMessage(new EmbedBuilder()
		.setTitle("Moder")
		.setDescription("Moder is a powerful moderation bot, built for Discord-Hack-Week!\nModer can currently manage roles and show logs of moderation and roles")
		.addField("Help", "`+help` :: Show this message", false)
		.addField("Roles", "`+roles show` :: Shows all roles in this server \n`+roles history (Role)` :: Shows edit history of a role\n`+roles rollback (Role) (Time)` :: Rollbacks a role to that time\n`+roles allrollback (Time)` :: Rollbacks all the roles in the server to that time.\n`+roles details (Role) [Time]` :: Shows detailed information of a role", false)
		.addField("Moderation", "`+mod history [all / ban / kick / unban] (Date since) [Date end]` :: Shows moderation history", false)
		.addField("Users", "`+users count` :: shows guild member count", false)
		.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
		return false;
	}

}
