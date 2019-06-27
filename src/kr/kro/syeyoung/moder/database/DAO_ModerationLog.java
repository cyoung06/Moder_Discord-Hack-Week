package kr.kro.syeyoung.moder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DAO_ModerationLog {
	public static Optional<DTO_ModerationLog> getModerationLogByEventId(long l) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM GENERIC_GUILD_MODERATION_LOG where EVENT_ID = ?");
		ps.setLong(1, l);
		
		DTO_ModerationLog ml = null;
		
		ResultSet rs = ps.getResultSet();
		
		if (rs.first()) {
			ml = new DTO_ModerationLog();
			ml.setEventId(l);
			ml.setType(DTO_ModerationLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			ml.setUserId(rs.getLong(3));
			ml.setReason(rs.getString(4));
		}
		
		rs.close();
		ps.close();
		
		return Optional.ofNullable(ml);
	}
	
	public static List<DTO_ModerationLog> getModerationLogsByGuildIdandBetweenTime(long guild_id, Date start, Date end) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM GENERIC_GUILD_MODERATION_LOG inner join event_log on event_log.EVENT_ID = generic_guild_moderation_log.EVENT_ID where event_log.TIME <= ? and event_log.TIME >= ? and event_log.GUILD_ID = ?");
		ps.setLong(3, guild_id);
		ps.setDate(2, new java.sql.Date(start.getTime()));
		ps.setDate(1, new java.sql.Date(end.toInstant().plus(1, ChronoUnit.DAYS).toEpochMilli()));
		
		DTO_ModerationLog ml = null;
		
		ResultSet rs = ps.getResultSet();
		
		List<DTO_ModerationLog> list = new ArrayList<>();
		
		while (rs.next()) {
			ml = new DTO_ModerationLog();
			ml.setEventId(rs.getLong(1));
			ml.setType(DTO_ModerationLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			ml.setUserId(rs.getLong(3));
			ml.setReason(rs.getString(4));
			list.add(ml);
		}
		
		return list;
	}//
	
	public static List<DTO_ModerationLog> getModerationLogsByGuildIdandBetweenTimeOnType(long guild_id, byte type, Date start, Date end) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM GENERIC_GUILD_MODERATION_LOG inner join event_log on event_log.EVENT_ID = generic_guild_moderation_log.EVENT_ID where event_log.TIME <= ? and event_log.TIME >= ? and event_log.GUILD_ID = ? and generic_guild_moderation_log.EVENT_TYPE = ?");
		ps.setLong(3, guild_id);
		ps.setDate(2, new java.sql.Date(start.getTime()));
		ps.setDate(1, new java.sql.Date(end.toInstant().plus(1, ChronoUnit.DAYS).toEpochMilli()));
		ps.setByte(4, type);
		
		DTO_ModerationLog ml = null;
		
		ResultSet rs = ps.getResultSet();
		
		List<DTO_ModerationLog> list = new ArrayList<>();
		
		while (rs.next()) {
			ml = new DTO_ModerationLog();
			ml.setEventId(rs.getLong(1));
			ml.setType(DTO_ModerationLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			ml.setUserId(rs.getLong(3));
			ml.setReason(rs.getString(4));
			list.add(ml);
		}
		
		return list;
	}
	
	
	public static boolean newModerationLog(long event_id, DTO_ModerationLog log) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO GENERIC_GUILD_MODERATION_LOG (EVENT_ID, EVENT_TYPE, USER_ID, REASON) values (?,?,?,?)");
		ps.setLong(1, event_id);
		ps.setByte(2, log.getType().getTypeId());
		ps.setLong(3, log.getUserId());
		if (log.getReason() == null) ps.setNull(4, Types.VARCHAR);
		else ps.setString(4, log.getReason());
		
		boolean successed= ps.executeUpdate() != 0;
		ps.close();
		
		log.setEventId(event_id);
		return successed;
	}
}
