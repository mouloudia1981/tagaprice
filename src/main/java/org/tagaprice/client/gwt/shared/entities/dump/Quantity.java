package org.tagaprice.client.gwt.shared.entities.dump;

import org.tagaprice.client.gwt.shared.entities.Unit;

/**
 * A {@link Quantity} defines in which way a user can buy a {@link Product}.
 */
public class Quantity implements IQuantity {

	private static final long serialVersionUID = -8569323869233802603L;

	private double _quantity;
	private Unit _unit;

	/**
	 * This constructor is used by the serialization algorithm
	 */
	public Quantity() {}

	/**
	 * TODO should quantity be in int?
	 * Create a Quantity object defines by the quantity and a {@link Unit}
	 * @param quantity the quantity
	 * @param unit
	 */
	public Quantity(double quantity, Unit unit) {
		this._quantity = quantity;
		this._unit = unit;
	}

	@Override
	public void setQuantity(double quantity) {
		this._quantity = quantity;
	}

	@Override
	public double getQuantity() {
		return this._quantity;
	}

	@Override
	public void setUnit(Unit unit) {
		this._unit = unit;
	}

	@Override
	public Unit getUnit() {
		return this._unit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Quantity [_quantity=" + _quantity + ", _unit=" + _unit + "]";
	}



}
