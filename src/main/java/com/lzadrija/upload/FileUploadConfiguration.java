/**
 * Contains classes that handle file upload request.
 */
package com.lzadrija.upload;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Contains Spring MVC configuration that defines a bean which returns the
 * configured {@link org.springframework.web.multipart.MultipartResolver}.
 * 
 * @author lzadrija
 * 
 */
@Configuration
@EnableWebMvc
public class FileUploadConfiguration {

	private static final int MAX_SIZE_UPLOAD_IN_MB = 5 * 1024 * 1024;

	/**
	 * Returns the {@link org.springframework.web.multipart.MultipartResolver}
	 * that defines common configuration properties and parsing functionality
	 * for multipart requests. The returned resolver contains the information
	 * about maximum allowed size (in bytes) before uploads are refused
	 * and maximum allowed size (in bytes) before uploads are written to disk.
	 * 
	 * @return multipart resolver
	 */
	@Bean
	public MultipartResolver multipartResolver() {

		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxUploadSize(MAX_SIZE_UPLOAD_IN_MB);
		commonsMultipartResolver.setMaxInMemorySize(MAX_SIZE_UPLOAD_IN_MB);
		return commonsMultipartResolver;
	}
}
