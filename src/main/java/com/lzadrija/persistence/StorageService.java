/**
 * Contains model and persistence related classes - for storing data to database
 * and on disk.
 */
package com.lzadrija.persistence;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lzadrija.persistence.db.FileRepository;
import com.lzadrija.persistence.db.model.File;

/**
 * Service used for storing the validated file to database, and optionally on
 * disk.
 * 
 * @author lzadrija
 * 
 */
@Service
@Transactional
@PropertySource("classpath:Messages.properties")
public class StorageService {

	private static final Logger logger = LoggerFactory.getLogger(StorageService.class);
	private static final String PROJECT_DIR = "user.dir", STORE_TO_FILE_RESPONSE = "storeResultsToDisk.success",
	        STORE_TO_DB_RESPONSE = "storeResultsToDb.success";

	private FileRepository filesRepository;
	private String fileToStoreName, validationResultFormat;

	@Autowired
	private Environment env;

	/**
	 * Default constructor.
	 */
	public StorageService() {
	}

	/**
	 * Constructor.
	 * 
	 * @param resultFileName
	 *            Name of the file in which the validated result can be
	 *            optionally stored
	 * @param resultFormat
	 *            format used for storing validated results to file
	 * @param validatedFilesfrepository
	 *            used for storing validated files to database and providing
	 *            CRUD operations on stored data
	 */
	public StorageService(String resultFileName, String resultFormat, FileRepository validatedFilesfrepository) {

		fileToStoreName = resultFileName;
		validationResultFormat = resultFormat;
		filesRepository = validatedFilesfrepository;
	}

	/**
	 * Stores validation results to database, and based on the value of the
	 * given flag {@code storeToDisk}, in special file named
	 * {@code ValidationResults.txt} located in the project's direcotry
	 * on disk. This method returns a discriptive message with information about
	 * the manner of storing the validated file data, and the location of the
	 * data.
	 * 
	 * @param validatedFile
	 *            validated file that is to be persisted
	 * @param storeToDisk
	 *            flag that indicated if the validated file content is to be
	 *            stored on disk
	 * @throws StorageServiceException
	 *             if results cannot be stored on disk
	 */
	public String storeValidationResults(File validatedFile, boolean storeToDisk) throws StorageServiceException {

		validatedFile = filesRepository.save(validatedFile);

		String response = MessageFormat.format(env.getRequiredProperty(STORE_TO_DB_RESPONSE), validatedFile.getName());

		if (storeToDisk) {
			String validatedFormatedContent = validatedFile.getRepresentation(validationResultFormat);
			Path path = FileSystems.getDefault().getPath(fileToStoreName);
			try {
				Files.write(path, validatedFormatedContent.getBytes(), StandardOpenOption.CREATE);
			} catch (IOException ioException) {
				logger.error("Unable to write validation results to file: " + fileToStoreName, ioException);
				throw new StorageServiceException("Unable to write validation results to file: " + fileToStoreName, ioException);
			}
			response += MessageFormat.format(env.getRequiredProperty(STORE_TO_FILE_RESPONSE), System.getProperty(PROJECT_DIR), fileToStoreName);
		}

		return response;
	}
}
