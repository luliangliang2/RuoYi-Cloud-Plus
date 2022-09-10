import request from "@/utils/request";

/**
 * 导出HTML数据库文档
 * @returns {*}
 */
export function exportHtml() {
  return request({
    url: '/database/doc/exportHtml',
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导出Word数据库文档
 * @returns {*}
 */
export function exportWord() {
  return request({
    url: '/database/doc/exportWord',
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 导出Markdown数据库文档
 * @returns {*}
 */
export function exportMarkdown() {
  return request({
    url: '/database/doc/exportMarkdown',
    method: 'get',
    responseType: 'blob'
  })
}
