/**
 * Contains classes that are used as validators for different types of values
 * stored in each line of the file whose content is validated.
 */
package com.lzadrija.validation.value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lzadrija.persistence.db.model.ValidationResult;

/**
 * An implementation of {@link ValueValidator} that is used for the validation
 * of the numeric values from the file line.
 * Each line that contains value of type {@code NUMBER} is validated using
 * validator of this type. Numeric value that is considered valid can only be an
 * integer or decimal number, positive or negative. This validator marks all
 * other values of type {@code NUMBER} as invalid.
 * 
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
