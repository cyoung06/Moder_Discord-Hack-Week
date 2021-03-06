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
import kr.kro.syeyoung.moder.database.DTO_EventLog;
import kr.kro.syeyoung.moder.database.DTO_Role;
import kr.kro.syeyoung.moder.database.DTO_RoleLog;
import kr.kro.syeyoung.moder.event.RoleEventHandler;
import kr.kro.syeyoung.moder.utils.AuditLogUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CMD_Role_Rollback extends CommandBase {
	public String getPrefix() {
		return "roles rollback";
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public boolean execute(MessageReceivedEvent e) {
		Message m = e.getMessage();
		String raw = m.getContentRaw();

		String[] args = raw.split(" ");

		if (args.length < 5) {
			e.getChannel()
					.sendMessage(
							new EmbedBuilder()
									.setTitle("Correct Usage")
									.setDescription(
											"+roles rollback (Role ID) [Time]")
									.setColor(Color.ORANGE)
									.setTimestamp(Instant.now())
									.setFooter(
											"Moder :: A Powerful Discord Moderation Bot",
											null).build()).queue();
			return true;
		}
		Date d;
		try {
			d = sdf.parse(args[3] + " " + args[4]);
		} catch (ParseException e1) {
			e.getChannel()
					.sendMessage(
							new EmbedBuilder()
									.setTitle("Error")
									.setDescription(
											"Unknown Date Format "
													+ args[3]
													+ ", should be yyyy-MM-dd hh:mm:ss format")
									.setColor(Color.ORANGE)
									.setTimestamp(Instant.now())
									.setFooter(
											"Moder :: A Powerful Discord Moderation Bot",
											null).build()).queue();
			return true;
		}

		if (e.getGuild().getRoleById(args[2]) == null) {
			e.getChannel()
					.sendMessage(
							new EmbedBuilder()
									.setTitle("Error")
									.setDescription(
											"Can't find a role with that id")
									.setColor(Color.ORANGE)
									.setTimestamp(Instant.now())
									.setFooter(
											"Moder :: A Powerful Discord Moderation Bot",
											null).build()).queue();
			return true;
		}

		DTO_Role role;
		try {
			Optional<DTO_Role> orole = DAO_RoleLog
					.findRoleByTimeBeforeAndDiscordRoleId(
							Long.parseLong(args[2]), d);
			if (orole.isPresent()) {
				role = orole.get();
			} else {
				e.getChannel()
						.sendMessage(
								new EmbedBuilder()
										.setTitle("Error")
										.setDescription(
												"No Role Data present before "
														+ sdf.format(d))
										.setColor(Color.ORANGE)
										.setTimestamp(Instant.now())
										.setFooter(
												"Moder :: A Powerful Discord Moderation Bot",
												null).build()).queue();
				return true;
			}
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
			e1.printStackTrace();
			return true;
		}

		e.getGuild().getRoleById(role.getDiscordRoleId()).getManager()
		.setName(role.getName())
		.setColor(role.getColor())
		.setMentionable(role.isMentionable())
		.setPermissions(role.getPermission()).reason("Role rollbacked by "+e.getAuthor().getAsTag()+" / by time of "+sdf.format(d)).queue(a -> {
					DTO_Role role2 = RoleEventHandler.discordRoleToDTO(e.getGuild().getRoleById(role.getDiscordRoleId()));
					try {
						DAO_RoleLog.newDTO_Role(role2);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					DTO_EventLog elog = new DTO_EventLog();
					elog.setUserId(e.getAuthor().getIdLong());
					elog.setType(DTO_EventLog.EventType.GuildRole);
					elog.setGuildId(e.getGuild().getIdLong());
					elog.setD(new Date());
					
					long id = 0;
					try {
						id = DAO_EventLog.newEventLog(elog);
					} catch (SQLException e1) {
						e1.printStackTrace();
						return;
					}
					
					DTO_RoleLog log = new DTO_RoleLog();
					log.setRole(role2);
					log.setType(DTO_RoleLog.EventType.Rollback_Edition);
					
					try {
						DAO_RoleLog.newDTO_Log(id, log);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
		});
		

		
		
		e.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("Success!")
				.setDescription("Successfully rollbacked role "+role.getDiscordRoleId()+" to the time of "+sdf.format(d))
				.addField("Name", role.getName(), true)
				.addField("Color", Integer.toHexString(role.getColor()), true)
				.addField("MENTIONABLE", role.isMentionable() ? "YES" : "NO", true)
				.addField("POSITION", role.getPosition() + "", true)
				.addField("PERMISSION", role.getPermission() + "", true)
				.addField("HOISTED", role.isHoisted() ? "YES" : "NO", true)
				.setTimestamp(Instant.now()).setColor(Color.green).build()).queue();
		
		
		return false;
	}
}
