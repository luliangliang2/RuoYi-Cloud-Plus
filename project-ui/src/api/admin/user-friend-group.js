import request from '@/utils/request'

// 查询好友分组列表
export function listUserFriendGroup(query) {
  return request({
    url: '/admin/user-friend-group/list',
    method: 'get',
    params: query
  })
}

// 查询好友分组详细
export function getUserFriendGroup(id) {
  return request({
    url: '/admin/user-friend-group/' + id,
    method: 'get'
  })
}

// 新增好友分组
export function addUserFriendGroup(data) {
  return request({
    url: '/admin/user-friend-group',
    method: 'post',
    data: data
  })
}

// 修改好友分组
export function updateUserFriendGroup(data) {
  return request({
    url: '/admin/user-friend-group',
    method: 'put',
    data: data
  })
}

// 删除好友分组
export function delUserFriendGroup(id) {
  return request({
    url: '/admin/user-friend-group/' + id,
    method: 'delete'
  })
}
