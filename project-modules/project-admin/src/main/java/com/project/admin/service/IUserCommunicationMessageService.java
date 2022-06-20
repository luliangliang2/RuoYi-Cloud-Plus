//package com.project.admin.service;
//
//import com.project.admin.domain.UserCommunicationMessage;
//import com.project.admin.domain.vo.UserCommunicationMessageVo;
//import com.project.admin.domain.bo.UserCommunicationMessageBo;
//import com.project.common.mybatis.core.page.PageQuery;
//import com.project.common.mybatis.core.page.TableDataInfo;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * 沟通消息Service接口
// *
// * @author huan.li
// * @date 2022-06-16
// */
//public interface IUserCommunicationMessageService {
//
//    /**
//     * 查询沟通消息
//     *
//     * @param id 沟通消息主键
//     * @return 沟通消息
//     */
//    UserCommunicationMessageVo queryById(Long id);
//
//    /**
//     * 查询沟通消息列表
//     *
//     * @param userCommunicationMessage 沟通消息
//     * @return 沟通消息集合
//     */
//    TableDataInfo<UserCommunicationMessageVo> queryPageList(UserCommunicationMessageBo bo, PageQuery pageQuery);
//
//    /**
//     * 查询沟通消息列表
//     *
//     * @param userCommunicationMessage 沟通消息
//     * @return 沟通消息集合
//     */
//    List<UserCommunicationMessageVo> queryList(UserCommunicationMessageBo bo);
//
//    /**
//     * 修改沟通消息
//     *
//     * @param userCommunicationMessage 沟通消息
//     * @return 结果
//     */
//    Boolean insertByBo(UserCommunicationMessageBo bo);
//
//    /**
//     * 修改沟通消息
//     *
//     * @param userCommunicationMessage 沟通消息
//     * @return 结果
//     */
//    Boolean updateByBo(UserCommunicationMessageBo bo);
//
//    /**
//     * 校验并批量删除沟通消息信息
//     *
//     * @param ids 需要删除的沟通消息主键集合
//     * @param isValid 是否校验,true-删除前校验,false-不校验
//     * @return 结果
//     */
//    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
//}
