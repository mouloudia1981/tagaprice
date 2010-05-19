/*
 * Copyright 2010 TagAPrice.org
 * 
 * Licensed under the Creative Commons License. You may not
 * use this file except in compliance with the License. 
 *
 * http://creativecommons.org/licenses/by-nc/3.0/
*/

/**
 * Project: TagAPriceUI
 * Filename: ProductPreviewContainer.java
 * Date: 14.05.2010
*/
package org.tagaprice.shared;

import java.util.ArrayList;

/**
 * Contains all important information to represent a
 * product. 
 *
 */
public class ProductData implements Entity{
	private static final long serialVersionUID = 1L;

	private int id;
	private int brandId;
	private int typeId;
	private String name;
	private String imageSrc;
	private int progress; //In percent 0-100
	private int rating; //in percent 0-100
	private int price; //In Cents
	private String currency;
	private String quantitiy;
	private String unit;
	private boolean hasReceipt;
	private ArrayList<ProductData> properties = new ArrayList<ProductData>(); 
	
	/**
	 * 
	 * @param id
	 * @param brandId
	 * @param typeId
	 * @param name
	 * @param imageSrc
	 * @param progress
	 * @param rating
	 * @param price
	 * @param currency
	 * @param quantitiy
	 * @param unit
	 */
	public ProductData(
			int id,
			int brandId,
			int typeId,
			String name,
			String imageSrc,
			int progress,
			int rating,
			int price,
			String currency,
			String quantitiy,
			String unit) {
		
		this(
				id,
				brandId,
				typeId,
				name,
				imageSrc,
				progress,
				rating,
				price,
				currency,
				quantitiy,
				unit,
				true);
	}
	

	/**
	 * 
	 * @param id
	 * @param brandId
	 * @param typeId
	 * @param name
	 * @param imageSrc
	 * @param progress
	 * @param rating
	 * @param price
	 * @param currency
	 * @param quantitiy
	 * @param unit
	 * @param hasReceipt
	 */
	public ProductData(
			int id,
			int brandId,
			int typeId,
			String name,
			String imageSrc,
			int progress,
			int rating,
			int price,
			String currency,
			String quantitiy,
			String unit,
			boolean hasReceipt) {
		
		setId(id);
		setBrandId(brandId);
		setTypeId(typeId);
		setName(name);
		setImageSrc(imageSrc);
		setPrice(price);
		setRating(rating);
		setCurrency(currency);
		setQuantitiy(quantitiy);
		setUnit(unit);
		setHasReceipt(hasReceipt);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	/**
	 * @return the brandId
	 */
	public int getBrandId() {
		return brandId;
	}


	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}


	/**
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}


	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}


	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getImageSrc(){
		return imageSrc;
	}	
	
	/**
	 * 
	 * @param imageSrc
	 */
	public void setImageSrc(String imageSrc){
		this.imageSrc=imageSrc;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getProgress(){
		return progress;
	}
	
	/**
	 * 
	 * @param progress
	 */
	public void setProgress(int progress){
		this.progress=progress;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getRating(){
		return rating;
	}
	
	/**
	 * 
	 * @param rating
	 */
	public void setRating(int rating){
		this.rating=rating;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getPrice() {
		return price;
	}
	
	/**
	 * 
	 * @param price
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}	
	
	/**
	 * 
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getQuantitiy() {
		return quantitiy;
	}
	
	/**
	 * 
	 * @param quantitiy
	 */
	public void setQuantitiy(String quantitiy) {
		this.quantitiy = quantitiy;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUnit() {
		return unit;
	}
	
	/**
	 * 
	 * @param unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasReceipt() {
		return hasReceipt;
	}
	
	/**
	 * 
	 * @param hasReceipt
	 */
	public void setHasReceipt(boolean hasReceipt) {
		this.hasReceipt = hasReceipt;
	}


	/**
	 * @return the properties
	 */
	public ArrayList<ProductData> getProperties() {
		return properties;
	}


	/**
	 * @param properties the properties to set
	 */
	public void setProperties(ArrayList<ProductData> properties) {
		this.properties = properties;
	}
	
	
	
}
