/**
 * 
 */
package com.lzadrija.persistence.db.model;

import java.text.MessageFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
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
	 * 
	 */
	public Entry() {
	}

	/**
	 * 
	 * @param entryValue
	 * @param valueType
	 */
	public Entry(String entryValue, ValueType valueType, ValidationResult validationResult) {
		value = entryValue;
		type = valueType;
		validation = validationResult;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setType(ValueType type) {
		this.type = type;
	}

	public ValueType getType() {
		return type;
	}

	public void setValidationResult(ValidationResult validationResult) {
		this.validation = validationResult;
	}

	public ValidationResult getValidationResult() {
		return validation;
	}

	/**
	 * 
	 * @param format
	 * @return
	 */
	public String format(String format) {
		return MessageFormat.format(format, type, value, validation);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return String.format("Entry: [Type = %s, Value = %s, Validation: %s]", type, value, validation);
	}
}
