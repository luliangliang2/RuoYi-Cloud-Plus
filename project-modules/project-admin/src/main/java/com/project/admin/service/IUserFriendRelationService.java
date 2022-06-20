//package com.project.admin.service;
//
//import com.project.admin.domain.UserFriendRelation;
//import com.project.admin.domain.vo.UserFriendRelationVo;
//import com.project.admin.domain.bo.UserFriendRelationBo;
//import com.project.common.mybatis.core.page.PageQuery;
//import com.project.common.mybatis.core.page.TableDataInfo;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * 好友关系Service接口
// *
// * @author huan.li
// * @date 2022-06-16
// */
//public interface IUserFriendRelationService {
//
//    /**
//     * 查询好友关系
//     *
//     * @param id 好友关系主键
//     * @return 好友关系
//     */
//    UserFriendRelationVo queryById(Long id);
//
//    /**
//     * 查询好友关系列表
//     *
//     * @param userFriendRelation 好友关系
//     * @return 好友关系集合
//     */
//    TableDataInfo<UserFriendRelationVo> queryPageList(UserFriendRelationBo bo, PageQuery pageQuery);
//
//    /**
//     * 查询好友关系列表
//     *
//     * @param userFriendRelation 好友关系
//     * @return 好友关系集合
//     */
//    List<UserFriendRelationVo> queryList(UserFriendRelationBo bo);
//
//    /**
//     * 修改好友关系
//     *
//     * @param userFriendRelation 好友关系
//     * @return 结果
//     */
//    Boolean insertByBo(UserFriendRelationBo bo);
//
//    /**
//     * 修改好友关系
//     *
//     * @param userFriendRelation 好友关系
//     * @return 结果
//     */
//    Boolean updateByBo(UserFriendRelationBo bo);
//
//    /**
//     * 校验并批量删除好友关系信息
//     *
//     * @param ids 需要删除的好友关系主键集合
//     * @param isValid 是否校验,true-删除前校验,false-不校验
//     * @return 结果
//     */
//    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
//}
