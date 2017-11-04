package spittr.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 适用于整个Web项目的异常捕获类
 * ControllerAdvice表示该类会异常，并根据ExceptionHandler匹配指定异常
 *
 * @author wq
 * repository：2016-11-29
 *
 */
@ControllerAdvice                     
public class AppWideExceptionHandler {
	
	/**
	 * 触发SpittleNotFoundException异常时的操作	
	 * 会捕获该Controller里的所有的SpittleNotFoundException异常
	 * @return
	 */
	@ExceptionHandler(SpittleNotFoundException.class)
	public String handleSpittleNotFound(){
		return "error";
	}
}
