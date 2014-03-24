/**
 * 
 */
package com.lzadrija.persistence.db.model;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * 
	 * @param format
	 * @return
	 */
	public String toFormatedString(String format) {

		StringBuilder text = new StringBuilder();
		for (Entry entry : entries) {
			text.append(entry.format(format)).append(System.lineSeparator());
		}
		return text.toString();
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return String.format("File: [Id = %d Name = %s, Content: %s]", id, name, entries);// validatedContent);
	}
}
