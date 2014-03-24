/**
 * 
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
 * @author lzadrija
 * 
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	public static final String DEFAULT_ERROR_VIEW = "error", EXCEPTION_ATTRIBUTE = "exception", REQ_URL_ATTRIBUTE = "url";

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