package org.tagaprice.shared.exception;

import java.sql.SQLException;

/**
 * Indicates that an error has occured when performing the requested action in a DAO. 
 * E.g. SQLException due to database misbehavior, XMLExceptions on parse errors, etc
 * @author Martin Weik (afraidoferrors)
 *
 */
public class DAOException extends ServerException {
	private static final long serialVersionUID = 1L;

	public DAOException(String message, SQLException e) {
		super();
	}
	
	public DAOException(String message) {
		super(message);
	}
	
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

}
