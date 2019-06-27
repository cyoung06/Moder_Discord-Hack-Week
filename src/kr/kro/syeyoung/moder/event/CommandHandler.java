package kr.kro.syeyoung.moder.event;

import java.util.ArrayList;
import java.util.List;

import kr.kro.syeyoung.moder.Main;
import kr.kro.syeyoung.moder.command.CMD_Help;
import kr.kro.syeyoung.moder.command.CMD_Role_History;
import kr.kro.syeyoung.moder.command.CMD_Roles;
import kr.kro.syeyoung.moder.command.CommandBase;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandHandler extends ListenerAdapter {
	
	public static List<CommandBase> commands = new ArrayList<>();
	
	public static final String UNIVERSAL_PREFIX = Main.getInstance().getConfig().getProperty("PREFIX", "+");
	
	public CommandHandler() {
		commands = new ArrayList<>();
		commands.add(new CMD_Help());
		commands.add(new CMD_Roles());
		commands.add(new CMD_Role_History());
		commands.add(new CMD_Role_Details());
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		User u = e.getAuthor();
		if (u.isBot()) return;
		if (u.isFake()) return;
		 
		if (!e.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
		
		String cmd = e.getMessage().getContentRaw();

		if (!cmd.startsWith(UNIVERSAL_PREFIX)) return;
		
		for (CommandBase base:commands) {
			if (cmd.startsWith(UNIVERSAL_PREFIX + base.getPrefix())) {
				base.execute(e);
				return;
			}
		}
	}
}
