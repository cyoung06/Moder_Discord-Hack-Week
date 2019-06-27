package kr.kro.syeyoung.moder.command;

import java.awt.Color;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import kr.kro.syeyoung.moder.database.DAO_EventLog;
import kr.kro.syeyoung.moder.database.DAO_ModerationLog;
import kr.kro.syeyoung.moder.database.DTO_EventLog;
import kr.kro.syeyoung.moder.database.DTO_ModerationLog;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class CMD_Mod_History extends CommandBase {

	@Override
	public String getPrefix() {
		return "mod history";
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public boolean execute(MessageReceivedEvent e) {
		String[] args = e.getMessage().getContentRaw().split(" ");
		
		if (args.length == 3) {
			e.getChannel().sendMessage(new EmbedBuilder()
									.setTitle("Correct Usage")
									.setDescription("+mod history [all / ban / kick / unban] (Date since) [Date end]")
									.setColor(Color.ORANGE)
									.setTimestamp(Instant.now())
									.setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
			return true;
		}
		
		DTO_ModerationLog.EventType type;
		
		try {
			type = DTO_ModerationLog.EventType.valueOf(args[2]);
		} catch (Exception e2) {
			type = null;
		}
		
		Date start;
		try {
			start = sdf.parse(args[3]);
		} catch (ParseException e1) {
			e.getChannel()
			.sendMessage(
					new EmbedBuilder()
							.setTitle("Error")
							.setDescription(
									"Unknown Date Format "
											+ args[3]
											+ ", should be yyyy-MM-dd format")
							.setColor(Color.ORANGE)
							.setTimestamp(Instant.now())
							.setFooter(
									"Moder :: A Powerful Discord Moderation Bot",
									null).build()).queue();
			return true;
		}
		
		Date end = new Date();
		if (args.length == 5) {
			try {
				end = sdf.parse(args[4]);
			} catch (ParseException e1) {
				e.getChannel()
				.sendMessage(
						new EmbedBuilder()
								.setTitle("Error")
								.setDescription(
										"Unknown Date Format "
												+ args[3]
												+ ", should be yyyy-MM-dd format")
								.setColor(Color.ORANGE)
								.setTimestamp(Instant.now())
								.setFooter(
										"Moder :: A Powerful Discord Moderation Bot",
										null).build()).queue();
				return true;
			}
		}
		
		List<DTO_ModerationLog> logs;
		try {
			if (type == null) {
				logs = DAO_ModerationLog.getModerationLogsByGuildIdandBetweenTime(e.getGuild().getIdLong(), start, end);
			} else {
				logs = DAO_ModerationLog.getModerationLogsByGuildIdandBetweenTimeOnType(e.getGuild().getIdLong(), type.getTypeId(), start, end);
			}
		} catch (SQLException e1) {
			e.getChannel()
			.sendMessage(
					new EmbedBuilder()
							.setTitle("Error")
							.setDescription(
									"An Error occurred while querying database...")
							.setColor(Color.ORANGE)
							.setTimestamp(Instant.now())
							.setFooter(
									"Moder :: A Powerful Discord Moderation Bot",
									null).build()).queue();
			e1.printStackTrace();
			return true;
		}
		
		
		MessageAction MA = e.getChannel().sendMessage(new EmbedBuilder().setTitle("Moderation History for action "+args[2]).setDescription("There were total of "+logs.size()+" moderation history").build());
		List<MessageAction> MAS = new LinkedList<>();
		for (DTO_ModerationLog log:logs) {
			DTO_EventLog elog;
			try {
				elog = DAO_EventLog.getEventLogById(log.getEventId()).get();
			} catch (Exception e1) {
				e.getChannel()
				.sendMessage(
						new EmbedBuilder()
								.setTitle("Error")
								.setDescription(
										"An Error occurred while querying database...")
								.setColor(Color.ORANGE)
								.setTimestamp(Instant.now())
								.setFooter(
										"Moder :: A Powerful Discord Moderation Bot",
										null).build()).queue();
				return true;
			}
			MAS.add(e.getChannel().sendMessage(new EmbedBuilder().addField("Action", log.getType().name(), true)
					.addField("User", log.getDiscordUser().map(u -> u.getAsTag()).orElse("Unknown User : "+log.getUserId()), true)
					.addField("Moderator", elog.getDiscordUser().map(u -> u.getAsTag()).orElse("Unknown User"+elog.getUserId()), true)
					.addField("Reason", log.getReason() == null ? "EMPTY" : log.getReason(), false)
					.setTimestamp(new Date(elog.getD().getTime()).toInstant()).setColor(new Color(54,57,63)).build()));
		}
		
		
		MA.queue();
		MAS.forEach(MessageAction::queue);
		
		
		return false;
	}

}
