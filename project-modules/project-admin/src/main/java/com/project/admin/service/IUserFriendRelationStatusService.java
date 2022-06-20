//package com.project.admin.service;
//
//import com.project.admin.domain.UserFriendRelationStatus;
//import com.project.admin.domain.vo.UserFriendRelationStatusVo;
//import com.project.admin.domain.bo.UserFriendRelationStatusBo;
//import com.project.common.mybatis.core.page.PageQuery;
//import com.project.common.mybatis.core.page.TableDataInfo;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * 关系状态Service接口
// *
// * @author huan.li
// * @date 2022-06-16
// */
//public interface IUserFriendRelationStatusService {
//
//    /**
//     * 查询关系状态
//     *
//     * @param id 关系状态主键
//     * @return 关系状态
//     */
//    UserFriendRelationStatusVo queryById(Long id);
//
//    /**
//     * 查询关系状态列表
//     *
//     * @param userFriendRelationStatus 关系状态
//     * @return 关系状态集合
//     */
//    TableDataInfo<UserFriendRelationStatusVo> queryPageList(UserFriendRelationStatusBo bo, PageQuery pageQuery);
//
//    /**
//     * 查询关系状态列表
//     *
//     * @param userFriendRelationStatus 关系状态
//     * @return 关系状态集合
//     */
//    List<UserFriendRelationStatusVo> queryList(UserFriendRelationStatusBo bo);
//
//    /**
//     * 修改关系状态
//     *
//     * @param userFriendRelationStatus 关系状态
//     * @return 结果
//     */
//    Boolean insertByBo(UserFriendRelationStatusBo bo);
//
//    /**
//     * 修改关系状态
//     *
//     * @param userFriendRelationStatus 关系状态
//     * @return 结果
//     */
//    Boolean updateByBo(UserFriendRelationStatusBo bo);
//
//    /**
//     * 校验并批量删除关系状态信息
//     *
//     * @param ids 需要删除的关系状态主键集合
//     * @param isValid 是否校验,true-删除前校验,false-不校验
//     * @return 结果
//     */
//    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
//}
