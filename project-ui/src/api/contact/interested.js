import request from '@/utils/request'

// 查询对我感兴趣列表
export function listInterested(query) {
  return request({
    url: '/contact/interested/list',
    method: 'get',
    params: query
  })
}

// 查询对我感兴趣详细
export function getInterested(id) {
  return request({
    url: '/contact/interested/' + id,
    method: 'get'
  })
}

// 新增对我感兴趣
export function addInterested(data) {
  return request({
    url: '/contact/interested',
    method: 'post',
    data: data
  })
}

// 修改对我感兴趣
export function updateInterested(data) {
  return request({
    url: '/contact/interested',
    method: 'put',
    data: data
  })
}

// 删除对我感兴趣
export function delInterested(id) {
  return request({
    url: '/contact/interested/' + id,
    method: 'delete'
  })
}
