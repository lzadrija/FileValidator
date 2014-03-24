/**
 * 
 */
package com.lzadrija.inspection;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import com.lzadrija.FileContentConfiguration;

/**
 * @author lzadrija
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FileContentConfiguration.class)
public class FileInspectorTest {

	private static final Logger logger = LoggerFactory.getLogger(FileInspectorTest.class);
	private static final String FILE_LEGAL_CONTENT = "FileLegalTest.txt", FILE_ILLEGAL_LINE_FORMAT = "FileIllegalTest.txt",
	        FILE_ILLEGAL_VALUE_TYPE = "FileIllegalTypeTest.txt", FILE_NON_PRINTABLE_CHARS = "FileNonPrintableTest.txt";

	@Autowired
	@Qualifier("loadFileInspector")
	private FileInspector fileInspector;

	/**
	 * Test method for
	 * {@link com.lzadrija.inspection.FileInspector#checkFileStructure(org.springframework.web.multipart.MultipartFile)}
	 */
	@Test
	public void checkFileStructureLegalFile() {
		testCheckFileStructureFor(FILE_LEGAL_CONTENT, InspectionStatus.SUCCESS);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.inspection.FileInspector#checkFileStructure(org.springframework.web.multipart.MultipartFile)}
	 */
	@Test
	public void checkFileStructureNonPrintable() {
		testCheckFileStructureFor(FILE_NON_PRINTABLE_CHARS, InspectionStatus.SUCCESS);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.inspection.FileInspector#checkFileStructure(org.springframework.web.multipart.MultipartFile)}
	 */
	@Test
	public void testCheckFileStructureIllegalFile() {
		testCheckFileStructureFor(FILE_ILLEGAL_LINE_FORMAT, InspectionStatus.FAIL);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.inspection.FileInspector#checkFileStructure(org.springframework.web.multipart.MultipartFile)}
	 */
	@Test
	public void testCheckFileStructureIllegalType() {
		testCheckFileStructureFor(FILE_ILLEGAL_VALUE_TYPE, InspectionStatus.FAIL);
	}

	/**
	 * 
	 * @param fileName
	 * @param inspectionStatus
	 */
	private void testCheckFileStructureFor(String fileName, InspectionStatus inspectionStatus) {
		InputStream contentStream = this.getClass().getResourceAsStream(fileName);
		MultipartFile multipartFile;
		try {
			multipartFile = new MockMultipartFile("file", fileName, null, contentStream);
			FileInspectorResponse response = fileInspector.checkFileStructure(multipartFile);

			assertEquals(inspectionStatus, response.getStatus());
		} catch (IOException ioException) {
			logger.error("Cannot check file structure", ioException);
		}
	}

}
