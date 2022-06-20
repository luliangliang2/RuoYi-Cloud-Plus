//package com.project.admin.service;
//
//import com.project.admin.domain.UserEducation;
//import com.project.admin.domain.vo.UserEducationVo;
//import com.project.admin.domain.bo.UserEducationBo;
//import com.project.common.mybatis.core.page.PageQuery;
//import com.project.common.mybatis.core.page.TableDataInfo;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * 学历Service接口
// *
// * @author huan.li
// * @date 2022-06-16
// */
//public interface IUserEducationService {
//
//    /**
//     * 查询学历
//     *
//     * @param id 学历主键
//     * @return 学历
//     */
//    UserEducationVo queryById(Long id);
//
//    /**
//     * 查询学历列表
//     *
//     * @param userEducation 学历
//     * @return 学历集合
//     */
//    TableDataInfo<UserEducationVo> queryPageList(UserEducationBo bo, PageQuery pageQuery);
//
//    /**
//     * 查询学历列表
//     *
//     * @param userEducation 学历
//     * @return 学历集合
//     */
//    List<UserEducationVo> queryList(UserEducationBo bo);
//
//    /**
//     * 修改学历
//     *
//     * @param userEducation 学历
//     * @return 结果
//     */
//    Boolean insertByBo(UserEducationBo bo);
//
//    /**
//     * 修改学历
//     *
//     * @param userEducation 学历
//     * @return 结果
//     */
//    Boolean updateByBo(UserEducationBo bo);
//
//    /**
//     * 校验并批量删除学历信息
//     *
//     * @param ids 需要删除的学历主键集合
//     * @param isValid 是否校验,true-删除前校验,false-不校验
//     * @return 结果
//     */
//    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
//}
