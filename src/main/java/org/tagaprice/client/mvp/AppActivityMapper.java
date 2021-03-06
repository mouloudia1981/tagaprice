package org.tagaprice.client.mvp;

import org.tagaprice.client.ClientFactory;
import org.tagaprice.client.features.accountmanagement.inviteFriends.InviteFriendsActivity;
import org.tagaprice.client.features.accountmanagement.inviteFriends.InviteFriendsPlace;
import org.tagaprice.client.features.accountmanagement.settings.SettingsActivity;
import org.tagaprice.client.features.accountmanagement.settings.SettingsPlace;
import org.tagaprice.client.features.categorymanagement.product.ProductCategoryActivity;
import org.tagaprice.client.features.categorymanagement.product.ProductCategoryPlace;
import org.tagaprice.client.features.categorymanagement.shop.ShopCategoryActivity;
import org.tagaprice.client.features.categorymanagement.shop.ShopCategoryPlace;
import org.tagaprice.client.features.productmanagement.createProduct.CreateProductActivity;
import org.tagaprice.client.features.productmanagement.createProduct.CreateProductPlace;
import org.tagaprice.client.features.receiptmanagement.createReceipt.CreateReceiptActivity;
import org.tagaprice.client.features.receiptmanagement.createReceipt.CreateReceiptPlace;
import org.tagaprice.client.features.receiptmanagement.listReceipts.ListReceiptsActivity;
import org.tagaprice.client.features.receiptmanagement.listReceipts.ListReceiptsPlace;
import org.tagaprice.client.features.searchmanagement.SearchActivity;
import org.tagaprice.client.features.searchmanagement.SearchPlace;
import org.tagaprice.client.features.shopmanagement.createShop.CreateShopActivity;
import org.tagaprice.client.features.shopmanagement.createShop.CreateShopPlace;
import org.tagaprice.client.features.startmanagement.StartActivity;
import org.tagaprice.client.features.startmanagement.StartPlace;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
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
		if (place instanceof CreateProductPlace) {
			Log.debug("return new CreateProductActivity");
			return new CreateProductActivity((CreateProductPlace) place, this.clientFactory);
		}else if (place instanceof CreateShopPlace){
			Log.debug("return new CreateShopActivity");
			return new CreateShopActivity((CreateShopPlace)place, this.clientFactory);
		} else if(place instanceof CreateReceiptPlace){
			Log.debug("return new CreateReceiptActivity");
			return new CreateReceiptActivity((CreateReceiptPlace)place, this.clientFactory);
		}else if(place instanceof ListReceiptsPlace){
			Log.debug("return new ListReceiptsActivity");
			return new ListReceiptsActivity((ListReceiptsPlace)place, this.clientFactory);
		}else if(place instanceof StartPlace){
			Log.debug("return new StartActivity");
			return new StartActivity((StartPlace)place, clientFactory);
		}else if(place instanceof SearchPlace){
			Log.debug("return new SearchPlace");
			return new SearchActivity((SearchPlace)place, clientFactory);
		}else if(place instanceof ProductCategoryPlace){
			Log.debug("return new ProductCategoryPlace");
			return new ProductCategoryActivity((ProductCategoryPlace)place, clientFactory);
		}else if(place instanceof ShopCategoryPlace){
			Log.debug("return new ShopCategoryPlace");
			return new ShopCategoryActivity((ShopCategoryPlace)place, clientFactory); 
		}else if(place instanceof SettingsPlace){
			Log.debug("return new SettingsPlace");
			return new SettingsActivity((SettingsPlace)place, clientFactory); 
		}else if(place instanceof InviteFriendsPlace){
			Log.debug("return new InviteFriendsPlace");
			return new InviteFriendsActivity((InviteFriendsPlace)place, clientFactory);
		}

		else {

			// THIS ELSE IS IMPORTANT TO AVOID FAILURES
			// IF THE PROGRAMER FORGOTT TO RETURN A VALUE
			return null;
		}
	}

}