import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getCurrentUser } from '@/api/user'

// 本地存儲鍵名
const USER_INFO_KEY = 'friendsync_user_info'

// 定義用户信息接口
export interface UserInfo {
  id: number
  username: string    // 對應後端的username（用户暱稱）
  nickname: string    // 前端顯示名，可以是username或userAccount
  avatar: string      // 對應後端的avatarUrl
  bio: string         // 對應後端的profile
  email: string
  phone: string
  userAccount?: string // 新增字段，對應後端的userAccount
  gender?: number     // 新增字段，對應後端的gender
  tags?: string       // 用户標籤，JSON字符串格式
  role: string
  createTime: string
}

export const useUserStore = defineStore('user', () => {
  // 從本地存儲讀取用户信息
  const storedUserInfo = localStorage.getItem(USER_INFO_KEY)
  const initialUserInfo = storedUserInfo ? JSON.parse(storedUserInfo) : null
  
  // 用户信息
  const userInfo = ref<UserInfo | null>(initialUserInfo)
  // 登錄狀態
  const isLogin = ref<boolean>(!!initialUserInfo)
  // 用户令牌
  const token = ref<string | null>(localStorage.getItem('token'))

  // 設置用户信息
  function setUserInfo(info: UserInfo | null) {
    userInfo.value = info
    isLogin.value = !!info
    
    // 存儲到本地
    if (info) {
      localStorage.setItem(USER_INFO_KEY, JSON.stringify(info))
    } else {
      localStorage.removeItem(USER_INFO_KEY)
    }
  }

  // 設置令牌
  function setToken(newToken: string | null) {
    token.value = newToken
    if (newToken) {
      localStorage.setItem('token', newToken)
    } else {
      localStorage.removeItem('token')
    }
  }

  // 獲取令牌
  function getToken(): string | null {
    return token.value
  }

  // 獲取當前用户信息
  async function fetchCurrentUser() {
    try {
      console.log('獲取用户信息 - 發送請求')
      const response = await getCurrentUser()
      console.log('獲取用户信息 - 獲取響應:', response)
      
      // 防禦性編程：檢查響應是否有效
      if (!response || typeof response !== 'object') {
        console.error('獲取用户信息 - 響應無效:', response)
        return null
      }
      
      // 從響應中提取用户數據
      let userData = null
      // 處理後端返回的新格式 code=200
      if (response.code === 200 && response.data && typeof response.data === 'object') {
        userData = response.data
      } 
      // 兼容舊格式 code=0
      else if (response.code === 0 && response.data && typeof response.data === 'object') {
        userData = response.data
      } 
      // 直接返回用户對象的情況
      else if (response.id) {
        userData = response
      }
      else {
        console.error('獲取用户信息 - 響應格式不符合預期:', response)
        return null
      }
      
      // 確保用户數據中有ID
      if (!userData || !('id' in userData)) {
        console.error('獲取用户信息 - 用户數據無效')
        return null
      }
      
      // 打印userData查看結構
      console.log('獲取用户信息 - 提取的用户數據:', userData)
      
      // 轉換API返回的字段以匹配UserInfo接口
      const userInfoData: UserInfo = {
        id: userData.id || 0,
        userAccount: userData.userAccount || '',
        username: userData.username || userData.userAccount || '用户',
        nickname: userData.username || userData.userAccount || '用户',
        avatar: userData.avatarUrl || '',
        bio: userData.profile || '',
        email: userData.email || '',
        phone: userData.phone || '',
        gender: userData.gender,
        tags: userData.tags || '',
        role: userData.userRole === 1 ? 'admin' : 'user',
        createTime: userData.createTime || new Date().toISOString()
      }
      
      console.log('獲取用户信息 - 處理後的用户數據:', userInfoData)
      
      // 更新用户信息
      setUserInfo(userInfoData)
      return userInfoData
    } catch (error) {
      // 請求出錯，清除用户信息
      console.error('獲取用户信息 - 發生錯誤:', error)
      return null
    }
  }

  // 退出登錄
  function logout() {
    setUserInfo(null)
    setToken(null)
  }

  // 同步檢查用户是否登錄（供路由守衞使用）
  function checkLoginStatus(): boolean {
    // 先檢查內存中的狀態
    if (isLogin.value) {
      return true
    }
    
    // 如果內存中未登錄，檢查本地存儲
    const userInfoStr = localStorage.getItem(USER_INFO_KEY)
    
    if (userInfoStr) {
      try {
        // 嘗試解析用户信息
        const parsedUserInfo = JSON.parse(userInfoStr)
        // 如果有合法的用户信息，則設置為已登錄狀態
        if (parsedUserInfo && parsedUserInfo.id) {
          userInfo.value = parsedUserInfo
          isLogin.value = true
          return true
        }
      } catch (e) {
        console.error('解析存儲的用户信息失敗', e)
      }
    }
    
    // 所有檢查都失敗，則為未登錄狀態
    return false
  }

  return {
    userInfo,
    isLogin,
    setUserInfo,
    fetchCurrentUser,
    logout,
    checkLoginStatus,
    setToken,
    getToken
  }
}) 