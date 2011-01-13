package org.tagaprice.core.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "productrevision")
@SecondaryTables({ @SecondaryTable(name = "entityrevision") })
public class ProductRevision implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long _id = null;
	private Integer _revisionNumber = null;
	private String _title;
	private Date _createdAt = null;
	private Account _creator = null;
	private Unit _unit;
	private Double _amount;
	private Category _category;
	private String _imageURL;

	public ProductRevision() {
	}

	public ProductRevision(Long id, Integer revisionNumber, String title, Date createdAt, Account creator, Unit unit, Double amount, Category category, String imageURL) {
		_id = id;
		_title = title;
		_createdAt = createdAt;
		_revisionNumber = revisionNumber;
		_creator = creator;
		_category = category;
		_imageURL = imageURL;
	}

	@Id
	@Column(name = "ent_id")
	public Long getId() {
		return _id;
	}
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this._id = id;
	}

	@Id
	@Column(name = "rev")
	public Integer getRevisionNumber() {
		return _revisionNumber;
	}
	@SuppressWarnings("unused")
	private void setRevisionNumber(Integer revisionNumber) {
		this._revisionNumber = revisionNumber;
	}

	@Column(table = "entityrevision", name = "title")
	public String getTitle() {
		return _title;
	}
	@SuppressWarnings("unused")
	private void setTitle(String title) {
		this._title = title;
	}

	@Column(table = "entityrevision", name = "created_at")
	public Date getCreatedAt() {
		return _createdAt;
	}
	@SuppressWarnings("unused")
	private void setCreatedAt(Date createdAt) {
		this._createdAt = createdAt;
	}

	// @ManyToOne
	// @JoinColumn(table="entity", name = "creator")
	@Transient
	public Account getCreator() {
		return _creator;
	}
	@SuppressWarnings("unused")
	private void setCreator(Account creator) {
		this._creator = creator;
	}

	//
	// product specific
	//

	// @ManyToOne
	// @JoinColumn(name = "type_id")
	@Transient
	public Unit getUnit() {
		return _unit;
	}
	public void setUnit(Unit unit) {
		_unit = unit;
	}

	@Transient
	public Double getAmount() {
		return _amount;
	}
	public void setAmount(double amount) {
		_amount = amount;
	}

	@Transient
	public Category getCategory() {
		return _category;
	}
	@SuppressWarnings("unused")
	private void setCategory(Category category) {
		_category = category;
	}

	@Column(name = "imageurl")
	public String getImageURL() {
		return _imageURL;
	}
	@SuppressWarnings("unused")
	private void setImageURL(String imageURL) {
		_imageURL = imageURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_amount == null) ? 0 : _amount.hashCode());
		result = prime * result + ((_category == null) ? 0 : _category.hashCode());
		result = prime * result + ((_createdAt == null) ? 0 : _createdAt.hashCode());
		result = prime * result + ((_creator == null) ? 0 : _creator.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((_imageURL == null) ? 0 : _imageURL.hashCode());
		result = prime * result + ((_revisionNumber == null) ? 0 : _revisionNumber.hashCode());
		result = prime * result + ((_title == null) ? 0 : _title.hashCode());
		result = prime * result + ((_unit == null) ? 0 : _unit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductRevision other = (ProductRevision) obj;
		if (_amount == null) {
			if (other._amount != null)
				return false;
		} else if (!_amount.equals(other._amount))
			return false;
		if (_category == null) {
			if (other._category != null)
				return false;
		} else if (!_category.equals(other._category))
			return false;
		if (_createdAt == null) {
			if (other._createdAt != null)
				return false;
		} else if (!_createdAt.equals(other._createdAt))
			return false;
		if (_creator == null) {
			if (other._creator != null)
				return false;
		} else if (!_creator.equals(other._creator))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_imageURL == null) {
			if (other._imageURL != null)
				return false;
		} else if (!_imageURL.equals(other._imageURL))
			return false;
		if (_revisionNumber == null) {
			if (other._revisionNumber != null)
				return false;
		} else if (!_revisionNumber.equals(other._revisionNumber))
			return false;
		if (_title == null) {
			if (other._title != null)
				return false;
		} else if (!_title.equals(other._title))
			return false;
		if (_unit == null) {
			if (other._unit != null)
				return false;
		} else if (!_unit.equals(other._unit))
			return false;
		return true;
	}
}
