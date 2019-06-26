package kr.kro.syeyoung.moder.event;

import java.util.ArrayList;
import java.util.List;

import kr.kro.syeyoung.moder.Main;
import kr.kro.syeyoung.moder.command.CommandBase;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandHandler extends ListenerAdapter {
	
	List<CommandBase> bases = new ArrayList<>();
	
	public static final String UNIVERSAL_PREFIX = Main.getInstance().getConfig().getProperty("PREFIX", "+");
	
	public CommandHandler() {
		
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		User u = e.getAuthor();
		if (u.isBot()) return;
		if (u.isFake()) return;
		 
		
		String cmd = e.getMessage().getContentRaw();

		if (!cmd.startsWith(UNIVERSAL_PREFIX)) return;
		
		for (CommandBase base:bases) {
			if (cmd.startsWith(UNIVERSAL_PREFIX + base.getPrefix())) {
				base.execute(e);
				return;
			}
		}
	}
}
