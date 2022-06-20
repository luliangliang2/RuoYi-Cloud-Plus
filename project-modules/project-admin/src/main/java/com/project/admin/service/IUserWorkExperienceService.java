//package com.project.admin.service;
//
//import com.project.admin.domain.UserWorkExperience;
//import com.project.admin.domain.vo.UserWorkExperienceVo;
//import com.project.admin.domain.bo.UserWorkExperienceBo;
//import com.project.common.mybatis.core.page.PageQuery;
//import com.project.common.mybatis.core.page.TableDataInfo;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * 工作经历Service接口
// *
// * @author huan.li
// * @date 2022-06-16
// */
//public interface IUserWorkExperienceService {
//
//    /**
//     * 查询工作经历
//     *
//     * @param id 工作经历主键
//     * @return 工作经历
//     */
//    UserWorkExperienceVo queryById(Long id);
//
//    /**
//     * 查询工作经历列表
//     *
//     * @param userWorkExperience 工作经历
//     * @return 工作经历集合
//     */
//    TableDataInfo<UserWorkExperienceVo> queryPageList(UserWorkExperienceBo bo, PageQuery pageQuery);
//
//    /**
//     * 查询工作经历列表
//     *
//     * @param userWorkExperience 工作经历
//     * @return 工作经历集合
//     */
//    List<UserWorkExperienceVo> queryList(UserWorkExperienceBo bo);
//
//    /**
//     * 修改工作经历
//     *
//     * @param userWorkExperience 工作经历
//     * @return 结果
//     */
//    Boolean insertByBo(UserWorkExperienceBo bo);
//
//    /**
//     * 修改工作经历
//     *
//     * @param userWorkExperience 工作经历
//     * @return 结果
//     */
//    Boolean updateByBo(UserWorkExperienceBo bo);
//
//    /**
//     * 校验并批量删除工作经历信息
//     *
//     * @param ids 需要删除的工作经历主键集合
//     * @param isValid 是否校验,true-删除前校验,false-不校验
//     * @return 结果
//     */
//    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
//}
