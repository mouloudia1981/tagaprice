package org.tagaprice.client.features.productmanagement.createProduct;

import java.util.Date;
import java.util.List;

import org.tagaprice.client.ClientFactory;
import org.tagaprice.client.generics.events.InfoBoxDestroyEvent;
import org.tagaprice.client.generics.events.InfoBoxShowEvent;
import org.tagaprice.client.generics.events.InfoBoxShowEvent.INFOTYPE;
import org.tagaprice.shared.entities.BoundingBox;
import org.tagaprice.shared.entities.productmanagement.*;
import org.tagaprice.shared.entities.searchmanagement.StatisticResult;
import org.tagaprice.shared.exceptions.UserNotLoggedInException;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class CreateProductActivity implements ICreateProductView.Presenter, Activity {

	private CreateProductPlace _place;
	private ClientFactory _clientFactory;
	private Product _product;
	private ICreateProductView _createProductView;

	public CreateProductActivity(CreateProductPlace place, ClientFactory clientFactory) {
		Log.debug("CreateProductActivity created");
		_place = place;
		_clientFactory = clientFactory;
	}

	@Override
	public void goTo(Place place) {
		_clientFactory.getPlaceController().goTo(place);
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
	public void onCategorySelectedEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSaveEvent() {
		Log.debug("Save Product");


		//Get data from View
		_product.setTitle(_createProductView.getProductTitle());
		_product.setCategory(_createProductView.getCategory());
		_product.setUnit(_createProductView.getUnit());
		_product.setPackages(_createProductView.getPackages());

		this._clientFactory.getProductService().saveProduct(_clientFactory.getAccountPersistor().getSessionId(), _product, new AsyncCallback<Product>() {

			@Override
			public void onFailure(Throwable caught) {
				try{
					throw caught;
				}catch (UserNotLoggedInException e){
					Log.warn(e.getMessage());
				}catch (Throwable e){
					Log.error(e.getMessage());
				}

			}

			@Override
			public void onSuccess(Product result) {
				Log.debug("Product save successfull");
				updateView(result);
			}
		});

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTitleSelectedEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitSelectedEvent() {
		Log.debug("Unit has changed");

	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		_product = new Product();
		Log.debug("activity startet");

		_createProductView = _clientFactory.getCreateProductView();
		_createProductView.setPresenter(this);


		if (_place.getId() == null) {
			Log.debug("Create new Product");

			updateView(_product);
			panel.setWidget(_createProductView);
			// panel.setWidget(new Label("Create new Product"));
		} else {
			Log.debug("Get Product: id=" + _place.getId() + ", rev: "
					+ _place.getRevision());
			// panel.setWidget(new
			// Label("Get Product: id="+_place.getRevisionId().getId()+", rev: "+_place.getRevisionId().getRevision()));


			Log.debug("Load Categories...");


			this._clientFactory.getProductService().getProduct(_place.getId(), _place.getRevision(), new AsyncCallback<Product>() {

				@Override
				public void onFailure(Throwable caught) {
					Log.error("ERROR at getProduct: "+caught.getMessage());
				}

				@Override
				public void onSuccess(Product result) {
					Log.debug("Get Product sucessfull id: "+result.getId());
					updateView(result);
					panel.setWidget(_createProductView);
				}
			});


		}

	}

	private void updateView(Product product) {
		_product = product;
		ICreateProductView view = this._clientFactory.getEditProductView();
		view.setTitle(product.getTitle());
		view.setCategory(product.getCategory());
		view.setUnit(product.getUnit());

		view.setPackages(product.getPackages());
	}

	@Override
	public void onStatisticChangedEvent(BoundingBox bbox, Date begin, Date end) {
		Log.debug("onStatisticChangedEvent: bbox: "+bbox+", begin: "+begin+", end: "+end);
		_clientFactory.getEventBus().fireEvent(new InfoBoxShowEvent(CreateProductActivity.class, "Getting statistic data: ", INFOTYPE.INFO,0));

		_clientFactory.getSearchService().searchProductPrices(_product.getId(), bbox, begin, end, new AsyncCallback<List<StatisticResult>>() {

			@Override
			public void onSuccess(List<StatisticResult> response) {
				_clientFactory.getEventBus().fireEvent(new InfoBoxDestroyEvent(CreateProductActivity.class, INFOTYPE.INFO));
				_createProductView.setStatisticResults(response);
			}

			@Override
			public void onFailure(Throwable e) {
				_clientFactory.getEventBus().fireEvent(new InfoBoxShowEvent(CreateProductActivity.class, "searchproblem: "+e, INFOTYPE.ERROR,0));
				Log.error("searchproblem: "+e);
			}
		});

	}

}
