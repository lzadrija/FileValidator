/**
 * Contains model and persistence related classes - for storing data to database
 * and on disk.
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
 * Sets up the configuration for the service {@link StorageService} and creates
 * an object of this class. This configuration sets up a CRUD repository for
 * validated files, format of the validated content and name of the file in
 * which the validated content can be stored.
 * 
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

	/**
	 * Creates and configures an instance of the {@link StorageService} class,
	 * by specifying the CRUD repository, format, and file name for storing the
	 * validation results.
	 * 
	 * @return storage service used for persisting the validation results
	 */
	@Bean
	public StorageService loadStorageService() {
		return new StorageService(env.getRequiredProperty("validationResults.fileNameToStore"),
		        env.getRequiredProperty("validationResults.format"), filesRepository);
	}
}
