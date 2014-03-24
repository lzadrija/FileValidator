/**
 * Base package, contains classes for initializing this web application.
 */
package com.lzadrija;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import com.lzadrija.inspection.FileInspector;
import com.lzadrija.persistence.db.model.ValueType;
import com.lzadrija.validation.FileValidatorService;
import com.lzadrija.validation.value.ValueValidator;

/**
 * Sets up the configuration necessary for file inspection and validation.
 * 
 * @author lzadrija
 * 
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:ValueValidatorsConfiguration.properties"),
        @PropertySource("classpath:FileContentConfiguration.properties"),
        @PropertySource("classpath:Messages.properties")
})
public class FileContentConfiguration {

	private static final String ENTRY_STRUCT_KEY = "entry.structure", ENTRY_PATTERN_KEY = "entry.pattern";

	@Autowired
	private Environment env;

	/**
	 * Dynamically instantiates value validators for every valid value type in a
	 * file that is being validated.
	 * 
	 * @return map with value validator for every valid value type from file
	 *         that is validated
	 * @throws ClassNotFoundException
	 *             if the value validator class cannot be located
	 * @throws InstantiationException
	 *             if the value validator object cannot be instantiated
	 * @throws IllegalAccessException
	 *             if the value validator class or its nullary constructor is
	 *             not accessible
	 */
	private Map<String, ValueValidator> loadValueValidators() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Map<String, ValueValidator> validatorsByTypeMap = new HashMap<String, ValueValidator>();

		for (ValueType type : ValueType.values()) {
			String className = env.getRequiredProperty(type.name());

			ValueValidator validator = null;

			if (className != null) {
				Class<?> cls;
				cls = Class.forName(className);
				validator = (ValueValidator) cls.newInstance();
				validatorsByTypeMap.put(type.toString(), validator);
			}
		}
		return validatorsByTypeMap;
	}

	/**
	 * Creates and returns the file inspector object used for verifying that the
	 * file that is to be validated contains the appropriate structure.
	 * 
	 * @return file inspector
	 * @throws ClassNotFoundException
	 *             if the value validator class cannot be located
	 * @throws InstantiationException
	 *             if the value validator object cannot be instantiated
	 * @throws IllegalAccessException
	 *             if the value validator class or its nullary constructor is
	 *             not accessible
	 */
	@Bean
	public FileInspector loadFileInspector() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Pattern entryPattern = Pattern.compile(env.getRequiredProperty(ENTRY_PATTERN_KEY));
		return new FileInspector(env.getRequiredProperty(ENTRY_STRUCT_KEY), entryPattern, loadValueValidators());
	}

	/**
	 * Creates and returns the file validator service used for validating the
	 * content of a file with the appropriate structure.
	 * 
	 * @return file validator service
	 * @throws ClassNotFoundException
	 *             if the value validator class cannot be located
	 * @throws InstantiationException
	 *             if the value validator object cannot be instantiated
	 * @throws IllegalAccessException
	 *             if the value validator class or its nullary constructor is
	 *             not accessible
	 */
	@Bean
	public FileValidatorService loadFileValidatorService() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Pattern entryPattern = Pattern.compile(env.getRequiredProperty(ENTRY_PATTERN_KEY));
		return new FileValidatorService(entryPattern, loadValueValidators());
	}

}
