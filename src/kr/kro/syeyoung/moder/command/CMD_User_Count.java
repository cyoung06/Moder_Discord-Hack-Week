package kr.kro.syeyoung.moder.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CMD_User_Count extends CommandBase {
	public String getPrefix() {
		return "users count";
	}

	@Override
	public boolean execute(MessageReceivedEvent e) {
		long online = e.getGuild().getMembers().stream().filter(m -> m.getOnlineStatus() == OnlineStatus.ONLINE).count();
		long offline = e.getGuild().getMembers().stream().filter(m -> m.getOnlineStatus() == OnlineStatus.OFFLINE).count();
		long dnd = e.getGuild().getMembers().stream().filter(m -> m.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB).count();
		long idle = e.getGuild().getMembers().stream().filter(m -> m.getOnlineStatus() == OnlineStatus.IDLE).count();
		long invisible = e.getGuild().getMembers().stream().filter(m -> m.getOnlineStatus() == OnlineStatus.INVISIBLE).count();
		
		int total = e.getGuild().getMembers().size();
		
		e.getChannel().sendMessage(new EmbedBuilder().setTitle("User count").setDescription("There are total of "+total+" Users in this server")
				.addField(":green_heart: Online Users", online+"", false)
				.addField("Offline Users", (offline+invisible)+"", false)
				.addField(":red_circle: Do not disturb Users", dnd+"", false)
				.addField(":yellow_heart: AFK Users", idle+"", false).build()).queue();
		return false;
	}

}
