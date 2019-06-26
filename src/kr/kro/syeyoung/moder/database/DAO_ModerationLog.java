package kr.kro.syeyoung.moder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DAO_ModerationLog {
	public static Optional<DTO_ModerationLog> getModerationLogByEventId(long l) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM GENERIC_GUILD_MOERATION_LOG where EVENT_ID = ?");
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
		
		return Optional.ofNullable(ml);
	}
	
	public static boolean newModerationLog(long event_id, DTO_ModerationLog log) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO GENERIC_GUILD_MODERATION_LOG (EVENT_ID, EVENT_TYPE, USER_ID, REASON) values (?,?,?,?)");
		ps.setLong(1, event_id);
		ps.setByte(2, log.getType().getTypeId());
		ps.setLong(3, log.getUserId());
		ps.setString(4, log.getReason());
		
		boolean successed= ps.executeUpdate() != 0;
		ps.close();
		return successed;
	}
}
