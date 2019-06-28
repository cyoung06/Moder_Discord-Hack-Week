package kr.kro.syeyoung.moder.database;

import java.util.Date;
import java.util.Optional;

import kr.kro.syeyoung.moder.Main;
import net.dv8tion.jda.core.entities.Role;

public class DTO_Role {
	
	private long roleId;
	private long discordRoleId;
	private String name;
	private int color;
	private long permission;
	private int position;
	private boolean mentionable;
	private boolean hoisted;
	private Date lastUpdate;
	
	public long getRoleId() {
		return roleId;
	}
	protected void setRoleId(long id) {
		this.roleId = id;
	}
	public Optional<Role> getRole() {
		return Optional.ofNullable(Main.getInstance().getJDA().getRoleById(roleId));
	}
	public long getDiscordRoleId() {
		return discordRoleId;
	}
	public void setDiscordRoleId(long discordRoleId) {
		this.discordRoleId = discordRoleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public long getPermission() {
		return permission;
	}
	public void setPermission(long permission) {
		this.permission = permission;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public boolean isMentionable() {
		return mentionable;
	}
	public void setMentionable(boolean mentionable) {
		this.mentionable = mentionable;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public boolean isHoisted() {
		return hoisted;
	}
	public void setHoisted(boolean hoisted) {
		this.hoisted = hoisted;
	}
}
