package kr.kro.syeyoung.moder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAO_UserLog {
	public static Optional<DTO_UserLog> getUserLogByEventId(long eventId) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM GENERIC_GUILD_USER_LOG where EVENT_ID = ?");
		ps.setLong(1, eventId);
		
		ResultSet rs = ps.executeQuery();
		DTO_UserLog dul = null;
		if (rs.first()) {
			dul = new DTO_UserLog();
			dul.setEventId(eventId);
			dul.setType(DTO_UserLog.EventType.getEventTypeByTypeId(rs.getByte(2)));
			dul.setUserId(rs.getLong(3));
			long l = rs.getLong(4);;
			dul.setMemberId(rs.wasNull() ? null : l);
			dul.setStatus(DTO_UserLog.OnlineStatus.getOnlineStatusByTypeId(rs.getByte(5)));
			dul.setPresenceId(rs.getLong(6));
		}
		
		rs.close();
		ps.close();
		
		return Optional.ofNullable(dul);
	}
	
	public static Optional<DTO_Member> getUserByInternalMemberId(long memberId) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM MEMBER_OBJECT where MEMBER_ID = ?");
		ps.setLong(1, memberId);
		
		ResultSet rs = ps.executeQuery();
		DTO_Member dm = null;
		if (rs.first()) {
			dm = new DTO_Member();
			dm.setMemberId(memberId);
			dm.setUserId(rs.getLong(2));
			dm.setUserName(rs.getString(3));
			dm.setDiscriminator(rs.getShort(4));
			dm.setMemberName(rs.getString(5));
			dm.setDefaultAvatar(rs.getBoolean(6));
			long l = rs.getLong(7);
			dm.setAvatar(rs.wasNull() ? null : l);
			
			List<Long> roles = new ArrayList<Long>();
			
			Connection conn2 = DataSource.getConnection();
			PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM MEMBER_OBJECT_has_GUILD_ROLES where MEMBER_OBJECT_MEMBER_ID=?");
			ps2.setLong(1, dm.getMemberId());
			ResultSet rs2 = ps2.executeQuery();
			
			while (rs2.next()) {
				roles.add(rs2.getLong(2));
			}

			rs2.close();
			ps2.close();
			conn2.close();
			dm.setRoles(roles);
		}
		
		rs.close();
		ps.close();
		return Optional.ofNullable(dm);
	}
	
	
	public static boolean newUserLog(long eventId, DTO_UserLog log) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO GENERIC_GUILD_USER_LOG (EVENT_ID, EVENT_TYPE, USER_ID, MEMBER, ONLINE_STATUS, PRESEMCE) value (? ,? ,? ,? ,? ,?)");
		ps.setLong(1, eventId);
		ps.setByte(2, log.getType().getTypeId());
		ps.setLong(3, log.getUserId());
		if (log.getMemberId() == null) ps.setNull(4, Types.BIGINT);
		else ps.setLong(4, log.getMemberId());	
		ps.setByte(5, log.getStatus().getTypeId());
		if (log.getPresenceId() == null) ps.setNull(6, Types.BIGINT);
		else ps.setLong(6, log.getPresenceId());	
		
		boolean successed = ps.executeUpdate() != 0;
		
		ps.close();
		conn.close();
		
		return successed;
	}
	
	public static long newMember(DTO_Member user) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO MEMBER_OBJECT (USER_ID, USER_NAME, USER_DISCRIMINATOR, MEMBER_NAME, DEFAULT_AVATAR, AVATAR) values (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, user.getUserId());
		ps.setString(2, user.getUserName());
		ps.setShort(3, user.getDiscriminator());
		if (user.getMemberName() == null) ps.setNull(4, Types.VARCHAR);
		else ps.setString(4, user.getMemberName());
		ps.setBoolean(5, user.isDefaultAvatar());
		if (user.getAvatarId() == null) ps.setNull(5, Types.BIGINT);
		ps.setLong(6, user.getAvatarId());
		
		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		long mid = -1;
		if (rs.first()) {
			mid = rs.getLong(1);
		}
		rs.close();
		ps.close();
		
		return mid;
			
	}
}
