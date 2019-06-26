package kr.kro.syeyoung.moder.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class CommandBase {
	public abstract String getPrefix();
	
	public abstract String getUsage();
	
	public abstract String getDescription();
	
	public abstract boolean execute(MessageReceivedEvent e);
}
