package kr.kro.syeyoung.moder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DAO_Assets {
	public static Optional<DTO_ImageAsset> getImageAssetByObjectId(long oid) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM IMAGE_OBJECT where OBJECT_ID = ?");
		ps.setLong(1, oid);
		
		DTO_ImageAsset dia = null;
		ResultSet rs = ps.getResultSet();
		if (rs.first()) {
			dia = new DTO_ImageAsset();
			dia.setObjectId(oid);
			dia.setOrigin(rs.getString(2));
			dia.setWidth(rs.getInt(3));
			dia.setHeight(rs.getInt(4));
		}
		rs.close();
		ps.close();
		
		return Optional.ofNullable(dia);
	}
	
	public static Optional<DTO_VideoAsset> getVideoAssetByObjectId(long oid) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM VIDEO_OBJECT where OBJECT_ID = ?");
		ps.setLong(1, oid);
		
		DTO_VideoAsset dia = null;
		ResultSet rs = ps.getResultSet();
		if (rs.first()) {
			dia = new DTO_VideoAsset();
			dia.setObjectId(oid);
			dia.setOrigin(rs.getString(2));
			dia.setWidth(rs.getInt(3));
			dia.setHeight(rs.getInt(4));
			dia.setDuration(rs.getInt(5));
		}
		rs.close();
		ps.close();
		
		return Optional.ofNullable(dia);
	}
	
	public static long newImageAsset(DTO_ImageAsset ia) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT * INTO IMAGE_OBJECT (ORIGIN, WIDTH, HEIGHT) values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, ia.getOrigin());
		ps.setInt(2, ia.getWidth());
		ps.setInt(3, ia.getHeight());
		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		long key = -1;
		if (rs.first()) {
			key = rs.getLong(1);
			ia.setObjectId(key);
		}
		rs.close();
		ps.close();
		return key;
	}
	
	public static long newVideoAsset(DTO_VideoAsset ia) throws SQLException {
		Connection conn = DataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT * INTO VIDEO_OBJECT (ORIGIN, WIDTH, HEIGHT,DURATION) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, ia.getOrigin());
		ps.setInt(2, ia.getWidth());
		ps.setInt(3, ia.getHeight());
		ps.setInt(4, ia.getDuration());
		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		long key = -1;
		if (rs.first()) {
			key = rs.getLong(1);
			ia.setObjectId(key);
		}
		rs.close();
		ps.close();
		return key;
	}
}
