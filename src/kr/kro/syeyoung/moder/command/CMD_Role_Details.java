package kr.kro.syeyoung.moder.command;

import java.awt.Color;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import kr.kro.syeyoung.moder.database.DAO_EventLog;
import kr.kro.syeyoung.moder.database.DAO_RoleLog;
import kr.kro.syeyoung.moder.database.DTO_Role;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CMD_Role_Details extends CommandBase {
	public String getPrefix() {
		return "roles details";
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public boolean execute(MessageReceivedEvent e) {
		Message m = e.getMessage();
		String raw = m.getContentRaw();
		
		String[] args = raw.split(" ");
		
		if (args.length < 3) {
			e.getChannel().sendMessage(new EmbedBuilder().setTitle("Correct Usage").setDescription("+roles details (Role ID) [Time]").setColor(Color.ORANGE)
					.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
			return true;
		}
		
		Date d = new Date();
		if (args.length == 4) {
			e.getChannel().sendMessage(new EmbedBuilder().setTitle("Error").setDescription("Unknown Date Format " + args[3] + ", should be yyyy-MM-dd hh:mm:ss format").setColor(Color.ORANGE)
					.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
			return true;
		}
		if (args.length == 5) {
			try {
				d = sdf.parse(args[3] +" "+ args[4]);
			} catch (ParseException e1) {
				e.getChannel().sendMessage(new EmbedBuilder().setTitle("Error").setDescription("Unknown Date Format " + args[3] + ", should be yyyy-MM-dd hh:mm:ss format").setColor(Color.ORANGE)
						.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
				return true;
			}
		}
		
		if (e.getGuild().getRoleById(args[2]) == null) {
			e.getChannel().sendMessage(new EmbedBuilder().setTitle("Error").setDescription("Can't find a role with that id").setColor(Color.ORANGE)
					.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
			return true;
		}
		
		DTO_Role role;
		try {
			Optional<DTO_Role> orole = DAO_RoleLog.findRoleByTimeBeforeAndDiscordRoleId(Long.parseLong(args[2]), d);
			if (orole.isPresent()) {
				role = orole.get();
			} else {
				e.getChannel().sendMessage(new EmbedBuilder().setTitle("Error").setDescription("No Role Data present before "+sdf.format(d)).setColor(Color.ORANGE)
						.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
				return true;
			}
		} catch (Exception e1) {
			e.getChannel().sendMessage(new EmbedBuilder().setTitle("Error").setDescription("An Error occurred while querying database...").setColor(Color.ORANGE)
					.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
			e1.printStackTrace();
			return true;
		}
		
		
		
		try {
			e.getChannel().sendMessage(new EmbedBuilder()
					.addField("Name", role.getName(), true)
					.addField("Color", Integer.toHexString(role.getColor()), true)
					.addField("MENTIONABLE", role.isMentionable() ? "YES" : "NO", true)
					.addField("POSITION", role.getPosition() + "", true)
					.addField("PERMISSION", role.getPermission() + "", true)
					.addField("HOISTED", role.isHoisted() ? "YES" : "NO", true)
					.addField("Changes Made by", DAO_EventLog.getEventLogById(DAO_RoleLog.getRoleLogByRoleId(role.getRoleId()).get().getEventId()).get().getDiscordUser().map(a -> a.getAsTag()).orElse("Unknown") + "", true)
					.setTimestamp(role.getLastUpdate().toInstant()).setColor(new Color(54,57,63)).build()).queue();
		} catch (SQLException e1) {
			e.getChannel().sendMessage(new EmbedBuilder().setTitle("Error").setDescription("An Error occurred while querying database...").setColor(Color.ORANGE)
					.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
			e1.printStackTrace();
			return true;
		}
		
		return false;
	}

}
