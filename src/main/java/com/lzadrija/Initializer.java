/**
 * Base package, contains classes for initializing this web application.
 */
package com.lzadrija;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.lzadrija.persistence.StorageServiceConfiguration;
import com.lzadrija.upload.FileUploadConfiguration;

/**
 * Used for configuring {@link ServletContext}.
 * 
 * @author lzadrija
 * 
 */
public class Initializer implements WebApplicationInitializer {

	private static final String DISPATCHER_SERVLET_NAME = "dispatcher";

	/**
	 * Configures the given {@code ServletContext} with listener and servlet
	 * that are necessary for initializing this web application.
	 * 
	 * @param servletContext
	 *            servlet context to be configured.
	 * @throws ServletException
	 *             if any call against the given {@code ServletContext} throws a
	 *             {@code ServletException}
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(FileContentConfiguration.class, StorageServiceConfiguration.class, FileUploadConfiguration.class, ViewConfiguration.class);
		servletContext.addListener(new ContextLoaderListener(ctx));

		ctx.setServletContext(servletContext);

		Dynamic servlet = servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(ctx));
		servlet.addMapping("/");
		servlet.setLoadOnStartup(1);
	}
}