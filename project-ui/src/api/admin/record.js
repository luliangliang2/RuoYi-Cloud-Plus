import request from '@/utils/request'

// 查询钱包交易记录列表
export function listRecord(query) {
  return request({
    url: '/admin/record/list',
    method: 'get',
    params: query
  })
}

// 查询钱包交易记录详细
export function getRecord(id) {
  return request({
    url: '/admin/record/' + id,
    method: 'get'
  })
}

// 新增钱包交易记录
export function addRecord(data) {
  return request({
    url: '/admin/record',
    method: 'post',
    data: data
  })
}

// 修改钱包交易记录
export function updateRecord(data) {
  return request({
    url: '/admin/record',
    method: 'put',
    data: data
  })
}

// 删除钱包交易记录
export function delRecord(id) {
  return request({
    url: '/admin/record/' + id,
    method: 'delete'
  })
}
