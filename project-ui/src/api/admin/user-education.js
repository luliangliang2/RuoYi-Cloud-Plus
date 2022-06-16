import request from '@/utils/request'

// 查询学历列表
export function listUserEducation(query) {
  return request({
    url: '/admin/user-education/list',
    method: 'get',
    params: query
  })
}

// 查询学历详细
export function getUserEducation(id) {
  return request({
    url: '/admin/user-education/' + id,
    method: 'get'
  })
}

// 新增学历
export function addUserEducation(data) {
  return request({
    url: '/admin/user-education',
    method: 'post',
    data: data
  })
}

// 修改学历
export function updateUserEducation(data) {
  return request({
    url: '/admin/user-education',
    method: 'put',
    data: data
  })
}

// 删除学历
export function delUserEducation(id) {
  return request({
    url: '/admin/user-education/' + id,
    method: 'delete'
  })
}
