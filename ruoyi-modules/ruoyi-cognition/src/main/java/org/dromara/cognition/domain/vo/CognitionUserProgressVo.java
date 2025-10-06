package org.dromara.cognition.domain.vo;

import org.dromara.cognition.domain.CognitionUserProgress;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 用户场景学习进度视图对象 cognition_user_progress
 *
 * @author zhang
 * @date 2025-10-03
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CognitionUserProgress.class)
public class CognitionUserProgressVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 场景
     */
    @ExcelProperty(value = "场景")
    private Long sceneId;

    /**
     * 当前步骤
     */
    @ExcelProperty(value = "当前步骤")
    private Long stepId;

    /**
     * 用户
     */
    @ExcelProperty(value = "用户")
    private Long userId;

    /**
     * 是否完成
     */
    @ExcelProperty(value = "是否完成", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_yes_no")
    private Long isCompleted;


}
