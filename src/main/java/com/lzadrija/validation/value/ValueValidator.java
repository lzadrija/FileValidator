/**
 * Contains classes that are used as validators for different types of values
 * stored in each line of the file whose content is validated.
 */
package com.lzadrija.validation.value;

import com.lzadrija.persistence.db.model.ValidationResult;

/**
 * Defines method that is used for file line value validation that is based on
 * the this value's type.
 * 
 * @author lzadrija
 * 
 */
public interface ValueValidator {

	/**
	 * Validates the value of the given line based on the value's type and
	 * returns the validation result which can be {@code VALID} or
	 * {@code INVALID}
	 * 
	 * @param line
	 *            line whose value id validated
	 * @return result of the line value validation
	 */
	public ValidationResult validate(String line);
}
