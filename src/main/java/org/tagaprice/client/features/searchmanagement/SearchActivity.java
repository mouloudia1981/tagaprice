package org.tagaprice.client.features.searchmanagement;

import java.util.List;

import org.tagaprice.client.ClientFactory;
import org.tagaprice.client.features.searchmanagement.ISearchView.Presenter;
import org.tagaprice.client.generics.events.InfoBoxShowEvent;
import org.tagaprice.client.generics.events.InfoBoxShowEvent.INFOTYPE;
import org.tagaprice.shared.entities.Address;
import org.tagaprice.shared.entities.BoundingBox;
import org.tagaprice.shared.entities.Document;
import org.tagaprice.shared.exceptions.dao.DaoException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.code.gwt.geolocation.client.Geolocation;
import com.google.code.gwt.geolocation.client.Position;
import com.google.code.gwt.geolocation.client.PositionCallback;
import com.google.code.gwt.geolocation.client.PositionError;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class SearchActivity extends AbstractActivity implements Presenter {

	private SearchPlace _place;
	private ClientFactory _clientFactory;
	private ISearchView _searchView;
	private Address _curAddress;
	private int _searchCount=0;
	
	public SearchActivity(SearchPlace place, ClientFactory clientFactory) {
		
		_place=place;
		_clientFactory=clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.debug("Activity starts...");
		
		_searchView = _clientFactory.getSearchView();
		_searchView.setPresenter(this);
		
		
		
		//TODO TestLocation
		{
			_searchView.addSelectableAddress(new Address("Flossgasse", "1020", "Wien", "at", 48.21657, 16.37456));
		}
		
		
		
		
		panel.setWidget(_searchView);
		
	}

	@Override
	public void goTo(Place place) {
		_clientFactory.getPlaceController().goTo(place);
	}

	@Override
	public void setAddress(Address address) {
		Log.debug("setAddress. "+address);
		_curAddress = address;
		
		
		_searchView.setAddress(_curAddress);
	}
	
	@Override
	public void onFindGpsPosition(){
		//get current location
		Geolocation.getGeolocation().getCurrentPosition(new PositionCallback() {
			
			@Override
			public void onSuccess(Position position) {	
				setAddress(new Address("Current location", "", "", "", position.getCoords().getLatitude(), position.getCoords().getLongitude()));
			}
			
			@Override
			public void onFailure(PositionError error) {
				Log.error("Could not find position:" + error);
				_clientFactory.getEventBus().fireEvent(new InfoBoxShowEvent(SearchActivity.class, "Could not find position", INFOTYPE.ERROR));								
			}
		});
	}

	@Override
	public void onSearch(String searchString, BoundingBox bbox) {
		Log.debug("searchString: "+searchString+", bbox: "+bbox);
		_searchCount++;		
		final int curSearchCount=_searchCount;
		
		_clientFactory.getSearchService().search(searchString.trim(), bbox, new AsyncCallback<List<Document>>() {
			
			@Override
			public void onSuccess(List<Document> response) {
				if(curSearchCount==_searchCount){
					
					_searchView.setSearchResults(response);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				try{
					throw caught;
				}catch (DaoException e){
					Log.warn(e.getMessage());
				}catch (Throwable e){
					Log.error(e.getMessage());
				}
				
			}
		});
		
	}
	

}
