/**
 * Contains classes for file content structure inspection.
 */
package com.lzadrija.inspection;

/**
 * Represents the result of file content structure inspection.
 * 
 * @author lzadrija
 * 
 */
public enum InspectionStatus {

	/**
	 * This status indicates that the file content does not contain lines with
	 * the appropriate structure.
	 */
	FAIL,
	/**
	 * This status indicates that the file content meets the required standard.
	 */
	SUCCESS;
}
