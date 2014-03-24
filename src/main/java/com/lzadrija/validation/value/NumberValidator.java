/**
 * 
 */
package com.lzadrija.validation.value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lzadrija.persistence.db.model.ValidationResult;

/**
 * @author lzadrija
 * 
 */
public class NumberValidator implements ValueValidator {

	private static final String PRINTABLE_NUMS_REGEX = "^[-+]?\\d*\\.\\d+$|^[-+]?\\d+$";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lzadrija.validation.value.ValueValidator(java.lang.Class)
	 */
	@Override
	public ValidationResult validate(String entry) {

		Matcher matcher = Pattern.compile(PRINTABLE_NUMS_REGEX).matcher(entry);
		return matcher.matches() ? ValidationResult.VALID : ValidationResult.INVALID;
	}

}
