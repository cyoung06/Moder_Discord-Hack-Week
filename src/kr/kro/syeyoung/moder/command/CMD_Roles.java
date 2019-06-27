package kr.kro.syeyoung.moder.command;

import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.Route.Roles;

public class CMD_Roles extends CommandBase {
	public String getPrefix() {
		return "roles show";
	}


	@Override
	public boolean execute(MessageReceivedEvent e) {
		Guild g = e.getGuild();
		List<Role> rs = g.getRoles();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Roles").setDescription("Here are list of roles in this server").setColor(new Color(54,57,63))
		.addField("Mentionable Roles", 
				rs.stream().filter(a -> a.isMentionable()).map(a -> "`"+a.getName().replace("`", "")+"` ("+a.getIdLong()+")").collect(Collectors.joining("\n"))
		, false)
		.addField("Unmentionable Roles", rs.stream().filter(a -> !a.isMentionable()).map(a -> "`"+a.getName().replace("`", "")+"` ("+a.getIdLong()+")").collect(Collectors.joining("\n")), false)
		.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null);
		e.getChannel().sendMessage(eb.build()).queue();
		return true;
	}
	
}
