package org.tagaprice.client.generics.widgets.devView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.event.MapMoveEndListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.tagaprice.client.generics.widgets.IStatisticChangeHandler;
import org.tagaprice.client.generics.widgets.IStatisticSelecter;
import org.tagaprice.shared.entities.BoundingBox;
import org.tagaprice.shared.entities.searchmanagement.StatisticResult;
import org.tagaprice.shared.entities.shopmanagement.Shop;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class StatisticSelecter extends Composite implements IStatisticSelecter {

	VerticalPanel vePa1 = new VerticalPanel();
	DatePicker beginDate = new DatePicker();
	DatePicker endDate = new DatePicker();

	VerticalPanel resultList = new VerticalPanel();

	//OSM
	Map _osmMap;
	Vector layer = new Vector("shops");
	IStatisticChangeHandler _handler;

	public StatisticSelecter() {
		initWidget(vePa1);

		//inti Maps
		HorizontalPanel hoPa1 = new HorizontalPanel();
		MapOptions defaultMapOptions = new MapOptions();
		MapWidget omapWidget = new MapWidget("300px", "200px", defaultMapOptions);
		OSM osm_2 = OSM.Mapnik("Mapnik");   // Label for menu 'LayerSwitcher'
		osm_2.setIsBaseLayer(true);
		_osmMap = omapWidget.getMap();
		_osmMap.addLayer(osm_2);
		hoPa1.add(omapWidget);

		LonLat lonLat = new LonLat(16.37692,48.21426);
		lonLat.transform("EPSG:4326", "EPSG:900913");
		_osmMap.setCenter(lonLat, 15);
		_osmMap.addLayer(layer);

		//Datens
		vePa1.add(new Label("Set date range"));

		hoPa1.add(beginDate);
		hoPa1.add(endDate);
		setDate(new Date(), new Date());
		vePa1.add(hoPa1);


		//Add resultList
		vePa1.add(new Label("DetailList"));
		vePa1.add(resultList);



		_osmMap.addMapMoveEndListener(new MapMoveEndListener() {

			@Override
			public void onMapMoveEnd(MapMoveEndEvent eventObject) {
				sendSomethingChanged();
			}
		});


		beginDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> arg0) {
				sendSomethingChanged();
			}
		});

		endDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> arg0) {
				sendSomethingChanged();
			}
		});




	}

	@Override
	public void setStatisticResults(List<StatisticResult> results) {
		layer.destroyFeatures();
		resultList.clear();
		HashMap<Shop, ArrayList<StatisticResult>> sortedByShopList = new HashMap<Shop, ArrayList<StatisticResult>>();
		for(StatisticResult s:results){
			if(!sortedByShopList.containsKey(s.getShop())){
				sortedByShopList.put(s.getShop(), new ArrayList<StatisticResult>());
			}

			sortedByShopList.get(s.getShop()).add(s);

			/*
			resultList.add(new Label(
					s.getPrice().getPrice().toString()+""+
					s.getPrice().getCurrency()+" "+
					s.getQuantity().getQuantity().toString()+""+
					s.getQuantity().getUnit().getTitle()+" "+
					s.getShop().getTitle()));

			LonLat l = new LonLat(s.getShop().getAddress().getLng(), s.getShop().getAddress().getLat());
			l.transform("EPSG:4326", "EPSG:900913");
			Point point = new Point(l.lon(), l.lat());
			VectorFeature pointFeature = new VectorFeature(point);
			layer.addFeature(pointFeature);
			 */
		}

		//Draw List
		for(Shop key: sortedByShopList.keySet()){
			VerticalPanel vePa = new VerticalPanel();

			BigDecimal cheapest = null;
			for(StatisticResult sr: sortedByShopList.get(key)){
				vePa.add(new Label(" - "+
						sr.getPrice().getPrice().toString()+""+
						sr.getPrice().getCurrency()+" "+
						sr.getQuantity().getQuantity().toString()+""+
						sr.getQuantity().getUnit().getTitle()));


				if(cheapest==null)
				{
					cheapest=sr.getPrice().getPrice().multiply(new BigDecimal("1000")).divide(sr.getQuantity().getQuantity(), BigDecimal.ROUND_HALF_EVEN);
				}
				else
					if(-1==cheapest.compareTo(sr.getPrice().getPrice().multiply(new BigDecimal("1000")).divide(sr.getQuantity().getQuantity(), BigDecimal.ROUND_HALF_EVEN))){
						cheapest=sr.getPrice().getPrice().multiply(new BigDecimal("1000")).divide(sr.getQuantity().getQuantity(), BigDecimal.ROUND_HALF_EVEN);
					}

			}
			resultList.add(new Label(cheapest.toString()+" "+key.getTitle()));
			resultList.add(vePa);

			LonLat l = new LonLat(key.getAddress().getLng(), key.getAddress().getLat());
			l.transform("EPSG:4326", "EPSG:900913");
			Point point = new Point(l.lon(), l.lat());
			VectorFeature pointFeature = new VectorFeature(point);
			layer.addFeature(pointFeature);
		}

	}

	@Override
	public void setDate(Date begin, Date end) {
		beginDate.setValue(begin);
		endDate.setValue(end);
	}

	@Override
	public void addStatisticChangeHandler(IStatisticChangeHandler handler) {
		_handler=handler;
	}

	private void sendSomethingChanged(){
		if(_handler!=null){

			LonLat southWest = new LonLat(_osmMap.getExtent().getLowerLeftX(), _osmMap.getExtent().getLowerLeftY());
			LonLat northEast = new LonLat(_osmMap.getExtent().getUpperRightX(), _osmMap.getExtent().getUpperRightY());
			southWest.transform("EPSG:900913","EPSG:4326");
			northEast.transform("EPSG:900913","EPSG:4326");


			_handler.onChange(new BoundingBox(
					southWest.lat(),
					southWest.lon(),
					northEast.lat(),
					northEast.lon()), beginDate.getValue(), endDate.getValue());
		}
	}

}