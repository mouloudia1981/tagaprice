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
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.event.MapMoveEndListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.tagaprice.client.generics.widgets.IStatisticChangeHandler;
import org.tagaprice.client.generics.widgets.IStatisticSelecter;
import org.tagaprice.shared.entities.BoundingBox;
import org.tagaprice.shared.entities.Unit;
import org.tagaprice.shared.entities.productmanagement.Product;
import org.tagaprice.shared.entities.receiptManagement.Currency;
import org.tagaprice.shared.entities.searchmanagement.StatisticResult;
import org.tagaprice.shared.entities.shopmanagement.Shop;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class StatisticSelecter extends Composite implements IStatisticSelecter {

	TYPE _type = null;
	VerticalPanel vePa1 = new VerticalPanel();
	DatePicker beginDate = new DatePicker();
	DatePicker endDate = new DatePicker();

	VerticalPanel resultList = new VerticalPanel();

	//OSM
	Map _osmMap;
	VectorOptions vectorOptions = new VectorOptions();
	Vector layer = new Vector("shops",vectorOptions);
	IStatisticChangeHandler _handler;

	public StatisticSelecter() {
		initWidget(vePa1);

		//******** INIT OSM Vector ************/
		//Style
		Style style = new Style();
		style.setStrokeColor("#000000");
		style.setStrokeWidth(2);
		style.setFillColor("#00FF00");
		style.setFillOpacity(0.5);
		style.setPointRadius(8);
		style.setStrokeOpacity(0.8);
		vectorOptions.setStyle(style);

		//inti Maps
		HorizontalPanel hoPa1 = new HorizontalPanel();
		MapOptions defaultMapOptions = new MapOptions();
		MapWidget omapWidget = new MapWidget("300px", "200px", defaultMapOptions);
		OSM osm_2 = OSM.Mapnik("Mapnik");   // Label for menu 'LayerSwitcher'
		osm_2.setIsBaseLayer(true);
		_osmMap = omapWidget.getMap();
		_osmMap.addLayer(osm_2);
		hoPa1.add(omapWidget);

		_osmMap.zoomTo(14);
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
				sendChangeRequest();
			}
		});


		beginDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> arg0) {
				sendChangeRequest();
			}
		});

		endDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> arg0) {
				sendChangeRequest();
			}
		});




	}

	private void setShopResults(List<StatisticResult> results){
		layer.destroyFeatures();
		resultList.clear();
		HashMap<Product, ArrayList<StatisticResult>> sortedByProductList = new HashMap<Product, ArrayList<StatisticResult>>();
		for(StatisticResult s:results){
			if(!sortedByProductList.containsKey(s.getProduct())){
				sortedByProductList.put(s.getProduct(), new ArrayList<StatisticResult>());
			}

			sortedByProductList.get(s.getProduct()).add(s);
			//resultList.add(new Label(s.getProduct().getTitle()+", "+s.getPrice().getPrice()+""+s.getPrice().getCurrency()));
		}

		for(Product key: sortedByProductList.keySet()){
			VerticalPanel vePa = new VerticalPanel();

			BigDecimal cheapest = null;
			Currency currency = Currency.euro;
			Unit unit = new Unit();
			for(StatisticResult sr: sortedByProductList.get(key)){
				vePa.add(new Label(" - "+
						sr.getPrice().getPrice().toString()+""+
						sr.getPrice().getCurrency()+"/"+
						sr.getPackage().getQuantity().getQuantity().toString()+""+
						sr.getPackage().getQuantity().getUnit().getTitle()));


				if(cheapest==null)
				{
					currency=sr.getPrice().getCurrency();
					unit = sr.getPackage().getQuantity().getUnit();
					cheapest=sr.getPrice().getPrice().divide(sr.getPackage().getQuantity().getQuantity(), 5, BigDecimal.ROUND_HALF_EVEN);
				}
				else
					if(-1==cheapest.compareTo(sr.getPrice().getPrice().divide(sr.getPackage().getQuantity().getQuantity(), 5, BigDecimal.ROUND_HALF_EVEN))){
						cheapest=sr.getPrice().getPrice().divide(sr.getPackage().getQuantity().getQuantity(), 5, BigDecimal.ROUND_HALF_EVEN);
					}

			}
			resultList.add(new HTML("<a href=\"#product:/null/id/"+key.getId()+"\" >"+cheapest.toString()+""+currency+"/1"+unit.getTitle()+" "+key.getTitle()+"</a>"));
			resultList.add(vePa);

			/* TODO Get data from child shops
			LonLat l = new LonLat(key.getAddress().getLon(), key.getAddress().getLat());
			l.transform("EPSG:4326", "EPSG:900913");
			Point point = new Point(l.lon(), l.lat());
			VectorFeature pointFeature = new VectorFeature(point);

			layer.addFeature(pointFeature);
			 */
		}
	}

	public void setProductResults(List<StatisticResult> results){
		layer.destroyFeatures();
		resultList.clear();
		HashMap<Shop, ArrayList<StatisticResult>> sortedByShopList = new HashMap<Shop, ArrayList<StatisticResult>>();
		for(StatisticResult s:results){
			if(!sortedByShopList.containsKey(s.getShop())){
				sortedByShopList.put(s.getShop(), new ArrayList<StatisticResult>());
			}

			sortedByShopList.get(s.getShop()).add(s);

		}

		//Draw List
		for(Shop key: sortedByShopList.keySet()){
			VerticalPanel vePa = new VerticalPanel();

			BigDecimal cheapest = null;
			Currency currency = Currency.euro;
			Unit unit = new Unit();
			for(StatisticResult sr: sortedByShopList.get(key)){
				vePa.add(new Label(" - "+
						sr.getPrice().getPrice().toString()+""+
						sr.getPrice().getCurrency()+"/"+
						sr.getPackage().getQuantity().getQuantity().toString()+""+
						sr.getPackage().getQuantity().getUnit().getTitle()));


				if(cheapest==null)
				{
					currency=sr.getPrice().getCurrency();
					unit = sr.getPackage().getQuantity().getUnit();
					cheapest=sr.getPrice().getPrice().divide(sr.getPackage().getQuantity().getQuantity(), 5, BigDecimal.ROUND_HALF_EVEN);
				}
				else
					if(-1==cheapest.compareTo(sr.getPrice().getPrice().divide(sr.getPackage().getQuantity().getQuantity(), 5, BigDecimal.ROUND_HALF_EVEN))){
						cheapest=sr.getPrice().getPrice().divide(sr.getPackage().getQuantity().getQuantity(), 5, BigDecimal.ROUND_HALF_EVEN);
					}

			}
			resultList.add(new HTML("<a href=\"#shop:/null/id/"+key.getId()+"\" >"+cheapest.toString()+""+currency+"/1"+unit.getTitle()+" "+key.getTitle()+"</a>"));
			resultList.add(vePa);

			LonLat lonLat = key.getAddress().getPos().toLonLat();
			lonLat.transform("EPSG:4326", "EPSG:900913");
			Point point = new Point(lonLat.lon(), lonLat.lat());
			VectorFeature pointFeature = new VectorFeature(point);

			layer.addFeature(pointFeature);
		}

	}


	@Override
	public void setStatisticResults(List<StatisticResult> results) {

		if(_type!=null){
			if(_type==TYPE.PRODUCT){
				setProductResults(results);
			}else if(_type==TYPE.SHOP){
				setShopResults(results);
			}
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

	private void sendChangeRequest(){
		if(_handler!=null){


			_handler.onChange(getBoundingBox(), getBeginDate(), getEndDate());
		}
	}

	@Override
	public void setType(TYPE type) {
		_type=type;
	}

	@Override
	public BoundingBox getBoundingBox() {
		LonLat southWest = new LonLat(_osmMap.getExtent().getLowerLeftX(), _osmMap.getExtent().getLowerLeftY());
		LonLat northEast = new LonLat(_osmMap.getExtent().getUpperRightX(), _osmMap.getExtent().getUpperRightY());
		southWest.transform("EPSG:900913","EPSG:4326");
		northEast.transform("EPSG:900913","EPSG:4326");
		
		
		return new BoundingBox(
				southWest.lat(),
				southWest.lon(),
				northEast.lat(),
				northEast.lon());
	}

	@Override
	public Date getBeginDate() {
		return beginDate.getValue();
	}

	@Override
	public Date getEndDate() {
		return endDate.getValue();
	}

	@Override
	public void setLatLon(double lat, double lon) {
		LonLat lonLat = new LonLat(lon,lat);
		lonLat.transform("EPSG:4326", "EPSG:900913");	
		_osmMap.setCenter(lonLat);
	}

	@Override
	public void setMapVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoading() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

}
