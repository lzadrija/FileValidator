/**
 * Base package, contains classes for initializing this web application.
 */
package com.lzadrija;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Assists all controllers in the application by defining a global exception
 * handler, which will be invoked when an exception is thrown from this
 * application.
 * 
 * @author lzadrija
 * 
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	public static final String DEFAULT_ERROR_VIEW = "error", EXCEPTION_ATTRIBUTE = "exception", REQ_URL_ATTRIBUTE = "url";

	/**
	 * Defines and returns the default holder for Model and View that is to be
	 * invoked every time an exception is thrown from this application. The
	 * default error view is not invoked for exceptions that are defined in this
	 * application and have defined HTTP status code.
	 * 
	 * @param req
	 *            HTTP servlet request; the exception for which this method is
	 *            called is thrown while processing this request
	 * @param exception
	 *            exception that is thrown
	 * @return holder for Model and View that contains the default error view
	 * @throws Exception
	 *             if this method is invoked for an exception that has defined
	 *             HTTP status code
	 */
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception exception) throws Exception {

		if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
			throw exception;
		}
		logger.error("Application has encountered an unexpected error: ", exception);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(DEFAULT_ERROR_VIEW);
		return modelAndView;
	}
}