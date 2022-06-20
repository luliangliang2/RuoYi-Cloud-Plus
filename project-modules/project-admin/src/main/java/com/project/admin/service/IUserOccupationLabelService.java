//package com.project.admin.service;
//
//import com.project.admin.domain.UserOccupationLabel;
//import com.project.admin.domain.vo.UserOccupationLabelVo;
//import com.project.admin.domain.bo.UserOccupationLabelBo;
//import com.project.common.mybatis.core.page.PageQuery;
//import com.project.common.mybatis.core.page.TableDataInfo;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * 职业标签Service接口
// *
// * @author huan.li
// * @date 2022-06-16
// */
//public interface IUserOccupationLabelService {
//
//    /**
//     * 查询职业标签
//     *
//     * @param id 职业标签主键
//     * @return 职业标签
//     */
//    UserOccupationLabelVo queryById(Long id);
//
//    /**
//     * 查询职业标签列表
//     *
//     * @param userOccupationLabel 职业标签
//     * @return 职业标签集合
//     */
//    TableDataInfo<UserOccupationLabelVo> queryPageList(UserOccupationLabelBo bo, PageQuery pageQuery);
//
//    /**
//     * 查询职业标签列表
//     *
//     * @param userOccupationLabel 职业标签
//     * @return 职业标签集合
//     */
//    List<UserOccupationLabelVo> queryList(UserOccupationLabelBo bo);
//
//    /**
//     * 修改职业标签
//     *
//     * @param userOccupationLabel 职业标签
//     * @return 结果
//     */
//    Boolean insertByBo(UserOccupationLabelBo bo);
//
//    /**
//     * 修改职业标签
//     *
//     * @param userOccupationLabel 职业标签
//     * @return 结果
//     */
//    Boolean updateByBo(UserOccupationLabelBo bo);
//
//    /**
//     * 校验并批量删除职业标签信息
//     *
//     * @param ids 需要删除的职业标签主键集合
//     * @param isValid 是否校验,true-删除前校验,false-不校验
//     * @return 结果
//     */
//    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
//}
