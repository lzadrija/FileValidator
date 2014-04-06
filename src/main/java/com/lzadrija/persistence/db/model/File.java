/**
 * Contains the classes that represent the value holders from the model.
 */
package com.lzadrija.persistence.db.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This {@code Entity} contains the content of the file that was validated,
 * along with validation results for each value from the file lines.
 * 
 * @author lzadrija
 * 
 */
@Entity
@Table(name = "files")
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@ElementCollection
	@CollectionTable(name = "validated_entries")
	private List<Entry> entries;

	/**
	 * Sets the id of this file.
	 * Id is also generated automatically.
	 * 
	 * @param id
	 *            id of this file
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Returns file id.
	 * 
	 * @return file id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the name of this file.
	 * 
	 * @param name
	 *            file name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of this file.
	 * 
	 * @return file name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the list of file entries.
	 * 
	 * @param entries
	 *            list of validated lines from file
	 */
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	/**
	 * Returns the list of file entries.
	 * 
	 * @return list of file entries
	 */
	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * Returns the list of formatted entries of this file that are formatted
	 * using the given format.
	 * 
	 * @param format
	 *            the format used to create the representetion of this file
	 * @return list of formatted entries
	 */
	public List<String> getFormattedEntries(String format) {

		List<String> formattedEntries = new ArrayList<>();
		for (Entry entry : entries) {
			formattedEntries.add(entry.getRepresentation(format));
		}
		return formattedEntries;
	}

	/**
	 * Returns a representation of this file. The exact details
	 * of the representation are subject to change, but the following may be
	 * regarded as typical:
	 * 
	 * "File: [Id = 1, Name = FileToValidate.txt, Content = [Entry: [Type =
	 * NUMBER, Value = 158184.484, Validation = VALID],
	 * Entry: [Type = TEXT, Value = textExample, Validation = VALID]]]"
	 */
	@Override
	public String toString() {
		return String.format("File: [Id = %d Name = %s, Content = %s]", id, name, entries);// validatedContent);
	}
}
