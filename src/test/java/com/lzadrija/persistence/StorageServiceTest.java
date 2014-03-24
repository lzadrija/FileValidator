/**
 * 
 */
package com.lzadrija.persistence;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.lzadrija.persistence.db.model.Entry;
import com.lzadrija.persistence.db.model.File;
import com.lzadrija.persistence.db.model.ValidationResult;
import com.lzadrija.persistence.db.model.ValueType;

/**
 * @author lzadrija
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = StorageServiceConfiguration.class)
@WebAppConfiguration
public class StorageServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(StorageServiceTest.class);
	private static final String FILE_NAME = "FileLegal.txt", RESULTS_FILE_NAME = "ValidationResults.txt", PROJECT_DIR = "user.dir";

	@Autowired
	@Qualifier("loadStorageService")
	private StorageService storageService;

	/**
	 * Test method for
	 * {@link com.lzadrija.parsistence.StorageService#storeValidationResults(java.lang.String, java.lang.String, com.lzadrija.persistence.db.FileRepository)}
	 */
	@Test
	public void testStoreValidationResults() {

		File validatedFile = new File();
		validatedFile.setName(FILE_NAME);

		List<Entry> entries = new ArrayList<>();
		entries.add(new Entry("Prvi primjer nekog teksta", ValueType.TEXT, ValidationResult.VALID));
		entries.add(new Entry("Drugi primjer nekog teksta", ValueType.TEXT, ValidationResult.VALID));

		validatedFile.setEntries(entries);
		try {
			storageService.storeValidationResults(validatedFile, true);

			Path filePath = Paths.get(System.getProperty(PROJECT_DIR), RESULTS_FILE_NAME);
			boolean isFileCreated = Files.exists(filePath);
			assertTrue(isFileCreated);
		} catch (StorageServiceException storageServiceException) {
			logger.error("Unable to store validation results of file: {0} to file: {1}.", new Object[] { validatedFile.getName(), RESULTS_FILE_NAME },
			             storageServiceException);
		}
	}
}
