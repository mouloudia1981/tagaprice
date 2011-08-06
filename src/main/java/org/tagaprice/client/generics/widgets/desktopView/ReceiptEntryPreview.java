package org.tagaprice.client.generics.widgets.desktopView;

import java.math.BigDecimal;

import org.tagaprice.client.generics.widgets.CurrencySelecter;
import org.tagaprice.client.generics.widgets.IMorphWidget.Type;
import org.tagaprice.shared.entities.receiptManagement.ReceiptEntry;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ReceiptEntryPreview extends PackagePreview {

	private HorizontalPanel _hoPa1 = new HorizontalPanel();
	private MorphWidget _price = new MorphWidget();
	private CurrencySelecter _currency = new CurrencySelecter();
	private boolean _readonly = true;
	private ReceiptEntry _receiptEntry;
	
	
	public ReceiptEntryPreview(ReceiptEntry receiptEntry) {
		super(receiptEntry.getPackage().getProduct(), receiptEntry.getPackage());
		_receiptEntry=receiptEntry;
		
		//price
		_price.config(Type.DOUBLE, true, null, true, false);
		_price.setValue(receiptEntry.getPrice().getPrice().toPlainString());
		_price.setWidth("50px");
		_hoPa1.add(_price);
		
		//Currency
		_currency.setCurrency(receiptEntry.getPrice().getCurrency());
		_hoPa1.add(_currency);
		
		_vePaPricePack.add(_hoPa1);
	}

	public boolean isReadOnly() {
		return _readonly;
	}
	
	public void setReadOnly(boolean read) {
		_readonly=read;
		_price.setReadOnly(_readonly);
		
		System.out.println("getQuantity: "+_receiptEntry.getPackage().getQuantity().getQuantity());
		
		if(_receiptEntry.getPackage().getQuantity().getQuantity().equals(new BigDecimal("0.0"))){
			super.setReadOnly(_readonly);
		}
		
		
		
	}
}
