import request from '@/utils/request'

// 查询沟通消息列表
export function listUserCommunicationMessage(query) {
  return request({
    url: '/admin/user-communication-message/list',
    method: 'get',
    params: query
  })
}

// 查询沟通消息详细
export function getUserCommunicationMessage(id) {
  return request({
    url: '/admin/user-communication-message/' + id,
    method: 'get'
  })
}

// 新增沟通消息
export function addUserCommunicationMessage(data) {
  return request({
    url: '/admin/user-communication-message',
    method: 'post',
    data: data
  })
}

// 修改沟通消息
export function updateUserCommunicationMessage(data) {
  return request({
    url: '/admin/user-communication-message',
    method: 'put',
    data: data
  })
}

// 删除沟通消息
export function delUserCommunicationMessage(id) {
  return request({
    url: '/admin/user-communication-message/' + id,
    method: 'delete'
  })
}
