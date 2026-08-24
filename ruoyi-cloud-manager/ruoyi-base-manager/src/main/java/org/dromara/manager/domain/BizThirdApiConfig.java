package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.Date;

/**
 * 第三方API配置对象 biz_third_api_config
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_third_api_config")
public class BizThirdApiConfig extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(value = "config_id")
    private Long configId;

    /**
     * API名称
     */
    private String apiName;

    /**
     * API编码
     */
    private String apiCode;

    /**
     * API分类
     */
    private String apiCategory;

    /**
     * 服务商名称
     */
    private String providerName;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用Key
     */
    private String appKey;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 应用Code
     */
    private String appCode;

    /**
     * 接口地址
     */
    private String endpointUrl;

    /**
     * 认证方式
     */
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
    private String extJson;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

}
