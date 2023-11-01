package org.dromara.auth.service;


import org.dromara.auth.domain.model.AuthParams;
import org.dromara.auth.domain.vo.LoginVo;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.system.api.domain.vo.RemoteClientVo;

/**
 * 授权策略
 *
 * @author Michelle.Chung
 */
public interface IAuthStrategy<T extends AuthParams> {

    String BASE_NAME = "AuthStrategy";

    /**
     * 登录
     */
    static < T extends AuthParams> LoginVo login(String grantType, T authParams, RemoteClientVo client) {

        String beanName = grantType + BASE_NAME;
        if (!SpringUtils.containsBean(beanName)) {
            throw new ServiceException("授权类型不正确!");
        }
        IAuthStrategy<T> instance = SpringUtils.getBean(beanName);
        instance.validate(authParams);
        return instance.login(authParams, client);
    }

    /**
     * 参数校验
     */
    default void validate(T authParams){}

    /**
     * 登录
     */
    LoginVo login(T authParams, RemoteClientVo client);

}