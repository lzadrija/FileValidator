/**
 * Contains classes for file content validation.
 */
package com.lzadrija.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.lzadrija.persistence.db.model.Entry;
import com.lzadrija.persistence.db.model.File;
import com.lzadrija.persistence.db.model.ValidationResult;
import com.lzadrija.persistence.db.model.ValueType;
import com.lzadrija.validation.value.ValueValidator;

/**
 * Service used for validation of the given structured (file) content. Every
 * line from the content has a specific structure and contains values of some
 * type. Values are examined using validators that validate only values of
 * certain type.
 * 
 * @author lzadrija
 * 
 */
@Service
public class ValidatorService {

	private Pattern pattern;
	private Map<String, ValueValidator> validatorsByType;

	/**
	 * Default constructor.
	 */
	public ValidatorService() {
	}

	/**
	 * Constructor.
	 * 
	 * @param pattern
	 *            pattern that each line from the content (entry) follows
	 * @param validatorsByTypeMap
	 *            map that contains validators for each value type that can
	 *            appear in the file content
	 */
	public ValidatorService(Pattern entryPattern, Map<String, ValueValidator> validatorsByTypeMap) {
		pattern = entryPattern;
		validatorsByType = validatorsByTypeMap;

	}

	/**
	 * Validates the given lines form the file with the given name and returns
	 * the file that contains the validation result for each line. Each file
	 * that is to be validated must contain lines of the following structure:
	 * {@code type=<VALUE_TYPE>;value=<VALUE>;}
	 * 
	 * @param fileName
	 *            name of the file whose content is validated
	 * @param lines
	 *            list of lines (content) whose values are validated
	 * @return file object that contains the original content with validation
	 *         result for each line
	 */
	public File validate(String fileName, List<String> lines) {

		File validatedFile = new File();
		validatedFile.setName(fileName);

		List<Entry> entries = new ArrayList<>();
		for (String line : lines) {
			entries.add(validateAndGetEntry(line, fileName));
		}
		validatedFile.setEntries(entries);

		return validatedFile;
	}

	/**
	 * Examines and validates the given line from the file with the given name.
	 * This method returns an entry that contains the original value, its type
	 * and validation result. Validation result for some value can be valid or
	 * invalid, based on the type of the value.
	 * 
	 * @param line
	 *            a line that is validated
	 * @param fileName
	 *            name of the file that contains the given line
	 * @return entry that contains the validated line
	 * @throws IllegalArgumentException
	 *             if the line does not have the proper structure
	 */
	private Entry validateAndGetEntry(String line, String fileName) {

		Matcher matcher = pattern.matcher(line);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Unable to validate file: " + fileName + ". Entry from file has illegal structure: " + line);
		}
		String valueTypeStr = matcher.group(1);
		ValueValidator valueValidator = validatorsByType.get(valueTypeStr);

		String value = matcher.group(2);
		ValidationResult validationResult;
		Entry entry = new Entry();

		validationResult = valueValidator.validate(value);
		entry.setType(ValueType.valueOf(valueTypeStr));
		entry.setValue(value);
		entry.setValidationResult(validationResult);

		return entry;
	}

}
