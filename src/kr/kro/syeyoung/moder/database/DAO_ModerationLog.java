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
	}
}
