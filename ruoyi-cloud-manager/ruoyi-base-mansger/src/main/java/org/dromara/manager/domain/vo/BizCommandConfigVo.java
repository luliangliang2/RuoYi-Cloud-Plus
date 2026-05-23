package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizCommandConfig;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 指令配置视图对象 biz_command_config
 *
 * @author LionLi
 * @date 2026-05-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizCommandConfig.class)
public class BizCommandConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 指令ID
     */
    @ExcelProperty(value = "指令ID")
    private Long commandId;

    /**
     * 分类树ID
     */
    private Long treeId;

    /**
     * 分类节点ID
     */
    private Long categoryNodeId;

    /**
     * 分类节点名称
     */
    @ExcelProperty(value = "分类")
    private String categoryNodeName;

    /**
     * 指令编码
     */
    @ExcelProperty(value = "指令编码")
    private String commandCode;

    /**
     * 指令名称
     */
    @ExcelProperty(value = "指令名称")
    private String commandName;

    /**
     * 指令类型（single单指令 multiple多指令）
     */
    @ExcelProperty(value = "指令类型")
    private String commandType;

    /**
     * 指令JSON模板
     */
    private String commandTemplate;

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
