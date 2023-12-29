package org.dromara.resource.api.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 邮件客户端设置
 *
 * @author Feng
 */
@Data
public class RemoteMailSettings implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * SMTP服务器域名
     */
    @NotBlank(message = "SMTP服务器域名不能为空")
    private String host;

    /**
     * SMTP服务端口
     */
    @NotNull(message = "SMTP服务端口不能为空")
    private Integer port;

    /**
     * 是否需要用户名密码验证
     */
    @NotNull(message = "是否需要用户名密码验证不能为空")
    private Boolean auth;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String user;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String pass;

    /**
     * 发送方，遵循RFC-822标准
     */
    @NotBlank(message = "发送方不能为空")
    private String from;

}
