package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizThirdApiConfig;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 第三方API配置视图对象 biz_third_api_config
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizThirdApiConfig.class)
public class BizThirdApiConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @ExcelProperty(value = "配置ID")
    private Long configId;

    /**
     * API名称
     */
    @ExcelProperty(value = "API名称")
    private String apiName;

    /**
     * API编码
     */
    @ExcelProperty(value = "API编码")
    private String apiCode;

    /**
     * API分类
     */
    @ExcelProperty(value = "API分类")
    private String apiCategory;

    /**
     * 服务商名称
     */
    @ExcelProperty(value = "服务商名称")
    private String providerName;

    /**
     * 应用ID
     */
    @ExcelProperty(value = "应用ID")
    private String appId;

    /**
     * 应用Key
     */
    @ExcelProperty(value = "应用Key")
    private String appKey;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 应用Code
     */
    @ExcelProperty(value = "应用Code")
    private String appCode;

    /**
     * 接口地址
     */
    @ExcelProperty(value = "接口地址")
    private String endpointUrl;

    /**
     * 认证方式
     */
    @ExcelProperty(value = "认证方式")
    private String authType;

    /**
     * 计费开始时间
     */
    @ExcelProperty(value = "计费开始时间")
    private Date billingStartTime;

    /**
     * 计费到期时间
     */
    @ExcelProperty(value = "计费到期时间")
    private Date billingEndTime;

    /**
     * 扩展参数JSON
     */
    private String extJson;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
