package spittr.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 用户更新信息未找到的异常
 * 将异常重命名为404
 *
 * @author wq
 * repository：2016-11-29
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND,   
		reason="用户更新信息未找到")
public class SpittleNotFoundException extends RuntimeException{

}
