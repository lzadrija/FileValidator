/**
 * Contains classes that handle file upload request.
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
import com.lzadrija.validation.ValidatorService;

/**
 * This controller handles requests from the dispatcher servlet for validation
 * of the files uploaded by the user, and delegates the response which contains
 * the validated file content which is also persisted in the database and can be
 * stored on the disk.
 * 
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
	private ValidatorService fileValidatorService;

	@Autowired
	@Qualifier("loadStorageService")
	private StorageService storageService;

	/**
	 * Handler method for the initial GET request that returns the name of the
	 * view that presents the form for the file upload.
	 * 
	 * @return name of the view that presents the form for the file upload
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String start() {
		return UPLOAD_VIEW;
	}

	/**
	 * Handler method that processes uploaded files. Uploaded files are
	 * inspected and validated if their structure is appropriate. File's content
	 * and validation results are persisted to database and can be stored in
	 * separate file, based on the given {@code storeToDisk} flag.
	 * 
	 * @param file
	 *            uploaded file whose content is to be examined (and validated)
	 * @param storeToDisk
	 *            indicates if the file content and validation results should be
	 *            stored on disk
	 * @return holder object for Model and View that contains view with file
	 *         content and validation results or view that displays an error
	 *         message
	 * @throws StorageServiceException
	 *             if the file could not be stored to disk
	 */
	@RequestMapping(value = "/validation", method = RequestMethod.POST)
	public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file,
	                                     @RequestParam("storeToDisk") String storeToDisk) throws StorageServiceException {

		FileInspectorResponse inspectionResponse = fileInspector.checkFileStructure(file);

		ModelAndView modelAndView;
		if (inspectionResponse.isFileStructureCorrect()) {

			File validatedFile = fileValidatorService.validate(inspectionResponse.getFileName(), inspectionResponse.getFileLines());
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
