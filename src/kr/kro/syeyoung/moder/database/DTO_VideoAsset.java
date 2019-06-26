package kr.kro.syeyoung.moder.database;

public class DTO_VideoAsset {
	private long objectId;
	private String origin;
	private int width;
	private int height;
	private int duration;
	public long getObjectId() {
		return objectId;
	}
	protected void setObjectId(long id) {
		this.objectId = id;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
