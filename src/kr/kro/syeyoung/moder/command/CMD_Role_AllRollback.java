package kr.kro.syeyoung.moder.command;

import java.awt.Color;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import kr.kro.syeyoung.moder.database.DAO_RoleLog;
import kr.kro.syeyoung.moder.database.DTO_Role;
import kr.kro.syeyoung.moder.database.DTO_RoleLog;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;

public class CMD_Role_AllRollback extends CommandBase {

	@Override
	public String getPrefix() {
		return "+roles allrollback";
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Override
	public boolean execute(MessageReceivedEvent e) {
		Message m = e.getMessage();
		String raw = m.getContentRaw();

		String[] args = raw.split(" ");

		if (args.length < 4) {
			e.getChannel()
					.sendMessage(
							new EmbedBuilder()
									.setTitle("Correct Usage")
									.setDescription("+roles allrollback [Time]")
									.setColor(Color.ORANGE)
									.setTimestamp(Instant.now())
									.setFooter(
											"Moder :: A Powerful Discord Moderation Bot",
											null).build()).queue();
			return true;
		}
		Date d;
		try {
			d = sdf.parse(args[2] + " " + args[3]);
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
		List<DTO_RoleLog> logs;
		try {
			logs = DAO_RoleLog.findRolesByTimeBeforeAndGuildId(e.getGuild()
					.getIdLong(), d);
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
		
		List<RestAction> tobe = new ArrayList<>();
		List<Long> current_roles = e.getGuild().getRoles().stream().map(ra -> ra.getIdLong()).collect(Collectors.toCollection(() -> new ArrayList<>()));
		
		for (DTO_RoleLog log : logs) {
			DTO_Role role;
			try {
				role = log.getDTORole();
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
			Role r = e.getGuild().getRoleById(role.getDiscordRoleId());

			current_roles.remove(role.getDiscordRoleId());
			if (r != null) {
				tobe.add(r.getManager()
				.setName(role.getName())
				.setColor(role.getColor())
				.setMentionable(role.isMentionable())
				.setPermissions(role.getPermission()).reason("Role rollbacked by "+e.getAuthor().getAsTag()+" / by time of "+sdf.format(d)));
			} else {
				tobe.add(e.getGuild().getController().createRole()
				.setName(role.getName())
				.setColor(role.getColor())
				.setMentionable(role.isMentionable())
				.setPermissions(role.getPermission())
				.reason("Role rollbacked by "+e.getAuthor().getAsTag()+" / by time of "+sdf.format(d)));
			}
			tobe.add(e.getChannel().sendMessage(new EmbedBuilder()
			.setTitle("Success!")
			.setDescription("Successfully rollbacked role "+role.getDiscordRoleId()+" to the time of "+sdf.format(d))
			.addField("Name", role.getName(), true)
			.addField("Color", Integer.toHexString(role.getColor()), true)
			.addField("MENTIONABLE", role.isMentionable() ? "YES" : "NO", true)
			.addField("POSITION", role.getPosition() + "", true)
			.addField("PERMISSION", role.getPermission() + "", true)
			.setTimestamp(Instant.now()).setColor(Color.green).build()));
		}
		for (Long l: current_roles) {
			tobe.add(e.getGuild().getRoleById(l).delete().reason("Role rollbacked by "+e.getAuthor().getAsTag()+" / by time of "+sdf.format(d)));
		}
		tobe.forEach(RestAction::queue);
		return true;
	}
}
