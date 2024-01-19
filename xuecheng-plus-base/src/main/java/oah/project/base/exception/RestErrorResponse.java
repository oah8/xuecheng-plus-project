package oah.project.base.exception;

import java.io.Serializable;

/**
 * @ClassName RestErrorResponse
 * @Description 和前端约定返回的异常信息
 * @Author _oah
 * @Date 2023.11.12 19:02
 * @Version 1.0
 */
public class RestErrorResponse implements Serializable {

    private String errMessage;

    public RestErrorResponse(String errMessage){
        this.errMessage= errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

}
