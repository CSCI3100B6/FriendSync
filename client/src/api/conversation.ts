import http from './http'

// 定義會話類型枚舉
export enum ConversationType {
  CHAT = 'CHAT', // 私聊
  TEAM = 'TEAM',  // 團隊聊天
  ROOM = 'ROOM'  // 聊天室
}

// 搜索會話
export function searchConversations(keyword: string) {
  return http.get('/conversation/search', {
    params: { keyword }
  })
}

// 創建團隊聊天室
export function createTeamRoom(name: string, information: string, type: ConversationType = ConversationType.TEAM, license?: string) {
  return http.post('/conversation/create-team-room', {
    name,
    information,
    type,
    license
  })
}

// 創建私聊
export function createPrivateChat(ownerId: number, othersId: number) {
  return http.post('/conversation/create-chat', {
    ownerId,
    othersId
  })
}

// 生成會話邀請碼
export function generateInviteCode(id: number) {
  return http.get('/conversation/generate', {
    params: { id }
  })
}

// 獲取個人創建的會話
export function getOwnConversations(owner?: number) {
  return http.get('/conversation/getown', {
    params: { owner }
  })
}

// 刪除會話
export function deleteConversation(id: number) {
  return http.get('/conversation/delete', {
    params: { id }
  })
}

// 獲取所有會話（管理員）
export function getAllConversations() {
  return http.get('/conversation/all')
}

// 生成對話摘要
export function generateConversationSummary(conversationId: number) {
  return http.get('/conversation/summary', {
    params: { conversationId }
  })
}

// 獲取當前用户已加入的會話
export function getJoinedConversations() {
  return http.get('/user-conversation/joined')
}

// 加入公共聊天室
export function joinPublicRoom(id: number) {
  return http.get('/user-conversation/join-room', {
    params: { id }
  })
}

// 加入加密團隊
export function joinTeamWithLicense(id: number, license: string) {
  return http.get('/user-conversation/join-team', {
    params: { id, license }
  })
}

// 離開會話
export function leaveConversation(id: number, newOwner?: number) {
  return http.get('/user-conversation/leave', {
    params: { 
      id,
      new_owner: newOwner
    }
  })
}

export default {
  searchConversations,
  createTeamRoom,
  createPrivateChat,
  generateInviteCode,
  getOwnConversations,
  deleteConversation,
  getAllConversations,
  generateConversationSummary,
  getJoinedConversations,
  joinPublicRoom,
  joinTeamWithLicense,
  leaveConversation
} 