/**
 * 
 */
package com.lzadrija.validation.value;

import com.lzadrija.persistence.db.model.ValidationResult;

/**
 * @author lzadrija
 * 
 */
public interface ValueValidator {

	/**
	 * 
	 * @param entry
	 * @return
	 */
	public ValidationResult validate(String entry);
}
