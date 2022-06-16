import request from '@/utils/request'

// 查询对我感兴趣列表
export function listUserInterestedToMe(query) {
  return request({
    url: '/admin/user-interested-to-me/list',
    method: 'get',
    params: query
  })
}

// 查询对我感兴趣详细
export function getUserInterestedToMe(id) {
  return request({
    url: '/admin/user-interested-to-me/' + id,
    method: 'get'
  })
}

// 新增对我感兴趣
export function addUserInterestedToMe(data) {
  return request({
    url: '/admin/user-interested-to-me',
    method: 'post',
    data: data
  })
}

// 修改对我感兴趣
export function updateUserInterestedToMe(data) {
  return request({
    url: '/admin/user-interested-to-me',
    method: 'put',
    data: data
  })
}

// 删除对我感兴趣
export function delUserInterestedToMe(id) {
  return request({
    url: '/admin/user-interested-to-me/' + id,
    method: 'delete'
  })
}
