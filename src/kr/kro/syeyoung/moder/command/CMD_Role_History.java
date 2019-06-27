package kr.kro.syeyoung.moder.command;

import java.awt.Color;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import kr.kro.syeyoung.moder.database.DAO_EventLog;
import kr.kro.syeyoung.moder.database.DAO_RoleLog;
import kr.kro.syeyoung.moder.database.DTO_Role;
import kr.kro.syeyoung.moder.database.DTO_RoleLog;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class CMD_Role_History extends CommandBase {
	public String getPrefix() {
		// TODO Auto-generated method stub
		return "roles history";
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	@Override
	public boolean execute(MessageReceivedEvent e) {
		Message msg = e.getMessage();
		String raw = msg.getContentRaw();
		if (raw.split(" ").length == 2) {
			e.getChannel().sendMessage(new EmbedBuilder().setTitle("Correct Usage").setDescription("+roles history (Role ID)").setColor(Color.ORANGE)
					.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
			return true;
		}
		
		Role r = e.getGuild().getRoleById(raw.split(" ")[2]);
		if (r == null) {
			e.getChannel().sendMessage(new EmbedBuilder().setTitle("Error").setDescription("Can't find a role with that id").setColor(Color.ORANGE)
					.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
			return true;
		}
		List<DTO_RoleLog> logs;
		try {
			logs = DAO_RoleLog.findRoleLogsByRoleId(r.getIdLong());
		} catch (SQLException e1) {
			e.getChannel().sendMessage(new EmbedBuilder().setTitle("Error").setDescription("An Error occurred while querying database...").setColor(Color.ORANGE)
					.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
			e1.printStackTrace();
			return true;
		}
		MessageAction MA = e.getChannel().sendMessage(new EmbedBuilder().setTitle("Role Edit History for "+r.getName()+" ("+r.getPosition()+")").setDescription("There were total of "+logs.size()+" edit history for the role").build());
		List<MessageAction> MAS = new LinkedList<>();
		for (int i = 0;i < logs.size(); i ++) {
			DTO_RoleLog elog = logs.get(i);
			DTO_Role role = null;
			try {
				role = elog.getDTORole();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				MAS.add(e.getChannel().sendMessage(new EmbedBuilder().addField("Action", logs.get(i).getType().name(), true)
						.addField("Name", role.getName(), true)
						.addField("Color", Integer.toHexString(role.getColor()), true)
						.addField("MENTIONABLE", role.isMentionable() ? "YES" : "NO", true)
						.addField("POSITION", role.getPosition() + "", true)
						.addField("PERMISSION", role.getPermission() + "", true)
						.addField("Changes Made by", DAO_EventLog.getEventLogById(elog.getEventId()).get().getDiscordUser().map(a -> a.getAsTag()).orElse("Unknown") + "", true)
						.setTimestamp(role.getLastUpdate().toInstant()).setColor(new Color(54,57,63)).build()));
			} catch (SQLException e1) {
				e.getChannel().sendMessage(new EmbedBuilder().setTitle("Error").setDescription("An Error occurred while querying database...").setColor(Color.ORANGE)
						.setTimestamp(Instant.now()).setFooter("Moder :: A Powerful Discord Moderation Bot", null).build()).queue();
				e1.printStackTrace();
				return true;
			}
		}
		
		
		MA.queue();
		MAS.forEach(MessageAction::queue);
		return true;
	}

}
