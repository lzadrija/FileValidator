/**
 * 
 */
package com.lzadrija.upload;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.lzadrija.FileContentConfiguration;
import com.lzadrija.GlobalExceptionHandler;
import com.lzadrija.inspection.FileInspector;
import com.lzadrija.inspection.FileInspectorResponse;
import com.lzadrija.inspection.FileInspectorTest;
import com.lzadrija.inspection.InspectionStatus;
import com.lzadrija.persistence.StorageService;
import com.lzadrija.persistence.StorageServiceException;
import com.lzadrija.persistence.db.model.Entry;
import com.lzadrija.persistence.db.model.File;
import com.lzadrija.persistence.db.model.ValidationResult;
import com.lzadrija.persistence.db.model.ValueType;
import com.lzadrija.validation.ValidatorService;

/**
 * @author lzadrija
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { FileContentConfiguration.class })
public class FileUploadControllerTest {

	private static final String ILLEGAL_FILE_TO_VALIDATE_NAME = "FileIllegalTest.txt", LEGAL_FILE_TO_VALIDATE_NAME = "FileNonPrintableTest.txt",
	        UPLOAD_VIEW = "upload", RESULTS_VIEW = "validation_results", ERROR_VIEW = "error", PROJECT_DIR = "user.dir";

	@Mock
	private FileInspector fileInspector;
	@Mock
	private ValidatorService fileValidatorService;
	@Mock
	private StorageService storageService;

	@InjectMocks
	private FileUploadController fileUploadController;

	private MockMvc mockMvc;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(fileUploadController).
		        setHandlerExceptionResolvers(createExceptionResolver(), new ResponseStatusExceptionResolver()).build();
	}

	/**
	 * 
	 * @return
	 */
	private ExceptionHandlerExceptionResolver createExceptionResolver() {

		ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {

			protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {

				Method method = new ExceptionHandlerMethodResolver(GlobalExceptionHandler.class).resolveMethod(exception);
				return new ServletInvocableHandlerMethod(new GlobalExceptionHandler(), method);
			}
		};
		exceptionResolver.afterPropertiesSet();
		return exceptionResolver;
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.upload.FileUploadController#handleFileUpload(org.springframework.web.multipart.MultipartFile, String)}
	 * 
	 * @throws Exception
	 */
	@Ignore
	public void testHandleFileUploadGet() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name(UPLOAD_VIEW));
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.upload.FileUploadController#handleFileUpload(org.springframework.web.multipart.MultipartFile, String)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHandleFileUploadLegalFileAndStoreToDisk() throws Exception {

		InputStream contentStream = FileInspectorTest.class.getResourceAsStream(LEGAL_FILE_TO_VALIDATE_NAME);
		MockMultipartFile multipartFile = new MockMultipartFile("file", LEGAL_FILE_TO_VALIDATE_NAME, null, contentStream);

		File validatedFile = setControllerServicesLegalData(multipartFile);

		String storageServiceResponse = "Content of file: " + LEGAL_FILE_TO_VALIDATE_NAME
		        + " is successfully validated and persisted. \nValidation results are stored on disk in directory: " + System.getProperty(PROJECT_DIR)
		        + ", under file name: ValidationResults.txt";
		when(storageService.storeValidationResults(validatedFile, true)).thenReturn(storageServiceResponse);

		this.mockMvc.perform(fileUpload("/validation").file(multipartFile).param("storeToDisk", Boolean.TRUE.toString())
		        .contentType(MediaType.MULTIPART_FORM_DATA))
		        .andExpect(status().isOk())
		        .andExpect(view().name(RESULTS_VIEW))
		        .andExpect(model().attribute("storageServiceResponse", storageServiceResponse))
		        .andExpect(model().attribute("file", validatedFile));
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.upload.FileUploadController#handleFileUpload(org.springframework.web.multipart.MultipartFile, String)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHandleFileUploadIllegalFileStructure() throws Exception {

		InputStream contentStream = FileInspectorTest.class.getResourceAsStream(ILLEGAL_FILE_TO_VALIDATE_NAME);
		MockMultipartFile multipartFile = new MockMultipartFile("file", ILLEGAL_FILE_TO_VALIDATE_NAME, null, contentStream);
		List<String> lines = Arrays.asList(new String[] { "value=Prvi primjer nekog teksta;type=TEXT;",
		        "type=NUMBER;value=-.1894Prvi primjer nekog teksta;" });
		FileInspectorResponse inspectorResponse = new FileInspectorResponse(ILLEGAL_FILE_TO_VALIDATE_NAME, InspectionStatus.FAIL, lines,
		        " Entry: value=Prvi primjer nekog teksta;type=TEXT; from file: " + ILLEGAL_FILE_TO_VALIDATE_NAME +
		                " does not have the proper structure: type=VALUE_TYPE;value=VALUE;.");

		when(fileInspector.checkFileStructure(multipartFile)).thenReturn(inspectorResponse);

		this.mockMvc.perform(fileUpload("/validation").file(multipartFile).param("storeToDisk", Boolean.TRUE.toString())
		        .contentType(MediaType.MULTIPART_FORM_DATA))
		        .andExpect(status().isOk()).andExpect(view().name(UPLOAD_VIEW))
		        .andExpect(model().attribute("fileInspectorResponse", inspectorResponse));
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.upload.FileUploadController#handleFileUpload(org.springframework.web.multipart.MultipartFile, String)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHandleFileUploadGeneralException() throws Exception {

		InputStream contentStream = FileInspectorTest.class.getResourceAsStream(LEGAL_FILE_TO_VALIDATE_NAME);
		MockMultipartFile multipartFile = new MockMultipartFile("file", LEGAL_FILE_TO_VALIDATE_NAME, null, contentStream);

		when(fileInspector.checkFileStructure(multipartFile)).thenThrow(new IllegalArgumentException());

		this.mockMvc.perform(fileUpload("/validation").file(multipartFile).param("storeToDisk", Boolean.TRUE.toString())
		        .contentType(MediaType.MULTIPART_FORM_DATA))
		        .andExpect(status().isOk()).andExpect(view().name(ERROR_VIEW));
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.upload.FileUploadController#handleFileUpload(org.springframework.web.multipart.MultipartFile, String)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHandleFileUploadStorageException() throws Exception {

		InputStream contentStream = FileInspectorTest.class.getResourceAsStream(LEGAL_FILE_TO_VALIDATE_NAME);
		MockMultipartFile multipartFile = new MockMultipartFile("file", LEGAL_FILE_TO_VALIDATE_NAME, null, contentStream);

		File validatedFile = setControllerServicesLegalData(multipartFile);
		when(storageService.storeValidationResults(validatedFile, true)).thenThrow(new StorageServiceException());

		this.mockMvc.perform(fileUpload("/validation").file(multipartFile).param("storeToDisk", Boolean.TRUE.toString())
		        .contentType(MediaType.MULTIPART_FORM_DATA))
		        .andExpect(status().is5xxServerError());
	}

	/**
	 * 
	 * @param multipartFile
	 * @return
	 */
	private File setControllerServicesLegalData(MockMultipartFile multipartFile) {

		List<String> lines = Arrays.asList(new String[] { "type=TEXT;value=Prvi primjernekog teksta;", "type=TEXT;value=Drugi primjer nekog teksta;",
		        "type=NUMBER;value=1234;", "type=NUMBER;value=1234aaa;", "type=TEXT;value=1234aaa;" });
		when(fileInspector.checkFileStructure(multipartFile)).thenReturn(new FileInspectorResponse(LEGAL_FILE_TO_VALIDATE_NAME, InspectionStatus.SUCCESS,
		        lines, "File: " + LEGAL_FILE_TO_VALIDATE_NAME + " has correct structure."));

		File validatedFile = new File();
		validatedFile.setId(1);
		validatedFile.setName(LEGAL_FILE_TO_VALIDATE_NAME);
		List<Entry> entries = new ArrayList<>();
		entries.add(new Entry("Prvi primjernekog teksta", ValueType.TEXT, ValidationResult.INVALID));
		entries.add(new Entry("Drugi primjer nekog teksta", ValueType.TEXT, ValidationResult.VALID));
		entries.add(new Entry("1234", ValueType.NUMBER, ValidationResult.VALID));
		entries.add(new Entry("1234aaa", ValueType.NUMBER, ValidationResult.INVALID));
		entries.add(new Entry("1234aaa", ValueType.TEXT, ValidationResult.VALID));
		validatedFile.setEntries(entries);
		when(fileValidatorService.validate(LEGAL_FILE_TO_VALIDATE_NAME, lines)).thenReturn(validatedFile);

		return validatedFile;

	}
}
