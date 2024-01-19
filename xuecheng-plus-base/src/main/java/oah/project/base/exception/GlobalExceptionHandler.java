package oah.project.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GlobalException
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.12 19:11
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice
//@RestControllerAdvice
public class GlobalExceptionHandler {


    // 对项目的自定义异常XueChengPlusException类型进行处理，此异常是程序员主动抛出的，可预知异常
    @ResponseBody   // 将信息返回为json格式
    @ExceptionHandler(XueChengPlusException.class) // 此方法捕获XueChengPlusException异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 状态码返回500
    public RestErrorResponse customException(XueChengPlusException e) {
        // 记录异常
        log.error("系统异常{}", e.getErrMessage(), e);

        // 解析出异常信息
        String errMessage = e.getErrMessage();

        // 写异常处理的逻辑
        // ...

        RestErrorResponse restErrorResponse = new RestErrorResponse(errMessage);
        return restErrorResponse;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        // 记录异常
        log.error("系统异常{}", e.getMessage(), e);
        if(e.getMessage().equals("不允许访问")) {
            return new RestErrorResponse("您没有权限操作此功能");
        }

        // 解析出异常信息
        RestErrorResponse restErrorResponse = new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
        return restErrorResponse;
    }


    // MethodArgumentNotValidException

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        // 存储错误信息
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().stream().forEach(item-> {
            errors.add(item.getDefaultMessage());
        });

        // 将list中的错误信息拼接起来
        String errMessage = StringUtils.join(errors, ",");

        // 记录异常
        log.error("系统异常{}", e.getMessage(), errMessage);

        // 解析出异常信息
        RestErrorResponse restErrorResponse = new RestErrorResponse(errMessage);
        return restErrorResponse;
    }

}















