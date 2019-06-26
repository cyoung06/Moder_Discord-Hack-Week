package kr.kro.syeyoung.moder.database;

import java.sql.SQLException;

import kr.kro.syeyoung.moder.database.DTO_ModerationLog.EventType;

public class DTO_RoleLog {
	public enum EventType {
		Creation(0), Deletion(1), Color(2), Mentionable(3), Name(4), Permission(5), Position(6);
		
		private int TypeId;
		
		private EventType(int i) {
			this.TypeId = i;
		}
		
		public byte getTypeId() {
			return (byte) TypeId;
		}

		public static EventType getEventTypeByTypeId(byte id) {
			for (EventType et:values()) {
				if (et.getTypeId() == id) {
					return et;
				}
			}
			return null;
		}
	}
	
	private long eventId;
	
	private EventType type;
	
	private long roleId;

	public long getEventId() {
		return eventId;
	}

	protected void setEventId(long id) {
		this.eventId = id;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public DTO_Role getDTORole() throws SQLException {
		return DAO_RoleLog.getRoleByRoleId(roleId).get();
	}
	
	/**
	 * @return The Internally managed role ID. NOT A DISCORD ROLE ID
	 */
	public long getRoleId() {
		return roleId;
	}

	protected void setRoleId(long role) {
		this.roleId = role;
	}
	public void setRole(DTO_Role role) {
		this.roleId = role.getRoleId();
	}
}
