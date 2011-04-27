package org.tagaprice.client.features.shopmanagement.createShop.devView;

import java.util.List;

import org.tagaprice.client.features.shopmanagement.createShop.ICreateShopView;
import org.tagaprice.client.generics.widgets.AddressSelecter;
import org.tagaprice.shared.entities.Address;
import org.tagaprice.shared.entities.shopmanagement.Shop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

public class CreateShopViewImpl extends Composite implements ICreateShopView {
	interface CreateShopViewImpleUiBinder extends
	UiBinder<Widget, CreateShopViewImpl> {
	}

	private static CreateShopViewImpleUiBinder uiBinder = GWT
	.create(CreateShopViewImpleUiBinder.class);

	private Presenter _presenter;


	@UiField
	TextBox _name;

	@UiField
	TextBox _branding;


	@UiField
	AddressSelecter _address;

	@UiField
	Button _saveButton;

	@UiField
	FlexTable _receiptEntriesTable;

	PopupPanel _brandiPop = new PopupPanel(true);

	Shop _brand;

	public CreateShopViewImpl() {
		initWidget(CreateShopViewImpl.uiBinder.createAndBindUi(this));


		//Branding Search
		_branding.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				_presenter.brandingSearch(_branding.getText());
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.tagaprice.client.features.shopmanagement.createShop.devView.ICreateShopView#setPresenter(org.tagaprice.client.features.shopmanagement.createShop.ICreateShopView.Presenter)
	 */
	@Override
	public void setPresenter(Presenter presenter) {
		_presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see org.tagaprice.client.features.shopmanagement.createShop.devView.ICreateShopView#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		_name.setText(title);

	}

	@UiHandler("_saveButton")
	public void onSaveEvent(ClickEvent event) {
		this._presenter.onSaveEvent();
	}


	/* (non-Javadoc)
	 * @see org.tagaprice.client.features.shopmanagement.createShop.devView.ICreateShopView#getTitle()
	 */
	@Override
	public String getShopTitle() {
		return _name.getText();
	}

	@Override
	public void setShopTitle(String title) {
		_name.setText(title);
	}


	@Override
	public void setAddress(Address address) {
		_address.setCurrentAddress(address);
	}

	@Override
	public void setBrandingSearchResults(List<Shop> results) {
		VerticalPanel vePa = new VerticalPanel();

		for(final Shop s:results){
			Label l = new Label(s.getTitle());
			vePa.add(l);

			l.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					setBranding(s);

				}
			});
		}

		Label l = new Label("has no Branding");
		l.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				setBranding(null);

			}
		});
		vePa.add(l);

		_brandiPop.setWidget(vePa);
		_brandiPop.showRelativeTo(_branding);

	}

	@Override
	public Address getAddress() {
		return _address.getAddress();
	}

	@Override
	public Shop getBranding() {
		return _brand;
	}

	@Override
	public void setBranding(Shop branding) {
		_brand=branding;
		if(branding==null)_branding.setText("");
		else _branding.setText(_brand.getTitle());
		_brandiPop.hide();
	}


}
