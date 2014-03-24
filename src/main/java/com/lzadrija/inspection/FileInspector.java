/**
 * 
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
	 * 
	 */
	public FileInspector() {

	}

	/**
	 * 
	 * @param pattern
	 * @param validatorsByTypeMap
	 */
	public FileInspector(String structure, Pattern pattern, Map<String, ValueValidator> validatorsByTypeMap) {

		entryStructure = structure;
		entryPattern = pattern;
		validatorsByType = validatorsByTypeMap;
	}

	/**
	 * 
	 * @param file
	 * @return
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

				errorMsg = inspectEntry(line, file);
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
	 * 
	 * @param line
	 * @param file
	 * @return
	 */
	private String inspectEntry(String line, MultipartFile file) {

		String valueType, errorMsg = null;

		Matcher matcher = entryPattern.matcher(line);
		if (!matcher.matches()) {
			errorMsg = getMsg(MsgKey.FILE_ENTRY_INPROPER_STRUCT, line, file.getOriginalFilename(), entryStructure);
		} else if (!validatorsByType.containsKey(valueType = matcher.group(1))) {
			errorMsg = getMsg(MsgKey.INVALID_VALUE_TYPE, file.getOriginalFilename(), valueType, validatorsByType.keySet());
		}
		return errorMsg;
	}

	/**
	 * 
	 * @param key
	 * @param params
	 * @return
	 */
	private String getMsg(MsgKey key, Object... params) {
		return MessageFormat.format(env.getRequiredProperty(key.getKey()), params);
	}

	/**
	 * 
	 * @author lzadrija
	 * 
	 */
	private enum MsgKey {

		FILE_EMPTY("multipartFile.empty"),
		FILE_UNABLE_TO_READ("multipartFile.unableToRead"),
		FILE_ENTRY_INPROPER_STRUCT("fileEntry.improperStructure"),
		INVALID_VALUE_TYPE("valueType.invalid"),
		FILE_INSPECT_SUCCESS("inspection.success");

		private final String key;

		/**
		 * 
		 * @param key
		 */
		private MsgKey(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		/**
		 * 
		 */
		@Override
		public String toString() {
			return key;
		}

	}

}
