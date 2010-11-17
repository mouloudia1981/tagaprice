package org.tagaprice.server.dao.interfaces;

import org.tagaprice.shared.SerializableArrayList;
import org.tagaprice.shared.entities.Unit;
import org.tagaprice.shared.exception.DAOException;

public interface IUnitDAO {

	/**
	 * Retrieves units similar to the one indicated by given unit id, i.e. the base unit or the unit itself.
	 * @param unitId Id of unit to find similar units for.
	 * @return All units similar to unit indicated by given unitId. Never null, can be empty.
	 * @throws DAOException
	 */
	SerializableArrayList<Unit> getSimilar(long unitId) throws DAOException;

	/**
	 * Saves given unit to storage. If successful given unit's revision will be set to the revision in the database.
	 * @param unit Unit to save.
	 * @return True if unit could be successfully saved, false if not.
	 * @throws DAOException
	 */
	boolean save(Unit unit) throws DAOException;

	/**
	 * Retrieves the unit indicated by given id from storage. 
	 * @param unitId Id of unit to get.
	 * @return Unit indicated by given id or null if no unit could be found.
	 * @throws DAOException
	 */
	Unit getById(long unitId) throws DAOException;

}