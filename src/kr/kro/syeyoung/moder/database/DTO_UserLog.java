package kr.kro.syeyoung.moder.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import kr.kro.syeyoung.moder.Main;
import net.dv8tion.jda.core.entities.User;

public class DTO_UserLog {
	public enum EventType {
		Game(0), OnlineStatus(1), Avatar(2), Discriminator(3), Name(4),
		Join(5), Leave(6), RoleAdd(7), RoleRemove(8);
		
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
	
	public enum OnlineStatus {
		ONLINE(0), DND(1) /*Do not Disturb*/, IDLE(2) /*AFK*/, OFFLINE(3);
		private int TypeId;
		
		private OnlineStatus(int i) {
			this.TypeId = i;
		}
		
		public byte getTypeId() {
			return (byte) TypeId;
		}

		public static OnlineStatus getOnlineStatusByTypeId(byte id) {
			for (OnlineStatus et:values()) {
				if (et.getTypeId() == id) {
					return et;
				}
			}
			return null;
		}
	}
	
	private long eventId;
	private EventType type;
	private long userId;
	private Long memberId; // Internally managed one
	private OnlineStatus status;
	private Long presence;
	
	
	
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
	public long getUserId() {
		return userId;
	}
	public Optional<User> getDiscordUser() {
		return Optional.ofNullable(Main.getInstance().getJDA().getUserById(userId));
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public OnlineStatus getStatus() {
		return status;
	}
	public void setStatus(OnlineStatus status) {
		this.status = status;
	}
	public Long getPresenceId() {
		return presence;
	}
	
	//TODO get Presence and Return
	public DTO_Presence getPresence() {
		return null;
	}
	public void setPresence(DTO_Presence presence) {
		this.presence = presence.getPresenceId();
	}

	protected void setPresenceId(long long1) {
		this.presence = long1;
	}
	
}
