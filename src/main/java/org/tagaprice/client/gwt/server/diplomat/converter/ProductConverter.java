package org.tagaprice.client.gwt.server.diplomat.converter;

import java.util.*;

import org.slf4j.*;
import org.tagaprice.client.gwt.shared.entities.*;
import org.tagaprice.client.gwt.shared.entities.dump.*;
import org.tagaprice.client.gwt.shared.entities.productmanagement.IProduct;
import org.tagaprice.core.entities.*;
import org.tagaprice.core.entities.Category;
import org.tagaprice.core.entities.Locale;

public class ProductConverter {


	Logger _log = LoggerFactory.getLogger(ProductConverter.class);

	private static final ProductConverter instance = new ProductConverter();

	// dummy values
	public static final Locale defaultCoreLocale = new Locale(1, "de", "de");
	public static final Account defaultCoreAccount = new Account(1L, "love@you.org", "super", new Date());
	public static final Category defaultCoreCategory = new Category(1L, "X", null, new Date(), ProductConverter.defaultCoreAccount);

	public static ProductConverter getInstance() {
		return ProductConverter.instance;
	}

	public org.tagaprice.core.entities.Product convertProductToCore(final IProduct productGWT) {
		_log.debug("Convert GWT -> core, id: " + productGWT.getRevisionId());
		// Default values for new product...
		Long productId = null;
		Integer revisionNumber = 0;

		if (productGWT.getRevisionId() != null) {
			productId = productGWT.getRevisionId().getId();
			revisionNumber = new Long(productGWT.getRevisionId().getRevision()).intValue();


		}
		String title = productGWT.getTitle();
		Date date = new Date();
		String  imageUrl = "";

		// TODO Category must never be null!
		Category category;
		if (productGWT.getCategory() != null) {
			category = new Category(new Long(productGWT.getCategory().getId()), productGWT.getCategory().getTitle(),
					null, new Date(), ProductConverter.defaultCoreAccount);
		} else {
			category = ProductConverter.defaultCoreCategory;
		}

		Double amount = productGWT.getQuantity().getQuantity();
		Unit unit = productGWT.getQuantity().getUnit();

		// If product already exists...
		ProductRevision revision = new ProductRevision(productId, revisionNumber, title, date,
				ProductConverter.defaultCoreAccount, unit, amount, category, "");
		Set<ProductRevision> revisions = new HashSet<ProductRevision>();
		revisions.add(revision);

		// ids must be the same value. if they are null the product must be created as new.

		Product productCore = new Product(productId, ProductConverter.defaultCoreLocale, revisions);
		return productCore;
	}

	/**
	 * 
	 * @param productCore
	 * @param revision
	 *            when 0, then the latest revision is returned.
	 * @return
	 */
	public IProduct convertProductToGWT(final Product productCore, int revisionToGet) {
		_log.debug("Convert core -> GWT, id: " + productCore.getId() + ", rev: " + revisionToGet);
		// these are always existing products!!!
		ProductRevision pr = productCore.getCurrentRevision();


		// get the data from the latest revision
		long id = productCore.getId();
		long revision = pr.getRevisionNumber();
		String title = pr.getTitle();
		ICategory category = new org.tagaprice.client.gwt.shared.entities.dump.Category(pr.getCategory().getTitle());
		IQuantity quantity = new Quantity(pr.getAmount(), pr.getUnit());

		IRevisionId revisionId = new RevisionId(id, revision);
		IProduct productGWT = new org.tagaprice.client.gwt.shared.entities.productmanagement.Product(revisionId, title,
				category, quantity);
		return productGWT;
	}




}