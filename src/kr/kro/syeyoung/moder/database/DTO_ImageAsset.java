package kr.kro.syeyoung.moder.database;

public class DTO_ImageAsset {
	private long objectId;
	private String origin;
	private int width;
	private int height;
	
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
}
