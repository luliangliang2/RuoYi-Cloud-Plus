package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizCameraBo;
import org.dromara.manager.domain.vo.BizCameraVo;

import java.util.Collection;
import java.util.List;

/**
 * 上装相机Service接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface IBizCameraService {

    /**
     * 查询上装相机
     *
     * @param cameraId 主键
     * @return 上装相机
     */
    BizCameraVo queryById(Long cameraId);

    /**
     * 分页查询上装相机列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 上装相机分页列表
     */
    TableDataInfo<BizCameraVo> queryPageList(BizCameraBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的上装相机列表
     *
     * @param bo 查询条件
     * @return 上装相机列表
     */
    List<BizCameraVo> queryList(BizCameraBo bo);

    /**
     * 查询可绑定相机列表
     *
     * @param vehicleId 车辆ID
     * @param keyword 关键字
     * @return 上装相机列表
     */
    List<BizCameraVo> queryBindableList(Long vehicleId, String keyword);

    /**
     * 新增上装相机
     *
     * @param bo 上装相机
     * @return 是否新增成功
     */
    Boolean insertByBo(BizCameraBo bo);

    /**
     * 修改上装相机
     *
     * @param bo 上装相机
     * @return 是否修改成功
     */
    Boolean updateByBo(BizCameraBo bo);

    /**
     * 校验并批量删除上装相机信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
