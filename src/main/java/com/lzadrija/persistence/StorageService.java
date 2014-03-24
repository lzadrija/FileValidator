/**
 * 
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
	 * 
	 */
	public StorageService() {
	}

	/**
	 * 
	 * @param resultFileName
	 * @param resultFormat
	 * @param crudFilesRepository
	 */
	public StorageService(String resultFileName, String resultFormat, FileRepository crudFilesRepository) {

		fileToStoreName = resultFileName;
		validationResultFormat = resultFormat;
		filesRepository = crudFilesRepository;
	}

	/**
	 * 
	 * @param validatedFile
	 * @param storeToDisk
	 * @throws StorageServiceException
	 */
	public String storeValidationResults(File validatedFile, boolean storeToDisk) throws StorageServiceException {

		filesRepository.saveAndFlush(validatedFile);

		String response = MessageFormat.format(env.getRequiredProperty(STORE_TO_DB_RESPONSE), validatedFile.getName());

		if (storeToDisk) {
			String validatedFormatedContent = validatedFile.toFormatedString(validationResultFormat);
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
