import request from '@/utils/request'

// 查询工作经历列表
export function listUserWorkExperience(query) {
  return request({
    url: '/admin/user-work-experience/list',
    method: 'get',
    params: query
  })
}

// 查询工作经历详细
export function getUserWorkExperience(id) {
  return request({
    url: '/admin/user-work-experience/' + id,
    method: 'get'
  })
}

// 新增工作经历
export function addUserWorkExperience(data) {
  return request({
    url: '/admin/user-work-experience',
    method: 'post',
    data: data
  })
}

// 修改工作经历
export function updateUserWorkExperience(data) {
  return request({
    url: '/admin/user-work-experience',
    method: 'put',
    data: data
  })
}

// 删除工作经历
export function delUserWorkExperience(id) {
  return request({
    url: '/admin/user-work-experience/' + id,
    method: 'delete'
  })
}
