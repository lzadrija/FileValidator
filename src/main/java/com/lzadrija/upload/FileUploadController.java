/**
 * 
 */
package com.lzadrija.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lzadrija.inspection.FileInspector;
import com.lzadrija.inspection.FileInspectorResponse;
import com.lzadrija.persistence.StorageService;
import com.lzadrija.persistence.StorageServiceException;
import com.lzadrija.persistence.db.model.File;
import com.lzadrija.validation.FileValidatorService;

/**
 * @author lzadrija
 * 
 */
@Controller
public class FileUploadController {

	private static final String VALIDATION_RESULTS_VIEW = "validation_results", UPLOAD_VIEW = "upload";

	@Autowired
	@Qualifier("loadFileInspector")
	private FileInspector fileInspector;

	@Autowired
	@Qualifier("loadFileValidatorService")
	private FileValidatorService fileValidatorService;

	@Autowired
	@Qualifier("loadStorageService")
	private StorageService storageService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String start() {
		return UPLOAD_VIEW;
	}

	@RequestMapping(value = "/validation", method = RequestMethod.POST)
	public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file,
	                                     @RequestParam("storeToDisk") String storeToDisk) throws StorageServiceException {

		FileInspectorResponse inspectionResponse = fileInspector.checkFileStructure(file);

		ModelAndView modelAndView;
		if (inspectionResponse.isFileStructureCorrect()) {

			File validatedFile = fileValidatorService.validateFile(inspectionResponse.getFileName(), inspectionResponse.getFileLines());
			String storageServiceResponse = storageService.storeValidationResults(validatedFile, Boolean.valueOf(storeToDisk));

			modelAndView = new ModelAndView(VALIDATION_RESULTS_VIEW);
			modelAndView.addObject("file", validatedFile);
			modelAndView.addObject("storageServiceResponse", storageServiceResponse);
		} else {
			modelAndView = new ModelAndView(UPLOAD_VIEW);
			modelAndView.addObject("fileInspectorResponse", inspectionResponse);
		}
		return modelAndView;
	}
}
