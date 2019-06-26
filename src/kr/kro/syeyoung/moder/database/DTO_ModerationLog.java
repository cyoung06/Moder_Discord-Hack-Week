package kr.kro.syeyoung.moder.database;

import java.util.Optional;

import net.dv8tion.jda.core.entities.User;
import kr.kro.syeyoung.moder.Main;
import kr.kro.syeyoung.moder.database.DTO_EventLog.EventType;

public class DTO_ModerationLog {
	public enum EventType {
		BAN(0), KICK(1), UNBAN(2);
		
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
	
	private long userId; // The one being moderated
	
	private String reason; // REASON

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

	public Optional<User> getDiscordUser() {
		return Optional.ofNullable(Main.getInstance().getJDA().getUserById(userId));
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
