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
public class TextValidator implements ValueValidator {

	private static final String PRINTABLE_CHRS_REGEX = "[^\\x00\\x08\\x0B\\x0C\\x0E-\\x1F]+";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lzadrija.validation.value.ValueValidator(java.lang.Class)
	 */
	@Override
	public ValidationResult validate(String entry) {

		Matcher matcher = Pattern.compile(PRINTABLE_CHRS_REGEX).matcher(entry);
		return matcher.matches() ? ValidationResult.VALID : ValidationResult.INVALID;
	}

}
