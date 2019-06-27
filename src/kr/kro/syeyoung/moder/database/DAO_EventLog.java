package kr.kro.syeyoung.moder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DAO_EventLog {
	public static Optional<DTO_EventLog> getEventLogById(long id) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM EVENT_LOG where EVENT_ID = ?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		DTO_EventLog log = null;
		if (rs.next()) {
			log = new DTO_EventLog();
			log.setEventId(rs.getLong(1));
			log.setType(DTO_EventLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			log.setUserId(rs.getLong(3));
			long val = rs.getLong(4);
			log.setGuildId(rs.wasNull() ? null : val);
			log.setD(rs.getDate(5));
		}
		rs.close();
		ps.close();
		return Optional.ofNullable(log);
	}
	
	public static List<DTO_EventLog> findEventLogsByGuildId(long gid) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM EVENT_LOG where GUILD_ID = ?");
		ps.setLong(1, gid);
		
		ResultSet rs = ps.getResultSet();
		List<DTO_EventLog> logs = new LinkedList<DTO_EventLog>();
		while (rs.next()) {

			DTO_EventLog log = new DTO_EventLog();
			log.setEventId(rs.getLong(1));
			log.setType(DTO_EventLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			log.setUserId(rs.getLong(3));
			long val = rs.getLong(4);
			log.setGuildId(rs.wasNull() ? null : val);
			log.setD(rs.getDate(5));
			
			logs.add(log);
		}
		rs.close();
		ps.close();
		return logs;
	}
	

	public static List<DTO_EventLog> findEventLogsByGuildIdAndType(long gid, DTO_EventLog.EventType type) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM EVENT_LOG where GUILD_ID = ? AND EVENT_TYPE = ?");
		ps.setLong(1, gid);
		ps.setByte(2, type.getTypeId());
		
		ResultSet rs = ps.getResultSet();
		List<DTO_EventLog> logs = new LinkedList<DTO_EventLog>();
		while (rs.next()) {

			DTO_EventLog log = new DTO_EventLog();
			log.setEventId(rs.getLong(1));
			log.setType(DTO_EventLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			log.setUserId(rs.getLong(3));
			long val = rs.getLong(4);
			log.setGuildId(rs.wasNull() ? null : val);
			log.setD(rs.getDate(5));
			
			logs.add(log);
		}
		rs.close();
		ps.close();
		return logs;
	}
	
	/**
	 * 
	 * @param log The DTO_EventLog
	 * @return eventId
	 * @throws SQLException
	 */
	public static long newEventLog(DTO_EventLog log) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO EVENT_LOG (EVENT_TYPE, USER_ID, GUILD_ID, TIME) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		ps.setByte(1, log.getType().getTypeId());
		if (log.getUserId() == null) ps.setNull(2, Types.BIGINT);
		else ps.setLong(2, log.getUserId());
		if (log.getGuildId() == null) ps.setNull(3, Types.BIGINT);
		else ps.setLong(3, log.getGuildId());
		ps.setTimestamp(4, new Timestamp(log.getD().getTime()));
		
		long eventId = 0;
		if (ps.executeUpdate() != 0) {
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			eventId = rs.getLong(1);
			rs.close();
		}
		log.setEventId(eventId);
		ps.close();
		return eventId;
	}
}
