/**
 * Contains classes for file content structure inspection.
 */
package com.lzadrija.inspection;

import java.util.List;

/**
 * Contains information that the {@link FileInspector} service returns as a
 * result of the inspection of the file content structure.
 * 
 * @author lzadrija
 * 
 */
public class FileInspectorResponse {

	private final InspectionStatus status;
	private final List<String> fileLines;
	private final String fileName, message;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name of the file whose structure was inspected
	 * @param inspectionStatus
	 *            result of the inspection - {@code FAIL} or @ SUCCESS}
	 * @param lines
	 *            content of the file that was inspected
	 * @param msg
	 *            message that describes the result of file content inspection
	 */
	public FileInspectorResponse(String name, InspectionStatus inspectionStatus, List<String> lines, String msg) {
		fileName = name;
		status = inspectionStatus;
		fileLines = lines;
		message = msg;
	}

	/**
	 * Verifies if the file inspection was a success, ie, that the file contains
	 * the appropriate structure.
	 * 
	 * @return true if the file has the appropriate structure, false otherwise
	 */
	public boolean isFileStructureCorrect() {
		return InspectionStatus.SUCCESS.equals(status);
	}

	/**
	 * Returns the name of the file that was inspected.
	 * 
	 * @return the name of the file that was inspected
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns the result of the file content inspection.
	 * 
	 * @return the result of the file content inspection
	 */
	public InspectionStatus getStatus() {
		return status;
	}

	/**
	 * The list of lines of the file that was inspected.
	 * 
	 * @return list of file lines
	 */
	public List<String> getFileLines() {
		return fileLines;
	}

	/**
	 * Returns the informational message with details of the file inspection.
	 * 
	 * @return message that describes the result of file content inspection
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns a brief description of this inspector response. The exact details
	 * of the representation are unspecified and subject to change, but the
	 * following may be regarded as typical: "Inspection status: SUCCESS,\nFile:
	 * FileToInspect.txt,\nLines: [type=TEXT;value=Text example;
	 * type=NUMBER;value=54.564;]\n,Message: File: FileToInspect.txt has
	 * appropriate structure."
	 */
	@Override
	public String toString() {
		return "Inspection status: " + status + ",\nFile: " + fileName + ",\nLines: " + fileLines + ",\nMessage:  " + message;
	}

}
