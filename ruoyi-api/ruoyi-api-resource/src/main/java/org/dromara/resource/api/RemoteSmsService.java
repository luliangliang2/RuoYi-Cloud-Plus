package org.dromara.resource.api;

import org.dromara.common.core.enums.SmsType;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.resource.api.domain.RemoteSms;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 短信服务
 *
 * @author Feng
 */
public interface RemoteSmsService {

    /**
     * 同步方法：发送简单文本短信
     *
     * @param phone   目标手机号
     * @param message 短信内容
     * @param smsType 短信供应商类型
     * @return 封装了短信发送结果的 RemoteSms 对象
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    RemoteSms sendMessage(String phone, String message, SmsType smsType) throws ServiceException;

    /**
     * 同步方法：发送带参数的短信
     *
     * @param phone      目标手机号
     * @param templateId 短信模板ID
     * @param messages   短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @param smsType    短信供应商类型
     * @return 封装了短信发送结果的 RemoteSms 对象
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    RemoteSms sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages, SmsType smsType) throws ServiceException;

    /**
     * 同步方法：群发简单文本短信
     *
     * @param phones  目标手机号列表
     * @param message 短信内容
     * @param smsType 短信供应商类型
     * @return 封装了短信发送结果的 RemoteSms 对象
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    RemoteSms massTexting(List<String> phones, String message, SmsType smsType) throws ServiceException;

    /**
     * 同步方法：群发带参数的短信
     *
     * @param phones     目标手机号列表
     * @param templateId 短信模板ID
     * @param messages   短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @param smsType    短信供应商类型
     * @return 封装了短信发送结果的 RemoteSms 对象
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    RemoteSms massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, SmsType smsType) throws ServiceException;

    /**
     * 异步方法：发送简单文本短信
     *
     * @param phone   目标手机号
     * @param message 短信内容
     * @param smsType 短信供应商类型
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    void sendMessageAsync(String phone, String message, SmsType smsType) throws ServiceException;

    /**
     * 异步方法：发送带参数的短信
     *
     * @param phone      目标手机号
     * @param templateId 短信模板ID
     * @param messages   短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @param smsType    短信供应商类型
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, SmsType smsType) throws ServiceException;

    /**
     * 延迟发送简单文本短信
     *
     * @param phone       目标手机号
     * @param message     短信内容
     * @param delayedTime 延迟发送时间（毫秒）
     * @param smsType     短信供应商类型
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    void delayedMessage(String phone, String message, Long delayedTime, SmsType smsType) throws ServiceException;


    /**
     * 延迟发送带参数的短信
     *
     * @param phone       目标手机号
     * @param templateId  短信模板ID
     * @param messages    短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @param delayedTime 延迟发送时间（毫秒）
     * @param smsType     短信供应商类型
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime, SmsType smsType) throws ServiceException;

    /**
     * 延迟群发简单文本短信
     *
     * @param phones      目标手机号列表
     * @param message     短信内容
     * @param delayedTime 延迟发送时间（毫秒）
     * @param smsType     短信供应商类型
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    void delayMassTexting(List<String> phones, String message, Long delayedTime, SmsType smsType) throws ServiceException;

    /**
     * 延迟批量发送带参数的短信
     *
     * @param phones      目标手机号列表
     * @param templateId  短信模板ID
     * @param messages    短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @param delayedTime 延迟发送时间（毫秒）
     * @param smsType     短信供应商类型，用于确定使用哪个短信供应商发送短信
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime, SmsType smsType) throws ServiceException;
}
