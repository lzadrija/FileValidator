/**
 * 
 */
package com.lzadrija.validation.value;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.lzadrija.persistence.db.model.ValidationResult;

/**
 * @author lzadrija
 * 
 */
public class TextValidatorTest {

	private ValueValidator textValidator;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		textValidator = new TextValidator();
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.TextValidator#validate(java.lang.String)}
	 */
	@Test
	public void testValidTextEntry() {
		assertEquals(textValidator.validate("123+$64a*aa5478/\n.~!%€¼^65 '	\" ¶\\x08 \u0003"), ValidationResult.VALID);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.TextValidator#validate(java.lang.String)}
	 */
	@Test
	public void testInvalidTextEntry() {
		char[] nonPrintables = new char[] { 0x03, 0x1e };
		String textToValidate = new String(nonPrintables);

		assertEquals(textValidator.validate(textToValidate), ValidationResult.INVALID);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.TextValidator#validate(java.lang.String)}
	 */
	@Test
	public void testEmptyEntry() {
		assertEquals(textValidator.validate(""), ValidationResult.INVALID);
	}

}
