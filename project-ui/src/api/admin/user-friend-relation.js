import request from '@/utils/request'

// 查询好友关系列表
export function listUserFriendRelation(query) {
  return request({
    url: '/admin/user-friend-relation/list',
    method: 'get',
    params: query
  })
}

// 查询好友关系详细
export function getUserFriendRelation(id) {
  return request({
    url: '/admin/user-friend-relation/' + id,
    method: 'get'
  })
}

// 新增好友关系
export function addUserFriendRelation(data) {
  return request({
    url: '/admin/user-friend-relation',
    method: 'post',
    data: data
  })
}

// 修改好友关系
export function updateUserFriendRelation(data) {
  return request({
    url: '/admin/user-friend-relation',
    method: 'put',
    data: data
  })
}

// 删除好友关系
export function delUserFriendRelation(id) {
  return request({
    url: '/admin/user-friend-relation/' + id,
    method: 'delete'
  })
}
