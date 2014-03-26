/**
 * Contains classes for file content structure inspection.
 */
package com.lzadrija.inspection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lzadrija.validation.value.ValueValidator;

/**
 * Service used for the inspection of the structure of the file content. If
 * every line in the file does not follow the speciffic pattern, the file cannot
 * be validated. Every line must be structured in the following manner:
 * {@code type=<VALUE_TYPE>;value=<VALUE>;}, where the {@code <VALUE_TYPE>}
 * represents the type of the {@code value} part of the line.
 * 
 * @author lzadrija
 * 
 */
@Service
public class FileInspector {

	private static final Logger logger = LoggerFactory.getLogger(FileInspector.class);

	private String entryStructure;
	private Pattern entryPattern;
	private Map<String, ValueValidator> validatorsByType;

	@Autowired
	private Environment env;

	/**
	 * Default constructor.
	 */
	public FileInspector() {

	}

	/**
	 * Constructor.
	 * 
	 * @param structure
	 *            the descriptive structure that that the line form the file
	 *            must conform to: {@code type=<VALUE_TYPE>;value=<VALUE>;}
	 * @param pattern
	 *            compiled representation of a regular expression that is used
	 *            to extract type and value from the line
	 * @param validatorsByTypeMap
	 *            map with value types and coresponding value validators
	 */
	public FileInspector(String structure, Pattern pattern, Map<String, ValueValidator> validatorsByTypeMap) {

		entryStructure = structure;
		entryPattern = pattern;
		validatorsByType = validatorsByTypeMap;
	}

	/**
	 * Checks if the given multipart file can be opened and read, and if it can,
	 * inspects if its content conforms to the given structure. The file
	 * inspection is considered a success if the following requirements are met:
	 * 1. The file is not empty
	 * 2. Every file line has the following structure:
	 * {@code type=<VALUE_TYPE>;value=<VALUE>;},
	 * 3. {@code <VALUE_TYPE>} must be one of the following types:
	 * {@code TEXT, NUMBER}
	 * 
	 * @param file
	 *            multipart file to inspect
	 * @return file inspection response that contains the content of the file
	 *         and the inspection status with description
	 */
	public FileInspectorResponse checkFileStructure(MultipartFile file) {

		List<String> lines = new ArrayList<>();
		if (file.isEmpty()) {
			return new FileInspectorResponse(file.getOriginalFilename(), InspectionStatus.FAIL, lines, getMsg(MsgKey.FILE_EMPTY));
		}
		String errorMsg = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			while ((line = br.readLine()) != null && errorMsg == null) {

				errorMsg = inspectLine(line, file.getOriginalFilename());
				lines.add(line);
			}
		} catch (IOException ioException) {
			errorMsg = getMsg(MsgKey.FILE_UNABLE_TO_READ, file.getOriginalFilename());
			logger.error(errorMsg, ioException);
		}
		FileInspectorResponse response = (null != errorMsg) ? new FileInspectorResponse(file.getOriginalFilename(), InspectionStatus.FAIL, lines, errorMsg)
		                                                   : new FileInspectorResponse(file.getOriginalFilename(), InspectionStatus.SUCCESS, lines,
		                                                           getMsg(MsgKey.FILE_INSPECT_SUCCESS, file.getOriginalFilename()));
		return response;
	}

	/**
	 * Inspects if the file line has the appropriate structure.
	 * 
	 * @param line
	 *            line to inspect
	 * @param fileName
	 *            name of the file that contains the given line
	 * @return error message if the line does not have the appropriate structure
	 *         or null
	 */
	private String inspectLine(String line, String fileName) {

		String valueType, errorMsg = null;

		Matcher matcher = entryPattern.matcher(line);
		if (!matcher.matches()) {
			errorMsg = getMsg(MsgKey.FILE_ENTRY_INPROPER_STRUCT, line, fileName, entryStructure);
		} else if (!validatorsByType.containsKey(valueType = matcher.group(1))) {
			errorMsg = getMsg(MsgKey.INVALID_VALUE_TYPE, fileName, valueType, validatorsByType.keySet());
		}
		return errorMsg;
	}

	/**
	 * Returns the formatted message that describes the result of file content
	 * inspection.
	 * 
	 * @param key
	 *            key to retrieve message from the properties file
	 * @param params
	 *            parameters for the message
	 * @return formatted description message
	 */
	private String getMsg(MsgKey key, Object... params) {
		return MessageFormat.format(env.getRequiredProperty(key.getKey()), params);
	}

	/**
	 * Key for the appropriate inspection description message.
	 * 
	 * @author lzadrija
	 * 
	 */
	private enum MsgKey {

		/**
		 * Key for retrieving the message that is shown when the multipart file
		 * is empty.
		 */
		FILE_EMPTY("multipartFile.empty"),
		/**
		 * Key for retrieving the message that is shown when the multipart file
		 * can not be opened for reading.
		 */
		FILE_UNABLE_TO_READ("multipartFile.unableToRead"),
		/**
		 * Key for retrieving the message that is shown when the multipart file
		 * does not have the appropriate structure.
		 */
		FILE_ENTRY_INPROPER_STRUCT("fileEntry.improperStructure"),
		/**
		 * Key for retrieving the message that is shown when the line from the
		 * multipart file contains value of unknown type.
		 */
		INVALID_VALUE_TYPE("valueType.invalid"),
		/**
		 * Key for retrieving the message that is shown when the multipart file
		 * is successfuly inspected.
		 */
		FILE_INSPECT_SUCCESS("inspection.success");

		private final String key;

		/**
		 * Constructor.
		 * 
		 * @param key
		 *            key for the inspection description message
		 */
		private MsgKey(String key) {
			this.key = key;
		}

		/**
		 * Returns the key for the inspection message.
		 * 
		 * @return key to retrieve message from the message properties file
		 */
		public String getKey() {
			return key;
		}

		/**
		 * Returns the key for the inspection message.
		 */
		@Override
		public String toString() {
			return key;
		}

	}

}
