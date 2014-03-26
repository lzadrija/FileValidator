/**
 * Contains the classes that represent the value holders from the model.
 */
package com.lzadrija.persistence.db.model;

/**
 * Represents the type of the value from a file line.
 * 
 * @author lzadrija
 * 
 */
public enum ValueType {

	/**
	 * Value of this type can be an integer or a decimal number, negative or
	 * positive.
	 */
	NUMBER,
	/**
	 * Value type that represents a text containing only printable characters.
	 */
	TEXT;

}
