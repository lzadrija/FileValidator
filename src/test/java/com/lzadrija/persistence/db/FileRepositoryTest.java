/**
 * 
 */
package com.lzadrija.persistence.db;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.lzadrija.persistence.db.model.Entry;
import com.lzadrija.persistence.db.model.File;
import com.lzadrija.persistence.db.model.ValidationResult;
import com.lzadrija.persistence.db.model.ValueType;

/**
 * @author lzadrija
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaConfiguration.class)
@Transactional
@TransactionConfiguration
@WebAppConfiguration
public class FileRepositoryTest {

	@Autowired
	private FileRepository fileRepository;

	/**
	 * Test method for
	 * {@link com.lzadrija.persistence.db.FileRepository#saveAndFlush(com.lzadrija.persistence.db.model.File)}
	 */
	@Test
	public void testCreteFileAndStore() {

		File file = new File();
		file.setName("FileToValidate.txt");

		List<Entry> entries = new ArrayList<>();
		entries.add(new Entry("Prvi primjer nekog teksta", ValueType.TEXT, ValidationResult.VALID));
		entries.add(new Entry("Drugi primjer nekog teksta", ValueType.TEXT, ValidationResult.VALID));
		entries.add(new Entry("Prvi primjer nekog teksta", ValueType.NUMBER, ValidationResult.INVALID));
		entries.add(new Entry("189", ValueType.NUMBER, ValidationResult.VALID));

		file.setEntries(entries);

		File savedFile = fileRepository.saveAndFlush(file);
		assertNotNull(savedFile);
	}
}
