package kr.kro.syeyoung.moder.event;

import java.sql.SQLException;
import java.util.Date;

import kr.kro.syeyoung.moder.database.DAO_EventLog;
import kr.kro.syeyoung.moder.database.DAO_RoleLog;
import kr.kro.syeyoung.moder.database.DTO_EventLog;
import kr.kro.syeyoung.moder.database.DTO_Role;
import kr.kro.syeyoung.moder.database.DTO_RoleLog;
import kr.kro.syeyoung.moder.utils.AuditLogUtil;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.role.RoleCreateEvent;
import net.dv8tion.jda.core.events.role.RoleDeleteEvent;
import net.dv8tion.jda.core.events.role.update.GenericRoleUpdateEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateMentionableEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdatePositionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class RoleEventHandler extends ListenerAdapter {
	
	public DTO_Role discordRoleToDTO(Role r) {
		DTO_Role dr = new DTO_Role();
		dr.setName(r.getName());
		dr.setColor(r.getColorRaw());
		dr.setDiscordRoleId(r.getIdLong());
		dr.setMentionable(r.isMentionable());
		dr.setPermission(r.getPermissionsRaw());
		dr.setPosition(r.getPositionRaw());
		dr.setLastUpdate(new Date());
		return dr;
	}
	
	@Override
	public void onRoleCreate(RoleCreateEvent e) {
		AuditLogEntry ale = AuditLogUtil.getLastExecutor(en -> en.getType() == ActionType.ROLE_CREATE && en.getTargetIdLong() == e.getRole().getIdLong(), e.getGuild()).orElseGet(null);
		User executor = ale.getUser();
		
		DTO_Role dr = discordRoleToDTO(e.getRole());
		try {
			DAO_RoleLog.newDTO_Role(dr);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}
		
		DTO_EventLog elog = new DTO_EventLog();
		elog.setUserId(executor.getIdLong());
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
		log.setRole(dr);
		log.setType(DTO_RoleLog.EventType.Creation);
		
		try {
			DAO_RoleLog.newDTO_Log(id, log);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void onRoleDelete(RoleDeleteEvent e) {
		AuditLogEntry ale = AuditLogUtil.getLastExecutor(en -> en.getType() == ActionType.ROLE_DELETE && en.getTargetIdLong() == e.getRole().getIdLong(), e.getGuild()).orElseGet(null);
		User executor = ale.getUser();
		
		DTO_Role dr = discordRoleToDTO(e.getRole());
		try {
			DAO_RoleLog.newDTO_Role(dr);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}
		
		DTO_EventLog elog = new DTO_EventLog();
		elog.setUserId(executor.getIdLong());
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
		log.setRole(dr);
		log.setType(DTO_RoleLog.EventType.Deletion);
		
		try {
			DAO_RoleLog.newDTO_Log(id, log);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void onRoleUpdate(GenericRoleUpdateEvent e, DTO_RoleLog.EventType type) {
		AuditLogEntry ale = AuditLogUtil.getLastExecutor(en -> en.getType() == ActionType.ROLE_UPDATE && en.getTargetIdLong() == e.getRole().getIdLong(), e.getGuild()).orElseGet(null);
		User executor = ale.getUser();
		
		DTO_Role dr = discordRoleToDTO(e.getRole());
		try {
			DAO_RoleLog.newDTO_Role(dr);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}
		
		DTO_EventLog elog = new DTO_EventLog();
		elog.setUserId(executor.getIdLong());
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
		log.setRole(dr);
		log.setType(type);
		
		try {
			DAO_RoleLog.newDTO_Log(id, log);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void onRoleUpdateColor(RoleUpdateColorEvent e) {
		onRoleUpdate(e, DTO_RoleLog.EventType.Color);
	}

	@Override
	public void onRoleUpdateMentionable(RoleUpdateMentionableEvent e) {
		onRoleUpdate(e, DTO_RoleLog.EventType.Mentionable);
	}

	@Override
	public void onRoleUpdateName(RoleUpdateNameEvent e) {
		onRoleUpdate(e, DTO_RoleLog.EventType.Name);
	}
	
	@Override
	public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent e) {
		onRoleUpdate(e, DTO_RoleLog.EventType.Permission);
	}
	
	@Override
	public void onRoleUpdatePosition(RoleUpdatePositionEvent e) {
		onRoleUpdate(e, DTO_RoleLog.EventType.Position);
	}
}
