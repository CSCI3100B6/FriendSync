import http from './http'

/**
 * 獲取歷史消息
 * @param conversationId 會話ID
 * @param offset 偏移量
 * @param num 獲取條數
 * @returns 消息列表
 */
export function getHistoryMessages(conversationId: number, offset: number, num: number) {
  return http.post('/message/get', {
    conversationId,
    offset,
    num
  })
}

/**
 * 發送消息
 * @param conversationId 會話ID
 * @param content 消息內容
 * @param userId 發送者ID
 * @returns 發送結果
 */
export function sendMessage(conversationId: number, content: string, userId: number) {
  return http.post('/message/send', {
    conversationId,
    userId,
    content
  })
}

// 刪除消息（管理員）
export function deleteMessage(id: number) {
  return http.get('/message/delete', {
    params: { id }
  })
}

// 獲取所有消息（管理員）
export function getAllMessages() {
  return http.get('/message/all')
}

export default {
  getHistoryMessages,
  sendMessage,
  deleteMessage,
  getAllMessages
} 