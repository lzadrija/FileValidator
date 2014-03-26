/**
 * Contains the classes that represent the value holders from the model.
 */
package com.lzadrija.persistence.db.model;

import java.text.MessageFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Value holder for the content of one line from the input file and the result
 * of the validation of the value from this line.
 * 
 * @author lzadrija
 * 
 */
@Embeddable
public class Entry {

	private String value;
	@Enumerated(EnumType.STRING)
	private ValueType type;
	@Enumerated(EnumType.STRING)
	@Column(name = "validation_result")
	private ValidationResult validation;

	/**
	 * Default constructor.
	 */
	public Entry() {
	}

	/**
	 * Constructor.
	 * 
	 * @param entryValue
	 *            value from a file line that was validated
	 * @param valueType
	 *            type of the given value
	 * @param validationResult
	 *            result of the value validation
	 */
	public Entry(String entryValue, ValueType valueType, ValidationResult validationResult) {
		value = entryValue;
		type = valueType;
		validation = validationResult;
	}

	/**
	 * Sets the value of this entry.
	 * 
	 * @param value
	 *            value from the file line
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns the value of this entry.
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the type of the value from this entry.
	 * 
	 * @param type
	 *            value type
	 */
	public void setType(ValueType type) {
		this.type = type;
	}

	/**
	 * Returns the type of the value from this entry.
	 * 
	 * @return value type
	 */
	public ValueType getType() {
		return type;
	}

	/**
	 * Sets the result of the value validation.
	 * 
	 * @param validationResult
	 *            the result of the value validation
	 */
	public void setValidationResult(ValidationResult validationResult) {
		this.validation = validationResult;
	}

	/**
	 * Returns the result of the value validation.
	 * 
	 * @return the result of the entry value validation
	 */
	public ValidationResult getValidationResult() {
		return validation;
	}

	/**
	 * Returns the string representation of this entry that is created using
	 * the given format.
	 * 
	 * @param format
	 *            the format used to create the representetion of this entry
	 * @return formatted entry representation
	 */
	public String getRepresentation(String format) {
		return MessageFormat.format(format, type, value, validation);
	}

	/**
	 * Returns a representation of this entry. The exact details
	 * of the representation are subject to change, but the following may be
	 * regarded as typical:
	 * 
	 * "Entry: [Type = NUMBER, Value = 158184.484, Validation = VALID]"
	 */
	@Override
	public String toString() {
		return String.format("Entry: [Type = %s, Value = %s, Validation = %s]", type, value, validation);
	}
}
