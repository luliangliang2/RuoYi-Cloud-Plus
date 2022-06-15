package com.project.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.project.common.mybatis.core.mapper.BaseMapperPlus;
import com.project.admin.domain.UserCommunicationMessage;
import com.project.admin.domain.vo.UserCommunicationMessageVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 沟通消息Mapper接口
 *
 * @author huan.li
 * @date 2022-06-15
 */
public interface UserCommunicationMessageMapper extends BaseMapperPlus<UserCommunicationMessageMapper, UserCommunicationMessage, UserCommunicationMessageVo> {
}
