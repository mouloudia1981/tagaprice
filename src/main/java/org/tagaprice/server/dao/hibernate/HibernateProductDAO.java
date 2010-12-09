package org.tagaprice.server.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.tagaprice.core.beans.Product;
import org.tagaprice.server.dao.ints.IProductDAO;

/** TODO proper setup through spring beans config */
public class HibernateProductDAO implements IProductDAO {

	/** TODO @Autowired */
	private SessionFactory _sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		_sessionFactory = sessionFactory;
	}
	
	@Override
//	@Transactional
	/** TODO remove @Transactional from this method! if removed, tests aren't working. find a way to setup tests correctly */
	public Product save(Product product) {
		_sessionFactory.getCurrentSession().save(product);
		return product;
	}

}