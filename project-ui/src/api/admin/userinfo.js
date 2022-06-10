import request from '@/utils/request'

// 查询用户详情列表
export function listUserinfo(query) {
  return request({
    url: '/admin/userinfo/list',
    method: 'get',
    params: query
  })
}

// 查询用户详情详细
export function getUserinfo(id) {
  return request({
    url: '/admin/userinfo/' + id,
    method: 'get'
  })
}

// 新增用户详情
export function addUserinfo(data) {
  return request({
    url: '/admin/userinfo',
    method: 'post',
    data: data
  })
}

// 修改用户详情
export function updateUserinfo(data) {
  return request({
    url: '/admin/userinfo',
    method: 'put',
    data: data
  })
}

// 删除用户详情
export function delUserinfo(id) {
  return request({
    url: '/admin/userinfo/' + id,
    method: 'delete'
  })
}
