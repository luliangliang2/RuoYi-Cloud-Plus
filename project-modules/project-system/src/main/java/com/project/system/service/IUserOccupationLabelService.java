package com.project.system.service;

import com.project.system.domain.UserOccupationLabel;
import com.project.system.domain.vo.UserOccupationLabelVo;
import com.project.system.domain.bo.UserOccupationLabelBo;
import com.project.common.mybatis.core.page.PageQuery;
import com.project.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 人脉职业标签Service接口
 *
 * @author project
 * @date 2022-06-10
 */
public interface IUserOccupationLabelService {

    /**
     * 查询人脉职业标签
     *
     * @param id 人脉职业标签主键
     * @return 人脉职业标签
     */
    UserOccupationLabelVo queryById(Long id);

    /**
     * 查询人脉职业标签列表
     *
     * @param userOccupationLabel 人脉职业标签
     * @return 人脉职业标签集合
     */
    TableDataInfo<UserOccupationLabelVo> queryPageList(UserOccupationLabelBo bo, PageQuery pageQuery);

    /**
     * 查询人脉职业标签列表
     *
     * @param userOccupationLabel 人脉职业标签
     * @return 人脉职业标签集合
     */
    List<UserOccupationLabelVo> queryList(UserOccupationLabelBo bo);

    /**
     * 修改人脉职业标签
     *
     * @param userOccupationLabel 人脉职业标签
     * @return 结果
     */
    Boolean insertByBo(UserOccupationLabelBo bo);

    /**
     * 修改人脉职业标签
     *
     * @param userOccupationLabel 人脉职业标签
     * @return 结果
     */
    Boolean updateByBo(UserOccupationLabelBo bo);

    /**
     * 校验并批量删除人脉职业标签信息
     *
     * @param ids 需要删除的人脉职业标签主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
