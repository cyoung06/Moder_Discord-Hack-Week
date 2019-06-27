package kr.kro.syeyoung.moder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DAO_RoleLog {
	public static Optional<DTO_RoleLog> getRoleLogByEventId(long eventId) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM GENERIC_GUILD_ROLE_LOG where EVENT_ID = ?");
		ps.setLong(1, eventId);
		
		ResultSet rs = ps.executeQuery();
		DTO_RoleLog drl = null;
		if (rs.first()) {
			drl = new DTO_RoleLog();
			drl.setEventId(eventId);
			drl.setType(DTO_RoleLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			drl.setRoleId(rs.getLong(3));
		}
		rs.close();
		ps.close();
		
		return Optional.ofNullable(drl);
	}
	
	public static Optional<DTO_RoleLog> getRoleLogByRoleId(long roleId) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM GENERIC_GUILD_ROLE_LOG where ROLE = ?");
		ps.setLong(1, roleId);
		
		ResultSet rs = ps.executeQuery();
		DTO_RoleLog drl = null;
		if (rs.first()) {
			drl = new DTO_RoleLog();
			drl.setEventId(rs.getLong(1));
			drl.setType(DTO_RoleLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			drl.setRoleId(rs.getLong(3));
		}
		rs.close();
		ps.close();
		
		return Optional.ofNullable(drl);
	}
	
	public static Optional<DTO_Role> getRoleByRoleId(long roleId) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM GUILD_ROLES where ROLE_ID = ?");
		ps.setLong(1, roleId);
		
		ResultSet rs = ps.executeQuery();
		DTO_Role dr = null;
		if (rs.first()) {
			dr = new DTO_Role();
			dr.setRoleId(roleId);
			dr.setDiscordRoleId(rs.getLong(2));
			dr.setName(rs.getString(3));
			dr.setColor(rs.getInt(4));
			dr.setPermission(rs.getLong(5));
			dr.setPosition(rs.getInt(6));
			dr.setMentionable(rs.getBoolean(7));
			dr.setLastUpdate(rs.getTimestamp(8));
		}
		
		rs.close();
		ps.close();
		return Optional.ofNullable(dr);
	}
	
	public static List<DTO_Role> getRolesByDiscordRoleId(long roleId) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM GUILD_ROLES where DISCORD_ROLE_ID = ?");
		ps.setLong(1, roleId);
		
		ResultSet rs = ps.executeQuery();
		List<DTO_Role> drs = new LinkedList<>();
		while (rs.next()) {
			DTO_Role dr = new DTO_Role();
			dr.setRoleId(rs.getLong(1));
			dr.setDiscordRoleId(rs.getLong(2));
			dr.setName(rs.getString(3));
			dr.setColor(rs.getInt(4));
			dr.setPermission(rs.getLong(5));
			dr.setPosition(rs.getInt(6));
			dr.setMentionable(rs.getBoolean(7));
			dr.setLastUpdate(rs.getTimestamp(8));
			drs.add(dr);
		}
		rs.close();
		ps.close();
		return drs;
	}
	
	//
	public static List<DTO_RoleLog> findRoleLogsByRoleId(long roleid) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("select generic_guild_role_log.*, guild_roles.DISCORD_ROLE_ID from generic_guild_role_log inner join guild_roles on generic_guild_role_log.ROLE = guild_roles.ROLE_ID where guild_roles.DISCORD_ROLE_ID = ?;");
		ps.setLong(1, roleid);
		
		ResultSet rs = ps.executeQuery();
		List<DTO_RoleLog> logs = new LinkedList<DTO_RoleLog>();
		while (rs.next()) { 
			DTO_RoleLog drl = new DTO_RoleLog();
			drl.setEventId(rs.getLong(1));
			drl.setType(DTO_RoleLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			drl.setRoleId(rs.getLong(3));
			logs.add(drl);
		}
		rs.close();
		ps.close();
		return logs;
	}
	
	public static Optional<DTO_Role> findRoleByTimeBeforeAndDiscordRoleId(long discordid, Date time) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps= conn.prepareStatement("select * from guild_roles where LASTUPDATE <= ? AND DISCORD_ROLE_ID = ? limit 1");
		
		ps.setTimestamp(1, new Timestamp(time.getTime()));
		ps.setLong(2, discordid);

		ResultSet rs = ps.executeQuery();
		DTO_Role dr = null;
		if (rs.first()) {
			dr = new DTO_Role();
			dr.setRoleId(rs.getLong(1));
			dr.setDiscordRoleId(discordid);
			dr.setName(rs.getString(3));
			dr.setColor(rs.getInt(4));
			dr.setPermission(rs.getLong(5));
			dr.setPosition(rs.getInt(6));
			dr.setMentionable(rs.getBoolean(7));
			dr.setLastUpdate(rs.getTimestamp(8));
		}
		
		rs.close();
		ps.close();
		return Optional.ofNullable(dr);
		
	}
	
	public static long newDTO_Role(DTO_Role dr) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO GUILD_ROLES (DISCORD_ROLE_ID, NAME, COLOR, PERMISSIONS, POSITION, MENTIONABLE, LASTUPDATE) values (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, dr.getDiscordRoleId());
		ps.setString(2, dr.getName());
		ps.setInt(3, dr.getColor());
		ps.setLong(4, dr.getPermission());
		ps.setInt(5, dr.getPosition());
		ps.setBoolean(6, dr.isMentionable());
		ps.setTimestamp(7, new Timestamp(dr.getLastUpdate().getTime()));
		
		int result = ps.executeUpdate();
		
		ResultSet genkey = ps.getGeneratedKeys();
		long role_id = -1;
		if (genkey.first()) {
			role_id = genkey.getLong(1);
		}
		dr.setRoleId(role_id);
		genkey.close();
		ps.close();
		
		
		return role_id;
	}
	
	public static boolean newDTO_Log(long eventId, DTO_RoleLog drl) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO GENERIC_GUILD_ROLE_LOG (EVENT_ID, EVENT_TYPE, ROLE) values (?,?,?)");
		ps.setLong(1, eventId);
		ps.setByte(2, drl.getType().getTypeId());
		ps.setLong(3, drl.getRoleId());
		
		boolean success = ps.executeUpdate() != 0;
		ps.close();
		conn.close();
		drl.setEventId(eventId);
		
		return success;
	}
}
