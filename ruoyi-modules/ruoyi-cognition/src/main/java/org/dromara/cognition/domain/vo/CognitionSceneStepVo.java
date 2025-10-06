package org.dromara.cognition.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import org.dromara.cognition.domain.CognitionSceneStep;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;


/**
 * 认知场景步骤视图对象 cognition_scene_step
 *
 * @author zhang
 * @date 2025-10-02
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CognitionSceneStep.class)
public class CognitionSceneStepVo implements Serializable {

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
     * 步骤序号
     */
    @ExcelProperty(value = "步骤序号")
    private Long stepOrder;

    /**
     * 步骤标题
     */
    @ExcelProperty(value = "步骤标题")
    private String title;

    /**
     * 步骤讲解
     */
    @ExcelProperty(value = "步骤讲解")
    private String description;

    /**
     * 步骤图片
     */
    @ExcelProperty(value = "步骤图片")
    private Long imageId;

    /**
     * 步骤图片Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "imageId")
    private String imageIdUrl;
    /**
     * 步骤视频
     */
    @ExcelProperty(value = "步骤视频")
    private Long videoId;
    /**
     *
     * 步骤视频Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "videoId")
    private String videoUrl;
}
