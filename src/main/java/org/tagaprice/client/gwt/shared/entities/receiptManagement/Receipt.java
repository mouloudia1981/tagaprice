package org.tagaprice.client.gwt.shared.entities.receiptManagement;

import java.util.ArrayList;
import java.util.Date;
import org.tagaprice.client.gwt.shared.entities.AEntity;
import org.tagaprice.client.gwt.shared.entities.IRevisionId;
import org.tagaprice.client.gwt.shared.entities.accountmanagement.User;
import org.tagaprice.client.gwt.shared.entities.shopmanagement.IAddress;

/**
 * A single Receipt
 * Holds information about the creator {@link User} of the receipt,
 * the date when the receipt was created, a reference to the shop {@link Shop} where the receipt is from
 * and all {@link ReceiptEntry} of a receipt.
 * 
 * @author Helga Weik (kaltra)
 *
 */
public class Receipt extends AEntity<IReceipt> implements IReceipt {



	private static final long serialVersionUID = -1411130663050015079L;

	private IAddress _address;
	private Date _date;
	private ArrayList<IReceiptEntry> _receiptEntries = new ArrayList<IReceiptEntry>();
	private User _user;


	public Receipt() {
		super();

	}

	/**
	 * Create new Receipt. Used on the Client
	 * @param title the title of the receipt
	 * @param date date of the receipt
	 * @param address the shop-address
	 * @param receiptEntries all entries plus price
	 */
	public Receipt(String title, Date date, IAddress address) {
		setTitle(title);
		setDate(date);
		setAddress(address);
	}


	/**
	 * Update or select Receipt. Used on Client and Server
	 * @param title
	 * @param id
	 * @param date the date and time when a receipt was created
	 * @param shop {@link Shop} where the receipt is from
	 * @param user {@link User} who created the receipt
	 * @param receiptEntries  {@link ReceiptEntry}
	 */

	public Receipt(String title, Long id, Date date, IAddress address, User user) {
		setTitle(title);
		setDate(date);
		setAddress(address);
		setUser(user);
	}


	@Override
	public void addReceiptEntriy(IReceiptEntry receiptEntry) {
		receiptEntry.setReceipt(this);
		_receiptEntries.add(receiptEntry);

	}

	@Override
	public IAddress getAddress() {
		return _address;
	}


	@Override
	public Date getDate() {
		return _date;
	}

	@Override
	public ArrayList<IReceiptEntry> getReceiptEntries() {
		return _receiptEntries;
	}

	public IRevisionId getShopId(){
		return getAddress().getRevisionID();
	}

	@Override
	public User getUser() {
		return _user;
	}

	@Override
	public void setAddress(IAddress address) {
		_address=address;
	}

	@Override
	public void setDate(Date date) {
		_date = date;
	}

	@Override
	public void setReceiptEntries(ArrayList<IReceiptEntry> receiptEntries) {
		_receiptEntries.clear();
		_receiptEntries.addAll(receiptEntries);
	}

	public void setShopId(IRevisionId shopId){
		// TODO
	}

	@Override
	public void setUser(User user) {
		_user = user;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Receipt [_date=" + _date + ", _address=" + _address + "]";
	}





}