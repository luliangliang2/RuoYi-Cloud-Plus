import request from '@/utils/request'

// 查询职业标签列表
export function listUserOccupationLabel(query) {
  return request({
    url: '/admin/user-occupation-label/list',
    method: 'get',
    params: query
  })
}

// 查询职业标签详细
export function getUserOccupationLabel(id) {
  return request({
    url: '/admin/user-occupation-label/' + id,
    method: 'get'
  })
}

// 新增职业标签
export function addUserOccupationLabel(data) {
  return request({
    url: '/admin/user-occupation-label',
    method: 'post',
    data: data
  })
}

// 修改职业标签
export function updateUserOccupationLabel(data) {
  return request({
    url: '/admin/user-occupation-label',
    method: 'put',
    data: data
  })
}

// 删除职业标签
export function delUserOccupationLabel(id) {
  return request({
    url: '/admin/user-occupation-label/' + id,
    method: 'delete'
  })
}
