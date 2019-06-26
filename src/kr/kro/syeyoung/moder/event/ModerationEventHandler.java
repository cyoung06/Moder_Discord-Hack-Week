package kr.kro.syeyoung.moder.event;

import java.sql.SQLException;
import java.util.Date;

import kr.kro.syeyoung.moder.database.DAO_EventLog;
import kr.kro.syeyoung.moder.database.DAO_ModerationLog;
import kr.kro.syeyoung.moder.database.DTO_EventLog;
import kr.kro.syeyoung.moder.database.DTO_ModerationLog;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ModerationEventHandler extends ListenerAdapter {
	
	@Override
	public void onGuildBan(GuildBanEvent e) {
		try {
			Thread.sleep(100L);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		AuditLogEntry ale = e.getGuild().getAuditLogs().complete().stream().filter(en -> en.getType() == ActionType.BAN).findFirst().orElse(null);
		
		for (AuditLogEntry ale2 : e.getGuild().getAuditLogs().complete()) {
			System.out.println(ale2.getUser() + " _ " + ale2.getType().toString());
		}
		
		DTO_EventLog elog = new DTO_EventLog();
		elog.setD(new Date());
		elog.setGuildId(e.getGuild().getIdLong());
		elog.setType(DTO_EventLog.EventType.GuildModeration);
		elog.setUserId(ale.getUser().getIdLong());
		
		long id;
		try {
			id = DAO_EventLog.newEventLog(elog);
		} catch (SQLException e1) {
			e1.printStackTrace();
			id = 0;
		}
		
		if (id == 0) {System.out.println("Failed to Log :: ");}
		
		DTO_ModerationLog mlog = new DTO_ModerationLog();
		mlog.setReason(ale.getReason());
		mlog.setType(DTO_ModerationLog.EventType.BAN);
		mlog.setUserId(ale.getTargetIdLong());
		
		try {
			DAO_ModerationLog.newModerationLog(id, mlog);
		} catch (SQLException e1) {
			System.out.println("Failed to Log :: ");
			e1.printStackTrace();
		}
	}
}