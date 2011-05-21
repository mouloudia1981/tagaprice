package org.tagaprice.client.mvp;

import org.tagaprice.client.ClientFactory;
import org.tagaprice.client.features.accountmanagement.login.LoginActivity;
import org.tagaprice.client.features.accountmanagement.login.LoginPlace;
import org.tagaprice.client.features.accountmanagement.register.RegisterActivity;
import org.tagaprice.client.features.accountmanagement.register.RegisterPlace;
import org.tagaprice.client.features.productmanagement.createProduct.CreateProductActivity;
import org.tagaprice.client.features.productmanagement.createProduct.CreateProductPlace;
import org.tagaprice.client.features.productmanagement.listProducts.*;
import org.tagaprice.client.features.receiptmanagement.createReceipt.CreateReceiptActivity;
import org.tagaprice.client.features.receiptmanagement.createReceipt.CreateReceiptPlace;
import org.tagaprice.client.features.receiptmanagement.listReceipts.ListReceiptsActivity;
import org.tagaprice.client.features.receiptmanagement.listReceipts.ListReceiptsPlace;
import org.tagaprice.client.features.shopmanagement.createShop.CreateShopActivity;
import org.tagaprice.client.features.shopmanagement.createShop.CreateShopPlace;
import org.tagaprice.client.features.shopmanagement.listShops.*;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.*;
import com.google.gwt.place.shared.Place;

/**
 * Maps Places to Activities.
 * 
 */
public class AppActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	public AppActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("I was asked for an activity...");
		// TODO make this gin
		if (place instanceof ListProductsPlace) {
			Log.debug("return new ListProductsActivity");
			return new ListProductsActivity((ListProductsPlace) place, this.clientFactory);
		} else if (place instanceof CreateProductPlace) {
			Log.debug("return new CreateProductActivity");
			return new CreateProductActivity((CreateProductPlace) place, this.clientFactory);
		}else if(place instanceof LoginPlace){
			Log.debug("return new LoginActivity");
			return new LoginActivity((LoginPlace)place, this.clientFactory);
		}else if (place instanceof CreateShopPlace){
			Log.debug("return new CreateShopActivity");
			return new CreateShopActivity((CreateShopPlace)place, this.clientFactory);
		} else if(place instanceof ListShopsPlace) {
			Log.debug("return new ListShopsActivity");
			return new ListShopsActivity((ListShopsPlace) place, this.clientFactory);
		}else if(place instanceof CreateReceiptPlace){
			Log.debug("return new CreateReceiptActivity");
			return new CreateReceiptActivity((CreateReceiptPlace)place, this.clientFactory);
		}else if(place instanceof RegisterPlace){
			Log.debug("return new RegisterActivity");
			return new RegisterActivity((RegisterPlace)place, this.clientFactory);
		}if(place instanceof ListReceiptsPlace){
			Log.debug("return new ListReceiptsActivity");
			return new ListReceiptsActivity((ListReceiptsPlace)place, this.clientFactory);
		}

		else {

			// THIS ELSE IS IMPORTANT TO AVOID FAILURES
			// IF THE PROGRAMER FORGOTT TO RETURN A VALUE
			return null;
		}
	}

}