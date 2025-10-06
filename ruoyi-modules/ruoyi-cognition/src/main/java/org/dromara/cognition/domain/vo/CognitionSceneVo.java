package org.dromara.cognition.domain.vo;

import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.cognition.domain.CognitionScene;
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
 * 认知场景视图对象 cognition_scene
 *
 * @author zhang
 * @date 2025-10-02
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = CognitionScene.class)
public class CognitionSceneVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 场景名称
     */
    @ExcelProperty(value = "场景名称")
    private String sceneName;

    /**
     * 场景描述
     */
    @ExcelProperty(value = "场景描述")
    private String description;

    /**
     * 封面图片
     */
    @ExcelProperty(value = "封面图片")
    private Long coverImageId;

    /**
     * 封面图片Url
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL, mapper = "coverImageId")
    private String coverImageIdUrl;

}
