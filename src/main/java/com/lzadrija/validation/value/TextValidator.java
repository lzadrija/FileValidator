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
 * of the text values from the file line. Each line that contains values of type
 * {@code TEXT} is validated using validator of this type. Text values that are
 * considered valid can contain any printable character. Lines that contain text
 * values with non-printable characters are marked invalid by this validator.
 * 
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
