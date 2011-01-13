package org.tagaprice.core.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hamcrest.Matcher;

@Entity
@Table(name = "product")
@SecondaryTables({ @SecondaryTable(name = "entity") })
@SuppressWarnings("unused")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long _id = null;
	private Set<ProductRevision> _revisions = new HashSet<ProductRevision>();
	private Locale _locale;

	protected Product() {
	}

	/**
	 * 
	 * @param id Id of Product to create. Can be null, then this product and all its revisions are treated as new concerning the database and a fresh id will be created and assigned. If id is not null it not must be greater than 0.
	 * @param locale indicates the language and location of this product.
	 * @param revisions A non-empty set of ProductRevisions. The Set must also have consecutive revisions numbers without gaps. E.g. the set with revisions: 2,3,4 is valid whereas the set with revisions: 2,4,5 is invalid.
	 */
	public Product(Long id, Locale locale, Set<ProductRevision> revisions) {
		_id = id;
		_locale = locale;
		_revisions.addAll(revisions);
	}

	@Id
	@Column(name = "ent_id")
	public Long getId() {
		return _id;
	}
	public void setId(Long id) { //TODO this is public due to service having to set the id if not present, should not be public probably
		this._id = id;
		for(ProductRevision rev : _revisions) {
			rev.setId(id);
		}
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinColumn(table = "entity", name = "locale_id", referencedColumnName = "locale_id")
	public Locale getLocale() {
		return _locale;
	}
	private void setLocale(Locale locale) {
		this._locale = locale;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "ent_id")
	public Set<ProductRevision> getRevisions() {
		return _revisions;
	}
	private void setRevisions(Set<ProductRevision> revisions) {
		_revisions = revisions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
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
		Product other = (Product) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [_id=" + _id + "]";
	}

	/**
	 * Returns the current, i.e. highest, revision of this product.
	 */
	@Transient
	public ProductRevision getCurrentRevision() {
		return _revisions.iterator().next();
	}
}
