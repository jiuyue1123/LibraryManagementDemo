package org.example.exception;

/**
 * 用户未找到异常
 * 当根据条件查询用户时，未找到对应记录时抛出此异常
 *
 * @author nanak
 */
public class PasswordErrorException extends RuntimeException {

    /**
     * 无参构造方法
     */
    public PasswordErrorException() {
        super("用户或密码错误");
    }

    /**
     * 带错误信息的构造方法
     *
     * @param message 错误信息
     */
    public PasswordErrorException(String message) {
        super(message);
    }

    /**
     * 带错误信息和原因的构造方法
     *
     * @param message 错误信息
     * @param cause   异常原因
     */
    public PasswordErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}