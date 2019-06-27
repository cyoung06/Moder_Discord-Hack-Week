package kr.kro.syeyoung.moder.database;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

public class DTO_Presence {
	public enum ActivityType {
		Game(0), Streaming(1), Listening(2);
		private int TypeId;
		
		private ActivityType(int i) {
			this.TypeId = i;
		}
		
		public byte getTypeId() {
			return (byte) TypeId;
		}

		public static ActivityType getActivityTypeByTypeId(byte id) {
			for (ActivityType et:values()) {
				if (et.getTypeId() == id) {
					return et;
				}
			}
			return null;
		}
	}
	
	private long presenceId;
	private String name;
	private ActivityType type;
	
	private Boolean isRich;
	private String url;
	private Date since;
	private String details, state;
	private Integer Flag;
	private Long BigImage;
	private Long SmallImage;
	private String BigTooltip;
	private String SmallTooltip;
	
	
	
	public long getPresenceId() {
		return presenceId;
	}
	protected void setPresenceId(long id) {
		this.presenceId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ActivityType getType() {
		return type;
	}
	public void setType(ActivityType type) {
		this.type = type;
	}
	public Boolean getIsRich() {
		return isRich;
	}
	public void setIsRich(Boolean isRich) {
		this.isRich = isRich;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getSince() {
		return since;
	}
	public void setSince(Date since) {
		this.since = since;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getFlag() {
		return Flag;
	}
	public void setFlag(Integer flag) {
		Flag = flag;
	}
	
	//TODO Asset Object
	public Long getBigImageId() {
		return BigImage;
	}
	public DTO_ImageAsset getBigImage() throws SQLException {
		return DAO_Assets.getImageAssetByObjectId(BigImage).get();
	}
	public void setBigImage(Long bigImage) {
		BigImage = bigImage;
	}
	public Long getSmallImageId() {
		return SmallImage;
	}
	public DTO_ImageAsset getSmallImage() throws SQLException {
		return DAO_Assets.getImageAssetByObjectId(SmallImage).get();
	}
	public void setSmallImage(Long smallImage) {
		SmallImage = smallImage;
	}
	public String getBigTooltip() {
		return BigTooltip;
	}
	public void setBigTooltip(String bigTooltip) {
		BigTooltip = bigTooltip;
	}
	public String getSmallTooltip() {
		return SmallTooltip;
	}
	public void setSmallTooltip(String smallTooltip) {
		SmallTooltip = smallTooltip;
	}
}
