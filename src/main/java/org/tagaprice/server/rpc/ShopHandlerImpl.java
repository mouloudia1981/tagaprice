package org.tagaprice.server.rpc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import org.tagaprice.server.DBConnection;
import org.tagaprice.server.dao.ShopDAO;
import org.tagaprice.server.dao.postgres.LoginDAO;
import org.tagaprice.shared.PropertyValidator;
import org.tagaprice.shared.ShopData;
import org.tagaprice.shared.Type;
import org.tagaprice.shared.exception.InvalidLocaleException;
import org.tagaprice.shared.exception.InvalidLoginException;
import org.tagaprice.shared.exception.NotFoundException;
import org.tagaprice.shared.exception.RevisionCheckException;
import org.tagaprice.shared.rpc.ShopHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ShopHandlerImpl extends RemoteServiceServlet implements ShopHandler{
	ShopDAO sDao;
	LoginDAO loginDao; 
	
	public ShopHandlerImpl() {
		DBConnection db;
		try {
			db = new DBConnection();
			sDao = new ShopDAO(db);
			loginDao = new LoginDAO(db);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		
	}
	
	@Override
	public ShopData get(long id) throws IllegalArgumentException {
		
		ShopData sd = new ShopData();				
		sd._setId(id);
		
		try {
			sDao.get(sd);
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		} catch (NotFoundException e) {
			throw new IllegalArgumentException(e);
		}		
		return sd;
	}

	@Override
	public ShopData save(ShopData data) throws IllegalArgumentException, InvalidLoginException {
		// TODO Auto-generated method stub		
		TypeHandlerImpl th = new TypeHandlerImpl();
		
		if(PropertyValidator.isValid(th.get(new Type(data.getTypeId())), data.getProperties())){		
			try {				
				data.setCreatorId(loginDao.getId(getSid()));
				sDao.save(data);				
			} catch (SQLException e) {
				throw new IllegalArgumentException(e);
			} catch (NotFoundException e) {
				throw new IllegalArgumentException(e);
			} catch (RevisionCheckException e) {
				throw new IllegalArgumentException(e);
			} catch (InvalidLocaleException e) {
				throw new IllegalArgumentException(e);
			}			
			
		}else{
			System.out.println("save InVALID");
		}
		

		return data;
	}
	
	private String getSid() throws InvalidLoginException{
		return loginDao.getSid(this.getThreadLocalRequest().getCookies());
	}

}
