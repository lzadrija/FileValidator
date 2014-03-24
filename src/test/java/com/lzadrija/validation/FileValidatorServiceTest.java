/**
 * 
 */
package com.lzadrija.validation;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lzadrija.FileContentConfiguration;
import com.lzadrija.persistence.db.model.File;

/**
 * @author lzadrija
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = FileContentConfiguration.class)
public class FileValidatorServiceTest {

	private static final String FILE_NAME = "FileLegal.txt";

	@Autowired
	@Qualifier("loadFileValidatorService")
	private FileValidatorService validatorService;

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.FileValidatorService#validateFile(org.springframework.web.multipart.MultipartFile, java.lang.String)}
	 */
	@Test
	public void testValidateAndStoreFile() {

		List<String> lines = new ArrayList<>();
		lines.add("type=TEXT;value=Prvi primjer nekog teksta5m4tym4yvmm105k,1iuu.5.3k,111..u666605.3g2v03b 1428j56g2zs10.ol+4uy6t0e21sckfg,erhk.yj;");
		lines.add("type=TEXT;value=Drugi primjer nekog teksta	;");
		lines.add("type=NUMBER;value=Prvi primjer nekog teksta;");
		lines.add("type=NUMBER;value=-1234.2156;");
		lines.add("type=NUMBER;value=1234aaa;");
		lines.add("type=TEXT;value=1234aaa;");
		lines.add("type=TEXT;value=1235.1256;");
		File validatedFile = validatorService.validateFile(FILE_NAME, lines);

		assertNotNull(validatedFile);
	}
}
