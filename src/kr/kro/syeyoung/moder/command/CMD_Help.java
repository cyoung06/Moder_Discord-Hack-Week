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
		.setDescription("Moder is a powerful moderation bot, built for Discord-Hack-Week!\nModer can currently manage roles and users.\n~~Actually, the developer made the message part, but database design was so complex, he gave up to make that part~~")
		.addField("Help", "`+help` :: Show this message", false)
		.addField("Roles", "`+roles show` :: Shows all roles in this server \n`+roles history (Role)` :: Shows edit history of a role\n`+roles rollback (Role) (Time)` :: Rollbacks a role to that time\n`+roles allrollback (Time)` :: Rollbacks all the roles in the server to that time.\n`+roles details (Role) [Time]` :: Shows detailed information of a role", false)
		.addField("Moderation", "`+mod history [all / ban / kick / unban] [Time since] [Time end]`", false)
		.addField("Users", "`+users count` `+users history (User)`", false)
		.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
		return false;
	}

}
