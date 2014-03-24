/**
 * 
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
 * @author lzadrija
 * 
 */
@Service
public class FileValidatorService {

	private Pattern pattern;
	private Map<String, ValueValidator> validatorsByType;

	/**
	 * 
	 */
	public FileValidatorService() {
	}

	/**
	 * @param pattern
	 * @param validatorsByTypeMap
	 */
	public FileValidatorService(Pattern entryPattern, Map<String, ValueValidator> validatorsByTypeMap) {
		pattern = entryPattern;
		validatorsByType = validatorsByTypeMap;

	}

	/**
	 * 
	 * @param fileName
	 * @param lines
	 * @return
	 */
	public File validateFile(String fileName, List<String> lines) {

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
	 * 
	 * @param line
	 * @param fileName
	 * @return
	 * @throws IllegalArgumentException
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
