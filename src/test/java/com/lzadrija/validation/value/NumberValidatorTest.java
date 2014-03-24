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
public class NumberValidatorTest {

	private ValueValidator numValidator;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		numValidator = new NumberValidator();
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.NumberValidator#validate(java.lang.String)}
	 */
	@Test
	public void testIntegerNumberEntry() {
		assertEquals(numValidator.validate("1046526536255228"), ValidationResult.VALID);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.NumberValidator#validate(java.lang.String)}
	 */
	@Test
	public void testNegativeDoubleNumberEntry() {
		assertEquals(numValidator.validate("-1046526536.255228"), ValidationResult.VALID);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.NumberValidator#validate(java.lang.String)}
	 */
	@Test
	public void testPositiveDoubleNumberEntry() {
		assertEquals(numValidator.validate("+0.255228"), ValidationResult.VALID);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.NumberValidator#validate(java.lang.String)}
	 */
	@Test
	public void testShortDoubleNumberEntry() {
		assertEquals(numValidator.validate(".255228"), ValidationResult.VALID);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.NumberValidator#validate(java.lang.String)}
	 */
	@Test
	public void testNegShortDoubleNumberEntry() {
		assertEquals(numValidator.validate("-.255228"), ValidationResult.VALID);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.NumberValidator#validate(java.lang.String)}
	 */
	@Test
	public void testRedundantDoubleNumberEntry() {
		assertEquals(numValidator.validate("00000000.255228"), ValidationResult.VALID);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.NumberValidator#validate(java.lang.String)}
	 */
	@Test
	public void tesInvalidDoubleNumberEntry() {
		assertEquals(numValidator.validate("0."), ValidationResult.INVALID);
	}

	/**
	 * Test method for
	 * {@link com.lzadrija.validation.value.NumberValidator#validate(java.lang.String)}
	 */
	@Test
	public void testNonExistentEntry() {
		assertEquals(numValidator.validate(""), ValidationResult.INVALID);
	}

}
