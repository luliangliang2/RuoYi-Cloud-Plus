package org.dromara.resource.dubbo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.enums.SmsType;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.resource.api.RemoteSmsService;
import org.dromara.resource.api.domain.RemoteSms;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.provider.enumerate.SupplierType;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信服务
 *
 * @author Feng
 */
@Slf4j
@RequiredArgsConstructor
@Service
@DubboService(timeout = 30000)
public class RemoteSmsServiceImpl implements RemoteSmsService {

    /**
     * 映射 SmsType 到 SupplierType 的关系
     * 使用不可修改的 Map，确保在类加载时初始化
     */
    private static final Map<SmsType, SupplierType> SUPPLIER_TYPE_MAP;

    static {
        SUPPLIER_TYPE_MAP = Map.of(
            SmsType.ALIBABA, SupplierType.ALIBABA,
            SmsType.HUAWEI, SupplierType.HUAWEI,
            SmsType.YUNPIAN, SupplierType.YUNPIAN,
            SmsType.TENCENT, SupplierType.TENCENT,
            SmsType.UNI_SMS, SupplierType.UNI_SMS,
            SmsType.JD_CLOUD, SupplierType.JD_CLOUD,
            SmsType.CLOOPEN, SupplierType.CLOOPEN,
            SmsType.EMAY, SupplierType.EMAY,
            SmsType.CTYUN, SupplierType.CTYUN,
            SmsType.NETEASE, SupplierType.NETEASE);
    }

    /**
     * 根据 SmsType 获取对应的 SupplierType，并创建相应的 SmsBlend 实例
     *
     * @param smsType SmsType 枚举值
     * @return 对应的 SmsBlend 实例
     */
    private SmsBlend getSupplierType(SmsType smsType) {
        SupplierType supplierType = SUPPLIER_TYPE_MAP.get(smsType);
        return SmsFactory.createSmsBlend(supplierType);
    }

    /**
     * 根据给定的 SmsResponse 对象创建并返回一个 RemoteSms 对象，封装短信发送的响应信息
     *
     * @param smsResponse 短信发送的响应信息
     * @return 封装了短信发送结果的 RemoteSms 对象
     */
    private RemoteSms getRemoteSms(SmsResponse smsResponse) {
        // 创建一个 RemoteSms 对象，封装响应信息
        RemoteSms sysSms = new RemoteSms();
        sysSms.setIsSuccess(smsResponse.isSuccess());
        sysSms.setMessage(smsResponse.getMessage());
        sysSms.setResponse(JsonUtils.toJsonString(smsResponse));
        return sysSms;
    }

    /**
     * 同步方法：发送简单文本短信
     *
     * @param phone   目标手机号
     * @param message 短信内容
     * @param smsType 短信供应商类型
     * @return 封装了短信发送结果的 RemoteSms 对象
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    @Override
    public RemoteSms sendMessage(String phone, String message, SmsType smsType) throws ServiceException {
        // 调用 getSupplierType 方法获取对应短信供应商的 SmsBlend 实例
        SmsResponse smsResponse = getSupplierType(smsType).sendMessage(phone, message);
        return getRemoteSms(smsResponse);
    }

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
    @Override
    public RemoteSms sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages, SmsType smsType) throws ServiceException {
        // 调用 getSupplierType 方法获取对应短信供应商的 SmsBlend 实例
        SmsResponse smsResponse = getSupplierType(smsType).sendMessage(phone, templateId, messages);
        return getRemoteSms(smsResponse);
    }

    /**
     * 同步方法：群发简单文本短信
     *
     * @param phones  目标手机号列表
     * @param message 短信内容
     * @param smsType 短信供应商类型
     * @return 封装了短信发送结果的 RemoteSms 对象
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    @Override
    public RemoteSms massTexting(List<String> phones, String message, SmsType smsType) throws ServiceException {
        // 调用 getSupplierType 方法获取对应短信供应商的 SmsBlend 实例
        SmsResponse smsResponse = getSupplierType(smsType).massTexting(phones, message);
        return getRemoteSms(smsResponse);
    }

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
    @Override
    public RemoteSms massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, SmsType smsType) throws ServiceException {
        // 调用 getSupplierType 方法获取对应短信供应商的 SmsBlend 实例
        SmsResponse smsResponse = getSupplierType(smsType).massTexting(phones, templateId, messages);
        return getRemoteSms(smsResponse);
    }

    /**
     * 异步方法：发送简单文本短信
     *
     * @param phone   目标手机号
     * @param message 短信内容
     * @param smsType 短信供应商类型
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    @Override
    public void sendMessageAsync(String phone, String message, SmsType smsType) throws ServiceException {
        getSupplierType(smsType).sendMessageAsync(phone, message);
    }

    /**
     * 异步方法：发送带参数的短信
     *
     * @param phone      目标手机号
     * @param templateId 短信模板ID
     * @param messages   短信模板参数，使用 LinkedHashMap 以保持参数顺序
     * @param smsType    短信供应商类型
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    @Override
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, SmsType smsType) throws ServiceException {
        getSupplierType(smsType).sendMessageAsync(phone, templateId, messages);
    }

    /**
     * 延迟发送简单文本短信
     *
     * @param phone       目标手机号
     * @param message     短信内容
     * @param delayedTime 延迟发送时间（毫秒）
     * @param smsType     短信供应商类型
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    @Override
    public void delayedMessage(String phone, String message, Long delayedTime, SmsType smsType) throws ServiceException {
        getSupplierType(smsType).delayedMessage(phone, message, delayedTime);
    }

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
    @Override
    public void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime, SmsType smsType) throws ServiceException {
        getSupplierType(smsType).delayedMessage(phone, templateId, messages, delayedTime);
    }

    /**
     * 延迟群发简单文本短信
     *
     * @param phones      目标手机号列表
     * @param message     短信内容
     * @param delayedTime 延迟发送时间（毫秒）
     * @param smsType     短信供应商类型
     * @throws ServiceException 发送短信过程中可能抛出的异常
     */
    @Override
    public void delayMassTexting(List<String> phones, String message, Long delayedTime, SmsType smsType) throws ServiceException {
        getSupplierType(smsType).delayMassTexting(phones, message, delayedTime);
    }

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
    @Override
    public void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime, SmsType smsType) throws ServiceException {
        getSupplierType(smsType).delayMassTexting(phones, templateId, messages, delayedTime);
    }

}
