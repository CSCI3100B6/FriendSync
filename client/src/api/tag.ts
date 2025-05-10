import http from './http'

// 標籤類型定義
export interface Tag {
  id: number
  tagname: string
  createtime?: string
  updatetime?: string
  isdelete?: number
}

// 獲取所有標籤
export function getAllTags() {
  return http.get('/tag/list')
}

// 更新用户標籤 - 適配新接口
export function updateUserTags(id: number, tags: string[]) {
  return http.post('/user/updateTags', {
    id,
    tags
  })
}

// 導出默認對象
export default {
  getAllTags,
  updateUserTags
} 