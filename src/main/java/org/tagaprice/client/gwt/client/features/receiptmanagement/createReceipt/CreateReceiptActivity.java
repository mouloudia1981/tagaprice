package org.tagaprice.client.gwt.client.features.receiptmanagement.createReceipt;

import java.util.ArrayList;
import java.util.Date;

import org.tagaprice.client.gwt.client.ClientFactory;
import org.tagaprice.client.gwt.shared.entities.productmanagement.IProduct;
import org.tagaprice.client.gwt.shared.entities.receiptManagement.IReceipt;
import org.tagaprice.client.gwt.shared.entities.receiptManagement.Receipt;
import org.tagaprice.client.gwt.shared.entities.shopmanagement.IShop;
import org.tagaprice.client.gwt.shared.logging.LoggerFactory;
import org.tagaprice.client.gwt.shared.logging.MyLogger;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class CreateReceiptActivity implements ICreateReceiptView.Presenter, Activity {
	private static final MyLogger _logger = LoggerFactory.getLogger(CreateReceiptActivity.class);

	private CreateReceiptPlace _place;
	private ClientFactory _clientFactory;
	private IReceipt _receipt;
	private ICreateReceiptView _createReceiptView;

	public CreateReceiptActivity(CreateReceiptPlace place, ClientFactory clientFactory) {
		CreateReceiptActivity._logger.log("CreateProductActivity created");
		_place = place;
		_clientFactory = clientFactory;
	}

	@Override
	public String mayStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}


	@Override
	public void goTo(Place place) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		CreateReceiptActivity._logger.log("activity startet");
		_createReceiptView = _clientFactory.getCreateReceiptView();
		_createReceiptView.setPresenter(this);



		if (_place.getId() == 0L) {
			CreateReceiptActivity._logger.log("Create new Receipt");
			_receipt=new Receipt();
			_receipt.setTitle("New Receipt");
			_receipt.setDate(new Date());
			updateView(_receipt);
		}else{
			CreateReceiptActivity._logger.log("Get Receipt: id= "+_place.getId());

			_clientFactory.getReceiptService().getReceipt(_place.getId(), new AsyncCallback<IReceipt>() {

				@Override
				public void onSuccess(IReceipt response) {
					_receipt=response;
					updateView(_receipt);
				}

				@Override
				public void onFailure(Throwable e) {
					CreateReceiptActivity._logger.log("ERROR AT Get Receipt: id= "+_place.getId()+"e:"+ e);

				}
			});
		}




		panel.setWidget(_createReceiptView);
	}

	private void updateView(IReceipt receipt){
		_receipt=receipt;

		_createReceiptView.setTitle(_receipt.getTitle());
		_createReceiptView.setDate(_receipt.getDate());
		_createReceiptView.setAddress(_receipt.getAddress());
		_createReceiptView.setReceiptEntries(_receipt.getReceiptEntries());
	}

	@Override
	public void shopSearchStringHasChanged(String shopSearch) {
		CreateReceiptActivity._logger.log("Start shopSearch: "+shopSearch);

		_clientFactory.getSearchService().searchShop(
				shopSearch,
				_createReceiptView.getBoundingBox(),
				new AsyncCallback<ArrayList<IShop>>() {

					@Override
					public void onSuccess(ArrayList<IShop> response) {

						_createReceiptView.setShopSearchResults(response);
					}

					@Override
					public void onFailure(Throwable e) {
						// TODO Auto-generated method stub
						CreateReceiptActivity._logger.log("shopSearch ERROR: "+e);
					}
				});

	}

	@Override
	public void productSearchStringHasChanged(String productSearch) {
		CreateReceiptActivity._logger.log("Start productSearch: "+productSearch);

		_clientFactory.getSearchService().searchProduct(
				productSearch,
				_createReceiptView.getAddress(),
				new AsyncCallback<ArrayList<IProduct>>() {

					@Override
					public void onSuccess(ArrayList<IProduct> response) {
						_createReceiptView.setProductSearchResults(response);
					}

					@Override
					public void onFailure(Throwable e) {
						// TODO Auto-generated method stub
						CreateReceiptActivity._logger.log("productSearch ERROR: "+e);
					}
				});
	}

}
