package kr.kro.syeyoung.moder.database;

import java.util.Date;
import java.util.Optional;

import kr.kro.syeyoung.moder.Main;
import net.dv8tion.jda.core.entities.User;

public class DTO_EventLog {
	
	public enum EventType {
		GuildModeration(0), GuildRole(1), User(2), Message(3);
		
		private int typeid;
		
		private EventType(int b) {
			typeid = b;
		}
		
		public byte getTypeId() {
			return (byte) typeid;
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
	
	private Long userId;
	
	private Long guildId;
	
	private Date d;

	public long getEventId() {
		return eventId;
	}
	
	protected void setEventId(long id) {
		this.eventId = id;
	}

	public EventType getType() {
		return type;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getGuildId() {
		return guildId;
	}

	public Date getD() {
		return d;
	}
	
	public Optional<User> getDiscordUser() {
		return Optional.ofNullable(Main.getInstance().getJDA().getUserById(userId));
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setGuildId(Long guildId) {
		this.guildId = guildId;
	}

	public void setD(Date d) {
		this.d = d;
	}
}
