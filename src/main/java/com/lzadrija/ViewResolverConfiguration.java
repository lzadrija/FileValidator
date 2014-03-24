/**
 * 
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
 * @author lzadrija
 * 
 */
@Configuration
@EnableWebMvc
public class ViewResolverConfiguration extends WebMvcConfigurerAdapter {

	private static final String PAGES_LOCATION = "/WEB-INF/view/", PAGES_SUFFIX = ".jsp", RESOURCE_HANDLER = "/resources/**",
	        RESOURCE_LOCATION = "resources/";

	/**
	 * 
	 * @return
	 */
	@Bean
	public UrlBasedViewResolver setupViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix(PAGES_LOCATION);
		resolver.setSuffix(PAGES_SUFFIX);
		resolver.setViewClass(JstlView.class);
		return resolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(RESOURCE_HANDLER).addResourceLocations(RESOURCE_LOCATION);
	}
}
