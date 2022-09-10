import request from '@/utils/request'

// 查询数据库配置列表
export function listConfig(query) {
  return request({
    url: '/database/config/list',
    method: 'get',
    params: query
  })
}

// 查询数据库配置详细
export function getConfig(dbId) {
  return request({
    url: '/database/config/' + dbId,
    method: 'get'
  })
}

// 新增数据库配置
export function addConfig(data) {
  return request({
    url: '/database/config',
    method: 'post',
    data: data
  })
}

// 修改数据库配置
export function updateConfig(data) {
  return request({
    url: '/database/config',
    method: 'put',
    data: data
  })
}

// 删除数据库配置
export function delConfig(dbId) {
  return request({
    url: '/database/config/' + dbId,
    method: 'delete'
  })
}

// 用户状态修改
export function changeDatabaseConfigStatus(dbId, status) {
  const data = {
    dbId,
    status
  }
  return request({
    url: '/database/config/changeStatus',
    method: 'put',
    data: data
  })
}
// 测试数据库连接
export function testDatabaseConnection(data) {
  return request({
    url: '/database/config/testConnect',
    method: 'post',
    data: data
  })
}
