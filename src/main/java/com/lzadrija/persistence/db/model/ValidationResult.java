/**
 * Contains the classes that represent the value holders from the model.
 */
package com.lzadrija.persistence.db.model;

/**
 * Result of the validation ofthe value form the file line.
 * This result contains the information is the given value a value of the given
 * type that is associated to it.
 * 
 * @author lzadrija
 * 
 */
public enum ValidationResult {

	/**
	 * Result that is the product of the value (from a file line) validation,
	 * from which it was determined that this value does not conform to the
	 * parameters set for the values of the associated type.
	 */
	INVALID,
	/**
	 * Result that is the product of the value (from a file line) validation,
	 * from which it was determined that this value is a valid value of the type
	 * that is given to it.
	 */
	VALID;
}
