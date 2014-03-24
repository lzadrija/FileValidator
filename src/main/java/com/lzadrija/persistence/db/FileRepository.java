/**
 * 
 */
package com.lzadrija.persistence.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lzadrija.persistence.db.model.File;

/**
 * @author lzadrija
 * 
 */
public interface FileRepository extends JpaRepository<File, Integer> {

}
