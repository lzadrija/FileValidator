/**
 * 
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
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
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
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Bean
	public FileInspector loadFileInspector() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Pattern entryPattern = Pattern.compile(env.getRequiredProperty(ENTRY_PATTERN_KEY));
		return new FileInspector(env.getRequiredProperty(ENTRY_STRUCT_KEY), entryPattern, loadValueValidators());
	}

	/**
	 * 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	@Bean
	public FileValidatorService loadFileValidatorService() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Pattern entryPattern = Pattern.compile(env.getRequiredProperty(ENTRY_PATTERN_KEY));
		return new FileValidatorService(entryPattern, loadValueValidators());
	}

}
