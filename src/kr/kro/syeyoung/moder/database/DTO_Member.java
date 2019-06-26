package kr.kro.syeyoung.moder.database;

import java.util.List;
import java.util.Optional;

import kr.kro.syeyoung.moder.Main;
import net.dv8tion.jda.core.entities.User;

public class DTO_Member {
	private long memberId;
	private long userId;
	private String userName;
	private int discriminator;
	private String memberName;
	private boolean defaultAvatar;
	private Long Avatar;
	private List<Long> roleIds;
	
	public long getMemberId() {
		return memberId;
	}
	protected void setMemberId(long id) {
		this.memberId = id;
	}
	public long getUserId() {
		return userId;
	}
	public Optional<User> getUser() {
		return Optional.ofNullable(Main.getInstance().getJDA().getUserById(userId));
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getDiscriminator() {
		return discriminator;
	}
	public void setDiscriminator(int discriminator) {
		this.discriminator = discriminator;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public boolean isDefaultAvatar() {
		return defaultAvatar;
	}
	public void setDefaultAvatar(boolean defaultAvatar) {
		this.defaultAvatar = defaultAvatar;
	}
	
	public List<Long> getRoleIds() {
		return roleIds;
	}
	// TODO Roles
	public List<DTO_Role> getRoles() {
		return null;
	}
	
	public void addRole(DTO_Role role) {
		roleIds.add(role.getRoleId());
	}
	public void removeRole(DTO_Role role) {
		roleIds.remove(role.getRoleId());
	}
	
	
	//TODO Asset Object
	public Optional<DTO_ImageAsset> getAvatar() {
		return Optional.ofNullable(null);
	}
	public Long getAvatarId() {
		return Avatar;
	}
	public void setAvatar(Long avatar) {
		Avatar = avatar;
	}
}
