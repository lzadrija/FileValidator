/**
 * 
 */
package com.lzadrija.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.lzadrija.persistence.db.FileRepository;
import com.lzadrija.persistence.db.JpaConfiguration;

/**
 * @author lzadrija
 * 
 */
@Configuration
@Import(JpaConfiguration.class)
@PropertySource("classpath:StorageServiceConfiguration.properties")
public class StorageServiceConfiguration {

	@Autowired
	private Environment env;
	@Autowired
	private FileRepository filesRepository;

	@Bean
	public StorageService loadStorageService() {
		return new StorageService(env.getRequiredProperty("validationResults.fileNameToStore"), env.getRequiredProperty("validationResults.format"),
		        filesRepository);
	}
}
