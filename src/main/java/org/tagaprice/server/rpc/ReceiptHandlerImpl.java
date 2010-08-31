/*
 * Copyright 2010 TagAPrice.org
 * 
 * Licensed under the Creative Commons License. You may not
 * use this file except in compliance with the License. 
 *
 * http://creativecommons.org/licenses/by-nc/3.0/
*/

/**
 * Project: TagAPrice
 * Filename: ReceiptHandlerImpl.java
 * Date: 30.05.2010
*/
package org.tagaprice.server.rpc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.tagaprice.server.DBConnection;
import org.tagaprice.server.dao.LocaleDAO;
import org.tagaprice.server.dao.LoginDAO;
import org.tagaprice.server.dao.ReceiptDAO;
import org.tagaprice.shared.Address;
import org.tagaprice.shared.Country;
import org.tagaprice.shared.Price;
import org.tagaprice.shared.ProductData;
import org.tagaprice.shared.Quantity;
import org.tagaprice.shared.ReceiptData;
import org.tagaprice.shared.ShopData;
import org.tagaprice.shared.Unit;
import org.tagaprice.shared.exception.InvalidLocaleException;
import org.tagaprice.shared.exception.InvalidLoginException;
import org.tagaprice.shared.exception.NotFoundException;
import org.tagaprice.shared.exception.RevisionCheckException;
import org.tagaprice.shared.rpc.ReceiptHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ReceiptHandlerImpl extends RemoteServiceServlet implements ReceiptHandler {

	private DBConnection db;
	private LoginDAO loginDao;
	private ReceiptDAO receiptDao;
	private int localeId;

	
	public ReceiptHandlerImpl() {
		try {
			db = new DBConnection();
			loginDao = new LoginDAO(db);
			receiptDao = new ReceiptDAO(db);
			localeId = new LocaleDAO(db).get("English").getId();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		} catch (InvalidLocaleException e) {
			throw new IllegalArgumentException(e);
		}
		
	}
	
	
	@Override
	public ReceiptData get(ReceiptData data) throws IllegalArgumentException, InvalidLoginException {
		
		
		try {
			data._setCreatorId(loginDao.getId(getSid()));
			receiptDao.get(data);
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		} catch (NotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		
		
		System.out.println("data: "+data.getId()+", "+data.getTitle());
		
		return data;

		
		
		
	}

	@Override
	public ReceiptData save(ReceiptData data) throws IllegalArgumentException, InvalidLoginException {
		if(data==null){
			
			try {
				data = new ReceiptData("default title", localeId, loginDao.getId(getSid()), new Date(), 1, null, null, true);
			} catch (SQLException e) {
				throw new IllegalArgumentException("SQLException: "+e);
			}		 
			
		}
		
		//create new Draft
		try {
			receiptDao.save(data);
		}  catch (SQLException e){
			throw new IllegalArgumentException("SQLException: "+e);
		} catch (NotFoundException e) {
			throw new IllegalArgumentException("NotFoundException: "+e);		
		} catch (RevisionCheckException e) {
			throw new IllegalArgumentException("RevisionCheckException: "+e);
		} catch (InvalidLocaleException e) {
			throw new IllegalArgumentException("InvalidLocaleException: "+e);
		}

		return data;
	}

	private String getSid() throws InvalidLoginException{
		return loginDao.getSid(this.getThreadLocalRequest().getCookies());	
	}


	
}
