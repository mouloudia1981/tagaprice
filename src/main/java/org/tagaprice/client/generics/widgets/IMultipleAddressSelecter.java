package org.tagaprice.client.generics.widgets;

import java.util.ArrayList;

import org.tagaprice.shared.entities.Address;
import org.tagaprice.shared.entities.shopmanagement.IShop;
import com.google.gwt.user.client.ui.IsWidget;

public interface IMultipleAddressSelecter extends IsWidget {

	/**
	 * Set the current address (position) of the user
	 * @param address current address (position) of the user
	 */
	public void setCurrentAddress(Address address);

	/**
	 * Set some {@link IAddress} to the {@link IAddressSeleter}
	 * @param address that will be added to the {@link IAddressSeleter}
	 */
	public void setShops(ArrayList<IShop> address);

	/**
	 * 
	 * @param shop
	 */
	public void addShop(IShop shop);


	/**
	 * Returns all {@link IAddress} includes in the {@link IAddressSeleter}
	 * @return all {@link IAddress} includes in the {@link IAddressSeleter}
	 */
	public ArrayList<IShop> getShops();
}
