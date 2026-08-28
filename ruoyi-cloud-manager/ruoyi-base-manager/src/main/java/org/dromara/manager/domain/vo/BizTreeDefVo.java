package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizTreeDef;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 维护树定义视图对象 biz_tree_def
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizTreeDef.class)
public class BizTreeDefVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 树ID
     */
    @ExcelProperty(value = "树ID")
    private Long treeId;

    /**
     * 树编码
     */
    @ExcelProperty(value = "树编码")
    private String treeCode;

    /**
     * 树名称
     */
    @ExcelProperty(value = "树名称")
    private String treeName;

    /**
     * 树类型
     */
    @ExcelProperty(value = "树类型")
    private String treeType;

    /**
     * 选择模式（single单选 multiple多选）
     */
    @ExcelProperty(value = "选择模式")
    private String selectMode;

    /**
     * 使用模块编码
     */
    @ExcelProperty(value = "使用模块编码")
    private String moduleCode;

    /**
     * 根节点模式（1单根 2多根）
     */
    private String rootMode;

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
