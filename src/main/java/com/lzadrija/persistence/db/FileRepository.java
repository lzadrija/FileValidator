/**
 * Contains classes for manipulation of the validated data stored in the
 * database.
 */
package com.lzadrija.persistence.db;

import org.springframework.data.repository.CrudRepository;

import com.lzadrija.persistence.db.model.File;

/**
 * Interface for generic CRUD operations on a repository for a
 * file with validated content.
 * 
 * @author lzadrija
 * 
 */
public interface FileRepository extends CrudRepository<File, Integer> {

}
