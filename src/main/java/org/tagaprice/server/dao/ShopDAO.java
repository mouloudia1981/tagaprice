package org.tagaprice.server.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.tagaprice.server.DBConnection;
import org.tagaprice.shared.ShopData;
import org.tagaprice.shared.exception.InvalidLocaleException;
import org.tagaprice.shared.exception.NotFoundException;
import org.tagaprice.shared.exception.RevisionCheckException;

public class ShopDAO implements DAOClass<ShopData> {
	private static ShopDAO instance = null;
	private DBConnection db;
	private EntityDAO entityDAO;
	private CountryDAO countryDAO;
	
	private ShopDAO(DBConnection db) {
		this.db = db;
	}
	
	public static ShopDAO getInsance(DBConnection db) {
		if (instance == null) {
			instance = new ShopDAO(db);
			instance.entityDAO = EntityDAO.getInstance(db);
			instance.countryDAO = CountryDAO.getInstance(db);
		}
		return instance;
	}

	@Override
	public void get(ShopData shop) throws SQLException, NotFoundException {
		//Get Entity Data
		entityDAO.get(shop);
		
		// TODO implement fetching of a specific shop revision
		//Get Shop Data
		String sql = "SELECT type_id, imageUrl, lat, lng, street, city, country_code " +
				"FROM shopRevision " +
				"INNER JOIN ENTITY ON(ent_id = shop_id AND current_revision = rev) " +
				"WHERE shop_id = ?";
		PreparedStatement pstmt = db.prepareStatement(sql);
		pstmt.setLong(1, shop.getId());
		ResultSet res = pstmt.executeQuery();
		
		if (!res.next()) throw new NotFoundException("Shop not found");
		
		if (res.getString("type_id") != null) shop.setTypeId(res.getLong("type_id"));
		else shop.setTypeId(null);
		
		shop.setImageSrc(res.getString("imageurl"));
		
		if (res.getString("lat") != null && res.getString("lng") != null) {
			shop.getAddress().setCoordinates(res.getDouble("lat"), res.getDouble("lng"));
		}
		else shop.getAddress().setCoordinates(null, null);
		
		shop.getAddress().setAddress(
				res.getString("street"),
				res.getString("city"),
				countryDAO.get(res.getString("country_code")));
	}

	@Override
	public void save(ShopData shop) throws SQLException, NotFoundException,
			RevisionCheckException, InvalidLocaleException {
		PreparedStatement pstmt;
		
		entityDAO.save(shop);
		if (shop.getRev() == 1) {
			// create a new Product
			pstmt = db.prepareStatement("INSERT INTO shop (shop_id) VALUES (?)");
			pstmt.setLong(1, shop.getId());
			pstmt.executeUpdate();
		}
		else if (shop.getRev() < 1) throw new RevisionCheckException("invalid shop revision: "+shop.getRev());

		String sql = "INSERT INTO shopRevision (shop_id, rev, type_id, imageUrl, lat, lng, street, city, country_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		pstmt = db.prepareStatement(sql);
		pstmt.setLong(1, shop.getId());
		pstmt.setInt(2, shop.getRev());
		
		if (shop.getTypeId() != null) pstmt.setLong(3, shop.getTypeId());
		else pstmt.setNull(3, Types.BIGINT);

		pstmt.setString(4, shop.getImageSrc());

		if (shop.getLat() != null && shop.getLng() != null) {
			pstmt.setDouble(5, shop.getLat());
			pstmt.setDouble(6, shop.getLng());
		}
		else {
			pstmt.setNull(5, Types.DOUBLE);
			pstmt.setNull(6, Types.DOUBLE);
		}
		
		pstmt.setString(7, shop.getAddress().getStreet());
		pstmt.setString(8, shop.getAddress().getCity());
		pstmt.setString(9, shop.getAddress().getCountry().getCode());

		pstmt.executeUpdate();
	}

}