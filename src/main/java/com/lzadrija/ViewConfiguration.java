/**
 * Base package, contains classes for initializing this web application.
 */
package com.lzadrija;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * Contains Spring MVC configuration, adds static resource handlers and sets up
 * view resolver to be processed by the container.
 * 
 * @author lzadrija
 * 
 */
@Configuration
@EnableWebMvc
public class ViewConfiguration extends WebMvcConfigurerAdapter {

	private static final String VIEWS_LOCATION = "/WEB-INF/view/", VIEWS_SUFFIX = ".jsp", RESOURCE_HANDLER = "/resources/**",
	        RESOURCE_LOCATION = "resources/";

	/**
	 * Sets up view resolver, adds information such as structure and location
	 * for views used by this web application.
	 * 
	 * @return URL based view resolver which allows direct resolution of
	 *         symbolic view names to URLs
	 */
	@Bean
	public UrlBasedViewResolver setupViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix(VIEWS_LOCATION);
		resolver.setSuffix(VIEWS_SUFFIX);
		resolver.setViewClass(JstlView.class);
		return resolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(RESOURCE_HANDLER).addResourceLocations(RESOURCE_LOCATION);
	}
}
