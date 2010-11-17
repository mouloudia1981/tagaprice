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
 * Filename: ShopPreview.java
 * Date: 15.05.2010
 */
package org.tagaprice.client.pages.previews;


import org.tagaprice.client.ImageBundle;
import org.tagaprice.client.widgets.ProgressWidget;
import org.tagaprice.client.widgets.RatingWidget;
import org.tagaprice.shared.entities.Shop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays the most important properties of a Shop.
 * Properties: title, rating, progress, address
 *
 */
public class ShopPagePreview extends APagePreview {
	public interface MyUiBinder extends UiBinder<Widget, ShopPagePreview>{}
	private MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private Shop shopData;
	private boolean editable;
	private RatingWidget ratingWidget;

	@UiField SimplePanel ratingPanel;
	@UiField HorizontalPanel HoPa1;
	@UiField HorizontalPanel HoPa2;
	@UiField Label name;
	@UiField Label street;
	@UiField Label city;
	@UiField Label country;
	@UiField SimplePanel logoPanel;
	@UiField VerticalPanel VePa1;
	@UiField VerticalPanel VePa2;

	/**
	 * 
	 * @param productData 
	 * @param editable 
	 */
	public ShopPagePreview(Shop _shopData, boolean _editable) {
		super();
		shopData=_shopData;
		editable=_editable;
		initWidget(uiBinder.createAndBindUi(this));


		//HoPa1
		HoPa1.setWidth("100%");
		HoPa1.setStyleName("ShopPreview");

		HoPa1.setCellWidth(logoPanel, "40px");
		//HoPa1.setBorderWidth(1);

		//VePa1
		VePa1.setSize("100%", "40px");



		//HoPa2
		HoPa2.setWidth("100%");		
		ratingWidget=new RatingWidget(shopData.getRating(), this.editable);
		ratingPanel.setWidget(ratingWidget);
		HoPa2.setCellWidth(ratingPanel, "80px");

		//VePa2
		HoPa2.setCellHorizontalAlignment(VePa2, HasHorizontalAlignment.ALIGN_RIGHT);
		street.setStyleName("ShopPreview-Street");
		city.setStyleName("ShopPreview-Street");

		//Street
		if(shopData.getAddress()!=null){
			if(shopData.getAddress().getStreet()!=null) street.setText(shopData.getAddress().getStreet());
			if(shopData.getAddress().getCity()!=null)	city.setText(shopData.getAddress().getCity());
		}


		Image progressImage = new Image(ImageBundle.INSTANCE.productPriview()); 
		logoPanel.add(new ProgressWidget(progressImage, 50));
		logoPanel.setHeight(ImageBundle.INSTANCE.productPriview().getHeight()+"px");


		name.setText(shopData.getTitle());

		//TODO Just for a Test
		progressImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("shop/get&id="+shopData.getId());				
			}
		});
		
	}

	@Override
	public void click(){	
		//TODO By clicking on the rating widget, the shopPage should not open.
		
		//History.newItem("shop/get&id="+shopData.getId());
	}
	

	/**
	 * Return current ProductData
	 * @return 
	 */
	public Shop getShopData(){
		if(editable){
			shopData.setRating(ratingWidget.getRating());
		}

		return shopData;
	}

	/**
	 * 
	 * @return Is ProductPreview editable
	 */
	public boolean isEditable(){
		return editable;
	}


}