/**
 * 
 */
package com.lzadrija.inspection;

import java.util.List;

/**
 * @author lzadrija
 * 
 */
public class FileInspectorResponse {

	private final InspectionStatus status;
	private final List<String> fileLines;
	private final String fileName, message;

	/**
	 * 
	 * @param inspectionStatus
	 * @param lines
	 * @param msg
	 */
	public FileInspectorResponse(String name, InspectionStatus inspectionStatus, List<String> lines, String msg) {
		fileName = name;
		status = inspectionStatus;
		fileLines = lines;
		message = msg;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFileStructureCorrect() {
		return InspectionStatus.SUCCESS.equals(status);
	}

	public String getFileName() {
		return fileName;
	}

	public InspectionStatus getStatus() {
		return status;
	}

	public List<String> getFileLines() {
		return fileLines;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "Inspection status: " + status + ",\nFile: " + fileName + ",\nLines: " + fileLines + ",\nMessage:  " + message;
	}

}
