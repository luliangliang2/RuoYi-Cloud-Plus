import request from '@/utils/request'

// 查询钱包变动日志列表
export function listLog(query) {
  return request({
    url: '/admin/log/list',
    method: 'get',
    params: query
  })
}

// 查询钱包变动日志详细
export function getLog(id) {
  return request({
    url: '/admin/log/' + id,
    method: 'get'
  })
}

// 新增钱包变动日志
export function addLog(data) {
  return request({
    url: '/admin/log',
    method: 'post',
    data: data
  })
}

// 修改钱包变动日志
export function updateLog(data) {
  return request({
    url: '/admin/log',
    method: 'put',
    data: data
  })
}

// 删除钱包变动日志
export function delLog(id) {
  return request({
    url: '/admin/log/' + id,
    method: 'delete'
  })
}
