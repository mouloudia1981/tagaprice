package org.tagaprice.client.features.productmanagement.listProducts.devView;

import java.util.ArrayList;
import java.util.List;

import org.tagaprice.client.features.productmanagement.listProducts.ListProductsView;
import org.tagaprice.client.generics.ColumnDefinition;
import org.tagaprice.shared.logging.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * The Class ListProductsViewImpl<T> uses UIBinder and the template ListProductsViewImpl.ui.xml
 * 
 * 
 * @param <T>
 */
public class ListProductsViewImpl<T> extends Composite implements ListProductsView<T> {

	Presenter presenter;

	private static MyLogger logger = LoggerFactory.getLogger(ListProductsViewImpl.class);

	/**
	 * UiBinder Magic...
	 * 
	 * @author Martin
	 * 
	 */
	@SuppressWarnings("rawtypes")
	interface ListProductsViewImplUiBinder extends UiBinder<Widget, ListProductsViewImpl> {
	}

	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * UiBinder Magic again...
	 */
	private static ListProductsViewImplUiBinder uiBinder = GWT.create(ListProductsViewImplUiBinder.class);

	/**
	 * and again...
	 */
	@UiField
	FlexTable table;

	@UiField
	Button addProduct;

	@UiField
	TextBox textbox;

	public FlexTable getTable() {
		return this.table;
	}

	@UiHandler("search")
	public void onSearchButtonClicked(ClickEvent event) {
		ListProductsViewImpl.logger.log("Search Button clicked");
		this.presenter.onSearch(this.textbox.getText());
	}

	@UiHandler("addProduct")
	public void onAddProductButtonClicked(ClickEvent event) {
		ListProductsViewImpl.logger.log("Button addProduct clicked");
		this.presenter.onAddProduct();
	}

	@UiHandler("table")
	public void onTableEntryClicked(ClickEvent event) {
		ListProductsViewImpl.logger.log("Entry on Table clicked");
		this.presenter.onEditProduct(this.table.getCellForEvent(event).getRowIndex());
	}

	ArrayList<ColumnDefinition<T>> columnDefinitions;

	public ListProductsViewImpl() {
		super();
		this.initWidget(ListProductsViewImpl.uiBinder.createAndBindUi(this));
	}

	/**
	 * Sets the ColumnDefinitions
	 * 
	 * @param columnDefinitions
	 */
	public void setColumnDefinitions(ArrayList<ColumnDefinition<T>> columnDefinitions) {
		this.columnDefinitions = columnDefinitions;
	}

	@Override
	public void setData(List<T> data) {
		this.table.removeAllRows();
		for (int i = 0; i < data.size(); i++) {
			T elem = data.get(i);
			for (int j = 0; j < this.columnDefinitions.size(); j++) {
				ColumnDefinition<T> actualColumnDefinition = this.columnDefinitions.get(j);
				this.table.setWidget(i, j, actualColumnDefinition.render(elem));
			}
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}