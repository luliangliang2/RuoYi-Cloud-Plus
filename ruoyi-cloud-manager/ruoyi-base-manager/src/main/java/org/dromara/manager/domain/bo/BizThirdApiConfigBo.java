package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizThirdApiConfig;

import java.util.Date;

/**
 * 第三方API配置业务对象 biz_third_api_config
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizThirdApiConfig.class, reverseConvertGenerate = false)
public class BizThirdApiConfigBo extends BaseEntity {

    /**
     * 配置ID
     */
    @NotNull(message = "配置ID不能为空", groups = { EditGroup.class })
    private Long configId;

    /**
     * API名称
     */
    @NotBlank(message = "API名称不能为空")
    @Size(max = 100, message = "API名称长度不能超过{max}个字符")
    private String apiName;

    /**
     * API编码
     */
    @NotBlank(message = "API编码不能为空")
    @Size(max = 64, message = "API编码长度不能超过{max}个字符")
    private String apiCode;

    /**
     * API分类
     */
    @NotBlank(message = "API分类不能为空")
    @Size(max = 64, message = "API分类长度不能超过{max}个字符")
    private String apiCategory;

    /**
     * 服务商名称
     */
    @Size(max = 100, message = "服务商名称长度不能超过{max}个字符")
    private String providerName;

    /**
     * 应用ID
     */
    @Size(max = 128, message = "应用ID长度不能超过{max}个字符")
    private String appId;

    /**
     * 应用Key
     */
    @Size(max = 128, message = "应用Key长度不能超过{max}个字符")
    private String appKey;

    /**
     * 应用密钥
     */
    @Size(max = 256, message = "应用密钥长度不能超过{max}个字符")
    private String appSecret;

    /**
     * 应用Code
     */
    @Size(max = 128, message = "应用Code长度不能超过{max}个字符")
    private String appCode;

    /**
     * 接口地址
     */
    @Size(max = 500, message = "接口地址长度不能超过{max}个字符")
    private String endpointUrl;

    /**
     * 认证方式
     */
    @Size(max = 32, message = "认证方式长度不能超过{max}个字符")
    private String authType;

    /**
     * 计费开始时间
     */
    private Date billingStartTime;

    /**
     * 计费到期时间
     */
    private Date billingEndTime;

    /**
     * 扩展参数JSON
     */
    @Size(max = 2000, message = "扩展参数JSON长度不能超过{max}个字符")
    private String extJson;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;

}
