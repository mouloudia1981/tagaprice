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
 * Filename: TaPManagerImpl.java
 * Date: 18.05.2010
*/
package org.tagaprice.client;

import java.util.ArrayList;

import org.tagaprice.client.PriceMapWidget.PriceMapType;
import org.tagaprice.client.SearchWidget.SearchType;
import org.tagaprice.shared.BoundingBox;
import org.tagaprice.shared.Entity;
import org.tagaprice.shared.PriceData;
import org.tagaprice.shared.ProductData;
import org.tagaprice.shared.ReceiptData;
import org.tagaprice.shared.ShopData;
import org.tagaprice.shared.Type;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface for the DAO Manager
 *
 */
public interface TaPManager {
	
	/**
	 * Starts Product Page with id
	 * @param id
	 */
	public void showProductPage(Long id);
	
	
	/**
	 * Creates empty productPage
	 * @param title
	 */
	public void newProductPage(String title);
	
	
	/**
	 * Starts Shop Page with id
	 * @param id
	 */
	public void showShopPage(Long id);
	
	/**
	 * Creates empty shopPage. 
	 * @param title
	 */
	public void newShopPage(String title);
	
	
	/**
	 * Starts Receipt Page
	 * @param id
	 */
	public void showReceiptPage(final Long id);
	
	

	
	/**
	 *  
	 * @param id Unique Receipt Id (If Id=0 you get an empty Draft-Container with a new draft-id )
	 * @param draft Is receipt a draft.
	 * @return Returns a ReceiptContainer
	 */
	public void getReceipt(Long id, AsyncCallback<ReceiptData> response);
	
	/**
	 * Returns product by ID.
	 * @param id
	 * @return
	 */
	public void getProduct(Long id, AsyncCallback<ProductData> response);
	
	
	
	/**
	 * Save, create or update a product.
	 * @param data
	 * @param response
	 * @return
	 */
	public void saveProduct(ProductData data, AsyncCallback<ProductData> response); 
	
	/**
	 * Get price by Shop, Product, ProductGroup, ShopGroup
	 * @param id
	 * @param bbox
	 * @param type
	 * @param response
	 */
	public void getPrice(Long id, BoundingBox bbox, PriceMapType type, AsyncCallback<ArrayList<PriceData>> response);
	
	/**
	 * Returns shop by ID.
	 * @param id
	 * @return
	 */
	public ShopData getShop(Long id);
	
	
	/**
	 * Returns shop by location
	 * @param lat Latitude
	 * @param lng Longitude
	 * @return
	 */
	public ShopData getShop(double lat, double lng);
	
	/**
	 * 
	 * @param receiptContainer
	 * @param draft
	 */
	public void saveReceipt(ReceiptData receiptData);
	
	/**
	 * Returns the UIManager
	 * @return
	 */
	public UIManager getUIManager();

	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public void getType(long id, AsyncCallback<Type> response);
	
	/**
	 * 
	 * @param type
	 * @param response
	 */
	public void getTypeList(Type type, AsyncCallback<ArrayList<Type>> response);
	
	/**
	 * Start a new user Registration
	 * @param varificationCode If not null user has just being verified.
	 */
	public void showUserRegistrationPage(String verificationCode);
	
	/**
	 * 
	 * @param sText
	 * @param callback
	 */
	public void search(String sText, SearchType searchType, AsyncCallback<ArrayList<Entity>> callback);
	
	/**
	 * 
	 * @param sText
	 * @param bbox
	 * @param callback
	 */
	public void search(String sText, SearchType searchType, BoundingBox bbox, AsyncCallback<ArrayList<Entity>> callback);
	
	/**
	 * 
	 * @param sText
	 * @param shopData
	 * @param callback
	 */
	public void search(String sText, ShopData shopData, AsyncCallback<ArrayList<Entity>> callback);
	
	
	
	
	
}
