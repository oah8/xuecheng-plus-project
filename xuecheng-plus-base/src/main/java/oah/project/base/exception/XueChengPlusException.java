package oah.project.base.exception;

/**
 * @ClassName XueChengPlusException
 * @Description 本项目自定义异常信息
 * @Author _oah
 * @Date 2023.11.12 19:03
 * @Version 1.0
 */
public class XueChengPlusException extends RuntimeException{


    private String errMessage;

    public XueChengPlusException() {

    }

    public XueChengPlusException(String message) {
        super(message);
        this.errMessage = message;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public static void cast(String message) {
        throw new XueChengPlusException(message);
    }

    public static void cast(CommonError error) {
        throw new XueChengPlusException(error.getErrMessage());
    }


}




