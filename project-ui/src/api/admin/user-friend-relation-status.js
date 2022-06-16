import request from '@/utils/request'

// 查询关系状态列表
export function listUserFriendRelationStatus(query) {
  return request({
    url: '/admin/user-friend-relation-status/list',
    method: 'get',
    params: query
  })
}

// 查询关系状态详细
export function getUserFriendRelationStatus(id) {
  return request({
    url: '/admin/user-friend-relation-status/' + id,
    method: 'get'
  })
}

// 新增关系状态
export function addUserFriendRelationStatus(data) {
  return request({
    url: '/admin/user-friend-relation-status',
    method: 'post',
    data: data
  })
}

// 修改关系状态
export function updateUserFriendRelationStatus(data) {
  return request({
    url: '/admin/user-friend-relation-status',
    method: 'put',
    data: data
  })
}

// 删除关系状态
export function delUserFriendRelationStatus(id) {
  return request({
    url: '/admin/user-friend-relation-status/' + id,
    method: 'delete'
  })
}
